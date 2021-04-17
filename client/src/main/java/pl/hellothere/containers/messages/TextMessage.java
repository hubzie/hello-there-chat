package pl.hellothere.containers.messages;

public class TextMessage implements Message {
    int uid;
    String content;

    public TextMessage(int uid, String content) {
        this.uid = uid;
        this.content = content;
    }

    @Override
    public int getSenderID() {
        return uid;
    }

    @Override
    public <T> T getContent(T a) throws ClassCastException {
        return (T) content;
    }
}
