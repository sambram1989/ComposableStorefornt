package com.wholescripts.core.populators;

import com.wholescripts.core.model.BrandCustomerClassRestrictionModel;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.WholescriptsExternalExclusionCustclasBrandData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;

/**
 * @author Vinay
 */

public class WholescriptsExternalAPIBrandCustomerClassRestrictionPopulator implements Populator<BrandCustomerClassRestrictionModel, WholescriptsExternalExclusionCustclasBrandData> {

    @Override
    public void populate(BrandCustomerClassRestrictionModel source, WholescriptsExternalExclusionCustclasBrandData target) throws ConversionException {
        if (StringUtils.isNotBlank(source.getBrandCode())) {
            target.setZ_mm_brands(source.getBrandCode());
        }
        if (StringUtils.isNotBlank(source.getCustomerClass())) {
            target.setKukla(source.getCustomerClass());
        }
        if (StringUtils.isNotBlank(source.getDescription())) {
            target.setDescription(source.getDescription());
        }
        if (source.getValidFrom() != null) {
            target.setValid_from(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidFrom()));
        }
        if (source.getValidTo() != null) {
            target.setValid_to(WholescriptsDateUtils.convertDateInUTCFormat(source.getValidTo()));
        }

    }
}
