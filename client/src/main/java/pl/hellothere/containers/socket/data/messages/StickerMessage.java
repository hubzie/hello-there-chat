package pl.hellothere.containers.socket.data.messages;

public class StickerMessage extends Message {
    public static final String TYPE = "S";

    protected StickerMessage() { super(); }

    @Override
    public String getType() { return TYPE; }
}
