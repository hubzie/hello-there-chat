package pl.hellothere.client.network;

import pl.hellothere.containers.socket.authorization.*;
import pl.hellothere.containers.socket.connection.SecurityData;
import pl.hellothere.containers.socket.connection.commands.Command;
import pl.hellothere.containers.socket.connection.requests.*;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.AddableUsersList;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.tools.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ServerClient {
    final Socket connection;

    private final ClientCommunicator communicator;
    private final Encryptor encryptor = new Encryptor();

    int conv_id = -1;
    int conv_cnt = 16;
    UserData user = null;

    public ServerClient() throws ConnectionError {
        try (InputStream file = new FileInputStream("src/main/resources/connection.properties")){
            Properties config = new Properties();
            config.load(file);

            String address = config.getProperty("server.address");
            int port = Integer.parseInt(config.getProperty("server.port"));

            connection = new Socket(address, port);
            communicator = new ClientCommunicator(connection);

            SecurityData key = communicator.sendAndRead(new SecurityData(encryptor.getPublicKey()));
            encryptor.setReceiverKey(key.getKey());
        } catch (IOException | CommunicationException e) {
            e.printStackTrace();
            throw new ConnectionError(e);
        }
    }

    public RegistrationResult.Code register(String name, String login, String email, String password) throws CommunicationException {
        RegistrationResult res = communicator.sendAndRead(new RegistrationRequest(name, login, email, encryptor.encrypt(password)));
        if (res.getCode().equals(RegistrationResult.Code.SERVER_ERROR))
            throw new CommunicationException();
        return res.getCode();
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

    public ModifyUserResult.Code modifyUser(int id, String name, String login, String password) throws CommunicationException {
        ModifyUserResult res = communicator.sendAndRead(new ModifyUserRequest(id, name, login, encryptor.encrypt(password)));
        if (res.getCode().equals(ModifyUserResult.Code.SERVER_ERROR))
            throw new CommunicationException();
        return res.getCode();
    }

    public List<Conversation> getConversationList() throws CommunicationException {
        return communicator.sendAndRead(new ConversationListRequest(conv_cnt));
    }

    public List<Conversation> loadMoreConversationsAndReload() throws CommunicationException {
        conv_cnt += 8;
        return getConversationList();
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

    public void createConversation(String name) throws CommunicationException { communicator.send(ModifyConversationRequest.create(name)); }

    public void removeConversation(int id) throws CommunicationException { communicator.send(ModifyConversationRequest.delete(id)); }

    public void renameConversation(int id, String name) throws CommunicationException { communicator.send(ModifyConversationRequest.rename(id, name)); }

    public List<UserData> getAddableUserList(String prefix) throws CommunicationException {
        return ((AddableUsersList) communicator.sendAndRead(new AddableUserListRequest(prefix))).getList();
    }

    public void addMember(int id) throws CommunicationException { communicator.send(new ManageMembersRequest(ManageMembersRequest.Type.ADD, id)); }

    public void removeMember(int id) throws CommunicationException { communicator.send(new ManageMembersRequest(ManageMembersRequest.Type.REMOVE, id)); }

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