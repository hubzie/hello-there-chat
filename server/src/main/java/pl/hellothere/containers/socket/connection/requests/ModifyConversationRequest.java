package pl.hellothere.containers.socket.connection.requests;

public class ModifyConversationRequest extends Request {
    public enum Type {
        CREATE, DELETE, RENAME
    }

    protected ModifyConversationRequest() {}

    Type type;
    String name;
    int id;

    public static ModifyConversationRequest create(String name) {
        ModifyConversationRequest r = new ModifyConversationRequest();
        r.type = Type.CREATE;
        r.name = name;
        return r;
    }

    public static ModifyConversationRequest delete(int id) {
        ModifyConversationRequest r = new ModifyConversationRequest();
        r.type = Type.DELETE;
        r.id = id;
        return r;
    }

    public static ModifyConversationRequest rename(int id, String name) {
        ModifyConversationRequest r = new ModifyConversationRequest();
        r.type = Type.RENAME;
        r.name = name;
        r.id = id;
        return r;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }
}
