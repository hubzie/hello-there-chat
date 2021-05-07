package pl.hellothere.containers.socket.data.converstions;

import pl.hellothere.containers.SocketPackage;

import java.util.AbstractList;
import java.util.List;

public class ConversationList extends AbstractList<Conversation> implements SocketPackage {
    List<Conversation> list;

    protected ConversationList() {}

    public ConversationList(List<Conversation> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Conversation get(int index) {
        return list.get(index);
    }
}
