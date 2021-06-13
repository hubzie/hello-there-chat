package pl.hellothere.containers.socket.connection.requests;

public class AddableUserListRequest extends Request {
    protected AddableUserListRequest() {}

    String prefix;

    public AddableUserListRequest(String prefix) { this.prefix = prefix; }

    public String getPrefix() { return prefix; }
}
