package pl.hellothere.containers.socket.data.messages;

import pl.hellothere.containers.SocketPackage;

import java.util.Date;

public abstract class Message implements SocketPackage {
    int senderId = -1;
    Date sendTime;
    String content;

    protected Message() {}

    public void fill(int senderId, Date sendTime) {
        this.senderId = senderId;
        this.sendTime = sendTime;
    }

    public int getSenderID() {
        return senderId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }

    public abstract MessageType getType();

    static Message getMessage(MessageType type) throws MessageTypeNotFoundException {
        switch (type) {
            case Text: return new TextMessage();
            case Sticker: return new StickerMessage();
            default: throw new MessageTypeNotFoundException();
        }
    }

    public static Message createMessage(String content, MessageType type) throws MessageTypeNotFoundException {
        Message msg = getMessage(type);
        msg.content = content;
        return msg;
    }

    public static Message createMessage(int senderId, Date sendTime, String content, MessageType type) throws MessageTypeNotFoundException {
        Message msg = getMessage(type);
        msg.senderId = senderId;
        msg.sendTime = sendTime;
        msg.content = content;
        return msg;
    }
}
