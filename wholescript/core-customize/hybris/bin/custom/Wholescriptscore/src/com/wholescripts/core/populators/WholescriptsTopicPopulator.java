/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import com.wholescripts.core.model.WholescriptsTopicModel;
import com.wholescripts.dto.WholescriptsTopicData;


/**
 *
 */
public class WholescriptsTopicPopulator implements Populator<WholescriptsTopicModel, WholescriptsTopicData>
{

	@Resource(name = "mediaModelConverter")
	private Converter<MediaModel, MediaData> mediaModelConverter;

	@Override
	public void populate(final WholescriptsTopicModel source, final WholescriptsTopicData target) throws ConversionException
	{
		// XXX Auto-generated method stub
		if (source.getCode() != null)
		{
		target.setCode(source.getCode());
		}
		if (source.getPage() != null)
		{
		target.setPage(source.getPage());
		}
		if (source.getChildTopic() != null)
		{
		target.setChildTopic(source.getChildTopic());
		}
		if (source.getHeadline() != null)
		{
		target.setHeadline(source.getHeadline());
		}
		if (source.getAuthorization() != null)
		{
		target.setAuthorization(source.getAuthorization());
		}
		if (source.getAuthorizationURL() != null)
		{
		target.setAuthorizationURL(source.getAuthorizationURL());
		}
		if (source.getRequestBody() != null)
		{
		target.setRequestBody(source.getRequestBody());
		}
		if (source.getCurlURL() != null)
		{
		target.setCurlURL(source.getCurlURL());
		}
		if (source.getRequestValues() != null)
		{
		target.setRequestValues(source.getRequestValues());
		}
		if (source.getResponse() != null)
		{

		target.setResponse(source.getResponse());
		}
		if (source.getResponseValues() != null)
		{
		target.setResponseValues(source.getResponseValues());
		}
		if (source.getMainContent() != null)
		{
		target.setMainContent(source.getMainContent());
		}
		if (source.getImages() != null)
		{
		target.setImages(mediaModelConverter.convertAll(source.getImages()));
		}
		if (source.getSpecs() != null)
		{
		target.setSpecs(source.getSpecs());
		}
		if (source.getRequestURL() != null)
		{
		target.setRequestURL(source.getRequestURL());
		}

	}



}
