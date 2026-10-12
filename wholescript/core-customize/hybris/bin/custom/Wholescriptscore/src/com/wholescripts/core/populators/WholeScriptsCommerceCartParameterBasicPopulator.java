package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang3.StringUtils;

public class WholeScriptsCommerceCartParameterBasicPopulator implements Populator<AddToCartParams, CommerceCartParameter> {
    @Override
    public void populate(AddToCartParams addToCartParams, CommerceCartParameter commerceCartParameter) throws ConversionException {

        if(StringUtils.isNotBlank(addToCartParams.getAutoShipCode()))
        {
            commerceCartParameter.setAutoShipCode(addToCartParams.getAutoShipCode());

        }
    }
}
