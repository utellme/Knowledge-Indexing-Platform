package com.myproject.knowledge.repository;

//import com.myproject.knowledge.entity.DocumentEntity;
import com.myproject.knowledge.model.DocumentResponse;

import java.util.List;

public interface DocumentRepositoryCustom {

    List<DocumentResponse> searchByTenant(
            String tenantId,
            String query,
            int limit,
            int offset
    );

    long countByTenant(String tenantId);
}
