package it.mdg.inspireme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.mdg.inspireme.dto.LoginDto;
import it.mdg.inspireme.dto.RefreshTokenRequest;
import it.mdg.inspireme.security.AppUserDetails;
import it.mdg.inspireme.security.AppUserDetailsService;
import it.mdg.inspireme.security.jwt.JwtRefreshTokenService;
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

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDto dto) {
		try {
			authenticate(dto.getUsername(), dto.getPassword());
			AppUserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
			String accessToken = jwtTokenService.generateAuthenticationTokenForUser(userDetails);
			String refreshToken = jwtTokenService.generateRefreshTokenForUser(userDetails);
			return ResponseEntity.ok(new JwtUser<UserDetails>().setDetails(userDetails).setAccessToken(accessToken)
					.setRefreshToken(refreshToken));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	private void authenticate(String username, String password) throws AuthenticationException {
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(username, password)
	    );
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	@PostMapping(value = "/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
	    String refreshToken = request.getRefreshToken();
	    System.out.println(String.format("refresh token %s", refreshToken));
	    try {
	        String username = extractUsernameFromRefreshToken(refreshToken);
	        if (username == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
	        }
	        if (jwtTokenService.validateToken(refreshToken, username)) {
	            AppUserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            String newAccessToken = jwtTokenService.generateAuthenticationTokenForUser(userDetails);
	            JwtUser<UserDetails> jwtUser = new JwtUser<UserDetails>()
	                    .setDetails(userDetails)
	                    .setAccessToken(newAccessToken)
	                    .setRefreshToken(refreshToken); // Restituiamo il refresh token
	            return ResponseEntity.ok(jwtUser);
	        } else {
	        	System.out.println("Refresh token is invalid or expired for user: " + username);
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
	        }
	    } catch (Exception e) {
	    	System.out.println("Refresh token exception");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Could not refresh token");
	    }
	}

	
	private String extractUsernameFromRefreshToken(String refreshToken) {
	    return jwtTokenService.getUsernameFromToken(refreshToken);
	}

	@GetMapping(value = "/utente")
	public AppUserDetails getUserInfo() throws Exception {
		return AppUserDetails.loggedUser();
	}

}