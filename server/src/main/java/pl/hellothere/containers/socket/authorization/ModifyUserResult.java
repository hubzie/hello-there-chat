package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.socket.connection.requests.Request;

public class ModifyUserResult extends Request {
    public enum Code {
        OK, LOGIN_ALREADY_USED, SERVER_ERROR
    }

    Code code;

    protected ModifyUserResult() {}

    public ModifyUserResult(Code code) { this.code = code; }

    public Code getCode() { return code; }
}
