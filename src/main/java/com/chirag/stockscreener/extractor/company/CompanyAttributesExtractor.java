package com.chirag.stockscreener.extractor.company;

import com.chirag.stockscreener.model.CompanyAttributes;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class CompanyAttributesExtractor implements Function<CompanyMetadata, CompanyAttributes> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyAttributesExtractor.class);

    private final HttpClientUtil httpClientUtil;
    private final CompanyNumericAttributeExtractor companyNumericAttributeExtractor;
    private final CompanyDescriptionExtractor companyDescriptionExtractor;

    public CompanyAttributesExtractor(HttpClientUtil httpClientUtil) {
        this.httpClientUtil = httpClientUtil;
        this.companyNumericAttributeExtractor = new CompanyNumericAttributeExtractor();
        this.companyDescriptionExtractor = new CompanyDescriptionExtractor();
    }

    @Override
    public CompanyAttributes apply(CompanyMetadata metadata) {
        CompanyAttributes detail = new CompanyAttributes();
        detail.setCompanyCode(metadata.getCompanyCode());
        detail.setCompanyName(metadata.getCompanyName());

        try {
            // Fetch company detail page HTML
            String html = httpClientUtil.fetchHtml(metadata.getScreenerCompanyLink());
            if (html == null) {
                logger.error("Failed to fetch company detail page: {}", metadata.getScreenerCompanyLink());
                return detail;
            }

            // Parse the HTML document
            Document doc = Jsoup.parse(html);

            // Extract market cap
            companyNumericAttributeExtractor.apply(doc, "Market Cap").ifPresent(detail::setMarketCap);

            // Extract P/E Ratio
            companyNumericAttributeExtractor.apply(doc, "P/E").ifPresent(detail::setPeRatio);

            // Extract company description
            detail.setDescription(companyDescriptionExtractor.apply(doc));

            logger.info("Successfully parsed company detail for: {}", metadata.getCompanyCode());
        } catch (Exception e) {
            logger.error("Error parsing company detail for: {}", metadata.getCompanyCode(), e);
        }

        return detail;
    }
}
