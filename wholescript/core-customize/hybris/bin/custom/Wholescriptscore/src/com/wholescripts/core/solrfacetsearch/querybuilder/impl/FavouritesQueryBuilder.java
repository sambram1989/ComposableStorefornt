/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.solrfacetsearch.querybuilder.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.impl.AbstractFreeTextQueryBuilder;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.core.service.WholescriptsWishlist2Service;


public class FavouritesQueryBuilder extends AbstractFreeTextQueryBuilder
{
	private String propertyName;
	private int boost;
	@Resource(name = "wholescriptsUserService")
	WholescriptsUserService wholescriptsUserService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "wholescriptsWishlist2Service")
	WholescriptsWishlist2Service wholescriptsWishlist2Service;

	protected String getPropertyName()
	{
		return propertyName;
	}

	@Required
	public void setPropertyName(final String propertyName)
	{
		this.propertyName = propertyName;
	}

	protected int getBoost()
	{
		return boost;
	}

	@Required
	public void setBoost(final int boost)
	{
		this.boost = boost;
	}

	@Override
	public void addFreeTextQuery(final SearchQuery searchQuery, final String fullText, final String[] textWords)
	{
		final IndexedType indexedType = searchQuery.getIndexedType();
		if (indexedType != null)
		{
			final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get(getPropertyName());
			if (indexedProperty != null)
			{
				addFreeTextQuery(searchQuery, indexedProperty, fullText, getFavouriteProductCodes(fullText), getBoost());
			}
		}
	}

	private String[] getFavouriteProductCodes(final String fullText) {
		
		final List<String> productCodes = new ArrayList<String>();
		
		final UserModel currentUser = userService.getCurrentUser();
		 final List<Wishlist2Model> userWishLists = wholescriptsWishlist2Service.findWishlistsByUser(currentUser);
		 final List<ProductModel> wishListProducts = new ArrayList<ProductModel>();
		 userWishLists.forEach(userWishList -> userWishList.getEntries()
				 .forEach(wishlistEntry -> wishListProducts.add(wishlistEntry.getProduct())));
		 
		 wishListProducts.forEach(wishListProduct -> {
	        if(!wishListProduct.getIsMedpax()) {
	      	  productCodes.add(wishListProduct.getCode());
	        }
	    });
		 
		if (CollectionUtils.isNotEmpty(productCodes)) {
			productCodes.add(fullText);
		}
		// Convert List to an array
		return productCodes.toArray(new String[productCodes.size()]); // Fetch the current user
	}
}
