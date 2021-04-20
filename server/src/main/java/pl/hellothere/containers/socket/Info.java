package pl.hellothere.containers.socket;

import pl.hellothere.containers.SocketPackage;

public class Info implements SocketPackage {
    public enum Status {
        GET_MESSAGES, NEXT_MESSAGE, NO_MORE_MESSAGES,
        CONVERSATION_LIST, NO_MORE_CONVERSATION, CHOOSE_CONVERSATION, CONVERSATION_NOT_FOUND,
        SERVER_ERROR
    }

    Status status;
    int data;

    Info() {}
    Info(Status status) {
        this.status = status;
    }
    Info(Status status, int data) {
        this.status = status;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }
    public int getData() {
        return data;
    }

    public static Info GetMessages= new Info(Status.GET_MESSAGES);
    public static Info NextMessage = new Info(Status.NEXT_MESSAGE);
    public static Info NoMoreMessages = new Info(Status.NO_MORE_MESSAGES);

    public static Info GetConversationList = new Info(Status.CONVERSATION_LIST);
    public static Info NoMoreConversation = new Info(Status.NO_MORE_CONVERSATION);
    public static Info chooseConversation(int id) {
        return new Info(Status.CHOOSE_CONVERSATION, id);
    }
    public static Info ConversationNotFound = new Info(Status.CONVERSATION_NOT_FOUND);

    public static Info ServerError = new Info(Status.SERVER_ERROR);
}