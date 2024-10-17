package it.mdg.inspireme.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtUser<T extends UserDetails> {

	String token;
	UserDetails details;

	public String getToken() {
		return token;
	}

	public JwtUser<T> setToken(String token) {
		this.token = token;
		return this;
	}

	public UserDetails getDetails() {
		return details;
	}

	public JwtUser<T> setDetails(UserDetails details) {
		this.details = details;
		return this;
	}

}
