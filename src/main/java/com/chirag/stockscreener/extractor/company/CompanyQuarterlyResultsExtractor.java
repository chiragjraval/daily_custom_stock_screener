package com.chirag.stockscreener.extractor.company;

import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.model.CompanyQuarterlyResult;
import com.chirag.stockscreener.util.HttpClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class CompanyQuarterlyResultsExtractor implements Function<CompanyMetadata, List<CompanyQuarterlyResult>> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyQuarterlyResultsExtractor.class);

    private final HttpClientUtil httpClientUtil;

    public CompanyQuarterlyResultsExtractor(HttpClientUtil httpClientUtil) {
        this.httpClientUtil = httpClientUtil;
    }

    @Override
    public List<CompanyQuarterlyResult> apply(CompanyMetadata companyMetadata) {
        List<CompanyQuarterlyResult> results = new ArrayList<>();

        try {
            // Fetch technical history with link from metadata.screenerQuarterlyResultsLink attribute
            // it returns JSON for which sample is available in resources/sample-data/ScreenerQuarterlyResultsApi.json
            String json = httpClientUtil.fetch(companyMetadata.getScreenerQuarterlyResultsLink());
            if (json == null) {
                logger.error("Failed to fetch technical history for company: {}", companyMetadata.getCompanyCode());
                return results;
            }

            // Parse JSON response
            ObjectMapper mapper = new JsonMapper();
            JsonNode root = mapper.readTree(json);

            // Transform JSON data into metric map
            Map<LocalDate, Map<String, Double>> datasets = new TreeMap<>();
            root.get("datasets").forEach(dataset -> {
                String metric = dataset.get("metric").asText().toLowerCase();
                dataset.get("values").forEach(value -> {
                    LocalDate date = LocalDate.parse(value.get(0).asText());
                    Double val = value.get(1).isNull() ? null : value.get(1).asDouble();
                    datasets.putIfAbsent(date, new HashMap<>());
                    datasets.get(date).put(metric, val);
                });
            });

            // Create CompanyTechnicalHistory objects
            for (Map.Entry<LocalDate, Map<String, Double>> entry : datasets.entrySet()) {
                CompanyQuarterlyResult quarterlyResult = getCompanyQuarterlyResult(entry);
                results.add(quarterlyResult);
            }

            // Calculate YoY Sales growth for each quarter
            for (int i = 4; i < results.size(); i++) {
                CompanyQuarterlyResult current = results.get(i);
                CompanyQuarterlyResult previousYear = results.get(i - 4);
                if (previousYear.getSales() != 0) {
                    double yoyGrowth = ((current.getSales() - previousYear.getSales()) / previousYear.getSales()) * 100;
                    current.setSalesGrowthYoY(yoyGrowth);
                } else if (current.getSales() != 0) {
                    current.setSalesGrowthYoY(100.0);
                } else {
                    current.setSalesGrowthYoY(0.0);
                }
            }
        } catch (JsonProcessingException e) {
            // Handle JSON parsing exceptions
            logger.error("Error parsing technical history JSON for company: {}", companyMetadata.getCompanyCode(), e);
        }

        return results;
    }

    @NotNull
    private static CompanyQuarterlyResult getCompanyQuarterlyResult(Map.Entry<LocalDate, Map<String, Double>> entry) {
        LocalDate date = entry.getKey();
        Map<String, Double> metrics = entry.getValue();

        Double sales = metrics.get("quarter sales") != null ? metrics.get("quarter sales") : 0.0;
        Double gpm = metrics.get("gpm") != null ? metrics.get("gpm") : 0.0;
        Double opm = metrics.get("opm") != null ? metrics.get("opm") : 0.0;
        Double npm = metrics.get("npm") != null ? metrics.get("npm") : 0.0;

        return new CompanyQuarterlyResult(
                date,
                sales,
                gpm,
                sales * gpm / 100,
                opm,
                sales * opm / 100,
                npm,
                sales * npm / 100
        );
    }
}
