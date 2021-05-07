package pl.hellothere.tools;

public class ConnectionError extends Exception {
    public ConnectionError() {
        super();
    }

    public ConnectionError(Throwable cause) {
        super(cause);
    }
}