package pl.hellothere.client.network;

import pl.hellothere.message.AuthorizationRequest;
import pl.hellothere.message.AuthorizationResult;

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

    public boolean signIn(String login, String password) {
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
            throw new RuntimeException(e);
        }
    }

    public boolean isLoggedIn() {
        return user_id != -1;
    }
}
