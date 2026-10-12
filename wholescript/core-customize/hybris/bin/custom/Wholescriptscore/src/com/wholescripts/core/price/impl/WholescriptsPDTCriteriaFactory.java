package com.wholescripts.core.price.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.pdt.criteria.PriceValueInfoCriteria;
import de.hybris.platform.order.strategies.calculation.pdt.criteria.impl.DefaultPriceValueInfoCriteria;
import de.hybris.platform.order.strategies.calculation.pdt.impl.PDTEnumGroupsHelper;

import javax.annotation.Resource;
import java.util.Date;

public class WholescriptsPDTCriteriaFactory implements WholeScriptsDefaultPDTCriteriaFactory {

    @Resource(name = "pdtEnumGroupsHelper")
    private PDTEnumGroupsHelper pdtEnumGroupsHelper;

    @Resource(name = "enumerationService")
    private EnumerationService enumerationService;


    public PriceValueInfoCriteria msrpBasePriceValueCriteriaFromOrderEntry(final AbstractOrderEntryModel orderEntry)
            throws CalculationException
    {
        final AbstractOrderModel order = orderEntry.getOrder();
        CurrencyModel currency = null;
        ProductPriceGroup productGroup = null;
        UserModel user = null;
        UserPriceGroup userGroup = null;
        UnitModel unit = null;
        long quantity = 0;
        boolean net = false;
        Date date = null;

        final ProductModel product = orderEntry.getProduct();
        final boolean giveAwayMode = orderEntry.getGiveAway().booleanValue();
        final boolean entryRejected = orderEntry.getRejected().booleanValue();

        productGroup = pdtEnumGroupsHelper.getPPG(product);
        user = order.getUser();
        userGroup = enumerationService.getEnumerationValue(UserPriceGroup.class, "02");
        // Fallback if User Price Group "02" is not found
        if (userGroup==null) {
            userGroup = pdtEnumGroupsHelper.getUPG(orderEntry);
        }
        quantity = orderEntry.getQuantity().longValue();
        unit = orderEntry.getUnit();
        currency = order.getCurrency();
        date = order.getDate();
        net = order.getNet().booleanValue();

        return DefaultPriceValueInfoCriteria.buildForValue() //
                .withProduct(product) //
                .withProductPriceGroup(productGroup) //
                .withUser(user) //
                .withUserPriceGroup(userGroup) //
                .withCurrency(currency) //
                .withQuantity(quantity) //
                .withUnit(unit) //
                .withDate(date) //
                .withNet(net) //
                .withGiveAwayMode(giveAwayMode) //
                .withEntryRejected(entryRejected) //
                .build();
    }

}
