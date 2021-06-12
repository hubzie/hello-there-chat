package pl.hellothere.server.database.exceptions;

public class InvalidDataException extends DatabaseException {
    public InvalidDataException() {}
    public InvalidDataException(Throwable cause) { super(cause); }
    public InvalidDataException(String message) { super(message); }
}
