package com.chirag.stockscreener.extractor.company;

import com.chirag.stockscreener.model.CompanyDetail;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.util.HttpClientUtil;

import java.util.function.Function;

public class CompanyDetailExtractor implements Function<CompanyMetadata, CompanyDetail> {

    private final CompanyAttributesExtractor companyAttributesExtractor;
    private final CompanyTechnicalHistoryExtractor companyTechnicalHistoryExtractor;
    private final CompanyQuarterlyResultsExtractor companyQuarterlyResultsExtractor;

    public CompanyDetailExtractor(HttpClientUtil httpClientUtil) {
        this.companyAttributesExtractor = new CompanyAttributesExtractor(httpClientUtil);
        this.companyTechnicalHistoryExtractor = new CompanyTechnicalHistoryExtractor(httpClientUtil);
        this.companyQuarterlyResultsExtractor = new CompanyQuarterlyResultsExtractor(httpClientUtil);
    }

    @Override
    public CompanyDetail apply(CompanyMetadata companyMetadata) {
        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setMetadata(companyMetadata);
        companyDetail.setAttributes(companyAttributesExtractor.apply(companyMetadata));
        companyDetail.setTechnicalHistory(companyTechnicalHistoryExtractor.apply(companyMetadata));
        companyDetail.setQuarterlyResults(companyQuarterlyResultsExtractor.apply(companyMetadata));
        return companyDetail;
    }
}
