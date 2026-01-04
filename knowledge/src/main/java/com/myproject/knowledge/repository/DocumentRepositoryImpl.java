package com.myproject.knowledge.repository;

import com.myproject.knowledge.entity.DocumentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.myproject.knowledge.model.DocumentResponse;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DocumentResponse> searchByTenant( String tenantId, String query, int limit, int offset ) {

        String jpql = """
            SELECT d
            FROM DocumentEntity d
            WHERE d.tenantId = :tenantId
              AND (
                   LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%'))
              )
            ORDER BY d.createdAt DESC
        """;

        List<DocumentEntity> entities =  entityManager.createQuery(jpql, DocumentEntity.class)
                .setParameter("tenantId", tenantId)
                .setParameter("query", query)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        
        List<DocumentResponse> results = new ArrayList<>();

        for (DocumentEntity entity : entities) {
            Long id = entity.getId();
            String title = entity.getTitle();
            double score = getScore(entity, query);

            results.add(new DocumentResponse(id, title, score));
        }
        return results;
    }

    /*
        Helper function to get score
     */
    private double getScore(DocumentEntity entity, String query ){

        double score = 0;

        if (entity.getTitle().contains(query)) score += 8;
        if (entity.getContent().contains(query)) score += 4;
        if (entity.getTitle().equalsIgnoreCase(query)) score += 10;

        return score;
    }

    /**
     * Optional helper for test verification or debugging.
     */
    public long countByTenant(String tenantId) {
        return entityManager.createQuery(
                "SELECT COUNT(d) FROM DocumentEntity d WHERE d.tenantId = :tenantId",
                Long.class
        )
        .setParameter("tenantId", tenantId)
        .getSingleResult();
    }
}

 