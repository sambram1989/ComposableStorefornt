/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.interceptor;

import de.hybris.platform.b2b.interceptor.B2BCreditLimitModelValidateInterceptor;
import de.hybris.platform.b2b.model.B2BCreditLimitModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.math.BigDecimal;

/**
 *
 */
public class WholescriptsB2BCreditLimitModelValidateInterceptor extends B2BCreditLimitModelValidateInterceptor
{
	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof B2BCreditLimitModel)
		{
			final B2BCreditLimitModel creditLimit = (B2BCreditLimitModel) model;

			//Both Date Period and Date Range cannot be present together
			/*
			 * if (creditLimit.getDatePeriod() != null && creditLimit.getDateRange() != null) { throw new
			 * InterceptorException(getL10NService().getLocalizedString("error.dateperiodorrange.bothpresent")); } else if
			 * (creditLimit.getDatePeriod() == null && creditLimit.getDateRange() == null) { throw new
			 * InterceptorException(getL10NService().getLocalizedString("error.dateperiodorrange.missing")); }
			 * 
			 * if (creditLimit.getAmount() != null && creditLimit.getAmount().compareTo(BigDecimal.ZERO) <= 0) { throw new
			 * InterceptorException(getL10NService().getLocalizedString("error.amount.lessoreqaulszero")); } if
			 * (creditLimit.getAlertThreshold() != null && creditLimit.getAlertThreshold().compareTo(BigDecimal.ZERO) <= 0)
			 * { throw new InterceptorException(getL10NService().getLocalizedString("error.threshold.lessoreqaulszero")); }
			 */
		}
	}

}
