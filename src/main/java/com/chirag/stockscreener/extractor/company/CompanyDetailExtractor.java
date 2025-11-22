package com.chirag.stockscreener.extractor.company;

import com.chirag.stockscreener.model.CompanyDetail;
import com.chirag.stockscreener.model.CompanyMetadata;
import com.chirag.stockscreener.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class CompanyDetailExtractor implements Function<CompanyMetadata, CompanyDetail> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyDetailExtractor.class);

    private final CompanyAttributesExtractor companyAttributesExtractor;
    private final CompanyTechnicalHistoryExtractor companyTechnicalHistoryExtractor;

    public CompanyDetailExtractor(HttpClientUtil httpClientUtil) {
        this.companyAttributesExtractor = new CompanyAttributesExtractor(httpClientUtil);
        this.companyTechnicalHistoryExtractor = new CompanyTechnicalHistoryExtractor(httpClientUtil);
    }

    @Override
    public CompanyDetail apply(CompanyMetadata companyMetadata) {
        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setMetadata(companyMetadata);
        companyDetail.setAttributes(companyAttributesExtractor.apply(companyMetadata));
        companyDetail.setTechnicalHistory(companyTechnicalHistoryExtractor.apply(companyMetadata));
        return companyDetail;
    }
}
