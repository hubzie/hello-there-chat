package pl.hellothere.server;

import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.server.database.DatabaseClient.*;

import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {
    private final DatabaseClient db;
    private final Socket client;
    private final ObjectInputStream c_in;
    private final ObjectOutputStream c_out;

    private int user_id;

    public ClientHandler(DatabaseClient db, Socket client) throws IOException, ClientAuthenticationException {
        System.out.println("Initialize connection");

        this.db = db;
        this.client = client;

        c_out = new ObjectOutputStream(client.getOutputStream());
        c_out.flush();
        c_in = new ObjectInputStream(client.getInputStream());

        System.out.println("Try to log in");
        authenticate();
    }

    void authenticate() throws ClientAuthenticationException {
        try {
            System.out.println("Read login");
            String login = (String) c_in.readObject();
            System.out.println("Read password");
            String password = (String) c_in.readObject();

            user_id = db.authenticate(login, password);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            sendMessage("Server unavailable");
            throw new ClientAuthenticationException();
        } catch (DatabaseException e) {
            System.out.println(e);
            sendMessage("Unknown user");
            throw new ClientAuthenticationException();
        }
    }

    void sendMessage(String text) {
        try {
            c_out.writeObject(text);
            c_out.flush();
        } catch (IOException e) {
            System.out.println("Unable to send message to user");
        }
    }

    @Override
    public void run() {
        System.out.println("New client connected: " + user_id);
        sendMessage("Hello user!");
    }
}

class ClientHandlerException extends Exception {}
class ClientAuthenticationException extends ClientHandlerException {}