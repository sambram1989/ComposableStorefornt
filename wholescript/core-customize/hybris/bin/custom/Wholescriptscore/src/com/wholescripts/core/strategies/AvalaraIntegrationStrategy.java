package com.wholescripts.core.strategies;

import de.hybris.platform.b2b.model.B2BCustomerModel;

import java.util.Map;

/**
 *
 */
public interface AvalaraIntegrationStrategy
{
   public void integrate(Map<String, String> configParams, B2BCustomerModel customer);
}
