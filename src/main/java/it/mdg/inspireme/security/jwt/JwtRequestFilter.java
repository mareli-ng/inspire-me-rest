package it.mdg.inspireme.security.jwt;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import it.mdg.inspireme.security.AppUserDetailsService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private AppUserDetailsService userDetailsService;
	@Autowired
	private JwtTokenService jwtTokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		logger.debug("JwtRequestFilter - Incoming request: " + request.getRequestURI());

		String username = null;
		String jwtToken = jwtTokenService.getJwtToken(request);

		if (!Objects.isNull(jwtToken)) {

			try {
				username = jwtTokenService.getUsernameFromToken(jwtToken);
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (Boolean.TRUE.equals(jwtTokenService.validateToken(jwtToken, userDetails))) {
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}
				}
			} catch (IllegalArgumentException e) {
				logger.debug("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.debug("JWT Token has expired");
			}
		} else {
			logger.debug("JWT Token does not begin with Bearer String");
		}
		chain.doFilter(request, response);
		logger.debug("JwtRequestFilter - Response status: " + response.getStatus());
	}

}