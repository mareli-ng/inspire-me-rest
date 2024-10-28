package it.mdg.inspireme.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;

import it.mdg.inspireme.entities.Riferimento;

public class RiferimentoDaoCustomImpl implements RiferimentoDaoCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Riferimento> findRandomWithFilters(List<Integer> categoryIds, List<Integer> origineIds, List<Integer> tagIds, Pageable pageable) {
        // Costruzione della query base
        StringBuilder sql = new StringBuilder("SELECT * FROM (SELECT DISTINCT r.* FROM riferimenti r");

        // Aggiunta delle join
        boolean hasCategoryIds = categoryIds != null && !categoryIds.isEmpty();
        boolean hasTagIds = tagIds != null && !tagIds.isEmpty();

        if (hasCategoryIds) {
            sql.append(" LEFT JOIN riferimenti_categorie rc ON r.id = rc.id_riferimento");
        }
        if (hasTagIds) {
            sql.append(" LEFT JOIN riferimenti_tag rt ON r.id = rt.id_riferimento");
        }

        // Aggiunta delle condizioni di filtro
        sql.append(" WHERE 1=1");

        if (hasCategoryIds) {
            sql.append(" AND rc.id_categoria IN (:categoryIds)");
        }
        if (origineIds != null && !origineIds.isEmpty()) {
            sql.append(" AND r.id_origine IN (:origineIds)");
        }
        if (hasTagIds) {
            sql.append(" AND rt.id_tag IN (:tagIds)");
        }

        // Chiusura della sottoquery
        sql.append(") AS subquery ORDER BY RANDOM()");

        // Creazione della query
        Query query = entityManager.createNativeQuery(sql.toString(), Riferimento.class);

        // Impostazione dei parametri
        if (hasCategoryIds) {
            query.setParameter("categoryIds", categoryIds);
        }
        if (origineIds != null && !origineIds.isEmpty()) {
            query.setParameter("origineIds", origineIds);
        }
        if (hasTagIds) {
            query.setParameter("tagIds", tagIds);
        }

        // Impostazione della paginazione
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Esecuzione della query e restituzione dei risultati
        @SuppressWarnings("unchecked")
        List<Riferimento> resultList = query.getResultList();

        return resultList;
    }
}