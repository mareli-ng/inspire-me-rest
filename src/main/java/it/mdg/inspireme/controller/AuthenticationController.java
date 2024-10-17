package it.mdg.inspireme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.mdg.inspireme.dto.LoginDto;
import it.mdg.inspireme.security.AppUserDetails;
import it.mdg.inspireme.security.AppUserDetailsService;
import it.mdg.inspireme.security.jwt.JwtTokenService;
import it.mdg.inspireme.security.jwt.JwtUser;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private AppUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDto dto) {
		ResponseEntity<?> responseEntity = null;
		AppUserDetails user = userDetailsService.loadUserByUsername(dto.getUsername());
		try {
			if (user != null) {
				authenticate(dto.getUsername(), dto.getPassword());
				UserDetails details = user;
				responseEntity = ResponseEntity.ok(new JwtUser<UserDetails>().setDetails(details)
						.setToken(jwtTokenService.generateAuthenticationTokenForUser(details)));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		} catch (AuthenticationException e) {
			System.out.println("UNAUTHORIZED user ");
			responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (Exception e) {
			System.err.println("Authentication error: " + e.getMessage());
			throw e;
		}
		return responseEntity;
	}

	private void authenticate(String username, String password) {
		/**
		 * l'autenticazione può essere effettuata da più URI, per questo motivo la
		 * chiamata all'authentication manager viene fatta nel Controller e non usando
		 * lo UsernamePasswordAuthenticationFiletr
		 */
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		authenticationManager.authenticate(authRequest);
	}

	@GetMapping(value = "/utente")
	public AppUserDetails getUserInfo() throws Exception {
		return AppUserDetails.loggedUser();
	}

}