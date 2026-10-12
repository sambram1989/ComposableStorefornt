package com.wholescripts.core.price.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.pdt.criteria.PriceValueInfoCriteria;

public interface WholeScriptsDefaultPDTCriteriaFactory {

    public PriceValueInfoCriteria msrpBasePriceValueCriteriaFromOrderEntry(final AbstractOrderEntryModel orderEntry)
            throws CalculationException;
}
