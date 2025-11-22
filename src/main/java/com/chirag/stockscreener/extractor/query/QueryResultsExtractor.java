package com.chirag.stockscreener.extractor.query;

import com.chirag.stockscreener.context.ExecutionContext;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Extractor for screener query results including total results and company metadata
 */
public class QueryResultsExtractor implements Function<String, QueryResultsExtractor.QueryResults> {

    private static final Logger logger = LoggerFactory.getLogger(QueryResultsExtractor.class);

    public record QueryResults(TotalResultsMetadataExtractor.TotalResults totalResults, Map<String, CompanyMetadata> companyMetadataMap) {
    }

    private final ExecutionContext executionContext;
    private final HttpClientUtil httpClientUtil;
    private final TotalResultsMetadataExtractor totalResultsMetadataExtractor;
    private final CompanyMetadataExtractor companyMetadataExtractor;

    public QueryResultsExtractor(ExecutionContext executionContext, HttpClientUtil httpClientUtil) {
        this.executionContext = executionContext;
        this.httpClientUtil = httpClientUtil;
        this.totalResultsMetadataExtractor = new TotalResultsMetadataExtractor();
        this.companyMetadataExtractor = new CompanyMetadataExtractor();
    }

    /**
     * Extract screener query results including total results and company metadata
     * @param screenerQueryUrl The base URL of the screener query
     * @return QueryResults containing total results and company metadata map
     **/
    @Override
    public QueryResults apply(String screenerQueryUrl) {
        // Fetch first page to determine total results
        Optional<Document> doc = getScreenerPageDocument(screenerQueryUrl, 1);
        TotalResultsMetadataExtractor.TotalResults totalResults = doc.map(totalResultsMetadataExtractor)
                .orElse(TotalResultsMetadataExtractor.TotalResults.empty());

        int currentPage = 1;
        int maxPagesToScan = Math.min(totalResults.totalPages(), executionContext.getMaxPagesToScan());
        Map<String, CompanyMetadata> results = new HashMap<>();
        while (currentPage <= maxPagesToScan) {
            // Extract company metadata from current page
            doc.map(d -> d.select("table tbody tr")).ifPresent(rows -> {
                rows.stream().map(companyMetadataExtractor).forEach(metadata -> {
                    metadata.ifPresent(m -> results.put(m.getCompanyCode(), m));
                });
            });

            // Fetch next page
            currentPage++;
            doc = getScreenerPageDocument(screenerQueryUrl, currentPage);
        }

        return new QueryResults(totalResults, results);
    }

    /**
     * Fetch and parse the screener page document for a given page number
     * @param baseUrl The base screener query URL
     * @param page The page number to fetch
     * @return Optional containing the Document if successful, empty otherwise
     **/
    private Optional<Document> getScreenerPageDocument(String baseUrl, int page) {
        logger.info("Fetching screener page {} for URL: {}", page, baseUrl);
        String pageUrl = getScreenerQueryUrl(baseUrl, page);
        String html = httpClientUtil.fetch(pageUrl);
        if (html == null) {
            logger.error("Failed to fetch screener page: {}", pageUrl);
            return Optional.empty();
        }
        return Optional.of(Jsoup.parse(html));
    }

    /**
     * Construct the screener query URL for a specific page
     * @param baseUrl The base screener query URL
     * @param page The page number
     * @return The full URL for the specified page
     **/
    private String getScreenerQueryUrl(String baseUrl, int page) {
        if (page <= 1) {
            return baseUrl;
        }
        if (baseUrl.contains("?")) {
            return baseUrl + "&page=" + page;
        } else {
            return baseUrl + "?page=" + page;
        }
    }
}
