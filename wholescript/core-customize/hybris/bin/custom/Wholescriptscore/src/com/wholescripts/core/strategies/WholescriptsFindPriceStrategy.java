package com.wholescripts.core.strategies;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindPriceStrategy;
import de.hybris.platform.util.PriceValue;

public interface WholescriptsFindPriceStrategy {

    public PriceValue findMSRPBasePrice(AbstractOrderEntryModel entry) throws CalculationException;

}

