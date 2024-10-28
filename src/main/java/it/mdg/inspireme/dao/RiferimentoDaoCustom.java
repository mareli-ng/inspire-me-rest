package it.mdg.inspireme.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import it.mdg.inspireme.entities.Riferimento;

public interface RiferimentoDaoCustom {
	
	List<Riferimento> findRandomWithFilters(List<Integer> categoryIds, List<Integer> origineIds, List<Integer> tagIds,
			Pageable pageable);
}
