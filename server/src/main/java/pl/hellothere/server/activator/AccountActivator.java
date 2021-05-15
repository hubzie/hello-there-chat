package pl.hellothere.server.activator;

import pl.hellothere.server.database.DatabaseClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

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

    boolean parse(String request) {
        String[] data = request.split("[ ?&]");
        if (!data[0].equals("GET") || !data[1].equals("/activate.html"))
            return false;

        String token = null;
        protocol = data[data.length-1];
        for(int i=2;i<data.length-2;i++)
            if(data[i].startsWith("token=")) {
                if(token != null)
                    return false;
                token = data[i].substring("token=".length());
            }

        if(token == null)
            return false;

        return true;
        //        return db.activateAccount(token);
    }

    @Override
    public void start() {
        try {
            if(!parse(request))
                sendResult("404 ERROR", "Not found");
            else
                sendResult("200 OK", "Hello world!");
        } catch (IOException e) {
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
