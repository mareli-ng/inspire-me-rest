package it.mdg.inspireme.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import it.mdg.inspireme.dao.UtentiDao;
import it.mdg.inspireme.entities.Utente;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UtentiDao utentiDao;

	public AppUserDetails loadUserByUsername(String username) {
		return loadUserFromDb(username);
	}

	private AppUserDetails loadUserFromDb(String username) {
		Utente utente = utentiDao.findByUsername(username).orElseThrow(IllegalArgumentException::new);
		return utentiToUserDetails(utente);
	}

	private AppUserDetails utentiToUserDetails(Utente utente) {
		Set<SimpleGrantedAuthority> grants = this.getAuthoritiesByIdUtente(utente);
		AppUserDetails user = new AppUserDetails(utente.getUsername(), utente.getPassword(), true, true, true, true,
				grants);
		user.setUtente(utente);
		return user;
	}

	private Set<SimpleGrantedAuthority> getAuthoritiesByIdUtente(Utente utente) {
		return new HashSet<>(Arrays.asList(new SimpleGrantedAuthority("user")));
	}

}
