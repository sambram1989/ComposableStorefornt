/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


public class WholescriptsProductDefaultPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

	private static final String WHOLESALE_GROUP = "wholescripts.wholesale.group";
	private static final String GUEST_GROUP = "wholescripts.guest.group";

	private FieldNameProvider fieldNameProvider;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException {

		if (model instanceof ProductModel productModel) {

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			final Collection<PriceRowModel> priceRows = productModel.getEurope1Prices();
			String priceGroup = null;

			if (productModel.getIsMedpax()) {
				priceGroup = Config.getParameter(WHOLESALE_GROUP);

			}
			else {
				priceGroup = Config.getParameter(GUEST_GROUP);
			}
			if (CollectionUtils.isNotEmpty(priceRows)) {
				final Double value = getPriceValue(priceRows, priceGroup);
				if (value != null) {
					for (final CurrencyModel currency : indexConfig.getCurrencies()) {
						fieldValues.addAll(createFieldValue(indexedProperty, productModel, value, currency));
					}
				}
			}
			return fieldValues;
		}
		else {
			throw new FieldValueProviderException("Product is not type of product model");
		}

	}

	protected Collection<FieldValue> createFieldValue(final IndexedProperty indexedProperty, final ProductModel product,
			final Double price, final CurrencyModel currency) {

		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		addFieldValues(fieldValues, indexedProperty, price, currency);
		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value,
			final CurrencyModel currency) {

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				currency.getIsocode().toLowerCase());
		for (final String fieldName : fieldNames) {
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected Double getPriceValue(final Collection<PriceRowModel> priceRows, final String priceGroup)
			throws FieldValueProviderException {

		final PriceRowModel priceRow = priceRows.stream()
				.filter(pr -> pr.getUg() != null && priceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
		if (priceRow != null && priceRow.getPrice() != null) {
			return priceRow.getPrice();
		}

		return null;
	}

	protected FieldNameProvider getFieldNameProvider() {
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider) {
		this.fieldNameProvider = fieldNameProvider;
	}


}


