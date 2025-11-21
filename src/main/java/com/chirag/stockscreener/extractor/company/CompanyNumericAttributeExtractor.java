package com.chirag.stockscreener.extractor.company;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalDouble;
import java.util.function.BiFunction;

public class CompanyNumericAttributeExtractor implements BiFunction<Document, String, OptionalDouble> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyNumericAttributeExtractor.class);

    @Override
    public OptionalDouble apply(Document document, String attributeName) {
        String rawValue = extractFieldValue(document, attributeName);
        String cleanedValue = cleanValue(rawValue != null ? rawValue : "");
        return !cleanedValue.isEmpty() ? OptionalDouble.of(Double.parseDouble(cleanedValue)) : OptionalDouble.empty();
    }

    /**
     * Extract a specific field value from the document
     * @param doc The parsed document
     * @param fieldName The human-readable field name
     * @return The field value or null
     */
    private String extractFieldValue(Document doc, String fieldName) {
        try {
            String value = null;

            Elements spans = doc.select("span");
            for (Element span : spans) {
                if (span.text().contains(fieldName)) {
                    // 1) immediate next element sibling if it's a span
                    Element next = span.nextElementSibling();
                    if (next != null && "span".equalsIgnoreCase(next.tagName())) {
                        value = next.text();
                        if (!value.isEmpty()) return value;
                    }

                    // 2) look for next span within the same parent
                    Element parent = span.parent();
                    if (parent != null) {
                        Elements siblingSpans = parent.select("span");
                        for (int i = 0; i < siblingSpans.size(); i++) {
                            if (siblingSpans.get(i).equals(span) && i + 1 < siblingSpans.size()) {
                                value = siblingSpans.get(i + 1).text();
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
                                value = firstSpan.text();
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

    private String cleanValue(String raw) {
        // Remove all special characters except dot and minus
        return raw.replaceAll("[^0-9.-]", "");
    }
}
