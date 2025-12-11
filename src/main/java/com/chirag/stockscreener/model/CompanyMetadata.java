package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing metadata about a company from screener.in
 */
public class CompanyMetadata {

    @JsonProperty("screenerCompanyId")
    private String screenerCompanyId;

    @JsonProperty("companyCode")
    private String companyCode;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("screenerCompanyLink")
    private String screenerCompanyLink;

    @JsonProperty("screenerTechnicalHistoryLink")
    private String screenerTechnicalHistoryLink;

    @JsonProperty("screenerQuarterlyResultsLink")
    private String screenerQuarterlyResultsLink;

    @JsonProperty("attributes")
    private CompanyAttributes attributes;

    @JsonProperty("companyScore")
    private CompanyScore companyScore;

    public CompanyMetadata(String screenerCompanyId, String companyCode, String companyName, String screenerCompanyLink) {
        this.screenerCompanyId = screenerCompanyId;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.screenerCompanyLink = screenerCompanyLink;
        this.screenerTechnicalHistoryLink = "https://www.screener.in/api/company/" + screenerCompanyId +
                                        "/chart/?q=Price-DMA50-DMA200-Volume&days=365";
        this.screenerQuarterlyResultsLink = "https://www.screener.in/api/company/" + screenerCompanyId +
                                            "/chart/?q=GPM-OPM-NPM-Quarter+Sales&days=10000";
    }

    // Getters and Setters
    public String getScreenerCompanyId() {
        return screenerCompanyId;
    }

    public void setScreenerCompanyId(String screenerCompanyId) {
        this.screenerCompanyId = screenerCompanyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getScreenerCompanyLink() {
        return screenerCompanyLink;
    }

    public void setScreenerCompanyLink(String screenerCompanyLink) {
        this.screenerCompanyLink = screenerCompanyLink;
    }

    public String getScreenerTechnicalHistoryLink() {
        return screenerTechnicalHistoryLink;
    }

    public void setScreenerTechnicalHistoryLink(String screenerTechnicalHistoryLink) { this.screenerTechnicalHistoryLink = screenerTechnicalHistoryLink; }

    public String getScreenerQuarterlyResultsLink() {
        return screenerQuarterlyResultsLink;
    }

    public void setScreenerQuarterlyResultsLink(String screenerQuarterlyResultsLink) { this.screenerQuarterlyResultsLink = screenerQuarterlyResultsLink; }

    public CompanyAttributes getAttributes() { return attributes; }

    public void setAttributes(CompanyAttributes attributes) { this.attributes = attributes; }

    public CompanyScore getCompanyScore() { return companyScore; }

    public void setCompanyScore(CompanyScore companyScore) { this.companyScore = companyScore; }

    @Override
    public String toString() {
        return "CompanyMetadata{" +
                "screenerCompanyId='" + screenerCompanyId + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", screenerCompanyLink='" + screenerCompanyLink + '\'' +
                ", screenerTechnicalHistoryLink='" + screenerTechnicalHistoryLink + '\'' +
                ", screenerQuarterlyResultsLink='" + screenerQuarterlyResultsLink + '\'' +
                ", attributes=" + attributes +
                ", companyScore=" + companyScore +
                '}';
    }
}