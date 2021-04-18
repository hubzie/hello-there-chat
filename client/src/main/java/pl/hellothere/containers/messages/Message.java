package pl.hellothere.containers.messages;

import java.util.Date;

public abstract class Message {
    int uid;
    Date date;

    public abstract int getSenderID();
    public abstract Date getDate();
}
