package com.chirag.stockscreener.parser;

import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for extracting company metadata from screener.in list pages
 */
public class ScreenerCompanyListParser {

    private static final Logger logger = LoggerFactory.getLogger(ScreenerCompanyListParser.class);
    private static final String SCREENER_BASE_URL = "https://www.screener.in";

    /**
     * Extract the total number of pages from a screener link
     * @param screenerLink The URL to the screener page
     * @return Total number of pages
     */
    public static int extractTotalPages(String screenerLink) {
        try {
            String html = HttpClientUtil.fetchHtml(screenerLink);
            if (html == null) {
                logger.error("Failed to fetch screener link: {}", screenerLink);
                return 1;
            }

            Document doc = Jsoup.parse(html);

            // Find the div containing pagination info
            Elements allElements = doc.getAllElements();
            for (Element element : allElements) {
                String text = element.text();
                if (text.contains("results found: Showing page")) {
                    Pattern pattern = Pattern.compile("(\\d+)\\s+results found: Showing page\\s+(\\d+)\\s+of\\s+(\\d+)");
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        int totalPages = Integer.parseInt(matcher.group(3));
                        logger.info("Found {} total pages", totalPages);
                        return totalPages;
                    }
                }
            }

            logger.warn("Could not find pagination info, defaulting to 1 page");
            return 1;
        } catch (Exception e) {
            logger.error("Error extracting total pages", e);
            return 1;
        }
    }

    /**
     * Parse a screener page and extract company metadata
     * @param screenerLink The URL to the screener page
     * @return List of CompanyMetadata
     */
    public static List<CompanyMetadata> parseScreenerLink(String screenerLink) {
        List<CompanyMetadata> companies = new ArrayList<>();

        try {
            int totalPages = extractTotalPages(screenerLink);
            logger.info("Parsing {} pages from {}", totalPages, screenerLink);

            for (int page = 1; page <= totalPages; page++) {
                String pageUrl = screenerLink.contains("?") ?
                        screenerLink + "&page=" + page :
                        screenerLink + "?page=" + page;

                logger.info("Parsing page {} of {}: {}", page, totalPages, pageUrl);
                List<CompanyMetadata> pageCompanies = parsePageCompanies(pageUrl);
                companies.addAll(pageCompanies);

                // Be respectful to the server - add delay between requests
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            logger.info("Total companies found: {}", companies.size());
        } catch (Exception e) {
            logger.error("Error parsing screener link", e);
        }

        return companies;
    }

    /**
     * Parse a single page and extract company data
     * @param pageUrl The URL to the page
     * @return List of CompanyMetadata from this page
     */
    private static List<CompanyMetadata> parsePageCompanies(String pageUrl) {
        List<CompanyMetadata> companies = new ArrayList<>();

        try {
            String html = HttpClientUtil.fetchHtml(pageUrl);
            if (html == null) {
                logger.error("Failed to fetch page: {}", pageUrl);
                return companies;
            }

            Document doc = Jsoup.parse(html);

            // Find table rows containing company data
            Elements rows = doc.select("table tbody tr");

            for (Element row : rows) {
                try {
                    // Extract company link from first column
                    Element linkElement = row.selectFirst("a[href*=/company/]");
                    if (linkElement == null) {
                        continue;
                    }

                    String href = linkElement.attr("href");
                    String companyCode = extractCompanyCode(href);
                    String companyName = linkElement.text();

                    if (companyCode != null && !companyCode.isEmpty()) {
                        // Extract screener company ID from data attributes or URL patterns
                        String screenerCompanyId = extractScreenerCompanyId(row, companyCode);

                        CompanyMetadata metadata = new CompanyMetadata(screenerCompanyId, companyCode, companyName);
                        companies.add(metadata);
                        logger.debug("Parsed company: {}", metadata);
                    }
                } catch (Exception e) {
                    logger.warn("Error parsing company row", e);
                }
            }

            logger.info("Parsed {} companies from page", companies.size());
        } catch (Exception e) {
            logger.error("Error parsing page companies", e);
        }

        return companies;
    }

    /**
     * Extract company code from company link href
     * @param href The href attribute value
     * @return Company code (ticker symbol)
     */
    private static String extractCompanyCode(String href) {
        Pattern pattern = Pattern.compile("/company/([A-Z0-9]+)");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Extract screener company ID from page data
     * @param row The table row element
     * @param companyCode The company code
     * @return Screener company ID
     */
    private static String extractScreenerCompanyId(Element row, String companyCode) {
        // Try to extract from data attributes or generate based on company code
        String dataId = row.attr("data-company-id");
        if (dataId != null && !dataId.isEmpty()) {
            return dataId;
        }
        // Fallback: use company code as ID
        return companyCode;
    }
}

