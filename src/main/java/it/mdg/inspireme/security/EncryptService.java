package it.mdg.inspireme.security;

import java.io.Serializable;
import java.util.Objects;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptService implements Serializable {

	private static final long serialVersionUID = 776612489538661022L;

	@Value("${property.secret}")
	private String propertySecret;

	@Value("${property.salt_default}")
	private String defaultSalt;

	public String decrypt(String cipherText, String salt) {
		String newSalt = Objects.isNull(salt) ? defaultSalt : salt;
		TextEncryptor encryptor = Encryptors.text(Hex.toHexString(propertySecret.getBytes()), Hex.toHexString(newSalt.getBytes()));
		return encryptor.decrypt(cipherText);
	}

	public String encrypt(String plainText, String salt) {
		String newSalt = Objects.isNull(salt) ? defaultSalt : salt;
		TextEncryptor encryptor = Encryptors.text(Hex.toHexString(propertySecret.getBytes()), Hex.toHexString(newSalt.getBytes()));
		return encryptor.encrypt(plainText);
	}

}
