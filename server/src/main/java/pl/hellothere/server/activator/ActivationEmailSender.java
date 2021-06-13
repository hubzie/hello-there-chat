package pl.hellothere.server.activator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivationEmailSender {
    private final Properties config;
    private final ExecutorService sender = Executors.newFixedThreadPool(1);

    public ActivationEmailSender() throws EmailActivatorException {
        try (InputStream file = new FileInputStream("src/main/resources/email.properties")) {
            config = new Properties();
            config.load(file);
        } catch (Exception e) {
            throw new EmailActivatorException(e);
        }
    }

    public void sendToken(String email, String token) {
        sender.submit(() -> {
            String link = config.getProperty("server.activationAddress") + token;

            try {
                Session session = Session.getInstance(config, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getProperty("mail.user"), config.getProperty("mail.password"));
                    }
                });

                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(config.getProperty("mail.user"), "Hello There Chat"));
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
                msg.setSubject("Activation link");
                msg.setContent("Cilic <a href=\"" + link + "\">here</a> to activate your account", "text/html; charset=utf-8");

                Transport.send(msg);
            } catch (Exception e) {
                System.out.println("Activation link: " + link);
            }
        });
    }
}
