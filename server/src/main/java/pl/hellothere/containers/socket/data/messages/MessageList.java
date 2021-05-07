package pl.hellothere.containers.socket.data.messages;

import pl.hellothere.containers.SocketPackage;

import java.util.AbstractList;
import java.util.List;

public class MessageList extends AbstractList<Message> implements SocketPackage {
    List<Message> list;

    protected MessageList() {}

    public MessageList(List<Message> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Message get(int index) {
        return list.get(index);
    }
}