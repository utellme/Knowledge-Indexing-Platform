package com.myproject.knowledge.model;


public class DocumentResponse {

    private Long id;
    private String title;
    private double score;

    /**
     * Default constructor
     */
    public DocumentResponse() {
    }

    public DocumentResponse(Long id, String title, double score) {
        this.id = id;
        this.title = title;
        this.score = score;
    }

    // -------------------------
    // Getters and Setters
    // -------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Document title (display-only)
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Relevance score returned by full-text search.
     */
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
