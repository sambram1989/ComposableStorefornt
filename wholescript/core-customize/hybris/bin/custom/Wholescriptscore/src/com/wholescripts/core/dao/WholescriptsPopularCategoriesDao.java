/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import java.util.List;

import com.wholescripts.core.model.WsPopularCategoriesModel;
import com.wholescripts.core.model.WsPopularProductsModel;


public interface WholescriptsPopularCategoriesDao
{

	List<WsPopularCategoriesModel> getPopularCategoriesDetails();

	List<WsPopularProductsModel> getPopularProductsDetails(final String categoryCode);

	WsPopularProductsModel getPopularProductsDetails(final String categoryCode,final String productCode);

	WsPopularCategoriesModel getPopularCategoriesDetails(final String categoryCode);

}
