package pl.hellothere.server.listener;

import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.server.client.ClientHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ListenerManager {
    ConcurrentHashMap<Integer, Listener> map = new ConcurrentHashMap<>();

    public void listen(ClientHandler handler, int old_conv, int new_conv) {
        if(map.containsKey(old_conv))
            map.get(old_conv).unlisten(handler);
        map.computeIfAbsent(new_conv, x -> new Listener()).listen(handler);
    }

    public void sendUpdate(int conv, Message msg) {
        new Thread(() -> {
            if(map.containsKey(conv))
                map.get(conv).sendUpdate(msg);
        }).start();
    }

    public void unlisten(ClientHandler handler, int conv) {
        if(map.containsKey(conv))
            map.get(conv).unlisten(handler);
    }
}