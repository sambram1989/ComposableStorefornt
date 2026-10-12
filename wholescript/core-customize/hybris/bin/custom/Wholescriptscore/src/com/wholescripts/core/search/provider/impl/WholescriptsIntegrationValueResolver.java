/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import com.wholescripts.core.model.WsIntegrationBadgeParagraphComponentModel;
import com.wholescripts.core.model.WsIntegrationContentParagraphComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.ModelAttributesValueResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.wholescripts.core.model.ContentSearchModel;


public class WholescriptsIntegrationValueResolver extends ModelAttributesValueResolver<ContentSearchModel>
{
	private static final Logger LOG = Logger.getLogger(WholescriptsIntegrationValueResolver.class);
	private static final String KEY_MEDIA_URL = "mediaURL";
	private static final String KEY_CMS_CONTENT = "cmsContent";
	private static final String KEY_URL = "url";
	private static final String KEY_CMS_BADGE_CONTENT = "CmsBadgeContent";


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	protected Object getAttributeValue(final IndexedProperty indexedProperty, final ContentSearchModel contentSearchModel,
			final String attributeName)
			throws FieldValueProviderException
	{
		return getModelAttributeValue(contentSearchModel, attributeName);
	}

	protected Object getModelAttributeValue(final ContentSearchModel contentSearchModel, final String attributeName)
	{
		final ContentSlotForPageModel value = contentSearchModel.getIntegration();
		if (null == value)
		{
			return null;
		}
		final ContentSlotForPageModel slot = value;
		return extractComponentDataFromSlot(slot);
	}

	/**
	 *
	 */
	private Object extractComponentDataFromSlot(final ContentSlotForPageModel contentSlotForPageModel)
	{
		final Map<String, String> integrationMap = new HashMap<String, String>();
		final List<AbstractCMSComponentModel> cmsComp = contentSlotForPageModel.getContentSlot().getCmsComponents();

		cmsComp.forEach(component -> extractComponentData(component, integrationMap));
		final String valueString = integrationMap.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
				.collect(Collectors.joining(" ; "));
		return valueString;
	}

	private void extractComponentData(final AbstractCMSComponentModel component,
			final Map<String, String> integrationMap)
	{
		if (component instanceof SimpleBannerComponentModel)
		{
			setMedia(component, integrationMap);
		}
		else if (component instanceof WsIntegrationContentParagraphComponentModel)
		{
			setCmsContent(component, integrationMap);
		}
		else if (component instanceof CMSLinkComponentModel)
		{
			setBannerUrl(component, integrationMap);
		}
		else if (component instanceof WsIntegrationBadgeParagraphComponentModel)
		{
			setCmsBadgeContent(component, integrationMap);
		}
	}

	/**
	 *
	 */
	private void setBannerUrl(final AbstractCMSComponentModel component, final Map<String, String> integrationMap)
	{
		integrationMap.put(KEY_URL, ((CMSLinkComponentModel) component).getUrl());
	}

	/**
	 *
	 */
	private void setCmsContent(final AbstractCMSComponentModel component, final Map<String, String> integrationMap)
	{
		final Locale local = Locale.ENGLISH;
		integrationMap.put(KEY_CMS_CONTENT, ((WsIntegrationContentParagraphComponentModel) component).getContent(local));
	}

	/**
	 *
	 */
	private void setMedia(final AbstractCMSComponentModel component, final Map<String, String> integrationMap)
	{
		final MediaModel media = ((SimpleBannerComponentModel) component).getMedia();
		if (media != null)
		{
			integrationMap.put(KEY_MEDIA_URL, media.getURL());
		}
	}

	/**
	 *
	 */
	private void setCmsBadgeContent(final AbstractCMSComponentModel component, final Map<String, String> integrationMap)
	{
		final Locale local = Locale.ENGLISH;
		integrationMap.put(KEY_CMS_BADGE_CONTENT, ((WsIntegrationBadgeParagraphComponentModel) component).getContent(local));
	}
}
