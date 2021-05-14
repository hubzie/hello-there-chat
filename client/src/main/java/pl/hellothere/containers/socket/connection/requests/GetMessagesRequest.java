package pl.hellothere.containers.socket.connection.requests;

import java.util.Date;

public class GetMessagesRequest extends Request {
    Date time;

    public GetMessagesRequest() {}

    public GetMessagesRequest(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }
}
