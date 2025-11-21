package com.chirag.stockscreener.extractor.query;

import com.chirag.stockscreener.model.CompanyMetadata;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompanyMetadataExtractor implements Function<Element, Optional<CompanyMetadata>> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyMetadataExtractor.class);

    @Override
    public Optional<CompanyMetadata> apply(Element row) {
        // Extract company link from first column
        Element linkElement = row.selectFirst("a[href*=/company/]");

        if (linkElement != null) {
            String href = linkElement.attr("href");
            String companyCode = extractCompanyCode(href);
            String companyName = linkElement.text();

            if (companyCode != null && !companyCode.isEmpty()) {
                // Extract screener company ID from data attributes or URL patterns
                String screenerCompanyId = extractScreenerCompanyId(row, companyCode);

                CompanyMetadata metadata = new CompanyMetadata(screenerCompanyId, companyCode, companyName);
                logger.info("Parsed company: {}", metadata);
                return Optional.of(metadata);
            }
        }

        return Optional.empty();
    }

    /**
     * Extract company code from company link href
     * @param href The href attribute value
     * @return Company code (ticker symbol)
     */
    private String extractCompanyCode(String href) {
        Pattern pattern = Pattern.compile("/company/([^/]+)");
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
    private String extractScreenerCompanyId(Element row, String companyCode) {
        // Try to extract from data attributes or generate based on company code
        String dataId = row.attr("data-row-company-id");
        if (!dataId.isEmpty()) {
            return dataId;
        }
        // Fallback: use company code as ID
        return companyCode;
    }
}
