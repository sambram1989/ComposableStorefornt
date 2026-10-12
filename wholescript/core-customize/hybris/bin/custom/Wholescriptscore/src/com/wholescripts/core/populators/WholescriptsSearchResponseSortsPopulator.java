/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;


import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseSortsPopulator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;



public class WholescriptsSearchResponseSortsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_RESULT_TYPE, ITEM>
		extends SearchResponseSortsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_RESULT_TYPE, ITEM> {

	private static final String GUEST_USER_INVALID_SORTS = "ws.guest.user.sort.disabled";
	private static final String RETAIL_USER_INVALID_SORTS = "ws.patient.user.sort.disabled";
	private static final String WHOLESALE_USER_INVALID_SORTS = "ws.wholesale.user.sort.disabled";
	private static final String RELEVANCE_SORT = "relevance";
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "configurationService")
   private ConfigurationService configurationService;
	
	@Override
	protected List<SortData> buildSorts(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, IndexedTypeSort, SEARCH_RESULT_TYPE> source)
	{
		final List<SortData> result = new ArrayList<>();

		final SearchResult searchResult = (SearchResult) source.getSearchResult();
		if(searchResult != null) {
			final IndexedTypeSort currentNamedSort = searchResult.getCurrentNamedSort();
			final String currentNamedSortCode = currentNamedSort != null ? currentNamedSort.getCode() : null;

			final List<IndexedTypeSort> sorts = ((SearchResult) source.getSearchResult()).getAvailableNamedSorts();
			for (final IndexedTypeSort sort : sorts) {
				if (!isInvalidSort(sort)) {
					addSortData(result, currentNamedSortCode, sort);
				}
				
			}
		}
		return result;
	}
	
	private boolean isInvalidSort(final IndexedTypeSort sort) {
		UserModel user = userService.getCurrentUser();
		boolean invalidSort = false;
		if(userService.isAnonymousUser(user)) {
			final String guestUserInvalidSort = configurationService.getConfiguration().getString(GUEST_USER_INVALID_SORTS);
			final String[] guestUserInvalidSorts = guestUserInvalidSort.split(",");
			boolean contains = Arrays.stream(guestUserInvalidSorts).anyMatch(sort.getCode()::equals);
			if(contains) {
				invalidSort = true;
			} 
		} else if (user instanceof B2BCustomerModel b2bCustomer) {
			
			final B2BUnitModel b2bUnit = b2bCustomer.getDefaultB2BUnit();
			if (Objects.nonNull((b2bUnit.getSapCustomerGroup())) && "02".equals(b2bUnit.getSapCustomerGroup().getCode())) {
				
				final String retailUserInvalidSort = configurationService.getConfiguration().getString(RETAIL_USER_INVALID_SORTS);
				final String[] retailUserInvalidSorts = retailUserInvalidSort.split(",");
				boolean contains = Arrays.stream(retailUserInvalidSorts).anyMatch(sort.getCode()::equals);
				if(contains) {
					invalidSort = true;
				} 
			} else {
				
				final String wholesaleUserInvalidSort = configurationService.getConfiguration().getString(WHOLESALE_USER_INVALID_SORTS);
				final String[] wholesaleUserInvalidSorts = wholesaleUserInvalidSort.split(",");
				boolean contains = Arrays.stream(wholesaleUserInvalidSorts).anyMatch(sort.getCode()::equals);
				if(contains) {
					invalidSort = true;
				} 
			}
		}
		if (sort.getCode().equalsIgnoreCase(RELEVANCE_SORT)) {
			invalidSort = true;
		}
		return invalidSort;
		
	}

	
}
