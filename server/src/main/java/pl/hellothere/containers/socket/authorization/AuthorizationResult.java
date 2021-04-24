package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.SocketPackage;
import pl.hellothere.containers.data.UserData;

public class AuthorizationResult implements SocketPackage {
    public enum Code {
        OK, ERROR, SERVER_ERROR
    }

    Code result;
    UserData user;

    AuthorizationResult() {}

    public AuthorizationResult(Code result, UserData user) {
        this.result = result;
        this.user = user;
    }

    public boolean success() {
        return (user != null);
    }

    public UserData getUserData() {
        return user;
    }
}
