package pl.hellothere.server.activator;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ActivationEmailSender {
    private static final String activationAddress;

    static {
        try (InputStream file = new FileInputStream("src/main/resources/server.properties")) {
            Properties config = new Properties();
            config.load(file);

            activationAddress = config.getProperty("server.activationAddress");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendToken(String token) {
        System.out.println(activationAddress+token);
    }
}
