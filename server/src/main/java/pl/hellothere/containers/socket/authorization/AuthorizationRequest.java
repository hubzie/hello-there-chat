package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.SocketPackage;

public class AuthorizationRequest implements SocketPackage {
    String login;
    String password;

    AuthorizationRequest() {}

    public AuthorizationRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
