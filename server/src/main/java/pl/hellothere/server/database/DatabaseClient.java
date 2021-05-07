package pl.hellothere.server.database;

import pl.hellothere.containers.socket.data.UserData;
import pl.hellothere.containers.socket.data.converstions.Conversation;
import pl.hellothere.containers.socket.data.converstions.ConversationDetails;
import pl.hellothere.containers.socket.data.messages.Message;
import pl.hellothere.containers.socket.data.messages.TextMessage;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseClient implements AutoCloseable {
    private Connection db = null;

    public DatabaseClient(String db_address, String db_login, String db_password) throws DatabaseInitializationException {
        try {
            Class.forName("org.postgresql.Driver");
            db = DriverManager.getConnection(db_address, db_login, db_password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new DatabaseInitializationException(e);
        }
    }

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
        try (PreparedStatement s = db.prepareStatement("select salt from users where login = ?")) {
            s.setString(1, login);

            try (ResultSet r = s.executeQuery()) {
                if (!r.next())
                    throw new DatabaseAuthenticationException();
                return PasswordHasher.encode(password, r.getBytes(1));
            }
        } catch (SQLException | PasswordHasher.HasherException e) {
            throw new DatabaseException(e);
        }
    }

    public UserData authenticate(String login, String password) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("select user_id, name from users where login = ? and password = ?")) {
            s.setString(1, login);
            s.setBytes(2, getEncodedPassword(login, password));

            try (ResultSet r = s.executeQuery()) {
                if (r.next())
                    return new UserData(r.getInt(1), r.getString(2));
                throw new DatabaseAuthenticationException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    public List<Conversation> getConversationList(int user_id) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement(
                "select conversation_id, name " +
                "from conversations " +
                "natural join (select conversation_id from membership where user_id = ?) member " +
                "natural join (select conversation_id, max(send_time) as last_update from messages group by conversation_id ) last " +
                "order by last_update " +
                "limit 8"
        )) {
            s.setInt(1, user_id);

            try (ResultSet r = s.executeQuery()) {
                List<Conversation> list = new LinkedList<>();
                while(r.next())
                    list.add(new Conversation(r.getInt(1), r.getString(2)));
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    public List<Message> getMessages(int conv_id) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement(
                "select *" +
                        "from messages " +
                        "where conversation_id = ? " +
                        "order by send_time " +
                        "limit 16"
        )) {
            s.setInt(1, conv_id);

            try (ResultSet r = s.executeQuery()) {
                List<Message> list = new LinkedList<>();
                while(r.next())
                    list.add(new TextMessage(r.getInt("user_id"), r.getTimestamp("send_time"), r.getString("content")));
                return list;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    public ConversationDetails getConversationDetails(int conv_id) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("select * from conversations where conversation_id = ?")) {
            s.setInt(1, conv_id);

            try (ResultSet r = s.executeQuery()) {
                if(r.next())
                    return new ConversationDetails(r.getInt("conversation_id"), r.getString("name"));
                throw new InvalidDataException("Invalid converstaion id: "+conv_id);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    static public class DatabaseException extends Exception {
        public DatabaseException() {
            super();
        }

        public DatabaseException(Throwable cause) {
            super(cause);
        }

        public DatabaseException(String message) {
            super(message);
        }
    }
    static public class DatabaseInitializationException extends DatabaseException {
        public DatabaseInitializationException(Throwable cause) {
            super(cause);
        }
    }
    static public class DatabaseAuthenticationException extends DatabaseException {
    }
    static public class InvalidDataException extends DatabaseException {
        public InvalidDataException(String message) {
            super(message);
        }
    }
}