/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import com.wholescripts.core.model.ItemTimeModel;

import java.util.List;

/**
 *
 */
public interface WholescriptsItemTimeDao
{

	public ItemTimeModel getItemTimeByCode(final String code);

	public ItemTimeModel getItemTimeByName(final String name);
}
