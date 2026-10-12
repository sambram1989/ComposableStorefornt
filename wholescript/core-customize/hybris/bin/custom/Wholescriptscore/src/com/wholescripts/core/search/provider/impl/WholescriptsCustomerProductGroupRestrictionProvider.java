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

import com.wholescripts.core.model.CustomerProductGroupRestrictionModel;
import com.wholescripts.core.service.WholescriptsProductsService;


public class WholescriptsCustomerProductGroupRestrictionProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

	private static final Logger LOG = Logger.getLogger(WholescriptsCustomerProductGroupRestrictionProvider.class);
	
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

		if (model instanceof final ProductModel productModel) {
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			if (productModel.getSapMaterialGroup() != null) {


				final List<CustomerProductGroupRestrictionModel> customerProductGroupRestrictions = wholescriptsProductsService
						.getCustomerProductGroupRestrictions(productModel.getSapMaterialGroup().getCode());
				if (CollectionUtils.isNotEmpty(customerProductGroupRestrictions)) {
					try {
						
						final List<String> customerSapIds = new ArrayList<String>();

						customerProductGroupRestrictions
								.forEach(customerProductRestriction -> customerSapIds.add(customerProductRestriction.getCustomerSAPId()));

						if (CollectionUtils.isNotEmpty(customerSapIds)) {

							for (final String customerSapId : customerSapIds) {
								fieldValues.addAll(createFieldValue(indexedProperty, productModel, customerSapId.replaceFirst("^0+", "")));
							}

						}
					}
					catch (final Exception e) {
						LOG.error("Error in Customer Customer Product Group Restriction provider " + e.getMessage());
					}
				}
			}
			return fieldValues;

		} else {
			throw new FieldValueProviderException("Product is not type of product model OR material group is empty");
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
