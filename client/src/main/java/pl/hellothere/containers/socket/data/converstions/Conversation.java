package pl.hellothere.containers.socket.data.converstions;

import pl.hellothere.containers.SocketPackage;

public class Conversation implements SocketPackage {
    int id;
    String name;

    protected Conversation() {}

    public Conversation(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isValid() {
        return id != -1;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Conversation) && ((Conversation) o).getID() == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
