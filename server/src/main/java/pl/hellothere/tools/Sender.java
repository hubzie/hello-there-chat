package pl.hellothere.tools;

import pl.hellothere.containers.SocketPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Sender {
    ObjectOutputStream out;

    public Sender(OutputStream out) throws CommunicationException {
        try {
            this.out = new ObjectOutputStream(out);
            this.out.flush();
        } catch (IOException e){
            throw new CommunicationException("Initialize sender error", e);
        }
    }

    public void send(SocketPackage pkg) throws CommunicationException {
        try {
            out.writeObject(pkg);
            out.flush();
        } catch (IOException e) {
            throw new CommunicationException("Send package error", e);
        }
    }
}