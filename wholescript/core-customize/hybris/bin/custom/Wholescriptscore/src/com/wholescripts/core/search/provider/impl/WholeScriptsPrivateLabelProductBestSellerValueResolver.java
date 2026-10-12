/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.WsPopularProductsModel;
import com.wholescripts.core.service.WholescriptsPopularCategoriesService;

/**
 *
 */
public class WholeScriptsPrivateLabelProductBestSellerValueResolver extends AbstractValueResolver<CustomerMaterialInformationModel, Object, Object>
{
	private static final String BEST_SELLAR_COUNT = "wholescripts.bestSeller.products.count";
	@Resource(name = "productService")
	ProductService productService;
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "wholescriptsPopularCategoriesService")
	private WholescriptsPopularCategoriesService wholescriptsPopularCategoriesService;
	@Resource(name="commerceProductService")
	private CommerceProductService commerceProductService;

	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final CustomerMaterialInformationModel custMatInfoModel,
			final ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if (StringUtils.isNotEmpty(custMatInfoModel.getProductCode()))
		{
			try
			{
				final ProductModel productModel = sessionService.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public ProductModel execute()
					{
						searchRestrictionService.disableSearchRestrictions();
						final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
								configurationService.getConfiguration().getString("wholescripts.product.catalog"),
								configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
						return productService.getProductForCode(catalogVersion, String.format("%018d",Integer.valueOf(custMatInfoModel.getProductCode())));
					}
				});
				if(Objects.nonNull(productModel)) {
					Collection<CategoryModel> categories=commerceProductService.getSuperCategoriesExceptClassificationClassesForProduct(productModel);
					final List<WsPopularProductsModel> bestSellerProducts = new ArrayList<WsPopularProductsModel>();
					String count = Config.getParameter(BEST_SELLAR_COUNT);
					long productCount = 0;
					if(StringUtils.isNotBlank(count))
					{
						productCount = Long.parseLong(count);
					}
					if(CollectionUtils.isNotEmpty(categories))
					{
						boolean isBestSellarFound=false;
						for (final CategoryModel category : categories)
						{
							if(category != null && !isBestSellarFound )
							{
								List<WsPopularProductsModel> popularProducts= wholescriptsPopularCategoriesService.getPopularProductsDetails(category.getCode());
								bestSellerProducts.addAll(popularProducts.stream()
										.sorted(Comparator.comparing(WsPopularProductsModel::getWeight).reversed())
										.limit(productCount).collect(Collectors.toSet()));
								if(CollectionUtils.isNotEmpty(bestSellerProducts))
								{
									isBestSellarFound=  getBestSellerValue(bestSellerProducts,productModel);

								}
							}
						}
						inputDocument.addField(indexedProperty, isBestSellarFound, valueResolverContext.getFieldQualifier());
					}
				}
			}
			catch (final Exception e)
			{
				// XXX: handle exception
			}
		}
	}

	private boolean getBestSellerValue(List<WsPopularProductsModel> bestSellerProducts, ProductModel productModel) {
		boolean isBestSellarFound=false;
		for(final WsPopularProductsModel product : bestSellerProducts)
		{
			if(product!=null && product.getProductCode().equals(productModel.getCode()))
			{
				isBestSellarFound=true;
				break;
			}


		}
		return isBestSellarFound;
	}
}
