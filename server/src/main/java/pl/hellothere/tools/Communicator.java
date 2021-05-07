package pl.hellothere.tools;

import pl.hellothere.containers.SocketPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Communicator {
    final Sender sender;
    final Receiver receiver;

    public Communicator(Socket s) throws IOException, CommunicationException {
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
