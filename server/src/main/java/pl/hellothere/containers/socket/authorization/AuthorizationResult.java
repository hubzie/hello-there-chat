package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.SocketPackage;

public class AuthorizationResult implements SocketPackage {
    public enum Code {
        OK, ERROR, SERVER_ERROR
    }

    Code result;
    int user_id;

    AuthorizationResult() {}

    public AuthorizationResult(Code result, int user_id) {
        this.result = result;
        this.user_id = user_id;
    }

    public boolean success() {
        return (user_id != -1);
    }

    public int getID() {
        return user_id;
    }
}
