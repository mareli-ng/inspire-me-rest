package it.mdg.inspireme.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.RiferimentoTag;

@Repository
public interface RiferimentiTagDao extends JpaRepository<RiferimentoTag, Integer> {
	void deleteByIdRiferimento(Integer idRiferimento);

	void deleteByIdTag(Integer idTag);

	Optional<RiferimentoTag> findByIdRiferimentoAndIdTag(Integer idRiferimento, Integer idTag);
}
