/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.helper;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.dto.WsRecommendationData;
import com.wholescripts.dto.WsRecommendationEntryData;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;


/**
 *
 */
public  class WholescriptsHelper
{

	public static WsRecommendationEntryData getRecommendationEntryForNumber(final WsRecommendationData recommend,
			final long number)
	{
		final Integer requestedEntryNumber = (int) number;
		return CollectionUtils.emptyIfNull(recommend.getRecommendationEntries()).stream()
				.filter(entry -> entry != null && requestedEntryNumber.equals(entry.getEntryNumber())).findFirst()
				.orElseThrow(() -> new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number)));
	}

	public static AddressModel returnValidAddress(B2BUnitModel source) {

		Collection<AddressModel> addresses = source.getAddresses();
		for (AddressModel addressModel : addresses) {
			if (addressModel.getShippingAddress().equals(Boolean.TRUE)) {
				return addressModel;
			}
		}
		return null;
	}

	public AddressModel returnValidAddressFromAddress(AddressModel addressModel) {
		String publicKey = addressModel.getPublicKey();
		if (addressModel.getShippingAddress().equals(Boolean.TRUE) && StringUtils.isNotBlank(publicKey)) {
			String[] splittedPublicKey = publicKey.split("\\|");
			if (splittedPublicKey.length >= 2 && splittedPublicKey[0].contains(splittedPublicKey[1])) {
				return addressModel;
			}
		}
		return null;
	}

	public static String addLeadingZeros(String productCode) {
		if (productCode == null) {
			return "000000000000000000"; // Handle null input
		}
		int paddingLength = 18 - productCode.length();
		if (paddingLength > 0) {
			return "0".repeat(paddingLength) + productCode;
		} else {
			return productCode; // String is already 18 or more chars
		}
	}

	public static Long getTotalUnit(final Collection<StockLevelModel> stockLevels)
	{
		long totalActualAmount = 0;
		for (final StockLevelModel stockLevel : stockLevels)
		{
			// If any stock level is flagged as FORCEINSTOCK then return null to indicate in stock
			if (InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()))
			{
				final long availableToSellQuantity = getAvailableToSellQuantity(stockLevel);
				if (availableToSellQuantity > 0 || !stockLevel.isTreatNegativeAsZero())
				{
					totalActualAmount += availableToSellQuantity;
				}
			}
		}
		return Long.valueOf(totalActualAmount);
	}


	public static long getAvailableToSellQuantity(final StockLevelModel stockLevel)
	{
		return stockLevel.getAvailable() - stockLevel.getReserved() + (long) stockLevel.getOverSelling();
	}
	
	public static AddressModel returnValidBillingAddress(B2BUnitModel source) {

		Collection<AddressModel> addresses = source.getAddresses();
		for (AddressModel addressModel : addresses) {
			if (addressModel.getBillingAddress().equals(Boolean.TRUE)) {
				return addressModel;
			}
		}
		return null;
	}
}
