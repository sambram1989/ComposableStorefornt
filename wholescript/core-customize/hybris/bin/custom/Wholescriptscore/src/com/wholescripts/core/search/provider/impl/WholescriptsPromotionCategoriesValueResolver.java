/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.PromotionCategoryModel;

/**
 *
 */
public class WholescriptsPromotionCategoriesValueResolver extends AbstractValueResolver<ProductModel, Object, Object>
{

	@Override
	protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext arg1, IndexedProperty indexedProperty, ProductModel product,
			ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if(CollectionUtils.isNotEmpty(product.getPromtionCategory())) {
			for(PromotionCategoryModel promoCat: product.getPromtionCategory()) {
				inputDocument.addField(indexedProperty, promoCat.getCode(), valueResolverContext.getFieldQualifier());
			}
		}
	}
}
