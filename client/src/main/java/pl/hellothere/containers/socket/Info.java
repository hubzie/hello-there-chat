package pl.hellothere.containers.socket;

import pl.hellothere.containers.SocketPackage;

public class Info implements SocketPackage {
    enum Status {
        NO_MORE_MESSAGES, NEXT_MESSAGE, SERVER_ERROR
    }

    Status status;

    Info() {}
    Info(Status status) {
        this.status = status;
    }

    public static Info NextMessage = new Info(Status.NEXT_MESSAGE);
    public static Info NoMoreMessages = new Info(Status.NO_MORE_MESSAGES);
    public static Info ServerError = new Info(Status.SERVER_ERROR);
}
