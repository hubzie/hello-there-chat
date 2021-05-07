package pl.hellothere.containers.socket.connection;

import pl.hellothere.containers.SocketPackage;

import java.security.PublicKey;

public class SecurityData implements SocketPackage {
    PublicKey key;

    protected SecurityData() {}

    public SecurityData(PublicKey key) {
        this.key = key;
    }

    public PublicKey getKey() {
        return key;
    }
}
