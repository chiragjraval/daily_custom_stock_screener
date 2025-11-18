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

            // Extract market cap
            detail.setMarketCap(extractFieldValue(doc, "Market Cap", "div[data-field='market_cap']"));

            // Extract P/E Ratio
            detail.setPeRatio(extractFieldValue(doc, "P/E", "div[data-field='pe_ratio']"));

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
                String value = cleanValue(element.text());
                if (!value.isEmpty()) {
                    return value;
                }
            }

            // Fallback: look for span that contains the fieldName, value is in sibling span
            Elements spans = doc.select("span");
            for (Element span : spans) {
                if (span.text().contains(fieldName)) {
                    // 1) immediate next element sibling if it's a span
                    Element next = span.nextElementSibling();
                    if (next != null && "span".equalsIgnoreCase(next.tagName())) {
                        String value = cleanValue(next.text());
                        if (!value.isEmpty()) return value;
                    }

                    // 2) look for next span within the same parent
                    Element parent = span.parent();
                    if (parent != null) {
                        Elements siblingSpans = parent.select("span");
                        for (int i = 0; i < siblingSpans.size(); i++) {
                            if (siblingSpans.get(i).equals(span) && i + 1 < siblingSpans.size()) {
                                String value = cleanValue(siblingSpans.get(i + 1).text());
                                if (!value.isEmpty()) return value;
                            }
                        }
                    }

                    // 3) as a last resort, try the parent's next element and pick its first span
                    if (parent != null) {
                        Element parentNext = parent.nextElementSibling();
                        if (parentNext != null) {
                            Element firstSpan = parentNext.selectFirst("span");
                            if (firstSpan != null) {
                                String value = cleanValue(firstSpan.text());
                                if (!value.isEmpty()) return value;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Error extracting field {}: {}", fieldName, e.getMessage());
        }

        return null;
    }

    private static String cleanValue(String raw) {
        if (raw == null) return "";
        return raw.replace("\u00A0", " ")
                .replace("\u200B", "")
                .replace("+", "")
                .trim();
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

