/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import com.wholescripts.core.model.MedPaxModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;

/**
 *
 */
public interface WholescriptsCalculationService
{
	void recalculate(AbstractOrderModel order,boolean isResetRequired) throws CalculationException;

	void calculateMedPax(MedPaxModel medPaxModel) throws CalculationException;
}
