package pl.hellothere.tools;

import pl.hellothere.containers.SocketPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Receiver {
    ObjectInputStream in;

    public Receiver(InputStream in) throws CommunicationException {
        try {
            this.in = new ObjectInputStream(in);
        } catch (IOException e) {
            throw new CommunicationException("Initialize receiver error", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends SocketPackage> T read() throws CommunicationException {
        try {
            return (T) in.readObject();
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            throw new CommunicationException("Receive package error", e);
        }
    }
}