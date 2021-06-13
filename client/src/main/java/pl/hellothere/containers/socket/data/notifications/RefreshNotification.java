package pl.hellothere.containers.socket.data.notifications;

public class RefreshNotification extends Notification {
    public enum Context {
        CONVERSATION_LIST, CONVERSATION_DATA
    }

    protected RefreshNotification() {}

    Context context;

    public RefreshNotification(Context context) { this.context = context; }

    public Context getContext() { return context; }
}