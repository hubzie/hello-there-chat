package pl.hellothere.client.network;

import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.connection.commands.Command;
import pl.hellothere.containers.socket.connection.requests.ChangeConversationRequest;
import pl.hellothere.containers.socket.connection.requests.ConversationListRequest;
import pl.hellothere.containers.socket.connection.requests.GetMessagesRequest;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.tools.*;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ServerClient {
    private static final String address = "localhost";
    private static final int port = 8374;

    final Socket connection;

    private final Receiver receiver;
    private final Sender sender;
    private final Encryptor encryptor = new Encryptor();

    int conv_id = -1;
    UserData user = null;

    public ServerClient() throws ConnectionError {
        try {
            connection = new Socket(address, port);

            sender = new Sender(connection.getOutputStream());
            receiver = new Receiver(connection.getInputStream());

            sender.send(new SecurityData(encryptor.getPublicKey()));
            encryptor.setReceiverKey(receiver.<SecurityData>read().getKey());
        } catch (IOException | CommunicationException e) {
            throw new ConnectionError(e);
        }
    }

    public boolean signIn(String login, String password) throws ConnectionError {
        if(user != null)
            throw new UserAlreadyLoggedException();

        try {
            sender.send(new AuthorizationRequest(login, encryptor.encrypt(password)));
            AuthorizationResult ar = receiver.read();

            user = ar.getUserData();
            if(ar.isServerError())
                throw new ServerClientException();
            return ar.success();
        } catch (CommunicationException e) {
            throw new ServerClientException(e);
        }
    }

    public UserData getUser() {
        return user;
    }

    public List<Conversation> getConversationList() throws CommunicationException {
        sender.send(new ConversationListRequest());
        return receiver.read();
    }

    public ConversationDetails changeConversation(int conv_id) throws CommunicationException {
        sender.send(new ChangeConversationRequest(this.conv_id = conv_id));
        return receiver.read();
    }

    public List<Message> getMessageList() throws CommunicationException {
        sender.send(new GetMessagesRequest());
        return receiver.read();
    }

    public void close() {
        try {
            sender.send(Command.CloseConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ServerClientException extends ConnectionError {
        public ServerClientException() {
            super();
        }

        public ServerClientException(Throwable cause) {
            super(cause);
        }
    }
    public static class UserAlreadyLoggedException extends ServerClientException {}
}