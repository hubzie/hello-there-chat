package pl.hellothere.containers.socket.data.messages;

public class StickerMessage extends Message {
    protected StickerMessage() { super(); }

    @Override
    public MessageType getType() { return MessageType.Sticker; }
}
