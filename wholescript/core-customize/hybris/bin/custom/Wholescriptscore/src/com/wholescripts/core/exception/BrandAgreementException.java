package com.wholescripts.core.exception;

import com.wholescripts.dto.WholescriptsBrandData;

import java.util.List;

public class BrandAgreementException extends Exception{
    private List<WholescriptsBrandData> brandsAgreementList;

    public List<WholescriptsBrandData> getBrandsAgreementList() {
        return brandsAgreementList;
    }
    public BrandAgreementException(String message) {
        super(message);
    }
    public void setBrandsAgreementList(List<WholescriptsBrandData> brandsAgreementList) {
        this.brandsAgreementList = brandsAgreementList;
    }

    public BrandAgreementException(List<WholescriptsBrandData> brandsAgreementList) {
        this.setBrandsAgreementList(brandsAgreementList);
    }
}
