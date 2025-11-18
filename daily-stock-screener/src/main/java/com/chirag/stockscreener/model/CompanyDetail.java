package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing detailed information about a company
 */
public class CompanyDetail {

    @JsonProperty("company_code")
    private String companyCode;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("market_cap")
    private String marketCap;

    @JsonProperty("pe_ratio")
    private String peRatio;

    @JsonProperty("description")
    private String description;

    // Constructors
    public CompanyDetail() {
    }

    // Getters and Setters
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

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(String peRatio) {
        this.peRatio = peRatio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CompanyDetail{" +
                "companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", marketCap='" + marketCap + '\'' +
                ", peRatio='" + peRatio + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

