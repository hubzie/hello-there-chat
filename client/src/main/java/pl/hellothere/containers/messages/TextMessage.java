package pl.hellothere.containers.messages;

import java.util.Date;

public class TextMessage extends Message {
    String content;

    public TextMessage(int uid, Date date, String content) {
        this.uid = uid;
        this.date = date;
        this.content = content;
    }

    @Override
    public int getSenderID() {
        return uid;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
