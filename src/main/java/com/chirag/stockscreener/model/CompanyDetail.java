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

    @JsonProperty("technicalHistory")
    private List<CompanyTechnicalHistory> technicalHistory;

    @JsonProperty("latestTechnicalAttributes")
    private CompanyTechnicalHistory latestTechnicalAttributes;

    @JsonProperty("quarterlyResults")
    private List<CompanyQuarterlyResult> quarterlyResults;

    @JsonProperty("latestQuarterlyResult")
    private CompanyQuarterlyResult latestQuarterlyResult;

    @JsonProperty("companyScore")
    private CompanyScore companyScore;

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

    public CompanyTechnicalHistory getLatestTechnicalAttributes() { return latestTechnicalAttributes; }

    public void setLatestTechnicalAttributes(CompanyTechnicalHistory latestTechnicalAttributes) { this.latestTechnicalAttributes = latestTechnicalAttributes; }

    public List<CompanyQuarterlyResult> getQuarterlyResults() { return quarterlyResults; }

    public void setQuarterlyResults(List<CompanyQuarterlyResult> quarterlyResults) { this.quarterlyResults = quarterlyResults; }

    public CompanyQuarterlyResult getLatestQuarterlyResult() { return latestQuarterlyResult; }

    public void setLatestQuarterlyResult(CompanyQuarterlyResult latestQuarterlyResult) { this.latestQuarterlyResult = latestQuarterlyResult; }

    public CompanyScore getCompanyScore() { return companyScore; }

    public void setCompanyScore(CompanyScore companyScore) { this.companyScore = companyScore; }

    @Override
    public String toString() {
        return "CompanyDetail{" +
                "metadata=" + metadata +
                ", attributes=" + attributes +
                ", technicalHistory=" + technicalHistory +
                ", latestTechnicalAttributes=" + latestTechnicalAttributes +
                ", quarterlyResults=" + quarterlyResults +
                ", latestQuarterlyResult=" + latestQuarterlyResult +
                ", companyScore=" + companyScore +
                '}';
    }
}

