/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsMaterialExclusionDao;
import com.wholescripts.core.model.BrandCustomerClassRestrictionModel;
import com.wholescripts.core.model.CustomerClassProductRestrictionModel;
import com.wholescripts.core.model.CustomerProductGroupRestrictionModel;
import com.wholescripts.core.model.CustomerProductRestrictionModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;

/**
 *
 */
public class WholescriptsMaterialExclusionDaoImpl implements WholescriptsMaterialExclusionDao
{
	private static final String GET_BRAND_CUSTOMER_CLASS_RESTRICTION ="SELECT {br.PK} FROM {BrandCustomerClassRestriction as br} where {br.brandCode}=?brandCode and {br.customerClass} =?customerClass and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_PRODUCT_CUSTOMER_CLASS_RESTRICTION ="SELECT {cr.PK} FROM {CustomerClassProductRestriction as cr} where {cr.productCode}=?productCode and {cr.customerClass} =?customerClass and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_PRODUCT_CUSTOMER_RESTRICTION ="SELECT {pr.PK} FROM {CustomerProductRestriction as pr} where {pr.productCode}=?productCode and {pr.customerSAPId} =?customerSAPId and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_PRODUCT_CUSTOMER_GROUP_RESTRICTION ="SELECT {gr.PK} FROM {CustomerProductGroupRestriction as gr} where {gr.productGroup}=?productGroup and {gr.customerSAPId} =?customerSAPId and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	
	private static final String GET_PRODUCT_CUSTOMER_RESTRICTION_FOR_PRODUCT ="SELECT {pr.PK} FROM {CustomerProductRestriction as pr} where {pr.productCode}=?productCode and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_PRODUCT_CUSTOMER_GROUP_RESTRICTION_FOR_PRODUCT ="SELECT {gr.PK} FROM {CustomerProductGroupRestriction as gr} where {gr.productGroup}=?productGroup and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_PRODUCT_CUSTOMER_CLASS_RESTRICTION_FOR_PRODUCT ="SELECT {cr.PK} FROM {CustomerClassProductRestriction as cr} where {cr.productCode}=?productCode and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_BRAND_CUSTOMER_CLASS_RESTRICTION_FOR_PRODUCT ="SELECT {br.PK} FROM {BrandCustomerClassRestriction as br} where {br.brandCode}=?brandCode and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Override
	public boolean validateBrandCustomerClassRestriction(ProductModel product,
			B2BCustomerModel customer)
	{
		if(Objects.nonNull(customer)  && Objects.nonNull(product.getWholescriptsBrand()) && Objects.nonNull(customer.getDefaultB2BUnit()) && Objects.nonNull(customer.getDefaultB2BUnit().getSapCustomerClassification())) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_BRAND_CUSTOMER_CLASS_RESTRICTION);
			query.addQueryParameter(BrandCustomerClassRestrictionModel.BRANDCODE, product.getWholescriptsBrand().getCode());
			query.addQueryParameter(BrandCustomerClassRestrictionModel.CUSTOMERCLASS, customer.getDefaultB2BUnit().getSapCustomerClassification().getCode());
			final SearchResult<BrandCustomerClassRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult().isEmpty() ? false : true;
		}
		return false;		
	}

	@Override
	public boolean validateCustomerClassProductRestriction(ProductModel product,
			B2BCustomerModel customer)
	{
		if(Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit()) && Objects.nonNull(customer.getDefaultB2BUnit().getSapCustomerClassification())) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_CUSTOMER_CLASS_RESTRICTION);
			query.addQueryParameter(CustomerClassProductRestrictionModel.PRODUCTCODE, product.getCode());
			query.addQueryParameter(CustomerClassProductRestrictionModel.CUSTOMERCLASS, customer.getDefaultB2BUnit().getSapCustomerClassification().getCode());
			final SearchResult<CustomerClassProductRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult().isEmpty() ? false : true;
		}
		return false;
	}

	@Override
	public boolean validateCustomerProductRestriction(ProductModel product, B2BCustomerModel customer)
	{
		if(Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_CUSTOMER_RESTRICTION);
			query.addQueryParameter(CustomerProductRestrictionModel.PRODUCTCODE, product.getCode());
			query.addQueryParameter(CustomerProductRestrictionModel.CUSTOMERSAPID, WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid()));
			final SearchResult<CustomerProductRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult().isEmpty() ? false : true;
		}
		return false;
	}

	@Override
	public boolean validateCustomerProductGroupRestriction(ProductModel product,
			B2BCustomerModel customer)
	{
		if(Objects.nonNull(product.getSapMaterialGroup()) && Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_CUSTOMER_GROUP_RESTRICTION);
			query.addQueryParameter(CustomerProductGroupRestrictionModel.PRODUCTGROUP, product.getSapMaterialGroup().getCode());
			query.addQueryParameter(CustomerProductGroupRestrictionModel.CUSTOMERSAPID, WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid()));
			final SearchResult<CustomerProductGroupRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult().isEmpty() ? false : true;
		}
		return false;
	}
	
	@Override
	public List<CustomerProductRestrictionModel> getCustomerProductRestrictions(String productCode) {
		List<CustomerProductRestrictionModel> customerProductRestrictions = null;
		if(StringUtils.isNotEmpty(productCode)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_CUSTOMER_RESTRICTION_FOR_PRODUCT);
			query.addQueryParameter(CustomerProductRestrictionModel.PRODUCTCODE, productCode);
			final SearchResult<CustomerProductRestrictionModel> result = flexibleSearchService.search(query);
			customerProductRestrictions = result.getResult();
		}
		return customerProductRestrictions;
	}
	
	@Override
	public List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions(final String productGroup) {
		
		List<CustomerProductGroupRestrictionModel> customerProductGroupRestrictions = null;
		if(StringUtils.isNotEmpty(productGroup)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_CUSTOMER_GROUP_RESTRICTION_FOR_PRODUCT);
			query.addQueryParameter(CustomerProductGroupRestrictionModel.PRODUCTGROUP, productGroup);
			final SearchResult<CustomerProductGroupRestrictionModel> result = flexibleSearchService.search(query);
			customerProductGroupRestrictions = result.getResult();
		}
		return customerProductGroupRestrictions;
	}
	
	@Override
	public List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions(final String productCode) {
		
		List<CustomerClassProductRestrictionModel> customerClassProductRestrictions = null;
		
		if(StringUtils.isNotEmpty(productCode)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_CUSTOMER_CLASS_RESTRICTION_FOR_PRODUCT);
			query.addQueryParameter(CustomerClassProductRestrictionModel.PRODUCTCODE, productCode);
			final SearchResult<CustomerClassProductRestrictionModel> result = flexibleSearchService.search(query);
			customerClassProductRestrictions = result.getResult();
		}
		return customerClassProductRestrictions;
	}
	
	@Override
	public List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestriction(final String brandCode) {
		
		List<BrandCustomerClassRestrictionModel> brandCustomerClassRestrictions = null;
		
		if(StringUtils.isNotEmpty(brandCode)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_BRAND_CUSTOMER_CLASS_RESTRICTION_FOR_PRODUCT);
			query.addQueryParameter(BrandCustomerClassRestrictionModel.BRANDCODE, brandCode);
			final SearchResult<BrandCustomerClassRestrictionModel> result = flexibleSearchService.search(query);
			return brandCustomerClassRestrictions = result.getResult();
		}
		return brandCustomerClassRestrictions;		
	}

	@Override
	public boolean isMedpaxExcluedForCustomer(String accountNumber) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_CUSTOMER_GROUP_RESTRICTION);
			query.addQueryParameter(CustomerProductGroupRestrictionModel.PRODUCTGROUP, configurationService.getConfiguration().getString("wholescripts.medpax.material.code"));
			query.addQueryParameter(CustomerProductGroupRestrictionModel.CUSTOMERSAPID, accountNumber);
			final SearchResult<CustomerProductGroupRestrictionModel> result = flexibleSearchService.search(query);
			return !result.getResult().isEmpty();
	}


}


