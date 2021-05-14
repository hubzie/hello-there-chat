package pl.hellothere.server;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.containers.socket.connection.commands.Command;
import pl.hellothere.containers.socket.connection.requests.*;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.ConversationList;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.messages.MessageList;
import pl.hellothere.containers.socket.data.notifications.MessageNotification;
import pl.hellothere.containers.socket.data.notifications.StopNotification;
import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.tools.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends Thread {
    private final DatabaseClient db;
    private final Socket connection;

    private final ServerCommunicator communicator;
    private final Encryptor encryptor = new Encryptor();
    private final BlockingQueue<Message> notifications = new LinkedBlockingQueue<>();

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

    public void sendUpdate(Message msg) {
        notifications.add(msg);
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

    boolean closed = false;

    public boolean isClosed() {
        return closed || connection.isClosed();
    }

    public void close() {
        try {
            logOut();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            closed = true;
        }
    }

    public boolean isLogged() {
        return !isClosed() && user != null;
    }

    public void logOut() throws CommunicationException {
        db.getListenerManager().unlisten(this, conv_id);

        conv_id = -1;
        user = null;
    }

    void handleRequest(Request req) throws CommunicationException, ClassNotFoundException, DatabaseClient.DatabaseException {
        if (req instanceof ConversationListRequest)
            communicator.send(new ConversationList(db.getConversationList(user.getID())));
        else if (req instanceof ChangeConversationRequest) {
            db.getListenerManager().listen(this, conv_id, ((ChangeConversationRequest) req).getConversationID());
            communicator.send(db.getConversationDetails(conv_id = ((ChangeConversationRequest) req).getConversationID()));
        } else if (req instanceof GetMessagesRequest)
            communicator.send(new MessageList(db.getMessages(conv_id, ((GetMessagesRequest) req).getTime())));
        else if (req instanceof SendMessageRequest)
            db.sendMessage(((SendMessageRequest) req).getContent(), user.getID(), conv_id);
        else throw new ClassNotFoundException();
    }

    void handleCommand(Command cmd) throws IOException, ClassNotFoundException, CommunicationException {
        switch (cmd.getStatus()) {
            case CLOSE_CONNECTION: close(); break;
            case LOG_OUT: logOut(); break;
            default: throw new ClassNotFoundException();
        }
    }

    void startApp() throws ConnectionError {
        Thread notifier = new Thread(() -> {
            try {
                while (isLogged()) {
                    Message msg = notifications.poll(1000, TimeUnit.MILLISECONDS);
                    if (msg != null)
                        communicator.send(new MessageNotification(msg));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        notifier.start();
        try {
            while (isLogged()) {
                SocketPackage pkg = communicator.read();
                if(pkg instanceof Request) handleRequest((Request) pkg);
                else if(pkg instanceof Command) handleCommand((Command) pkg);
                else throw new ClassNotFoundException();
            }

            notifier.join();
        } catch (InterruptedException | IOException | ClassNotFoundException | CommunicationException | DatabaseClient.DatabaseException e) {
            throw new ConnectionError(e);
        }
    }

    @Override
    public void run() {
        try {
            while(!isClosed())
                if(authenticate())
                    startApp();
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
    }
}