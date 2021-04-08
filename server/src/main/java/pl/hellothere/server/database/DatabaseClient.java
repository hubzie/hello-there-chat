package pl.hellothere.server.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

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
            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Cannot execute query");
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
}