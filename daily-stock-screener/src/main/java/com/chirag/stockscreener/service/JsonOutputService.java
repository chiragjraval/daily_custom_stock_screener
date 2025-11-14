package com.chirag.stockscreener.service;

import com.chirag.stockscreener.model.CompanyResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for writing company results to JSON files
 */
public class JsonOutputService {

    private static final Logger logger = LoggerFactory.getLogger(JsonOutputService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Output company results to JSON files
     * @param companies List of CompanyResult to output
     * @param outputDirectory Directory to write JSON files
     */
    public static void outputToJson(List<CompanyResult> companies, String outputDirectory) {
        try {
            Path outputPath = Paths.get(outputDirectory);
            Files.createDirectories(outputPath);

            logger.info("Output directory created/exists: {}", outputPath.toAbsolutePath());

            // Write individual company files
            int successCount = 0;
            for (CompanyResult company : companies) {
                try {
                    String fileName = company.getMetadata().getCompanyCode() + ".json";
                    Path filePath = outputPath.resolve(fileName);

                    objectMapper.writeValue(filePath.toFile(), company);
                    successCount++;
                    logger.debug("Written company JSON: {}", fileName);
                } catch (Exception e) {
                    logger.error("Error writing company result to JSON", e);
                }
            }

            // Write summary file
            writeSummaryFile(companies, outputPath);

            logger.info("Successfully output {} company files to {}", successCount, outputPath.toAbsolutePath());
        } catch (IOException e) {
            logger.error("Error creating output directory", e);
        }
    }

    /**
     * Write a summary file with all company metadata
     * @param companies List of CompanyResult
     * @param outputPath Output directory path
     */
    private static void writeSummaryFile(List<CompanyResult> companies, Path outputPath) {
        try {
            SummaryData summary = new SummaryData(
                    companies.size(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    companies.stream().map(c -> c.getMetadata()).toList()
            );

            Path summaryPath = outputPath.resolve("summary.json");
            objectMapper.writeValue(summaryPath.toFile(), summary);
            logger.info("Written summary file: {}", summaryPath.toAbsolutePath());
        } catch (IOException e) {
            logger.error("Error writing summary file", e);
        }
    }

    /**
     * Summary data structure for JSON output
     */
    public static class SummaryData {
        public int totalCompanies;
        public String generatedAt;
        public List<?> companies;

        public SummaryData(int totalCompanies, String generatedAt, List<?> companies) {
            this.totalCompanies = totalCompanies;
            this.generatedAt = generatedAt;
            this.companies = companies;
        }
    }
}

