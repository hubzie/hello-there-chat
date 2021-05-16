package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.SocketPackage;

public class RegistrationResult implements SocketPackage {
    public enum Code {
        OK, LOGIN_ALREADY_USED, EMAIL_ALREADY_USED, SERVER_ERROR
    }

    Code code;

    protected RegistrationResult() {}

    public RegistrationResult(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
