package pl.hellothere.server;

import pl.hellothere.server.database.DatabaseClient;

import java.net.ServerSocket;

public class Server {
    private static final String db_address = "jdbc:postgresql://localhost:5432/hellothere";
    private static final String db_login = "hellothere";
    private static final String db_password = "password";
    private static final int port = 8374;

    public static void main(String[] args) {
        try (DatabaseClient db = new DatabaseClient(db_address, db_login, db_password)) {
            System.out.println("Connection with database set up successfully");

            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println("Server socket created");

                while(true) {
                    try {
                        ClientHandler client = new ClientHandler(db, server.accept());
                        client.start();
                    } catch (Exception e) {}
                }
            } catch (Exception e) {
                System.out.println("Unable to setup server socket on this port");
                e.printStackTrace();
            }
        } catch(Exception e) {
            System.out.println("Unable to connect to database");
            e.printStackTrace();
        }
    }
}
