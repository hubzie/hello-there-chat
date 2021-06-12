package pl.hellothere.server.database.exceptions;

public class DatabaseSQLException extends DatabaseException {
    public DatabaseSQLException() {}
    public DatabaseSQLException(String message) { super(message); }
}
