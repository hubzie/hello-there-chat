package pl.hellothere.server.client;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.authorization.*;
import pl.hellothere.containers.socket.connection.commands.Command;
import pl.hellothere.containers.socket.connection.requests.*;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.AddableUsersList;
import pl.hellothere.containers.socket.data.converstions.ConversationList;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.messages.MessageList;
import pl.hellothere.containers.socket.data.notifications.ErrorNotification;
import pl.hellothere.containers.socket.data.notifications.MessageNotification;
import pl.hellothere.containers.socket.data.notifications.Notification;
import pl.hellothere.containers.socket.data.notifications.StopNotification;
import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.server.database.exceptions.DatabaseAuthenticationException;
import pl.hellothere.server.database.exceptions.DatabaseException;
import pl.hellothere.server.database.exceptions.DatabaseUpdateException;
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
    private final BlockingQueue<Notification> notifications = new LinkedBlockingQueue<>();

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

    public void sendUpdate(Notification msg) {
        notifications.add(msg);
    }

    void register(RegistrationRequest req) {
        RegistrationResult res;
        try {
            res = new RegistrationResult(db.register(req.getName(), req.getLogin(), req.getEmail(), encryptor.decrypt(req.getPassword())));
        } catch (Exception e) {
            res = new RegistrationResult(RegistrationResult.Code.SERVER_ERROR);
        }

        communicator.send(res);
    }

    boolean authenticate() throws ConnectionError {
        try {
            SocketPackage pkg = communicator.read();

            if (pkg.equals(Command.CloseConnection)) {
                close();
                return false;
            } else if (pkg instanceof RegistrationRequest) {
                register((RegistrationRequest) pkg);
                return false;
            } else if (pkg instanceof AuthorizationRequest) {
                AuthorizationRequest ar = (AuthorizationRequest) pkg;

                AuthorizationResult.Code result;
                try {
                    user = db.authenticate(ar.getLogin(), encryptor.decrypt(ar.getPassword()));
                    result = AuthorizationResult.Code.OK;
                } catch (DatabaseAuthenticationException e) {
                    result = AuthorizationResult.Code.ERROR;
                } catch (DatabaseException e) {
                    result = AuthorizationResult.Code.SERVER_ERROR;
                    communicator.send(new AuthorizationResult(result, user));
                    throw e;
                }

                communicator.send(new AuthorizationResult(result, user));
                return (user != null);
            } else
                throw new ClassNotFoundException(pkg.getClass().toString());
        } catch (ClassNotFoundException | CommunicationException | DatabaseException e) {
            throw new ConnectionError(e);
        }
    }

    boolean closed = false;

    public boolean isClosed() {
        return closed || connection.isClosed();
    }

    void close() {
        try {
            logOut();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                communicator.send(new StopNotification());
                communicator.join();
                connection.close();
            } catch (IOException | CommunicationException e) {
                e.printStackTrace();
            }

            closed = true;
        }
    }

    public boolean isLogged() {
        return !isClosed() && user != null;
    }

    void logOut() {
        if(user != null)
            db.getUserListener().unlisten(this, user.getID());
        db.getConversationListener().unlisten(this, conv_id);

        conv_id = -1;
        user = null;
    }

    void manageMembers(ManageMembersRequest req) throws DatabaseException {
        switch (req.getType()) {
            case ADD: db.addMember(conv_id, req.getId()); break;
            case REMOVE: db.removeMember(conv_id, req.getId()); break;
        }
    }

    void modifyConversation(ModifyConversationRequest req) throws DatabaseException {
        switch (req.getType()) {
            case CREATE: db.addMember(db.createConversation(), user.getID()); break;
            case DELETE: db.deleteConversation(req.getID(), user.getID()); break;
            case RENAME: db.renameConversation(req.getID(), user.getID(), req.getName()); break;
        }
    }

    void modifyUser(ModifyUserRequest req) {
        ModifyUserResult res;
        try {
            res = new ModifyUserResult(db.modifyUser(req.getID(), req.getName(), req.getLogin(), encryptor.decrypt(req.getPassword())));
        } catch (Exception e) {
            res = new ModifyUserResult(ModifyUserResult.Code.SERVER_ERROR);
        }

        communicator.send(res);
    }

    void handleRequest(Request req) throws CommunicationException, ClassNotFoundException, DatabaseException {
        if (req instanceof ConversationListRequest)
            communicator.send(new ConversationList(db.getConversationList(user.getID(), ((ConversationListRequest) req).getCount())));
        else if (req instanceof ChangeConversationRequest) {
            db.getConversationListener().listen(this, conv_id, ((ChangeConversationRequest) req).getConversationID());
            communicator.send(db.getConversationDetails(conv_id = ((ChangeConversationRequest) req).getConversationID(), user.getID()));
        } else if (req instanceof GetMessagesRequest)
            communicator.send(new MessageList(db.getMessages(conv_id, ((GetMessagesRequest) req).getTime())));
        else if (req instanceof SendMessageRequest)
            db.sendMessage(((SendMessageRequest) req).getContent(), conv_id, user.getID());
        else if (req instanceof ManageMembersRequest)
            manageMembers((ManageMembersRequest) req);
        else if (req instanceof ModifyConversationRequest)
            modifyConversation((ModifyConversationRequest) req);
        else if (req instanceof ModifyUserRequest)
            modifyUser((ModifyUserRequest) req);
        else if (req instanceof AddableUserListRequest)
            communicator.send(new AddableUsersList(db.getAddableUserList(conv_id, ((AddableUserListRequest) req).getPrefix())));
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
                    Notification msg = notifications.poll(1000, TimeUnit.MILLISECONDS);
                    if (msg != null)
                        communicator.send(msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        notifier.start();

        db.getUserListener().listen(this, -1, user.getID());
        try {
            while (isLogged()) {
                SocketPackage pkg = communicator.read();
                try {
                    if (pkg instanceof Request) handleRequest((Request) pkg);
                    else if (pkg instanceof Command) handleCommand((Command) pkg);
                    else throw new ClassNotFoundException();
                } catch (DatabaseUpdateException e) {
                    communicator.send(new ErrorNotification(e.getMessage()));
                }
            }

            notifier.join();
        } catch (InterruptedException | IOException | ClassNotFoundException | CommunicationException | DatabaseException e) {
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