package pl.hellothere.containers.socket.data.messages;

import pl.hellothere.containers.SocketPackage;

import java.util.Date;

public abstract class Message implements SocketPackage {
    public enum MessageType {
        TextMessage
    }

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

    public abstract String getType();

    static Message getMessage(String type) throws MessageTypeNotFoundException {
        switch (type) {
            case TextMessage.TYPE: return new TextMessage();
            case StickerMessage.TYPE: return new StickerMessage();
            default: throw new MessageTypeNotFoundException();
        }
    }

    public static Message createMessage(String content, String type) throws MessageTypeNotFoundException {
        Message msg = getMessage(type);
        msg.content = content;
        return msg;
    }

    public static Message createMessage(int senderId, Date sendTime, String content, String type) throws MessageTypeNotFoundException {
        Message msg = getMessage(type);
        msg.senderId = senderId;
        msg.sendTime = sendTime;
        msg.content = content;
        return msg;
    }
}
