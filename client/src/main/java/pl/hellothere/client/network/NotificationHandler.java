package pl.hellothere.client.network;

import pl.hellothere.containers.socket.data.notifications.*;

public interface NotificationHandler {
    void handle(Notification action);
    void handle(ErrorNotification action);
    void handle(RefreshNotification action);
}
