package it.mdg.inspireme.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.mdg.inspireme.entities.Categoria;

@Repository
public interface CategorieDao extends JpaRepository<Categoria, Integer> {
	List<Categoria> findByIdCategoriaPadre(Integer idCategoriaPadre);

	@Query("select c from Categoria c where c.id in (:idCategorie)")
	List<Categoria> findByIdList(List<Integer> idCategorie);
}
