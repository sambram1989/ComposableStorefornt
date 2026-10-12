package com.wholescripts.core.facade.impl;

import com.wholescripts.core.dao.WholescriptsPractitionerDao;
import com.wholescripts.core.facade.WholescriptsPractitionerFacade;
import com.wholescripts.core.service.WholescriptsPractitionerService;
import com.wholescripts.dto.WholeScriptsPractitionerWsDTo;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;


public class WholescriptsPractitionerFacadeImpl implements WholescriptsPractitionerFacade {

    @Resource(name="wholescriptsPractitionerService")
    private WholescriptsPractitionerService wholescriptsPractitionerService;

    @Resource(name="defaultGoogleMapsGeoServiceWrapper")
    private GeoWebServiceWrapper geoWebServiceWrapper;

    @Resource(name = "wholescriptsUserConverter")
    Converter wholescriptsUserConverter;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "wholescriptsPractitionerDao")
    private WholescriptsPractitionerDao wholescriptsPractitionerDao;

    @Resource(name = "pointOfServiceConverter")
    Converter pointOfServiceConverter;

    @Override
    public GPS geocodeAddress(WholeScriptsPractitionerWsDTo practitioner) {
        if (practitioner!=null && CollectionUtils.isNotEmpty(practitioner.getPractitionerDetails())) {
            AddressWsDTO address = practitioner.getPractitionerDetails().get(0).getAddress();
            if(address!=null)
            {
                final AddressData addressData = new AddressData();
                addressData.setCity(address.getTown());
                addressData.setCountryCode("US");
                addressData.setStreet(address.getLine1());
                addressData.setBuilding(address.getLine2());
                addressData.setZip(address.getPostalCode());
                return geoWebServiceWrapper.geocodeAddress(addressData);
            }

        }
        return null;
    }

    @Override
    public PointOfServiceData savePractitionerDetails(WholeScriptsPractitionerWsDTo  practitioner,GPS gps) {
        B2BCustomerModel customer= wholescriptsPractitionerService.savePractitionerDetails(practitioner,gps);
        PointOfServiceData posData=new PointOfServiceData();
        if(customer!=null && customer  instanceof B2BCustomerModel)
        {
            CustomerData customerData= (CustomerData) wholescriptsUserConverter.convert(customer);
            return customerData.getPosData().get(customerData.getPosData().size()-1);
        }

        return posData;
    }

    @Override
    public PointOfServiceData getPractitionerDetails() {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        PointOfServiceData posData=new PointOfServiceData();
        if(customer!=null && customer  instanceof B2BCustomerModel)
        {
            PointOfServiceModel pos=wholescriptsPractitionerService.getPosDetailsbyUid(customer.getUid());
            if(Objects.nonNull(pos))
            {
                return (PointOfServiceData) pointOfServiceConverter.convert(pos);

            }
        }

        return posData;
    }

    @Override
    public PointOfServiceData updatePractitionerDirectoryOptIn(String posName, Boolean optOutPractice) {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        PointOfServiceData posData=new PointOfServiceData();
        if(customer!=null && customer  instanceof B2BCustomerModel)
        {
            PointOfServiceModel pos=wholescriptsPractitionerService.updatePractitionerDirectoryOptIn(posName,optOutPractice,customer);
            if(Objects.nonNull(pos))
            {
                return (PointOfServiceData) pointOfServiceConverter.convert(pos);

            }
        }

        return posData;
    }

    @Override
    public PointOfServiceData updatePractitionerDetails(WholeScriptsPractitionerWsDTo practitioner, String posName) {
        GPS gps= geocodeAddress(practitioner);
        PointOfServiceData posData=new PointOfServiceData();
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        if(gps!=null)
        {
            if(customer!=null && customer  instanceof B2BCustomerModel)
            {
                PointOfServiceModel pos = wholescriptsPractitionerService.updatePractitionerDetails(posName,customer,practitioner,gps);
                if(Objects.nonNull(pos))
                {
                    return (PointOfServiceData) pointOfServiceConverter.convert(pos);

                }
            }

        }
        else {
            PointOfServiceModel pos = wholescriptsPractitionerService.getPractitionerDetails(posName,customer);
            if(Objects.nonNull(pos))
            {
                return (PointOfServiceData) pointOfServiceConverter.convert(pos);

            }
        }
        return posData;
    }


    @Override
    public MediaData saveProfileImage(String posName, MultipartFile file) {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        PointOfServiceModel pos=wholescriptsPractitionerService.saveProfileImage(posName,customer,file);
        MediaData mediaData=new MediaData();
        if(pos!=null && pos.getMapIcon()!=null)
        {
            MediaModel media=pos.getMapIcon();
            mediaData.setAltText(media.getAltText());
            mediaData.setCode(media.getCode());
            mediaData.setDescription(media.getDescription());
            mediaData.setDownloadUrl(media.getDownloadURL());
            mediaData.setMime(media.getMime());
            mediaData.setUrl(media.getURL());
        }

        return mediaData;
    }

    @Override
    public void deletePractitionerProfileImage(String posName) {
        wholescriptsPractitionerService.deletePractitionerProfileImage(posName);
    }


}
