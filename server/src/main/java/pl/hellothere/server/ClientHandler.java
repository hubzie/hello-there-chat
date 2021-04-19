package pl.hellothere.server;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.messages.TextMessage;
import pl.hellothere.containers.socket.Info;
import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.server.database.DatabaseClient.*;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

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

    List<TextMessage> messages = null;
    Iterator<TextMessage> it;
    void handleMessageInfo(Info msg) throws IOException {
        if(messages == null) {
            try {
                messages = db.getMessages();
            } catch (DatabaseException e) {
                c_out.writeObject(Info.ServerError);
                c_out.flush();
                return;
            }

            it = messages.iterator();
        }

        if(!it.hasNext())
            c_out.writeObject(Info.NoMoreMessages);
        else
            c_out.writeObject(it.next());

        c_out.flush();
    }

    @Override
    public void run() {
        while (!client.isClosed()) {
            try {
                SocketPackage msg = (SocketPackage) c_in.readObject();

                if (msg instanceof AuthorizationRequest) authenticate((AuthorizationRequest) msg);
                else if (msg instanceof Info) handleMessageInfo((Info) msg);
                else throw new ClassNotFoundException();
            } catch (EOFException e) {
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