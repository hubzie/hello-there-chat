package pl.hellothere.client.control;

import javafx.application.Application;
import pl.hellothere.client.network.ServerClient;
import pl.hellothere.client.view.controller.ClientViewController;

public class Client {
    final ServerClient connection;

    public Client() {
        try {
            connection = new ServerClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        ClientViewController.setCredentialsTest(connection::signIn);
        Application.launch(ClientViewController.class);
    }

    public static void main(String[] args) {
        try {
            Client c = new Client();
            c.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
