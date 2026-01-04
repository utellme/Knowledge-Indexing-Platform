package com.myproject.knowledge.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {

    @JsonProperty("title")
    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title must be <= 255 characters")
    private String title;

    @JsonProperty("content")
    @NotBlank(message = "content is required")
    private String content;

    @JsonProperty("tags")
    private List<String> tags;

    // Default constructor required by Jackson
    public Document() {}

    public Document(String title, String content, List<String> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    // -------- Getters / Setters --------
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
