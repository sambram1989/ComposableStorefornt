package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.model.CustomerClassProductRestrictionModel;
import com.wholescripts.core.model.CustomerProductRestrictionModel;
import com.wholescripts.core.service.WholescriptsProductsService;


public class WholescriptsCustomerClassProductRestrictionProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

	private static final Logger LOG = Logger.getLogger(WholescriptsCustomerClassProductRestrictionProvider.class);
	
	private FieldNameProvider fieldNameProvider;

	@Resource(name = "wholescriptsProductsService")
   WholescriptsProductsService wholescriptsProductsService;

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

		if (model instanceof ProductModel productModel) {
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			
			final List<CustomerClassProductRestrictionModel> customerClassProductRestrictions = wholescriptsProductsService.getCustomerClassProductRestrictions(productModel.getCode());
			if (CollectionUtils.isNotEmpty(customerClassProductRestrictions)) {
				try {
					
					final List<String> customerClasses = new ArrayList<String>();
					
					customerClassProductRestrictions.forEach(customerProductRestriction -> customerClasses.add(customerProductRestriction.getCustomerClass()));
					
					if (CollectionUtils.isNotEmpty(customerClasses)) {
						
						for (final String customerClass : customerClasses) {
							fieldValues.addAll(createFieldValue(indexedProperty, productModel, customerClass));
						}
						
					}
				}
				catch (Exception e) {
					LOG.error("Error in Customer Customer Product Restriction provider " + e.getMessage());
				}
			}
			return fieldValues;

		} else {
			throw new FieldValueProviderException("Product is not type of product model");
		}
		
	}


	protected Collection<FieldValue> createFieldValue(final IndexedProperty indexedProperty, final ProductModel product,
			final String customerSapId) {
		
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		addFieldValues(fieldValues, indexedProperty, customerSapId);
		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value) {
		
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames) {
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}
}
