/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.data.OtherAvailableProductsData;


public class WholescriptsMedPaxUrlValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

	private static final Logger LOG = Logger.getLogger(WholescriptsMedPaxUrlValueProvider.class);
	private static final String SAP_BASIC_MATERIAL = "wholescripts.pdp.medpax.product.prefix";

	private FieldNameProvider fieldNameProvider;

	@Resource(name = "productService")
	ProductService productService;
	
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	protected FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException {

		if (model instanceof ProductModel mainProduct) {
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			final String sapBasicMaterial = mainProduct.getSapBasicMaterial();
			if (StringUtils.isNotBlank(sapBasicMaterial)) {
				final String productCode = Config.getParameter(SAP_BASIC_MATERIAL) + sapBasicMaterial;
				try {
					final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
							configurationService.getConfiguration().getString("wholescripts.product.catalog"),
							configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
					
					final ProductModel medpaxProduct = productService.getProductForCode(catalogVersion, productCode);
					if (Objects.nonNull(medpaxProduct) && !productCode.equals(mainProduct.getCode())) {
						boolean isMedPax = medpaxProduct.getIsMedpax();
						if (isMedPax) {
							final String medPaxUrl = "?q=\"" + medpaxProduct.getName() + "\"";
							fieldValues.addAll(createFieldValue(indexedProperty, mainProduct, medPaxUrl));
						}

					}
				}
				catch (Exception e) {
					LOG.error("Error in MedPaxUrl provider " + e.getMessage());
				}
			}
			return fieldValues;

		} else {
			throw new FieldValueProviderException("Product is not type of product model");
		}
		
	}


	protected Collection<FieldValue> createFieldValue(final IndexedProperty indexedProperty, final ProductModel product,
			final String medPaxUrl) {
		
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		addFieldValues(fieldValues, indexedProperty, medPaxUrl);
		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value) {
		
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames) {
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}
}
