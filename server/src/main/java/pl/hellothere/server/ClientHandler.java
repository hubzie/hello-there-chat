package pl.hellothere.server;

import pl.hellothere.message.*;
import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.server.database.DatabaseClient.*;

import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {
    private final DatabaseClient db;
    private final Socket client;
    private final ObjectInputStream c_in;
    private final ObjectOutputStream c_out;

    private int user_id = -1;

    public ClientHandler(DatabaseClient db, Socket client) throws IOException {
        this.db = db;
        this.client = client;

        c_out = new ObjectOutputStream(client.getOutputStream());
        c_out.flush();
        c_in = new ObjectInputStream(client.getInputStream());
    }

    void authenticate(AuthorizationRequest msg) {
        AuthorizationResult.Code res;

        try {
            user_id = db.authenticate(msg.getLogin(), msg.getPassword());
            res = (user_id != -1 ? AuthorizationResult.Code.OK : AuthorizationResult.Code.ERROR);
        } catch (DatabaseException e) {
            res = AuthorizationResult.Code.SERVER_ERROR;
        }

        try {
            c_out.writeObject(new AuthorizationResult(res, user_id));
            c_out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (client.isConnected()) {
            try {
                Message msg = (Message) c_in.readObject();

                if (msg instanceof AuthorizationRequest) authenticate((AuthorizationRequest) msg);
                else throw new ClassNotFoundException();
            }  catch (EOFException e) {
                try {
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (ClassNotFoundException | ClassCastException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}