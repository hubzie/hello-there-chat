package pl.hellothere.containers.socket.data.messages;

import java.util.Date;

public class TextMessage extends Message {
    String content;

    protected TextMessage() {
        super();
    }

    public TextMessage(String content) {
        super();
        this.content = content;
    }

    public TextMessage(int senderId, Date sendTime, String content) {
        super(senderId, sendTime);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
