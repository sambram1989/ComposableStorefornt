/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;


import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.core.service.WholescriptsMedPaxService;
import com.wholescripts.data.MedPaxPacketsData;
import com.wholescripts.data.MedpaxCartonData;
import com.wholescripts.data.PacketEntryData;
import com.wholescripts.data.ReviewMedPaxData;
import com.wholescripts.dto.DosingData;
import com.wholescripts.dto.ItemTimeData;


/**
 *
 */
public class WholescriptsMedpaxPacketsPopulator implements Populator<MedPaxModel, ReviewMedPaxData>
{
	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;

	@Resource(name = "wScriptDosingConverter")
	private Converter<DosingModel, DosingData> wScriptDosingConverter;

	@Resource(name = "wScriptsItemTimeConverter")
	private Converter<ItemTimeModel, ItemTimeData> wScriptsItemTimeConverter;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "wholescriptsMedPaxService")
	private WholescriptsMedPaxService wholescriptsMedPaxService;

	@Resource(name = "wholescriptsItemTimeDao")
	private WholescriptsItemTimeDao wholescriptsItemTimeDaoImpl;
	@Resource(name = "mediaModelConverter")
   private Converter<MediaModel, MediaData> mediaModelConverter;
	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Override
	public void populate(final MedPaxModel source, final ReviewMedPaxData target) throws ConversionException
	{
		target.setName(source.getName());
		target.setMedPaxCode(source.getCode());
		target.setDescription(source.getDescription());
		// Retrieve and split the packet timings once
		final List<String> packetTimings = Arrays
				.asList(configurationService.getConfiguration().getString("packet.itemtimes").split(","));
		// Populate the packet list using streams
		final List<MedPaxPacketsData> medPaxPacket = packetTimings.stream()
				.map(timing -> {
					final String configValue = configurationService.getConfiguration().getString(timing);
					// Check if configValue is not null or empty before calling populatePacketsEntry
					return (configValue != null && !configValue.isEmpty()) ? populatePacketsEntry(source, configValue) : null;
				}).filter(Objects::nonNull) // Remove any null results from the stream
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(medPaxPacket))
		{
			target.setPackets(medPaxPacket);
		}
	   target.setCarton(getCartonData(source));
	   final PriceData msrpPriceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getMsrpTotalPrice()), "USD");
	   target.setMsrpTotalPrice(msrpPriceData);
	   final PriceData totalPriceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getTotalPrice()), "USD");
	   target.setTotalPrice(totalPriceData);
		calculateMedPaxFormula(source, target, medPaxPacket);
	}

	private MedpaxCartonData getCartonData(final MedPaxModel source)
	{
		MedpaxCartonData cartonData = new MedpaxCartonData();
		cartonData.setCartonName(source.getCartonDescription());

		if(Objects.nonNull(source.getCarton())) {
			cartonData.setProductCode(source.getCarton().getCode());
			if(Objects.nonNull(source.getCarton().getPicture()))
			{
				cartonData.setImage(mediaModelConverter.convert(source.getCarton().getPicture()));

			}
		}
		if(configurationService.getConfiguration().getString("medpax.product.carton").equals(source.getCarton().getCode())) {
			cartonData.setDefaultCarton(Boolean.TRUE);
		}
		return cartonData;
	}

	public MedPaxPacketsData populatePacketsEntry(final MedPaxModel source, final String timingCode)
			throws ConversionException
	{
		final List<PacketEntryData> packets = new ArrayList<PacketEntryData>();
		final MedPaxPacketsData medPaxPacketsData = new MedPaxPacketsData();
		final List<String> timeCodes = Arrays.asList(timingCode.split(","));
		final List<MedpaxEntryModel> entries = filterByTimeCode(source, timeCodes);
		String itemCode =StringUtils.EMPTY;
		for (final MedpaxEntryModel medpaxEntryModel : entries)
		{
			final PacketEntryData packetEntryData = new PacketEntryData();
			packetEntryData.setEntryNumbers(medpaxEntryModel.getEntryNumber());
			packetEntryData.setPrice(medpaxEntryModel.getPrice());
			packetEntryData.setProduct(productConverter.convert(medpaxEntryModel.getProduct()));

			// Extract dosing information and set the quantity once
			medpaxEntryModel.getDosing().stream().findFirst() // Only need to set the quantity from the first dosing
					.ifPresent(dosing -> packetEntryData.setQuantity(dosing.getQuantity().longValue()));

			// Process item times and set packet code and item time details
			packets.add(packetEntryData);
			if(StringUtils.isEmpty(itemCode)) {
				for(DosingModel dosing :medpaxEntryModel.getDosing()) {
					itemCode =dosing.getItemTimes().stream().filter(i -> timeCodes.contains(i.getCode())).map(i -> i.getCode()).findFirst().orElse(StringUtils.EMPTY);
					break;
				}
			}

		}
		final String packetCode = timeCodes.get(0);
		medPaxPacketsData.setPacketCode(packetCode);
		if(StringUtils.isNotEmpty(itemCode)) {
			medPaxPacketsData.setItemTime(wScriptsItemTimeConverter.convert(wholescriptsItemTimeDaoImpl.getItemTimeByCode(itemCode)));
		}

		if (CollectionUtils.isNotEmpty(packets))
		{
			medPaxPacketsData.setPacketEntries(packets);
			return medPaxPacketsData;
		}
		return null;
	}


	private void calculateMedPaxFormula(final MedPaxModel medPaxModel, final ReviewMedPaxData target,
			final List<MedPaxPacketsData> medPaxPacket)
	{
		final Set<String> productCodes = new HashSet<String>();
		final List<MedpaxEntryModel> entris = medPaxModel.getMedpaxEntry();
		for (final MedpaxEntryModel medpaxEntryModel : entris)
		{
			final ProductModel productModel = medpaxEntryModel.getProduct();
			if (null != productModel)
			{
				productCodes.add(productModel.getCode());
			}
		}

		target.setFormulas(productCodes.size());
		
		int abstractOrderQty=1;
		if(Objects.nonNull(medPaxModel.getAbstractOrderQty()) && medPaxModel.getAbstractOrderQty() > 0L) {
			abstractOrderQty =  medPaxModel.getAbstractOrderQty().intValue();
		}
		final Integer totalPackets = medPaxPacket.size() * 30 * abstractOrderQty;
		final Integer boxCapacity = 90; // Number of packets each box can hold

		// Calculate the number of full boxes required
		Integer fullBoxes = totalPackets / boxCapacity;

		// Check if an additional box is needed for the remainder
		final int remainder = totalPackets % boxCapacity;
		if (remainder > 0)
		{
			fullBoxes += 1; // Add one more box for the remaining packets
		}

		target.setPacketCount(totalPackets);
		target.setBox(fullBoxes);

		wholescriptsMedPaxService.updateMedpaxHeader(medPaxModel, fullBoxes, totalPackets, productCodes.size());
	}


	public List<MedpaxEntryModel> filterByTimeCode(final MedPaxModel medpax, final List<String> timeCode)
	{
		// Filter the medpax entries based on matching time codes within item times
		return medpax.getMedpaxEntry().stream()
				.filter(entry -> entry.getDosing().stream()
						.anyMatch(dosing -> dosing.getItemTimes().stream().anyMatch(itemTime -> timeCode.contains(itemTime.getCode()))))
				.collect(Collectors.toList());
	}
}
