/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.AbstractOrderService;


public interface WholescriptsAbstractOrderService extends AbstractOrderService<AbstractOrderModel, AbstractOrderEntryModel>
{
	public AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final AbstractOrderModel order,
			final ProductModel product, final long qty, final UnitModel unit, final int number, final boolean addToPresent,final String medpaxId,final int medpaxEntryNumber);
	
	public AbstractOrderEntryModel addNewEntry(final AbstractOrderModel order, final ProductModel product, final long qty, final UnitModel unit, final int number,
         final boolean addToPresent,final String medpaxId, int medpaxEntryNumber);
	
	public int addEntryAtPosition(final AbstractOrderModel order, final AbstractOrderEntryModel entry, final int requested,final String medpaxId,final int medpaxEntryNumber);
	
	AbstractOrderEntryModel addNewEntry(final AbstractOrderModel order, final ProductModel product, final long qty, final UnitModel unit, final int number,
			final boolean addToPresent, final int entryGroupNumber);
	
	AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final AbstractOrderModel order, final ProductModel product, final long qty,
			final UnitModel unit, final int number, final boolean addToPresent, final int entryGroupNumber);

	int addEntryAtPosition(final AbstractOrderModel order, final AbstractOrderEntryModel entry, final int requested, final int entryGroupNumber);
}
