package pl.hellothere.containers.socket.authorization;

import pl.hellothere.containers.socket.connection.requests.Request;

public class ModifyUserRequest extends Request {
    int id;
    String name;
    String login;
    byte[] password;

    protected ModifyUserRequest() {}

    public ModifyUserRequest(int id, String name, String login, byte[] password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public int getID() { return id; }

    public String getName() { return name; }

    public String getLogin() { return login; }

    public byte[] getPassword() { return password; }
}
