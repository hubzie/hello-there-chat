package pl.hellothere.containers.socket.data;

import pl.hellothere.containers.SocketPackage;

public class UserData implements SocketPackage {
    int id;
    String name;

    private UserData() {}

    public UserData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}