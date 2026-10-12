/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;


import com.wholescripts.core.dao.WholescriptsProductsDao;
import com.wholescripts.core.model.RegionProductAgeRestrictionModel;
import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import com.wholescripts.core.service.WholescriptsProudctsService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.collections.CollectionUtils;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import org.apache.commons.lang.StringUtils;


import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class WholescriptsProudctsServiceImpl implements WholescriptsProudctsService {

    @Resource(name = "wholescriptsProductsDao")
    WholescriptsProductsDao wholescriptsProductsDao;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Override
    public List<ProductModel> getNonPLProductsBySKUsList(final List<String> productSkusList, CatalogVersionModel catalogVersion) {
        return wholescriptsProductsDao.getNonPLProductsBySKUsList(productSkusList, catalogVersion);
    }

    @Override
    public List<ProductModel> getAllNonPLProducts(final CatalogVersionModel catalogVersion){
        return wholescriptsProductsDao.getAllNonPLProducts(catalogVersion);
    }

    public ProductModel getPLProductBySKU(final String productCode, CatalogVersionModel catalogVersion){
        return wholescriptsProductsDao.getPLProductBySKU(productCode,catalogVersion);
    }

    @Override
    public String getProductSwapRestriction(final String productCode) {
        Collection<RegionProductSwapRestrictionModel> swapRestrictionModels = wholescriptsProductsDao.getProductSwapRestriction(productCode);
        if (CollectionUtils.isNotEmpty(swapRestrictionModels)) {
                return swapRestrictionModels.iterator().next().getSwapProductCode();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<RegionProductAgeRestrictionModel> getProductAgeRestriction(final String productCode) {
        return wholescriptsProductsDao.getProductAgeRestriction(productCode);
    }

    @Override
    public List<CustomerMaterialInformationModel> getCustomerMaterialInformationByCustomerId(String customerId){
        return wholescriptsProductsDao.getCustomerMaterialInformationByCustomerId(customerId);
    }
}