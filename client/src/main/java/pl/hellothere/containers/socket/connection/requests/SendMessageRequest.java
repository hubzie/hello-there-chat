package pl.hellothere.containers.socket.connection.requests;

import pl.hellothere.containers.socket.data.messages.Message;

public class SendMessageRequest extends Request {
    Message content;

    protected SendMessageRequest() {
        super();
    }

    public SendMessageRequest(Message msg) {
        content = msg;
    }

    public Message getContent() {
        return content;
    }
}
