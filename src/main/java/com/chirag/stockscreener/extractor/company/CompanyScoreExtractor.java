package com.chirag.stockscreener.extractor.company;

import com.chirag.stockscreener.model.CompanyDetail;
import com.chirag.stockscreener.model.CompanyQuarterlyResult;
import com.chirag.stockscreener.model.CompanyScore;
import com.chirag.stockscreener.model.CompanyTechnicalHistory;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class CompanyScoreExtractor implements Function<CompanyDetail, CompanyScore> {

    private static final double MAX_VALUE_HIGHER_THAN_DMA_SCORE = 5.0;
    private static final double MAX_PRICE_ABOVE_DMA_PERCENTAGE_SCORE = 5.0;
    private static final double MAX_VOLUME_ABOVE_AVERAGE_SCORE = 5.0;
    private static final double MAX_SALES_GROWTH_PERCENTILE_SCORE = 20.0;
    private static final double MAX_OPERATING_PROFIT_MARGIN_PERCENTILE_SCORE = 20.0;
    private static final double MAX_NET_PROFIT_MARGIN_PERCENTILE_SCORE = 10.0;

    @Override
    public CompanyScore apply(CompanyDetail companyDetail) {
        // Extract necessary data from CompanyDetail
        CompanyTechnicalHistory latestTechnicalAttributes = companyDetail.getLatestTechnicalAttributes();
        List<CompanyTechnicalHistory> technicalHistories = companyDetail.getTechnicalHistory();
        CompanyQuarterlyResult latestQuarterlyResult = companyDetail.getLatestQuarterlyResult();
        List<CompanyQuarterlyResult> quarterlyResults = companyDetail.getQuarterlyResults();

        // Calculate various scores and set in CompanyScore.Builder
        CompanyScore.Builder builder = new CompanyScore.Builder();

        builder.setPriceHigherThanDma50(getHigherThanDmaScore(latestTechnicalAttributes.getPrice(), latestTechnicalAttributes.getDma50(), MAX_VALUE_HIGHER_THAN_DMA_SCORE));
        builder.setPriceHigherThanDma100(getHigherThanDmaScore(latestTechnicalAttributes.getPrice(), latestTechnicalAttributes.getDma100(), MAX_VALUE_HIGHER_THAN_DMA_SCORE));
        builder.setPriceHigherThanDma200(getHigherThanDmaScore(latestTechnicalAttributes.getPrice(), latestTechnicalAttributes.getDma200(), MAX_VALUE_HIGHER_THAN_DMA_SCORE));
        builder.setDma50HigherThanDma100(getHigherThanDmaScore(latestTechnicalAttributes.getDma50(), latestTechnicalAttributes.getDma100(), MAX_VALUE_HIGHER_THAN_DMA_SCORE));
        builder.setDma50HigherThanDma200(getHigherThanDmaScore(latestTechnicalAttributes.getDma50(), latestTechnicalAttributes.getDma200(), MAX_VALUE_HIGHER_THAN_DMA_SCORE));
        builder.setDma100HigherThanDma200(getHigherThanDmaScore(latestTechnicalAttributes.getDma100(), latestTechnicalAttributes.getDma200(), MAX_VALUE_HIGHER_THAN_DMA_SCORE));
        builder.setPricePercentageAboveDma50(getPercentageAboveDma(latestTechnicalAttributes.getPrice(), latestTechnicalAttributes.getDma50(), MAX_PRICE_ABOVE_DMA_PERCENTAGE_SCORE));
        builder.setPricePercentageAboveDma100(getPercentageAboveDma(latestTechnicalAttributes.getPrice(), latestTechnicalAttributes.getDma100(), MAX_PRICE_ABOVE_DMA_PERCENTAGE_SCORE));
        builder.setPricePercentageAboveDma200(getPercentageAboveDma(latestTechnicalAttributes.getPrice(), latestTechnicalAttributes.getDma200(), MAX_PRICE_ABOVE_DMA_PERCENTAGE_SCORE));
        builder.setWeeklyVolumeHigherThanYearlyVolume(getVolumeScore(technicalHistories, MAX_VOLUME_ABOVE_AVERAGE_SCORE));

        builder.setSalesGrowthPercentile(getPercentileScore(
                latestQuarterlyResult.getSalesGrowthYoY(),
                quarterlyResults,
                CompanyQuarterlyResult::getSalesGrowthYoY,
                MAX_SALES_GROWTH_PERCENTILE_SCORE));
        builder.setOperatingProfitMarginPercentile(getPercentileScore(
                latestQuarterlyResult.getOperatingProfitMargin(),
                quarterlyResults,
                CompanyQuarterlyResult::getOperatingProfitMargin,
                MAX_OPERATING_PROFIT_MARGIN_PERCENTILE_SCORE));
        builder.setNetProfitMarginPercentile(getPercentileScore(
                latestQuarterlyResult.getNetProfitMargin(),
                quarterlyResults,
                CompanyQuarterlyResult::getNetProfitMargin,
                MAX_NET_PROFIT_MARGIN_PERCENTILE_SCORE));

        // Build and return CompanyScore
        return builder.build();
    }

    private double getHigherThanDmaScore(Double value, Double dma, double maxScore) {
        return (value != null && dma != null && value > dma) ? maxScore : 0.0;
    }

    private double getPercentageAboveDma(Double value, Double dma, double maxScore) {
        if (value == null || dma == null) {
            return 0.0;
        }
        double percentageAboveDma = ((value - dma) / dma) * 100;
        double score = maxScore - (percentageAboveDma * maxScore / 100);
        return Math.max(score, 0.0);
    }

    private double getVolumeScore(List<CompanyTechnicalHistory> technicalHistories, double maxScore) {
        //  Calculate average weekly volume based on last 5 records from list
        double weeklyVolume = technicalHistories.stream()
                .skip(Math.max(0, technicalHistories.size() - 5))
                .map(CompanyTechnicalHistory::getVolume)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        // Calculate average yearly volume based on last all records from list
        double yearlyVolume = technicalHistories.stream()
                .map(CompanyTechnicalHistory::getVolume)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return (weeklyVolume > yearlyVolume) ? maxScore : 0.0;
    }

    private double getPercentileScore(Double latestValue, List<CompanyQuarterlyResult> quarterlyResults, Function<CompanyQuarterlyResult, Double> valueGetter, double maxScore) {
        List<Double> allValues = quarterlyResults.stream()
                .map(valueGetter)
                .filter(Objects::nonNull)
                .toList();

        if (latestValue == null || allValues.isEmpty()) {
            return 0.0;
        }

        // Percentile against all quarterly results
        long countLowerValueQuarters = allValues.stream().filter(value -> value != null && value <= latestValue).count();
        double percentileByCount = ((double) countLowerValueQuarters / allValues.size()) * 100;

        // Percentage against min & max of all quarterly results
        double min = allValues.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double max = allValues.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double percentageByRange = 0;
        if (min != max) {
            percentageByRange = ((latestValue - min) / (max - min)) * 100;
        }

        // Take the higher of both percentiles
        double percentile = Math.max(percentileByCount, percentageByRange);

        // Scale percentile to maxScore
        return percentile * (maxScore / 100);
    }
}
