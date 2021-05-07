package pl.hellothere.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Receiver {
    ObjectInputStream in;

    public Receiver(InputStream in) throws IOException {
        this.in = new ObjectInputStream(in);
    }

    public Object read() throws IOException, ClassNotFoundException {
        return in.readObject();
    }
}