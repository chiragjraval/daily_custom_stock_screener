package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Model representing quarterly result data for a company
 */
public class CompanyQuarterlyResult {

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("sales")
    private Double sales;

    @JsonProperty("salesGrowthYoY")
    private Double salesGrowthYoY;

    @JsonProperty("grossProfitMargin")
    private Double grossProfitMargin;

    @JsonProperty("grossProfit")
    private Double grossProfit;

    @JsonProperty("operatingProfitMargin")
    private Double operatingProfitMargin;

    @JsonProperty("operatingProfit")
    private Double operatingProfit;

    @JsonProperty("netProfitMargin")
    private Double netProfitMargin;

    @JsonProperty("netProfit")
    private Double netProfit;

    public CompanyQuarterlyResult(LocalDate date, Double sales, Double grossProfitMargin,
                                  Double grossProfit, Double operatingProfitMargin, Double operatingProfit,
                                  Double netProfitMargin, Double netProfit) {
        this.date = date;
        this.sales = sales;
        this.grossProfitMargin = grossProfitMargin;
        this.grossProfit = grossProfit;
        this.operatingProfitMargin = operatingProfitMargin;
        this.operatingProfit = operatingProfit;
        this.netProfitMargin = netProfitMargin;
        this.netProfit = netProfit;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Double getSalesGrowthYoY() {
        return salesGrowthYoY;
    }

    public void setSalesGrowthYoY(Double salesGrowthYoY) {
        this.salesGrowthYoY = salesGrowthYoY;
    }

    public Double getGrossProfitMargin() {
        return grossProfitMargin;
    }

    public void setGrossProfitMargin(Double grossProfitMargin) {
        this.grossProfitMargin = grossProfitMargin;
    }

    public Double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(Double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Double getOperatingProfitMargin() {
        return operatingProfitMargin;
    }

    public void setOperatingProfitMargin(Double operatingProfitMargin) {
        this.operatingProfitMargin = operatingProfitMargin;
    }

    public Double getOperatingProfit() {
        return operatingProfit;
    }

    public void setOperatingProfit(Double operatingProfit) {
        this.operatingProfit = operatingProfit;
    }

    public Double getNetProfitMargin() {
        return netProfitMargin;
    }

    public void setNetProfitMargin(Double netProfitMargin) {
        this.netProfitMargin = netProfitMargin;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    @Override
    public String toString() {
        return "CompanyQuarterlyResult{" +
                "date=" + date +
                ", sales=" + sales +
                ", salesGrowthYoY=" + salesGrowthYoY +
                ", grossProfitMargin=" + grossProfitMargin +
                ", grossProfit=" + grossProfit +
                ", operatingProfitMargin=" + operatingProfitMargin +
                ", operatingProfit=" + operatingProfit +
                ", netProfitMargin=" + netProfitMargin +
                ", netProfit=" + netProfit +
                '}';
    }
}
