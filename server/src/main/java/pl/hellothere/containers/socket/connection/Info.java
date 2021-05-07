package pl.hellothere.containers.socket.connection;

import pl.hellothere.containers.SocketPackage;

public class Info implements SocketPackage {
    public enum Status {
        CLOSE_CONNECTION
    }

    Status status;

    protected Info() {}
    protected Info(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Info))
            return false;
        return ((Info) o).getStatus().equals(getStatus());
    }

    public static Info CloseConnection = new Info(Status.CLOSE_CONNECTION);
}