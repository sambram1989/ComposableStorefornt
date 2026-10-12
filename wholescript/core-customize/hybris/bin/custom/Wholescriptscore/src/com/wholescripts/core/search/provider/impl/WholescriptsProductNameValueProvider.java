/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.constants.WholescriptscoreConstants;


public class WholescriptsProductNameValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

	private static final Logger LOG = Logger.getLogger(WholescriptsProductNameValueProvider.class);

	private FieldNameProvider fieldNameProvider;

	@Resource(name = "classificationService")
	private ClassificationService classificationService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException {

		if (model instanceof final ProductModel mainProduct) {
			String attributeValue = null;
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			final FeatureList featureList = classificationService.getFeatures(mainProduct);
			final Feature pimProductName = featureList.getFeatureByCode(configurationService.getConfiguration().getString(WholescriptscoreConstants.CLASSIFICATION_ATTRIBUTE_PRODUCT_NAME));

			if (Objects.nonNull(pimProductName)) {
				final FeatureValue productNameValue = pimProductName.getValue();
				if (Objects.nonNull(productNameValue)) {
					attributeValue = productNameValue.getValue().toString();
				}
				else {
					attributeValue = mainProduct.getName();
				}
			}
			else {
				attributeValue = mainProduct.getName();

			}
			if (attributeValue != null) {
				if (indexedProperty.isLocalized()) {
					final Collection<LanguageModel> languages = indexConfig.getLanguages();
					for (final LanguageModel language : languages) {
						fieldValues.addAll(createFieldValue(attributeValue, language, indexedProperty));
					}
				}
			}
			return fieldValues;
		}
		return Collections.emptyList();

	}


	protected List<FieldValue> createFieldValue(final String value, final LanguageModel language,
			final IndexedProperty indexedProperty) {

		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		if (language != null) {
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, language.getIsocode());
			for (final String fieldName : fieldNames) {
				fieldValues.add(new FieldValue(fieldName, value));
			}
		}
		else {
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
			for (final String fieldName : fieldNames) {
				fieldValues.add(new FieldValue(fieldName, value));
			}
		}

		return fieldValues;
	}

	protected FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}
}
