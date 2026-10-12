/*
* * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.ModelAttributesValueResolver;

import org.apache.log4j.Logger;

import com.wholescripts.core.model.ContentSearchModel;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.model.WholescriptsFaqModel;


public class WholescriptsContentTypeValueResolver extends ModelAttributesValueResolver<ContentSearchModel>
{
	private static final Logger LOG = Logger.getLogger(WholescriptsContentTypeValueResolver.class);
	private static final String CONTENT_TYPE_KEY = "contentType";
	private static final String CONTENT_TYPE_BRAND_VALUE = "Brand";
	private static final String CONTENT_TYPE_FAQ_VALUE = "Faq";
	private static final String CONTENT_TYPE_CATEGORY_VALUE = "Category";
	private static final String CONTENT_TYPE_INTEGRATIONS_VALUE = "Integrations";

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
		final WholescriptsBrandModel brand = contentSearchModel.getBrand();
		final ContentSlotForPageModel slot = contentSearchModel.getIntegration();
		final WholescriptsFaqModel faq = contentSearchModel.getFaq();

		if (null != brand)
		{
			return CONTENT_TYPE_BRAND_VALUE;
		}

		if (null != faq)
		{
			return CONTENT_TYPE_FAQ_VALUE;
		}

		if (null != category)
		{
			return CONTENT_TYPE_CATEGORY_VALUE;
		}

		if (null != slot)
		{
			return CONTENT_TYPE_INTEGRATIONS_VALUE;
		}

		return null;
	}
}
