package pl.hellothere.server.activator;

import pl.hellothere.server.database.DatabaseClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AccountActivator extends Thread {
    private final DatabaseClient db;
    private final Socket client;
    private final String request;
    private String protocol = "HTTP/1.1";

    public AccountActivator(DatabaseClient db, Socket client, String request) {
        this.db = db;
        this.client = client;
        this.request = request;
    }

    void sendResult(String code, String result) throws IOException {
        PrintWriter writer = new PrintWriter(client.getOutputStream());
        writer.println(protocol+" "+code);
        writer.println("Content-Type: text/html");
        writer.println("\r\n");
        writer.println("<p>"+result+"</p>");
        writer.flush();
    }

    boolean parse(String request) throws DatabaseClient.DatabaseException {
        String[] data = request.split("[ ?&]");

        if (data.length != 4 || !data[0].equals("GET") || !data[1].equals("/activate.html") || !data[2].startsWith("token="))
            return false;

        protocol = data[3];
        return db.activateAccount(data[2].substring("token=".length()));
    }

    @Override
    public void start() {
        try {
            if(!parse(request))
                sendResult("404 ERROR", "Not found");
            else
                sendResult("200 OK", "Account activated successfully");
        } catch (IOException | DatabaseClient.DatabaseException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
