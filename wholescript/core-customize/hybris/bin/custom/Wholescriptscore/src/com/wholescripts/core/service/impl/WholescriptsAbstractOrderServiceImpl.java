/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.AbstractOrderEntryService;
import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.order.impl.DefaultAbstractOrderService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.service.WholescriptsAbstractOrderService;


/**
 *
 */
public class WholescriptsAbstractOrderServiceImpl extends DefaultAbstractOrderService<AbstractOrderModel, AbstractOrderEntryModel>
		implements WholescriptsAbstractOrderService
{
	private static final Logger LOG = Logger.getLogger(WholescriptsAbstractOrderServiceImpl.class);
	private static final int APPEND_AS_LAST = -1;
	@Resource(name = "abstractOrderEntryService")
	private AbstractOrderEntryService<AbstractOrderEntryModel> abstractOrderEntryService;
	@Resource(name = "abstractOrderEntryTypeService")
	private AbstractOrderEntryTypeService abstractOrderEntryTypeService;
	@Resource(name = "entryGroupService")
	private EntryGroupService entryGroupService;

	@Override
	public AbstractOrderEntryModel addNewEntry(final AbstractOrderModel order, final ProductModel product, final long qty,
			final UnitModel unit, final int number, final boolean addToPresent, final String medpaxId, final int medpaxEntryNumber)
	{
		validateParameterNotNullStandardMessage("order", order);
		return addNewEntry(abstractOrderEntryTypeService.getAbstractOrderEntryType(order), order, product, qty, unit, number,
				addToPresent, medpaxId,medpaxEntryNumber);
	}
	
	@Override
	public AbstractOrderEntryModel addNewEntry(final AbstractOrderModel order, final ProductModel product, final long qty,
			final UnitModel unit, final int number, final boolean addToPresent, final int entryGroupNumber)
	{
		validateParameterNotNullStandardMessage("order", order);
		return addNewEntry(abstractOrderEntryTypeService.getAbstractOrderEntryType(order), order, product, qty, unit, number,
				addToPresent, entryGroupNumber);
	}

	@Override
	public AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final AbstractOrderModel order,
			final ProductModel product, final long qty, final UnitModel unit, final int number, final boolean addToPresent,
			final String medpaxId,final int medpaxEntryNumber)
	{
		validateParameterNotNullStandardMessage("entryType", entryType);
		validateParameterNotNullStandardMessage("product", product);
		validateParameterNotNullStandardMessage("order", order);
		if (qty <= 0)
		{
			throw new IllegalArgumentException("Quantity must be a positive non-zero value");
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
			for (final AbstractOrderEntryModel e : getEntriesForProduct(order, product))
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
			addEntryAtPosition(order, ret, number,medpaxId,medpaxEntryNumber);
		}
		order.setCalculated(Boolean.FALSE);
		return ret;
	}

	@Override
	public int addEntryAtPosition(final AbstractOrderModel order, final AbstractOrderEntryModel entry, final int requested,final String medpaxId, final int medpaxEntryNumber)
	{
		Optional<EntryGroup> entryGroup = Optional.empty();
		final List<AbstractOrderEntryModel> all = order.getEntries();
		int entryNumber=100;
		int lastIndexEntryNumberValue =0;
		if(medpaxEntryNumber == 0) {
			if(StringUtils.isEmpty(medpaxId) && !all.isEmpty()) {
				entryNumber+= all.stream().filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers()))
						.map(AbstractOrderEntryModel::getEntryNumber)
						.max(Integer::compareTo)
						.orElse(0);
			}else if(StringUtils.isNotEmpty(medpaxId) && !all.isEmpty()) {
				 entryGroup = order.getEntryGroups().stream().filter(e -> StringUtils.isNotEmpty(e.getExternalReferenceId()) && e.getExternalReferenceId().equals(medpaxId)).findFirst();
				if(entryGroup.isPresent()) {
					final int lastIndex = all.size() - 1;
					lastIndexEntryNumberValue = all.get(lastIndex).getEntryNumber().intValue();     
				}else {
					lastIndexEntryNumberValue =0;
					entryNumber+= all.stream().filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers()))
							.map(AbstractOrderEntryModel::getEntryNumber)
							.max(Integer::compareTo)
							.orElse(0);
				}
				
			}
		}
	
		int ret = requested;

		if (requested < 0)
		{
			if(medpaxEntryNumber > 0) {
				ret = medpaxEntryNumber;
			}else {
				ret = lastIndexEntryNumberValue == 0 ? entryNumber : lastIndexEntryNumberValue + 2;
			}
		}
		else
		{
			boolean foundEntryWithNumber = false;
			for (int i = 0, s = all.size(); i < s; i++)
			{
				final AbstractOrderEntryModel currentEntry = all.get(i);
				final int enr = currentEntry.getEntryNumber().intValue();
				// other entry got this number -> we need to shift numbers now
				if (foundEntryWithNumber)
				{
					currentEntry.setEntryNumber(Integer.valueOf(enr + 1));
				}
				// found it now?
				else if (enr == requested)
				{
					foundEntryWithNumber = true;
					currentEntry.setEntryNumber(Integer.valueOf(currentEntry.getEntryNumber().intValue() + 1));
				}
				// no other entry got this number -> dont need to shift other entry numbers
				else if (enr > requested)
				{
					break;
				}
			}
		}
		entry.setEntryNumber(Integer.valueOf(ret));

		final List<AbstractOrderEntryModel> newEntries = new ArrayList<AbstractOrderEntryModel>(all);
		newEntries.add(entry);
		Collections.sort(newEntries, new Comparator<AbstractOrderEntryModel>()
		{
			@Override
			public int compare(final AbstractOrderEntryModel order1, final AbstractOrderEntryModel order2)
			{
				return order1.getEntryNumber().compareTo(order2.getEntryNumber());
			}
		});

		order.setEntries(newEntries);
		return ret;
	}
	
	@Override
	public AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final AbstractOrderModel order,
			final ProductModel product, final long qty, final UnitModel unit, final int number, final boolean addToPresent,
			final int entryGroupNumber) {
		validateParameterNotNullStandardMessage("entryType", entryType);
		validateParameterNotNullStandardMessage("product", product);
		validateParameterNotNullStandardMessage("order", order);
		if (qty <= 0)
		{
			throw new IllegalArgumentException("Quantity must be a positive non-zero value");
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
			for (final AbstractOrderEntryModel e : getEntriesForProduct(order, product))
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
			addEntryAtPosition(order, ret, number, entryGroupNumber);
		}
		order.setCalculated(Boolean.FALSE);
		return ret;
	}
	
	@Override
	public int addEntryAtPosition(final AbstractOrderModel order, final AbstractOrderEntryModel entry, final int requested, final int entryGroupNumber) {
		
		final List<AbstractOrderEntryModel> all = order.getEntries();
		entry.setEntryNumber(Integer.valueOf(requested));
		if(entryGroupNumber > 0) {
			entry.setEntryGroupNumbers(Collections.singleton(Integer.valueOf(entryGroupNumber)));
		}

		final List<AbstractOrderEntryModel> newEntries = new ArrayList<AbstractOrderEntryModel>(all);
		newEntries.add(entry);
		Collections.sort(newEntries, new Comparator<AbstractOrderEntryModel>()
		{
			@Override
			public int compare(final AbstractOrderEntryModel order1, final AbstractOrderEntryModel order2)
			{
				return order1.getEntryNumber().compareTo(order2.getEntryNumber());
			}
		});

		order.setEntries(newEntries);
		return requested;
	}

	@Override
	public AbstractOrderModel clone(final ComposedTypeModel orderType, final ComposedTypeModel entryType,
			final AbstractOrderModel original, final String code)
	{
		return getCloneAbstractOrderStrategy().clone(orderType, entryType, original, code, AbstractOrderModel.class,
				AbstractOrderEntryModel.class);
	}


}
