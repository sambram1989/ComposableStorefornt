/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.ModelAttributesValueResolver;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.wholescripts.core.model.ContentSearchModel;

import java.util.Optional;


public class WholescriptsCategoryValueResolver extends ModelAttributesValueResolver<ContentSearchModel>
{
	private static final Logger LOG = Logger.getLogger(WholescriptsCategoryValueResolver.class);

	@Resource(name = "categoryModelUrlResolver")
	private UrlResolver<CategoryModel> categoryModelUrlResolver;


	@Override
	protected Object getAttributeValue(final IndexedProperty indexedProperty, final ContentSearchModel contentSearchModel,
			final String attributeName)
			throws FieldValueProviderException
	{
		return getModelAttributeValue(contentSearchModel, attributeName);
	}

	protected Object getModelAttributeValue(final ContentSearchModel contentSearchModel, final String attributeName)
	{
		final CategoryModel category = contentSearchModel.getCategory();
		if (!(category instanceof CategoryModel))
		{
			return null;
		}

		// Fetch the composed type for the brand
		final ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(category.getClass());

		if (attributeName.equals("categoryPath"))
		{
			//return categoryModelUrlResolver.resolve((CategoryModel) category);
			return Optional.ofNullable(category.getCategoryUrl()).orElse("");
		}

		// Check if the composed type has the specified attribute
		if (!getTypeService().hasAttribute(composedType, attributeName))
		{
			return null;
		}

		// Retrieve the attribute value
		Object value = getModelService().getAttributeValue(category, attributeName);

		// Transform the value based on its type
		if (value instanceof MediaModel)
		{
			value = ((MediaModel) value).getURL();
		}

		return value;
	}


}
