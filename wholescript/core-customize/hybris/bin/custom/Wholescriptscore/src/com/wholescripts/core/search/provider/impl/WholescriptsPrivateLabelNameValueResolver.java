/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
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

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.model.CustomerMaterialInformationModel;


/**
 *
 */
public class WholescriptsPrivateLabelNameValueResolver
		extends AbstractValueResolver<CustomerMaterialInformationModel, Object, Object>
{
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
	
	@Resource(name = "classificationService")
	private ClassificationService classificationService;

	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final CustomerMaterialInformationModel custMatInfoModel,
			final ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
				configurationService.getConfiguration().getString("wholescripts.product.catalog"),
				configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));

		if (StringUtils.isNotEmpty(custMatInfoModel.getProductCode()))
		{
			try
			{
				final ProductModel product = sessionService.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public ProductModel execute()
					{
						searchRestrictionService.disableSearchRestrictions();
						return productService.getProductForCode(catalogVersion,String.format("%018d",Integer.valueOf(custMatInfoModel.getProductCode())));
					}
				});
				if (StringUtils.isNotEmpty(custMatInfoModel.getBlueLabel()))
				{
					final ProductModel blueLabelProduct = sessionService.executeInLocalView(
							new SessionExecutionBody()
					{
						@Override
						public ProductModel execute()
						{
							searchRestrictionService.disableSearchRestrictions();
							return productService.getProductForCode(catalogVersion,custMatInfoModel.getBlueLabel());
						}
					});
					if(Objects.nonNull(blueLabelProduct))
					{
						String blueLabelProductName=getProductName(blueLabelProduct);

							inputDocument.addField(indexedProperty, blueLabelProductName, valueResolverContext.getFieldQualifier());

					}
					else {

						if(Objects.nonNull(product))
						{
							String productName=getProductName(product);
							inputDocument.addField(indexedProperty, productName, valueResolverContext.getFieldQualifier());
						}
					}
				}
				else {

					if(Objects.nonNull(product))
					{
						String productName=getProductName(product);
						inputDocument.addField(indexedProperty, productName, valueResolverContext.getFieldQualifier());
					}
				}

			}
			catch (final Exception e)
			{
				// XXX: handle exception
			}
		}

	}

	private String getProductName(ProductModel product) {
		String attributeValue = null;
			final FeatureList featureList = classificationService.getFeatures(product);
			final Feature pimProductName = featureList.getFeatureByCode(configurationService.getConfiguration().getString(WholescriptscoreConstants.CLASSIFICATION_ATTRIBUTE_PRODUCT_NAME));

			if (Objects.nonNull(pimProductName)) {
				final FeatureValue productNameValue = pimProductName.getValue();
				if (Objects.nonNull(productNameValue)) {
					attributeValue = productNameValue.getValue().toString();
				}
				else {
					attributeValue = product.getName();
				}
			}
			else {
				attributeValue = product.getName();
			}

		return attributeValue;
	}

}
