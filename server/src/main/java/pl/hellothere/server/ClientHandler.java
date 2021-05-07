package pl.hellothere.server;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.containers.socket.connection.Info;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.server.database.DatabaseClient;
import pl.hellothere.tools.*;

import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {
    private final DatabaseClient db;
    private final Socket connection;

    private final Receiver receiver;
    private final Sender sender;
    private final Encryptor encryptor = new Encryptor();

    UserData user = null;

    public ClientHandler(DatabaseClient db, Socket connection) throws ConnectionError {
        try {
            this.db = db;
            this.connection = connection;

            sender = new Sender(connection.getOutputStream());
            receiver = new Receiver(connection.getInputStream());

            sender.send(new SecurityData(encryptor.getPublicKey()));
            encryptor.setReceiverKey(receiver.<SecurityData>read().getKey());
        } catch (IOException | CommunicationException e) {
            throw new ConnectionError(e);
        }
    }

    public boolean authenticate() throws ConnectionError {
        try {
            SocketPackage pkg = receiver.read();

            if (pkg.equals(Info.CloseConnection)) {
                connection.close();
                return false;
            } else if (pkg instanceof AuthorizationRequest) {
                AuthorizationRequest ar = (AuthorizationRequest) pkg;

                AuthorizationResult.Code result;
                try {
                    user = db.authenticate(ar.getLogin(), encryptor.decrypt(ar.getPassword()));
                    result = AuthorizationResult.Code.OK;
                } catch (DatabaseClient.DatabaseInitializationException e) {
                    result = AuthorizationResult.Code.ERROR;
                } catch (DatabaseClient.DatabaseException e) {
                    result = AuthorizationResult.Code.SERVER_ERROR;
                    sender.send(new AuthorizationResult(result, user));
                    throw e;
                }

                sender.send(new AuthorizationResult(result, user));
                return (user != null);
            } else
                throw new ClassNotFoundException(pkg.getClass().toString());
        } catch (ClassNotFoundException | IOException | CommunicationException | DatabaseClient.DatabaseException e) {
            throw new ConnectionError(e);
        }
    }

    void startApp() throws ConnectionError {
        try {
            connection.close();
        } catch (IOException e) {
            throw new ConnectionError(e);
        }
    }

    @Override
    public void run() {
        try {
            while(!connection.isClosed())
                if(authenticate())
                    startApp();
        } catch (ConnectionError e) {
            e.printStackTrace();
        }
    }
}