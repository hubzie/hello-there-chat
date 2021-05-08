package pl.hellothere.containers.socket.data.notifications;

public class SampleNotification extends Notification {
    protected SampleNotification() {
        super();
    }

    int it = 0;
    public SampleNotification(int it) {
        super();
        this.it = it;
    }

    @Override
    public String toString() {
        return "Iteration: "+it;
    }
}
