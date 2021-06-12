package pl.hellothere.containers.socket.data.notifications;

public class ErrorNotification extends Notification {
    protected ErrorNotification() {}

    String message;

    public ErrorNotification(String message) { this.message = message; }
}
