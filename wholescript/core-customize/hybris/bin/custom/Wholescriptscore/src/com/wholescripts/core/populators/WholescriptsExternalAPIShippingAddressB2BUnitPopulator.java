package com.wholescripts.core.populators;

import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.*;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Vinay
 */

public class WholescriptsExternalAPIShippingAddressB2BUnitPopulator implements Populator<B2BUnitModel, WholescriptsExternalAPIAddressData> {

    @Resource(name = "wholescriptsHelper")
    protected WholescriptsHelper wholescriptsHelper;

    @Override
    public void populate(B2BUnitModel b2BUnitModel, WholescriptsExternalAPIAddressData target) throws ConversionException {

        AddressModel addressModel = WholescriptsHelper.returnValidAddress(b2BUnitModel);
        if(null != addressModel){
      	  
            target.setLanguage("E");
            if(null != b2BUnitModel.getSapShippingCond()){
                target.setShippingCondition(b2BUnitModel.getSapShippingCond().getCode());
            }

            if (addressModel.getSapCustomerID() != null) {
            	target.setAddressId(String.format("%010d",Integer.valueOf(addressModel.getSapCustomerID())));
            }
            target.setFirstName(addressModel.getFirstname());
            target.setLastName(addressModel.getLastname());
            target.setAddressName(addressModel.getRemarks());
            target.setPhone(addressModel.getPhone1());

            Set<PrincipalModel> members = b2BUnitModel.getMembers();
            if(CollectionUtils.isNotEmpty(members)){
                for(PrincipalModel member: members){
                    if(member instanceof CustomerModel){
                        B2BCustomerModel customer = (B2BCustomerModel)member;
                            target.setEmail(customer.getEmail());
                        break;
                    }
                }
            }
            target.setStreet1(addressModel.getLine1());
            target.setStreet2(addressModel.getLine2());
            target.setCity(addressModel.getTown());
            if (addressModel.getRegion() != null) {
                target.setRegion(addressModel.getRegion().getIsocodeShort());
            }
            target.setZip(addressModel.getPostalcode());
            if (null != addressModel.getCountry()) {
                target.setCountry(addressModel.getCountry().getIsocode());
            }
        }
    }
}


