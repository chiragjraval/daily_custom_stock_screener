package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing detailed information about a company
 */
public class CompanyAttributes {

    @JsonProperty("companyCode")
    private String companyCode;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("marketCap")
    private Double marketCap;

    @JsonProperty("peRatio")
    private Double peRatio;

    @JsonProperty("description")
    private String description;

    // Constructors
    public CompanyAttributes() {
        this.companyCode = "";
        this.companyName = "";
        this.marketCap = 0.0;
        this.peRatio = 0.0;
        this.description = "";
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

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(Double peRatio) {
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
        return "CompanyAttributes{" +
                "companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", marketCap='" + marketCap + '\'' +
                ", peRatio='" + peRatio + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

