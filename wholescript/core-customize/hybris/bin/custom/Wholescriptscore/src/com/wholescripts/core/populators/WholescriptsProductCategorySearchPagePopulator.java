/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.ProductCategorySearchPagePopulator;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


public class WholescriptsProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ITEM extends ProductData, SCAT, CATEGORY> extends ProductCategorySearchPagePopulator<QUERY, STATE, RESULT,ProductData, SCAT, CATEGORY>
{
	@Resource(name = "productService")
	private ProductService productService;
	private String code;

	private static final Logger LOG = Logger.getLogger(WholescriptsProductCategorySearchPagePopulator.class);

	@Override
	public void populate(final ProductCategorySearchPageData<QUERY, RESULT, SCAT> source,
			final ProductCategorySearchPageData<STATE, ProductData, CATEGORY> target)
	{
		target.setFreeTextSearch(source.getFreeTextSearch());
		target.setCategoryCode(source.getCategoryCode());

		if (source.getSubCategories() != null)
		{
			target.setSubCategories(Converters.convertAll(source.getSubCategories(), getCategoryConverter()));
		}

		if (source.getBreadcrumbs() != null)
		{
			target.setBreadcrumbs(Converters.convertAll(source.getBreadcrumbs(), getBreadcrumbConverter()));
		}

		target.setCurrentQuery(getSearchStateConverter().convert(source.getCurrentQuery()));

		if (source.getFacets() != null)
		{
			target.setFacets(Converters.convertAll(source.getFacets(), getFacetConverter()));
		}

		target.setPagination(source.getPagination());

		if (source.getResults() != null)
		{
			final List<RESULT> filteredList = new ArrayList<>(source.getResults());
			long filteredNum=0;
			for(final RESULT data : source.getResults()) {
				final SearchResultValueData resultData =	(SearchResultValueData) data;
				final String productCode = (String) resultData.getValues().get(getCode());
				try {
					productService.getProductForCode(productCode);
				}catch (final UnknownIdentifierException e) {
					filteredList.remove(data);
					filteredNum++;
				}
			}
			target.setExcludedProductCount(filteredNum);
			target.setResults(Converters.convertAll(filteredList, getSearchResultProductConverter()));
			
			final PaginationData modifyPaginationData =	source.getPagination();
			
			LOG.info("WholescriptsProductCategorySearchPagePopulator, original pagination, number of pages "
					+ modifyPaginationData.getNumberOfPages()
					+" number of results " + modifyPaginationData.getTotalNumberOfResults()
					+" page size " + modifyPaginationData.getPageSize());
			
			if(filteredNum > 0) {
				
				long modifiedNumberOfResult = modifyPaginationData.getTotalNumberOfResults() - filteredNum;
				
				if (modifyPaginationData.getPageSize() > 0 ) {
					final int numOfPages= calculateTotalPages((int)modifiedNumberOfResult, modifyPaginationData.getPageSize());
					modifyPaginationData.setNumberOfPages(numOfPages);
				}

				modifyPaginationData.setTotalNumberOfResults(modifiedNumberOfResult);

				LOG.info("WholescriptsProductCategorySearchPagePopulator, modified pagination, number of pages "
						+ modifyPaginationData.getNumberOfPages()
						+" number of results " + modifyPaginationData.getTotalNumberOfResults()
						+" page size " + modifyPaginationData.getPageSize());

				target.setPagination(modifyPaginationData);
				
			}
		}

		target.setSorts(source.getSorts());

		if (source.getSpellingSuggestion() != null)
		{
			target.setSpellingSuggestion(getSpellingSuggestionConverter().convert(source.getSpellingSuggestion()));
		}

		target.setKeywordRedirectUrl(source.getKeywordRedirectUrl());
	}

	 private int calculateTotalPages(final int totalResults, final int pageSize) {
		 return (totalResults + pageSize - 1) / pageSize;
   }

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}
}
