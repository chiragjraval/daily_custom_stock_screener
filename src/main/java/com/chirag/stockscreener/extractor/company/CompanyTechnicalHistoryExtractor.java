package com.chirag.stockscreener.extractor.company;

import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.model.CompanyTechnicalHistory;
import com.chirag.stockscreener.util.HttpClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class CompanyTechnicalHistoryExtractor implements Function<CompanyMetadata, List<CompanyTechnicalHistory>> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyTechnicalHistoryExtractor.class);

    private final HttpClientUtil httpClientUtil;

    public CompanyTechnicalHistoryExtractor(HttpClientUtil httpClientUtil) {
        this.httpClientUtil = httpClientUtil;
    }

    @Override
    public List<CompanyTechnicalHistory> apply(CompanyMetadata companyMetadata) {
        List<CompanyTechnicalHistory> results = new ArrayList<>();

        try {
            // Fetch technical history with link from metadata.screenerTechnicalHistoryLink attribute
            // it returns JSON for which sample is available in resources/sample-data/ScreenerTechnicalHistoryApi.json
            String json = httpClientUtil.fetchHtml(companyMetadata.getScreenerTechnicalHistoryLink());
            if (json == null) {
                logger.error("Failed to fetch technical history for company: {}", companyMetadata.getCompanyCode());
                return results;
            }

            // Parse JSON response
            ObjectMapper mapper = new JsonMapper();
            JsonNode root = mapper.readTree(json);

            // Transform JSON data into CompanyTechnicalHistory objects
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
                LocalDate date = entry.getKey();
                Map<String, Double> metrics = entry.getValue();
                CompanyTechnicalHistory history = new CompanyTechnicalHistory(
                        date,
                        metrics.get("price"),
                        metrics.get("dma50"),
                        metrics.get("dma200"),
                        metrics.get("volume")
                );
                results.add(history);
            }

            // Calculate 100-day moving average (dma100)
            double sum100 = 0;
            for (int i=0; i<results.size(); i++) {
                CompanyTechnicalHistory result = results.get(i);
                sum100 += result.getPrice();
                if (i >= 100) {
                    sum100 -= results.get(i-100).getPrice();
                    result.setDma100(sum100/100);
                }
            }

            // Log success
            logger.info("Successfully parsed technical history for company: {}", companyMetadata.getCompanyCode());
        } catch (JsonProcessingException e) {
            // Handle JSON parsing exceptions
            logger.error("Error parsing technical history JSON for company: {}", companyMetadata.getCompanyCode(), e);
        }

        return results;
    }
}
