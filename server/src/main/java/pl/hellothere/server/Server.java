package pl.hellothere.server;

import pl.hellothere.server.activator.AccountActivator;
import pl.hellothere.server.client.ClientHandler;
import pl.hellothere.server.database.DatabaseClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;

public class Server {
    public static void main(String[] args) throws Exception {
        String db_address, db_login, db_password;
        int port;

        try (InputStream file = new FileInputStream("src/main/resources/server.properties")) {
            Properties config = new Properties();
            config.load(file);

            db_address = config.getProperty("db.address");
            db_login = config.getProperty("db.login");
            db_password = config.getProperty("db.password");
            port = Integer.parseInt(config.getProperty("server.port"));
        }

        try (DatabaseClient db = new DatabaseClient(db_address, db_login, db_password)) {
            System.out.println("Connection with database set up successfully");
            db.addMember(5,1);
            db.addMember(5,1);
/*
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println("Server socket created");

                while(true) {
                    try {
                        Socket socket = server.accept();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String request = reader.readLine();

                        if (request == null)
                            continue;

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
            }*/
        }
    }
}
