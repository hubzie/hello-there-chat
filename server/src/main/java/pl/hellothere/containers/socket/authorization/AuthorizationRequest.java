package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.SocketPackage;

public class AuthorizationRequest implements SocketPackage {
    String login;
    byte[] password;

    protected AuthorizationRequest() {}

    public AuthorizationRequest(String login, byte[] password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public byte[] getPassword() {
        return password;
    }
}