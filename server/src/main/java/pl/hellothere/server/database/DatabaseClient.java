package pl.hellothere.server.database;

import org.apache.commons.lang3.RandomStringUtils;
import pl.hellothere.containers.socket.authorization.ModifyUserResult;
import pl.hellothere.containers.socket.authorization.RegistrationResult;
import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.*;
import pl.hellothere.containers.socket.data.notifications.MessageNotification;
import pl.hellothere.containers.socket.data.notifications.RefreshNotification;
import pl.hellothere.server.activator.ActivationEmailSender;
import pl.hellothere.server.activator.EmailActivatorException;
import pl.hellothere.server.database.exceptions.*;
import pl.hellothere.server.listener.ListenerManager;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DatabaseClient implements AutoCloseable {
    private final Connection db;
    private final ListenerManager conversationListener = new ListenerManager();
    private final ListenerManager userListener = new ListenerManager();
    private final ActivationEmailSender emailSender;

    public DatabaseClient(String db_address, String db_login, String db_password) throws DatabaseInitializationException {
        try {
            Class.forName("org.postgresql.Driver");
            db = DriverManager.getConnection(db_address, db_login, db_password);
            emailSender = new ActivationEmailSender();
        } catch (SQLException | ClassNotFoundException | EmailActivatorException e) {
            throw new DatabaseInitializationException(e);
        }
    }

    public ListenerManager getConversationListener() {
        return conversationListener;
    }

    public ListenerManager getUserListener() { return userListener; }

    @Override
    public void close() {
        try {
            if (db != null)
                db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    byte[] getEncodedPassword(String login, String password) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("select salt from users where active = true and login = ?")) {
            s.setString(1, login);

            try (ResultSet r = s.executeQuery()) {
                if (!r.next())
                    throw new DatabaseAuthenticationException();
                return PasswordHasher.encode(password, r.getBytes(1));
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    boolean checkIfUsed(String query, String data) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement(query)) {
            s.setString(1, data);

            try (ResultSet r = s.executeQuery()) {
                r.next();
                if (r.getInt(1) != 0)
                    return true;
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }

        return false;
    }

    static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public RegistrationResult.Code register(String name, String login, String email, String password) {
        try {
            if(checkIfUsed("select count(*) from users where login = ?", login))
                return RegistrationResult.Code.LOGIN_ALREADY_USED;
            if(checkIfUsed("select count(*) from users where email = ?", email))
                return RegistrationResult.Code.EMAIL_ALREADY_USED;

            try (PreparedStatement s = db.prepareStatement("insert into users (name, login, email, password, salt, activation_token) values (?, ?, ?, ?, ?, ?)")) {
                s.setString(1, name);
                s.setString(2, login);
                s.setString(3, email);

                byte[] salt = generateSalt();
                String token = RandomStringUtils.randomAlphabetic(32);

                s.setBytes(4, PasswordHasher.encode(password, salt));
                s.setBytes(5, salt);
                s.setBytes(6, token.getBytes(StandardCharsets.UTF_8));

                s.execute();
                emailSender.sendToken(email, token);

                return RegistrationResult.Code.OK;
            }
        } catch (SQLException | DatabaseException | HasherException | EmailActivatorException e) {
            e.printStackTrace();
            return RegistrationResult.Code.SERVER_ERROR;
        }
    }

    public boolean activateAccount(String token) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("update users set active = true, activation_token = null where activation_token = ?")){
            s.setBytes(1, token.getBytes(StandardCharsets.UTF_8));
            return (s.executeUpdate() == 1);
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    public UserData authenticate(String login, String password) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("select user_id, name from users where active = true and login = ? and password = ?")) {
            s.setString(1, login);
            s.setBytes(2, getEncodedPassword(login, password));

            try (ResultSet r = s.executeQuery()) {
                if (r.next())
                    return new UserData(r.getInt(1), r.getString(2));
                throw new DatabaseAuthenticationException();
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    public ModifyUserResult.Code modifyUser(int id, String name, String login, String password) {
        try {
            if(checkIfUsed("select count(*) from users where login = ? and user_id <> "+id, login))
                return ModifyUserResult.Code.LOGIN_ALREADY_USED;

            try (PreparedStatement s = db.prepareStatement("update users set (name, login, password, salt) = (?, ?, ?, ?) where user_id = ?")) {
                s.setString(1, name);
                s.setString(2, login);

                byte[] salt = generateSalt();

                s.setBytes(3, PasswordHasher.encode(password, salt));
                s.setBytes(4, salt);
                s.setInt(5, id);

                if(s.executeUpdate() != 1)
                    return ModifyUserResult.Code.SERVER_ERROR;

                return ModifyUserResult.Code.OK;
            }
        } catch (SQLException | DatabaseException | HasherException e) {
            e.printStackTrace();
            return ModifyUserResult.Code.SERVER_ERROR;
        }}

    public List<Conversation> getConversationList(int user_id, int count) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement(
                "select conversation_id, name " +
                "from conversations " +
                "natural join (select conversation_id from membership where user_id = ?) member " +
                "left join (select conversation_id, max(send_time) as last_update from messages group by conversation_id ) last using (conversation_id) " +
                "order by last_update desc nulls last " +
                "limit ?"
        )) {
            s.setInt(1, user_id);
            s.setInt(2, count);

            try (ResultSet r = s.executeQuery()) {
                List<Conversation> list = new LinkedList<>();
                while(r.next())
                    list.add(new Conversation(r.getInt(1), r.getString(2)));
                return list;
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    public List<Message> getMessages(int conv_id, Date time) throws DatabaseException {
        String statement;
        if(time == null)
            statement = "select *" +
                "from messages " +
                "where conversation_id = ? " +
                "order by send_time desc " +
                "limit 16";
        else
            statement = "select *" +
                "from messages " +
                "where conversation_id = ? and send_time < ? " +
                "order by send_time desc " +
                "limit 8";

        try (PreparedStatement s = db.prepareStatement(statement)) {
            s.setInt(1, conv_id);
            if(time != null)
                s.setTimestamp(2, new Timestamp(time.getTime()));

            try (ResultSet r = s.executeQuery()) {
                List<Message> list = new LinkedList<>();
                while(r.next())
                    list.add(Message.createMessage(
                            r.getInt("user_id"),
                            r.getTimestamp("send_time"),
                            r.getString("content"),
                            MessageType.fromString(r.getString("type"))
                    ));
                return list;
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    String getConversationName(int conv_id, int user_id) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("select name from conversations where conversation_id = ?")) {
            s.setInt(1, conv_id);

            try (ResultSet r = s.executeQuery()) {
                if(r.next())
                    return r.getString("name");
                throw new InvalidDataException("Invalid converstaion id: "+conv_id);
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    List<UserData> getConversationMembers(int conv_id, int user_id) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement(
                "select user_id, name " +
                        "from membership " +
                        "natural join users " +
                        "where conversation_id = ?"
        )) {
            s.setInt(1, conv_id);

            try (ResultSet r = s.executeQuery()) {
                List<UserData> members = new LinkedList<>();
                while(r.next())
                    members.add(new UserData(r.getInt(1), r.getString(2)));
                return members;
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    public List<UserData> getAddableUserList(int conv_id, String prefix) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement(
                "select user_id, name " +
                        "from users u " +
                        "where ? not in ( select conversation_id from membership m where m.user_id = u.user_id ) " +
                        "and name ilike ? || '%' " +
                        "limit 8"
        )) {
            s.setInt(1, conv_id);
            s.setString(2, prefix);

            try (ResultSet r = s.executeQuery()) {
                List<UserData> members = new LinkedList<>();
                while(r.next())
                    members.add(new UserData(r.getInt(1), r.getString(2)));
                return members;
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    void verifyAction(int conv_id, int user_id) throws DatabaseException {
        if(conv_id == -1 || user_id == -1)
            return;

        try (PreparedStatement s = db.prepareStatement("select count(*) from membership where conversation_id = ? and user_id = ?")) {
            s.setInt(1, conv_id);
            s.setInt(2, user_id);

            try (ResultSet r = s.executeQuery()) {
                r.next();
                if(r.getInt(1) != 1)
                    throw new DatabaseUpdateException("You aren't member of this conversation");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void addMember(int conv_id, int user_id) throws DatabaseException {
        verifyAction(conv_id, user_id);

        try (PreparedStatement s = db.prepareStatement(
                "insert into membership values (?,?)"
        )) {
            s.setInt(1, user_id);
            s.setInt(2, conv_id);
            s.execute();

            conversationListener.sendUpdate(conv_id, new RefreshNotification(RefreshNotification.Context.CONVERSATION_DATA));
        } catch (SQLException e) {
            throw DatabaseException.convert(e,"Conversation doesn't exsit","User is already a member of this conversation");
        }
    }

    public void removeMember(int conv_id, int user_id) throws DatabaseException {
        verifyAction(conv_id, user_id);

        try (PreparedStatement s = db.prepareStatement(
                "delete from membership where conversation_id = ? and user_id = ?"
        )) {
            s.setInt(1, conv_id);
            s.setInt(2, user_id);
            if(s.executeUpdate() != 1)
                throw new DatabaseUpdateException("Unable to remove member");
        } catch (SQLException e) {
            conversationListener.sendUpdate(conv_id, new RefreshNotification(RefreshNotification.Context.CONVERSATION_LIST));
            throw DatabaseException.convert(e,null,null);
        }

        try (PreparedStatement s = db.prepareStatement(
                "select count(*) from membership where conversation_id = ?"
        )) {
            s.setInt(1, conv_id);

            try (ResultSet r = s.executeQuery()) {
                r.next();
                if(r.getInt(1) == 0)
                    deleteConversation(conv_id,-1);
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    public int createConversation() throws DatabaseException {
        int conv_id;
        try (PreparedStatement s = db.prepareStatement(
                "insert into conversations (name) values (null) returning conversation_id"
        )) {
            try (ResultSet r = s.executeQuery()){
                r.next();
                conv_id = r.getInt(1);
            }
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }

        return conv_id;
    }

    public void renameConversation(int conv_id, int user_id, String name) throws DatabaseException {
        verifyAction(conv_id, user_id);

        try (PreparedStatement s = db.prepareStatement(
                "update conversations set name = ? where conversation_id = ?"
        )) {
            s.setString(1, name);
            s.setInt(2, conv_id);

            if(s.executeUpdate() != 1)
                throw new DatabaseException("Conversation doesn't exist");

            conversationListener.sendUpdate(conv_id, new RefreshNotification(RefreshNotification.Context.CONVERSATION_DATA));
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,null);
        }
    }

    public void deleteConversation(int conv_id, int user_id) throws DatabaseException {
        verifyAction(conv_id, user_id);

        try (PreparedStatement s = db.prepareStatement(
                "delete from membership where conversation_id = ?;" +
                        "delete from conversations where conversation_id = ?"
        )) {
            s.setInt(1, conv_id);
            s.setInt(2, conv_id);

            s.execute();

            conversationListener.sendUpdate(conv_id, new RefreshNotification(RefreshNotification.Context.CONVERSATION_LIST));
        } catch (SQLException e) {
            throw DatabaseException.convert(e,null,"Unable to delete conversation");
        }
    }

    public ConversationDetails getConversationDetails(int conv_id, int user_id) throws DatabaseException {
        try {
            verifyAction(conv_id, user_id);
            return new ConversationDetails(conv_id,
                    getConversationName(conv_id, user_id),
                    getConversationMembers(conv_id, user_id));
        } catch (DatabaseUpdateException e) {
            e.printStackTrace();
            return new ConversationDetails(-1, null, null);
        }
    }

    public void sendMessage(Message msg, int conv_id, int user_id) throws DatabaseException {
        verifyAction(conv_id, user_id);

        try (PreparedStatement s = db.prepareStatement("insert into messages values (?, ?, now(), ?, ?) returning now()")) {
            s.setInt(1, user_id);
            s.setInt(2, conv_id);
            s.setString(3, msg.getContent());
            s.setString(4, msg.getType().toString());

            ResultSet r = s.executeQuery();
            r.next();
            msg.fill(user_id, r.getTimestamp(1));
            conversationListener.sendUpdate(conv_id, new MessageNotification(msg));

            for(UserData u : getConversationMembers(conv_id, -1))
                userListener.sendUpdate(u.getID(), new RefreshNotification(RefreshNotification.Context.CONVERSATION_LIST));
        } catch (SQLException e) {
            throw DatabaseException.convert(e,"Unable to send message",null);
        }
    }
}
