/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import java.util.List;

import com.wholescripts.core.model.WsPopularCategoriesModel;
import com.wholescripts.core.model.WsPopularProductsModel;
import de.hybris.platform.core.model.order.OrderModel;


public interface WholescriptsPopularCategoriesService
{

	List<WsPopularCategoriesModel> getPopularCategoriesDetails();

	List<WsPopularProductsModel> getPopularProductsDetails(final String categoryCode);

	WsPopularProductsModel getPopularProductsDetails(final String categoryCode,final String productCode);

	WsPopularCategoriesModel getPopularCategoriesDetails(final String categoryCode);

	void updateProductRankingFromOrders(OrderModel order);
}
