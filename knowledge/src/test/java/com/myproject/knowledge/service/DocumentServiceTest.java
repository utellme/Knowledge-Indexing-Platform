package com.myproject.knowledge.service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.myproject.knowledge.model.DocumentResponse;
import com.myproject.knowledge.repository.DocumentRepository;
import com.myproject.knowledge.metrics.SearchMetrics;
import com.myproject.knowledge.entity.DocumentEntity;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    
    @Mock
    private DocumentRepository repository;

    @Mock
    private SearchMetrics searchMetrics;
    
    @InjectMocks
    private DocumentService service;

    private String tenantId;
    private String query;

    private Long id;


    @BeforeEach
    public void beforeMethod() {
        tenantId = "tenant-a";
        query = "platform";
    }

    @Test
    void shouldSearchDocumentsForTenant() {
        // given
        List<DocumentResponse> repositoryResult = List.of(
                new DocumentResponse( id, "Platform Scalability", 98.0));

        when(repository.searchByTenant(eq("tenant-a"), eq("platform"),eq(10), eq(0)))
                .thenReturn(repositoryResult);

        // when
        List<DocumentResponse> serviceResult =
                service.search(tenantId, query, 10, 0); 

        // then
        assertThat(serviceResult).hasSize(1);
    }

    @Test
    void createDocumentForATenant() {
        // given

        DocumentEntity doc = new DocumentEntity();
        doc.setId(new Random().nextLong());
        doc.setTenantId(tenantId);
        doc.setTitle("Doc123");
        doc.setContent("platform");
        doc.setTags("Platform Engineering");
        doc.setCreatedAt(Instant.now());

        when(repository.save(any(DocumentEntity.class))).thenReturn(doc);
        DocumentEntity response = repository.save(doc);
        // then
        assertThat(response.getId()).isEqualTo(doc.getId());
    }
}
