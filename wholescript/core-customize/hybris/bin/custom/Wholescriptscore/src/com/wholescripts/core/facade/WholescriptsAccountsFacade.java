package com.wholescripts.core.facade;
import com.wholescripts.data.AgingResponseData;
import com.wholescripts.data.SharedRewardsResponseData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
public interface WholescriptsAccountsFacade {
     SharedRewardsResponseData getSharedRewards(B2BCustomerModel b2bCustomer,String date) throws Exception;
     AgingResponseData getAging(B2BCustomerModel b2bCustomer,String keyDate) throws Exception;
}