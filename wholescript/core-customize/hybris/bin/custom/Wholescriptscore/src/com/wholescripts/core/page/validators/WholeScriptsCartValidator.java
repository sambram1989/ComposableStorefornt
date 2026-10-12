/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.page.validators;

import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.wholescripts.dto.WholescriptsValidateCartsWsDTO;

public class WholeScriptsCartValidator implements Validator
{
	private static final int MAX_FIELD_LENGTH = 255;
	private static final int MAX_POSTCODE_LENGTH = 10;

	@Override
	public boolean supports(Class<?> clazz)
	{
		return WholescriptsValidateCartsWsDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		WholescriptsValidateCartsWsDTO requestDto = (WholescriptsValidateCartsWsDTO) target;
		validateStandardFields(requestDto.getAddress(), errors);
		validateCountrySpecificFields(requestDto.getAddress(), errors);
	}

	protected void validateStandardFields(final AddressWsDTO address, final Errors errors)
	{
		validateStringField(address.getCountry().getIsocode(), AddressField.COUNTRY, MAX_FIELD_LENGTH, errors);
		//validateStringField(address.getFirstName(), AddressField.FIRSTNAME, MAX_FIELD_LENGTH, errors);
		//validateStringField(address.getLastName(), AddressField.LASTNAME, MAX_FIELD_LENGTH, errors);
		validateStringField(address.getLine1(), AddressField.LINE1, MAX_FIELD_LENGTH, errors);
		validateStringField(address.getTown(), AddressField.TOWN, MAX_FIELD_LENGTH, errors);
		validateStringField(address.getPostalCode(), AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors);
	}

	protected enum AddressField
	{
		TITLE("titleCode", "address.title.invalid"), FIRSTNAME("firstName", "address.firstName.invalid"),
		LASTNAME("lastName", "address.lastName.invalid"), LINE1("line1", "address.line1.invalid"),
		LINE2("line2", "address.line2.invalid"), TOWN("townCity", "address.townCity.invalid"),
		POSTCODE("postcode", "address.postcode.invalid"), REGION("regionIso", "address.regionIso.invalid"),
		COUNTRY("countryIso", "address.country.invalid");

		private String fieldKey;
		private String errorKey;

		private AddressField(final String fieldKey, final String errorKey)
		{
			this.fieldKey = fieldKey;
			this.errorKey = errorKey;
		}

		public String getFieldKey()
		{
			return fieldKey;
		}

		public String getErrorKey()
		{
			return errorKey;
		}
	}

	protected static void validateStringField(final String addressField, final AddressField fieldType,
			final int maxFieldLength, final Errors errors)
	{
		if (StringUtils.isBlank(addressField) || (StringUtils.length(addressField) > maxFieldLength))
		{
			errors.reject("field.invalid", "invalid " + fieldType.getFieldKey());
		}
	}
	
	protected void validateCountrySpecificFields(final AddressWsDTO address, final Errors errors)
	{
		final String isoCode = address.getCountry().getIsocode();
		if (isoCode != null)
		{
			switch (CountryCode.lookup(isoCode))
			{
				case CANADA:
					validateStringFieldLength(address.getTitleCode(), AddressField.TITLE, MAX_FIELD_LENGTH, errors);
					validateFieldNotNull(address.getRegion().getIsocode(), AddressField.REGION, errors);
					break;
				case USA:
					validateStringFieldLength(address.getTitleCode(), AddressField.TITLE, MAX_FIELD_LENGTH, errors);
					validateFieldNotNull(address.getRegion().getIsocode(), AddressField.REGION, errors);
					break;
				default:
					validateStringFieldLength(address.getTitleCode(), AddressField.TITLE, MAX_FIELD_LENGTH, errors);
					break;
			}
		}
	}
	
	protected enum CountryCode
	{
		USA("US"), CANADA("CA"), DEFAULT("");

		private String isoCode;

		private static Map<String, CountryCode> lookupMap = new HashMap<String, CountryCode>();
		static
		{
			for (final CountryCode code : CountryCode.values())
			{
				lookupMap.put(code.getIsoCode(), code);
			}
		}

		private CountryCode(final String isoCodeStr)
		{
			this.isoCode = isoCodeStr;
		}

		public static CountryCode lookup(final String isoCodeStr)
		{
			CountryCode code = lookupMap.get(isoCodeStr);
			if (code == null)
			{
				code = DEFAULT;
			}
			return code;
		}

		public String getIsoCode()
		{
			return isoCode;
		}
	}
	
	protected static void validateStringFieldLength(final String field, final AddressField fieldType, final int maxFieldLength,
			final Errors errors)
	{
		if (StringUtils.isNotEmpty(field) && StringUtils.length(field) > maxFieldLength)
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}
	
	protected static void validateFieldNotNull(final String addressField, final AddressField fieldType,
		   final Errors errors)
{
if (addressField == null)
{
errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
}
}
}
