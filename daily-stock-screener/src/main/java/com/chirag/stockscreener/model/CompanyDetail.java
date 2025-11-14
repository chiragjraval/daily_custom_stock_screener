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

    @JsonProperty("industry")
    private String industry;

    @JsonProperty("market_cap")
    private String marketCap;

    @JsonProperty("pe_ratio")
    private String peRatio;

    @JsonProperty("pb_ratio")
    private String pbRatio;

    @JsonProperty("dividend_yield")
    private String dividendYield;

    @JsonProperty("debt_to_equity")
    private String debtToEquity;

    @JsonProperty("roe")
    private String roe;

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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

    public String getPbRatio() {
        return pbRatio;
    }

    public void setPbRatio(String pbRatio) {
        this.pbRatio = pbRatio;
    }

    public String getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(String dividendYield) {
        this.dividendYield = dividendYield;
    }

    public String getDebtToEquity() {
        return debtToEquity;
    }

    public void setDebtToEquity(String debtToEquity) {
        this.debtToEquity = debtToEquity;
    }

    public String getRoe() {
        return roe;
    }

    public void setRoe(String roe) {
        this.roe = roe;
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
                ", industry='" + industry + '\'' +
                ", marketCap='" + marketCap + '\'' +
                ", peRatio='" + peRatio + '\'' +
                ", pbRatio='" + pbRatio + '\'' +
                ", dividendYield='" + dividendYield + '\'' +
                ", debtToEquity='" + debtToEquity + '\'' +
                ", roe='" + roe + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

