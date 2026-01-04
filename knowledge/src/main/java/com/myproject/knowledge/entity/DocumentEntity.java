package com.myproject.knowledge.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(
    name = "documents",
    indexes = {
        @Index(name = "idx_documents_tenant", columnList = "tenantId"),
        @Index(name = "idx_documents_tenant_created", columnList = "tenantId, createdAt")
    }
)
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    /**
//      * Comma-separated tags.
//      * Example: "search,platform,knowledge"
//      */
    @Column(name = "tags", length = 512)
    private String tags;

    // getters and setters
    // -------------------------
    // Getters and Setters
    // -------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    /**
     * Large text field used for full-text search indexing.
     */
    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    /**
     * Tags stored as comma-separated values.
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
