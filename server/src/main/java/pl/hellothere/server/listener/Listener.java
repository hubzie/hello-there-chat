package pl.hellothere.server.listener;

import pl.hellothere.containers.socket.data.notifications.Notification;
import pl.hellothere.server.client.ClientHandler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Listener {
    Set<ClientHandler> handlers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void listen(ClientHandler handler) {
        handlers.add(handler);
    }

    public void unlisten(ClientHandler handler) {
        handlers.remove(handler);
    }

    public void sendUpdate(Notification msg) {
        for(ClientHandler h : handlers)
            h.sendUpdate(msg);
    }
}
