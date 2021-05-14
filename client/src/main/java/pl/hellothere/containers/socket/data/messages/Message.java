package pl.hellothere.containers.socket.data.messages;

import pl.hellothere.containers.SocketPackage;

import java.util.Date;

public class Message implements SocketPackage {
    Date sendTime;
    int senderId = -1;

    protected Message() {}

    public void fill(int senderId, Date sendTime) {
        this.senderId = senderId;
        this.sendTime = sendTime;
    }

    public Message(int senderId, Date sendTime) {
        this.senderId = senderId;
        this.sendTime = sendTime;
    }

    public int getSenderID() {
        return senderId;
    }

    public Date getSendTime() {
        return sendTime;
    }
}
