package com.requestManager.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class DesEncrypt {

    private final static String SECRET_KEY = "mySecretKey";
    private final static String TRANSFORMATION = "DES/ECB/PKCS5Padding";

    public static String encrypt(String message) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey key = generateKey(SECRET_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedMessage) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey key = generateKey(SECRET_KEY);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static SecretKey generateKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        // Pad or truncate the key to 8 bytes if necessary
        byte[] paddedKeyBytes = new byte[8];
        System.arraycopy(keyBytes, 0, paddedKeyBytes, 0, Math.min(keyBytes.length, 8));

        return new SecretKeySpec(paddedKeyBytes, "DES");
    }
}