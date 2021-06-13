package pl.hellothere.server.listener;

import pl.hellothere.containers.socket.data.notifications.Notification;
import pl.hellothere.server.client.ClientHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ListenerManager {
    ConcurrentHashMap<Integer, Listener> map = new ConcurrentHashMap<>();

    public void listen(ClientHandler handler, int old_id, int new_id) {
        if(map.containsKey(old_id))
            map.get(old_id).unlisten(handler);
        map.computeIfAbsent(new_id, x -> new Listener()).listen(handler);
    }

    public void sendUpdate(int id, Notification msg) {
        new Thread(() -> {
            if(map.containsKey(id))
                map.get(id).sendUpdate(msg);
        }).start();
    }

    public void unlisten(ClientHandler handler, int id) {
        if(map.containsKey(id))
            map.get(id).unlisten(handler);
    }
}