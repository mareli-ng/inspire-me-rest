package it.mdg.inspireme.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.Tag;

@Repository
public interface TagDao extends JpaRepository<Tag, Integer> {

	@Query("select t from Tag t where trim(lower(t.descrizione)) = trim(lower(:descrizione))")
	Optional<Tag> findByDescrizione(String descrizione);

	@Query("select t from Tag t where trim(lower(t.descrizione)) in (:tagList)")
	List<Tag> findByDescrizioneList(List<String> tagList);

	@Query("select t from Tag t, RiferimentoTag rt where t.id = rt.idTag and rt.idRiferimento = :idRiferimento")
	List<Tag> findByIdRiferimento(Integer idRiferimento);

}

