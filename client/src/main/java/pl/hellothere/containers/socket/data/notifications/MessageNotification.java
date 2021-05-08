package pl.hellothere.containers.socket.data.notifications;

import pl.hellothere.containers.socket.data.messages.Message;

public class MessageNotification extends Notification {
    Message content;

    protected MessageNotification() {}

    public MessageNotification(Message content) {
        this.content = content;
    }

    public Message getContent() {
        return content;
    }
}