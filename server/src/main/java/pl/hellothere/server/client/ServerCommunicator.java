package pl.hellothere.server.client;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.data.notifications.StopNotification;
import pl.hellothere.server.database.PasswordHasher;
import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.Communicator;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerCommunicator extends Communicator {
    Thread sender = null;
    boolean closed = false;

    public boolean isClosed() {
        return closed;
    }

    public ServerCommunicator(Socket s) throws IOException, CommunicationException {
        init(s);

        sender = new Thread(() -> {
            try {
                while (!s.isClosed() && !closed)
                    flush();
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
        });

        sender.setDaemon(true);
        sender.start();
    }

    public void join() throws CommunicationException {
        try {
            if (sender != null)
                sender.join();
        } catch (InterruptedException e) {
            throw new CommunicationException(e);
        }
    }

    BlockingQueue<SocketPackage> que = new LinkedBlockingQueue<>();

    @Override
    public void send(SocketPackage pkg) {
        que.add(pkg);
    }

    void flush() throws CommunicationException {
        try {
            SocketPackage pkg;
            while((pkg = que.poll(500, TimeUnit.MILLISECONDS)) != null) {
                if (pkg instanceof StopNotification)
                    closed = true;
                super.send(pkg);
            }
        } catch (InterruptedException e) {
            throw new CommunicationException(e);
        }
    }
}
