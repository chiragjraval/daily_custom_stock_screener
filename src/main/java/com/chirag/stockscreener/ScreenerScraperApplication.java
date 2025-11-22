package com.chirag.stockscreener;

import com.chirag.stockscreener.context.ExecutionContext;
import com.chirag.stockscreener.context.ExecutionContextImpl;
import com.chirag.stockscreener.extractor.company.CompanyAttributesExtractor;
import com.chirag.stockscreener.extractor.company.CompanyDetailExtractor;
import com.chirag.stockscreener.extractor.query.QueryResultsExtractor;
import com.chirag.stockscreener.model.CompanyAttributes;
import com.chirag.stockscreener.model.CompanyDetail;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.service.GitService;
import com.chirag.stockscreener.service.JsonOutputService;
import com.chirag.stockscreener.util.HttpClientUtil;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main scraper application for screener.in data extraction
 * Runs during Maven build phase (process-resources)
 */
public class ScreenerScraperApplication {

    private static final Logger logger = LoggerFactory.getLogger(ScreenerScraperApplication.class);
    private static final HttpClientUtil httpClientUtil = new HttpClientUtil(5, 2000, 60000);

    public static void main(String[] args) {
        logger.info("Starting Screener.in Data Scraper Application");
        ExecutionContext executionContext;

        try {
            executionContext = new ExecutionContextImpl(args);
            logger.info("ExecutionContext = {}", executionContext);
        }
        catch (ParseException | IOException e) {
            logger.error("Error initializing execution context", e);
            System.exit(1);
            return;
        }

        QueryResultsExtractor queryResultsExtractor = new QueryResultsExtractor(executionContext, httpClientUtil);
        CompanyDetailExtractor companyDetailExtractor = new CompanyDetailExtractor(httpClientUtil);

        try {
            // Step 1: Parse screener list to get company metadata
            logger.info("Step 1: Fetching company list from screener...");
            QueryResultsExtractor.QueryResults queryResults = queryResultsExtractor.apply(executionContext.getScreenerQueryLink());

            if (queryResults.companyMetadataMap().isEmpty()) {
                logger.warn("No companies found in screener list");
                System.exit(0);
            }

            logger.info("Found {} companies in screener list", queryResults.companyMetadataMap().size());

            // Step 2: Fetch detailed information for each company
            logger.info("Step 2: Fetching detailed information for each company...");
            List<CompanyDetail> details = queryResults.companyMetadataMap().keySet().stream().sorted().map(companyCode -> {
                CompanyMetadata metadata = queryResults.companyMetadataMap().get(companyCode);
                return companyDetailExtractor.apply(metadata);
            }).toList();
            logger.info("Successfully processed {} companies", details.size());

            // Step 3: Output results to JSON files
            logger.info("Step 3: Writing results to JSON files...");
            JsonOutputService.outputToJson(details, executionContext.getOutputDirectory());

            // Step 4: Commit changes if enabled and repository exists
            if (executionContext.isGitCommitEnabled() && GitService.isGitRepository(".")) {
                logger.info("Step 4: Committing changes to Git repository...");
                boolean commitSuccess = GitService.commitAndPushChanges(".", executionContext.getGitFilePattern());
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
            System.exit(1);
        }
    }
}

