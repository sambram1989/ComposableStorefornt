package com.wholescripts.core.populators;

import de.hybris.platform.b2b.enums.CheckoutPaymentType;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.populators.B2BPaymentTypePopulator;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

public class WholescriptsB2BPaymentTypePopulator extends B2BPaymentTypePopulator {

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;
    @Override
    public void populate(CheckoutPaymentType source, B2BPaymentTypeData target) {
        super.populate(source,target);
        target.setDescription(configurationService.getConfiguration().getString("CheckoutPaymentType."+ source.getCode() + ".description"));

    }
}
