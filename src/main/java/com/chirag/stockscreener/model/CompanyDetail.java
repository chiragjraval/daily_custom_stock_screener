package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Model representing the complete result for a company including metadata, details, and technical history
 */
public class CompanyDetail {

    @JsonProperty("metadata")
    private CompanyMetadata metadata;

    @JsonProperty("attributes")
    private CompanyAttributes attributes;

    @JsonProperty("technical_history")
    private List<CompanyTechnicalHistory> technicalHistory;

    @JsonProperty("quarterly_results")
    private List<CompanyQuarterlyResult> quarterlyResults;

    // Constructors
    public CompanyDetail() {
    }

    public CompanyDetail(CompanyMetadata metadata, CompanyAttributes attributes, List<CompanyTechnicalHistory> technicalHistory, List<CompanyQuarterlyResult> quarterlyResults) {
        this.metadata = metadata;
        this.attributes = attributes;
        this.technicalHistory = technicalHistory;
        this.quarterlyResults = quarterlyResults;
    }

    // Getters and Setters
    public CompanyMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CompanyMetadata metadata) {
        this.metadata = metadata;
    }

    public CompanyAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(CompanyAttributes attributes) {
        this.attributes = attributes;
    }

    public List<CompanyTechnicalHistory> getTechnicalHistory() {
        return technicalHistory;
    }

    public void setTechnicalHistory(List<CompanyTechnicalHistory> technicalHistory) { this.technicalHistory = technicalHistory; }

    public List<CompanyQuarterlyResult> getQuarterlyResults() { return quarterlyResults; }

    public void setQuarterlyResults(List<CompanyQuarterlyResult> quarterlyResults) { this.quarterlyResults = quarterlyResults; }

    @Override
    public String toString() {
        return "CompanyDetail{" +
                "metadata=" + metadata +
                ", attributes=" + attributes +
                ", technicalHistory=" + technicalHistory +
                '}';
    }
}

