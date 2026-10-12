package com.wholescripts.core.service.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

public class WholescriptsDefaultCustomerAccountService extends DefaultCustomerAccountService {
    @Override
    public List<AddressModel> getAllAddressEntries(CustomerModel customerModel) {
        validateParameterNotNull(customerModel, "Customer model cannot be null");
        final List<AddressModel> addressModels = new ArrayList<AddressModel>();
        B2BCustomerModel b2BCustomerModel= (B2BCustomerModel) customerModel;
        if(Objects.nonNull(b2BCustomerModel.getDefaultB2BUnit()) && CollectionUtils.isNotEmpty(b2BCustomerModel.getDefaultB2BUnit().getAddresses()))
        {
            addressModels.addAll(b2BCustomerModel.getDefaultB2BUnit().getAddresses());
        }

        return addressModels;
    }
}
