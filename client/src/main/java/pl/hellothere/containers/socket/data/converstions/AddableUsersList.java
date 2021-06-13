package pl.hellothere.containers.socket.data.converstions;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.data.UserData;

import java.util.List;

public class AddableUsersList implements SocketPackage {
    protected AddableUsersList() {}

    List<UserData> list;

    public AddableUsersList(List<UserData> list) { this.list = list; }

    public List<UserData> getList() { return list; }
}
