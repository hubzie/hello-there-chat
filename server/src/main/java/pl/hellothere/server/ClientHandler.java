package pl.hellothere.server;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.containers.socket.connection.commands.Command;
import pl.hellothere.containers.socket.connection.requests.ChangeConversationRequest;
import pl.hellothere.containers.socket.connection.requests.ConversationListRequest;
import pl.hellothere.containers.socket.connection.requests.GetMessagesRequest;
import pl.hellothere.containers.socket.connection.requests.Request;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.ConversationList;
import pl.hellothere.containers.socket.data.messages.MessageList;
import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.tools.*;

import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {
    private final DatabaseClient db;
    private final Socket connection;

    private final ServerCommunicator communicator;
    private final Encryptor encryptor = new Encryptor();

    int conv_id = -1;
    UserData user = null;

    public ClientHandler(DatabaseClient db, Socket connection) throws ConnectionError {
        try {
            this.db = db;
            this.connection = connection;

            communicator = new ServerCommunicator(connection);

            communicator.send(new SecurityData(encryptor.getPublicKey()));
            encryptor.setReceiverKey(communicator.<SecurityData>read().getKey());
        } catch (IOException | CommunicationException e) {
            throw new ConnectionError(e);
        }
    }

    public boolean authenticate() throws ConnectionError {
        try {
            SocketPackage pkg = communicator.read();

            if (pkg.equals(Command.CloseConnection)) {
                connection.close();
                return false;
            } else if (pkg instanceof AuthorizationRequest) {
                AuthorizationRequest ar = (AuthorizationRequest) pkg;

                AuthorizationResult.Code result;
                try {
                    user = db.authenticate(ar.getLogin(), encryptor.decrypt(ar.getPassword()));
                    result = AuthorizationResult.Code.OK;
                } catch (DatabaseClient.DatabaseAuthenticationException e) {
                    result = AuthorizationResult.Code.ERROR;
                } catch (DatabaseClient.DatabaseException e) {
                    result = AuthorizationResult.Code.SERVER_ERROR;
                    communicator.send(new AuthorizationResult(result, user));
                    throw e;
                }

                communicator.send(new AuthorizationResult(result, user));
                return (user != null);
            } else
                throw new ClassNotFoundException(pkg.getClass().toString());
        } catch (ClassNotFoundException | IOException | CommunicationException | DatabaseClient.DatabaseException e) {
            throw new ConnectionError(e);
        }
    }

    public boolean isLogged() {
        return (user != null);
    }

    public void logOut() {
        conv_id = -1;
        user = null;
    }

    void handleRequest(Request req) throws CommunicationException, ClassNotFoundException, DatabaseClient.DatabaseException {
        if (req instanceof ConversationListRequest)
            communicator.send(new ConversationList(db.getConversationList(user.getID())));
        else if (req instanceof ChangeConversationRequest)
            communicator.send(db.getConversationDetails(conv_id = ((ChangeConversationRequest) req).getConversationID()));
        else if (req instanceof GetMessagesRequest)
            communicator.send(new MessageList(db.getMessages(conv_id)));
        else throw new ClassNotFoundException();
    }

    void handleCommand(Command cmd) throws IOException, ClassNotFoundException {
        switch (cmd.getStatus()) {
            case CLOSE_CONNECTION: connection.close();
            case LOG_OUT: logOut(); break;
            default: throw new ClassNotFoundException();
        }
    }

    void startApp() throws ConnectionError {
        try {
            while(isLogged()) {
                SocketPackage pkg = communicator.read();
                if(pkg instanceof Request) handleRequest((Request) pkg);
                else if(pkg instanceof Command) handleCommand((Command) pkg);
                else throw new ClassNotFoundException();
            }
        } catch (IOException | ClassNotFoundException | CommunicationException | DatabaseClient.DatabaseException e) {
            throw new ConnectionError(e);
        }
    }

    @Override
    public void run() {
        try {
            while(!connection.isClosed())
                if(authenticate())
                    startApp();
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
    }
}