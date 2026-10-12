/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.hook;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.hook.PrePersistHook;
import de.hybris.platform.integrationservices.item.DefaultIntegrationItem;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.model.ReferralCodesModel;

/**
 *
 */
public class WholescriptsB2BUnitPrePersistHook implements PrePersistHook {

   private static final Logger LOG = LoggerFactory.getLogger(WholescriptsB2BUnitPrePersistHook.class);
   
   @Resource(name = "b2bUnitService")
   private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
   @Resource(name = "modelService")
   private ModelService modelService;

	@Override
	public Optional<ItemModel> execute(ItemModel item, PersistenceContext context)
	{
		if(item instanceof B2BUnitModel) {
			B2BUnitModel b2bUnit = (B2BUnitModel) item;
			if(Objects.nonNull(b2bUnit.getSapCustomerGroup()) && "02".equals(b2bUnit.getSapCustomerGroup().getCode())) {
				B2BUnitModel existingUnit = b2bUnitService.getUnitForUid(b2bUnit.getUid());
				if(Objects.nonNull(existingUnit)) {
					existingUnit.setReferralCodes(Collections.emptySet());
					if(CollectionUtils.isEmpty(existingUnit.getReferralCodes())) {
						List<DefaultIntegrationItem> itemList= (List<DefaultIntegrationItem>) context.getIntegrationItem().getAttribute("referralCodes");
						if(CollectionUtils.isNotEmpty(itemList)) {
							ReferralCodesModel newReferral = modelService.create(ReferralCodesModel.class);
							newReferral.setReferralCode(itemList.get(0).getAttribute("referralCode").toString());
							newReferral.setLastName(itemList.get(0).getAttribute("lastName").toString());
							newReferral.setReferralCodeType(itemList.get(0).getAttribute("referralCodeType").toString());
							newReferral.setValidFrom(itemList.get(0).getAttribute("validFrom").toString());
							newReferral.setValidTo(itemList.get(0).getAttribute("validTo").toString());
							if(Objects.nonNull(itemList.get(0).getAttribute("discount"))) {
								newReferral.setDiscount(Integer.valueOf(itemList.get(0).getAttribute("discount").toString()));
							}
							b2bUnit.setReferralCodes(Set.of(newReferral));
						}
					}
				}
			}
			return Optional.of(b2bUnit);
		}
		return Optional.empty();
	}

}
