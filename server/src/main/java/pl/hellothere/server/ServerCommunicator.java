package pl.hellothere.server;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.Receiver;
import pl.hellothere.tools.Sender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerCommunicator {
    final Sender sender;
    final Receiver receiver;

    public ServerCommunicator(Socket s) throws IOException, CommunicationException {
        sender = new Sender(new ObjectOutputStream(s.getOutputStream()));
        receiver = new Receiver(new ObjectInputStream(s.getInputStream()));
    }

    public void send(SocketPackage pkg) throws CommunicationException {
        sender.send(pkg);
    }

    public <T extends SocketPackage> T read() throws CommunicationException {
        return receiver.read();
    }
}
