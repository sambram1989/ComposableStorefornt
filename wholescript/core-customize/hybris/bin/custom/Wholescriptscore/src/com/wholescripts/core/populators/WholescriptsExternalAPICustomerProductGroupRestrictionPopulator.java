package com.wholescripts.core.populators;

import com.wholescripts.core.model.CustomerProductGroupRestrictionModel;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.WholescriptsExternalExclusionCustMatlgrpData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;

/**
 * @author Vinay
 */

public class WholescriptsExternalAPICustomerProductGroupRestrictionPopulator implements Populator<CustomerProductGroupRestrictionModel, WholescriptsExternalExclusionCustMatlgrpData> {

    @Override
    public void populate(CustomerProductGroupRestrictionModel source, WholescriptsExternalExclusionCustMatlgrpData target) throws ConversionException {
        if(StringUtils.isNotBlank(source.getProductGroup())){
            target.setMatkl(source.getProductGroup());
        }
        if(StringUtils.isNotBlank(source.getCustomerSAPId())){
            target.setKunnr(source.getCustomerSAPId());
        }
        if (source.getValidFrom() != null) {
           target.setValid_from(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidFrom()));
       }
       if (source.getValidTo() != null) {
           target.setValid_to(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidTo()));
       }
    }
}
