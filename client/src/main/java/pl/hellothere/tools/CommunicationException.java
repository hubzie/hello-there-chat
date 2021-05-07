package pl.hellothere.tools;

public class CommunicationException extends Exception {
    public CommunicationException(Throwable cause) {
        super(cause);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
