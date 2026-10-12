/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import com.wholescripts.dto.ConsentUpdateBrandListWsDTO;
import de.hybris.platform.commerceservices.order.CommerceCartModification;

/**
 *
 */
public interface WholescriptsCartService
{
	CommerceCartModification updateMedpaxCartEntry(String medpaxId, String entryNumber, String qty);
	void deleteMedPaxEntryFromCart(final String medpaxId, final String entryNumber, final String itemTimeCode);

	void updateBrandConsentForCustomer(ConsentUpdateBrandListWsDTO request) throws Exception;
}
