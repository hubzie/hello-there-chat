package pl.hellothere.server.database.exceptions;

public class DatabaseUpdateException extends DatabaseException {
    public DatabaseUpdateException() {}
    public DatabaseUpdateException(String message) { super(message); }
}
