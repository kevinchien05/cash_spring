package com.example.cash.converter;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PassConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String SECRET = "MySuperSecretKey"; // Must be 16 characters

    private SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET.getBytes(), "AES");
    }

    @Override
    public String convertToDatabaseColumn(String plainText) {
        if (plainText == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encrypted) {
        if (encrypted == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }
}
