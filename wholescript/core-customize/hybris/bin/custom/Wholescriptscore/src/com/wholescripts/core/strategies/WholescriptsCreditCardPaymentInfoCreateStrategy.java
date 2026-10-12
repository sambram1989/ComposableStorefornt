package com.wholescripts.core.strategies;

import com.wholescripts.data.WholescriptsADDCCInSapResponseData;
import com.wholescripts.data.WholescriptsAuthResponseData;
import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;

import java.util.Map;

public interface WholescriptsCreditCardPaymentInfoCreateStrategy {

    CreditCardPaymentInfoModel saveSubscription(Map<String, String> parameters, B2BCustomerModel customerModel, SopPaymentDetailsWsDTO sopPaymentDetailst, WholescriptsADDCCInSapResponseData sapPaymentResponse);
}
