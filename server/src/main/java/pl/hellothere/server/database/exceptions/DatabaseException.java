package pl.hellothere.server.database.exceptions;

import java.sql.SQLException;

public class DatabaseException extends Exception {
    public DatabaseException() {}
    public DatabaseException(Throwable cause) { super(cause); }
    public DatabaseException(String message) { super(message); }

    public static DatabaseException convert(SQLException e, String foreign, String unique) {
        if (e.getSQLState().equals("23503")) // FOREIGN KEY
            return new DatabaseSQLException(foreign);
        else if (e.getSQLState().equals("23505")) // UNIQUE KEY
            return new DatabaseSQLException(unique);
        else
            return new DatabaseException(e);
    }
}
