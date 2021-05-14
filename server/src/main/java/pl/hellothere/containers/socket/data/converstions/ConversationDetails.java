package pl.hellothere.containers.socket.data.converstions;

import pl.hellothere.containers.socket.data.UserData;

import java.util.List;

public class ConversationDetails extends Conversation {
    List<UserData> users;

    protected ConversationDetails() {
        super();
    }

    public ConversationDetails(int id, String name, List<UserData> users) {
        super(id, name);
        this.users = users;
    }

    public List<UserData> getUsers() {
        return users;
    }
}
