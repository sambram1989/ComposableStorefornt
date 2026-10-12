/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.exception;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;

/**
 *
 */
public class POBoxAgeRestrictionException extends Exception
{
	private List<OrderEntryData> entries;
	
	public POBoxAgeRestrictionException (List<OrderEntryData> entries) {
		this.setEntries(entries);
	}

	public POBoxAgeRestrictionException(String message) {
		super(message);
	}
	/**
	 * @return the entries
	 */
	public List<OrderEntryData> getEntries()
	{
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<OrderEntryData> entries)
	{
		this.entries = entries;
	}
}
