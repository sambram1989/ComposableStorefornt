package com.wholescripts.core.populators;

import com.wholescripts.core.model.CustomerProductRestrictionModel;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.WholescriptsExternalExclusionCustMatlData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;

/**
 * @author Vinay
 */

public class WholescriptsExternalAPICustomerProductRestrictionPopulator implements Populator<CustomerProductRestrictionModel, WholescriptsExternalExclusionCustMatlData> {

    @Override
    public void populate(CustomerProductRestrictionModel source, WholescriptsExternalExclusionCustMatlData target) throws ConversionException {
        if (StringUtils.isNotBlank(source.getCustomerSAPId())) {
            target.setCustomer(source.getCustomerSAPId());
        }
        if (StringUtils.isNotBlank(source.getProductCode())) {
            target.setMaterial(source.getProductCode());
        }
        if (source.getValidFrom() != null) {
           target.setValid_from(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidFrom()));
       }
       if (source.getValidTo() != null) {
           target.setValid_to(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidTo()));
       }
    }
}
