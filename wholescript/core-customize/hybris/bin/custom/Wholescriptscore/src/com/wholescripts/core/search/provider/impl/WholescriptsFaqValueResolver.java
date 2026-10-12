/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.ModelAttributesValueResolver;

import org.apache.log4j.Logger;

import com.wholescripts.core.model.ContentSearchModel;
import com.wholescripts.core.model.WholescriptsFaqModel;


public class WholescriptsFaqValueResolver extends ModelAttributesValueResolver<ContentSearchModel>
{
	private static final Logger LOG = Logger.getLogger(WholescriptsFaqValueResolver.class);

	@Override
	protected Object getAttributeValue(final IndexedProperty indexedProperty, final ContentSearchModel contentSearchModel,
			final String attributeName)
			throws FieldValueProviderException
	{
		return getModelAttributeValue(contentSearchModel, attributeName);
	}

	protected Object getModelAttributeValue(final ContentSearchModel contentSearchModel, final String attributeName)
	{
		final WholescriptsFaqModel faq = contentSearchModel.getFaq();
		if (null == faq)
		{
			return null;
		}
		// Fetch the composed type for the brand
		final ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(faq.getClass());

		// Check if the composed type has the specified attribute
		if (!getTypeService().hasAttribute(composedType, attributeName))
		{
			return null;
		}

		// Retrieve the attribute value
		final Object value = getModelService().getAttributeValue(faq, attributeName);

		return value;
	}
}
