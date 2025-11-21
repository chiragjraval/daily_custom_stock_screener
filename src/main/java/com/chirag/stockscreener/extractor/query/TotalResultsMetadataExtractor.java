package com.chirag.stockscreener.extractor.query;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TotalResultsMetadataExtractor implements Function<Document, TotalResultsMetadataExtractor.TotalResults> {

    private static final Logger logger = LoggerFactory.getLogger(TotalResultsMetadataExtractor.class);

    public record TotalResults(int totalCompanies, int totalPages) {
        public static TotalResults empty() {
            return new TotalResults(0, 1);
        }
    }

    @Override
    public TotalResults apply(Document document) {

        // Find the div containing pagination info
        Elements allElements = document.getAllElements();
        for (Element element : allElements) {
            String text = element.text();
            if (text.contains("results found: Showing page")) {
                Pattern pattern = Pattern.compile("(\\d+)\\s+results found: Showing page\\s+(\\d+)\\s+of\\s+(\\d+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    int totalCompanies = Integer.parseInt(matcher.group(1));
                    int totalPages = Integer.parseInt(matcher.group(3));
                    logger.info("Found {} total companies", totalCompanies);
                    logger.info("Found {} total pages", totalPages);
                    return new TotalResults(totalCompanies, totalPages);
                }
            }
        }

        return TotalResults.empty();
    }
}
