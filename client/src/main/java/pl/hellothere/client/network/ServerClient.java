package pl.hellothere.client.network;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.data.*;
import pl.hellothere.containers.messages.Message;
import pl.hellothere.containers.socket.Info;
import pl.hellothere.containers.socket.authorization.*;
import pl.hellothere.tools.Receiver;
import pl.hellothere.tools.Sender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerClient {
    private static final String address = "localhost";
    private static final int port = 8374;
    final Socket connection;
    private final Receiver receiver;
    private final Sender sender;
    UserData user = null;

    public ServerClient() {
        try {
            connection = new Socket(address, port);

            sender = new Sender(connection.getOutputStream());
            receiver = new Receiver(connection.getInputStream());
        } catch (Exception e) {
            throw new ConnectionError(e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public boolean signIn(String login, String password) throws ConnectionLost {
        if (user != null)
            throw new RuntimeException();

        try {
            send(new AuthorizationRequest(login, password));
            AuthorizationResult response = (AuthorizationResult) receive();

            if (response.success())
                user = response.getUserData();
            return response.success();
        } catch (ClassCastException e) {
            throw new ConnectionError(e);
        }
    }

    public List<Conversation> getConversationsList() throws ConnectionLost {
        try {
            send(Info.GetConversationList);

            List<Conversation> list = new ArrayList<>();
            while (true) {
                Object o = receive();
                if(o instanceof Info)
                    break;
                list.add((Conversation) o);
            }

            return list;
        } catch (ClassCastException e) {
            throw new ConnectionError(e);
        }
    }

    public ConversationDetails chooseConversation(int id) throws ConnectionLost {
        send(Info.chooseConversation(id));
        Object o = receive();

        if(o instanceof ConversationDetails)
            return (ConversationDetails) o;
        throw new ConnectionError();
    }

    public List<Message> getMessages() throws ConnectionLost {
        try {
            send(Info.GetMessages);

            List<Message> list = new ArrayList<>();
            while (true) {
                Object o = receive();
                if(o instanceof Info)
                    break;
                list.add((Message) o);
            }

            return list;
        } catch (ClassCastException e) {
            throw new ConnectionError(e);
        }
    }

    public UserData getUser() {
        return user;
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
        }
    }
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
        }
    }
}