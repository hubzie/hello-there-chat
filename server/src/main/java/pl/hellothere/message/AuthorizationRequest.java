package pl.hellothere.message;

public class AuthorizationRequest implements Message {
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
