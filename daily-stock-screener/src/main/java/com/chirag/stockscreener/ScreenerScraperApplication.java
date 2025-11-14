package com.chirag.stockscreener;

import com.chirag.stockscreener.model.CompanyDetail;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.model.CompanyResult;
import com.chirag.stockscreener.parser.ScreenerCompanyDetailParser;
import com.chirag.stockscreener.parser.ScreenerCompanyListParser;
import com.chirag.stockscreener.service.GitService;
import com.chirag.stockscreener.service.JsonOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Main scraper application for screener.in data extraction
 * Runs during Maven build phase (process-resources)
 */
public class ScreenerScraperApplication {

    private static final Logger logger = LoggerFactory.getLogger(ScreenerScraperApplication.class);

    // Configuration
    private static final String SCREENER_LINK =
            "https://www.screener.in/screens/2618573/companies-with-growth-salesprofitmargin/";

    private static final String OUTPUT_DIRECTORY = "src/main/resources/screener-data";

    private static final boolean ENABLE_GIT_COMMIT = true;
    private static final String GIT_FILE_PATTERN = "src/main/resources/screener-data/*";

    public static void main(String[] args) {
        logger.info("Starting Screener.in Data Scraper Application");
        logger.info("Screener Link: {}", SCREENER_LINK);
        logger.info("Output Directory: {}", OUTPUT_DIRECTORY);

        try {
            // Step 1: Parse screener list to get company metadata
            logger.info("Step 1: Fetching company list from screener...");
            List<CompanyMetadata> companiesList = ScreenerCompanyListParser.parseScreenerLink(SCREENER_LINK);

            if (companiesList.isEmpty()) {
                logger.warn("No companies found in screener list");
                System.exit(1);
            }

            logger.info("Found {} companies in screener list", companiesList.size());

            // Step 2: Fetch detailed information for each company
            logger.info("Step 2: Fetching detailed information for each company...");
            List<CompanyResult> results = new ArrayList<>();

            for (CompanyMetadata metadata : companiesList) {
                try {
                    logger.debug("Processing company: {}", metadata.getCompanyCode());

                    // Fetch company detail
                    CompanyDetail detail = ScreenerCompanyDetailParser.parseCompanyDetail(metadata);

                    // Create result object
                    CompanyResult result = new CompanyResult(metadata, detail, new ArrayList<>());
                    results.add(result);

                    // Add delay between requests to be respectful to the server
                    Thread.sleep(500);

                } catch (Exception e) {
                    logger.error("Error processing company: {}", metadata.getCompanyCode(), e);
                }
            }

            logger.info("Successfully processed {} companies", results.size());

            // Step 3: Output results to JSON files
            logger.info("Step 3: Writing results to JSON files...");
            JsonOutputService.outputToJson(results, OUTPUT_DIRECTORY);

            // Step 4: Commit changes if enabled and repository exists
            if (ENABLE_GIT_COMMIT && GitService.isGitRepository(".")) {
                logger.info("Step 4: Committing changes to Git repository...");
                boolean commitSuccess = GitService.commitChanges(".", GIT_FILE_PATTERN);
                if (commitSuccess) {
                    logger.info("Successfully committed changes to Git repository");
                } else {
                    logger.warn("Failed to commit changes to Git repository");
                }
            } else {
                logger.info("Step 4: Git commit is disabled or repository not found, skipping...");
            }

            logger.info("Screener Data Scraper Application completed successfully");
            System.exit(0);

        } catch (Exception e) {
            logger.error("Fatal error in scraper application", e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}

