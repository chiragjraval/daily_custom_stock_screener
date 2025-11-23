package com.chirag.stockscreener.service;

import com.chirag.stockscreener.model.CompanyDetail;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for writing company results to JSON files
 */
public class JsonOutputService {

    private static final Logger logger = LoggerFactory.getLogger(JsonOutputService.class);

    private static final SimpleModule doubleSerializerModule = new SimpleModule().addSerializer(Double.class, new JsonSerializer<Double>() {
        private static final DecimalFormat df = new DecimalFormat("0.00");

        @Override
        public void serialize(Double value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            // Format with 2 decimals, no scientific notation
            jsonGenerator.writeNumber(df.format(value));
        }
    });

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(doubleSerializerModule)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Output company results to JSON files
     * @param companies List of CompanyDetail to output
     * @param outputDirectory Directory to write JSON files
     */
    public static void outputToJson(List<CompanyDetail> companies, String outputDirectory) {
        try {
            String detailsOutputDirectory = outputDirectory + "/details";
            Path outputPath = Paths.get(detailsOutputDirectory);
            Files.createDirectories(outputPath);

            logger.info("Output directory created/exists: {}", outputPath.toAbsolutePath());

            // Write individual company files
            int successCount = 0;
            for (CompanyDetail company : companies) {
                String fileName = company.getMetadata().getCompanyCode() + ".json";
                Path filePath = outputPath.resolve(fileName);

                try (java.io.BufferedWriter writer = Files.newBufferedWriter(
                        filePath,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {
                    objectMapper.writeValue(writer, company);
                    successCount++;
                    logger.info("Written company JSON: {}", fileName);
                } catch (Exception e) {
                    logger.error("Error writing company result to JSON", e);
                }
            }

            // Write summary file
            writeSummaryFile(companies, outputDirectory);

            logger.info("Successfully output {} company files to {}", successCount, outputPath.toAbsolutePath());
        } catch (IOException e) {
            logger.error("Error creating output directory", e);
        }
    }

    /**
     * Write a summary file with all company metadata
     * @param companies List of CompanyDetail
     * @param outputDirectory Output directory path
     */
    private static void writeSummaryFile(List<CompanyDetail> companies, String outputDirectory) {
        try {
            Path outputPath = Paths.get(outputDirectory);

            SummaryData summary = new SummaryData(
                    companies.size(),
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    companies.stream().map(c -> c.getMetadata()).toList()
            );

            Path summaryPath = outputPath.resolve("summary.json");
            try (java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(
                    summaryPath,
                    java.nio.charset.StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING)) {
                objectMapper.writeValue(writer, summary);
            }
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

