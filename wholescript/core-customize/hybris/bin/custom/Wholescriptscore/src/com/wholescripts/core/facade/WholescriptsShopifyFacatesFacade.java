/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import com.wholescripts.data.ShopifySearchResultData;


public interface WholescriptsShopifyFacatesFacade {

	void buildShopifyFacate(final List<ProductModel> productModels, final ShopifySearchResultData shopifySearchResult);


}
