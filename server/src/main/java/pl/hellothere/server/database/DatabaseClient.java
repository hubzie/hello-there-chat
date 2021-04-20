package pl.hellothere.server.database;

import pl.hellothere.containers.data.Conversation;
import pl.hellothere.containers.data.ConversationDetails;
import pl.hellothere.containers.messages.Message;
import pl.hellothere.containers.messages.TextMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseClient implements AutoCloseable {
    private Connection db = null;

    public DatabaseClient(String db_address, String db_login, String db_password) throws ClassNotFoundException, DatabaseException {
        Class.forName("org.postgresql.Driver");
        try {
            db = DriverManager.getConnection(db_address, db_login, db_password);
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new DatabaseConnectionException();
        }
    }

    @Override
    public void close() {
        try {
            if (db != null)
                db.close();
        } catch (SQLException e) {
            System.out.println("Unable to close connection with database: " + e.toString());
        }
    }

    static String convert(byte[] arr) {
        StringBuilder b = new StringBuilder();
        for(byte a : arr)
            b.append(String.format("%02x", a));
        return b.toString();
    }

    static byte[] encode(String password, byte[] salt) throws DatabaseException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            if(salt != null)
                md.update(salt);
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new DatabaseException("Unable to encode data");
        }
    }

    byte[] getEncodedPassword(String login, String password) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("select salt from users where login = ?")) {
            s.setString(1, login);

            try (ResultSet r = s.executeQuery()) {
                if (!r.next())
                    throw new DatabaseAuthenticationException("No such user");
                return encode(password, r.getBytes(1));
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Cannot execute query");
        }
    }

    public int authenticate(String login, String password) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement("select user_id from users where login = ? and password = ?")) {
            s.setString(1, login);
            s.setBytes(2, getEncodedPassword(login, password));

            try (ResultSet r = s.executeQuery()) {
                if (r.next())
                    return r.getInt(1);
                throw new DatabaseAuthenticationException("Wrong password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    public List<Conversation> getConversationList(int user_id) throws DatabaseException {
        try (PreparedStatement s = db.prepareStatement(
                "select conversation_id, name " +
                "from conversation " +
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
                        "order by send_time desc " +
                        "limit 8"
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
        try (PreparedStatement s = db.prepareStatement("select * from conversation where conversation_id = ?")) {
            s.setInt(1, conv_id);

            try (ResultSet r = s.executeQuery()) {
                if(r.next())
                    return new ConversationDetails(r.getInt("conversation_id"), r.getString("name"));
                throw new InvalidData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    static public class DatabaseException extends Exception {
        public DatabaseException() {
            super();
        }

        public DatabaseException(String message) {
            super(message);
        }

        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }

        public DatabaseException(Throwable cause) {
            super(cause);
        }}
    static public class DatabaseConnectionException extends DatabaseException {
        public DatabaseConnectionException() {
            super();
        }

        public DatabaseConnectionException(String message) {
            super(message);
        }

        public DatabaseConnectionException(String message, Throwable cause) {
            super(message, cause);
        }

        public DatabaseConnectionException(Throwable cause) {
            super(cause);
        }}
    static public class DatabaseAuthenticationException extends DatabaseException {
        public DatabaseAuthenticationException() {
            super();
        }

        public DatabaseAuthenticationException(String message) {
            super(message);
        }

        public DatabaseAuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }

        public DatabaseAuthenticationException(Throwable cause) {
            super(cause);
        }
    }
    static public class InvalidData extends DatabaseException {
        public InvalidData() {
            super();
        }

        public InvalidData(String message) {
            super(message);
        }

        public InvalidData(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidData(Throwable cause) {
            super(cause);
        }}
}