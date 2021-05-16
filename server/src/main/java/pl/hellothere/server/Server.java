package pl.hellothere.server;

import pl.hellothere.server.activator.AccountActivator;
import pl.hellothere.server.client.ClientHandler;
import pl.hellothere.server.database.DatabaseClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

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
                        Socket socket = server.accept();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String request = reader.readLine();

                        if(request.equals("startMainApp")) {
                            ClientHandler client = new ClientHandler(db, socket);
                            client.start();
                        } else {
                            AccountActivator client = new AccountActivator(db, socket, request);
                            client.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
