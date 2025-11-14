package com.chirag.stockscreener.parser;

import com.chirag.stockscreener.model.CompanyDetail;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser for extracting detailed company information from screener.in company pages
 */
public class ScreenerCompanyDetailParser {

    private static final Logger logger = LoggerFactory.getLogger(ScreenerCompanyDetailParser.class);

    /**
     * Parse company detail page and extract company information
     * @param metadata CompanyMetadata containing company code and links
     * @return CompanyDetail with extracted information
     */
    public static CompanyDetail parseCompanyDetail(CompanyMetadata metadata) {
        CompanyDetail detail = new CompanyDetail();
        detail.setCompanyCode(metadata.getCompanyCode());
        detail.setCompanyName(metadata.getCompanyName());

        try {
            String html = HttpClientUtil.fetchHtml(metadata.getScreenerCompanyLink());
            if (html == null) {
                logger.error("Failed to fetch company detail page: {}", metadata.getScreenerCompanyLink());
                return detail;
            }

            Document doc = Jsoup.parse(html);

            // Extract industry
            detail.setIndustry(extractFieldValue(doc, "Industry", "div[data-field='industry']"));

            // Extract market cap
            detail.setMarketCap(extractFieldValue(doc, "Market Cap", "div[data-field='market_cap']"));

            // Extract P/E Ratio
            detail.setPeRatio(extractFieldValue(doc, "P/E", "div[data-field='pe_ratio']"));

            // Extract P/B Ratio
            detail.setPbRatio(extractFieldValue(doc, "P/B", "div[data-field='pb_ratio']"));

            // Extract Dividend Yield
            detail.setDividendYield(extractFieldValue(doc, "Dividend Yield", "div[data-field='dividend_yield']"));

            // Extract Debt to Equity
            detail.setDebtToEquity(extractFieldValue(doc, "Debt/Equity", "div[data-field='debt_equity']"));

            // Extract ROE
            detail.setRoe(extractFieldValue(doc, "ROE", "div[data-field='roe']"));

            // Extract company description
            detail.setDescription(extractDescription(doc));

            logger.info("Successfully parsed company detail for: {}", metadata.getCompanyCode());
        } catch (Exception e) {
            logger.error("Error parsing company detail for: {}", metadata.getCompanyCode(), e);
        }

        return detail;
    }

    /**
     * Extract a specific field value from the document
     * @param doc The parsed document
     * @param fieldName The human-readable field name
     * @param selector CSS selector for the field
     * @return The field value or null
     */
    private static String extractFieldValue(Document doc, String fieldName, String selector) {
        try {
            Element element = doc.selectFirst(selector);
            if (element != null) {
                String value = element.text();
                if (!value.isEmpty()) {
                    return value;
                }
            }

            // Fallback: look for field by text content
            Elements allElements = doc.getAllElements();
            for (Element el : allElements) {
                if (el.text().contains(fieldName)) {
                    // Try to find value in sibling or parent
                    Element sibling = el.nextElementSibling();
                    if (sibling != null) {
                        String value = sibling.text();
                        if (!value.isEmpty()) {
                            return value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Error extracting field {}: {}", fieldName, e.getMessage());
        }

        return null;
    }

    /**
     * Extract company description from the page
     * @param doc The parsed document
     * @return Company description or null
     */
    private static String extractDescription(Document doc) {
        try {
            // Look for description/overview section
            Element descElement = doc.selectFirst("div[class*='description'], div[class*='about'], div[class*='overview']");
            if (descElement != null) {
                return descElement.text();
            }

            // Fallback: get first paragraph of substantial length
            Elements paragraphs = doc.select("p");
            for (Element p : paragraphs) {
                String text = p.text();
                if (text.length() > 50) {
                    return text;
                }
            }
        } catch (Exception e) {
            logger.debug("Error extracting description: {}", e.getMessage());
        }

        return null;
    }
}

