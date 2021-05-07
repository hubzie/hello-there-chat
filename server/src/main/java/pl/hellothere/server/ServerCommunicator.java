package pl.hellothere.server;

import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.Communicator;

import java.io.IOException;
import java.net.Socket;

public class ServerCommunicator extends Communicator {
    public ServerCommunicator(Socket s) throws IOException, CommunicationException {
        super(s);
    }
}
