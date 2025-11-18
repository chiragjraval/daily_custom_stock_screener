package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model representing the complete result for a company including metadata, details, and technical history
 */
public class CompanyResult {

    @JsonProperty("metadata")
    private CompanyMetadata metadata;

    @JsonProperty("detail")
    private CompanyDetail detail;

    @JsonProperty("technical_history")
    private List<CompanyTechnicalHistory> technicalHistory;

    // Constructors
    public CompanyResult() {
    }

    public CompanyResult(CompanyMetadata metadata, CompanyDetail detail, List<CompanyTechnicalHistory> technicalHistory) {
        this.metadata = metadata;
        this.detail = detail;
        this.technicalHistory = technicalHistory;
    }

    // Getters and Setters
    public CompanyMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CompanyMetadata metadata) {
        this.metadata = metadata;
    }

    public CompanyDetail getDetail() {
        return detail;
    }

    public void setDetail(CompanyDetail detail) {
        this.detail = detail;
    }

    public List<CompanyTechnicalHistory> getTechnicalHistory() {
        return technicalHistory;
    }

    public void setTechnicalHistory(List<CompanyTechnicalHistory> technicalHistory) {
        this.technicalHistory = technicalHistory;
    }

    @Override
    public String toString() {
        return "CompanyResult{" +
                "metadata=" + metadata +
                ", detail=" + detail +
                ", technicalHistory=" + technicalHistory +
                '}';
    }
}

