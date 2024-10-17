package it.mdg.inspireme.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.Utente;

@Repository
public interface UtentiDao extends JpaRepository<Utente, Integer> {

	Optional<Utente> findByUsername(String username);

}
