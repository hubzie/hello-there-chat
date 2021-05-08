package pl.hellothere.client.network;

import pl.hellothere.containers.socket.data.notifications.Notification;

public interface NotificationHandler {
    void handle(Notification action);
}
