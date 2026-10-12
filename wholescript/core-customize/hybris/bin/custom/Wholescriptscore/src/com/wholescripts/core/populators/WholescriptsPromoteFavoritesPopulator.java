/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.FreeTextQueryBuilder;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.wholescripts.core.service.WholescriptsUserService;


public class WholescriptsPromoteFavoritesPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>> {

	private static final String FAVOURITE_PAGE = "FavouritesPage";
	private static final String GUEST_GROUP = "wholescripts.guest.group";
	private static final String RETAIL_GROUP = "wholescripts.retail.group";
	private static final String WHOLESALE_GROUP = "wholescripts.wholesale.group";
	
	
	@Resource(name = "wholescriptsUserService")
	WholescriptsUserService wholescriptsUserService;
	
	@Resource(name = "userService")
	private UserService userService;

	private List<FreeTextQueryBuilder> favouritesPageQueryBuilders;


	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target) {

		final String cleanedFreeTextSearch = cleanupSolrSearchText(target.getSearchQueryData().getFreeTextSearch());
		final SearchQuery searchQuery = target.getSearchQuery();

		if (StringUtils.isBlank(cleanedFreeTextSearch) && wholescriptsUserService.isWishlistAvailableForUser()) {
			List<ProductModel> products = wholescriptsUserService.getFavouriteProducts();
			products = sortPromotedProducts(searchQuery, products);
			products.forEach(product -> searchQuery.addPromotedItem(product.getPk()));
		}
		else if (FAVOURITE_PAGE.equalsIgnoreCase(cleanedFreeTextSearch)) {
			//Split the full text string to separate words
			final String[] words = cleanedFreeTextSearch.split("\\s+");
			favouritePageQueryBuilder(searchQuery, cleanedFreeTextSearch, words);
		}

	}

	protected String cleanupSolrSearchText(final String text) {
		String cleanedText = text;
		if (text == null) {
			cleanedText = "";
		}
		else {
			// Replace ':' special character in solr text with whitespace
			cleanedText = cleanedText.replace(':', ' ').trim();

			// Replace SOLR keywords with lowercased
			cleanedText = cleanedText.replace("AND", "and");
			cleanedText = cleanedText.replace("OR", "or");
		}

		return cleanedText;
	}

	private void favouritePageQueryBuilder(final SearchQuery searchQuery, final String cleanedFreeTextSearch, final String[] words) {
		for (final FreeTextQueryBuilder favouriteTextQueryBuilder : getFavouritesPageQueryBuilders()) {
			favouriteTextQueryBuilder.addFreeTextQuery(searchQuery, cleanedFreeTextSearch, words);
		}
	}
	
	private List<ProductModel> sortPromotedProducts(final SearchQuery searchQuery, final List<ProductModel> products) {
		
		if (searchQuery.getNamedSort() != null && searchQuery.getNamedSort().equalsIgnoreCase("name-asc")) {
			Collections.sort(products, (ProductModel p1, ProductModel p2) -> {
				return p1.getName().compareToIgnoreCase(p2.getName());
			});
		}
		
		if (searchQuery.getNamedSort() != null && searchQuery.getNamedSort().equalsIgnoreCase("name-desc")) {
			Collections.sort(products, (ProductModel p1, ProductModel p2) -> {
				return p2.getName().compareToIgnoreCase(p1.getName());
			});
		}
		
		String priceGroup = null;
		
		UserModel user = userService.getCurrentUser();
		if(userService.isAnonymousUser(user)) {
			priceGroup = Config.getParameter(GUEST_GROUP);
		} else if (user instanceof B2BCustomerModel b2bCustomer) {
			final B2BUnitModel b2bUnit = b2bCustomer.getDefaultB2BUnit();
			if ("02".equals(b2bUnit.getSapCustomerGroup().getCode())) {
				priceGroup = Config.getParameter(RETAIL_GROUP);
			} else {
				priceGroup = Config.getParameter(WHOLESALE_GROUP);
			}
		}
		
		final String userPriceGroup = priceGroup;
		
		if (searchQuery.getNamedSort() != null && searchQuery.getNamedSort().contains("price-asc")) {
			Collections.sort(products, (ProductModel p1, ProductModel p2) -> {
				return Double.compare(getPriceValue(p1.getEurope1Prices(), userPriceGroup), getPriceValue(p2.getEurope1Prices(), userPriceGroup));
			});
		}
		
		if (searchQuery.getNamedSort() != null && searchQuery.getNamedSort().contains("price-desc")) {
			Collections.sort(products, (ProductModel p1, ProductModel p2) -> {
				return Double.compare(getPriceValue(p2.getEurope1Prices(), userPriceGroup), getPriceValue(p1.getEurope1Prices(), userPriceGroup));
			});
		}
		return products;
	}
	
	protected Double getPriceValue(final Collection<PriceRowModel> priceRows, final String customerPriceGroup) {

		Double value = 0.0;
		PriceRowModel priceRow = priceRows.stream()
				.filter(pr -> pr.getUg() != null && customerPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
		if (priceRow != null && priceRow.getPrice() != null) {
			return priceRow.getPrice();
		}
		return value;
	}
	
	public List<FreeTextQueryBuilder> getFavouritesPageQueryBuilders() {
		return favouritesPageQueryBuilders;
	}

	
	public void setFavouritesPageQueryBuilders(final List<FreeTextQueryBuilder> favouritesPageQueryBuilders) {
		this.favouritesPageQueryBuilders = favouritesPageQueryBuilders;
	}
}
