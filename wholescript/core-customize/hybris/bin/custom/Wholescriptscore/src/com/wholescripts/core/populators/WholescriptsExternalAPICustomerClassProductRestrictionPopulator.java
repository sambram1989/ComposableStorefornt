package com.wholescripts.core.populators;

import com.wholescripts.core.model.CustomerClassProductRestrictionModel;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.WholescriptsExternalExclusionCustclasMaterialData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;

/**
 * @author Vinay
 */

public class WholescriptsExternalAPICustomerClassProductRestrictionPopulator implements Populator<CustomerClassProductRestrictionModel, WholescriptsExternalExclusionCustclasMaterialData> {

    @Override
    public void populate(CustomerClassProductRestrictionModel source, WholescriptsExternalExclusionCustclasMaterialData target) throws ConversionException {
        if (StringUtils.isNotBlank(source.getCustomerClass())) {
            target.setKukla(source.getCustomerClass());
        }
        if (StringUtils.isNotBlank(source.getProductCode())) {
            target.setMatnr(source.getProductCode());
        }
        if (source.getValidFrom() != null) {
           target.setValid_from(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidFrom()));
       }
       if (source.getValidTo() != null) {
           target.setValid_to(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidTo()));
       }
    }
}
