/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.ReferralCodesDao;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsPatientDirectHubService;
import com.wholescripts.data.WholescriptsAccountChangeResponseData;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;


import java.io.IOException;
import java.util.*;


import javax.annotation.Resource;

import com.wholescripts.core.model.ReferralCodesModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 */
public class WholescriptsPatientDirectHubServiceImpl implements WholescriptsPatientDirectHubService {
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "wsPatientDirectHubCodeGenerator")
    private KeyGenerator wsPatientDirectHubCodeGenerator;
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "referralCodesDao")
    private ReferralCodesDao referralCodesDao;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "mediaService")
    private MediaService mediaService;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;
    @Resource(name = "b2bCommerceB2BUserGroupService")
 	private B2BCommerceB2BUserGroupService b2bCommerceB2BUserGroupService;
 	@Resource(name = "configurationService")
 	private ConfigurationService configurationService;

    @Override
    public ReferralCodesModel createReferralCodeAndPatientDirectHub(ReferralCodeWsDTO referralCodeRequest) throws Exception {
        B2BCustomerModel user = b2bCustomerService.getCurrentB2BCustomer();
        wholescriptsIntegrationsService.createReferralCodeData(user, referralCodeRequest);
        ReferralCodesModel referralModel = modelService.create(ReferralCodesModel.class);

        referralModel.setReferralCode(referralCodeRequest.getReferralCode().toUpperCase());
        referralModel.setDiscount(referralCodeRequest.getDiscount());
        referralModel.setLastName(referralCodeRequest.getLastName().toUpperCase());
        referralModel.setReferralLink(referralCodeRequest.getReferralCode().toUpperCase()+"-"+referralCodeRequest.getLastName().toUpperCase() );
        if (Objects.nonNull(user) && Objects.nonNull(user.getDefaultB2BUnit())) {
            B2BUnitModel b2bUnit = user.getDefaultB2BUnit();
            referralModel.setB2bUnit(b2bUnit);  // Directly set B2BUnit
            if(Objects.isNull(b2bUnit.getIsPatientDirect()) || !b2bUnit.getIsPatientDirect()) {
            	b2bUnit.setIsPatientDirect(Boolean.TRUE);
            	Set<PrincipalGroupModel> groups = new HashSet<>(user.getGroups());
            	groups.add(b2bCommerceB2BUserGroupService.getUserGroupForUID(
   						configurationService.getConfiguration().getString("wholescripts.patient.direct.hub.group"), UserGroupModel.class));
            	user.setGroups(groups);
            	modelService.save(user);
            }

            //Remove Dependency of Patient DirectHub because we are setting referral codes in B2bUnit
           /* if (!Objects.nonNull(b2bUnit.getPatientDirectHub())) {
                PatientDirectHubModel patientDirectHubModel = createNewPatientDirectHub();
                associatePatientDirectHubWithCustomer(user, patientDirectHubModel);
                b2bUnit.setPatientDirectHub(patientDirectHubModel);
            }*/
        }

        modelService.save(referralModel);
        return referralModel;
    }

    //Remove Dependency of Patient DirectHub because we are setting referral codes in B2bUnit

   /* private PatientDirectHubModel createNewPatientDirectHub() {
        PatientDirectHubModel patientDirectHubModel = modelService.create(PatientDirectHubModel.class);
        patientDirectHubModel.setId(wsPatientDirectHubCodeGenerator.generate().toString());
        modelService.save(patientDirectHubModel);
        return patientDirectHubModel;
    }

    private void associatePatientDirectHubWithCustomer(UserModel user, PatientDirectHubModel patientDirectHubModel) {
        if (user instanceof B2BCustomerModel) {
            B2BCustomerModel customer = (B2BCustomerModel) user;
            Optional.ofNullable(customer.getDefaultB2BUnit())
                    .ifPresent(unit -> {
                        unit.setPatientDirectHub(patientDirectHubModel);
                        modelService.save(unit);
                    });
        }
    }*/


    @Override
    public SearchPageData<ReferralCodesModel> getReferralCodesData(final SearchPageData searchPageData, final String uId) {
        return referralCodesDao.getReferralCodesListByUser(searchPageData, uId);
    }

    @Override
    public ReferralCodesModel updateReferralCodeData(String referralCode, String referralLink, int discount) throws Exception {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        wholescriptsIntegrationsService.updateReferralCodeData(customer,referralCode,discount);
        final Optional<ReferralCodesModel> referralCodesModelOptional = referralCodesDao.getReferralCodeModelForID(referralCode.split("\\|")[0],referralCode.split("\\|")[1]);

        if (referralCodesModelOptional.isPresent()) {
            ReferralCodesModel referralCodesModel = referralCodesModelOptional.get();
            if (StringUtils.isNotEmpty(referralLink)) {
                referralCodesModel.setReferralLink(referralLink);
            }
            if (discount >= 0) {
                referralCodesModel.setDiscount(discount);
            }
            modelService.save(referralCodesModel);
            return referralCodesModel;
        } else {
            throw new ModelNotFoundException("Referral code not found for the given customer");
        }
    }

    @Override
    public List<ReferralCodesModel> getReferralCodesCSV()  {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        return referralCodesDao.getReferralCodesCSV(customer.getUid());
    }


    @Override
    public ReferralCodesModel uploadProfileImage(String referralCode, String lastName, B2BCustomerModel customerForProfileImage, MultipartFile imagefile) {
        // Fetch the ReferralCodesModel based on referral code and customer UID
        final Optional<ReferralCodesModel> referralCodesModelOptional = referralCodesDao.getReferralCodeModelForID(referralCode,lastName);

        // Check if referral code model exists
        if (!referralCodesModelOptional.isPresent()) {
            throw new IllegalArgumentException("Referral code not found for the customer");
        }

        final CatalogUnawareMediaModel mediaModel = modelService.create(CatalogUnawareMediaModel.class);
        mediaModel.setCode(imagefile.getOriginalFilename()+ UUID.randomUUID().toString());
        mediaModel.setRealFileName(imagefile.getOriginalFilename());
        mediaModel.setMime(imagefile.getContentType());
        modelService.save(mediaModel);

        try {
            mediaService.setStreamForMedia(mediaModel, imagefile.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload the profile image", e);
        }

        ReferralCodesModel referralCodesModel = referralCodesModelOptional.get();
        referralCodesModel.setProfileImage(mediaModel);
        modelService.save(referralCodesModel);

        return referralCodesModel;
    }
    @Override
    public AddressModel addingAddressToReferralCodes(String referralCodeId, AddressData addressData) {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();

        final Optional<ReferralCodesModel> referralCodesModelOptional = referralCodesDao.getReferralCodeModelForID(referralCodeId.split("\\|")[0],referralCodeId.split("\\|")[1]);
        if (referralCodesModelOptional.isEmpty()) {
            throw new IllegalArgumentException("Referral Code not found for ID: " + referralCodeId);
        }
        ReferralCodesModel referralCodesModel = referralCodesModelOptional.get();
        AddressModel addressModel = modelService.create(AddressModel.class);
        addressModel.setOwner(customer);
        if (StringUtils.isNotBlank(addressData.getTitleCode())) {
            try {
                final TitleModel titleExample = new TitleModel();
                titleExample.setCode(addressData.getTitleCode());
                TitleModel title = flexibleSearchService.getModelByExample(titleExample);
                addressModel.setTitle(title);
            } catch (ModelNotFoundException e) {
                addressModel.setTitle(null);
            }
        } else {
            addressModel.setTitle(null);
        }
        addressModel.setFirstname(addressData.getFirstName());
        addressModel.setLastname(addressData.getLastName());
        addressModel.setEmail(addressData.getEmail());
        addressModel.setLine1(addressData.getLine1());
        addressModel.setLine2(addressData.getLine2());
        addressModel.setTown(addressData.getTown());
        addressModel.setPostalcode(addressData.getPostalCode());
        addressModel.setPhone1(addressData.getPhone());

        // Set the region, if provided
        if (addressData.getRegion() != null && addressData.getCountry() != null) {
            final String regionIsocode = addressData.getRegion().getIsocode();
            final String countryIsocode = addressData.getCountry().getIsocode();
            try {
                final RegionModel regionModel = commonI18NService.getRegion(
                        commonI18NService.getCountry(countryIsocode), regionIsocode);
                addressModel.setRegion(regionModel);
            } catch (final UnknownIdentifierException | AmbiguousIdentifierException e) {
                throw new ConversionException("Region lookup failed for ISO code: " + regionIsocode, e);
            }
        }

        modelService.save(addressModel);

        Collection<AddressModel> addresses = referralCodesModel.getAddress();
        if (addresses == null) {
            addresses = new ArrayList<>();
        } else {

            addresses = new ArrayList<>(addresses);
        }
        addresses.add(addressModel);

        referralCodesModel.setWebsite(addressData.getWebsite());
        referralCodesModel.setDescription(addressData.getDescription());
        referralCodesModel.setAddress(addresses);

        modelService.save(referralCodesModel);
        return addressModel;
    }
    @Override
    public void removeProfileImage(String referralCode) {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        final Optional<ReferralCodesModel> referralCodesModelOptional = referralCodesDao.getReferralCodeModelForID(referralCode.split("\\|")[0],referralCode.split("\\|")[1]);
        if (!referralCodesModelOptional.isPresent()) {
            throw new IllegalArgumentException("Referral code not found for the customer");
        }
        ReferralCodesModel referralCodesModel = referralCodesModelOptional.get();
        if (referralCodesModel.getProfileImage() == null) {
            throw new IllegalStateException("No profile image found for this referral code");
        }
        referralCodesModel.setProfileImage(null);
        modelService.save(referralCodesModel);
    }

    @Override
    public void updateAccountUpdateForPatient(ReferralCodeWsDTO referralCodeRequest, String patientId) throws Exception {
        B2BCustomerModel customer = b2bCustomerService.getUserForUID(patientId);
        if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
            WholescriptsAccountChangeResponseData response = wholescriptsIntegrationsService.accountChangeForPatient(customer, referralCodeRequest);
            if (Objects.nonNull(response) && "0".equals(response.getReturnCode())) {
                B2BUnitModel unit = customer.getDefaultB2BUnit();
                if (unit != null) {
                    Set<ReferralCodesModel> referralCodes = unit.getReferralCodes();
                    if (referralCodes != null && !referralCodes.isEmpty()) {
                  	  modelService.removeAll(referralCodes);
                        ReferralCodesModel firstReferral = modelService.create(ReferralCodesModel.class);
                        firstReferral.setReferralCode(referralCodeRequest.getReferralCode());
                        firstReferral.setLastName(referralCodeRequest.getLastName());
                        firstReferral.setReferralCodeType("ZREFC1");
                        firstReferral.setDiscount(referralCodeRequest.getDiscount());
                        modelService.save(firstReferral);
                        unit.setDiscount(String.valueOf(referralCodeRequest.getDiscount()));   
                        unit.setReferralCodes(Set.of(firstReferral));
                        modelService.save(unit);
                        modelService.refresh(unit);
                    }
                }
            }
        }
    }



    @Override
    public ReferralCodesModel getReferralCodeDetails(String referralCode) {
        final Optional<ReferralCodesModel> referralCodesModelOptional = referralCodesDao.getReferralCodeModelForID(referralCode.split("\\|")[0],referralCode.split("\\|")[1]);
        return referralCodesModelOptional.get();
    }

    @Override
    public ReferralCodesModel getReferralLinkDetails(String referralLink) {
        final Optional<ReferralCodesModel> referralCodesModelOptional = referralCodesDao.getReferralLinkForID(referralLink);
        return referralCodesModelOptional.get();
    }
}
