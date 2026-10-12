package com.wholescripts.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.service.KlaviyoService;
import com.wholescripts.klaviyo.*;
import com.wholescripts.klaviyo.Properties;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class KlaviyoServiceImpl implements KlaviyoService {

    private static final Logger LOG = LogManager.getLogger(KlaviyoServiceImpl.class);
    private static String KLAVIYO_URL = "wholescripts.Klaviyo.api.url";
    private static String KLAVIYO_PRIVATE_KEY = "wholescripts.Klaviyo.api.privatekey";
    private static String AUTHORIZATION = "Authorization";
    private static String REVISION = "revision";
    private static String REVISION_CODE= "wholescripts.Klaviyo.api.revision";
    private static String KLAVIYO_API_KEY = "Klaviyo-API-Key ";
    private static String EVENT_TYPE = "event";
    private static String METRIC_TYPE = "metric";
    private static String PROFILE_TYPE = "profile";
    private static String METRIC_ATTRIBUTE_NAME = "Placed Order";
    private static String ORDERED_PRODUCT_METRIC_ATTRIBUTE_NAME = "Ordered Product";
    private static final String PRODUCT_NAME = "wholescripts.pdp.productname.classification.code";
    private static final String MEDIA_FORMAT="515x515";
    private static final String MEDIA_URL="media.wholescripts.https";
    private static final String MATERIAL_PRODUCT_GROUP = "wholescripts.material.product.group.classification.code";
    private static final String MEDPAX = "MedPax: ";

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name="productModelUrlResolver")
    private UrlResolver<ProductModel> productModelUrlResolver;

    @Resource(name="siteBaseUrlResolutionService")
    private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

    @Resource(name="classificationService")
    private ClassificationService classificationService;

    @Resource(name = "wholescriptsMedPaxDao")
    private WholescriptsMedPaxDao wholescriptsMedPaxDao;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<String> sendOrderDetails(OrderModel order) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.set(AUTHORIZATION, KLAVIYO_API_KEY+configurationService.getConfiguration().getString(KLAVIYO_PRIVATE_KEY));
            headers.set(REVISION,configurationService.getConfiguration().getString(REVISION_CODE));

            OrderEventDTO requestBody = prepareOrderEventRequest(order);
            HttpEntity<OrderEventDTO> requestEntity = new HttpEntity<>(requestBody, headers);
            String url = configurationService.getConfiguration().getString(KLAVIYO_URL);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            LOG.info("Klaviyo Request Body: {}", requestBodyJson);
            RestTemplate restTemplate =new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            LOG.info("Klaviyo Response Body: {}", response.getBody());

            return response;
        } catch (HttpStatusCodeException e) {
            LOG.error("HTTP error while sending order details: StatusCode: {}, Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (IllegalArgumentException e) {
                    LOG.error("Phone number validation failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid phone number: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unexpected error while sending order details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Override
    public ResponseEntity<String> sendOrderedProductDetails(OrderModel order, OrderEntryModel entry) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.set(AUTHORIZATION, KLAVIYO_API_KEY+configurationService.getConfiguration().getString(KLAVIYO_PRIVATE_KEY));
            headers.set(REVISION,configurationService.getConfiguration().getString(REVISION_CODE));

            OrderedProductEventDTO requestBody = prepareOrderedProductEventRequest(order,entry);
            HttpEntity<OrderedProductEventDTO> requestEntity = new HttpEntity<>(requestBody, headers);
            String url = configurationService.getConfiguration().getString(KLAVIYO_URL);

            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
                LOG.info("Klaviyo Ordered Product Request Body: {}", requestBodyJson);
            RestTemplate restTemplate =new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            LOG.info("Klaviyo Ordered Product Response Body: {}", response.getBody());

            return response;

        } catch (HttpStatusCodeException e) {
            LOG.error("HTTP error while sending order details: StatusCode: {}, Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (IllegalArgumentException e) {
            LOG.error("Phone number validation failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid phone number: " + e.getMessage());
        } catch (Exception e) {
            LOG.error("Unexpected error while sending order details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    private OrderEventDTO prepareOrderEventRequest(OrderModel order){
        OrderEventDTO orderEventDTO = new OrderEventDTO();
        Data data = new Data();
        data.setType(EVENT_TYPE);
        data.setAttributes(prepareAttributes(order));
        orderEventDTO.setData(data);
        return orderEventDTO;
    }

    private OrderedProductEventDTO prepareOrderedProductEventRequest(OrderModel order, OrderEntryModel entry){
        OrderedProductEventDTO orderedProductEventDTO = new OrderedProductEventDTO();
        OrderedProductData data = new OrderedProductData();
        data.setType(EVENT_TYPE);
        data.setAttributes(prepareOrderedProductAttributes(order, entry));
        orderedProductEventDTO.setData(data);
        return orderedProductEventDTO;
    }

    private OrderedProductAttributes prepareOrderedProductAttributes(OrderModel order, OrderEntryModel entry){
        OrderedProductAttributes attributes = new OrderedProductAttributes();
        attributes.setProperties(prepareOrderedProductProperties(order, entry));
        attributes.setMetric(prepareOrderedProductMetric());
        attributes.setProfile(prepareProfile(order));
        attributes.setTime(getTime());
        attributes.setValue(getOrderedProductTotalPrice(order,entry));
        attributes.setValue_currency(order.getCurrency().getIsocode());
        attributes.setUnique_id(generateID());
        return attributes;
    }

    private double getOrderedProductTotalPrice(OrderModel order, OrderEntryModel entry){
        if(StringUtils.isEmpty(entry.getTemplateId()) || !entry.getTemplateId().startsWith("medpax")) {
            return entry.getBasePrice();
        }else{
            String medPaxCode = entry.getTemplateId().split("-")[1];
            return BigDecimal.valueOf(
                            order.getEntries().stream()
                                    .filter(e -> e.getTemplateId() != null && e.getTemplateId().contains(medPaxCode))
                                    .mapToDouble(AbstractOrderEntryModel::getBasePrice)
                                    .sum())
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
    }

    private OrderedProductProperties prepareOrderedProductProperties(OrderModel order, OrderEntryModel entry){
        ProductModel product = entry.getProduct();
        OrderedProductProperties properties = new OrderedProductProperties();
        properties.setOrderId(order.getCode());
        properties.setProductID(product.getCode());
        properties.setSKU(product.getCode());
        if(StringUtils.isEmpty(entry.getTemplateId()) || !entry.getTemplateId().startsWith("medpax")) {
            properties.setProductName(getProductName(product));
            properties.setQuantity(entry.getQuantityByCase() != null ? entry.getQuantityByCase() : 0);
        }else{
            String medPaxCode = entry.getTemplateId().split("-")[1];
            MedPaxModel medPaxModel =	wholescriptsMedPaxDao.getMedPaxByCode(medPaxCode);
            if(medPaxModel != null) {
                properties.setProductName(MEDPAX+medPaxModel.getName());
            }
            if(entry.getAbstractOrderQty() != null){
                properties.setQuantity(entry.getAbstractOrderQty().intValue());
            }
        }

        final String baseUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(order.getSite(), true,
                StringUtils.EMPTY);
        String productURL = baseUrl.replaceAll("/+$", "") + "/" + productModelUrlResolver.resolve(product).replaceAll("^/+", "");
        properties.setProductURL(productURL);
        properties.setImageURL(getImageURL(product));
        properties.setCategories(getOrderedProductCategories(entry));
        if (product.getWholescriptsBrand() != null && StringUtils.isNotEmpty(product.getWholescriptsBrand().getName())) {
            properties.setProductBrand(product.getWholescriptsBrand().getName());
        }else{
            properties.setProductBrand("");
        }
        return properties;
    }

    private List<String> getOrderedProductCategories(OrderEntryModel entry) {
        Set<String> categorySet = new HashSet<>();
        ProductModel product = entry.getProduct();
        final FeatureList featureList = classificationService.getFeatures(product);
        final Feature materialProductGroups = featureList.getFeatureByCode(configurationService.getConfiguration().getString(MATERIAL_PRODUCT_GROUP));
        if (materialProductGroups != null) {
            List<FeatureValue> categories = materialProductGroups.getValues();
            if (CollectionUtils.isNotEmpty(categories)) {
                for (FeatureValue featureValue : categories) {
                    String value = ((ClassificationAttributeValueModel) featureValue.getValue())
                            .getName();
                    categorySet.add(value);
                }
            }
        }
        return new ArrayList<>(categorySet);
    }

    private Attributes prepareAttributes(OrderModel order){
        Attributes attributes = new Attributes();
        attributes.setProperties(prepareProperties(order));
        attributes.setMetric(prepareMetric());
        attributes.setProfile(prepareProfile(order));
        attributes.setTime(getTime());
        attributes.setValue(order.getTotalPrice());
        attributes.setValue_currency(order.getCurrency().getIsocode());
        attributes.setUnique_id(generateID());
        return attributes;
    }

    private Profile prepareProfile(OrderModel order){
        B2BCustomerModel customer = (order.getUser() instanceof B2BCustomerModel) ? (B2BCustomerModel) order.getUser() : null;
        if (customer == null) {
            return null;
        }
        B2BUnitModel b2bUnit = customer.getDefaultB2BUnit();
        AddressModel address = null;
        if (b2bUnit != null) {
            Collection<AddressModel> billingAddresses = b2bUnit.getBillingAddresses();
            if (billingAddresses != null && !billingAddresses.isEmpty()) {
                address = billingAddresses.iterator().next();
            }else{
                address = b2bUnit.getAddresses().stream()
                        .filter(AddressModel::getBillingAddress)
                        .findFirst()
                        .orElse(null);
            }
        }
        Profile profile = new Profile();
        DataProfile dataProfile = new DataProfile();
        dataProfile.setType(PROFILE_TYPE);
        AttributesProfile attributesProfile = new AttributesProfile();
        attributesProfile.setEmail(customer.getEmail());
        String parsedPhoneNumber =convertToE164(address.getPhone1(),address.getCountry().getIsocode());
        if(StringUtils.isNotEmpty(parsedPhoneNumber)) {
      	  attributesProfile.setPhone_number(parsedPhoneNumber);
        }
        dataProfile.setAttributes(attributesProfile);
        profile.setData(dataProfile);
        return profile;
    }
    private Metric prepareMetric(){
        Metric metric = new Metric();
        DataMetric dataMetric = new DataMetric();
        dataMetric.setType(METRIC_TYPE);
        AttributesMetric attributesMetric = new AttributesMetric();
        attributesMetric.setName(METRIC_ATTRIBUTE_NAME);
        dataMetric.setAttributes(attributesMetric);
        metric.setData(dataMetric);
        return metric;
    }

    private Metric prepareOrderedProductMetric(){
        Metric metric = new Metric();
        DataMetric dataMetric = new DataMetric();
        dataMetric.setType(METRIC_TYPE);
        AttributesMetric attributesMetric = new AttributesMetric();
        attributesMetric.setName(ORDERED_PRODUCT_METRIC_ATTRIBUTE_NAME);
        dataMetric.setAttributes(attributesMetric);
        metric.setData(dataMetric);
        return metric;
    }

    private Properties prepareProperties(OrderModel order){
        Properties properties = new Properties();
        properties.setOrderId(order.getCode());
        properties.setCategories(getAllCategories(order));
        properties.setItemNames(getProductNames(order));
        if(CollectionUtils.isNotEmpty(order.getAppliedCouponCodes())) {
            properties.setDiscountCode(order.getAppliedCouponCodes().iterator().next());
        }else {
            properties.setDiscountCode("");
        }
        properties.setDiscountValue(order.getTotalDiscounts());
        properties.setBrands(getBrands(order));
        properties.setItems(prepareItems(order));
        if(order.getDeliveryAddress() !=null){
            properties.setShippingAddress(prepareAddress(order.getDeliveryAddress()));
        }
        B2BCustomerModel customer = (order.getUser() instanceof B2BCustomerModel) ? (B2BCustomerModel) order.getUser() : null;
        B2BUnitModel b2bUnit = (customer != null) ? customer.getDefaultB2BUnit() : null;
        AddressModel billingAddress = null;
        if (b2bUnit != null) {
            Collection<AddressModel> billingAddresses = b2bUnit.getBillingAddresses();
            if (billingAddresses != null && !billingAddresses.isEmpty()) {
                billingAddress = billingAddresses.iterator().next();
            }else{
                billingAddress = b2bUnit.getAddresses().stream()
                        .filter(AddressModel::getBillingAddress)
                        .findFirst()
                        .orElse(null);
            }
        }
        properties.setBillingAddress(prepareAddress(billingAddress));
        return properties;
    }
    private String getTime(){
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX");
        return now.format(formatter);
    }

    private String generateID(){
        BigInteger number = new BigInteger(28 * 5, new Random());
        return String.format("%028d", number);
    }

    private List<String> getAllCategories(OrderModel order) {
        Set<String> categorySet = new HashSet<>();
        order.getEntries().forEach(entry -> {
            ProductModel product = entry.getProduct();
            final FeatureList featureList = classificationService.getFeatures(product);
            final Feature materialProductGroups = featureList.getFeatureByCode(configurationService.getConfiguration().getString(MATERIAL_PRODUCT_GROUP));
            if (materialProductGroups != null) {
                List<FeatureValue> categories = materialProductGroups.getValues();
                if (CollectionUtils.isNotEmpty(categories)) {
                    for (FeatureValue featureValue : categories) {
                        String value = ((ClassificationAttributeValueModel) featureValue.getValue())
                                .getName();
                        categorySet.add(value);
                    }
                }
            }
        });
        return new ArrayList<>(categorySet);
    }

    private List<String> getProductNames(OrderModel order){
        List<String> productNames = new ArrayList<>();
        order.getEntries().forEach(entry -> {
            if (CollectionUtils.isEmpty(entry.getEntryGroupNumbers())) {
                if(StringUtils.isEmpty(entry.getTemplateId()) || !entry.getTemplateId().startsWith("medpax")) {
                    ProductModel product = entry.getProduct();
                    productNames.add(getProductName(product));
                }else{
                    String medPaxCode = entry.getTemplateId().split("-")[1];
                    MedPaxModel medPaxModel =	wholescriptsMedPaxDao.getMedPaxByCode(medPaxCode);
                    if(medPaxModel != null) {
                        productNames.add(MEDPAX+medPaxModel.getName());
                    }
                }
            }
        });
        return productNames;
    }

    private List<String> getBrands(OrderModel order){
        Set<String> brands = new HashSet<>();
        order.getEntries().forEach(entry -> {
            ProductModel product = entry.getProduct();
            if (product != null && product.getName() != null) {
                WholescriptsBrandModel brandModel = product.getWholescriptsBrand();
                if(brandModel != null){
                    brands.add(brandModel.getName());
                }

            }
        });
        return new ArrayList<>(brands);
    }

    private List<Item> prepareItems(OrderModel order){
        List<Item> items = new ArrayList<>();
        order.getEntries().forEach(entry -> {
            if (CollectionUtils.isEmpty(entry.getEntryGroupNumbers())) {
                if(StringUtils.isEmpty(entry.getTemplateId()) || !entry.getTemplateId().startsWith("medpax")) {
                    ProductModel product = entry.getProduct();
                    if (product != null && product.getName() != null) {
                        Item item = new Item();
                        item.setProductID(product.getCode());
                        item.setSKU(product.getCode());
                        item.setProductName(getProductName(product));
                        item.setQuantity(entry.getQuantityByCase() != null ? entry.getQuantityByCase() : 0);

                        BigDecimal totalBasePrice = BigDecimal.valueOf(entry.getBasePrice());
                        int quantity = entry.getQuantityByCase() != null ? entry.getQuantityByCase() : 0;
                        double unitPrice = 0.0;
                        if (quantity > 0) {
                            unitPrice = totalBasePrice
                                    .divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP)
                                    .doubleValue();
                        }
                        item.setItemPrice(unitPrice);

                        item.setRowTotal(entry.getBasePrice());
                        item.setImageURL(getImageURL(product));

                        final String baseUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(order.getSite(), true,
                                StringUtils.EMPTY);
                        String productURL = baseUrl.replaceAll("/+$", "") + "/" + productModelUrlResolver.resolve(product).replaceAll("^/+", "");
                        item.setProductURL(productURL);

                        if (product.getWholescriptsBrand() != null && StringUtils.isNotEmpty(product.getWholescriptsBrand().getName())) {
                            item.setBrand(product.getWholescriptsBrand().getName());
                        }else{
                            item.setBrand("");
                        }

                        final FeatureList featureList = classificationService.getFeatures(product);
                        final Feature materialProductGroups = featureList.getFeatureByCode(configurationService.getConfiguration().getString(MATERIAL_PRODUCT_GROUP));
                        if (materialProductGroups != null) {
                            List<FeatureValue> categories = materialProductGroups.getValues();
                            Set<String> listOfCategories = new HashSet<>();
                            if (CollectionUtils.isNotEmpty(categories)) {
                                for (FeatureValue featureValue : categories) {
                                    String value = ((ClassificationAttributeValueModel) featureValue.getValue())
                                            .getName();
                                    listOfCategories.add(value);
                                }
                            }
                            item.setCategories(new ArrayList<>(listOfCategories));
                        }

                        items.add(item);
                    }
                }else{
                    items.add(prepareMedPaxItem((OrderEntryModel)entry,order));
                }
            }
        });
        return items;
    }

    private Item prepareMedPaxItem(OrderEntryModel entry, OrderModel order){
        Item item = new Item();
        ProductModel product = entry.getProduct();
        if (product != null && product.getName() != null) {
            item.setProductID(product.getCode());
            item.setSKU(product.getCode());

            if(StringUtils.isNotEmpty(entry.getTemplateId())){
                String medPaxCode = entry.getTemplateId().split("-")[1];
                MedPaxModel medPaxModel =	wholescriptsMedPaxDao.getMedPaxByCode(medPaxCode);
                if(medPaxModel != null) {
                    item.setProductName(MEDPAX+medPaxModel.getName());
                }
                if(entry.getAbstractOrderQty() != null){
                    item.setQuantity(entry.getAbstractOrderQty().intValue());
                }

                double rowTotal = BigDecimal.valueOf(
                                order.getEntries().stream()
                                        .filter(e -> e.getTemplateId() != null && e.getTemplateId().contains(medPaxCode))
                                        .mapToDouble(AbstractOrderEntryModel::getBasePrice)
                                        .sum())
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                BigDecimal quantity = entry.getAbstractOrderQty() != null
                        ? BigDecimal.valueOf(entry.getAbstractOrderQty().intValue())
                        : BigDecimal.ONE;
                BigDecimal unitPrice = BigDecimal.valueOf(rowTotal)
                        .divide(quantity, 2, RoundingMode.HALF_UP);

                item.setItemPrice(unitPrice.doubleValue());
                item.setRowTotal(rowTotal);
            }

            item.setImageURL(getImageURL(product));

            final String baseUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(order.getSite(), true,
                    StringUtils.EMPTY);
            String productURL = baseUrl.replaceAll("/+$", "") + "/" + productModelUrlResolver.resolve(product).replaceAll("^/+", "");
            item.setProductURL(productURL);

            if (product.getWholescriptsBrand() != null && StringUtils.isNotEmpty(product.getWholescriptsBrand().getName())) {
                item.setBrand(product.getWholescriptsBrand().getName());
            }else{
                item.setBrand("");
            }

            final FeatureList featureList = classificationService.getFeatures(product);
            final Feature materialProductGroups = featureList.getFeatureByCode(configurationService.getConfiguration().getString(MATERIAL_PRODUCT_GROUP));
            if (materialProductGroups != null) {
                List<FeatureValue> categories = materialProductGroups.getValues();
                Set<String> listOfCategories = new HashSet<>();
                if (CollectionUtils.isNotEmpty(categories)) {
                    for (FeatureValue featureValue : categories) {
                        String value = ((ClassificationAttributeValueModel) featureValue.getValue())
                                .getName();
                        listOfCategories.add(value);
                    }
                }
                item.setCategories(new ArrayList<>(listOfCategories));
            }
        }
        return item;
    }


    private Address prepareAddress(AddressModel addressModel){
        Address address = new Address();
        if(addressModel !=null) {
            address.setFirstName(addressModel.getFirstname());
            address.setLastName(addressModel.getLastname());
            address.setAddress1(addressModel.getStreetname());
            address.setCity(addressModel.getTown());
            address.setRegionCode(addressModel.getRegion().getIsocode());
            address.setCountryCode(addressModel.getCountry().getIsocode());
            address.setZip(addressModel.getPostalcode());
            address.setPhone(addressModel.getPhone1());
        }
        return address;
    }

	 private String convertToE164(String phoneNumber, String regionCode) {
        if (regionCode == null || regionCode.isEmpty()) {
            return StringUtils.EMPTY;
        }
        if (phoneNumber == null || phoneNumber.isEmpty()) {
      	  return StringUtils.EMPTY;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, regionCode.toUpperCase());

            if (!phoneNumberUtil.isValidNumber(numberProto)) {
            	 return StringUtils.EMPTY;
            }

            return phoneNumberUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
        }catch (NumberParseException e){
      	  return StringUtils.EMPTY;
        }
    }

    private String getProductName(ProductModel productModel){
        String productName = "";
        final FeatureList featureList = classificationService.getFeatures(productModel);
        final Feature feature = featureList.getFeatureByCode(Config.getParameter(PRODUCT_NAME));
        if (Objects.nonNull(feature)) {
            final FeatureValue productNameValue = feature.getValue();
            if (Objects.nonNull(productNameValue)) {
                final Object attributeValue = productNameValue.getValue();
                productName = attributeValue.toString();
            }
        }
        if(productName.isEmpty()){
            productName = productModel.getName();
        }
        return productName;
    }

    private String getImageURL(ProductModel source){
        String imageURL = "";
        List<MediaContainerModel> gallaryImagesContainer = source.getGalleryImages();
        if (CollectionUtils.isNotEmpty(gallaryImagesContainer)) {
            for (MediaContainerModel mediaContainerModel : gallaryImagesContainer) {
                Collection<MediaModel> gallaryImages = mediaContainerModel.getMedias();
                if (CollectionUtils.isNotEmpty(gallaryImages)) {
                    for (MediaModel gallaryImage : gallaryImages) {
                        if(gallaryImage.getMediaFormat().getQualifier().equalsIgnoreCase(MEDIA_FORMAT)) {
                            imageURL = configurationService.getConfiguration().getString(MEDIA_URL)+gallaryImage.getURL();
                        }
                    }
                }
            }
        }
        return imageURL;
    }
}
