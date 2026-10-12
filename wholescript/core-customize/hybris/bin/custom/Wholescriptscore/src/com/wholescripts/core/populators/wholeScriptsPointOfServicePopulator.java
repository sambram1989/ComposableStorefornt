package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.storelocator.converters.populator.PointOfServicePopulator;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

public class wholeScriptsPointOfServicePopulator extends PointOfServicePopulator {
    @Override
    public void populate(PointOfServiceModel source, PointOfServiceData target) {
            target.setPhoneNumber(source.getPhoneNumber());
            target.setEmail(source.getEmail());
            target.setWebSite(source.getWebSite());
            target.setPractitionerDirectoryOptIn(source.getPractitionerDirectoryOptIn());
            if(source.getAddress()!=null)
            {
                target.setPractitionerName(source.getAddress().getFirstname());

            }
    }
}
