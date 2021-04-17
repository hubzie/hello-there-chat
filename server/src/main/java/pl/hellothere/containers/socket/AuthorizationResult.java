package pl.hellothere.containers.socket;

public class AuthorizationResult implements Package {
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
