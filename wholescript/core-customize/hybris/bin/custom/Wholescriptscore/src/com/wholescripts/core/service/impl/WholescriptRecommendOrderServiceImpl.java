/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.AbstractOrderEntryService;
import de.hybris.platform.order.impl.DefaultAbstractOrderService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.wholescripts.core.model.RecommendationModel;


/**
 *
 */
public class WholescriptRecommendOrderServiceImpl
		extends DefaultAbstractOrderService<RecommendationModel, RecommendationEntryModel>
{
	private static final Logger LOG = Logger.getLogger(WholescriptRecommendOrderServiceImpl.class);
	private static final int APPEND_AS_LAST = -1;

	@Resource(name = "abstractOrderEntryService")
	private AbstractOrderEntryService<RecommendationEntryModel> abstractOrderEntryService;

	@Override
	public AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final RecommendationModel order,
			final ProductModel product, final long qty, final UnitModel unit, final int number, final boolean addToPresent)
	{
		validateParameterNotNullStandardMessage("entryType", entryType);
		validateParameterNotNullStandardMessage("product", product);
		validateParameterNotNullStandardMessage("order", order);
		if (qty < 0)
		{
			throw new IllegalArgumentException("Quantity must be a positive value");
		}
		if (number < APPEND_AS_LAST)
		{
			throw new IllegalArgumentException("Number must be greater or equal -1");
		}
		UnitModel usedUnit = unit;
		if (usedUnit == null)
		{
			LOG.debug("No unit passed, trying to get product unit");
			usedUnit = product.getUnit();
			validateParameterNotNullStandardMessage("usedUnit", usedUnit);
		}

		AbstractOrderEntryModel ret = null;
		// search for present entries for this product if needed
		if (addToPresent)
		{
			for (final RecommendationEntryModel e : getEntriesForProduct(order, product))
			{
				// Ensure that order entry is not a 'give away', and has same units
				if (Boolean.FALSE.equals(e.getGiveAway()) && usedUnit.equals(e.getUnit()))
				{
					e.setQuantity(Long.valueOf(e.getQuantity().longValue() + qty));
					ret = e;
					break;
				}
			}
		}

		if (ret == null)
		{
			ret = abstractOrderEntryService.createEntry(entryType, order);
			ret.setQuantity(Long.valueOf(qty));
			ret.setProduct(product);
			ret.setUnit(usedUnit);
			addEntryAtPosition(order, ret, number);
		}
		order.setCalculated(Boolean.FALSE);
		return ret;
	}

	@Override
	public RecommendationModel clone(final ComposedTypeModel orderType, final ComposedTypeModel entryType,
			final AbstractOrderModel original, final String code)
	{
		// XXX Auto-generated method stub
		return (RecommendationModel) getCloneAbstractOrderStrategy().clone(orderType, entryType, original, code,
				RecommendationModel.class, RecommendationEntryModel.class);
	}




}
