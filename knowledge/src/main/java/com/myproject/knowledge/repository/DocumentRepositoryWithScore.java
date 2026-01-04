package com.myproject.knowledge.repository;



// public class DocumentRepository {

//     private final JdbcTemplate jdbcTemplate;

//     /**
//      * Performs tenant-isolated full-text search with ranking.
//      */
//     public List<Map<String, Object>> search(
//         String tenantId, String query, int limit, int offset) {
//         return jdbcTemplate.queryForList("""
//             SELECT id, title, bm25(documents_fts) AS score
//             FROM documents_fts
//             WHERE tenant_id = ? AND documents_fts MATCH ?
//             ORDER BY score
//             LIMIT ? OFFSET ?
//         """, tenantId, query, limit, offset);
//     }

//     public DocumentRepository(JdbcTemplate jdbcTemplate) {
//         this.jdbcTemplate = jdbcTemplate;
//     }

//     /**
//      * Inserts document metadata into the primary table.
//      */
//     public void insert(String id, String tenantId, Document doc) {
//         jdbcTemplate.update(
//             """
//             INSERT INTO documents (id, tenant_id, title, content, tags)
//             VALUES (?, ?, ?, ?, ?)
//             """,
//             id,
//             tenantId,
//             doc.getTitle(),
//             doc.getContent(),
//             doc.getTags() != null ? String.join(",", doc.getTags()) : null
//         );
//     }

//     /**
//      * Inserts searchable fields into the FTS5 virtual table.
//      * This is required because SQLite FTS tables are not automatically synced.
//      */
//     public void insertFts(String id, String tenantId, Document doc) {
//         jdbcTemplate.update(
//             """
//             INSERT INTO documents_fts (id, tenant_id, title, content)
//             VALUES (?, ?, ?, ?)
//             """,
//             id,
//             tenantId,
//             doc.getTitle(),
//             doc.getContent()
//         );
//     }
    
// }

import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.myproject.knowledge.model.DocumentResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class DocumentRepositoryWithScore {

    
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Perform tenant-isolated full-text search using H2 FullText Search.
     *
     * @param tenantId tenant identifier (isolation boundary)
     * @param query    search query string
     * @param limit    max number of results
     * @param offset   pagination offset
     * @return ranked list of document responses
     */
    @SuppressWarnings("unchecked")
    public List<DocumentResponse> search( String tenantId, String query, int limit, int offset) {

        /*
         * FT_SEARCH_DATA returns:
         *  - KEYS: array of primary key values
         *  - SCORE: relevance score
         *
         * We JOIN back to the documents table and enforce tenant isolation.
         */

        // LOGGER.info("result string output \n " + resultString);

        String sql = """
            SELECT d.id, d.title, ft.score FROM FT_SEARCH_DATA(?, 0, 0) ft JOIN documents d ON d.id = ft.KEYS[1]
            WHERE d.tenant_id = ? ORDER BY ft.score DESC LIMIT ? OFFSET ? """;
     
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter(1, query);
        nativeQuery.setParameter(2, tenantId);
        nativeQuery.setParameter(3, limit);
        nativeQuery.setParameter(4, offset);

        List<Object[]> rows = nativeQuery.getResultList();
        List<DocumentResponse> results = new ArrayList<>();

        for (Object[] row : rows) {
            Long id = (Long) row[0];
            String title = (String) row[1];
            double score = ((Number) row[2]).doubleValue();

            results.add(new DocumentResponse(id, title, score));
        }

        return results;
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