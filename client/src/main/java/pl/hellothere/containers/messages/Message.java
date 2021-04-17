package pl.hellothere.containers.messages;

public interface Message {
    int getSenderID();
    <T> T getContent(T a);
}
