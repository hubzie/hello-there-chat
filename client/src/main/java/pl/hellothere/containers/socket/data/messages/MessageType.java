package pl.hellothere.containers.socket.data.messages;

public enum MessageType {
    Text("T"), Sticker("S");

    private final String type;
    MessageType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
