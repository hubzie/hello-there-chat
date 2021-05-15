package pl.hellothere.client.network;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.data.notifications.Notification;
import pl.hellothere.containers.socket.data.notifications.StopNotification;
import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.Communicator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientCommunicator extends Communicator {
    private enum Mode {
        LISTEN, STOP
    }

    NotificationHandler handler = null;
    Thread listener = null;
    Mode mode = Mode.LISTEN;
    BlockingQueue<Object> response = new ArrayBlockingQueue<>(1);

    public ClientCommunicator(Socket s) throws IOException, CommunicationException {
        OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
        out.write("startMainApp\r\n");
        out.flush();

        init(s);
        listen();
    }

    public void setHandler(NotificationHandler handler) {
        this.handler = handler;
    }

    public void listen() {
        if(listener != null)
            throw new RuntimeException();

        listener = new Thread(() -> {
            try {
                while (mode != Mode.STOP) {
                    while (mode == Mode.LISTEN) {
                        SocketPackage n = read();
                        if (n instanceof StopNotification)
                            mode = Mode.STOP;
                        else if (n instanceof Notification) {
                            if (handler != null)
                                handler.handle((Notification) n);
                        } else
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

    public void join() throws CommunicationException {
        try {
            if (listener != null)
                listener.join();
        } catch (InterruptedException e) {
            throw new CommunicationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends SocketPackage> T sendAndRead(SocketPackage s) throws CommunicationException {
        try {
            send(s);
            return (T) response.take();
        } catch (InterruptedException e) {
            throw new CommunicationException(e);
        }
    }
}
