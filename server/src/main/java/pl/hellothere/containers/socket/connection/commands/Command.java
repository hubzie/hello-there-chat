package pl.hellothere.containers.socket.connection.commands;

import pl.hellothere.containers.SocketPackage;

public class Command implements SocketPackage {
    public enum Status {
        CLOSE_CONNECTION, LOG_OUT
    }

    Status status;

    protected Command() {}
    protected Command(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Command))
            return false;
        return ((Command) o).getStatus().equals(getStatus());
    }

    public static Command CloseConnection = new Command(Status.CLOSE_CONNECTION);
    public static Command LogOut = new Command(Status.LOG_OUT);
}