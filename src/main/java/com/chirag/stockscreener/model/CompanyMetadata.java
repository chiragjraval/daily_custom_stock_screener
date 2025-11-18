package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing metadata about a company from screener.in
 */
public class CompanyMetadata {

    @JsonProperty("screener_company_id")
    private String screenerCompanyId;

    @JsonProperty("company_code")
    private String companyCode;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("screener_company_link")
    private String screenerCompanyLink;

    @JsonProperty("screener_price_history_link")
    private String screenerPriceHistoryLink;

    @JsonProperty("screener_quarterly_results_link")
    private String screenerQuarterlyResultsLink;

    public CompanyMetadata(String screenerCompanyId, String companyCode, String companyName) {
        this.screenerCompanyId = screenerCompanyId;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.screenerCompanyLink = "https://www.screener.in/company/" + companyCode;
        this.screenerPriceHistoryLink = "https://www.screener.in/api/company/" + screenerCompanyId +
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

    public String getScreenerPriceHistoryLink() {
        return screenerPriceHistoryLink;
    }

    public void setScreenerPriceHistoryLink(String screenerPriceHistoryLink) {
        this.screenerPriceHistoryLink = screenerPriceHistoryLink;
    }

    public String getScreenerQuarterlyResultsLink() {
        return screenerQuarterlyResultsLink;
    }

    public void setScreenerQuarterlyResultsLink(String screenerQuarterlyResultsLink) {
        this.screenerQuarterlyResultsLink = screenerQuarterlyResultsLink;
    }

    @Override
    public String toString() {
        return "CompanyMetadata{" +
                "screenerCompanyId='" + screenerCompanyId + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", screenerCompanyLink='" + screenerCompanyLink + '\'' +
                ", screenerPriceHistoryLink='" + screenerPriceHistoryLink + '\'' +
                ", screenerQuarterlyResultsLink='" + screenerQuarterlyResultsLink + '\'' +
                '}';
    }
}

