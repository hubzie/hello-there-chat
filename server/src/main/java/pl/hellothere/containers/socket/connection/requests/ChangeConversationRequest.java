package pl.hellothere.containers.socket.connection.requests;

public class ChangeConversationRequest extends Request {
    int id;

    protected ChangeConversationRequest() {}

    public ChangeConversationRequest(int id) {
        this.id = id;
    }

    public int getConversationID() {
        return id;
    }
}
