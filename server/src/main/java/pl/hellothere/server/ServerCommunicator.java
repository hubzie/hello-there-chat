package pl.hellothere.server;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.Communicator;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerCommunicator extends Communicator {
    public ServerCommunicator(Socket s) throws IOException, CommunicationException {
        super(s);

        Thread f = new Thread(() -> {
            try {
                while (!s.isClosed())
                    flush();
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
        });

        f.setDaemon(true);
        f.start();
    }

    BlockingQueue<SocketPackage> que = new LinkedBlockingQueue<>();

    @Override
    public void send(SocketPackage pkg) {
        que.add(pkg);
    }

    void flush() throws CommunicationException {
        while (!que.isEmpty())
            super.send(que.poll());
    }
}
