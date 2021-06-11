package pl.hellothere.containers.socket.data.messages;

public class TextMessage extends Message {
    public static final String TYPE = "T";

    protected TextMessage() { super(); }

    @Override
    public String getType() { return TYPE; }
}
