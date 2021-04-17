package pl.hellothere.containers.socket;

public class AuthorizationRequest implements Package {
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
