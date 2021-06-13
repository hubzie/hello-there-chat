package pl.hellothere.server.database;

import pl.hellothere.server.database.exceptions.HasherException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    static String convert(byte[] arr) {
        StringBuilder b = new StringBuilder();
        for(byte a : arr)
            b.append(String.format("%02x", a));
        return b.toString();
    }

    static byte[] encode(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            if(salt != null)
                md.update(salt);
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new HasherException();
        }
    }
}
