package it.mdg.inspireme.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.Origine;

@Repository
public interface OriginiDao extends JpaRepository<Origine, Integer> {

	@Query("select o from Origine o where trim(lower(o.descrizione)) = trim(lower(:descrizione))")
	Optional<Origine> findByDescrizione(String descrizione);

}
