package com.myproject.knowledge.model;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class DocumentRequest {

    @NotBlank(message = "title must not be null or empty")
    private String title;
    @NotBlank(message = "content must not be null or empty")
    private String content;
    @NotEmpty(message = "tags must not be empty")
    private List<String> tags;

    /**
     * Default constructor required for JSON deserialization
     */
    public DocumentRequest() {
    }

    public DocumentRequest(String title, String content, List<String> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    // -------------------------
    // Getters and Setters
    // -------------------------

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
     * Full document body used for indexing and search.
     */
    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    /**
     * Optional metadata tags.
     * Example: ["search", "platform", "knowledge"]
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
