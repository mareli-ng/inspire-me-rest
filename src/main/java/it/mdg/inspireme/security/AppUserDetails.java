package it.mdg.inspireme.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.mdg.inspireme.entities.Utente;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AppUserDetails extends User {

	private Utente utente;

	public AppUserDetails() {
		// spring user
		super("", "", new ArrayList<>());
	}

	public AppUserDetails(String username, String password) {
		// spring user
		super(username, password, new ArrayList<>());
	}

	public AppUserDetails(String username, String password, Boolean enabled, Boolean validAccount, Boolean validCredentials, Boolean activeAccount,
			Collection<? extends GrantedAuthority> authorities) {
		// spring user
		super(username, password, enabled, validAccount, validCredentials, activeAccount, authorities);
	}

	public static AppUserDetails loggedUser() {
		return (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public Utente getUtente() {
		return utente;
	}

	public AppUserDetails setUtente(Utente utente) {
		this.utente = utente;
		return this;
	}

	public boolean hasAuthority(String authority) {
		return this.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return super.isAccountNonExpired();
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return super.isAccountNonLocked();
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return super.isCredentialsNonExpired();
	}
	
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return super.isEnabled();
	}

}
