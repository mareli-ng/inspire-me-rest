package it.mdg.inspireme.security;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class EncryptService {

    @Value("${property.secret}")
    private String propertySecret;

    @Value("${property.salt_default}")
    private String defaultSalt;

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public String encrypt(String plainText, String salt) throws Exception {
        String newSalt = salt != null ? salt : defaultSalt;
        SecretKey secretKey = generateKey(propertySecret, newSalt);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16])); // Usa un IV casuale in un'applicazione reale
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String cipherText, String salt) throws Exception {
        String newSalt = salt != null ? salt : defaultSalt;
        SecretKey secretKey = generateKey(propertySecret, newSalt);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16])); // Usa lo stesso IV utilizzato durante la crittografia
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decryptedBytes);
    }

    private SecretKey generateKey(String secret, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKey temp = factory.generateSecret(spec);
        return new SecretKeySpec(temp.getEncoded(), ALGORITHM);
    }
}