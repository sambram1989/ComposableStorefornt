package com.wholescripts.core.service;

import com.wholescripts.data.WholescriptsPaymetricTokenizationData;
import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.acceleratorservices.payment.data.PaymentSubscriptionResultItem;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WholescriptsTokenizationService {
   public WholescriptsPaymetricTokenizationData getAccessToken(HttpServletRequest request);
   String getResponsePacket(HttpServletRequest request);

   PaymentSubscriptionResultItem completeSopCreatePaymentSubscription(B2BCustomerModel userModel, SopPaymentDetailsWsDTO sopPaymentDetails, Map<String, String> parameterMap) throws Exception;
}
