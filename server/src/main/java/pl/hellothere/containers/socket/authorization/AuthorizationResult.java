package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.socket.data.UserData;

public class AuthorizationResult implements SocketPackage {
    public enum Code {
        OK, ERROR, SERVER_ERROR
    }

    Code result;
    UserData user;

    protected AuthorizationResult() {}

    public AuthorizationResult(Code result, UserData user) {
        this.result = result;
        this.user = user;
    }

    public boolean success() {
        return (user != null);
    }

    public boolean isServerError() {
        return result.equals(Code.SERVER_ERROR);
    }

    public UserData getUserData() {
        return user;
    }
}