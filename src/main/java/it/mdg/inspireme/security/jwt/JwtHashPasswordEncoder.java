package it.mdg.inspireme.security.jwt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

//@Component
public class JwtHashPasswordEncoder implements PasswordEncoder {

	final static String SALT = "mdgwishlist";

	@Override
	public String encode(CharSequence rawPassword) {
		return this.encodeSha512(rawPassword);
	}

	private String encodeSha512(CharSequence rawPassword) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(SALT.getBytes());
			byte[] bytes = md.digest(rawPassword.toString().getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return generatedPassword;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(this.encode(rawPassword));
	}

}
