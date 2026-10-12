/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import com.wholescripts.data.*;
import com.wholescripts.dto.UpdateMedPaxPacketsInfoWsDTO;
import de.hybris.platform.cmsfacades.data.MediaData;
import com.wholescripts.core.service.WholescriptsBuyAgainService;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.wholescripts.core.facade.WholescriptsMedPaxFacade;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.core.service.WholescriptsMedPaxService;
import com.wholescripts.dto.MedPaxWsDTO;
import com.wholescripts.dto.MedpaxEntryWsDTO;
import com.wholescripts.dto.UpdateMedPaxPacketsWsDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

/**
 *
 */
public class WholescriptsMedPaxFacadeImpl implements WholescriptsMedPaxFacade
{
	@Resource(name = "wholescriptsMedPaxService")
	WholescriptsMedPaxService wholescriptsMedPaxService;

	@Resource(name = "wholescriptsMedpaxReverseConverter")
	private Converter<MedPaxWsDTO, MedPaxModel> wholescriptsMedpaxReverseConverter;

	@Resource(name = "wholescriptsMedpaxEntryReverseConverter")
	private Converter<MedpaxEntryWsDTO, MedpaxEntryModel> wholescriptsMedpaxEntryReverseConverter;

	@Resource(name = "wholescriptsMedpaxConverter")
	private Converter<MedPaxModel, MedPaxData> wholescriptsMedpaxConverter;

	@Resource(name = "wholescriptsMedpaxPacketsConverter")
	private Converter<MedPaxModel, ReviewMedPaxData> wholescriptsMedpaxPacketsConverter;

	@Resource(name = "wholescriptsMedpaxEntryConverter")
	private Converter<MedpaxEntryModel, MedpaxEntryData> wholescriptsMedpaxEntryConverter;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "mediaModelConverter")
   private Converter<MediaModel, MediaData> mediaModelConverter;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "wholescriptsBuyAgainService")
	private WholescriptsBuyAgainService wholescriptsBuyAgainService;

	@Resource(name = "productConfiguredPopulator")
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;

	@Resource(name="stockConverter")
	private Converter<ProductModel, StockData> stockConverter;

	@Override
	public MedPaxData createMedPax(final MedPaxWsDTO medPaxWsDTO)
	{
		return wholescriptsMedpaxConverter
				.convert(wholescriptsMedPaxService.createMedPax(wholescriptsMedpaxReverseConverter.convert(medPaxWsDTO)));
	}

	@Override
	public MedPaxData updateMedPaxName(final MedPaxWsDTO medPaxWsDTO)
	{
		return wholescriptsMedpaxConverter
				.convert(wholescriptsMedPaxService.updateMedPaxName(medPaxWsDTO));
	}

	@Override
	public MedPaxData updateMedPaxNotes(final MedPaxWsDTO medPaxWsDTO)
	{
		return wholescriptsMedpaxConverter.convert(wholescriptsMedPaxService.updateMedPaxNotes(medPaxWsDTO));
	}

	@Override
	public MedPaxData getMedPaxForCurrentUser()
	{
	final MedPaxModel medPaxModel =	wholescriptsMedPaxService.getMedPaxForCurrentUser();
		if(null != medPaxModel) {
			return wholescriptsMedpaxConverter.convert(medPaxModel);
		}
		return new MedPaxData();
	}

	@Override
	public ReviewMedPaxData getPacketsByMexpaxCode(final String code)
	{
		final MedPaxModel medPaxModel = wholescriptsMedPaxService.getMedPaxByCode(code);
		if (null != medPaxModel)
		{
			return wholescriptsMedpaxPacketsConverter.convert(medPaxModel);
		}
		return new ReviewMedPaxData();
	}

	@Override
	public MedPaxData addEntryToMedpax(final MedpaxEntryWsDTO MedpaxEntryWsDTO, final String code)
	{
		return wholescriptsMedpaxConverter.convert(
				wholescriptsMedPaxService.addEntryToMedpax(wholescriptsMedpaxEntryReverseConverter.convert(MedpaxEntryWsDTO), code));
	}

	@Override
	public MedpaxEntryData updateMedpax(final MedpaxEntryWsDTO medpaxEntryWsDTO, final String entryNumber)
	{
		return wholescriptsMedpaxEntryConverter.convert(
				wholescriptsMedPaxService.updateMedpax(medpaxEntryWsDTO, entryNumber));
	}

	@Override
	public ReviewMedPaxData updateMedpaxPackets(final UpdateMedPaxPacketsWsDTO updateMedPaxPacketsWsDTO)
	{
		return wholescriptsMedpaxPacketsConverter
				.convert(wholescriptsMedPaxService.updateMedpaxPackets(updateMedPaxPacketsWsDTO));
	}

	@Override
	public ReviewMedPaxData updateMedpaxPacketsInfo(final UpdateMedPaxPacketsInfoWsDTO updateMedPaxPacketsInfoWsDTO)
	{
		return wholescriptsMedpaxPacketsConverter
				.convert(wholescriptsMedPaxService.updateMedpaxPacketsInfo(updateMedPaxPacketsInfoWsDTO));
	}

	@Override
	public MedPaxData getMedPaxForOwner(final String protocolId, final String recommendationId, boolean isWSProtocol) {
		final MedPaxModel medPaxModel = wholescriptsMedPaxService.getMedPaxForOwner(protocolId, recommendationId,isWSProtocol);
		if (null != medPaxModel) {
			return wholescriptsMedpaxConverter.convert(medPaxModel);
		}
		return null;
	}
	
	@Override
	public MedPaxData getMedPaxByCode(final String medpaxId) {
		final MedPaxModel medPaxModel = wholescriptsMedPaxService.getMedPaxByCode(medpaxId);
		if (null != medPaxModel) {
			return wholescriptsMedpaxConverter.convert(medPaxModel);
		}
		return new MedPaxData();
	}

	@Override
	public void removeEntryFromMedpax(final String medPaxEntryNumber, final String code){
		wholescriptsMedPaxService.removeEntryFromMedpax(medPaxEntryNumber,code);
    }

	@Override
	public void removeMedpax(String code)
	{
		final MedPaxModel medPaxModel = wholescriptsMedPaxService.getMedPaxByCode(code);
		if(Objects.nonNull(medPaxModel)) {
			modelService.remove(medPaxModel);
		}
	}

	@Override
	public List<MedpaxCartonData> getCartonsData()
	{
		List<MedpaxCartonData> cartonDataList = new ArrayList<>();
		Map<ProductModel,String> cmir= wholescriptsMedPaxService.getCartonsFromCMIR();
		for (Map.Entry<ProductModel, String> map : cmir.entrySet()) {
			MedpaxCartonData cartonData = new MedpaxCartonData();
			ProductModel product = map.getKey();
			cartonData.setCartonName(map.getValue());
			cartonData.setProductCode(product.getCode());
			cartonData.setStock(stockConverter.convert(product));
			if(Objects.nonNull(product.getPicture())) {
			 cartonData.setImage(mediaModelConverter.convert(product.getPicture()));
			}
			if(configurationService.getConfiguration().getString("medpax.product.carton").equals(product.getCode())) {
				cartonData.setDefaultCarton(Boolean.TRUE);
			}
			cartonDataList.add(cartonData);
		}
		return cartonDataList;
	}

	@Override
	public void updateMedpaxForCarton(String productCode, String medpaxCode, String cartonName)
	{
		wholescriptsMedPaxService.updateMedpaxForCarton(productCode, medpaxCode, cartonName);
	}

	@Override
	public List<MedpaxWebKitData> getPrivateLabelWebKits()
	{
		return wholescriptsMedPaxService.getWebKitsFromCMIR();
	}

	@Override
	public OrderedMedpaxProductsData getOrderedMedpaxProductsForUser(final String uId,final String productSearch) {
		List<ProductModel> medPaxProductsList = wholescriptsBuyAgainService.getOrderedMedpaxProductsForUser(uId,productSearch);
		OrderedMedpaxProductsData orderedMedpaxProductsData = new OrderedMedpaxProductsData();

		if (CollectionUtils.isNotEmpty(medPaxProductsList)) {
			List<ProductData> productDataList = medPaxProductsList.stream().map(medPaxProduct -> {
				ProductData productData = getProductConverter().convert(medPaxProduct);
				getProductConfiguredPopulator().populate(medPaxProduct, productData, Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY, ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE, ProductOption.VARIANT_MATRIX_ALL_OPTIONS, ProductOption.VARIANT_MATRIX_BASE));
				return productData;
			}).collect(Collectors.toList());

			orderedMedpaxProductsData.setProducts(productDataList);
			orderedMedpaxProductsData.setFacets(buildFacets(productDataList));
		}
		return orderedMedpaxProductsData;
	}

	protected ProductFacetData buildFacets(List<ProductData> productDataList)
	{
		final ProductFacetData facetData = new ProductFacetData();
		buildCategoriesFacet(productDataList,facetData);
		buildBrandFacet(productDataList,facetData);
		buildDeliveryFormFacet(productDataList,facetData);
		return facetData;
	}

	protected void buildCategoriesFacet(List<ProductData> productDataList, ProductFacetData facetData)
	{
		final Set<ProductFacetValueData> categoryFacetValues = new HashSet<>();
		final Set<CategoryData> totalCategories = new HashSet<>();
		for (final ProductData productData : productDataList) {
			final Collection<CategoryData> categories = productData.getCategories();
			if (CollectionUtils.isNotEmpty(categories)) {
				for (final CategoryData category : categories) {
					totalCategories.add(category);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(totalCategories)) {
			for (final CategoryData category : totalCategories) {
				final ProductFacetValueData categoryFacetValue = new ProductFacetValueData();
				categoryFacetValue.setCode(category.getCode());
				categoryFacetValue.setName(category.getName());

				categoryFacetValues.add(categoryFacetValue);
			}
		}

		if (CollectionUtils.isNotEmpty(categoryFacetValues)) {
			facetData.setCategories(categoryFacetValues.stream().collect(Collectors.toList()));
		}
	}

	protected void buildBrandFacet(List<ProductData> productDataList, ProductFacetData facetData)
	{
		final Set<ProductFacetValueData> brandFacetValues = new HashSet<>();
		final Map<String, String> brandValues = new HashMap<String, String>();

		for (final ProductData product : productDataList)
		{
			brandValues.put(product.getBrandCode(), product.getBrandName());
		}

		if (MapUtils.isNotEmpty(brandValues))
		{
			for (final var entry : brandValues.entrySet())
			{
				final ProductFacetValueData brandFacetValue = new ProductFacetValueData();
				brandFacetValue.setCode(entry.getKey());
				brandFacetValue.setName(entry.getValue());
				brandFacetValues.add(brandFacetValue);
			}
		}

		if (CollectionUtils.isNotEmpty(brandFacetValues)) {
			facetData.setBrand(brandFacetValues.stream().collect(Collectors.toList()));
		}
	}

	private void buildDeliveryFormFacet(List<ProductData> productDataList, ProductFacetData facetData)
	{
		final Set<ProductFacetValueData> deliveryFormFacetValues = new HashSet<>();
		final Map<String, String> deliveryFormValues = new HashMap<String, String>();

		for (final ProductData product : productDataList)
		{
			deliveryFormValues.put(product.getDeliveryForm(), product.getDeliveryForm());
		}

		if (MapUtils.isNotEmpty(deliveryFormValues)) {
			for (final var entry : deliveryFormValues.entrySet()) {
				final ProductFacetValueData deliveryFormFacetValue = new ProductFacetValueData();
				deliveryFormFacetValue.setCode(entry.getKey());
				deliveryFormFacetValue.setName(entry.getValue());
				deliveryFormFacetValues.add(deliveryFormFacetValue);
			}
		}

		if (CollectionUtils.isNotEmpty(deliveryFormFacetValues)) {
			facetData.setDeliveryForm(deliveryFormFacetValues.stream().filter(d -> Objects.nonNull(d.getCode())).collect(Collectors.toList()));
		}
	}

	protected ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
	{
		return productConfiguredPopulator;
	}

	public void setProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConfiguredPopulator = productConfiguredPopulator;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

}
