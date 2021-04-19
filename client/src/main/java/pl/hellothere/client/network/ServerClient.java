package pl.hellothere.client.network;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.messages.Message;
import pl.hellothere.containers.messages.TextMessage;
import pl.hellothere.containers.socket.Info;
import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClient {
    private static final String address = "localhost";
    private static final int port = 8374;
    final Socket connection;
    final ObjectOutputStream c_out;
    final ObjectInputStream c_in;
    int user_id = -1;

    public ServerClient() {
        try {
            connection = new Socket(address, port);
            c_out = new ObjectOutputStream(connection.getOutputStream());
            c_out.flush();
            c_in = new ObjectInputStream(connection.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean signIn(String login, String password) throws ConnectionLost {
        if (user_id != -1)
            throw new RuntimeException();

        try {
            c_out.writeObject(new AuthorizationRequest(login, password));
            c_out.flush();

            AuthorizationResult response = (AuthorizationResult) c_in.readObject();
            if (response.success())
                user_id = response.getID();
            return response.success();
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            throw new ConnectionLost(e);
        }
    }

    public Message nextMessage() throws ConnectionLost, NoMoreMessages {
        try {
            c_out.writeObject(Info.NextMessage);
            c_out.flush();

            SocketPackage pkg = (SocketPackage) c_in.readObject();

            if(pkg instanceof Info) throw new NoMoreMessages();

            Message msg = (Message) pkg;

            if(msg instanceof TextMessage) return (TextMessage) msg;
            else throw new ClassNotFoundException();
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            throw new ConnectionLost(e);
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
    public static class NoMoreMessages extends Exception {
        public NoMoreMessages() {
            super();
        }

        public NoMoreMessages(String message) {
            super(message);
        }

        public NoMoreMessages(String message, Throwable cause) {
            super(message, cause);
        }

        public NoMoreMessages(Throwable cause) {
            super(cause);
        }}
}