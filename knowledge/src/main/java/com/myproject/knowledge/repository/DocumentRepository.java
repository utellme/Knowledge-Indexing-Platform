

package com.myproject.knowledge.repository;

import com.myproject.knowledge.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository
        extends JpaRepository<DocumentEntity, Long>,
                DocumentRepositoryCustom {
}
