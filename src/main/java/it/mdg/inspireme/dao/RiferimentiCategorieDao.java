package it.mdg.inspireme.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.RiferimentoCategoria;

@Repository
public interface RiferimentiCategorieDao extends JpaRepository<RiferimentoCategoria, Integer> {
	void deleteByIdRiferimento(Integer idRiferimento);

	void deleteByIdCategoria(Integer idCategoria);

	Optional<RiferimentoCategoria> findByIdRiferimentoAndIdCategoria(Integer idRiferimento, Integer idCategoria);

}
