package pl.hellothere.client.control;

import pl.hellothere.client.network.ServerClient;

import java.util.Scanner;

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
        Scanner in = new Scanner(System.in);
        while (!connection.isLoggedIn()) {
            System.out.print("Login: ");
            String login = in.nextLine();

            System.out.print("Password: ");
            String password = in.nextLine();

            if(connection.signIn(login, password))
                System.out.println("Welcome!");
            else
                System.out.println("Wrong login or password. Try again.");
        }
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
