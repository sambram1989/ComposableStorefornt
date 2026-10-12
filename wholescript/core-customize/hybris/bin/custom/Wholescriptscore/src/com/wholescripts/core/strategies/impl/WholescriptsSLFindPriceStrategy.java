package com.wholescripts.core.strategies.impl;

import com.wholescripts.core.price.impl.WholescriptsPDTCriteriaFactory;
import com.wholescripts.core.strategies.WholescriptsFindPriceStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindPriceHook;
import de.hybris.platform.order.strategies.calculation.pdt.FindPDTValueInfoStrategy;
import de.hybris.platform.order.strategies.calculation.pdt.criteria.PriceValueInfoCriteria;
import de.hybris.platform.util.PriceValue;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

public class WholescriptsSLFindPriceStrategy implements WholescriptsFindPriceStrategy {


    @Resource(name = "findPriceValueInfoStrategy")
    private FindPDTValueInfoStrategy<PriceValue, PriceInformation, PriceValueInfoCriteria> findPriceValueInfoStrategy;

    @Resource(name = "wholeScriptsDefaultPDTCriteriaFactory")
    private WholescriptsPDTCriteriaFactory wholeScriptsDefaultPDTCriteriaFactory;

    private List<FindPriceHook> findPriceHooks = Collections.emptyList();

    @Override
    public PriceValue findMSRPBasePrice(AbstractOrderEntryModel entry) throws CalculationException {
        final PriceValue defaultPrice = findMSRPBasePriceWithStrategy(entry);
        return getFindPriceHooks().stream().filter(h -> h.isApplicable(entry)).findFirst()
                .map(h -> h.findCustomBasePrice(entry, defaultPrice)).orElse(defaultPrice);
    }

    private PriceValue findMSRPBasePriceWithStrategy(final AbstractOrderEntryModel entry) throws CalculationException
    {
        final PriceValueInfoCriteria priceValuesCriteria = wholeScriptsDefaultPDTCriteriaFactory.msrpBasePriceValueCriteriaFromOrderEntry(entry);

        final List<PriceValue> pdtValues = findPriceValueInfoStrategy.getPDTValues(priceValuesCriteria);

        return pdtValues.isEmpty() ? null : pdtValues.get(0);
    }

    protected List<FindPriceHook> getFindPriceHooks()
    {
        return findPriceHooks;
    }

    public void setFindPriceHooks(final List<FindPriceHook> findPriceHooks)
    {
        this.findPriceHooks = findPriceHooks;
    }
}
