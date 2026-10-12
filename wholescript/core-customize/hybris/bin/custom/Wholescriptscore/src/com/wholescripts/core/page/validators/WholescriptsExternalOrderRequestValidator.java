package com.wholescripts.core.page.validators;

import static org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.wholescripts.data.ExternalOrderItemsData;
import com.wholescripts.data.ExternalOrderRequestData;


public class WholescriptsExternalOrderRequestValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return ExternalOrderRequestData.class.equals(clazz);
    }

    @Override
    public void validate(final Object object, final Errors errors) {

        final ExternalOrderRequestData request = (ExternalOrderRequestData) object;

        if (StringUtils.isBlank(request.getCustomerSAPId())) {
      	  errors.reject("field.invalid", "Invalid customerSAPId");
        }

        if (StringUtils.isBlank(request.getSalesOrg())) {
      	  errors.reject("field.invalid", "Invalid salesOrg");
        }

        if (StringUtils.isBlank(request.getDistChannel())) {
      	  errors.reject("field.invalid", "Invalid distChannel");
        }

        if (StringUtils.isBlank(request.getDivision())) {
      	  errors.reject("field.invalid", "Invalid division");
        }

        if (StringUtils.isBlank(request.getAddressId())) {
      	  errors.reject("field.invalid", "Invalid addressId");
        }

        if (CollectionUtils.isEmpty(request.getItems())) {
      	  errors.reject("field.invalid", "Invalid items");
        } else {
      	  
      	  boolean invalidLineNumber = request.getItems().stream().anyMatch(item -> (StringUtils.isEmpty(item.getLineNumber()) || Integer.valueOf(item.getLineNumber()) < 0 ));
      	  
      	  if (invalidLineNumber) {
      		  errors.reject("field.invalid", "invalid lineNumber");
      	  }
      	  
      	  final List<String> lineNumbers = request.getItems().stream()
      			  .map(lineNumber -> lineNumber.getLineNumber()).collect(Collectors.toList());
      	 
      	  final Set<String> duplicates = lineNumbers.stream()
                 .collect(Collectors.groupingBy(lineNumber -> lineNumber, Collectors.counting()))
                 .entrySet().stream()
                 .filter(entry -> entry.getValue() > 1)
                 .map(Map.Entry::getKey)
                 .collect(Collectors.toSet());
      	  
      	  if (CollectionUtils.isNotEmpty(duplicates)) {
      		  errors.reject("field.invalid", "duplicate lineNumber");
      	  }
      	  
        }

        if (StringUtils.isEmpty(request.getShippingMethod())) {
            errors.reject("field.invalid", "Invalid shippingMethod");
        }

        if (Objects.isNull(request.getShippingCharge())) {
            errors.reject("field.invalid", "Invalid shippingCharge");
        }

        if (StringUtils.isEmpty(request.getPaymentTerms())) {
            errors.reject("field.invalid", "Invalid shippingCharge");
        }

    }
}
