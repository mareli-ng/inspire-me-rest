package it.mdg.inspireme.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JwtUser<T extends UserDetails> {

	String accessToken;
	String refreshToken;
	UserDetails details;

}
