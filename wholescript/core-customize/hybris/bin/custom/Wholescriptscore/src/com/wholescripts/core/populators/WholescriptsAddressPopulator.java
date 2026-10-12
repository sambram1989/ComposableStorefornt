package com.wholescripts.core.populators;


import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;

public class WholescriptsAddressPopulator implements Populator<AddressModel, AddressData> {

    public void populate(AddressModel source, AddressData target) throws ConversionException {

        if (source.getSapPublicKey() != null && source.getSapPublicKey().contains("|")) {
            target.setAddressNumber(source.getSapPublicKey().split("\\|")[1]);
        }

        if (StringUtils.isNotBlank(source.getLine2())) {
            target.setLine2(source.getLine2());
        } else {
            target.setLine2(null);
        }
        target.setRemarks(source.getRemarks());

        if (StringUtils.isNotBlank(source.getRemarks())) {
            target.setAddressDescription(source.getRemarks());
        } else {
            target.setAddressDescription(target.getAddressNumber());
        }

        if (source.getOwner() instanceof B2BUnitModel) {
            B2BUnitModel b2BUnitModel = (B2BUnitModel) source.getOwner();
            if (null != b2BUnitModel.getSapCustomerGroup()) {
                target.setAddressType(b2BUnitModel.getSapCustomerGroup().getCode());
            }
        }
        populateRegion(source, target);
    }

    protected void populateRegion(final AddressModel source, final AddressData target)
 	{
 		final RegionModel regionModel = source.getRegion();
 		if (regionModel != null)
 		{
 			final RegionData regionData = new RegionData();
 			regionData.setIsocode(regionModel.getIsocode());
 			regionData.setIsocodeShort(regionModel.getIsocodeShort());
 			regionData.setName(regionModel.getName());
 			regionData.setIsShippingTAxWarningEnabled(regionModel.getIsShippingTAxWarningEnabled());
 			if (regionModel.getCountry() != null)
 			{
 				regionData.setCountryIso(regionModel.getCountry().getIsocode());
 			}
 			target.setRegion(regionData);
 		}
 	}
}
