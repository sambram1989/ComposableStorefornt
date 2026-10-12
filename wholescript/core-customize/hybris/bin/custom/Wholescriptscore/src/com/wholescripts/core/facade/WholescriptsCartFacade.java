/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import com.wholescripts.dto.ConsentUpdateBrandListWsDTO;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.store.BaseStoreModel;

import com.wholescripts.data.AddtocartRequestData;
import com.wholescripts.data.OrderSimulateResponseData;

/**
 *
 */
public interface WholescriptsCartFacade {
	public CartData addToCart(final String medpaxId, final String protocolId, final String recommendationId,final boolean isWSPage) throws CommerceCartModificationException;
	CartData setCartDeliveryAddressInternal(final String addressId);
   public OrderSimulateResponseData orderSimulate(final CartModel cart, final BaseStoreModel baseStore) throws Exception;

   public CartModificationData addToCart(String code, long quantity,String autoShipCode) throws CommerceCartModificationException;
	public CartModificationData addOrderEntry(final OrderEntryData cartEntry);

	CartModificationData updateCartEntry(long entryNumber, String pickupStore)throws CommerceCartModificationException;

	CartModificationData updateCartEntry(long entryNumber, long quantity, String autoShipCode) throws CommerceCartModificationException;

	void updateCartEntry(String entryNumbers) throws CommerceCartModificationException;
	CartModificationData updateMedpaxCartEntry(final String medpaxId,final String entryNumber,final String qty) throws CommerceCartModificationException;
	void deleteMedPaxEntryFromCart(final String medpaxId, final String entryNumber, final String itemTimeCode);
	CartData addToCart(final AddtocartRequestData requestData)throws Exception;

	void updateBrandConsentForCustomer(ConsentUpdateBrandListWsDTO request) throws Exception;
}

