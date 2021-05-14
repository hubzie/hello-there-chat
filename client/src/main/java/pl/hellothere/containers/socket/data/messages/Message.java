package pl.hellothere.containers.socket.data.messages;

import pl.hellothere.containers.SocketPackage;

import java.util.Date;

public class Message implements SocketPackage {
    Date sendTime;
    int senderId;

    protected Message() {}

    public Message(int senderId) {
        this.senderId = senderId;
    }

    public Message(int senderId, Date sendTime) {
        this.senderId = senderId;
        this.sendTime = sendTime;
    }

    public int getSenderID() {
        return senderId;
    }

    public Date getSendTime() {
        if(sendTime == null)
            throw new RuntimeException();
        return sendTime;
    }
}
