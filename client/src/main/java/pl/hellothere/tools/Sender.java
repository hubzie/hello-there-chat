package pl.hellothere.tools;

import pl.hellothere.containers.SocketPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Sender {
    ObjectOutputStream out;

    public Sender(OutputStream out) throws IOException {
        this.out = new ObjectOutputStream(out);
        this.out.flush();
    }

    public void send(SocketPackage pkg) throws IOException {
        out.writeObject(pkg);
        out.flush();
    }
}