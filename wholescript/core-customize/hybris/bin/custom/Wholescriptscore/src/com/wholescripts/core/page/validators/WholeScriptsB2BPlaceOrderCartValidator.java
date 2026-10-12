package com.wholescripts.core.page.validators;

import de.hybris.platform.b2b.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.CartData;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class WholeScriptsB2BPlaceOrderCartValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz)
    {
        return CartData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        final CartData cart = (CartData) target;

        if (cart.getDeliveryAddress() == null)
        {
            errors.reject("cart.deliveryAddressNotSet");
        }

        if (cart.getDeliveryMode() == null)
        {
            errors.reject("cart.deliveryModeNotSet");
        }

        if (CheckoutPaymentType.XCRD.getCode().equals(cart.getPaymentType().getCode()))
        {
            if (cart.getPaymentInfo() == null)
            {
                errors.reject("cart.paymentInfoNotSet");
            }
        }
    }
}
