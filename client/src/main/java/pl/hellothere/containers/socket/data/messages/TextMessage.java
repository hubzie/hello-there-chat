package pl.hellothere.containers.socket.data.messages;

public class TextMessage extends Message {
    protected TextMessage() { super(); }

    @Override
    public MessageType getType() { return MessageType.Text; }
}
