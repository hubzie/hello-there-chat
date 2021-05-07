package pl.hellothere.client.network;

import pl.hellothere.tools.CommunicationException;
import pl.hellothere.tools.Communicator;

import java.io.IOException;
import java.net.Socket;

public class ClientCommunicator extends Communicator {
    public ClientCommunicator(Socket s) throws IOException, CommunicationException {
        super(s);
    }
}
