package pl.hellothere.client.network;

import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.containers.socket.connection.Info;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.tools.*;

import java.io.IOException;
import java.net.Socket;

public class ServerClient {
    private static final String address = "localhost";
    private static final int port = 8374;

    final Socket connection;

    private final Receiver receiver;
    private final Sender sender;
    private final Encryptor encryptor = new Encryptor();

    UserData user = null;
/*
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
*/
    public ServerClient() throws ConnectionError {
        try {
            connection = new Socket(address, port);

            sender = new Sender(connection.getOutputStream());
            receiver = new Receiver(connection.getInputStream());

            sender.send(new SecurityData(encryptor.getPublicKey()));
            encryptor.setReceiverKey(receiver.<SecurityData>read().getKey());
        } catch (IOException | CommunicationException e) {
            throw new ConnectionError(e);
        }
    }

    public UserData signIn(String login, String password) throws ConnectionError {
        if(user != null)
            throw new UserAlreadyLoggedException();

        try {
            sender.send(new AuthorizationRequest(login, encryptor.encrypt(password)));
            AuthorizationResult ar = receiver.read();

            if (ar.success())
                return (user = ar.getUserData());
            else if(ar.isServerError())
                throw new ServerClientException();
            return null;
        } catch (CommunicationException e) {
            throw new ServerClientException(e);
        }
    }

    public void close() {
        try {
            sender.send(Info.CloseConnection);
            connection.close();
        } catch (Exception e) {}
    }

    public static class ServerClientException extends ConnectionError {
        public ServerClientException() {
            super();
        }

        public ServerClientException(Throwable cause) {
            super(cause);
        }
    }
    public static class UserAlreadyLoggedException extends ServerClientException {}
}