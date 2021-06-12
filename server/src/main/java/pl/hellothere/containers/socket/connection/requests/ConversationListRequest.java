package pl.hellothere.containers.socket.connection.requests;

public class ConversationListRequest extends Request {
    int count;

    protected ConversationListRequest() {}

    public ConversationListRequest(int count) { this.count = count; }

    public int getCount() { return count; }
}
