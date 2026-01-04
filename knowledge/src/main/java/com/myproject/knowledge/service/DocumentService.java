
package com.myproject.knowledge.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.knowledge.entity.DocumentEntity;
import com.myproject.knowledge.metrics.SearchMetrics;
import com.myproject.knowledge.model.DocumentRequest;
import com.myproject.knowledge.model.DocumentResponse;
import com.myproject.knowledge.repository.DocumentRepository;

import io.micrometer.core.instrument.Timer;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repo;

    @Autowired
    private SearchMetrics searchMetrics;

    // public DocumentIngestionService(DocumentRepository repo) {
    //     this.repo = repo;
    // }

    @Transactional
    public Long save(String tenantId, DocumentRequest req) {
        DocumentEntity doc = new DocumentEntity();
       // doc.setId(new Random().nextLong());
        doc.setTenantId(tenantId);
        doc.setTitle(req.getTitle());
        doc.setContent(req.getContent());
        doc.setTags(String.join(",", req.getTags()));
        doc.setCreatedAt(Instant.now());

        DocumentEntity response = repo.save(doc);
        return response.getId();
    }

    public List<DocumentResponse> search( String tenantId, String query, int limit, int offset ) {

        Timer.Sample sample = searchMetrics.startTimer();
        try {
            return repo.searchByTenant(tenantId, query, limit, offset);

        } finally {
            searchMetrics.stopTimer(sample);
        }
    }
}