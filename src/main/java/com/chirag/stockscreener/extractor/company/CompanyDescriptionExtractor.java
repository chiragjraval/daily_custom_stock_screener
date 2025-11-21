package com.chirag.stockscreener.extractor.company;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class CompanyDescriptionExtractor implements Function<Document, String> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDescriptionExtractor.class);

    @Override
    public String apply(Document document) {
        try {
            // Look for description/overview section
            Element descElement = document.selectFirst("div[class*='description'], div[class*='about'], div[class*='overview']");
            if (descElement != null) {
                return descElement.text();
            }

            // Fallback: get first paragraph of substantial length
            Elements paragraphs = document.select("p");
            for (Element p : paragraphs) {
                String text = p.text();
                if (text.length() > 50) {
                    return text;
                }
            }
        } catch (Exception e) {
            logger.debug("Error extracting description: {}", e.getMessage());
        }

        return "";
    }
}
