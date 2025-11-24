package com.chirag.stockscreener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyScore {

    @JsonProperty("priceHigherThanDma50")
    private Double priceHigherThanDma50;

    @JsonProperty("priceHigherThanDma100")
    private Double priceHigherThanDma100;

    @JsonProperty("priceHigherThanDma200")
    private Double priceHigherThanDma200;

    @JsonProperty("dma50HigherThanDma100")
    private Double dma50HigherThanDma100;

    @JsonProperty("dma50HigherThanDma200")
    private Double dma50HigherThanDma200;

    @JsonProperty("dma100HigherThanDma200")
    private Double dma100HigherThanDma200;

    @JsonProperty("pricePercentageAboveDma50")
    private Double pricePercentageAboveDma50;

    @JsonProperty("pricePercentageAboveDma100")
    private Double pricePercentageAboveDma100;

    @JsonProperty("pricePercentageAboveDma200")
    private Double pricePercentageAboveDma200;

    @JsonProperty("weeklyVolumeHigherThanYearlyVolume")
    private Double weeklyVolumeHigherThanYearlyVolume;

    @JsonProperty("salesGrowthPercentile")
    private Double salesGrowthPercentile;

    @JsonProperty("operatingProfitMarginPercentile")
    private Double operatingProfitMarginPercentile;

    @JsonProperty("netProfitMarginPercentile")
    private Double netProfitMarginPercentile;

    @JsonProperty("totalTechnicalScore")
    private Double totalTechnicalScore;

    @JsonProperty("totalFundamentalScore")
    private Double totalFundamentalScore;

    @JsonProperty("totalScore")
    private Double totalScore;

    public Double getPriceHigherThanDma50() { return priceHigherThanDma50; }

    public Double getPriceHigherThanDma100() { return priceHigherThanDma100; }

    public Double getPriceHigherThanDma200() { return priceHigherThanDma200; }

    public Double getDma50HigherThanDma100() { return dma50HigherThanDma100; }

    public Double getDma50HigherThanDma200() { return dma50HigherThanDma200; }

    public Double getDma100HigherThanDma200() { return dma100HigherThanDma200; }

    public Double getPricePercentageAboveDma50() { return pricePercentageAboveDma50; }

    public Double getPricePercentageAboveDma100() { return pricePercentageAboveDma100; }

    public Double getPricePercentageAboveDma200() { return pricePercentageAboveDma200; }

    public Double getWeeklyVolumeHigherThanYearlyVolume() { return weeklyVolumeHigherThanYearlyVolume; }

    public Double getSalesGrowthPercentile() { return salesGrowthPercentile; }

    public Double getOperatingProfitMarginPercentile() { return operatingProfitMarginPercentile; }

    public Double getNetProfitMarginPercentile() { return netProfitMarginPercentile; }

    public Double getTotalTechnicalScore() { return totalTechnicalScore; }

    public Double getTotalFundamentalScore() { return totalFundamentalScore; }

    public Double getTotalScore() { return totalScore; }

    public static class Builder {
        private final CompanyScore companyScore;

        public Builder() {
            companyScore = new CompanyScore();
        }

        public Builder setPriceHigherThanDma50(Double value) {
            companyScore.priceHigherThanDma50 = value;
            return this;
        }

        public Builder setPriceHigherThanDma100(Double value) {
            companyScore.priceHigherThanDma100 = value;
            return this;
        }

        public Builder setPriceHigherThanDma200(Double value) {
            companyScore.priceHigherThanDma200 = value;
            return this;
        }

        public Builder setDma50HigherThanDma100(Double value) {
            companyScore.dma50HigherThanDma100 = value;
            return this;
        }

        public Builder setDma50HigherThanDma200(Double value) {
            companyScore.dma50HigherThanDma200 = value;
            return this;
        }

        public Builder setDma100HigherThanDma200(Double value) {
            companyScore.dma100HigherThanDma200 = value;
            return this;
        }

        public Builder setPricePercentageAboveDma50(Double value) {
            companyScore.pricePercentageAboveDma50 = value;
            return this;
        }

        public Builder setPricePercentageAboveDma100(Double value) {
            companyScore.pricePercentageAboveDma100 = value;
            return this;
        }

        public Builder setPricePercentageAboveDma200(Double value) {
            companyScore.pricePercentageAboveDma200 = value;
            return this;
        }

        public Builder setWeeklyVolumeHigherThanYearlyVolume(Double value) {
            companyScore.weeklyVolumeHigherThanYearlyVolume = value;
            return this;
        }

        public Builder setSalesGrowthPercentile(Double value) {
            companyScore.salesGrowthPercentile = value;
            return this;
        }

        public Builder setOperatingProfitMarginPercentile(Double value) {
            companyScore.operatingProfitMarginPercentile = value;
            return this;
        }

        public Builder setNetProfitMarginPercentile(Double value) {
            companyScore.netProfitMarginPercentile = value;
            return this;
        }

        public CompanyScore build() {
            companyScore.totalTechnicalScore =
                (companyScore.priceHigherThanDma50 != null ? companyScore.priceHigherThanDma50 : 0) +
                (companyScore.priceHigherThanDma100 != null ? companyScore.priceHigherThanDma100 : 0) +
                (companyScore.priceHigherThanDma200 != null ? companyScore.priceHigherThanDma200 : 0) +
                (companyScore.dma50HigherThanDma100 != null ? companyScore.dma50HigherThanDma100 : 0) +
                (companyScore.dma50HigherThanDma200 != null ? companyScore.dma50HigherThanDma200 : 0) +
                (companyScore.dma100HigherThanDma200 != null ? companyScore.dma100HigherThanDma200 : 0) +
                (companyScore.pricePercentageAboveDma50 != null ? companyScore.pricePercentageAboveDma50 : 0) +
                (companyScore.pricePercentageAboveDma100 != null ? companyScore.pricePercentageAboveDma100 : 0) +
                (companyScore.pricePercentageAboveDma200 != null ? companyScore.pricePercentageAboveDma200 : 0) +
                (companyScore.weeklyVolumeHigherThanYearlyVolume != null ? companyScore.weeklyVolumeHigherThanYearlyVolume : 0);

            companyScore.totalFundamentalScore =
                (companyScore.salesGrowthPercentile != null ? companyScore.salesGrowthPercentile : 0) +
                (companyScore.operatingProfitMarginPercentile != null ? companyScore.operatingProfitMarginPercentile : 0) +
                (companyScore.netProfitMarginPercentile != null ? companyScore.netProfitMarginPercentile : 0);

            companyScore.totalScore = companyScore.totalTechnicalScore + companyScore.totalFundamentalScore;

            return companyScore;
        }
    }
}
