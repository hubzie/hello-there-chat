package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.SocketPackage;

public class RegistrationRequest implements SocketPackage {
    String name;
    String login;
    String email;
    byte[] password;

    protected RegistrationRequest() {}

    public RegistrationRequest(String name, String login, String email, byte[] password) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPassword() {
        return password;
    }
}
