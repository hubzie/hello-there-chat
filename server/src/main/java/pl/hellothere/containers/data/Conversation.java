package pl.hellothere.containers.data;

import pl.hellothere.containers.SocketPackage;

public class Conversation implements SocketPackage {
    int conv_id;
    String name;

    public Conversation(int conv_id, String name) {
        this.conv_id = conv_id;
        this.name = name;
    }

    public int getID() {
        return conv_id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "("+conv_id+") "+name;
    }
}
