package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WholescriptsPractitionerDao;
import com.wholescripts.core.service.WholescriptsPractitionerService;
import com.wholescripts.dto.WholeScriptsPractitionerWsDTo;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WholescriptsPractitionerServiceImpl implements WholescriptsPractitionerService {

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "wholescriptsPractitionerDao")
    private WholescriptsPractitionerDao wholescriptsPractitionerDao;

    @Resource(name = "mediaService")
    private MediaService mediaService ;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    @Resource(name = "pointOfServiceService")
    private PointOfServiceService pointOfServiceService;

    @Override
    public B2BCustomerModel savePractitionerDetails(WholeScriptsPractitionerWsDTo practitioner, GPS gps) {

        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        if(practitioner!=null && !practitioner.getPractitionerDetails().isEmpty())
        {
            PointOfServiceWsDTO practitionerDTO=practitioner.getPractitionerDetails().get(0);
            AddressModel address=modelService.create(AddressModel.class);
            final PointOfServiceModel practitionerDetails=modelService.create(PointOfServiceModel.class);
            AddressWsDTO addressDTo=practitionerDTO.getAddress();
            if(addressDTo!=null)
            {
                address.setStreetname(addressDTo.getLine1());
                address.setStreetnumber(addressDTo.getLine2());
                address.setPostalcode(addressDTo.getPostalCode());
                address.setTown(addressDTo.getTown());
                address.setOwner(practitionerDetails);
                address.setVisibleInAddressBook(false);
                if(addressDTo.getRegion()!=null && addressDTo.getRegion().getIsocode()!=null)
                {
                    String regionCode=addressDTo.getRegion().getIsocode();
                    final RegionModel regionModel = commonI18NService.getRegion(commonI18NService.getCountry("US"), regionCode);
                    address.setRegion(regionModel);
                }
            }
            address.setFirstname(practitionerDTO.getPractitionerName());

            practitionerDetails.setDisplayName(practitionerDTO.getName());
            practitionerDetails.setName(practitionerDTO.getName());
            practitionerDetails.setPractitionerDirectoryOptIn(true);
            practitionerDetails.setWebSite(practitionerDTO.getWebSite());
            practitionerDetails.setPhoneNumber(practitionerDTO.getPhoneNumber());
            practitionerDetails.setEmail(practitionerDTO.getEmail());
            practitionerDetails.setAddress(address);
            practitionerDetails.setType(PointOfServiceTypeEnum.PRACTICE);
            practitionerDetails.setLatitude(gps.getDecimalLatitude());
            practitionerDetails.setLongitude(gps.getDecimalLongitude());
            practitionerDetails.setBaseStore(baseStoreService.getCurrentBaseStore());
            Set<PointOfServiceModel> pointOfServiceModels=new HashSet<>();
            if(!customer.getPractitionerDetails().isEmpty())
            {
                Set<PointOfServiceModel> existingPos=new HashSet<>();
                existingPos.addAll(customer.getPractitionerDetails());
                pointOfServiceModels.addAll(existingPos);
            }
            pointOfServiceModels.add(practitionerDetails);

            customer.setPractitionerDetails(pointOfServiceModels);

            modelService.saveAll();

        }
        return  customer;
    }

    @Override
    public PointOfServiceModel updatePractitionerDirectoryOptIn(String posName,Boolean optOutPractice,B2BCustomerModel customer) {
        PointOfServiceModel pos = wholescriptsPractitionerDao.getPosDetailsByPosName(posName,customer, baseStoreService.getCurrentBaseStore());
        if(pos!=null)
        {
            pos.setPractitionerDirectoryOptIn(false);
            modelService.save(pos);
        }
        return pos;
    }

    @Override
    public PointOfServiceModel updatePractitionerDetails(String posName, B2BCustomerModel customer, WholeScriptsPractitionerWsDTo practitioner, GPS gps) {
        PointOfServiceModel pos= wholescriptsPractitionerDao.getPosDetailsByPosName(posName,customer, baseStoreService.getCurrentBaseStore());
        if(pos!=null)
        {
            AddressModel address=pos.getAddress();
            if(practitioner!=null && practitioner.getPractitionerDetails()!=null)
            {
                PointOfServiceWsDTO practitionerDTO=practitioner.getPractitionerDetails().get(0);
                if(practitionerDTO!=null)
                {
                    AddressWsDTO addressDTo=practitionerDTO.getAddress();
                    if(addressDTo!=null)
                    {
                        address.setStreetname(addressDTo.getLine1());
                        address.setStreetnumber(addressDTo.getLine2());
                        address.setPostalcode(addressDTo.getPostalCode());
                        address.setTown(addressDTo.getTown());
                        if(addressDTo.getRegion()!=null && addressDTo.getRegion().getIsocode()!=null)
                        {
                           String regionCode=addressDTo.getRegion().getIsocode();
                            final RegionModel regionModel = commonI18NService.getRegion(commonI18NService.getCountry("US"), regionCode);
                            address.setRegion(regionModel);
                        }
                    }
                    if(address!=null)
                    {
                        address.setFirstname(practitionerDTO.getPractitionerName());

                    }
                        if(StringUtils.isNotEmpty(practitionerDTO.getName()))
                        {
                            pos.setDisplayName(practitionerDTO.getName());
                        }
                        if(StringUtils.isNotEmpty(practitionerDTO.getWebSite()))
                        {
                            pos.setWebSite(practitionerDTO.getWebSite());
                        }
                        if(StringUtils.isNotEmpty(practitionerDTO.getPhoneNumber()))
                        {
                            pos.setPhoneNumber(practitionerDTO.getPhoneNumber());
                        }
                        if(StringUtils.isNotEmpty(practitionerDTO.getEmail()))
                        {
                            pos.setEmail(practitionerDTO.getEmail());
                        }
                        pos.setLongitude(gps.getDecimalLongitude());
                        pos.setLatitude(gps.getDecimalLatitude());
                        pos.setAddress(address);
                }
            }

            modelService.saveAll();
        }

        return pos;
    }

    @Override
    public PointOfServiceModel saveProfileImage(String posName, B2BCustomerModel customer, MultipartFile file) {
        PointOfServiceModel pos=wholescriptsPractitionerDao.getPosDetailsByPosName(posName,customer, baseStoreService.getCurrentBaseStore());
        final CatalogUnawareMediaModel mediaModel = modelService.create(CatalogUnawareMediaModel.class);
        mediaModel.setCode(file.getOriginalFilename()+ UUID.randomUUID().toString());
        mediaModel.setRealFileName(file.getOriginalFilename());
        modelService.save(mediaModel);
        if(pos!=null)
        {
            pos.setMapIcon(mediaModel);
            modelService.save(pos);
        }

        try
        {
            mediaService.setStreamForMedia(mediaModel,file.getInputStream());
        }
        catch (final Exception e)
        {
            e.printStackTrace();

        }
        return pos;
    }

    @Override
    public PointOfServiceModel getPractitionerDetails(String posName, B2BCustomerModel customer) {
        return   wholescriptsPractitionerDao.getPosDetailsByPosName(posName,customer,baseStoreService.getCurrentBaseStore());
    }

    @Override
    public PointOfServiceModel getPosDetailsbyUid(String uid) {
        return wholescriptsPractitionerDao.getPosDetailsbyUid(uid,baseStoreService.getCurrentBaseStore());
    }

    @Override
    public void deletePractitionerProfileImage(String posName) {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(posName);

        // Check if the current user is authorized to delete the profile image
        if (pos == null || pos.getPractitioner() == null || !pos.getPractitioner().equals(customer)) {
            throw new AccessDeniedException("Unauthorized access: Current user is not permitted to delete this profile image.");
        }

        if(pos.getMapIcon() != null){
            modelService.remove(pos.getMapIcon());
            pos.setMapIcon(null);
            modelService.save(pos);
        }else {
            throw new IllegalArgumentException("Map icon not found for the given PointOfService");
        }
    }

}
