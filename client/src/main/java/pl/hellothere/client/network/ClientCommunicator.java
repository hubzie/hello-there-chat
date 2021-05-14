package pl.hellothere.client.network;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.data.notifications.Notification;
import pl.hellothere.containers.socket.data.notifications.StopNotification;
import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.Communicator;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientCommunicator extends Communicator {
    private enum Mode {
        LISTEN, STOP
    }

    static Thread listener = null;
    static Mode mode = Mode.LISTEN;
    BlockingQueue<Object> response = new ArrayBlockingQueue<>(1);

    public ClientCommunicator(Socket s) throws IOException, CommunicationException {
        super(s);
    }

    public void listen(NotificationHandler handler) {
        if(listener != null)
            throw new RuntimeException();

        listener = new Thread(() -> {
            try {
                while (mode != Mode.STOP) {
                    while (mode == Mode.LISTEN) {
                        SocketPackage n = read();
                        if (n instanceof StopNotification)
                            mode = Mode.STOP;
                        else if (n instanceof Notification)
                            handler.handle((Notification) n);
                        else if (n != null)
                            response.put(n);
                    }
                }
            } catch (CommunicationException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                listener = null;
            }
        });

        listener.setDaemon(true);
        listener.start();
    }

    public void join() throws InterruptedException {
        if (listener != null)
            listener.join();
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends SocketPackage> T sendAndRead(SocketPackage s) throws CommunicationException {
        try {
            send(s);
            return (T) (listener == null ? read() : response.take());
        } catch (InterruptedException e) {
            throw new CommunicationException(e);
        }
    }
}
