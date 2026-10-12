/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import com.wholescripts.core.dao.WholescriptsPopularCategoriesDao;
import com.wholescripts.core.model.WsPopularCategoriesModel;
import com.wholescripts.core.model.WsPopularProductsModel;
import com.wholescripts.core.service.WholescriptsPopularCategoriesService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


public class WholescriptsPopularCategoriesServiceImpl implements WholescriptsPopularCategoriesService
{
	private final static Logger LOG = Logger.getLogger(WholescriptsPopularCategoriesServiceImpl.class);

	@Resource(name = "wholescriptsPopularCategoriesDao")
	private WholescriptsPopularCategoriesDao wholescriptsPopularCategoriesDao;

	@Resource(name="commerceProductService")
	private CommerceProductService commerceProductService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	public List<WsPopularCategoriesModel> getPopularCategoriesDetails()
	{
		return wholescriptsPopularCategoriesDao.getPopularCategoriesDetails();
	}

	@Override
	public List<WsPopularProductsModel> getPopularProductsDetails(final String categoryCode)
	{
		return wholescriptsPopularCategoriesDao.getPopularProductsDetails(categoryCode);
	}

	@Override
	public WsPopularProductsModel getPopularProductsDetails(final String categoryCode, final String productCode) {
		return wholescriptsPopularCategoriesDao.getPopularProductsDetails(categoryCode,productCode);
	}

	@Override
	public WsPopularCategoriesModel getPopularCategoriesDetails(String categoryCode) {
		return wholescriptsPopularCategoriesDao.getPopularCategoriesDetails(categoryCode);
	}

	@Override
	public void updateProductRankingFromOrders(OrderModel order) {
		try{
			for (final AbstractOrderEntryModel entry : order.getEntries()) {
				String  productCode=entry.getProduct().getCode();
				final List<String> excludedProducts = Arrays.asList(Config.getParameter("medpax.product.exclude").split(","));
				if (!excludedProducts.contains(productCode) && CollectionUtils.isEmpty(entry.getEntryGroupNumbers())) {
					//add catalog for  each entry
					try{
						WsPopularProductsModel existingPPModel = getPopularProductsDetails("catalog", productCode);
						updatePopularProductRank(existingPPModel,entry,productCode);

					} catch (Exception e) {
						LOG.error("Unable to update Rank for Popular Product  catalog for order" + order.getCode()  + e.getMessage());
					}
					try {
						WsPopularCategoriesModel existingCModel = getPopularCategoriesDetails("catalog");
						updatePopularCategoryRank(existingCModel,entry,"catalog");

					} catch (Exception e) {
						LOG.error("Unable to update Rank for Popular Categories catalog for order" + order.getCode()+ e.getMessage());

					}
					Collection<CategoryModel> categories=commerceProductService.getSuperCategoriesExceptClassificationClassesForProduct(entry.getProduct());
					if(CollectionUtils.isNotEmpty(categories))
					{
						for (final CategoryModel cat  : categories) {
								String categoryCode = cat.getCode();
								try {
									WsPopularProductsModel existingPPModel = getPopularProductsDetails(categoryCode, productCode);
									updatePopularProductRank(existingPPModel,entry,productCode);
								} catch (Exception e) {
									LOG.error("Unable to update Rank for Popular Product for order" + order.getCode()  + e.getMessage());
								}

								try {
									WsPopularCategoriesModel existingCModel = getPopularCategoriesDetails(categoryCode);
									updatePopularCategoryRank(existingCModel,entry,categoryCode);
								} catch (Exception e) {
									LOG.error("Unable to update Rank for Popular Categories for order" + order.getCode()+ e.getMessage());

								}

						}
					}
				}

			}

		} catch (Exception e) {
			LOG.error("Unable to update Rank for Popular Product/Categories" + order.getCode() + e.getMessage());

		}

	}

	private void updatePopularCategoryRank(WsPopularCategoriesModel existingCModel, AbstractOrderEntryModel entry, String categoryCode) {
		if (Objects.nonNull(existingCModel)) {
			Integer weight = existingCModel.getWeight();
			existingCModel.setWeight((int) (weight + entry.getQuantity()));
			modelService.save(existingCModel);

		} else {
			WsPopularCategoriesModel popularCategoriesModel = modelService.create(WsPopularCategoriesModel.class);
			popularCategoriesModel.setCategoryCode(categoryCode);
			popularCategoriesModel.setWeight(Math.toIntExact(entry.getQuantity()));
			modelService.save(popularCategoriesModel);
		}
	}

	private void updatePopularProductRank(WsPopularProductsModel existingPPModel, AbstractOrderEntryModel entry, String productCode) {
		if (Objects.nonNull(existingPPModel)) {
			Integer weight = existingPPModel.getWeight();
			existingPPModel.setWeight((int) (weight + entry.getQuantity()));
			modelService.save(existingPPModel);
		} else {
			WsPopularProductsModel popularProductsModel = modelService.create(WsPopularProductsModel.class);
			popularProductsModel.setProductCode(productCode);
			popularProductsModel.setCategoryCode("catalog");
			popularProductsModel.setWeight(Math.toIntExact(entry.getQuantity()));
			modelService.save(popularProductsModel);
		}
	}

}
