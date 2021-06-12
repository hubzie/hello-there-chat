package pl.hellothere.containers.socket.connection.requests;

public class ManageMembersRequest extends Request {
    public enum Type {
        ADD, REMOVE
    }

    protected ManageMembersRequest() {}

    Type type;
    int id;

    public ManageMembersRequest(Type type, int id) {
        this.type = type;
        this.id = id;
    }

    public Type getType() { return type; }
    public int getId() { return id; }
}