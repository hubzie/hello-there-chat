package pl.hellothere.server;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.data.Conversation;
import pl.hellothere.containers.data.UserData;
import pl.hellothere.containers.messages.Message;
import pl.hellothere.containers.socket.Info;
import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.server.database.DatabaseClient.*;
import pl.hellothere.tools.Receiver;
import pl.hellothere.tools.Sender;

import java.io.*;
import java.net.Socket;
import java.util.List;

class ClientHandler extends Thread {
    private final DatabaseClient db;
    private final Socket client;
    private final Receiver receiver;
    private final Sender sender;

    private int conv_id = -1;
    private UserData user = null;

    public ClientHandler(DatabaseClient db, Socket client) throws IOException {
        this.db = db;
        this.client = client;

        sender = new Sender(client.getOutputStream());
        receiver = new Receiver(client.getInputStream());
    }

    void send(SocketPackage pkg) throws ConnectionLost {
        try {
            sender.send(pkg);
        } catch (IOException e) {
            throw new ConnectionLost(e);
        }
    }

    Object receive() throws ConnectionLost {
        try {
            return receiver.read();
        } catch (IOException | ClassNotFoundException e) {
            throw new ConnectionLost(e);
        }
    }

    void authenticate(AuthorizationRequest msg) throws ConnectionLost {
        AuthorizationResult.Code res;

        try {
            user = db.authenticate(msg.getLogin(), msg.getPassword());
            res = (user != null ? AuthorizationResult.Code.OK : AuthorizationResult.Code.ERROR);
        } catch (DatabaseException e) {
            res = AuthorizationResult.Code.SERVER_ERROR;
        }

        try {
            send(new AuthorizationResult(res, user));
        } catch (Exception e) {
            throw new ConnectionLost(e);
        }
    }

    void sendConversationList() throws ConnectionLost {
        try {
            List<Conversation> list = db.getConversationList(user.getID());
            for(Conversation c : list)
                send(c);
            send(Info.NoMoreConversation);
        } catch (DatabaseException e) {
            throw new DatabaseError(e);
        }
    }

    void chooseConversation(int conv_id) throws ConnectionLost {
        try {
            send(db.getConversationDetails(conv_id));
            this.conv_id = conv_id;
        } catch (InvalidData e) {
            send(Info.ConversationNotFound);
        } catch (DatabaseException e) {
            throw new DatabaseError(e);
        }
    }

    void getMessages() throws ConnectionLost {
        try {
            List<Message> list = db.getMessages(conv_id);
            for(Message c : list)
                send(c);
            send(Info.NoMoreMessages);
        } catch (DatabaseException e) {
            throw new DatabaseError(e);
        }
    }

    void handleInfo(Info msg) throws ConnectionLost {
        switch (msg.getStatus()) {
            case CONVERSATION_LIST: sendConversationList(); break;
            case CHOOSE_CONVERSATION: chooseConversation(msg.getData()); break;
            case GET_MESSAGES: getMessages(); break;
            default: throw new ConnectionError();
        }
    }

    @Override
    public void run() {
        try {
            while (!client.isClosed()) {
                try {
                    SocketPackage msg = (SocketPackage) receive();

                    if (msg instanceof AuthorizationRequest) authenticate((AuthorizationRequest) msg);
                    else if (msg instanceof Info) handleInfo((Info) msg);
                    else throw new ConnectionError();
                } catch (ConnectionLost e) {
                    try {
                        client.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        } catch (ConnectionError e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ConnectionLost extends Exception {
        public ConnectionLost() {
            super();
        }

        public ConnectionLost(String message) {
            super(message);
        }

        public ConnectionLost(String message, Throwable cause) {
            super(message, cause);
        }

        public ConnectionLost(Throwable cause) {
            super(cause);
        }}
    public static class ConnectionError extends RuntimeException {
        public ConnectionError() {
            super();
        }

        public ConnectionError(String message) {
            super(message);
        }

        public ConnectionError(String message, Throwable cause) {
            super(message, cause);
        }

        public ConnectionError(Throwable cause) {
            super(cause);
        }}
    public static class DatabaseError extends RuntimeException {
        public DatabaseError() {
            super();
        }

        public DatabaseError(String message) {
            super(message);
        }

        public DatabaseError(String message, Throwable cause) {
            super(message, cause);
        }

        public DatabaseError(Throwable cause) {
            super(cause);
        }}
}