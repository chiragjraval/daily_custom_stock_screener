package com.chirag.stockscreener.extractor.company;

import com.chirag.stockscreener.model.CompanyDetail;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.util.HttpClientUtil;

import java.util.function.Function;

public class CompanyDetailExtractor implements Function<CompanyMetadata, CompanyDetail> {

    private final CompanyAttributesExtractor companyAttributesExtractor;
    private final CompanyTechnicalHistoryExtractor companyTechnicalHistoryExtractor;
    private final CompanyQuarterlyResultsExtractor companyQuarterlyResultsExtractor;
    private final CompanyScoreExtractor companyScoreExtractor;

    public CompanyDetailExtractor(HttpClientUtil httpClientUtil) {
        this.companyAttributesExtractor = new CompanyAttributesExtractor(httpClientUtil);
        this.companyTechnicalHistoryExtractor = new CompanyTechnicalHistoryExtractor(httpClientUtil);
        this.companyQuarterlyResultsExtractor = new CompanyQuarterlyResultsExtractor(httpClientUtil);
        this.companyScoreExtractor = new CompanyScoreExtractor();
    }

    @Override
    public CompanyDetail apply(CompanyMetadata companyMetadata) {
        CompanyDetail companyDetail = new CompanyDetail();

        // Set metadata
        companyDetail.setMetadata(companyMetadata);

        // Extract and set attributes, technical history, and quarterly results
        companyDetail.setAttributes(companyAttributesExtractor.apply(companyMetadata));
        companyDetail.setTechnicalHistory(companyTechnicalHistoryExtractor.apply(companyMetadata));
        companyDetail.setQuarterlyResults(companyQuarterlyResultsExtractor.apply(companyMetadata));

        // Set latest technical attributes
        if (!companyDetail.getTechnicalHistory().isEmpty()) {
            companyDetail.setLatestTechnicalAttributes(
                    companyDetail.getTechnicalHistory()
                            .get(companyDetail.getTechnicalHistory().size() - 1)
            );
        }

        // Set latest quarterly result
        if (!companyDetail.getQuarterlyResults().isEmpty()) {
            companyDetail.setLatestQuarterlyResult(
                    companyDetail.getQuarterlyResults()
                            .get(companyDetail.getQuarterlyResults().size() - 1)
            );
        }

        // Calculate and set company score
        companyDetail.setCompanyScore(companyScoreExtractor.apply(companyDetail));

        return companyDetail;
    }
}
