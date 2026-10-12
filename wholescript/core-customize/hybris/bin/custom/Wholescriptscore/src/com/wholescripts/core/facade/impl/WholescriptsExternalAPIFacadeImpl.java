package com.wholescripts.core.facade.impl;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.enums.ItemDuration;
import com.wholescripts.core.enums.ItemType;
import com.wholescripts.core.enums.RecommendationStatus;
import com.wholescripts.core.enums.SapCustomerGroupEnum;
import com.wholescripts.core.facade.WholescriptsCartFacade;
import com.wholescripts.core.facade.WholescriptsExternalAPIFacade;
import com.wholescripts.core.facade.WholescriptsOrdersFacade;
import com.wholescripts.core.facade.WholescriptsRecommendFacade;
import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.core.model.*;
import com.wholescripts.core.service.*;
import com.wholescripts.core.utils.WholeScriptsPriceUtils;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.*;
import com.wholescripts.dto.*;

import de.hybris.platform.acceleratorservices.payment.cybersource.enums.CardTypeEnum;
import de.hybris.platform.b2b.enums.CheckoutPaymentType;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.PaymentAuthorizationException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sadiq
 */
public class WholescriptsExternalAPIFacadeImpl implements WholescriptsExternalAPIFacade {

    private static final Logger LOG = Logger.getLogger(WholescriptsExternalAPIFacadeImpl.class);

    private final String DEFAULT_B2BUNIT = "wholescripts.default.b2b.unit.code";
    private final String SAP_CUSTOMER_GROUP = "external.api.customer.groups";

    @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;
    @Resource(name = "productService")
    ProductService productService;
    @Resource(name = "wholescriptsMedPaxService")
    WholescriptsMedPaxService wholescriptsMedPaxService;
    @Resource(name = "wholescriptsExternalAPIService")
    private WholescriptsExternalAPIService wholescriptsExternalAPIService;
    @Resource(name = "wholescriptsRecommendService")
    private WholescriptsRecommendService wholescriptsRecommendService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "wholescriptsRecommendFacade")
    private WholescriptsRecommendFacade wholescriptsRecommendFacade;
    @Resource(name = "wholescriptsMedpaxReverseConverter")
    private Converter<MedPaxWsDTO, MedPaxModel> wholescriptsMedpaxReverseConverter;
    @Resource(name = "wsExternalRecommendationConverter")
    private Converter<RecommendationModel, WsExternalRecommendationData> wsExternalRecommendationConverter;

    @Resource(name = "wholescriptsItemTimeDao")
    private WholescriptsItemTimeDao WholescriptsItemTimeDaoImpl;

    @Resource(name = "wholescriptsExternalAPIProductConverter")
    private Converter<ProductModel, WholescriptsExternalAPIProductData> wholescriptsExternalAPIProductConverter;

    @Resource(name = "wholescriptsExternalAPIPLProductConverter")
    private Converter<ProductModel, WholescriptsExternalAPIProductData> wholescriptsExternalAPIPLProductConverter;

    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Autowired
    private CartFactory cartFactory;

    @Resource(name = "b2bUnitService")
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

    @Autowired
    private CommonI18NService commonI18NService;

    @Autowired
    private CommerceCartService commerceCartService;

    @Autowired
    private BaseSiteService baseSiteService;

    @Resource(name = "wholescriptsCartFacade")
    private WholescriptsCartFacade wholescriptsCartFacade;

    @Resource(name = "b2bCheckoutFacade")
    private WholescriptsDefaultB2BCheckoutFacade b2bCheckoutFacade;

    @Resource(name = "wholescriptsOrdersFacade")
    private WholescriptsOrdersFacade wholescriptsOrdersFacade;

    @Autowired
    private ModelService modelService;

    @Autowired
    private DeliveryModeService deliveryModeService;

    @Resource(name = "wholescriptsProudctsService")
    private WholescriptsProudctsService wholescriptsProudctsService;

    @Resource(name = "wholescriptsBrandCustomerRestrictionClassConverter")
    private Converter<BrandCustomerClassRestrictionModel, WholescriptsExternalExclusionCustclasBrandData> wholescriptsBrandCustomerRestrictionClassConverter;

    @Resource(name = "wholescriptsCustomerClassProductRestrictionConverter")
    private Converter<CustomerClassProductRestrictionModel, WholescriptsExternalExclusionCustclasMaterialData> wholescriptsCustomerClassProductRestrictionConverter;

    @Resource(name = "wholescriptsCustomerProductGroupRestrictionConverter")
    private Converter<CustomerProductGroupRestrictionModel, WholescriptsExternalExclusionCustMatlgrpData> wholescriptsCustomerProductGroupRestrictionConverter;

    @Resource(name = "wholescriptsCustomerProductRestrictionConverter")
    private Converter<CustomerProductRestrictionModel, WholescriptsExternalExclusionCustMatlData> wholescriptsCustomerProductRestrictionConverter;

    @Resource(name = "wholescriptsExternalAPICustomerConverter")
    private Converter<B2BUnitModel, WholescriptsExternalAPICustomerData> wholescriptsExternalAPICustomerConverter;

    @Resource(name = "wholescriptsExternalAPIShippingAddressB2BUnitConverter")
    private Converter<B2BUnitModel, WholescriptsExternalAPIAddressData> wholescriptsExternalAPIShippingAddressB2BUnitConverter;

    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;

    @Resource(name = "customerConverter")
    private Converter<UserModel, CustomerData> customerConverter;

    @Resource(name = "wholescriptsExternalAPIPatientsConverter")
    private Converter<B2BUnitModel, WholescriptsExternalAPIPatientData> wholescriptsExternalAPIPatientsConverter;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Resource(name = "wholescriptsUserService")
    WholescriptsUserService wholescriptsUserService;

    @Resource(name = "externalAddressReversePopulator")
    private Populator<WholescriptsExternalAPIAddressData, AddressModel> externalAddressReversePopulator;

    @Override
    public List<String> getAllProductsBrandNames() {
        return wholescriptsExternalAPIService.getAllProductsBrandNames();
    }

    @Override
    public List<String> getAllProductsCategories() {
        return wholescriptsExternalAPIService.getAllProductsCategories();
    }

    @Override
    public ExternalAPIEMRRecommendationResponseWsDTO createRecommenDation(ExternalAPIEMRRecommendationWsDTO inputWsDTO) {

        ExternalAPIEMRRecommendationResponseWsDTO responseWsDTO = new ExternalAPIEMRRecommendationResponseWsDTO();
        B2BCustomerModel practitioner = null;
        B2BCustomerModel patient = null;
        final List<String> returnMessage = new ArrayList<String>();
        if (Objects.nonNull(inputWsDTO)) {
            if (Objects.nonNull(inputWsDTO.getPractitionerEmail())) {
                try {
                    practitioner = b2bCustomerService.getUserForUID(inputWsDTO.getPractitionerEmail());
                    patient = b2bCustomerService.getUserForUID(inputWsDTO.getPatientEmail());
                    final String defaultWSUnit = configurationService.getConfiguration().getString(DEFAULT_B2BUNIT);
                    if (Objects.nonNull(patient)) {
                  	  final String patientSalesPerson = patient.getDefaultB2BUnit() != null ? patient.getDefaultB2BUnit().getSalesPerson() : null;
                  	  final String practitionerAccount = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(practitioner.getDefaultB2BUnit().getUid());
                  	  
                  	  if (!defaultWSUnit.equalsIgnoreCase(patient.getDefaultB2BUnit().getUid()) && patientSalesPerson != null
                  			  && !practitionerAccount.equalsIgnoreCase(patientSalesPerson.replaceFirst("^0+", ""))) {
                  		  throw new RuntimeException("Patient " + inputWsDTO.getPatientEmail() + 
                  				  " not link to practitioner " + inputWsDTO.getPractitionerEmail());
                  	  }
                  	                    	  
                    } else {
                        patient = createPatient(inputWsDTO, defaultWSUnit);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }

                boolean referralCodeExists = false;
                if (Objects.nonNull(practitioner) && Objects.nonNull(practitioner.getDefaultB2BUnit())
                        && CollectionUtils.isNotEmpty(practitioner.getDefaultB2BUnit().getReferralCodes())) {

                    //final Set<ReferralCodesModel> patientRefferalCodes = patient.getDefaultB2BUnit().getReferralCodes();
                    final Set<ReferralCodesModel> practitionerRefferalCodes = practitioner.getDefaultB2BUnit().getReferralCodes();
                    //boolean verifyPatientRefferal = patientRefferalCodes.stream().anyMatch(refferal -> refferal.getReferralCode().equalsIgnoreCase(inputWsDTO.getReferralCode()) && refferal.getLastName().equalsIgnoreCase(inputWsDTO.getReferralName()));
                    boolean verifyPractitionerRefferal = practitionerRefferalCodes.stream().anyMatch(refferal -> refferal.getReferralCode().equalsIgnoreCase(inputWsDTO.getReferralCode()) && refferal.getLastName().equalsIgnoreCase(inputWsDTO.getReferralName()));
                    if (verifyPractitionerRefferal) {
                        referralCodeExists = true;
                    }
                }

                if (Objects.nonNull(practitioner) && Objects.nonNull(patient) && referralCodeExists) {
                    userService.setCurrentUser(practitioner);
                    final RecommendationModel recommendationModel = wholescriptsRecommendService.createRecommendation(inputWsDTO.getPractitionerEmail(), inputWsDTO.getPatientEmail());
                    recommendationModel.setReferralCode(inputWsDTO.getReferralCode());
                    recommendationModel.setReferralName(inputWsDTO.getReferralName());
                    recommendationModel.setExternalOrder(true);
                    recommendationModel.setRecommendationStatus(RecommendationStatus.SENT);
                    recommendationModel.setSourceId(inputWsDTO.getSourceId());
                    recommendationModel.setSourceName(inputWsDTO.getSourceName());
                    if (inputWsDTO.getNotes() != null) {
                        final List<CommentModel> comments = wholescriptsUserService.saveCustomerNotes(inputWsDTO.getNotes(), practitioner);
                        recommendationModel.setComments(comments);
                    }
                    modelService.save(recommendationModel);

                    List<ExternalAPIEMRRecommendationItemsWsDTO> items = inputWsDTO.getItems();
                    if (CollectionUtils.isNotEmpty(items) && Objects.nonNull(recommendationModel)) {
                        for (ExternalAPIEMRRecommendationItemsWsDTO item : items) {
                            if (item.getMedPaxName().isBlank() && CollectionUtils.isEmpty(item.getMedPaxPills())) {
                                WsRecommendationEntryInputWsDTO rmEntryWsDTO = new WsRecommendationEntryInputWsDTO();
                                List<ItemDuration> list = enumerationService.getEnumerationValues(ItemDuration.class);
                                for (ItemDuration duration : list) {
                                    if (enumerationService.getEnumerationName(duration).equalsIgnoreCase(item.getItemDuration())) {
                                        rmEntryWsDTO.setItemDuration(duration.getCode());
                                        break;
                                    }
                                }

                                rmEntryWsDTO.setProductItemType(ItemType.BOTTLE.getCode());
                                rmEntryWsDTO.setNotes(item.getNotes());
                                final List<DosingWsDTO> dosingWsDTOList = new ArrayList<DosingWsDTO>();
                                String dosing = item.getDosing();
                                if (StringUtils.isNotBlank(dosing)) {
                                    final Set<String> dosageList = Arrays.stream(dosing.split("\\|")).collect(Collectors.toSet());
                                    for (String dosage : dosageList) {
                                        DosingWsDTO dosingWsDTO = new DosingWsDTO();
                                        ItemTimeWsDTO itemTimeWsDTO = new ItemTimeWsDTO();
                                        final List<String> dosingTiming = Arrays.stream(dosage.split("-")).toList();

                                        itemTimeWsDTO.setCode(dosingTiming.get(1));
                                        dosingWsDTO.setQuantity(Integer.valueOf(dosingTiming.get(0)));
                                        dosingWsDTO.setItemTimes(itemTimeWsDTO);
                                        dosingWsDTOList.add(dosingWsDTO);

                                    }
                                    rmEntryWsDTO.setDosing(dosingWsDTOList);

                                }
                                try {
                                    wholescriptsRecommendFacade.createEntryRecommendation(item.getSku(), Long.parseLong(item.getQuantity()), recommendationModel.getCode(), rmEntryWsDTO);
                                } catch (CommerceCartModificationException e) {
                                    throw new RuntimeException(e);
                                }

                            } else if (StringUtils.isNotBlank(item.getMedPaxName()) && CollectionUtils.isNotEmpty(item.getMedPaxPills())) {
                               
                           	  MedPaxWsDTO medPaxWsDTO = new MedPaxWsDTO();
                           	  if(item.getSku() != null ) {
                           		  
                           		  String cartonMaterialGroup = configurationService.getConfiguration().getString("wholescripts.carton.cmir.group");
                           		  
                           		  ProductModel medPaxCarton = productService.getProductForCode(item.getSku());
                           		  
                           		  if(!cartonMaterialGroup.equalsIgnoreCase(medPaxCarton.getSapMaterialGroup().getCode())) {
                           			  throw new RuntimeException("sku " + item.getSku() + " is not a valid carton " );
                           		  }
                           		  medPaxWsDTO.setExternalCartonCode(item.getSku());
                           		  
                           	  }
                           	 
                           	  
                                List<MedpaxEntryWsDTO> medpaxEntryWsDTOS = new ArrayList<MedpaxEntryWsDTO>();
                                medPaxWsDTO.setName(item.getMedPaxName());
                                medPaxWsDTO.setNotes(item.getNotes());
                                medPaxWsDTO.setRecommendationId(recommendationModel.getCode());
                                List<ItemDuration> list = enumerationService.getEnumerationValues(ItemDuration.class);
                                for (ItemDuration duration : list) {
                                    if (enumerationService.getEnumerationName(duration).equalsIgnoreCase(item.getItemDuration())) {
                                    	 medPaxWsDTO.setItemDuration(duration.getCode());
                                        break;
                                    }
                                }
                                for (ExternalAPIEMRRecommendationMedpaxPillsWsDTO medPax : item.getMedPaxPills()) {
                                    final List<DosingWsDTO> dosingWsDTOList = new ArrayList<DosingWsDTO>();
                                    DosingWsDTO dosingWsDTO = new DosingWsDTO();
                                    MedpaxEntryWsDTO medpaxEntryWsDTO = new MedpaxEntryWsDTO();
                                    ProductWsDTO productWsDTO = new ProductWsDTO();
                                    ItemTimeWsDTO itemTimeWsDTO = new ItemTimeWsDTO();
                                    ItemTimeModel itemModel = WholescriptsItemTimeDaoImpl.getItemTimeByName(medPax.getItemTime());
                                    itemTimeWsDTO.setCode(itemModel.getCode());
                                    dosingWsDTO.setItemTimes(itemTimeWsDTO);
                                    dosingWsDTO.setQuantity(Integer.valueOf(medPax.getQuantity()));
                                    productWsDTO.setCode(medPax.getSku());
                                    dosingWsDTO.setProduct(productWsDTO);
                                    dosingWsDTOList.add(dosingWsDTO);
                                    ProductModel product = productService.getProductForCode(medPax.getSku());
                                    if (Objects.nonNull(product)) {
                                        medpaxEntryWsDTO.setPrice(WholeScriptsPriceUtils.getWholeSalePrice(product));

                                    }
                                    medpaxEntryWsDTO.setProduct(productWsDTO);
                                    medpaxEntryWsDTO.setDosing(dosingWsDTOList);
                                    medpaxEntryWsDTO.setQuantity(Integer.valueOf(medPax.getQuantity()));
                                    medpaxEntryWsDTOS.add(medpaxEntryWsDTO);

                                }
                                medPaxWsDTO.setMedpaxEntry(medpaxEntryWsDTOS);
                                try {
                                    MedPaxModel medPaxModel = wholescriptsMedPaxService.createMedPax(wholescriptsMedpaxReverseConverter.convert(medPaxWsDTO));
                                    if (medPaxWsDTO.getNotes() != null) {
                                        final List<CommentModel> comments = wholescriptsUserService.saveCustomerNotes(medPaxWsDTO.getNotes(), practitioner);
                                        medPaxModel.setComments(comments);
                                        modelService.save(medPaxModel);
                                    }
                                } catch (ConversionException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }

                    if (inputWsDTO.getSendEmail()) {
                        wholescriptsRecommendService.triggerRecommendationNotificationsForUser(recommendationModel, false, inputWsDTO.getSendEmail());
                        responseWsDTO.setSendEmailStatus(true);
                    }
                    ExternalAPIEMRRecommendationResponseWsDTO responseDTO = new ExternalAPIEMRRecommendationResponseWsDTO();
                    responseWsDTO.setRecommendationId(recommendationModel.getCode());
                    responseWsDTO.setSuccess(true);
                    responseWsDTO.setPatientLink("");
                    responseWsDTO.setPatientId(patient.getUid());

                } else {

                    responseWsDTO.setSuccess(false);
                    responseWsDTO.getPatientLink();

                    if (Objects.isNull(practitioner)) {
                        returnMessage.add("Given Practitioner not Available in the System");
                        responseWsDTO.setReturnMessage(returnMessage);

                    } else if (Objects.isNull(patient)) {
                        returnMessage.add("Given Patient not Available in the System");
                        responseWsDTO.setReturnMessage(returnMessage);
                    } else {
                        returnMessage.add("Given Refferal code and Refferal name not Available in the System");
                        responseWsDTO.setReturnMessage(returnMessage);
                    }

                }

            }
        }
        return responseWsDTO;
    }

    public List<WholescriptsExternalAPIProductData> getAllProductsData() {
        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
        List<ProductModel> productModelList = productService.getAllProductsForCatalogVersion(catalogVersion);
        if (productModelList == null || productModelList.isEmpty()) {
            // Return an empty list to avoid NullPointerException
            return Collections.emptyList();
        }
        return wholescriptsExternalAPIProductConverter.convertAll(productModelList);
    }

    public List<WholescriptsExternalAPIProductData> getSelectedNonPLProductsData(final List<String> productSkus) {
        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
        List<ProductModel> productModelList = wholescriptsProudctsService.getNonPLProductsBySKUsList(productSkus, catalogVersion);
        if (productModelList == null || productModelList.isEmpty()) {
            return Collections.emptyList();
        }
        return wholescriptsExternalAPIProductConverter.convertAll(productModelList);
    }

    public List<WholescriptsExternalAPIProductData> getAllNonPLProducts() {
        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
        List<ProductModel> productModelList = wholescriptsProudctsService.getAllNonPLProducts(catalogVersion);
        if (productModelList == null || productModelList.isEmpty()) {
            return Collections.emptyList();
        }
        return wholescriptsExternalAPIProductConverter.convertAll(productModelList);
    }


    @Override
    public SapAddressResponseData createExternalAPIAddress(WholescriptsExternalAPIAddressData addressData, final B2BUnitModel b2bUnitModel) throws Exception {

        SapAddressResponseData responseData = wholescriptsExternalAPIService.createExternalAPIAddress(addressData);
        if (responseData != null && responseData.getRETURN().equals("0")) {
            final AddressModel newAddress = modelService.create(AddressModel.class);
            externalAddressReversePopulator.populate(addressData, newAddress);
            final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(b2bUnitModel);
            if (CollectionUtils.isNotEmpty(customers)) {
                final B2BCustomerModel customer = customers.iterator().next();
                wholescriptsUserService.saveAddressEntry(customer, newAddress, responseData);
            }
        }
        return responseData;
    }

    @Override
    public WsExternalRecommendationResponse getRecommendationsForExternal(final ExternalRecommendationRequestData request) {

        final WsExternalRecommendationResponse response = new WsExternalRecommendationResponse();
        final List<String> returnMessage = new ArrayList<String>();
        if (StringUtils.isNotEmpty(request.getPractitionerEmail())) {
            final B2BCustomerModel practitioner = b2bCustomerService.getUserForUID(request.getPractitionerEmail());
            
            B2BCustomerModel patient = null;
            
            if(StringUtils.isNotEmpty(request.getPatientEmail())) {
            	patient = b2bCustomerService.getUserForUID(request.getPatientEmail());
            	if(Objects.isNull(patient)) {
            		response.setSuccess(false);
                  returnMessage.add("Patient id is not valid");
                  response.setReturnMessage(returnMessage);
                  return response;
            	}
            }
            
            if (Objects.nonNull(practitioner)) {
                final Collection<RecommendationModel> recommendationModels = wholescriptsExternalAPIService.getRecommendationsForExternal(practitioner, patient, request.getRecommendationId());
                if (CollectionUtils.isNotEmpty(recommendationModels)) {
                    final List<WsExternalRecommendationData> recommendationData = wsExternalRecommendationConverter
                            .convertAll(recommendationModels);
                    response.setRecommendations(recommendationData);
                    response.setSuccess(true);
                } else {
                    response.setSuccess(false);
                    returnMessage.add("No recommendation available");
                    response.setReturnMessage(returnMessage);
                }

            } else {
                response.setSuccess(false);
                returnMessage.add("Practitioner id is not valid");
                response.setReturnMessage(returnMessage);
            }

        } else {
            response.setSuccess(false);
            returnMessage.add("Practitioner id is not valid");
            response.setReturnMessage(returnMessage);
        }
        return response;
    }

    @Override
    public ExternalOrderResponseData createExternalOrder(final ExternalOrderRequestData externalOrderRequest, final OrderCreationRequest orderCreationRequest) throws Exception {

        final ExternalOrderResponseData response = new ExternalOrderResponseData();
        final CartModel cart = cartFactory.createCart();
        LOG.info("Inside create external order of WholescriptsExternalAPIFacadeImpl, cart id  " + cart.getCode());
        final B2BUnitModel unitModel = getUnitFromSapId(externalOrderRequest.getCustomerSAPId());
        final DeliveryModeModel deliveryMode = deliveryModeService
                .getDeliveryModeForCode(externalOrderRequest.getShippingMethod());
        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
        catalogVersionService.setSessionCatalogVersions(Collections.singletonList(catalogVersion));
        final String baseSiteId = configurationService.getConfiguration().getString("wholescripts.basesite.id");
        final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(baseSiteId);
        final List<String> returnMessage = new ArrayList<String>();

        if (unitModel != null && deliveryMode != null) {
            try {
                LOG.info("Inside create external order of WholescriptsExternalAPIFacadeImpl, find unit " + unitModel.getUid()
                        + " deliverymodel " + deliveryMode.getCode() + " payment term " + externalOrderRequest.getPaymentTerms());
                final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(unitModel);
                if (CollectionUtils.isNotEmpty(customers)) {
                    cart.setUser(customers.iterator().next());
                    cart.setUnit(unitModel);
                    cart.setStatus(OrderStatus.CREATED);
                } else {
                    modelService.remove(cart);
                    return orderResponseError(response, returnMessage, "Invalid user for account number " + unitModel.getUid());
                }

                AddressModel shippingAddressModel = null;
                List<B2BUnitModel> shippingAddressB2BUnits = wholescriptsExternalAPIService.getShippingAddressB2BUnits(unitModel);
                for (B2BUnitModel shippingAddressB2BUnit : shippingAddressB2BUnits) {
                    final AddressModel validShippingAddress = WholescriptsHelper.returnValidAddress(shippingAddressB2BUnit);
                    if (validShippingAddress != null) {
                        final String addressId = String.format("%010d", Integer.valueOf(validShippingAddress.getSapCustomerID()));
                        if (addressId.equalsIgnoreCase(externalOrderRequest.getAddressId())) {
                            shippingAddressModel = validShippingAddress;
                            break;
                        }

                    }
                }

                if (shippingAddressModel != null) {
                    cart.setDeliveryAddress(shippingAddressModel);
                } else {
                    modelService.remove(cart);
                    return orderResponseError(response, returnMessage, "Invalid delivery address for account number " + unitModel.getUid());
                }

                final AddressModel billingAddressModel = WholescriptsHelper.returnValidBillingAddress(unitModel);
                if (billingAddressModel != null) {
                    cart.setPaymentAddress(billingAddressModel);
                }

                BaseStoreModel baseStore = null;
                if (baseSite != null) {
                    cart.setSite(baseSite);
                    baseStore = baseSite.getStores().get(0);
                    cart.setStore(baseStore);
                    cart.setCurrency(baseStore.getDefaultCurrency());
                    cart.setLocale(commonI18NService.getLocaleForLanguage(baseStore.getDefaultLanguage()).toString());
                }
                cart.setDeliveryMode(deliveryMode);
                cart.setDeliveryCost(externalOrderRequest.getShippingCharge());
                cart.setPaymentType(findCheckoutPaymentType(externalOrderRequest.getPaymentTerms()));

                for (ExternalOrderItemsData item : externalOrderRequest.getItems()) {
                    final ProductModel productModel = productService.getProductForCode(catalogVersion, item.getSku());

                    final CommerceCartParameter parameter = new CommerceCartParameter();
                    parameter.setCart(cart);
                    parameter.setProduct(productModel);
                    parameter.setUnit(productModel.getUnit());
                    parameter.setQuantity(item.getQuantity());
                    parameter.setExternalCart(true);
                    parameter.setEntryNumber(Long.valueOf(item.getLineNumber()));
                    parameter.setDescription(item.getDescription());
                    parameter.setDoseNote(item.getDoseNote());
                    if (StringUtils.isNotBlank(item.getParentLineNumber()) && Integer.valueOf(item.getParentLineNumber()) >= 0) {
                        parameter.setEntryGroupNumbers(Collections.singleton(Integer.valueOf(item.getParentLineNumber())));
                    } else {
                        parameter.setEntryGroupNumbers(Collections.singleton(Integer.valueOf(0)));
                    }
                    LOG.info("Inside create external order of WholescriptsExternalAPIFacadeImpl, performing add to cart for cart id  " + cart.getCode());
                    commerceCartService.addToCart(parameter);
                }

                cart.setInstructions(externalOrderRequest.getInstructions());
                cart.setSpecProcId(externalOrderRequest.getSpecProcId());
                cart.setWebOrderNumber(externalOrderRequest.getWebOrderNumber());
                cart.setExternalOrder(true);

                final SalesApplication source = enumerationService.getEnumerationValue(SalesApplication.class,
                        externalOrderRequest.getOrigin());
                cart.setSourceApplication(source);

                LOG.info("Inside create external order of WholescriptsExternalAPIFacadeImpl, performing order simulation for cart id  " + cart.getCode());
                if (externalOrderRequest.getPaymentTerms().equalsIgnoreCase("XCRD")) {
                    wholescriptsCartFacade.orderSimulate(cart, baseStore);
                    try {
                        final String estoreCardToken = unitModel.getEstoreCardToken();
                        if (StringUtils.isNotEmpty(estoreCardToken)) {
                        	
                        	CreditCardPaymentInfoModel ccPaymentInfo = ccPaymentInfo(cart, estoreCardToken);
                           if (Objects.nonNull(ccPaymentInfo)) {
                              LOG.info("Inside create external order of WholescriptsExternalAPIFacadeImpl, performing authorization of cart id  " + cart.getCode());
                              wholescriptsOrdersFacade.authorizeExternalOrderPayment(cart, ccPaymentInfo, orderCreationRequest);
                           } else {
                           	modelService.remove(cart);
                              return orderResponseError(response, returnMessage, "No payment card flagged ");
                           }
                        	
                        } else {
                        	 modelService.remove(cart);
                            return orderResponseError(response, returnMessage, "No payment card flagged ");
                        }
                    } catch (PaymentAuthorizationException ex) {
                        modelService.remove(cart);
                        ex.printStackTrace();
                        return orderResponseError(response, returnMessage, "Payment authorization failed ");
                    }
                    modelService.save(cart);
                }

                LOG.info("Inside create external order of WholescriptsExternalAPIFacadeImpl, performing place order of cart id " + cart.getCode());
                final OrderModel orderModel = b2bCheckoutFacade.placeOrder(cart, source);

                if (orderModel != null) {
                    response.setOrderNumber(orderModel.getCode());
                    response.setSuccess(true);
                }
            } catch (RuntimeException ex) {
                modelService.remove(cart);
                ex.printStackTrace();
                throw new RuntimeException(ex.getMessage(), ex);
            }
        } else {
            if (Objects.isNull(unitModel)) {
                orderResponseError(response, returnMessage,
                        "Invalid customer sap id " + externalOrderRequest.getCustomerSAPId());
            }
            if (Objects.isNull(deliveryMode)) {
                orderResponseError(response, returnMessage,
                        "Invalid shipping method " + externalOrderRequest.getShippingMethod());
            }

        }

        return response;
    }

    public WholescriptsExternalRestrictedProductsData getRestrictedProductsData() {

        WholescriptsExternalRestrictedProductsData wholescriptsExternalRestrictedProductsData = new WholescriptsExternalRestrictedProductsData();

        List<BrandCustomerClassRestrictionModel> brandCustomerClassRestrictionModels = wholescriptsExternalAPIService.getBrandCustomerClassRestrictions();
        if (CollectionUtils.isNotEmpty(brandCustomerClassRestrictionModels)) {

            List<WholescriptsExternalExclusionCustclasBrandData> wholescriptsExternalExclusionCustclasBrandDataList = wholescriptsBrandCustomerRestrictionClassConverter.convertAll(brandCustomerClassRestrictionModels);
            wholescriptsExternalRestrictedProductsData.setExclusion_custclas_brand(wholescriptsExternalExclusionCustclasBrandDataList);

        }

        List<CustomerClassProductRestrictionModel> customerClassProductRestrictionModels = wholescriptsExternalAPIService.getCustomerClassProductRestrictions();
        if (CollectionUtils.isNotEmpty(customerClassProductRestrictionModels)) {

            List<WholescriptsExternalExclusionCustclasMaterialData> wholescriptsExternalExclusionCustclasMaterialDataList = wholescriptsCustomerClassProductRestrictionConverter.convertAll(customerClassProductRestrictionModels);
            wholescriptsExternalRestrictedProductsData.setExclusion_custclas_material(wholescriptsExternalExclusionCustclasMaterialDataList);

        }

        List<CustomerProductGroupRestrictionModel> customerProductGroupRestrictionModels = wholescriptsExternalAPIService.getCustomerProductGroupRestrictions();
        if (CollectionUtils.isNotEmpty(customerProductGroupRestrictionModels)) {

            List<WholescriptsExternalExclusionCustMatlgrpData> wholescriptsExternalExclusionCustMatlgrpDataList = wholescriptsCustomerProductGroupRestrictionConverter.convertAll(customerProductGroupRestrictionModels);
            wholescriptsExternalRestrictedProductsData.setExclusion_cust_matlgrp(wholescriptsExternalExclusionCustMatlgrpDataList);

        }

        List<CustomerProductRestrictionModel> customerProductRestrictionModels = wholescriptsExternalAPIService.getCustomerProductRestrictions();
        if (CollectionUtils.isNotEmpty(customerProductRestrictionModels)) {

            List<WholescriptsExternalExclusionCustMatlData> wholescriptsExternalExclusionCustMatlDataList = wholescriptsCustomerProductRestrictionConverter.convertAll(customerProductRestrictionModels);
            wholescriptsExternalRestrictedProductsData.setExclusion_cust_matl(wholescriptsExternalExclusionCustMatlDataList);

        }

        return wholescriptsExternalRestrictedProductsData;
    }

    public List<WholescriptsExternalAPIProductData> getPLProductsData(final String customerId) {

        List<CustomerMaterialInformationModel> customerMaterialInformationModels = wholescriptsProudctsService.getCustomerMaterialInformationByCustomerId(customerId);
        List<WholescriptsExternalAPIProductData> productDatas = new ArrayList<WholescriptsExternalAPIProductData>();
        if (CollectionUtils.isEmpty(customerMaterialInformationModels)) {
            return Collections.emptyList();
        } else {
      	  
      	  final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                 configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                 configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));

      	  for (CustomerMaterialInformationModel cmiModel : customerMaterialInformationModels) {
      		  
      		  WholescriptsExternalAPIProductData productData = new WholescriptsExternalAPIProductData();
      		  
				  if (StringUtils.isNotEmpty(cmiModel.getBlueLabel())) {
					  productData.setBlueLabelSku(cmiModel.getBlueLabel());
				  }

				  if (StringUtils.isNotEmpty(cmiModel.getBrightStock())) {
					  productData.setBrightstockSku(cmiModel.getBrightStock());
				  }
				  if (cmiModel.getProductCode() != null) {
					  String productCode = String.format("%018d", Integer.valueOf(cmiModel.getProductCode()));
					  ProductModel productModel = wholescriptsProudctsService.getPLProductBySKU(productCode, catalogVersion);
					  if (productModel != null) {
						  
						  productData = wholescriptsExternalAPIPLProductConverter.convert(productModel, productData);
						  productDatas.add(productData);

					  }
				  }
      	  }
        }
        return productDatas;
    }


    private ExternalOrderResponseData orderResponseError(final ExternalOrderResponseData response, final List<String> returnMessage, final String errorMsg) {
        response.setSuccess(false);
        returnMessage.add(errorMsg);
        response.setReturnMessage(returnMessage);
        return response;
    }

    @Override
    public ExternalOrderResponseData cancelExternalOrder(final ExternalOrderDetailRequestData request) throws Exception {
        final ExternalOrderResponseData response = new ExternalOrderResponseData();
        final List<String> returnMessage = new ArrayList<String>();
        if (StringUtils.isEmpty(request.getCustomerSAPId()) || StringUtils.isEmpty(request.getOrderNumber())) {
            orderResponseError(response, returnMessage, "Invalid customersapid or ordernumber");
            return response;

        }
        final B2BUnitModel unitModel = getUnitFromSapId(request.getCustomerSAPId());
        if (Objects.nonNull(unitModel)) {

            ExternalOrderDetailRequestData orderDetailRequest = new ExternalOrderDetailRequestData();
            orderDetailRequest.setCustomerSAPId(request.getCustomerSAPId());
            orderDetailRequest.setOrderNumber(request.getOrderNumber());
            ExternalOrderDetailsResponseWSDTO orderDetailResponse = getExternalOrderDetail(orderDetailRequest);
            if (Objects.nonNull(orderDetailResponse.getOrderHeaders())) {
                final String orderStatus = orderDetailResponse.getOrderHeaders().getStatus();
                if (orderStatus != null && (orderStatus.equalsIgnoreCase(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_CANCELLED) ||
                        orderStatus.equalsIgnoreCase(WholescriptscoreConstants.ORDER_STATUS_PROCESSING))) {
                    wholescriptsOrdersFacade.cancelOrderInSAP(request.getOrderNumber(), StringUtils.EMPTY);
                    response.setSuccess(true);
                } else {
                    orderResponseError(response, returnMessage, "Can not cancel Order as order " + orderStatus);
                }

            } else {
                orderResponseError(response, returnMessage, "Invalid order number");
            }


        } else {
            orderResponseError(response, returnMessage, "Invalid customersapid");
        }

        return response;

    }

    @Override
    public ExternalOrderDetailsResponseWSDTO getExternalOrderDetail(final ExternalOrderDetailRequestData request) throws Exception {
        ExternalOrderDetailsResponseWSDTO responseWsDTO = new ExternalOrderDetailsResponseWSDTO();
        ExternalOrderDetailsResponseData sapResponse = new ExternalOrderDetailsResponseData();
        final List<String> returnMessage = new ArrayList<String>();
        final B2BUnitModel unitModel = getUnitFromSapId(request.getCustomerSAPId());
        if (Objects.nonNull(unitModel)) {
            final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(unitModel);
            final B2BCustomerModel customer = customers.iterator().next();
            final String baseStoreId = configurationService.getConfiguration().getString("wholescripts.basesite.id");
            final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(baseStoreId);
            sapResponse = wholescriptsIntegrationsService.getExternalOrderDetails(customer, request.getOrderNumber(), baseStore);
            responseWsDTO.setSuccess(true);
            return prepareOrderDetailsResponse(responseWsDTO, sapResponse);
        } else {
            responseWsDTO.setSuccess(false);
            returnMessage.add("Invalid CustomerSAPId");
            responseWsDTO.setReturnMessage(returnMessage);
        }
        return responseWsDTO;

    }

    private B2BUnitModel getUnitFromSapId(String customerSAPId) {

        B2BUnitModel unitModel = null;
        int accountNuberWithoutZero = Integer.valueOf(customerSAPId);
        String formattedAccountNumber = String.valueOf(accountNuberWithoutZero);
        String accountNumber = formattedAccountNumber.concat(WholescriptscoreConstants.SALES_ORG_1710);
        unitModel = b2bUnitService.getUnitForUid(accountNumber);
        if (unitModel == null) {
            formattedAccountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customerSAPId);
            accountNumber = formattedAccountNumber.concat(WholescriptscoreConstants.SALES_ORG_1710);
            unitModel = b2bUnitService.getUnitForUid(accountNumber);
        }
        return unitModel;

    }

    private B2BCustomerModel createPatient(final ExternalAPIEMRRecommendationWsDTO inputWsDTO, final String defaultWSUnit) {

        final B2BUnitModel defaultB2BUnit = b2bUnitService.getUnitForUid(defaultWSUnit);
        final B2BCustomerModel b2bCustomer = modelService.create(B2BCustomerModel.class);
        b2bCustomer.setUid(inputWsDTO.getPatientEmail());
        b2bCustomer.setName(inputWsDTO.getPatientFirstName() + StringUtils.SPACE + inputWsDTO.getPatientLastName());
        b2bCustomer.setDefaultB2BUnit(defaultB2BUnit);
        b2bCustomer.setEmail(inputWsDTO.getPatientEmail());
        b2bCustomer.setCustomerProfile(CustomerProfileType.PATIENT);
        b2bCustomer.setActive(false);
        b2bCustomer.setLoginDisabled(true);
        modelService.save(b2bCustomer);
        return b2bCustomer;
    }

    public WholescriptsExternalAPICustomerData getCustomerData(final String customerId, final String email) {

        WholescriptsExternalAPICustomerData customerData = new WholescriptsExternalAPICustomerData();
        if (StringUtils.isNotEmpty(customerId)) {
            String salesAreaAcccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(customerId.replaceFirst("^0+", ""));
            final B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(salesAreaAcccountNo);

            if (Objects.nonNull(b2bUnit) && WholescriptsCoreUtils.isValidSAPCustomerGroup(b2bUnit)) {
            	customerData = wholescriptsExternalAPICustomerConverter.convert(b2bUnit);
            } else {
                throw new RuntimeException("Invalid sap customer id");
            }
        } else if (StringUtils.isNotEmpty(email)) {
            final B2BCustomerModel customer = (B2BCustomerModel) userService.getUserForUID(email);
            final String defaultWSUnit = configurationService.getConfiguration().getString(DEFAULT_B2BUNIT);

            if (customer.getDefaultB2BUnit() == null || customer.getDefaultB2BUnit().getUid().equalsIgnoreCase(defaultWSUnit)) {
                throw new RuntimeException("Invalid customer email");
            }
            if (customer.getDefaultB2BUnit() != null) {
                customerData = wholescriptsExternalAPICustomerConverter.convert(customer.getDefaultB2BUnit());
            }
        }
        return customerData;
    }

    public WholescriptsExternalAPIAddressListData getAddressData(final String customerId) {

        String salesAreaAcccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(customerId.replaceFirst("^0+", ""));
        final B2BUnitModel b2bUnitModel = b2bUnitService.getUnitForUid(salesAreaAcccountNo);
        WholescriptsExternalAPIAddressListData wsExternalAPIAddressListData = new WholescriptsExternalAPIAddressListData();
        if (null != b2bUnitModel && WholescriptsCoreUtils.isValidSAPCustomerGroup(b2bUnitModel)) {
            List<B2BUnitModel> shippingAddressB2BUnits = wholescriptsExternalAPIService.getShippingAddressB2BUnits(b2bUnitModel);
            List<WholescriptsExternalAPIAddressData> wsExternalAPIAddresses = new ArrayList<WholescriptsExternalAPIAddressData>();
            for (B2BUnitModel shippingAddressB2BUnit : shippingAddressB2BUnits) {
                WholescriptsExternalAPIAddressData wsExternalAPIAddressesData = wholescriptsExternalAPIShippingAddressB2BUnitConverter.convert(shippingAddressB2BUnit);
                wsExternalAPIAddressesData.setCustomerSAPId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bUnitModel.getUid()));
                wsExternalAPIAddresses.add(wsExternalAPIAddressesData);
            }
            if (CollectionUtils.isNotEmpty(wsExternalAPIAddresses)) {
                wsExternalAPIAddressListData.setAddressList(wsExternalAPIAddresses);
            }
        } else {
            throw new RuntimeException("Invalid sap customer id");
        }
        return wsExternalAPIAddressListData;
    }

    public WholescriptsExternalAPIPatientListData getPatientsData(final String customerId) {
        
   	  final List<B2BUnitModel> b2BUnitModels = wholescriptsExternalAPIService.getPatients(customerId);
   	  final List<WholescriptsExternalAPIPatientData> wholescriptsExternalAPIPatientDataList = new ArrayList<WholescriptsExternalAPIPatientData>();
   	  final WholescriptsExternalAPIPatientListData wholescriptsExternalAPIPatientListData = new WholescriptsExternalAPIPatientListData();
   	  for(B2BUnitModel b2BUnitModel : b2BUnitModels) {
   		  final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(b2BUnitModel);
   		  if (CollectionUtils.isNotEmpty(customers)) {
   			  WholescriptsExternalAPIPatientData wholescriptsExternalAPIPatientData = wholescriptsExternalAPIPatientsConverter.convert(b2BUnitModel);
   			  wholescriptsExternalAPIPatientDataList.add(wholescriptsExternalAPIPatientData);
   		  }
   	  }
   	  
   	  if (CollectionUtils.isNotEmpty(wholescriptsExternalAPIPatientDataList)) {
   		  wholescriptsExternalAPIPatientListData.setPatientList(wholescriptsExternalAPIPatientDataList);
   	  } else {
           throw new RuntimeException("Patient details not available");
       }
   	  
   	  return wholescriptsExternalAPIPatientListData;
    }

    private ExternalOrderDetailsResponseWSDTO prepareOrderDetailsResponse(final ExternalOrderDetailsResponseWSDTO responseWsDTO, final ExternalOrderDetailsResponseData sapResponse) {

        String orderShipTo = null;
        final List<String> returnMessage = new ArrayList<String>();

        if (Objects.nonNull(sapResponse.getOrderHeaders())) {

            final ExternalOrderHeaderResponseData orderHeader = sapResponse.getOrderHeaders().getItems();
            orderShipTo = orderHeader.getAddressId();

            if (Objects.nonNull(sapResponse.getOrderText())
                    && Objects.nonNull(sapResponse.getOrderText().getItems())
                    && StringUtils.isNotEmpty(sapResponse.getOrderText().getItems().getText())
                    && orderHeader.getOrderNumber().equalsIgnoreCase(sapResponse.getOrderText().getItems().getOrderNumber())) {
                orderHeader.setInstructions(sapResponse.getOrderText().getItems().getText().trim());
            }
            responseWsDTO.setOrderHeaders(orderHeader);


            if (Objects.nonNull(sapResponse.getOrderItems())) {
                responseWsDTO.setOrderItems(sapResponse.getOrderItems().getItems());
            }

            final String orderHeaderShipTo = orderShipTo;

            if (Objects.nonNull(sapResponse.getAddresses())) {
                responseWsDTO.setAddresses(sapResponse.getAddresses().getItems());
                final List<ExternalOrderAddressResponseData> addressItems = sapResponse.getAddresses().getItems();
                final List<ExternalOrderAddressResponseData> filteredAddresses = addressItems.stream().filter(addressItem -> orderHeaderShipTo != null
                                && orderHeaderShipTo.equalsIgnoreCase(addressItem.getAddressId()))
                        .collect(Collectors.toList());
                responseWsDTO.setAddresses(filteredAddresses);
            }

            if (Objects.nonNull(sapResponse.getCreditCards())) {
                responseWsDTO.setCreditCards(sapResponse.getCreditCards().getItems());
            }

            if (Objects.nonNull(sapResponse.getDeliveryHeaders())) {
                responseWsDTO.setDeliveryHeaders(sapResponse.getDeliveryHeaders().getItems());
            }

            if (Objects.nonNull(sapResponse.getDeliveryItems())) {
                responseWsDTO.setDeliveryItems(sapResponse.getDeliveryItems().getItems());
            }

            setOrderStatus(sapResponse);

        }

        return responseWsDTO;

    }

    private CheckoutPaymentType findCheckoutPaymentType(final String paymentTerms) {
        CheckoutPaymentType paymentType = null;
        if (paymentTerms != null) {
            try {
                paymentType = enumerationService.getEnumerationValue(CheckoutPaymentType.class, paymentTerms);
            } catch (Exception ex) {
                LOG.info("Payment type doesnt exist for payment terms " + paymentTerms + " , creating payment type");
                if (paymentTerms.startsWith("NT")) {
                    final EnumerationValueModel model = (EnumerationValueModel) modelService.create(CheckoutPaymentType._TYPECODE);
                    model.setCode(paymentTerms);
                    model.setName(paymentTerms);
                    modelService.save(model);
                    paymentType = enumerationService.getEnumerationValue(CheckoutPaymentType.class, paymentTerms);
                }
            }
        }
        return paymentType;
    }

    private void setOrderStatus(final ExternalOrderDetailsResponseData sapResponse) {

   	  final ExternalOrderHeaderResponseData orderHeader = sapResponse.getOrderHeaders().getItems();
   	  
   	  final ExternalOrderDeliveryHeadersResponseData orderDeliveryHeader = sapResponse.getDeliveryHeaders();
   	  
        Double totalCreditedAmount = 0.0;

        if (Objects.nonNull(sapResponse.getOrderHdrBil())) {
            final List<ExternalOrderHDRBillingResponseData> orderHdrBillItems = sapResponse.getOrderHdrBil().getItems();
            if (CollectionUtils.isNotEmpty(orderHdrBillItems)) {
                for (ExternalOrderHDRBillingResponseData orderHdrBillItem : orderHdrBillItems) {
                    if (WholescriptscoreConstants.ORDER_HEADER_BIL_DOC_O.equalsIgnoreCase(orderHdrBillItem.getDoctype())) {
                        final String hdrBillItemTotal = orderHdrBillItem.getTotal();
                        if (StringUtils.isNotBlank(hdrBillItemTotal)) {
                            totalCreditedAmount = totalCreditedAmount + Double.valueOf(hdrBillItemTotal);
                        }
                    }
                }

            }

        }
        
		  boolean partialshipped = false;
		  boolean shipped = false;

		  if (Objects.nonNull(sapResponse.getOrderItems()) 
				  && Objects.nonNull(sapResponse.getOrderItems().getItems())) {

			  List<ExternalOrderItemReponseData> orderItems = sapResponse.getOrderItems().getItems();

			  boolean partialOrderShipped = orderItems.stream().anyMatch(
					  orderItem -> !WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_C.equalsIgnoreCase(orderItem.getDeliveryStatus()));
			  
			  boolean completeOrderShipped = orderItems.stream().allMatch(
					  orderItem -> WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_C.equalsIgnoreCase(orderItem.getDeliveryStatus()));

			  if (partialOrderShipped) {
				  partialshipped = true;
			  }
			  
			  if (completeOrderShipped) {
				  shipped = true;
			  }

		  }

        if (Objects.nonNull(orderHeader)) {
            if (StringUtils.isNotBlank(orderHeader.getTotal()) && totalCreditedAmount > 0.0) {
                final Double headerTotal = Double.valueOf(orderHeader.getTotal());
                if (Double.compare(headerTotal, totalCreditedAmount) == 0) {
                    orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_REFUNDED);
                } else {
                    orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_REFUNDED);
                }

            } else if (WholescriptscoreConstants.ORDER_HEADER_BILL_STATUS_C.equalsIgnoreCase(orderHeader.getBilStatus())) {
                orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_COMPLETE);
            } else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_B.equalsIgnoreCase(orderHeader.getCancelled())) {
                orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_CANCELLED);
            } else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_C.equalsIgnoreCase(orderHeader.getCancelled())
                    || WholescriptscoreConstants.ORDER_HEADER_CANCELLED_X.equalsIgnoreCase(orderHeader.getCancelled())) {
                orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_CANCELLED);
            } else if (shipped && orderDeliveryHeader != null) {
            	orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_SHIPPED);
            } else if (partialshipped && orderDeliveryHeader != null) {
            	orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_SHIPPED);
            } else {
                orderHeader.setStatus(WholescriptscoreConstants.ORDER_STATUS_PROCESSING);
            }

        }

    }
    
    private CreditCardPaymentInfoModel ccPaymentInfo (final CartModel cart, final String commerceCardTokenDetail) throws Exception {
   	 
   	 CreditCardPaymentInfoModel ccPaymentInfo = null;
    	
    	 final B2BCustomerModel b2bCustomer = (B2BCustomerModel) cart.getUser();
    	 WholescriptsGetCCResponseData wholescriptsGetCCResponseData = wholescriptsIntegrationsService.getCreditCardInfoFromSap(b2bCustomer);
    	
       final WholescriptsGetCCCartItemListResponseData listResponseData = wholescriptsGetCCResponseData.getCartData();
       if (Objects.nonNull(listResponseData)) {
          for (final WholescriptsGetCCCartItemResponseData itemData : listResponseData.getItem()) {
          	
         	 final String sapCardNumberDetail = itemData.getCardNumber();
          	 final String[] cCardNumber = sapCardNumberDetail.split("-");
             final String[] cardValidity = itemData.getValidTo().split("-");
             final String sapCardType = itemData.getCardType();
             final  String sapValidMonth = cardValidity[1];
             final  String sapValidYear = cardValidity[0];
             
             if (sapCardNumberDetail != null && sapCardNumberDetail.equalsIgnoreCase(commerceCardTokenDetail)) {
            	 ccPaymentInfo = modelService.create(CreditCardPaymentInfoModel.class);
            	 ccPaymentInfo.setValidToMonth(sapValidMonth);
            	 ccPaymentInfo.setValidToYear(sapValidYear);
            	 ccPaymentInfo.setCcOwner(itemData.getCreditCardName());
            	 ccPaymentInfo.setUser(cart.getUser());
            	 ccPaymentInfo.setCode(b2bCustomer.getUid() + "_" + UUID.randomUUID());
            	 ccPaymentInfo.setNumber(sapCardNumberDetail);
            	 
            	 final CardTypeEnum cardTypeEnum = CardTypeEnum.get(WholescriptscoreConstants.CYBERSOURCE_SOP_CARD_TYPES.get(sapCardType));

                if (cardTypeEnum != null) {
               	 ccPaymentInfo.setType(CreditCardType.valueOf(cardTypeEnum.name().toUpperCase()));
                }
                cart.setPaymentInfo(ccPaymentInfo);
                
                break;
             }
             
          }
       }
   	 return ccPaymentInfo;
    }


}
