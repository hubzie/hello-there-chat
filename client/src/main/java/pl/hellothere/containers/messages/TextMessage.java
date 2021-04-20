package pl.hellothere.containers.messages;

import java.util.Date;

public class TextMessage extends Message {
    String content;

    public TextMessage(int uid, Date date, String content) {
        super(uid, date);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return date+": ("+uid+") -> "+content;
    }
}
