package pl.hellothere.client.network;

import pl.hellothere.containers.socket.authorization.AuthorizationRequest;
import pl.hellothere.containers.socket.authorization.AuthorizationResult;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.connection.commands.Command;
import pl.hellothere.containers.socket.connection.requests.ChangeConversationRequest;
import pl.hellothere.containers.socket.connection.requests.ConversationListRequest;
import pl.hellothere.containers.socket.connection.requests.GetMessagesRequest;
import pl.hellothere.containers.socket.connection.requests.SendMessageRequest;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.tools.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class ServerClient {
    private static final String address = "localhost";
    private static final int port = 8374;

    final Socket connection;

    private final ClientCommunicator communicator;
    private final Encryptor encryptor = new Encryptor();

    int conv_id = -1;
    UserData user = null;

    public ServerClient() throws ConnectionError {
        try {
            connection = new Socket(address, port);
            communicator = new ClientCommunicator(connection);

            SecurityData key = communicator.sendAndRead(new SecurityData(encryptor.getPublicKey()));
            encryptor.setReceiverKey(key.getKey());
        } catch (IOException | CommunicationException e) {
            throw new ConnectionError(e);
        }
    }

    public boolean signIn(String login, String password) throws ConnectionError {
        if(user != null)
            throw new UserAlreadyLoggedException();

        try {
            AuthorizationResult ar = communicator.sendAndRead(new AuthorizationRequest(login, encryptor.encrypt(password)));

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
        return communicator.sendAndRead(new ConversationListRequest());
    }

    public ConversationDetails changeConversation(int conv_id) throws CommunicationException {
        return communicator.sendAndRead(new ChangeConversationRequest(this.conv_id = conv_id));
    }

    public List<Message> getMessageList() throws CommunicationException {
        return communicator.sendAndRead(new GetMessagesRequest());
    }

    public List<Message> loadMoreMessages(Date time) throws CommunicationException {
        return communicator.sendAndRead(new GetMessagesRequest(time));
    }

    public void sendMessage(Message msg) throws CommunicationException {
        communicator.send(new SendMessageRequest(msg));
    }

    public void logOut() throws CommunicationException {
        communicator.send(Command.LogOut);
        communicator.setHandler(null);
        conv_id = -1;
        user = null;
    }

    public void close() {
        try {
            communicator.send(Command.CloseConnection);
            communicator.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen(NotificationHandler handler) {
        communicator.setHandler(handler);
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