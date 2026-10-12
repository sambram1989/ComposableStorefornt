/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.listeners;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchListener;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.wholescripts.core.utils.WholescriptsCoreUtils;


public class WholescriptsPrivateLabelFacetSearchListener implements FacetSearchListener
{
   private static final String ACCOUNT ="account";
   private static final String PRIVATELABEL_INDEXED_TYPE ="CustomerMaterialInformation";
	@Resource(name = "fieldNameTranslator")
	private FieldNameTranslator fieldNameTranslator;
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Override
	public void afterSearch(final FacetSearchContext var1) throws FacetSearchException
	{
		// XXX Auto-generated method stub

	}

	@Override
	public void afterSearchError(final FacetSearchContext var1) throws FacetSearchException
	{
		// XXX Auto-generated method stub

	}

	@Override
	public void beforeSearch(final FacetSearchContext facetSearchContext) throws FacetSearchException
	{
		final SearchQuery searchQuery = facetSearchContext.getSearchQuery();
		if(PRIVATELABEL_INDEXED_TYPE.equals(searchQuery.getIndexedType().getCode())) {
			B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
			// if default unit is null
			String accountNumber = "0";
			if(Objects.nonNull(customer) &&  Objects.nonNull(customer.getDefaultB2BUnit())) {
				accountNumber =WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
			}	
			final String inclusionCustomers = translateField(searchQuery,
					searchQuery.getIndexedType().getIndexedProperties().get(ACCOUNT).getName());
			searchQuery.addFilterQuery(inclusionCustomers, accountNumber);
		}
	}

	protected String translateField(final SearchQuery query, final String field)
	{
		return fieldNameTranslator.translate(query, field, FieldNameProvider.FieldType.INDEX);
	}
}
