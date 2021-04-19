package pl.hellothere.containers.messages;

import pl.hellothere.containers.SocketPackage;

import java.util.Date;

public abstract class Message implements SocketPackage {
    int uid;
    Date date;

    Message(int uid, Date date) {
        this.uid = uid;
        this.date = date;
    }

    public final int getSenderID() {
        return uid;
    }

    public final Date getDate() {
        return date;
    }
}
