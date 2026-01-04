package com.myproject.knowledge.performance;

import com.myproject.knowledge.entity.DocumentEntity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PerformanceDataLoader {

    private static final int TOTAL_DOCS = 10000;
    //private static final int TENANTS = 5;

    private final EntityManager entityManager;

    public PerformanceDataLoader(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    @Transactional
    public void loadData() {

        Random random = new Random();

        for (int i = 0; i < TOTAL_DOCS; i++) {

            DocumentEntity doc = new DocumentEntity();
            doc.setId(new Random().nextLong());
            doc.setTenantId("tenant-benchmark");
            doc.setTitle("Platform Engineering Doc " + i);
            doc.setContent(generateContent(random));
            doc.setTags("platform,search,benchmark");

            entityManager.persist(doc);

            if (i % 500 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        System.out.println("Loaded " + TOTAL_DOCS + " documents for benchmark");
    }

    private String generateContent(Random random) {
        return """
            Platform engineering team performing performance benchmarking of a dataset of 10k documents.
            Expect search result to perform 10k documents in less than 100 ms.
            """ + random.nextInt();
    }
}
