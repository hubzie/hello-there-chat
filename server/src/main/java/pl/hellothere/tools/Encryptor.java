package pl.hellothere.tools;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class Encryptor {
    private static final String algorithm = "AES";

    private final PublicKey publicKey;
    private final KeyAgreement agreement;
    private byte[] secret;
    private final Cipher cipher;

    public Encryptor() {
        try {
            cipher = Cipher.getInstance(algorithm);

            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(256);
            KeyPair kp = kpg.generateKeyPair();

            publicKey = kp.getPublic();
            agreement = KeyAgreement.getInstance("ECDH");
            agreement.init(kp.getPrivate());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public void setReceiverKey(PublicKey pk) {
        try {
            agreement.doPhase(pk, true);
            secret = agreement.generateSecret();
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(String data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            return cipher.doFinal(data.getBytes());
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, generateKey());
            return new String(cipher.doFinal(data));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private Key generateKey() {
        return new SecretKeySpec(secret, "AES");
    }
}