package pl.hellothere.client.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerClient {
    private static final String address = "localhost";
    private static final int port = 8374;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Login: ");
        String login = in.nextLine();
        System.out.print("Password: ");
        String password = in.nextLine();

        try (Socket c = new Socket(address, port)) {
            ObjectOutputStream c_out = new ObjectOutputStream(c.getOutputStream());
            c_out.flush();
            ObjectInputStream c_in = new ObjectInputStream(c.getInputStream());

            c_out.writeObject(login);
            c_out.writeObject(password);
            c_out.flush();

            String res = (String) c_in.readObject();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
