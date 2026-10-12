package com.wholescripts.core.page.validators;

import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

public class WholeScriptsSopPaymentDetailsValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return SopPaymentDetailsWsDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        final SopPaymentDetailsWsDTO form = (SopPaymentDetailsWsDTO) object;

        if (StringUtils.isBlank(form.getBillTo_country()))
        {
            rejectIfEmptyOrWhitespace(errors, "billTo_country", "field.invalid", new Object[] { "billTo_country" });
        }
        else
        {
            rejectIfEmptyOrWhitespace(errors, "billTo_firstName", "field.invalid", new Object[] { "billTo_firstName" });
            rejectIfEmptyOrWhitespace(errors, "billTo_lastName", "field.invalid", new Object[] { "billTo_lastName" });
            rejectIfEmptyOrWhitespace(errors, "billTo_street1", "field.invalid", new Object[] { "billTo_street1" });
            rejectIfEmptyOrWhitespace(errors, "billTo_city", "field.invalid", new Object[] { "billTo_city" });
            rejectIfEmptyOrWhitespace(errors, "billTo_postalCode", "field.invalid", new Object[] { "billTo_postalCode" });
            rejectIfEmptyOrWhitespace(errors, "billTo_region", "field.invalid", new Object[] { "billTo_region" });
        }

    }
}
