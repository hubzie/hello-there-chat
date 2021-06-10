package pl.hellothere.server.activator;

public class EmailActivatorException extends Exception {
    public EmailActivatorException() { super(); }
    public EmailActivatorException(String message) { super(message); }
    public EmailActivatorException(Throwable cause) { super(cause); }
    public EmailActivatorException(String message, Throwable cause) { super(message, cause); }
}