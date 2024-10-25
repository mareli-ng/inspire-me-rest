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

	@Query("SELECT r FROM Riferimento r JOIN RiferimentoCategoria rc ON r.id = rc.idRiferimento WHERE rc.idCategoria = :idCategoria")
	List<Riferimento> findByIdCategoria(Integer idCategoria);

	@Query(value = "SELECT r.* " + "FROM riferimenti r "
			+ "LEFT JOIN riferimenti_categorie rc ON r.id = rc.id_riferimento "
			+ "LEFT JOIN riferimenti_tag rt ON r.id = rt.id_riferimento " + "WHERE "
			+ "    (COALESCE(:categoryIds) IS NULL OR rc.id_categoria IN (:categoryIds)) "
			+ "    AND (COALESCE(:origineIds) IS NULL OR r.id_origine IN (:origineIds)) "
			+ "    AND (COALESCE(:tagIds) IS NULL OR rt.id_tag IN (:tagIds)) " + "GROUP BY r.id "
			+ "ORDER BY RANDOM() ", nativeQuery = true)
	List<Riferimento> findRandom(@Param("categoryIds") List<Integer> categoryIds,
			@Param("origineIds") List<Integer> origineIds, @Param("tagIds") List<Integer> tagIds, Pageable pageable);
}
