package com.myproject.knowledge.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Database configuration for H2 in-memory database.
 *
 * Responsibilities:
 *  - Initialize H2 Full-Text Search
 *  - Create full-text index on documents.content
 *  - Ensure idempotent startup behavior
 */
@Configuration
public class DatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);
    

    @PersistenceContext
    private EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    /**
     * Initializes H2 Full-Text Search (FTS) after schema creation.
     *
     * CommandLineRunner guarantees execution after
     * the Spring context is fully initialized.
     */

    public DatabaseConfig(PlatformTransactionManager txManager) {
        this.transactionTemplate = new TransactionTemplate(txManager);
    }

    @Bean
    public ApplicationRunner initH2FullTextSearch() {
        return args -> transactionTemplate.execute (status ->{
            try {
                LOGGER.info("Initializing H2 Full Text Search (explicit aliases)...");

                // 1. Register SQL aliases 
                entityManager.createNativeQuery(
                     "CREATE ALIAS IF NOT EXISTS FT_INIT " + "FOR 'org.h2.fulltext.FullText.init'"
                ).executeUpdate();

                entityManager.createNativeQuery(
                    "CREATE ALIAS IF NOT EXISTS FT_CREATE_INDEX " +
                    "FOR 'org.h2.fulltext.FullText.createIndex'"
                ).executeUpdate();


                entityManager.createNativeQuery(
                    "CREATE ALIAS IF NOT EXISTS FT_SEARCH " +
                    "FOR 'org.h2.fulltext.FullText.search'"
                ).executeUpdate();

                // entityManager.createNativeQuery(
                //     "CREATE ALIAS IF NOT EXISTS FT_SEARCH_NODES " +
                //     "FOR 'org.h2.fulltext.FullText.searchNodes'"
                // ).executeUpdate();

                // 2. Initialize FullText infrastructure
                entityManager.createNativeQuery("CALL FT_INIT()")
                         .executeUpdate();

                // 3. Create index (AFTER table exists)
                entityManager.createNativeQuery(
                    "CALL FT_CREATE_INDEX('PUBLIC', 'DOCUMENTS', 'CONTENT')"
                ).executeUpdate();

                LOGGER.info("H2 Full Text Search initialized successfully");
            } catch (Exception ex) {
                LOGGER.error("Failed to initialize H2 Full-Text Search", ex);
                LOGGER.error(ex.getMessage(), ex);
                throw ex;
            }
            return null;
        });
    }
}
