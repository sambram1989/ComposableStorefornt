/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class WholescriptsPrivateLabelSearchResultProductPopulator implements Populator<SearchResultValueData, ProductData>
{
	private ImageFormatMapping imageFormatMapping;
	private PriceDataFactory priceDataFactory;
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target) throws ConversionException
	{
		try{
			target.setCode(String.format("%018d",Integer.valueOf(this.<String> getValue(source, "productCode"))));
			target.setName(this.<String> getValue(source, "name"));
			target.setAccount(this.<String> getValue(source, "account"));
			target.setGenericPLName(this.<String> getValue(source, "genericPLName"));
			target.setProductDescription(this.<String> getValue(source, "productDescription"));
			target.setBlueLabel(this.<String> getValue(source, "blueLabel"));
			target.setBrightStock(this.<String> getValue(source, "brightStock"));
			target.setCaseQty(this.<String> getValue(source, "caseQty"));
			target.setPreLabeledCases(this.<String> getValue(source, "preLabeled-cases"));
			target.setPreLabeledUnit(this.<String> getValue(source, "preLabeled-unit"));
			target.setDeliveryForm(this.<String> getValue(source, "deliveryForm"));
			target.setShortDescription(this.<String> getValue(source, "shortDescription"));
			target.setStockStatus(this.<String> getValue(source, "stockLevelStatus"));
			target.setForceOutOfStock(this.<Boolean> getValue(source, "forceOutOfStock"));
			populateCasePrices(source,target);
			populateWholePrices(source,target);
			populateRetailPrices(source,target);

			final List<ImageData> images = createImageData(source);
			if (CollectionUtils.isNotEmpty(images))
			{
				target.setImages(images);
			}

		} catch (Exception e) {
         //
		}

	}

	protected void populateCasePrices(final SearchResultValueData source, final ProductData target)
	{
		final Double casePrice = this.<Double> getValue(source, "casePrice");
		//final Double priceValue =StringUtils.isNotEmpty(casePrice) ?  Double.valueOf(casePrice) : 0.0d;
		if (casePrice != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(casePrice.doubleValue()),
					getCommonI18NService().getCurrentCurrency());
			target.setCasePrice(priceData);
		}
	}
	protected void populateWholePrices(final SearchResultValueData source, final ProductData target)
	{
		final Double wholesalePrice = this.<Double> getValue(source, "wholesalePrice");
		//final Double priceValue = StringUtils.isNotEmpty(wholesalePrice) ?  Double.valueOf(wholesalePrice) : 0.0d;
		if (wholesalePrice != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(wholesalePrice.doubleValue()),
					getCommonI18NService().getCurrentCurrency());
			target.setWholeSalePrice(priceData);
		}
	}
	
	protected void populateRetailPrices(final SearchResultValueData source, final ProductData target)
	{
		final Double retailPrice = this.<Double> getValue(source, "retailPrice");
		//final Double priceValue = StringUtils.isNotEmpty(retailPrice) ?  Double.valueOf(retailPrice) : 0.0d;
		if (retailPrice != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(retailPrice.doubleValue()),
					getCommonI18NService().getCurrentCurrency());
			target.setRetailPrice(priceData);
		}
	}

	protected List<ImageData> createImageData(final SearchResultValueData source)
	{
		final List<ImageData> result = new ArrayList<ImageData>();

		addImageData(source, "thumbnail", result);
		addImageData(source, "product", result);

		return result;
	}

	protected void addImageData(final SearchResultValueData source, final String imageFormat, final List<ImageData> images)
	{
		final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
		if (mediaFormatQualifier != null && !mediaFormatQualifier.isEmpty())
		{
			addImageData(source, imageFormat, mediaFormatQualifier, ImageDataType.PRIMARY, images);
		}
	}

	protected void addImageData(final SearchResultValueData source, final String imageFormat, final String mediaFormatQualifier,
			final ImageDataType type, final List<ImageData> images)
	{
		final String imgValue = getValue(source, "img-" + mediaFormatQualifier);
		if (imgValue != null && !imgValue.isEmpty())
		{
			final ImageData imageData = createImageData();
			imageData.setImageType(type);
			imageData.setFormat(imageFormat);
			imageData.setUrl(imgValue);

			images.add(imageData);
		}
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}

		// DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
		return (T) source.getValues().get(propertyName);
	}

	protected ImageData createImageData()
	{
		return new ImageData();
	}

	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * @param priceDataFactory the priceDataFactory to set
	 */
	public void setPriceDataFactory(PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @return the imageFormatMapping
	 */
	public ImageFormatMapping getImageFormatMapping()
	{
		return imageFormatMapping;
	}

	/**
	 * @param imageFormatMapping the imageFormatMapping to set
	 */
	public void setImageFormatMapping(ImageFormatMapping imageFormatMapping)
	{
		this.imageFormatMapping = imageFormatMapping;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService the commonI18NService to set
	 */
	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

}
