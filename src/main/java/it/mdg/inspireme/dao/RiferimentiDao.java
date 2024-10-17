package it.mdg.inspireme.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.Riferimento;

@Repository
public interface RiferimentiDao extends JpaRepository<Riferimento, Integer> {
	@Query("SELECT r FROM Riferimento r JOIN RiferimentoCategoria rc ON r.id = rc.idRiferimento WHERE rc.idCategoria IN :categorieIds ORDER BY random()")
	List<Riferimento> findRandomByCategorie(@Param("categorieIds") List<Integer> categorieIds, Pageable pageable);
	
	@Query("SELECT r FROM Riferimento r JOIN RiferimentoCategoria rc ON r.id = rc.idRiferimento WHERE rc.idCategoria = :idCategoria")
	List<Riferimento> findByIdCategoria(Integer idCategoria);
}
