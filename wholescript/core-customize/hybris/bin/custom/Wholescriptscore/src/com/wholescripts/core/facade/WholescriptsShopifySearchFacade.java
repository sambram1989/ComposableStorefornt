
package com.wholescripts.core.facade;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;


public interface WholescriptsShopifySearchFacade<ITEM extends ProductData> {
	
	ProductSearchPageData<SearchStateData, ITEM> searchShopiyProducts(SearchStateData searchState,
			PageableData pageableData);

}