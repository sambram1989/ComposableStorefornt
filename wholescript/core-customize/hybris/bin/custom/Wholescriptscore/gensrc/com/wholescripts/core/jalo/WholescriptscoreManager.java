/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.BrandAgreement;
import com.wholescripts.core.jalo.BrandCustomerClassRestriction;
import com.wholescripts.core.jalo.ContentSearch;
import com.wholescripts.core.jalo.CustomerClassProductRestriction;
import com.wholescripts.core.jalo.CustomerMaterialInformation;
import com.wholescripts.core.jalo.CustomerMaterialInformationRequest;
import com.wholescripts.core.jalo.CustomerProductGroupRestriction;
import com.wholescripts.core.jalo.CustomerProductRestriction;
import com.wholescripts.core.jalo.Dosing;
import com.wholescripts.core.jalo.GetAvalaraTaxLinkCronjob;
import com.wholescripts.core.jalo.Ingredient;
import com.wholescripts.core.jalo.IngredientReference;
import com.wholescripts.core.jalo.InventoryOutboundRequest;
import com.wholescripts.core.jalo.ItemTime;
import com.wholescripts.core.jalo.MaterialRestrictionRequest;
import com.wholescripts.core.jalo.MedPax;
import com.wholescripts.core.jalo.MedpaxEntry;
import com.wholescripts.core.jalo.PatientDirectHub;
import com.wholescripts.core.jalo.Presenter;
import com.wholescripts.core.jalo.PromotionCategory;
import com.wholescripts.core.jalo.Protocol;
import com.wholescripts.core.jalo.Recommendation;
import com.wholescripts.core.jalo.ReferralCodes;
import com.wholescripts.core.jalo.RegionProductAgeRestriction;
import com.wholescripts.core.jalo.RegionProductAgeRestrictionOutboundRequest;
import com.wholescripts.core.jalo.RegionProductRestriction;
import com.wholescripts.core.jalo.RegionProductSwapRestriction;
import com.wholescripts.core.jalo.RegionProductSwapRestrictionOutboundRequest;
import com.wholescripts.core.jalo.SapUnitOfMeasure;
import com.wholescripts.core.jalo.SupportedAutoShip;
import com.wholescripts.core.jalo.W9Form;
import com.wholescripts.core.jalo.WSContactUsForm;
import com.wholescripts.core.jalo.WatchTracker;
import com.wholescripts.core.jalo.WebBannersAndScripts;
import com.wholescripts.core.jalo.WebBannersAndScriptsEntry;
import com.wholescripts.core.jalo.Webinar;
import com.wholescripts.core.jalo.WebinarCategoryTag;
import com.wholescripts.core.jalo.WholeScriptsBrightStockProducts;
import com.wholescripts.core.jalo.WholescriptsBannerComponent;
import com.wholescripts.core.jalo.WholescriptsBrand;
import com.wholescripts.core.jalo.WholescriptsCarouselComponent;
import com.wholescripts.core.jalo.WholescriptsCarouselListComponent;
import com.wholescripts.core.jalo.WholescriptsCustomerActivity;
import com.wholescripts.core.jalo.WholescriptsFaq;
import com.wholescripts.core.jalo.WholescriptsFaqList;
import com.wholescripts.core.jalo.WholescriptsFlexComponent;
import com.wholescripts.core.jalo.WholescriptsIntegrations;
import com.wholescripts.core.jalo.WholescriptsOutboundCronjob;
import com.wholescripts.core.jalo.WholescriptsOutboundRequest;
import com.wholescripts.core.jalo.WholescriptsOutboundSyncJob;
import com.wholescripts.core.jalo.WholescriptsTopic;
import com.wholescripts.core.jalo.WholescriptsUnsubscridbedEmails;
import com.wholescripts.core.jalo.WsAvailableSuppliesLast;
import com.wholescripts.core.jalo.WsDiscontinuedProducts;
import com.wholescripts.core.jalo.WsIntegrationBadgeParagraphComponent;
import com.wholescripts.core.jalo.WsIntegrationContentParagraphComponent;
import com.wholescripts.core.jalo.WsPopularCategories;
import com.wholescripts.core.jalo.WsPopularProducts;
import de.hybris.platform.b2b.jalo.B2BComment;
import de.hybris.platform.b2b.jalo.B2BCreditLimit;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.jalo.B2BMerchantCheck;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.basecommerce.constants.BasecommerceConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.commerceservices.jalo.OrgUnit;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess;
import de.hybris.platform.commerceservices.jalo.process.StoreFrontProcess;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryMode;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryModeValue;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.promotionengineservices.jalo.PromotionSourceRule;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.ruleengineservices.jalo.SourceRule;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type <code>WholescriptscoreManager</code>.
 */
@SuppressWarnings({"unused","cast"})
@SLDSafe
public class WholescriptscoreManager extends Extension
{
	/** Relation ordering override parameter constants for PromotionCategoryToProductRelation from ((Wholescriptscore))*/
	protected static String PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED = "relation.PromotionCategoryToProductRelation.source.ordered";
	protected static String PROMOTIONCATEGORYTOPRODUCTRELATION_TGT_ORDERED = "relation.PromotionCategoryToProductRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for PromotionCategoryToProductRelation from ((Wholescriptscore))*/
	protected static String PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED = "relation.PromotionCategoryToProductRelation.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n INGREDIENTREFERENCES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<IngredientReference> INGREDIENTREFERENCERELATIONINGREDIENTREFERENCESHANDLER = new OneToManyHandler<IngredientReference>(
	WholescriptscoreConstants.TC.INGREDIENTREFERENCE,
	true,
	"product",
	"productPOS",
	true,
	true,
	CollectionType.COLLECTION
	);
	/** Relation ordering override parameter constants for Webinar2Category from ((Wholescriptscore))*/
	protected static String WEBINAR2CATEGORY_SRC_ORDERED = "relation.Webinar2Category.source.ordered";
	protected static String WEBINAR2CATEGORY_TGT_ORDERED = "relation.Webinar2Category.target.ordered";
	/** Relation disable markmodifed parameter constants for Webinar2Category from ((Wholescriptscore))*/
	protected static String WEBINAR2CATEGORY_MARKMODIFIED = "relation.Webinar2Category.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n REFERRALCODES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ReferralCodes> B2BUNIT2REFERRALCODESREFERRALCODESHANDLER = new OneToManyHandler<ReferralCodes>(
	WholescriptscoreConstants.TC.REFERRALCODES,
	false,
	"b2bUnit",
	null,
	false,
	true,
	CollectionType.SET
	);
	/** Relation ordering override parameter constants for Protocol2Customer from ((Wholescriptscore))*/
	protected static String PROTOCOL2CUSTOMER_SRC_ORDERED = "relation.Protocol2Customer.source.ordered";
	protected static String PROTOCOL2CUSTOMER_TGT_ORDERED = "relation.Protocol2Customer.target.ordered";
	/** Relation disable markmodifed parameter constants for Protocol2Customer from ((Wholescriptscore))*/
	protected static String PROTOCOL2CUSTOMER_MARKMODIFIED = "relation.Protocol2Customer.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n RECOMMENDATION's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Recommendation> CUSTOMER2RECOMMENDATIONRECOMMENDATIONHANDLER = new OneToManyHandler<Recommendation>(
	WholescriptscoreConstants.TC.RECOMMENDATION,
	false,
	"patient",
	null,
	false,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n WHOLESCRIPTSINTEGRATIONSRECORDS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<WholescriptsIntegrations> CUSTOMER2WHOLESCRIPTSINTEGRATIONWHOLESCRIPTSINTEGRATIONSRECORDSHANDLER = new OneToManyHandler<WholescriptsIntegrations>(
	WholescriptscoreConstants.TC.WHOLESCRIPTSINTEGRATIONS,
	false,
	"customer",
	null,
	false,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n PRACTITIONERDETAILS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<PointOfService> CUSTOMER2POINTOFSERVICEPRACTITIONERDETAILSHANDLER = new OneToManyHandler<PointOfService>(
	BasecommerceConstants.TC.POINTOFSERVICE,
	false,
	"practitioner",
	null,
	false,
	true,
	CollectionType.SET
	);
	/** Relation ordering override parameter constants for PromotionToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED = "relation.PromotionToPrincipalGroupRelation.source.ordered";
	protected static String PROMOTIONTOPRINCIPALGROUPRELATION_TGT_ORDERED = "relation.PromotionToPrincipalGroupRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for PromotionToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String PROMOTIONTOPRINCIPALGROUPRELATION_MARKMODIFIED = "relation.PromotionToPrincipalGroupRelation.markmodified";
	/** Relation ordering override parameter constants for FaqListToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED = "relation.FaqListToPrincipalGroupRelation.source.ordered";
	protected static String FAQLISTTOPRINCIPALGROUPRELATION_TGT_ORDERED = "relation.FaqListToPrincipalGroupRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for FaqListToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED = "relation.FaqListToPrincipalGroupRelation.markmodified";
	/** Relation ordering override parameter constants for FaqToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED = "relation.FaqToPrincipalGroupRelation.source.ordered";
	protected static String FAQTOPRINCIPALGROUPRELATION_TGT_ORDERED = "relation.FaqToPrincipalGroupRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for FaqToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED = "relation.FaqToPrincipalGroupRelation.markmodified";
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("wholescriptsBrand", AttributeMode.INITIAL);
		tmp.put("discontinuedDate", AttributeMode.INITIAL);
		tmp.put("sapMaterialGroup", AttributeMode.INITIAL);
		tmp.put("sapBasicMaterial", AttributeMode.INITIAL);
		tmp.put("isMedpax", AttributeMode.INITIAL);
		tmp.put("sapSalesUnits", AttributeMode.INITIAL);
		tmp.put("isFeatured", AttributeMode.INITIAL);
		tmp.put("isNew", AttributeMode.INITIAL);
		tmp.put("weight", AttributeMode.INITIAL);
		tmp.put("width", AttributeMode.INITIAL);
		tmp.put("length", AttributeMode.INITIAL);
		tmp.put("height", AttributeMode.INITIAL);
		tmp.put("weightUnit", AttributeMode.INITIAL);
		tmp.put("dimensionUnit", AttributeMode.INITIAL);
		tmp.put("supportedAutoShip", AttributeMode.INITIAL);
		tmp.put("genericPLName", AttributeMode.INITIAL);
		tmp.put("sapUnitOfMeasure", AttributeMode.INITIAL);
		tmp.put("sapXdistrChainStatus", AttributeMode.INITIAL);
		tmp.put("sapDChainSpecStatus", AttributeMode.INITIAL);
		tmp.put("sapXPlantMatlStatus", AttributeMode.INITIAL);
		tmp.put("sapStatus", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("categoryUrl", AttributeMode.INITIAL);
		tmp.put("retailName", AttributeMode.INITIAL);
		tmp.put("availableForContentSearch", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.category.jalo.Category", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("patientDirectHub", AttributeMode.INITIAL);
		tmp.put("isPatientDirect", AttributeMode.INITIAL);
		tmp.put("receiveConsultationFee", AttributeMode.INITIAL);
		tmp.put("sapDeliveryBlock", AttributeMode.INITIAL);
		tmp.put("sapPaymentTerms", AttributeMode.INITIAL);
		tmp.put("sapOnCreditHold", AttributeMode.INITIAL);
		tmp.put("sapCustomerGroup", AttributeMode.INITIAL);
		tmp.put("sapCustomerPriceGroup", AttributeMode.INITIAL);
		tmp.put("sapIncoterms", AttributeMode.INITIAL);
		tmp.put("sapShippingCond", AttributeMode.INITIAL);
		tmp.put("sapCustomerClassification", AttributeMode.INITIAL);
		tmp.put("sapSalesOffice", AttributeMode.INITIAL);
		tmp.put("discount", AttributeMode.INITIAL);
		tmp.put("salesPerson", AttributeMode.INITIAL);
		tmp.put("sapBlocked", AttributeMode.INITIAL);
		tmp.put("ageValidationDate", AttributeMode.INITIAL);
		tmp.put("ageExpirationDate", AttributeMode.INITIAL);
		tmp.put("ageBlockedDate", AttributeMode.INITIAL);
		tmp.put("inquiryId", AttributeMode.INITIAL);
		tmp.put("sapCentralDeliveryBlock", AttributeMode.INITIAL);
		tmp.put("brandAgreements", AttributeMode.INITIAL);
		tmp.put("estoreCardToken", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.b2b.jalo.B2BUnit", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("creditUsed", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.b2b.jalo.B2BCreditLimit", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("customerProfile", AttributeMode.INITIAL);
		tmp.put("referralCode", AttributeMode.INITIAL);
		tmp.put("referralName", AttributeMode.INITIAL);
		tmp.put("avalaraTaxLink", AttributeMode.INITIAL);
		tmp.put("avalaraTaxLinkExpiryDate", AttributeMode.INITIAL);
		tmp.put("isSmsNotificationEnabled", AttributeMode.INITIAL);
		tmp.put("isEmailNotificationEnabled", AttributeMode.INITIAL);
		tmp.put("isAmendmentSigned", AttributeMode.INITIAL);
		tmp.put("AccertifyEmailVerified", AttributeMode.INITIAL);
		tmp.put("FailedAccertifyCheck", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.b2b.jalo.B2BCustomer", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("templateId", AttributeMode.INITIAL);
		tmp.put("sapTemplateId", AttributeMode.INITIAL);
		tmp.put("msrpBasePrice", AttributeMode.INITIAL);
		tmp.put("msrpTotalPrice", AttributeMode.INITIAL);
		tmp.put("selectedAutoShip", AttributeMode.INITIAL);
		tmp.put("isHidden", AttributeMode.INITIAL);
		tmp.put("doseNote", AttributeMode.INITIAL);
		tmp.put("description", AttributeMode.INITIAL);
		tmp.put("abstractOrderQty", AttributeMode.INITIAL);
		tmp.put("subTotal", AttributeMode.INITIAL);
		tmp.put("quantityByCase", AttributeMode.INITIAL);
		tmp.put("productDescription", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("msrpSubTotalPrice", AttributeMode.INITIAL);
		tmp.put("salesPolicyTermsAgreed", AttributeMode.INITIAL);
		tmp.put("webOrderNumber", AttributeMode.INITIAL);
		tmp.put("instructions", AttributeMode.INITIAL);
		tmp.put("specProcId", AttributeMode.INITIAL);
		tmp.put("externalOrder", AttributeMode.INITIAL);
		tmp.put("sourceApplication", AttributeMode.INITIAL);
		tmp.put("deliveryBlock", AttributeMode.INITIAL);
		tmp.put("achCardPaymentInfo", AttributeMode.INITIAL);
		tmp.put("wsName", AttributeMode.INITIAL);
		tmp.put("wsDescription", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("wsNotes", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.b2b.jalo.B2BComment", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("maximum", AttributeMode.INITIAL);
		tmp.put("sapCustomerGroupCode", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.deliveryzone.jalo.ZoneDeliveryModeValue", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapCarrier", AttributeMode.INITIAL);
		tmp.put("sapCarrierShippingOption", AttributeMode.INITIAL);
		tmp.put("apiCode", AttributeMode.INITIAL);
		tmp.put("provider", AttributeMode.INITIAL);
		tmp.put("sapShippingCond", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.deliveryzone.jalo.ZoneDeliveryMode", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("practitionerDirectoryOptIn", AttributeMode.INITIAL);
		tmp.put("phoneNumber", AttributeMode.INITIAL);
		tmp.put("webSite", AttributeMode.INITIAL);
		tmp.put("email", AttributeMode.INITIAL);
		tmp.put("practitioner", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.storelocator.jalo.PointOfService", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("w9Form", AttributeMode.INITIAL);
		tmp.put("recommendation", AttributeMode.INITIAL);
		tmp.put("wsContactUs", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.commerceservices.jalo.process.StoreFrontCustomerProcess", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapCustomerPriceGroup", AttributeMode.INITIAL);
		tmp.put("reconciliationAcct", AttributeMode.INITIAL);
		tmp.put("sortkey", AttributeMode.INITIAL);
		tmp.put("creditMemoPytTerm", AttributeMode.INITIAL);
		tmp.put("bankStatement", AttributeMode.INITIAL);
		tmp.put("deliveryPriority", AttributeMode.INITIAL);
		tmp.put("incoterms", AttributeMode.INITIAL);
		tmp.put("actAsGrp", AttributeMode.INITIAL);
		tmp.put("maxPartDeliveries", AttributeMode.INITIAL);
		tmp.put("taxClass", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("cardId", AttributeMode.INITIAL);
		tmp.put("cvvNumber", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.payment.PaymentInfo", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("paymentricStoreId", AttributeMode.INITIAL);
		tmp.put("isNotificationSettingsEnabled", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.store.BaseStore", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("nonDiscountable", AttributeMode.INITIAL);
		tmp.put("discountCash", AttributeMode.INITIAL);
		tmp.put("allowedStackable", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.promotionengineservices.jalo.PromotionSourceRule", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("promotionBanner", AttributeMode.INITIAL);
		tmp.put("colorBackground", AttributeMode.INITIAL);
		tmp.put("colorText", AttributeMode.INITIAL);
		tmp.put("showOnBanners", AttributeMode.INITIAL);
		tmp.put("showOnPLP", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.promotions.jalo.AbstractPromotion", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapBusinessPartnerID", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.Employee", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("isShippingTAxWarningEnabled", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.c2l.Region", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("referralAddress", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.Address", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute.
	 * @return the abstractOrderQty
	 */
	public Long getAbstractOrderQty(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Long)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.ABSTRACTORDERQTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute.
	 * @return the abstractOrderQty
	 */
	public Long getAbstractOrderQty(final AbstractOrderEntry item)
	{
		return getAbstractOrderQty( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute. 
	 * @return the abstractOrderQty
	 */
	public long getAbstractOrderQtyAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Long value = getAbstractOrderQty( ctx,item );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute. 
	 * @return the abstractOrderQty
	 */
	public long getAbstractOrderQtyAsPrimitive(final AbstractOrderEntry item)
	{
		return getAbstractOrderQtyAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final SessionContext ctx, final AbstractOrderEntry item, final Long value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.ABSTRACTORDERQTY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final AbstractOrderEntry item, final Long value)
	{
		setAbstractOrderQty( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final SessionContext ctx, final AbstractOrderEntry item, final long value)
	{
		setAbstractOrderQty( ctx, item, Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final AbstractOrderEntry item, final long value)
	{
		setAbstractOrderQty( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.AccertifyEmailVerified</code> attribute.
	 * @return the AccertifyEmailVerified
	 */
	public Date getAccertifyEmailVerified(final SessionContext ctx, final B2BCustomer item)
	{
		return (Date)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ACCERTIFYEMAILVERIFIED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.AccertifyEmailVerified</code> attribute.
	 * @return the AccertifyEmailVerified
	 */
	public Date getAccertifyEmailVerified(final B2BCustomer item)
	{
		return getAccertifyEmailVerified( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.AccertifyEmailVerified</code> attribute. 
	 * @param value the AccertifyEmailVerified
	 */
	public void setAccertifyEmailVerified(final SessionContext ctx, final B2BCustomer item, final Date value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ACCERTIFYEMAILVERIFIED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.AccertifyEmailVerified</code> attribute. 
	 * @param value the AccertifyEmailVerified
	 */
	public void setAccertifyEmailVerified(final B2BCustomer item, final Date value)
	{
		setAccertifyEmailVerified( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.achCardPaymentInfo</code> attribute.
	 * @return the achCardPaymentInfo
	 */
	public PaymentInfo getAchCardPaymentInfo(final SessionContext ctx, final AbstractOrder item)
	{
		return (PaymentInfo)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.ACHCARDPAYMENTINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.achCardPaymentInfo</code> attribute.
	 * @return the achCardPaymentInfo
	 */
	public PaymentInfo getAchCardPaymentInfo(final AbstractOrder item)
	{
		return getAchCardPaymentInfo( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.achCardPaymentInfo</code> attribute. 
	 * @param value the achCardPaymentInfo
	 */
	public void setAchCardPaymentInfo(final SessionContext ctx, final AbstractOrder item, final PaymentInfo value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.ACHCARDPAYMENTINFO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.achCardPaymentInfo</code> attribute. 
	 * @param value the achCardPaymentInfo
	 */
	public void setAchCardPaymentInfo(final AbstractOrder item, final PaymentInfo value)
	{
		setAchCardPaymentInfo( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.actAsGrp</code> attribute.
	 * @return the actAsGrp - act As Grp
	 */
	public String getActAsGrp(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.ACTASGRP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.actAsGrp</code> attribute.
	 * @return the actAsGrp - act As Grp
	 */
	public String getActAsGrp(final SAPConfiguration item)
	{
		return getActAsGrp( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.actAsGrp</code> attribute. 
	 * @param value the actAsGrp - act As Grp
	 */
	public void setActAsGrp(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.ACTASGRP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.actAsGrp</code> attribute. 
	 * @param value the actAsGrp - act As Grp
	 */
	public void setActAsGrp(final SAPConfiguration item, final String value)
	{
		setActAsGrp( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ageBlockedDate</code> attribute.
	 * @return the ageBlockedDate
	 */
	public String getAgeBlockedDate(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.AGEBLOCKEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ageBlockedDate</code> attribute.
	 * @return the ageBlockedDate
	 */
	public String getAgeBlockedDate(final B2BUnit item)
	{
		return getAgeBlockedDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ageBlockedDate</code> attribute. 
	 * @param value the ageBlockedDate
	 */
	public void setAgeBlockedDate(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.AGEBLOCKEDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ageBlockedDate</code> attribute. 
	 * @param value the ageBlockedDate
	 */
	public void setAgeBlockedDate(final B2BUnit item, final String value)
	{
		setAgeBlockedDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ageExpirationDate</code> attribute.
	 * @return the ageExpirationDate
	 */
	public String getAgeExpirationDate(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.AGEEXPIRATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ageExpirationDate</code> attribute.
	 * @return the ageExpirationDate
	 */
	public String getAgeExpirationDate(final B2BUnit item)
	{
		return getAgeExpirationDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ageExpirationDate</code> attribute. 
	 * @param value the ageExpirationDate
	 */
	public void setAgeExpirationDate(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.AGEEXPIRATIONDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ageExpirationDate</code> attribute. 
	 * @param value the ageExpirationDate
	 */
	public void setAgeExpirationDate(final B2BUnit item, final String value)
	{
		setAgeExpirationDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ageValidationDate</code> attribute.
	 * @return the ageValidationDate
	 */
	public String getAgeValidationDate(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.AGEVALIDATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.ageValidationDate</code> attribute.
	 * @return the ageValidationDate
	 */
	public String getAgeValidationDate(final B2BUnit item)
	{
		return getAgeValidationDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ageValidationDate</code> attribute. 
	 * @param value the ageValidationDate
	 */
	public void setAgeValidationDate(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.AGEVALIDATIONDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.ageValidationDate</code> attribute. 
	 * @param value the ageValidationDate
	 */
	public void setAgeValidationDate(final B2BUnit item, final String value)
	{
		setAgeValidationDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.allowedGroups</code> attribute.
	 * @return the allowedGroups
	 */
	public Set<PrincipalGroup> getAllowedGroups(final SessionContext ctx, final AbstractPromotion item)
	{
		final List<PrincipalGroup> items = item.getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			"PrincipalGroup",
			null,
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<PrincipalGroup>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.allowedGroups</code> attribute.
	 * @return the allowedGroups
	 */
	public Set<PrincipalGroup> getAllowedGroups(final AbstractPromotion item)
	{
		return getAllowedGroups( getSession().getSessionContext(), item );
	}
	
	public long getAllowedGroupsCount(final SessionContext ctx, final AbstractPromotion item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			"PrincipalGroup",
			null
		);
	}
	
	public long getAllowedGroupsCount(final AbstractPromotion item)
	{
		return getAllowedGroupsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.allowedGroups</code> attribute. 
	 * @param value the allowedGroups
	 */
	public void setAllowedGroups(final SessionContext ctx, final AbstractPromotion item, final Set<PrincipalGroup> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.allowedGroups</code> attribute. 
	 * @param value the allowedGroups
	 */
	public void setAllowedGroups(final AbstractPromotion item, final Set<PrincipalGroup> value)
	{
		setAllowedGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to allowedGroups. 
	 * @param value the item to add to allowedGroups
	 */
	public void addToAllowedGroups(final SessionContext ctx, final AbstractPromotion item, final PrincipalGroup value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to allowedGroups. 
	 * @param value the item to add to allowedGroups
	 */
	public void addToAllowedGroups(final AbstractPromotion item, final PrincipalGroup value)
	{
		addToAllowedGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from allowedGroups. 
	 * @param value the item to remove from allowedGroups
	 */
	public void removeFromAllowedGroups(final SessionContext ctx, final AbstractPromotion item, final PrincipalGroup value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from allowedGroups. 
	 * @param value the item to remove from allowedGroups
	 */
	public void removeFromAllowedGroups(final AbstractPromotion item, final PrincipalGroup value)
	{
		removeFromAllowedGroups( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.allowedStackable</code> attribute.
	 * @return the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public Boolean isAllowedStackable(final SessionContext ctx, final PromotionSourceRule item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PromotionSourceRule.ALLOWEDSTACKABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.allowedStackable</code> attribute.
	 * @return the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public Boolean isAllowedStackable(final PromotionSourceRule item)
	{
		return isAllowedStackable( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.allowedStackable</code> attribute. 
	 * @return the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public boolean isAllowedStackableAsPrimitive(final SessionContext ctx, final PromotionSourceRule item)
	{
		Boolean value = isAllowedStackable( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.allowedStackable</code> attribute. 
	 * @return the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public boolean isAllowedStackableAsPrimitive(final PromotionSourceRule item)
	{
		return isAllowedStackableAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.allowedStackable</code> attribute. 
	 * @param value the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public void setAllowedStackable(final SessionContext ctx, final PromotionSourceRule item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PromotionSourceRule.ALLOWEDSTACKABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.allowedStackable</code> attribute. 
	 * @param value the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public void setAllowedStackable(final PromotionSourceRule item, final Boolean value)
	{
		setAllowedStackable( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.allowedStackable</code> attribute. 
	 * @param value the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public void setAllowedStackable(final SessionContext ctx, final PromotionSourceRule item, final boolean value)
	{
		setAllowedStackable( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.allowedStackable</code> attribute. 
	 * @param value the allowedStackable - flag to indicate whether this promotion stackable
	 */
	public void setAllowedStackable(final PromotionSourceRule item, final boolean value)
	{
		setAllowedStackable( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.apiCode</code> attribute.
	 * @return the apiCode
	 */
	public String getApiCode(final SessionContext ctx, final ZoneDeliveryMode item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.APICODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.apiCode</code> attribute.
	 * @return the apiCode
	 */
	public String getApiCode(final ZoneDeliveryMode item)
	{
		return getApiCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.apiCode</code> attribute. 
	 * @param value the apiCode
	 */
	public void setApiCode(final SessionContext ctx, final ZoneDeliveryMode item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.APICODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.apiCode</code> attribute. 
	 * @param value the apiCode
	 */
	public void setApiCode(final ZoneDeliveryMode item, final String value)
	{
		setApiCode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.availableForContentSearch</code> attribute.
	 * @return the availableForContentSearch
	 */
	public Boolean isAvailableForContentSearch(final SessionContext ctx, final Category item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Category.AVAILABLEFORCONTENTSEARCH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.availableForContentSearch</code> attribute.
	 * @return the availableForContentSearch
	 */
	public Boolean isAvailableForContentSearch(final Category item)
	{
		return isAvailableForContentSearch( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.availableForContentSearch</code> attribute. 
	 * @return the availableForContentSearch
	 */
	public boolean isAvailableForContentSearchAsPrimitive(final SessionContext ctx, final Category item)
	{
		Boolean value = isAvailableForContentSearch( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.availableForContentSearch</code> attribute. 
	 * @return the availableForContentSearch
	 */
	public boolean isAvailableForContentSearchAsPrimitive(final Category item)
	{
		return isAvailableForContentSearchAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final SessionContext ctx, final Category item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Category.AVAILABLEFORCONTENTSEARCH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final Category item, final Boolean value)
	{
		setAvailableForContentSearch( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final SessionContext ctx, final Category item, final boolean value)
	{
		setAvailableForContentSearch( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final Category item, final boolean value)
	{
		setAvailableForContentSearch( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.avalaraTaxLink</code> attribute.
	 * @return the avalaraTaxLink
	 */
	public String getAvalaraTaxLink(final SessionContext ctx, final B2BCustomer item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.AVALARATAXLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.avalaraTaxLink</code> attribute.
	 * @return the avalaraTaxLink
	 */
	public String getAvalaraTaxLink(final B2BCustomer item)
	{
		return getAvalaraTaxLink( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.avalaraTaxLink</code> attribute. 
	 * @param value the avalaraTaxLink
	 */
	public void setAvalaraTaxLink(final SessionContext ctx, final B2BCustomer item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.AVALARATAXLINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.avalaraTaxLink</code> attribute. 
	 * @param value the avalaraTaxLink
	 */
	public void setAvalaraTaxLink(final B2BCustomer item, final String value)
	{
		setAvalaraTaxLink( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.avalaraTaxLinkExpiryDate</code> attribute.
	 * @return the avalaraTaxLinkExpiryDate
	 */
	public Date getAvalaraTaxLinkExpiryDate(final SessionContext ctx, final B2BCustomer item)
	{
		return (Date)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.AVALARATAXLINKEXPIRYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.avalaraTaxLinkExpiryDate</code> attribute.
	 * @return the avalaraTaxLinkExpiryDate
	 */
	public Date getAvalaraTaxLinkExpiryDate(final B2BCustomer item)
	{
		return getAvalaraTaxLinkExpiryDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.avalaraTaxLinkExpiryDate</code> attribute. 
	 * @param value the avalaraTaxLinkExpiryDate
	 */
	public void setAvalaraTaxLinkExpiryDate(final SessionContext ctx, final B2BCustomer item, final Date value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.AVALARATAXLINKEXPIRYDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.avalaraTaxLinkExpiryDate</code> attribute. 
	 * @param value the avalaraTaxLinkExpiryDate
	 */
	public void setAvalaraTaxLinkExpiryDate(final B2BCustomer item, final Date value)
	{
		setAvalaraTaxLinkExpiryDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.bankStatement</code> attribute.
	 * @return the bankStatement - bank Statement
	 */
	public String getBankStatement(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.BANKSTATEMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.bankStatement</code> attribute.
	 * @return the bankStatement - bank Statement
	 */
	public String getBankStatement(final SAPConfiguration item)
	{
		return getBankStatement( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.bankStatement</code> attribute. 
	 * @param value the bankStatement - bank Statement
	 */
	public void setBankStatement(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.BANKSTATEMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.bankStatement</code> attribute. 
	 * @param value the bankStatement - bank Statement
	 */
	public void setBankStatement(final SAPConfiguration item, final String value)
	{
		setBankStatement( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.brandAgreements</code> attribute.
	 * @return the brandAgreements
	 */
	public List<BrandAgreement> getBrandAgreements(final SessionContext ctx, final B2BUnit item)
	{
		List<BrandAgreement> coll = (List<BrandAgreement>)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.BRANDAGREEMENTS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.brandAgreements</code> attribute.
	 * @return the brandAgreements
	 */
	public List<BrandAgreement> getBrandAgreements(final B2BUnit item)
	{
		return getBrandAgreements( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.brandAgreements</code> attribute. 
	 * @param value the brandAgreements
	 */
	public void setBrandAgreements(final SessionContext ctx, final B2BUnit item, final List<BrandAgreement> value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.BRANDAGREEMENTS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.brandAgreements</code> attribute. 
	 * @param value the brandAgreements
	 */
	public void setBrandAgreements(final B2BUnit item, final List<BrandAgreement> value)
	{
		setBrandAgreements( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.cardId</code> attribute.
	 * @return the cardId - Card id this will be stored in SAP
	 */
	public String getCardId(final SessionContext ctx, final PaymentInfo item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.CreditCardPaymentInfo.CARDID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.cardId</code> attribute.
	 * @return the cardId - Card id this will be stored in SAP
	 */
	public String getCardId(final PaymentInfo item)
	{
		return getCardId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.cardId</code> attribute. 
	 * @param value the cardId - Card id this will be stored in SAP
	 */
	public void setCardId(final SessionContext ctx, final PaymentInfo item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.CreditCardPaymentInfo.CARDID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.cardId</code> attribute. 
	 * @param value the cardId - Card id this will be stored in SAP
	 */
	public void setCardId(final PaymentInfo item, final String value)
	{
		setCardId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.categoryUrl</code> attribute.
	 * @return the categoryUrl
	 */
	public String getCategoryUrl(final SessionContext ctx, final Category item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Category.CATEGORYURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.categoryUrl</code> attribute.
	 * @return the categoryUrl
	 */
	public String getCategoryUrl(final Category item)
	{
		return getCategoryUrl( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.categoryUrl</code> attribute. 
	 * @param value the categoryUrl
	 */
	public void setCategoryUrl(final SessionContext ctx, final Category item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Category.CATEGORYURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.categoryUrl</code> attribute. 
	 * @param value the categoryUrl
	 */
	public void setCategoryUrl(final Category item, final String value)
	{
		setCategoryUrl( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.colorBackground</code> attribute.
	 * @return the colorBackground - promotion category code
	 */
	public String getColorBackground(final SessionContext ctx, final AbstractPromotion item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.COLORBACKGROUND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.colorBackground</code> attribute.
	 * @return the colorBackground - promotion category code
	 */
	public String getColorBackground(final AbstractPromotion item)
	{
		return getColorBackground( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.colorBackground</code> attribute. 
	 * @param value the colorBackground - promotion category code
	 */
	public void setColorBackground(final SessionContext ctx, final AbstractPromotion item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.COLORBACKGROUND,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.colorBackground</code> attribute. 
	 * @param value the colorBackground - promotion category code
	 */
	public void setColorBackground(final AbstractPromotion item, final String value)
	{
		setColorBackground( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.colorText</code> attribute.
	 * @return the colorText - promotion category code
	 */
	public String getColorText(final SessionContext ctx, final AbstractPromotion item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.COLORTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.colorText</code> attribute.
	 * @return the colorText - promotion category code
	 */
	public String getColorText(final AbstractPromotion item)
	{
		return getColorText( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.colorText</code> attribute. 
	 * @param value the colorText - promotion category code
	 */
	public void setColorText(final SessionContext ctx, final AbstractPromotion item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.COLORTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.colorText</code> attribute. 
	 * @param value the colorText - promotion category code
	 */
	public void setColorText(final AbstractPromotion item, final String value)
	{
		setColorText( getSession().getSessionContext(), item, value );
	}
	
	public PaymentInfo createACHCardPaymentInfo(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("ACHCardPaymentInfo");
			return (PaymentInfo)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ACHCardPaymentInfo : "+e.getMessage(), 0 );
		}
	}
	
	public PaymentInfo createACHCardPaymentInfo(final Map attributeValues)
	{
		return createACHCardPaymentInfo( getSession().getSessionContext(), attributeValues );
	}
	
	public BrandAgreement createBrandAgreement(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("BrandAgreement");
			return (BrandAgreement)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating BrandAgreement : "+e.getMessage(), 0 );
		}
	}
	
	public BrandAgreement createBrandAgreement(final Map attributeValues)
	{
		return createBrandAgreement( getSession().getSessionContext(), attributeValues );
	}
	
	public BrandCustomerClassRestriction createBrandCustomerClassRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("BrandCustomerClassRestriction");
			return (BrandCustomerClassRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating BrandCustomerClassRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public BrandCustomerClassRestriction createBrandCustomerClassRestriction(final Map attributeValues)
	{
		return createBrandCustomerClassRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public ContentSearch createContentSearch(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("ContentSearch");
			return (ContentSearch)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ContentSearch : "+e.getMessage(), 0 );
		}
	}
	
	public ContentSearch createContentSearch(final Map attributeValues)
	{
		return createContentSearch( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerClassProductRestriction createCustomerClassProductRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("CustomerClassProductRestriction");
			return (CustomerClassProductRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerClassProductRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerClassProductRestriction createCustomerClassProductRestriction(final Map attributeValues)
	{
		return createCustomerClassProductRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerMaterialInformation createCustomerMaterialInformation(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("CustomerMaterialInformation");
			return (CustomerMaterialInformation)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerMaterialInformation : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerMaterialInformation createCustomerMaterialInformation(final Map attributeValues)
	{
		return createCustomerMaterialInformation( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerMaterialInformationRequest createCustomerMaterialInformationRequest(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("CustomerMaterialInformationRequest");
			return (CustomerMaterialInformationRequest)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerMaterialInformationRequest : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerMaterialInformationRequest createCustomerMaterialInformationRequest(final Map attributeValues)
	{
		return createCustomerMaterialInformationRequest( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerProductGroupRestriction createCustomerProductGroupRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("CustomerProductGroupRestriction");
			return (CustomerProductGroupRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerProductGroupRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerProductGroupRestriction createCustomerProductGroupRestriction(final Map attributeValues)
	{
		return createCustomerProductGroupRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public CustomerProductRestriction createCustomerProductRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("CustomerProductRestriction");
			return (CustomerProductRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CustomerProductRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public CustomerProductRestriction createCustomerProductRestriction(final Map attributeValues)
	{
		return createCustomerProductRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public Dosing createDosing(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("Dosing");
			return (Dosing)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Dosing : "+e.getMessage(), 0 );
		}
	}
	
	public Dosing createDosing(final Map attributeValues)
	{
		return createDosing( getSession().getSessionContext(), attributeValues );
	}
	
	public GetAvalaraTaxLinkCronjob createGetAvalaraTaxLinkCronjob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("GetAvalaraTaxLinkCronjob");
			return (GetAvalaraTaxLinkCronjob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating GetAvalaraTaxLinkCronjob : "+e.getMessage(), 0 );
		}
	}
	
	public GetAvalaraTaxLinkCronjob createGetAvalaraTaxLinkCronjob(final Map attributeValues)
	{
		return createGetAvalaraTaxLinkCronjob( getSession().getSessionContext(), attributeValues );
	}
	
	public Ingredient createIngredient(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("Ingredient");
			return (Ingredient)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Ingredient : "+e.getMessage(), 0 );
		}
	}
	
	public Ingredient createIngredient(final Map attributeValues)
	{
		return createIngredient( getSession().getSessionContext(), attributeValues );
	}
	
	public IngredientReference createIngredientReference(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("IngredientReference");
			return (IngredientReference)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating IngredientReference : "+e.getMessage(), 0 );
		}
	}
	
	public IngredientReference createIngredientReference(final Map attributeValues)
	{
		return createIngredientReference( getSession().getSessionContext(), attributeValues );
	}
	
	public InventoryOutboundRequest createInventoryOutboundRequest(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("InventoryOutboundRequest");
			return (InventoryOutboundRequest)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating InventoryOutboundRequest : "+e.getMessage(), 0 );
		}
	}
	
	public InventoryOutboundRequest createInventoryOutboundRequest(final Map attributeValues)
	{
		return createInventoryOutboundRequest( getSession().getSessionContext(), attributeValues );
	}
	
	public ItemTime createItemTime(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("ItemTime");
			return (ItemTime)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ItemTime : "+e.getMessage(), 0 );
		}
	}
	
	public ItemTime createItemTime(final Map attributeValues)
	{
		return createItemTime( getSession().getSessionContext(), attributeValues );
	}
	
	public MaterialRestrictionRequest createMaterialRestrictionRequest(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("MaterialRestrictionRequest");
			return (MaterialRestrictionRequest)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating MaterialRestrictionRequest : "+e.getMessage(), 0 );
		}
	}
	
	public MaterialRestrictionRequest createMaterialRestrictionRequest(final Map attributeValues)
	{
		return createMaterialRestrictionRequest( getSession().getSessionContext(), attributeValues );
	}
	
	public MedPax createMedPax(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("MedPax");
			return (MedPax)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating MedPax : "+e.getMessage(), 0 );
		}
	}
	
	public MedPax createMedPax(final Map attributeValues)
	{
		return createMedPax( getSession().getSessionContext(), attributeValues );
	}
	
	public MedpaxEntry createMedpaxEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("MedpaxEntry");
			return (MedpaxEntry)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating MedpaxEntry : "+e.getMessage(), 0 );
		}
	}
	
	public MedpaxEntry createMedpaxEntry(final Map attributeValues)
	{
		return createMedpaxEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public PatientDirectHub createPatientDirectHub(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("PatientDirectHub");
			return (PatientDirectHub)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PatientDirectHub : "+e.getMessage(), 0 );
		}
	}
	
	public PatientDirectHub createPatientDirectHub(final Map attributeValues)
	{
		return createPatientDirectHub( getSession().getSessionContext(), attributeValues );
	}
	
	public Presenter createPresenter(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("Presenter");
			return (Presenter)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Presenter : "+e.getMessage(), 0 );
		}
	}
	
	public Presenter createPresenter(final Map attributeValues)
	{
		return createPresenter( getSession().getSessionContext(), attributeValues );
	}
	
	public PromotionCategory createPromotionCategory(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("PromotionCategory");
			return (PromotionCategory)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PromotionCategory : "+e.getMessage(), 0 );
		}
	}
	
	public PromotionCategory createPromotionCategory(final Map attributeValues)
	{
		return createPromotionCategory( getSession().getSessionContext(), attributeValues );
	}
	
	public Protocol createProtocol(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("Protocol");
			return (Protocol)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Protocol : "+e.getMessage(), 0 );
		}
	}
	
	public Protocol createProtocol(final Map attributeValues)
	{
		return createProtocol( getSession().getSessionContext(), attributeValues );
	}
	
	public CartEntry createProtocolEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("ProtocolEntry");
			return (CartEntry)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ProtocolEntry : "+e.getMessage(), 0 );
		}
	}
	
	public CartEntry createProtocolEntry(final Map attributeValues)
	{
		return createProtocolEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public Recommendation createRecommendation(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("Recommendation");
			return (Recommendation)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Recommendation : "+e.getMessage(), 0 );
		}
	}
	
	public Recommendation createRecommendation(final Map attributeValues)
	{
		return createRecommendation( getSession().getSessionContext(), attributeValues );
	}
	
	public CartEntry createRecommendationEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("RecommendationEntry");
			return (CartEntry)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating RecommendationEntry : "+e.getMessage(), 0 );
		}
	}
	
	public CartEntry createRecommendationEntry(final Map attributeValues)
	{
		return createRecommendationEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public ReferralCodes createReferralCodes(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("ReferralCodes");
			return (ReferralCodes)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ReferralCodes : "+e.getMessage(), 0 );
		}
	}
	
	public ReferralCodes createReferralCodes(final Map attributeValues)
	{
		return createReferralCodes( getSession().getSessionContext(), attributeValues );
	}
	
	public RegionProductAgeRestriction createRegionProductAgeRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("RegionProductAgeRestriction");
			return (RegionProductAgeRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating RegionProductAgeRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public RegionProductAgeRestriction createRegionProductAgeRestriction(final Map attributeValues)
	{
		return createRegionProductAgeRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public RegionProductAgeRestrictionOutboundRequest createRegionProductAgeRestrictionOutboundRequest(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("RegionProductAgeRestrictionOutboundRequest");
			return (RegionProductAgeRestrictionOutboundRequest)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating RegionProductAgeRestrictionOutboundRequest : "+e.getMessage(), 0 );
		}
	}
	
	public RegionProductAgeRestrictionOutboundRequest createRegionProductAgeRestrictionOutboundRequest(final Map attributeValues)
	{
		return createRegionProductAgeRestrictionOutboundRequest( getSession().getSessionContext(), attributeValues );
	}
	
	public RegionProductRestriction createRegionProductRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("RegionProductRestriction");
			return (RegionProductRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating RegionProductRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public RegionProductRestriction createRegionProductRestriction(final Map attributeValues)
	{
		return createRegionProductRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public RegionProductSwapRestriction createRegionProductSwapRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("RegionProductSwapRestriction");
			return (RegionProductSwapRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating RegionProductSwapRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public RegionProductSwapRestriction createRegionProductSwapRestriction(final Map attributeValues)
	{
		return createRegionProductSwapRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public RegionProductSwapRestrictionOutboundRequest createRegionProductSwapRestrictionOutboundRequest(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("RegionProductSwapRestrictionOutboundRequest");
			return (RegionProductSwapRestrictionOutboundRequest)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating RegionProductSwapRestrictionOutboundRequest : "+e.getMessage(), 0 );
		}
	}
	
	public RegionProductSwapRestrictionOutboundRequest createRegionProductSwapRestrictionOutboundRequest(final Map attributeValues)
	{
		return createRegionProductSwapRestrictionOutboundRequest( getSession().getSessionContext(), attributeValues );
	}
	
	public SapUnitOfMeasure createSapUnitOfMeasure(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("sapUnitOfMeasure");
			return (SapUnitOfMeasure)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating sapUnitOfMeasure : "+e.getMessage(), 0 );
		}
	}
	
	public SapUnitOfMeasure createSapUnitOfMeasure(final Map attributeValues)
	{
		return createSapUnitOfMeasure( getSession().getSessionContext(), attributeValues );
	}
	
	public SupportedAutoShip createSupportedAutoShip(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("SupportedAutoShip");
			return (SupportedAutoShip)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating SupportedAutoShip : "+e.getMessage(), 0 );
		}
	}
	
	public SupportedAutoShip createSupportedAutoShip(final Map attributeValues)
	{
		return createSupportedAutoShip( getSession().getSessionContext(), attributeValues );
	}
	
	public W9Form createW9Form(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("W9Form");
			return (W9Form)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating W9Form : "+e.getMessage(), 0 );
		}
	}
	
	public W9Form createW9Form(final Map attributeValues)
	{
		return createW9Form( getSession().getSessionContext(), attributeValues );
	}
	
	public WatchTracker createWatchTracker(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WatchTracker");
			return (WatchTracker)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WatchTracker : "+e.getMessage(), 0 );
		}
	}
	
	public WatchTracker createWatchTracker(final Map attributeValues)
	{
		return createWatchTracker( getSession().getSessionContext(), attributeValues );
	}
	
	public WebBannersAndScripts createWebBannersAndScripts(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WebBannersAndScripts");
			return (WebBannersAndScripts)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WebBannersAndScripts : "+e.getMessage(), 0 );
		}
	}
	
	public WebBannersAndScripts createWebBannersAndScripts(final Map attributeValues)
	{
		return createWebBannersAndScripts( getSession().getSessionContext(), attributeValues );
	}
	
	public WebBannersAndScriptsEntry createWebBannersAndScriptsEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WebBannersAndScriptsEntry");
			return (WebBannersAndScriptsEntry)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WebBannersAndScriptsEntry : "+e.getMessage(), 0 );
		}
	}
	
	public WebBannersAndScriptsEntry createWebBannersAndScriptsEntry(final Map attributeValues)
	{
		return createWebBannersAndScriptsEntry( getSession().getSessionContext(), attributeValues );
	}
	
	public Webinar createWebinar(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("Webinar");
			return (Webinar)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Webinar : "+e.getMessage(), 0 );
		}
	}
	
	public Webinar createWebinar(final Map attributeValues)
	{
		return createWebinar( getSession().getSessionContext(), attributeValues );
	}
	
	public WebinarCategoryTag createWebinarCategoryTag(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WebinarCategoryTag");
			return (WebinarCategoryTag)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WebinarCategoryTag : "+e.getMessage(), 0 );
		}
	}
	
	public WebinarCategoryTag createWebinarCategoryTag(final Map attributeValues)
	{
		return createWebinarCategoryTag( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsBannerComponent createWholescriptsBannerComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsBannerComponent");
			return (WholescriptsBannerComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsBannerComponent : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsBannerComponent createWholescriptsBannerComponent(final Map attributeValues)
	{
		return createWholescriptsBannerComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsBrand createWholescriptsBrand(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsBrand");
			return (WholescriptsBrand)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsBrand : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsBrand createWholescriptsBrand(final Map attributeValues)
	{
		return createWholescriptsBrand( getSession().getSessionContext(), attributeValues );
	}
	
	public WholeScriptsBrightStockProducts createWholeScriptsBrightStockProducts(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholeScriptsBrightStockProducts");
			return (WholeScriptsBrightStockProducts)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholeScriptsBrightStockProducts : "+e.getMessage(), 0 );
		}
	}
	
	public WholeScriptsBrightStockProducts createWholeScriptsBrightStockProducts(final Map attributeValues)
	{
		return createWholeScriptsBrightStockProducts( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsCarouselComponent createWholescriptsCarouselComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsCarouselComponent");
			return (WholescriptsCarouselComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsCarouselComponent : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsCarouselComponent createWholescriptsCarouselComponent(final Map attributeValues)
	{
		return createWholescriptsCarouselComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsCarouselListComponent createWholescriptsCarouselListComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsCarouselListComponent");
			return (WholescriptsCarouselListComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsCarouselListComponent : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsCarouselListComponent createWholescriptsCarouselListComponent(final Map attributeValues)
	{
		return createWholescriptsCarouselListComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsCustomerActivity createWholescriptsCustomerActivity(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsCustomerActivity");
			return (WholescriptsCustomerActivity)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsCustomerActivity : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsCustomerActivity createWholescriptsCustomerActivity(final Map attributeValues)
	{
		return createWholescriptsCustomerActivity( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsFaq createWholescriptsFaq(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsFaq");
			return (WholescriptsFaq)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsFaq : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsFaq createWholescriptsFaq(final Map attributeValues)
	{
		return createWholescriptsFaq( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsFaqList createWholescriptsFaqList(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsFaqList");
			return (WholescriptsFaqList)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsFaqList : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsFaqList createWholescriptsFaqList(final Map attributeValues)
	{
		return createWholescriptsFaqList( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsFlexComponent createWholescriptsFlexComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsFlexComponent");
			return (WholescriptsFlexComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsFlexComponent : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsFlexComponent createWholescriptsFlexComponent(final Map attributeValues)
	{
		return createWholescriptsFlexComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsIntegrations createWholescriptsIntegrations(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsIntegrations");
			return (WholescriptsIntegrations)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsIntegrations : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsIntegrations createWholescriptsIntegrations(final Map attributeValues)
	{
		return createWholescriptsIntegrations( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsOutboundCronjob createWholescriptsOutboundCronjob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsOutboundCronjob");
			return (WholescriptsOutboundCronjob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsOutboundCronjob : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsOutboundCronjob createWholescriptsOutboundCronjob(final Map attributeValues)
	{
		return createWholescriptsOutboundCronjob( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsOutboundRequest createWholescriptsOutboundRequest(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsOutboundRequest");
			return (WholescriptsOutboundRequest)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsOutboundRequest : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsOutboundRequest createWholescriptsOutboundRequest(final Map attributeValues)
	{
		return createWholescriptsOutboundRequest( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsOutboundSyncJob createWholescriptsOutboundSyncJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsOutboundSyncJob");
			return (WholescriptsOutboundSyncJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsOutboundSyncJob : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsOutboundSyncJob createWholescriptsOutboundSyncJob(final Map attributeValues)
	{
		return createWholescriptsOutboundSyncJob( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsTopic createWholescriptsTopic(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsTopic");
			return (WholescriptsTopic)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsTopic : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsTopic createWholescriptsTopic(final Map attributeValues)
	{
		return createWholescriptsTopic( getSession().getSessionContext(), attributeValues );
	}
	
	public WholescriptsUnsubscridbedEmails createWholescriptsUnsubscridbedEmails(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WholescriptsUnsubscridbedEmails");
			return (WholescriptsUnsubscridbedEmails)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WholescriptsUnsubscridbedEmails : "+e.getMessage(), 0 );
		}
	}
	
	public WholescriptsUnsubscridbedEmails createWholescriptsUnsubscridbedEmails(final Map attributeValues)
	{
		return createWholescriptsUnsubscridbedEmails( getSession().getSessionContext(), attributeValues );
	}
	
	public WsAvailableSuppliesLast createWsAvailableSuppliesLast(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WsAvailableSuppliesLast");
			return (WsAvailableSuppliesLast)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WsAvailableSuppliesLast : "+e.getMessage(), 0 );
		}
	}
	
	public WsAvailableSuppliesLast createWsAvailableSuppliesLast(final Map attributeValues)
	{
		return createWsAvailableSuppliesLast( getSession().getSessionContext(), attributeValues );
	}
	
	public WSContactUsForm createWSContactUsForm(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WSContactUsForm");
			return (WSContactUsForm)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WSContactUsForm : "+e.getMessage(), 0 );
		}
	}
	
	public WSContactUsForm createWSContactUsForm(final Map attributeValues)
	{
		return createWSContactUsForm( getSession().getSessionContext(), attributeValues );
	}
	
	public WsDiscontinuedProducts createWsDiscontinuedProducts(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WsDiscontinuedProducts");
			return (WsDiscontinuedProducts)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WsDiscontinuedProducts : "+e.getMessage(), 0 );
		}
	}
	
	public WsDiscontinuedProducts createWsDiscontinuedProducts(final Map attributeValues)
	{
		return createWsDiscontinuedProducts( getSession().getSessionContext(), attributeValues );
	}
	
	public WsIntegrationBadgeParagraphComponent createWsIntegrationBadgeParagraphComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WsIntegrationBadgeParagraphComponent");
			return (WsIntegrationBadgeParagraphComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WsIntegrationBadgeParagraphComponent : "+e.getMessage(), 0 );
		}
	}
	
	public WsIntegrationBadgeParagraphComponent createWsIntegrationBadgeParagraphComponent(final Map attributeValues)
	{
		return createWsIntegrationBadgeParagraphComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public WsIntegrationContentParagraphComponent createWsIntegrationContentParagraphComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WsIntegrationContentParagraphComponent");
			return (WsIntegrationContentParagraphComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WsIntegrationContentParagraphComponent : "+e.getMessage(), 0 );
		}
	}
	
	public WsIntegrationContentParagraphComponent createWsIntegrationContentParagraphComponent(final Map attributeValues)
	{
		return createWsIntegrationContentParagraphComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public WsPopularCategories createWsPopularCategories(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WsPopularCategories");
			return (WsPopularCategories)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WsPopularCategories : "+e.getMessage(), 0 );
		}
	}
	
	public WsPopularCategories createWsPopularCategories(final Map attributeValues)
	{
		return createWsPopularCategories( getSession().getSessionContext(), attributeValues );
	}
	
	public WsPopularProducts createWsPopularProducts(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType("WsPopularProducts");
			return (WsPopularProducts)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating WsPopularProducts : "+e.getMessage(), 0 );
		}
	}
	
	public WsPopularProducts createWsPopularProducts(final Map attributeValues)
	{
		return createWsPopularProducts( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.creditMemoPytTerm</code> attribute.
	 * @return the creditMemoPytTerm - credit Memo PytTerm
	 */
	public String getCreditMemoPytTerm(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.CREDITMEMOPYTTERM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.creditMemoPytTerm</code> attribute.
	 * @return the creditMemoPytTerm - credit Memo PytTerm
	 */
	public String getCreditMemoPytTerm(final SAPConfiguration item)
	{
		return getCreditMemoPytTerm( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.creditMemoPytTerm</code> attribute. 
	 * @param value the creditMemoPytTerm - credit Memo PytTerm
	 */
	public void setCreditMemoPytTerm(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.CREDITMEMOPYTTERM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.creditMemoPytTerm</code> attribute. 
	 * @param value the creditMemoPytTerm - credit Memo PytTerm
	 */
	public void setCreditMemoPytTerm(final SAPConfiguration item, final String value)
	{
		setCreditMemoPytTerm( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.creditUsed</code> attribute.
	 * @return the creditUsed
	 */
	public String getCreditUsed(final SessionContext ctx, final B2BCreditLimit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCreditLimit.CREDITUSED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCreditLimit.creditUsed</code> attribute.
	 * @return the creditUsed
	 */
	public String getCreditUsed(final B2BCreditLimit item)
	{
		return getCreditUsed( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.creditUsed</code> attribute. 
	 * @param value the creditUsed
	 */
	public void setCreditUsed(final SessionContext ctx, final B2BCreditLimit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCreditLimit.CREDITUSED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCreditLimit.creditUsed</code> attribute. 
	 * @param value the creditUsed
	 */
	public void setCreditUsed(final B2BCreditLimit item, final String value)
	{
		setCreditUsed( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.customerProfile</code> attribute.
	 * @return the customerProfile - Stores the type of Customer
	 */
	public EnumerationValue getCustomerProfile(final SessionContext ctx, final B2BCustomer item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.CUSTOMERPROFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.customerProfile</code> attribute.
	 * @return the customerProfile - Stores the type of Customer
	 */
	public EnumerationValue getCustomerProfile(final B2BCustomer item)
	{
		return getCustomerProfile( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.customerProfile</code> attribute. 
	 * @param value the customerProfile - Stores the type of Customer
	 */
	public void setCustomerProfile(final SessionContext ctx, final B2BCustomer item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.CUSTOMERPROFILE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.customerProfile</code> attribute. 
	 * @param value the customerProfile - Stores the type of Customer
	 */
	public void setCustomerProfile(final B2BCustomer item, final EnumerationValue value)
	{
		setCustomerProfile( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.cvvNumber</code> attribute.
	 * @return the cvvNumber - Card CVV number.
	 */
	public String getCvvNumber(final SessionContext ctx, final PaymentInfo item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.CreditCardPaymentInfo.CVVNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.cvvNumber</code> attribute.
	 * @return the cvvNumber - Card CVV number.
	 */
	public String getCvvNumber(final PaymentInfo item)
	{
		return getCvvNumber( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.cvvNumber</code> attribute. 
	 * @param value the cvvNumber - Card CVV number.
	 */
	public void setCvvNumber(final SessionContext ctx, final PaymentInfo item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.CreditCardPaymentInfo.CVVNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CreditCardPaymentInfo.cvvNumber</code> attribute. 
	 * @param value the cvvNumber - Card CVV number.
	 */
	public void setCvvNumber(final PaymentInfo item, final String value)
	{
		setCvvNumber( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryBlock</code> attribute.
	 * @return the deliveryBlock
	 */
	public Boolean isDeliveryBlock(final SessionContext ctx, final AbstractOrder item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.DELIVERYBLOCK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryBlock</code> attribute.
	 * @return the deliveryBlock
	 */
	public Boolean isDeliveryBlock(final AbstractOrder item)
	{
		return isDeliveryBlock( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryBlock</code> attribute. 
	 * @return the deliveryBlock
	 */
	public boolean isDeliveryBlockAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Boolean value = isDeliveryBlock( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryBlock</code> attribute. 
	 * @return the deliveryBlock
	 */
	public boolean isDeliveryBlockAsPrimitive(final AbstractOrder item)
	{
		return isDeliveryBlockAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.deliveryBlock</code> attribute. 
	 * @param value the deliveryBlock
	 */
	public void setDeliveryBlock(final SessionContext ctx, final AbstractOrder item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.DELIVERYBLOCK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.deliveryBlock</code> attribute. 
	 * @param value the deliveryBlock
	 */
	public void setDeliveryBlock(final AbstractOrder item, final Boolean value)
	{
		setDeliveryBlock( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.deliveryBlock</code> attribute. 
	 * @param value the deliveryBlock
	 */
	public void setDeliveryBlock(final SessionContext ctx, final AbstractOrder item, final boolean value)
	{
		setDeliveryBlock( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.deliveryBlock</code> attribute. 
	 * @param value the deliveryBlock
	 */
	public void setDeliveryBlock(final AbstractOrder item, final boolean value)
	{
		setDeliveryBlock( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.deliveryPriority</code> attribute.
	 * @return the deliveryPriority - delivery Priority
	 */
	public String getDeliveryPriority(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.DELIVERYPRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.deliveryPriority</code> attribute.
	 * @return the deliveryPriority - delivery Priority
	 */
	public String getDeliveryPriority(final SAPConfiguration item)
	{
		return getDeliveryPriority( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.deliveryPriority</code> attribute. 
	 * @param value the deliveryPriority - delivery Priority
	 */
	public void setDeliveryPriority(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.DELIVERYPRIORITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.deliveryPriority</code> attribute. 
	 * @param value the deliveryPriority - delivery Priority
	 */
	public void setDeliveryPriority(final SAPConfiguration item, final String value)
	{
		setDeliveryPriority( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final AbstractOrderEntry item)
	{
		return getDescription( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final AbstractOrderEntry item, final String value)
	{
		setDescription( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.dimensionUnit</code> attribute.
	 * @return the dimensionUnit - Indicates the weight of product
	 */
	public EnumerationValue getDimensionUnit(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.DIMENSIONUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.dimensionUnit</code> attribute.
	 * @return the dimensionUnit - Indicates the weight of product
	 */
	public EnumerationValue getDimensionUnit(final Product item)
	{
		return getDimensionUnit( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.dimensionUnit</code> attribute. 
	 * @param value the dimensionUnit - Indicates the weight of product
	 */
	public void setDimensionUnit(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.DIMENSIONUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.dimensionUnit</code> attribute. 
	 * @param value the dimensionUnit - Indicates the weight of product
	 */
	public void setDimensionUnit(final Product item, final EnumerationValue value)
	{
		setDimensionUnit( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.discontinuedDate</code> attribute.
	 * @return the discontinuedDate - Data on which the product will be discontinued
	 */
	public Date getDiscontinuedDate(final SessionContext ctx, final Product item)
	{
		return (Date)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.DISCONTINUEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.discontinuedDate</code> attribute.
	 * @return the discontinuedDate - Data on which the product will be discontinued
	 */
	public Date getDiscontinuedDate(final Product item)
	{
		return getDiscontinuedDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.discontinuedDate</code> attribute. 
	 * @param value the discontinuedDate - Data on which the product will be discontinued
	 */
	public void setDiscontinuedDate(final SessionContext ctx, final Product item, final Date value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.DISCONTINUEDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.discontinuedDate</code> attribute. 
	 * @param value the discontinuedDate - Data on which the product will be discontinued
	 */
	public void setDiscontinuedDate(final Product item, final Date value)
	{
		setDiscontinuedDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.discount</code> attribute.
	 * @return the discount
	 */
	public String getDiscount(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.DISCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.discount</code> attribute.
	 * @return the discount
	 */
	public String getDiscount(final B2BUnit item)
	{
		return getDiscount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.discount</code> attribute. 
	 * @param value the discount
	 */
	public void setDiscount(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.DISCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.discount</code> attribute. 
	 * @param value the discount
	 */
	public void setDiscount(final B2BUnit item, final String value)
	{
		setDiscount( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.discountCash</code> attribute.
	 * @return the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public Boolean isDiscountCash(final SessionContext ctx, final PromotionSourceRule item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PromotionSourceRule.DISCOUNTCASH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.discountCash</code> attribute.
	 * @return the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public Boolean isDiscountCash(final PromotionSourceRule item)
	{
		return isDiscountCash( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.discountCash</code> attribute. 
	 * @return the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public boolean isDiscountCashAsPrimitive(final SessionContext ctx, final PromotionSourceRule item)
	{
		Boolean value = isDiscountCash( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.discountCash</code> attribute. 
	 * @return the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public boolean isDiscountCashAsPrimitive(final PromotionSourceRule item)
	{
		return isDiscountCashAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.discountCash</code> attribute. 
	 * @param value the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public void setDiscountCash(final SessionContext ctx, final PromotionSourceRule item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PromotionSourceRule.DISCOUNTCASH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.discountCash</code> attribute. 
	 * @param value the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public void setDiscountCash(final PromotionSourceRule item, final Boolean value)
	{
		setDiscountCash( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.discountCash</code> attribute. 
	 * @param value the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public void setDiscountCash(final SessionContext ctx, final PromotionSourceRule item, final boolean value)
	{
		setDiscountCash( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.discountCash</code> attribute. 
	 * @param value the discountCash - flag to indicate whether this promotion type will be Fixed
	 */
	public void setDiscountCash(final PromotionSourceRule item, final boolean value)
	{
		setDiscountCash( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.doseNote</code> attribute.
	 * @return the doseNote
	 */
	public String getDoseNote(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.DOSENOTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.doseNote</code> attribute.
	 * @return the doseNote
	 */
	public String getDoseNote(final AbstractOrderEntry item)
	{
		return getDoseNote( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.doseNote</code> attribute. 
	 * @param value the doseNote
	 */
	public void setDoseNote(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.DOSENOTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.doseNote</code> attribute. 
	 * @param value the doseNote
	 */
	public void setDoseNote(final AbstractOrderEntry item, final String value)
	{
		setDoseNote( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.email</code> attribute.
	 * @return the email
	 */
	public String getEmail(final SessionContext ctx, final PointOfService item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PointOfService.EMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.email</code> attribute.
	 * @return the email
	 */
	public String getEmail(final PointOfService item)
	{
		return getEmail( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.email</code> attribute. 
	 * @param value the email
	 */
	public void setEmail(final SessionContext ctx, final PointOfService item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PointOfService.EMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.email</code> attribute. 
	 * @param value the email
	 */
	public void setEmail(final PointOfService item, final String value)
	{
		setEmail( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.estoreCardToken</code> attribute.
	 * @return the estoreCardToken
	 */
	public String getEstoreCardToken(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.ESTORECARDTOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.estoreCardToken</code> attribute.
	 * @return the estoreCardToken
	 */
	public String getEstoreCardToken(final B2BUnit item)
	{
		return getEstoreCardToken( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.estoreCardToken</code> attribute. 
	 * @param value the estoreCardToken
	 */
	public void setEstoreCardToken(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.ESTORECARDTOKEN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.estoreCardToken</code> attribute. 
	 * @param value the estoreCardToken
	 */
	public void setEstoreCardToken(final B2BUnit item, final String value)
	{
		setEstoreCardToken( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.externalOrder</code> attribute.
	 * @return the externalOrder
	 */
	public Boolean isExternalOrder(final SessionContext ctx, final AbstractOrder item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.EXTERNALORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.externalOrder</code> attribute.
	 * @return the externalOrder
	 */
	public Boolean isExternalOrder(final AbstractOrder item)
	{
		return isExternalOrder( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.externalOrder</code> attribute. 
	 * @return the externalOrder
	 */
	public boolean isExternalOrderAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Boolean value = isExternalOrder( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.externalOrder</code> attribute. 
	 * @return the externalOrder
	 */
	public boolean isExternalOrderAsPrimitive(final AbstractOrder item)
	{
		return isExternalOrderAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.externalOrder</code> attribute. 
	 * @param value the externalOrder
	 */
	public void setExternalOrder(final SessionContext ctx, final AbstractOrder item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.EXTERNALORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.externalOrder</code> attribute. 
	 * @param value the externalOrder
	 */
	public void setExternalOrder(final AbstractOrder item, final Boolean value)
	{
		setExternalOrder( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.externalOrder</code> attribute. 
	 * @param value the externalOrder
	 */
	public void setExternalOrder(final SessionContext ctx, final AbstractOrder item, final boolean value)
	{
		setExternalOrder( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.externalOrder</code> attribute. 
	 * @param value the externalOrder
	 */
	public void setExternalOrder(final AbstractOrder item, final boolean value)
	{
		setExternalOrder( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute.
	 * @return the FailedAccertifyCheck
	 */
	public Boolean isFailedAccertifyCheck(final SessionContext ctx, final B2BCustomer item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.FAILEDACCERTIFYCHECK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute.
	 * @return the FailedAccertifyCheck
	 */
	public Boolean isFailedAccertifyCheck(final B2BCustomer item)
	{
		return isFailedAccertifyCheck( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute. 
	 * @return the FailedAccertifyCheck
	 */
	public boolean isFailedAccertifyCheckAsPrimitive(final SessionContext ctx, final B2BCustomer item)
	{
		Boolean value = isFailedAccertifyCheck( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute. 
	 * @return the FailedAccertifyCheck
	 */
	public boolean isFailedAccertifyCheckAsPrimitive(final B2BCustomer item)
	{
		return isFailedAccertifyCheckAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute. 
	 * @param value the FailedAccertifyCheck
	 */
	public void setFailedAccertifyCheck(final SessionContext ctx, final B2BCustomer item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.FAILEDACCERTIFYCHECK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute. 
	 * @param value the FailedAccertifyCheck
	 */
	public void setFailedAccertifyCheck(final B2BCustomer item, final Boolean value)
	{
		setFailedAccertifyCheck( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute. 
	 * @param value the FailedAccertifyCheck
	 */
	public void setFailedAccertifyCheck(final SessionContext ctx, final B2BCustomer item, final boolean value)
	{
		setFailedAccertifyCheck( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.FailedAccertifyCheck</code> attribute. 
	 * @param value the FailedAccertifyCheck
	 */
	public void setFailedAccertifyCheck(final B2BCustomer item, final boolean value)
	{
		setFailedAccertifyCheck( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.faq</code> attribute.
	 * @return the faq
	 */
	public Collection<WholescriptsFaq> getFaq(final SessionContext ctx, final PrincipalGroup item)
	{
		final List<WholescriptsFaq> items = item.getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			"WholescriptsFaq",
			null,
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.faq</code> attribute.
	 * @return the faq
	 */
	public Collection<WholescriptsFaq> getFaq(final PrincipalGroup item)
	{
		return getFaq( getSession().getSessionContext(), item );
	}
	
	public long getFaqCount(final SessionContext ctx, final PrincipalGroup item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			"WholescriptsFaq",
			null
		);
	}
	
	public long getFaqCount(final PrincipalGroup item)
	{
		return getFaqCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PrincipalGroup.faq</code> attribute. 
	 * @param value the faq
	 */
	public void setFaq(final SessionContext ctx, final PrincipalGroup item, final Collection<WholescriptsFaq> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PrincipalGroup.faq</code> attribute. 
	 * @param value the faq
	 */
	public void setFaq(final PrincipalGroup item, final Collection<WholescriptsFaq> value)
	{
		setFaq( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to faq. 
	 * @param value the item to add to faq
	 */
	public void addToFaq(final SessionContext ctx, final PrincipalGroup item, final WholescriptsFaq value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to faq. 
	 * @param value the item to add to faq
	 */
	public void addToFaq(final PrincipalGroup item, final WholescriptsFaq value)
	{
		addToFaq( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from faq. 
	 * @param value the item to remove from faq
	 */
	public void removeFromFaq(final SessionContext ctx, final PrincipalGroup item, final WholescriptsFaq value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from faq. 
	 * @param value the item to remove from faq
	 */
	public void removeFromFaq(final PrincipalGroup item, final WholescriptsFaq value)
	{
		removeFromFaq( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.faqList</code> attribute.
	 * @return the faqList
	 */
	public Collection<WholescriptsFaqList> getFaqList(final SessionContext ctx, final PrincipalGroup item)
	{
		final List<WholescriptsFaqList> items = item.getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			"WholescriptsFaqList",
			null,
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.faqList</code> attribute.
	 * @return the faqList
	 */
	public Collection<WholescriptsFaqList> getFaqList(final PrincipalGroup item)
	{
		return getFaqList( getSession().getSessionContext(), item );
	}
	
	public long getFaqListCount(final SessionContext ctx, final PrincipalGroup item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			"WholescriptsFaqList",
			null
		);
	}
	
	public long getFaqListCount(final PrincipalGroup item)
	{
		return getFaqListCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PrincipalGroup.faqList</code> attribute. 
	 * @param value the faqList
	 */
	public void setFaqList(final SessionContext ctx, final PrincipalGroup item, final Collection<WholescriptsFaqList> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PrincipalGroup.faqList</code> attribute. 
	 * @param value the faqList
	 */
	public void setFaqList(final PrincipalGroup item, final Collection<WholescriptsFaqList> value)
	{
		setFaqList( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to faqList. 
	 * @param value the item to add to faqList
	 */
	public void addToFaqList(final SessionContext ctx, final PrincipalGroup item, final WholescriptsFaqList value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to faqList. 
	 * @param value the item to add to faqList
	 */
	public void addToFaqList(final PrincipalGroup item, final WholescriptsFaqList value)
	{
		addToFaqList( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from faqList. 
	 * @param value the item to remove from faqList
	 */
	public void removeFromFaqList(final SessionContext ctx, final PrincipalGroup item, final WholescriptsFaqList value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from faqList. 
	 * @param value the item to remove from faqList
	 */
	public void removeFromFaqList(final PrincipalGroup item, final WholescriptsFaqList value)
	{
		removeFromFaqList( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.genericPLName</code> attribute.
	 * @return the genericPLName - Generic PL name(Ind. Std. description)
	 */
	public String getGenericPLName(final SessionContext ctx, final Product item)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedProduct.getGenericPLName requires a session language", 0 );
		}
		return (String)item.getLocalizedProperty( ctx, WholescriptscoreConstants.Attributes.Product.GENERICPLNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.genericPLName</code> attribute.
	 * @return the genericPLName - Generic PL name(Ind. Std. description)
	 */
	public String getGenericPLName(final Product item)
	{
		return getGenericPLName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.genericPLName</code> attribute. 
	 * @return the localized genericPLName - Generic PL name(Ind. Std. description)
	 */
	public Map<Language,String> getAllGenericPLName(final SessionContext ctx, final Product item)
	{
		return (Map<Language,String>)item.getAllLocalizedProperties(ctx,WholescriptscoreConstants.Attributes.Product.GENERICPLNAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.genericPLName</code> attribute. 
	 * @return the localized genericPLName - Generic PL name(Ind. Std. description)
	 */
	public Map<Language,String> getAllGenericPLName(final Product item)
	{
		return getAllGenericPLName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.genericPLName</code> attribute. 
	 * @param value the genericPLName - Generic PL name(Ind. Std. description)
	 */
	public void setGenericPLName(final SessionContext ctx, final Product item, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedProduct.setGenericPLName requires a session language", 0 );
		}
		item.setLocalizedProperty(ctx, WholescriptscoreConstants.Attributes.Product.GENERICPLNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.genericPLName</code> attribute. 
	 * @param value the genericPLName - Generic PL name(Ind. Std. description)
	 */
	public void setGenericPLName(final Product item, final String value)
	{
		setGenericPLName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.genericPLName</code> attribute. 
	 * @param value the genericPLName - Generic PL name(Ind. Std. description)
	 */
	public void setAllGenericPLName(final SessionContext ctx, final Product item, final Map<Language,String> value)
	{
		item.setAllLocalizedProperties(ctx,WholescriptscoreConstants.Attributes.Product.GENERICPLNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.genericPLName</code> attribute. 
	 * @param value the genericPLName - Generic PL name(Ind. Std. description)
	 */
	public void setAllGenericPLName(final Product item, final Map<Language,String> value)
	{
		setAllGenericPLName( getSession().getSessionContext(), item, value );
	}
	
	public static final WholescriptscoreManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (WholescriptscoreManager) em.getExtension(WholescriptscoreConstants.EXTENSIONNAME);
	}
	
	@Override
	public String getName()
	{
		return WholescriptscoreConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.height</code> attribute.
	 * @return the height - Indicates the weight of product
	 */
	public String getHeight(final SessionContext ctx, final Product item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.HEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.height</code> attribute.
	 * @return the height - Indicates the weight of product
	 */
	public String getHeight(final Product item)
	{
		return getHeight( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.height</code> attribute. 
	 * @param value the height - Indicates the weight of product
	 */
	public void setHeight(final SessionContext ctx, final Product item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.HEIGHT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.height</code> attribute. 
	 * @param value the height - Indicates the weight of product
	 */
	public void setHeight(final Product item, final String value)
	{
		setHeight( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.incoterms</code> attribute.
	 * @return the incoterms - inco terms
	 */
	public String getIncoterms(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.INCOTERMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.incoterms</code> attribute.
	 * @return the incoterms - inco terms
	 */
	public String getIncoterms(final SAPConfiguration item)
	{
		return getIncoterms( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.incoterms</code> attribute. 
	 * @param value the incoterms - inco terms
	 */
	public void setIncoterms(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.INCOTERMS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.incoterms</code> attribute. 
	 * @param value the incoterms - inco terms
	 */
	public void setIncoterms(final SAPConfiguration item, final String value)
	{
		setIncoterms( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.ingredientReferences</code> attribute.
	 * @return the ingredientReferences
	 */
	public Collection<IngredientReference> getIngredientReferences(final SessionContext ctx, final Product item)
	{
		return INGREDIENTREFERENCERELATIONINGREDIENTREFERENCESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.ingredientReferences</code> attribute.
	 * @return the ingredientReferences
	 */
	public Collection<IngredientReference> getIngredientReferences(final Product item)
	{
		return getIngredientReferences( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.ingredientReferences</code> attribute. 
	 * @param value the ingredientReferences
	 */
	public void setIngredientReferences(final SessionContext ctx, final Product item, final Collection<IngredientReference> value)
	{
		INGREDIENTREFERENCERELATIONINGREDIENTREFERENCESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.ingredientReferences</code> attribute. 
	 * @param value the ingredientReferences
	 */
	public void setIngredientReferences(final Product item, final Collection<IngredientReference> value)
	{
		setIngredientReferences( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ingredientReferences. 
	 * @param value the item to add to ingredientReferences
	 */
	public void addToIngredientReferences(final SessionContext ctx, final Product item, final IngredientReference value)
	{
		INGREDIENTREFERENCERELATIONINGREDIENTREFERENCESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to ingredientReferences. 
	 * @param value the item to add to ingredientReferences
	 */
	public void addToIngredientReferences(final Product item, final IngredientReference value)
	{
		addToIngredientReferences( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ingredientReferences. 
	 * @param value the item to remove from ingredientReferences
	 */
	public void removeFromIngredientReferences(final SessionContext ctx, final Product item, final IngredientReference value)
	{
		INGREDIENTREFERENCERELATIONINGREDIENTREFERENCESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from ingredientReferences. 
	 * @param value the item to remove from ingredientReferences
	 */
	public void removeFromIngredientReferences(final Product item, final IngredientReference value)
	{
		removeFromIngredientReferences( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.inquiryId</code> attribute.
	 * @return the inquiryId
	 */
	public String getInquiryId(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.INQUIRYID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.inquiryId</code> attribute.
	 * @return the inquiryId
	 */
	public String getInquiryId(final B2BUnit item)
	{
		return getInquiryId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.inquiryId</code> attribute. 
	 * @param value the inquiryId
	 */
	public void setInquiryId(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.INQUIRYID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.inquiryId</code> attribute. 
	 * @param value the inquiryId
	 */
	public void setInquiryId(final B2BUnit item, final String value)
	{
		setInquiryId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.instructions</code> attribute.
	 * @return the instructions
	 */
	public String getInstructions(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.INSTRUCTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.instructions</code> attribute.
	 * @return the instructions
	 */
	public String getInstructions(final AbstractOrder item)
	{
		return getInstructions( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.instructions</code> attribute. 
	 * @param value the instructions
	 */
	public void setInstructions(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.INSTRUCTIONS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.instructions</code> attribute. 
	 * @param value the instructions
	 */
	public void setInstructions(final AbstractOrder item, final String value)
	{
		setInstructions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isAmendmentSigned</code> attribute.
	 * @return the isAmendmentSigned
	 */
	public Boolean isIsAmendmentSigned(final SessionContext ctx, final B2BCustomer item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ISAMENDMENTSIGNED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isAmendmentSigned</code> attribute.
	 * @return the isAmendmentSigned
	 */
	public Boolean isIsAmendmentSigned(final B2BCustomer item)
	{
		return isIsAmendmentSigned( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isAmendmentSigned</code> attribute. 
	 * @return the isAmendmentSigned
	 */
	public boolean isIsAmendmentSignedAsPrimitive(final SessionContext ctx, final B2BCustomer item)
	{
		Boolean value = isIsAmendmentSigned( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isAmendmentSigned</code> attribute. 
	 * @return the isAmendmentSigned
	 */
	public boolean isIsAmendmentSignedAsPrimitive(final B2BCustomer item)
	{
		return isIsAmendmentSignedAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isAmendmentSigned</code> attribute. 
	 * @param value the isAmendmentSigned
	 */
	public void setIsAmendmentSigned(final SessionContext ctx, final B2BCustomer item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ISAMENDMENTSIGNED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isAmendmentSigned</code> attribute. 
	 * @param value the isAmendmentSigned
	 */
	public void setIsAmendmentSigned(final B2BCustomer item, final Boolean value)
	{
		setIsAmendmentSigned( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isAmendmentSigned</code> attribute. 
	 * @param value the isAmendmentSigned
	 */
	public void setIsAmendmentSigned(final SessionContext ctx, final B2BCustomer item, final boolean value)
	{
		setIsAmendmentSigned( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isAmendmentSigned</code> attribute. 
	 * @param value the isAmendmentSigned
	 */
	public void setIsAmendmentSigned(final B2BCustomer item, final boolean value)
	{
		setIsAmendmentSigned( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute.
	 * @return the isEmailNotificationEnabled
	 */
	public Boolean isIsEmailNotificationEnabled(final SessionContext ctx, final B2BCustomer item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ISEMAILNOTIFICATIONENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute.
	 * @return the isEmailNotificationEnabled
	 */
	public Boolean isIsEmailNotificationEnabled(final B2BCustomer item)
	{
		return isIsEmailNotificationEnabled( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute. 
	 * @return the isEmailNotificationEnabled
	 */
	public boolean isIsEmailNotificationEnabledAsPrimitive(final SessionContext ctx, final B2BCustomer item)
	{
		Boolean value = isIsEmailNotificationEnabled( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute. 
	 * @return the isEmailNotificationEnabled
	 */
	public boolean isIsEmailNotificationEnabledAsPrimitive(final B2BCustomer item)
	{
		return isIsEmailNotificationEnabledAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final SessionContext ctx, final B2BCustomer item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ISEMAILNOTIFICATIONENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final B2BCustomer item, final Boolean value)
	{
		setIsEmailNotificationEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final SessionContext ctx, final B2BCustomer item, final boolean value)
	{
		setIsEmailNotificationEnabled( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final B2BCustomer item, final boolean value)
	{
		setIsEmailNotificationEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isFeatured</code> attribute.
	 * @return the isFeatured - Is the product a featured Product
	 */
	public Boolean isIsFeatured(final SessionContext ctx, final Product item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.ISFEATURED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isFeatured</code> attribute.
	 * @return the isFeatured - Is the product a featured Product
	 */
	public Boolean isIsFeatured(final Product item)
	{
		return isIsFeatured( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isFeatured</code> attribute. 
	 * @return the isFeatured - Is the product a featured Product
	 */
	public boolean isIsFeaturedAsPrimitive(final SessionContext ctx, final Product item)
	{
		Boolean value = isIsFeatured( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isFeatured</code> attribute. 
	 * @return the isFeatured - Is the product a featured Product
	 */
	public boolean isIsFeaturedAsPrimitive(final Product item)
	{
		return isIsFeaturedAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isFeatured</code> attribute. 
	 * @param value the isFeatured - Is the product a featured Product
	 */
	public void setIsFeatured(final SessionContext ctx, final Product item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.ISFEATURED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isFeatured</code> attribute. 
	 * @param value the isFeatured - Is the product a featured Product
	 */
	public void setIsFeatured(final Product item, final Boolean value)
	{
		setIsFeatured( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isFeatured</code> attribute. 
	 * @param value the isFeatured - Is the product a featured Product
	 */
	public void setIsFeatured(final SessionContext ctx, final Product item, final boolean value)
	{
		setIsFeatured( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isFeatured</code> attribute. 
	 * @param value the isFeatured - Is the product a featured Product
	 */
	public void setIsFeatured(final Product item, final boolean value)
	{
		setIsFeatured( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.isHidden</code> attribute.
	 * @return the isHidden - Flag for hidden Carton entry
	 */
	public Boolean isIsHidden(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.ISHIDDEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.isHidden</code> attribute.
	 * @return the isHidden - Flag for hidden Carton entry
	 */
	public Boolean isIsHidden(final AbstractOrderEntry item)
	{
		return isIsHidden( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.isHidden</code> attribute. 
	 * @return the isHidden - Flag for hidden Carton entry
	 */
	public boolean isIsHiddenAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isIsHidden( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.isHidden</code> attribute. 
	 * @return the isHidden - Flag for hidden Carton entry
	 */
	public boolean isIsHiddenAsPrimitive(final AbstractOrderEntry item)
	{
		return isIsHiddenAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.isHidden</code> attribute. 
	 * @param value the isHidden - Flag for hidden Carton entry
	 */
	public void setIsHidden(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.ISHIDDEN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.isHidden</code> attribute. 
	 * @param value the isHidden - Flag for hidden Carton entry
	 */
	public void setIsHidden(final AbstractOrderEntry item, final Boolean value)
	{
		setIsHidden( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.isHidden</code> attribute. 
	 * @param value the isHidden - Flag for hidden Carton entry
	 */
	public void setIsHidden(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setIsHidden( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.isHidden</code> attribute. 
	 * @param value the isHidden - Flag for hidden Carton entry
	 */
	public void setIsHidden(final AbstractOrderEntry item, final boolean value)
	{
		setIsHidden( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isMedpax</code> attribute.
	 * @return the isMedpax - Is the product a Medpax Product
	 */
	public Boolean isIsMedpax(final SessionContext ctx, final Product item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.ISMEDPAX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isMedpax</code> attribute.
	 * @return the isMedpax - Is the product a Medpax Product
	 */
	public Boolean isIsMedpax(final Product item)
	{
		return isIsMedpax( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isMedpax</code> attribute. 
	 * @return the isMedpax - Is the product a Medpax Product
	 */
	public boolean isIsMedpaxAsPrimitive(final SessionContext ctx, final Product item)
	{
		Boolean value = isIsMedpax( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isMedpax</code> attribute. 
	 * @return the isMedpax - Is the product a Medpax Product
	 */
	public boolean isIsMedpaxAsPrimitive(final Product item)
	{
		return isIsMedpaxAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isMedpax</code> attribute. 
	 * @param value the isMedpax - Is the product a Medpax Product
	 */
	public void setIsMedpax(final SessionContext ctx, final Product item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.ISMEDPAX,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isMedpax</code> attribute. 
	 * @param value the isMedpax - Is the product a Medpax Product
	 */
	public void setIsMedpax(final Product item, final Boolean value)
	{
		setIsMedpax( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isMedpax</code> attribute. 
	 * @param value the isMedpax - Is the product a Medpax Product
	 */
	public void setIsMedpax(final SessionContext ctx, final Product item, final boolean value)
	{
		setIsMedpax( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isMedpax</code> attribute. 
	 * @param value the isMedpax - Is the product a Medpax Product
	 */
	public void setIsMedpax(final Product item, final boolean value)
	{
		setIsMedpax( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isNew</code> attribute.
	 * @return the isNew - Is the product a New Product
	 */
	public Boolean isIsNew(final SessionContext ctx, final Product item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.ISNEW);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isNew</code> attribute.
	 * @return the isNew - Is the product a New Product
	 */
	public Boolean isIsNew(final Product item)
	{
		return isIsNew( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isNew</code> attribute. 
	 * @return the isNew - Is the product a New Product
	 */
	public boolean isIsNewAsPrimitive(final SessionContext ctx, final Product item)
	{
		Boolean value = isIsNew( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.isNew</code> attribute. 
	 * @return the isNew - Is the product a New Product
	 */
	public boolean isIsNewAsPrimitive(final Product item)
	{
		return isIsNewAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isNew</code> attribute. 
	 * @param value the isNew - Is the product a New Product
	 */
	public void setIsNew(final SessionContext ctx, final Product item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.ISNEW,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isNew</code> attribute. 
	 * @param value the isNew - Is the product a New Product
	 */
	public void setIsNew(final Product item, final Boolean value)
	{
		setIsNew( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isNew</code> attribute. 
	 * @param value the isNew - Is the product a New Product
	 */
	public void setIsNew(final SessionContext ctx, final Product item, final boolean value)
	{
		setIsNew( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.isNew</code> attribute. 
	 * @param value the isNew - Is the product a New Product
	 */
	public void setIsNew(final Product item, final boolean value)
	{
		setIsNew( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute.
	 * @return the isNotificationSettingsEnabled
	 */
	public Boolean isIsNotificationSettingsEnabled(final SessionContext ctx, final BaseStore item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.BaseStore.ISNOTIFICATIONSETTINGSENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute.
	 * @return the isNotificationSettingsEnabled
	 */
	public Boolean isIsNotificationSettingsEnabled(final BaseStore item)
	{
		return isIsNotificationSettingsEnabled( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute. 
	 * @return the isNotificationSettingsEnabled
	 */
	public boolean isIsNotificationSettingsEnabledAsPrimitive(final SessionContext ctx, final BaseStore item)
	{
		Boolean value = isIsNotificationSettingsEnabled( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute. 
	 * @return the isNotificationSettingsEnabled
	 */
	public boolean isIsNotificationSettingsEnabledAsPrimitive(final BaseStore item)
	{
		return isIsNotificationSettingsEnabledAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute. 
	 * @param value the isNotificationSettingsEnabled
	 */
	public void setIsNotificationSettingsEnabled(final SessionContext ctx, final BaseStore item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.BaseStore.ISNOTIFICATIONSETTINGSENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute. 
	 * @param value the isNotificationSettingsEnabled
	 */
	public void setIsNotificationSettingsEnabled(final BaseStore item, final Boolean value)
	{
		setIsNotificationSettingsEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute. 
	 * @param value the isNotificationSettingsEnabled
	 */
	public void setIsNotificationSettingsEnabled(final SessionContext ctx, final BaseStore item, final boolean value)
	{
		setIsNotificationSettingsEnabled( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.isNotificationSettingsEnabled</code> attribute. 
	 * @param value the isNotificationSettingsEnabled
	 */
	public void setIsNotificationSettingsEnabled(final BaseStore item, final boolean value)
	{
		setIsNotificationSettingsEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.isPatientDirect</code> attribute.
	 * @return the isPatientDirect - Flag whether it is a patient direct
	 */
	public Boolean isIsPatientDirect(final SessionContext ctx, final B2BUnit item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.ISPATIENTDIRECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.isPatientDirect</code> attribute.
	 * @return the isPatientDirect - Flag whether it is a patient direct
	 */
	public Boolean isIsPatientDirect(final B2BUnit item)
	{
		return isIsPatientDirect( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.isPatientDirect</code> attribute. 
	 * @return the isPatientDirect - Flag whether it is a patient direct
	 */
	public boolean isIsPatientDirectAsPrimitive(final SessionContext ctx, final B2BUnit item)
	{
		Boolean value = isIsPatientDirect( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.isPatientDirect</code> attribute. 
	 * @return the isPatientDirect - Flag whether it is a patient direct
	 */
	public boolean isIsPatientDirectAsPrimitive(final B2BUnit item)
	{
		return isIsPatientDirectAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.isPatientDirect</code> attribute. 
	 * @param value the isPatientDirect - Flag whether it is a patient direct
	 */
	public void setIsPatientDirect(final SessionContext ctx, final B2BUnit item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.ISPATIENTDIRECT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.isPatientDirect</code> attribute. 
	 * @param value the isPatientDirect - Flag whether it is a patient direct
	 */
	public void setIsPatientDirect(final B2BUnit item, final Boolean value)
	{
		setIsPatientDirect( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.isPatientDirect</code> attribute. 
	 * @param value the isPatientDirect - Flag whether it is a patient direct
	 */
	public void setIsPatientDirect(final SessionContext ctx, final B2BUnit item, final boolean value)
	{
		setIsPatientDirect( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.isPatientDirect</code> attribute. 
	 * @param value the isPatientDirect - Flag whether it is a patient direct
	 */
	public void setIsPatientDirect(final B2BUnit item, final boolean value)
	{
		setIsPatientDirect( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Region.isShippingTAxWarningEnabled</code> attribute.
	 * @return the isShippingTAxWarningEnabled
	 */
	public Boolean isIsShippingTAxWarningEnabled(final SessionContext ctx, final Region item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Region.ISSHIPPINGTAXWARNINGENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Region.isShippingTAxWarningEnabled</code> attribute.
	 * @return the isShippingTAxWarningEnabled
	 */
	public Boolean isIsShippingTAxWarningEnabled(final Region item)
	{
		return isIsShippingTAxWarningEnabled( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Region.isShippingTAxWarningEnabled</code> attribute. 
	 * @return the isShippingTAxWarningEnabled
	 */
	public boolean isIsShippingTAxWarningEnabledAsPrimitive(final SessionContext ctx, final Region item)
	{
		Boolean value = isIsShippingTAxWarningEnabled( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Region.isShippingTAxWarningEnabled</code> attribute. 
	 * @return the isShippingTAxWarningEnabled
	 */
	public boolean isIsShippingTAxWarningEnabledAsPrimitive(final Region item)
	{
		return isIsShippingTAxWarningEnabledAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Region.isShippingTAxWarningEnabled</code> attribute. 
	 * @param value the isShippingTAxWarningEnabled
	 */
	public void setIsShippingTAxWarningEnabled(final SessionContext ctx, final Region item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Region.ISSHIPPINGTAXWARNINGENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Region.isShippingTAxWarningEnabled</code> attribute. 
	 * @param value the isShippingTAxWarningEnabled
	 */
	public void setIsShippingTAxWarningEnabled(final Region item, final Boolean value)
	{
		setIsShippingTAxWarningEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Region.isShippingTAxWarningEnabled</code> attribute. 
	 * @param value the isShippingTAxWarningEnabled
	 */
	public void setIsShippingTAxWarningEnabled(final SessionContext ctx, final Region item, final boolean value)
	{
		setIsShippingTAxWarningEnabled( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Region.isShippingTAxWarningEnabled</code> attribute. 
	 * @param value the isShippingTAxWarningEnabled
	 */
	public void setIsShippingTAxWarningEnabled(final Region item, final boolean value)
	{
		setIsShippingTAxWarningEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute.
	 * @return the isSmsNotificationEnabled
	 */
	public Boolean isIsSmsNotificationEnabled(final SessionContext ctx, final B2BCustomer item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ISSMSNOTIFICATIONENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute.
	 * @return the isSmsNotificationEnabled
	 */
	public Boolean isIsSmsNotificationEnabled(final B2BCustomer item)
	{
		return isIsSmsNotificationEnabled( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute. 
	 * @return the isSmsNotificationEnabled
	 */
	public boolean isIsSmsNotificationEnabledAsPrimitive(final SessionContext ctx, final B2BCustomer item)
	{
		Boolean value = isIsSmsNotificationEnabled( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute. 
	 * @return the isSmsNotificationEnabled
	 */
	public boolean isIsSmsNotificationEnabledAsPrimitive(final B2BCustomer item)
	{
		return isIsSmsNotificationEnabledAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final SessionContext ctx, final B2BCustomer item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.ISSMSNOTIFICATIONENABLED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final B2BCustomer item, final Boolean value)
	{
		setIsSmsNotificationEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final SessionContext ctx, final B2BCustomer item, final boolean value)
	{
		setIsSmsNotificationEnabled( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final B2BCustomer item, final boolean value)
	{
		setIsSmsNotificationEnabled( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.length</code> attribute.
	 * @return the length - Indicates the weight of product
	 */
	public String getLength(final SessionContext ctx, final Product item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.LENGTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.length</code> attribute.
	 * @return the length - Indicates the weight of product
	 */
	public String getLength(final Product item)
	{
		return getLength( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.length</code> attribute. 
	 * @param value the length - Indicates the weight of product
	 */
	public void setLength(final SessionContext ctx, final Product item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.LENGTH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.length</code> attribute. 
	 * @param value the length - Indicates the weight of product
	 */
	public void setLength(final Product item, final String value)
	{
		setLength( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.maximum</code> attribute.
	 * @return the maximum
	 */
	public Double getMaximum(final SessionContext ctx, final ZoneDeliveryModeValue item)
	{
		return (Double)item.getProperty( ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryModeValue.MAXIMUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.maximum</code> attribute.
	 * @return the maximum
	 */
	public Double getMaximum(final ZoneDeliveryModeValue item)
	{
		return getMaximum( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.maximum</code> attribute. 
	 * @return the maximum
	 */
	public double getMaximumAsPrimitive(final SessionContext ctx, final ZoneDeliveryModeValue item)
	{
		Double value = getMaximum( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.maximum</code> attribute. 
	 * @return the maximum
	 */
	public double getMaximumAsPrimitive(final ZoneDeliveryModeValue item)
	{
		return getMaximumAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryModeValue.maximum</code> attribute. 
	 * @param value the maximum
	 */
	public void setMaximum(final SessionContext ctx, final ZoneDeliveryModeValue item, final Double value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryModeValue.MAXIMUM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryModeValue.maximum</code> attribute. 
	 * @param value the maximum
	 */
	public void setMaximum(final ZoneDeliveryModeValue item, final Double value)
	{
		setMaximum( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryModeValue.maximum</code> attribute. 
	 * @param value the maximum
	 */
	public void setMaximum(final SessionContext ctx, final ZoneDeliveryModeValue item, final double value)
	{
		setMaximum( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryModeValue.maximum</code> attribute. 
	 * @param value the maximum
	 */
	public void setMaximum(final ZoneDeliveryModeValue item, final double value)
	{
		setMaximum( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.maxPartDeliveries</code> attribute.
	 * @return the maxPartDeliveries - max Part Deliveries
	 */
	public String getMaxPartDeliveries(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.MAXPARTDELIVERIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.maxPartDeliveries</code> attribute.
	 * @return the maxPartDeliveries - max Part Deliveries
	 */
	public String getMaxPartDeliveries(final SAPConfiguration item)
	{
		return getMaxPartDeliveries( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.maxPartDeliveries</code> attribute. 
	 * @param value the maxPartDeliveries - max Part Deliveries
	 */
	public void setMaxPartDeliveries(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.MAXPARTDELIVERIES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.maxPartDeliveries</code> attribute. 
	 * @param value the maxPartDeliveries - max Part Deliveries
	 */
	public void setMaxPartDeliveries(final SAPConfiguration item, final String value)
	{
		setMaxPartDeliveries( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute.
	 * @return the msrpBasePrice
	 */
	public Double getMsrpBasePrice(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Double)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.MSRPBASEPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute.
	 * @return the msrpBasePrice
	 */
	public Double getMsrpBasePrice(final AbstractOrderEntry item)
	{
		return getMsrpBasePrice( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute. 
	 * @return the msrpBasePrice
	 */
	public double getMsrpBasePriceAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Double value = getMsrpBasePrice( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute. 
	 * @return the msrpBasePrice
	 */
	public double getMsrpBasePriceAsPrimitive(final AbstractOrderEntry item)
	{
		return getMsrpBasePriceAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute. 
	 * @param value the msrpBasePrice
	 */
	public void setMsrpBasePrice(final SessionContext ctx, final AbstractOrderEntry item, final Double value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.MSRPBASEPRICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute. 
	 * @param value the msrpBasePrice
	 */
	public void setMsrpBasePrice(final AbstractOrderEntry item, final Double value)
	{
		setMsrpBasePrice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute. 
	 * @param value the msrpBasePrice
	 */
	public void setMsrpBasePrice(final SessionContext ctx, final AbstractOrderEntry item, final double value)
	{
		setMsrpBasePrice( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpBasePrice</code> attribute. 
	 * @param value the msrpBasePrice
	 */
	public void setMsrpBasePrice(final AbstractOrderEntry item, final double value)
	{
		setMsrpBasePrice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute.
	 * @return the msrpSubTotalPrice
	 */
	public Double getMsrpSubTotalPrice(final SessionContext ctx, final AbstractOrder item)
	{
		return (Double)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.MSRPSUBTOTALPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute.
	 * @return the msrpSubTotalPrice
	 */
	public Double getMsrpSubTotalPrice(final AbstractOrder item)
	{
		return getMsrpSubTotalPrice( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute. 
	 * @return the msrpSubTotalPrice
	 */
	public double getMsrpSubTotalPriceAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Double value = getMsrpSubTotalPrice( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute. 
	 * @return the msrpSubTotalPrice
	 */
	public double getMsrpSubTotalPriceAsPrimitive(final AbstractOrder item)
	{
		return getMsrpSubTotalPriceAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute. 
	 * @param value the msrpSubTotalPrice
	 */
	public void setMsrpSubTotalPrice(final SessionContext ctx, final AbstractOrder item, final Double value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.MSRPSUBTOTALPRICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute. 
	 * @param value the msrpSubTotalPrice
	 */
	public void setMsrpSubTotalPrice(final AbstractOrder item, final Double value)
	{
		setMsrpSubTotalPrice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute. 
	 * @param value the msrpSubTotalPrice
	 */
	public void setMsrpSubTotalPrice(final SessionContext ctx, final AbstractOrder item, final double value)
	{
		setMsrpSubTotalPrice( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.msrpSubTotalPrice</code> attribute. 
	 * @param value the msrpSubTotalPrice
	 */
	public void setMsrpSubTotalPrice(final AbstractOrder item, final double value)
	{
		setMsrpSubTotalPrice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute.
	 * @return the msrpTotalPrice
	 */
	public Double getMsrpTotalPrice(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Double)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.MSRPTOTALPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute.
	 * @return the msrpTotalPrice
	 */
	public Double getMsrpTotalPrice(final AbstractOrderEntry item)
	{
		return getMsrpTotalPrice( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute. 
	 * @return the msrpTotalPrice
	 */
	public double getMsrpTotalPriceAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Double value = getMsrpTotalPrice( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute. 
	 * @return the msrpTotalPrice
	 */
	public double getMsrpTotalPriceAsPrimitive(final AbstractOrderEntry item)
	{
		return getMsrpTotalPriceAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final SessionContext ctx, final AbstractOrderEntry item, final Double value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.MSRPTOTALPRICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final AbstractOrderEntry item, final Double value)
	{
		setMsrpTotalPrice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final SessionContext ctx, final AbstractOrderEntry item, final double value)
	{
		setMsrpTotalPrice( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final AbstractOrderEntry item, final double value)
	{
		setMsrpTotalPrice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.nonDiscountable</code> attribute.
	 * @return the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public Boolean isNonDiscountable(final SessionContext ctx, final PromotionSourceRule item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PromotionSourceRule.NONDISCOUNTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.nonDiscountable</code> attribute.
	 * @return the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public Boolean isNonDiscountable(final PromotionSourceRule item)
	{
		return isNonDiscountable( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.nonDiscountable</code> attribute. 
	 * @return the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public boolean isNonDiscountableAsPrimitive(final SessionContext ctx, final PromotionSourceRule item)
	{
		Boolean value = isNonDiscountable( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionSourceRule.nonDiscountable</code> attribute. 
	 * @return the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public boolean isNonDiscountableAsPrimitive(final PromotionSourceRule item)
	{
		return isNonDiscountableAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.nonDiscountable</code> attribute. 
	 * @param value the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public void setNonDiscountable(final SessionContext ctx, final PromotionSourceRule item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PromotionSourceRule.NONDISCOUNTABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.nonDiscountable</code> attribute. 
	 * @param value the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public void setNonDiscountable(final PromotionSourceRule item, final Boolean value)
	{
		setNonDiscountable( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.nonDiscountable</code> attribute. 
	 * @param value the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public void setNonDiscountable(final SessionContext ctx, final PromotionSourceRule item, final boolean value)
	{
		setNonDiscountable( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionSourceRule.nonDiscountable</code> attribute. 
	 * @param value the nonDiscountable - flag to indicate whether this promotion will be discount for non discount material
	 */
	public void setNonDiscountable(final PromotionSourceRule item, final boolean value)
	{
		setNonDiscountable( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.patientDirectHub</code> attribute.
	 * @return the patientDirectHub
	 */
	public PatientDirectHub getPatientDirectHub(final SessionContext ctx, final B2BUnit item)
	{
		return (PatientDirectHub)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.PATIENTDIRECTHUB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.patientDirectHub</code> attribute.
	 * @return the patientDirectHub
	 */
	public PatientDirectHub getPatientDirectHub(final B2BUnit item)
	{
		return getPatientDirectHub( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.patientDirectHub</code> attribute. 
	 * @param value the patientDirectHub
	 */
	public void setPatientDirectHub(final SessionContext ctx, final B2BUnit item, final PatientDirectHub value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.PATIENTDIRECTHUB,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.patientDirectHub</code> attribute. 
	 * @param value the patientDirectHub
	 */
	public void setPatientDirectHub(final B2BUnit item, final PatientDirectHub value)
	{
		setPatientDirectHub( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.paymentricStoreId</code> attribute.
	 * @return the paymentricStoreId - Used to send this ID to paymetric for pre auth/auth
	 */
	public String getPaymentricStoreId(final SessionContext ctx, final BaseStore item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.BaseStore.PAYMENTRICSTOREID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.paymentricStoreId</code> attribute.
	 * @return the paymentricStoreId - Used to send this ID to paymetric for pre auth/auth
	 */
	public String getPaymentricStoreId(final BaseStore item)
	{
		return getPaymentricStoreId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.paymentricStoreId</code> attribute. 
	 * @param value the paymentricStoreId - Used to send this ID to paymetric for pre auth/auth
	 */
	public void setPaymentricStoreId(final SessionContext ctx, final BaseStore item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.BaseStore.PAYMENTRICSTOREID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.paymentricStoreId</code> attribute. 
	 * @param value the paymentricStoreId - Used to send this ID to paymetric for pre auth/auth
	 */
	public void setPaymentricStoreId(final BaseStore item, final String value)
	{
		setPaymentricStoreId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.phoneNumber</code> attribute.
	 * @return the phoneNumber
	 */
	public String getPhoneNumber(final SessionContext ctx, final PointOfService item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PointOfService.PHONENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.phoneNumber</code> attribute.
	 * @return the phoneNumber
	 */
	public String getPhoneNumber(final PointOfService item)
	{
		return getPhoneNumber( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.phoneNumber</code> attribute. 
	 * @param value the phoneNumber
	 */
	public void setPhoneNumber(final SessionContext ctx, final PointOfService item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PointOfService.PHONENUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.phoneNumber</code> attribute. 
	 * @param value the phoneNumber
	 */
	public void setPhoneNumber(final PointOfService item, final String value)
	{
		setPhoneNumber( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.practitioner</code> attribute.
	 * @return the practitioner
	 */
	public B2BCustomer getPractitioner(final SessionContext ctx, final PointOfService item)
	{
		return (B2BCustomer)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PointOfService.PRACTITIONER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.practitioner</code> attribute.
	 * @return the practitioner
	 */
	public B2BCustomer getPractitioner(final PointOfService item)
	{
		return getPractitioner( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.practitioner</code> attribute. 
	 * @param value the practitioner
	 */
	public void setPractitioner(final SessionContext ctx, final PointOfService item, final B2BCustomer value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PointOfService.PRACTITIONER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.practitioner</code> attribute. 
	 * @param value the practitioner
	 */
	public void setPractitioner(final PointOfService item, final B2BCustomer value)
	{
		setPractitioner( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.practitionerDetails</code> attribute.
	 * @return the practitionerDetails
	 */
	public Set<PointOfService> getPractitionerDetails(final SessionContext ctx, final B2BCustomer item)
	{
		return (Set<PointOfService>)CUSTOMER2POINTOFSERVICEPRACTITIONERDETAILSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.practitionerDetails</code> attribute.
	 * @return the practitionerDetails
	 */
	public Set<PointOfService> getPractitionerDetails(final B2BCustomer item)
	{
		return getPractitionerDetails( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.practitionerDetails</code> attribute. 
	 * @param value the practitionerDetails
	 */
	public void setPractitionerDetails(final SessionContext ctx, final B2BCustomer item, final Set<PointOfService> value)
	{
		CUSTOMER2POINTOFSERVICEPRACTITIONERDETAILSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.practitionerDetails</code> attribute. 
	 * @param value the practitionerDetails
	 */
	public void setPractitionerDetails(final B2BCustomer item, final Set<PointOfService> value)
	{
		setPractitionerDetails( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to practitionerDetails. 
	 * @param value the item to add to practitionerDetails
	 */
	public void addToPractitionerDetails(final SessionContext ctx, final B2BCustomer item, final PointOfService value)
	{
		CUSTOMER2POINTOFSERVICEPRACTITIONERDETAILSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to practitionerDetails. 
	 * @param value the item to add to practitionerDetails
	 */
	public void addToPractitionerDetails(final B2BCustomer item, final PointOfService value)
	{
		addToPractitionerDetails( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from practitionerDetails. 
	 * @param value the item to remove from practitionerDetails
	 */
	public void removeFromPractitionerDetails(final SessionContext ctx, final B2BCustomer item, final PointOfService value)
	{
		CUSTOMER2POINTOFSERVICEPRACTITIONERDETAILSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from practitionerDetails. 
	 * @param value the item to remove from practitionerDetails
	 */
	public void removeFromPractitionerDetails(final B2BCustomer item, final PointOfService value)
	{
		removeFromPractitionerDetails( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute.
	 * @return the practitionerDirectoryOptIn
	 */
	public Boolean isPractitionerDirectoryOptIn(final SessionContext ctx, final PointOfService item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PointOfService.PRACTITIONERDIRECTORYOPTIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute.
	 * @return the practitionerDirectoryOptIn
	 */
	public Boolean isPractitionerDirectoryOptIn(final PointOfService item)
	{
		return isPractitionerDirectoryOptIn( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute. 
	 * @return the practitionerDirectoryOptIn
	 */
	public boolean isPractitionerDirectoryOptInAsPrimitive(final SessionContext ctx, final PointOfService item)
	{
		Boolean value = isPractitionerDirectoryOptIn( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute. 
	 * @return the practitionerDirectoryOptIn
	 */
	public boolean isPractitionerDirectoryOptInAsPrimitive(final PointOfService item)
	{
		return isPractitionerDirectoryOptInAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute. 
	 * @param value the practitionerDirectoryOptIn
	 */
	public void setPractitionerDirectoryOptIn(final SessionContext ctx, final PointOfService item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PointOfService.PRACTITIONERDIRECTORYOPTIN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute. 
	 * @param value the practitionerDirectoryOptIn
	 */
	public void setPractitionerDirectoryOptIn(final PointOfService item, final Boolean value)
	{
		setPractitionerDirectoryOptIn( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute. 
	 * @param value the practitionerDirectoryOptIn
	 */
	public void setPractitionerDirectoryOptIn(final SessionContext ctx, final PointOfService item, final boolean value)
	{
		setPractitionerDirectoryOptIn( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.practitionerDirectoryOptIn</code> attribute. 
	 * @param value the practitionerDirectoryOptIn
	 */
	public void setPractitionerDirectoryOptIn(final PointOfService item, final boolean value)
	{
		setPractitionerDirectoryOptIn( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.productDescription</code> attribute.
	 * @return the productDescription
	 */
	public String getProductDescription(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.PRODUCTDESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.productDescription</code> attribute.
	 * @return the productDescription
	 */
	public String getProductDescription(final AbstractOrderEntry item)
	{
		return getProductDescription( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.productDescription</code> attribute. 
	 * @param value the productDescription
	 */
	public void setProductDescription(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.PRODUCTDESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.productDescription</code> attribute. 
	 * @param value the productDescription
	 */
	public void setProductDescription(final AbstractOrderEntry item, final String value)
	{
		setProductDescription( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.promotion</code> attribute.
	 * @return the promotion
	 */
	public Collection<AbstractPromotion> getPromotion(final SessionContext ctx, final PrincipalGroup item)
	{
		final List<AbstractPromotion> items = item.getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			"AbstractPromotion",
			null,
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.promotion</code> attribute.
	 * @return the promotion
	 */
	public Collection<AbstractPromotion> getPromotion(final PrincipalGroup item)
	{
		return getPromotion( getSession().getSessionContext(), item );
	}
	
	public long getPromotionCount(final SessionContext ctx, final PrincipalGroup item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			"AbstractPromotion",
			null
		);
	}
	
	public long getPromotionCount(final PrincipalGroup item)
	{
		return getPromotionCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PrincipalGroup.promotion</code> attribute. 
	 * @param value the promotion
	 */
	public void setPromotion(final SessionContext ctx, final PrincipalGroup item, final Collection<AbstractPromotion> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PrincipalGroup.promotion</code> attribute. 
	 * @param value the promotion
	 */
	public void setPromotion(final PrincipalGroup item, final Collection<AbstractPromotion> value)
	{
		setPromotion( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to promotion. 
	 * @param value the item to add to promotion
	 */
	public void addToPromotion(final SessionContext ctx, final PrincipalGroup item, final AbstractPromotion value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to promotion. 
	 * @param value the item to add to promotion
	 */
	public void addToPromotion(final PrincipalGroup item, final AbstractPromotion value)
	{
		addToPromotion( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from promotion. 
	 * @param value the item to remove from promotion
	 */
	public void removeFromPromotion(final SessionContext ctx, final PrincipalGroup item, final AbstractPromotion value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from promotion. 
	 * @param value the item to remove from promotion
	 */
	public void removeFromPromotion(final PrincipalGroup item, final AbstractPromotion value)
	{
		removeFromPromotion( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.promotionBanner</code> attribute.
	 * @return the promotionBanner - Media to display on the promotion page when this promotion is available.
	 */
	public Media getPromotionBanner(final SessionContext ctx, final AbstractPromotion item)
	{
		return (Media)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.PROMOTIONBANNER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.promotionBanner</code> attribute.
	 * @return the promotionBanner - Media to display on the promotion page when this promotion is available.
	 */
	public Media getPromotionBanner(final AbstractPromotion item)
	{
		return getPromotionBanner( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.promotionBanner</code> attribute. 
	 * @param value the promotionBanner - Media to display on the promotion page when this promotion is available.
	 */
	public void setPromotionBanner(final SessionContext ctx, final AbstractPromotion item, final Media value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.PROMOTIONBANNER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.promotionBanner</code> attribute. 
	 * @param value the promotionBanner - Media to display on the promotion page when this promotion is available.
	 */
	public void setPromotionBanner(final AbstractPromotion item, final Media value)
	{
		setPromotionBanner( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.promtionCategory</code> attribute.
	 * @return the promtionCategory
	 */
	public Collection<PromotionCategory> getPromtionCategory(final SessionContext ctx, final Product item)
	{
		final List<PromotionCategory> items = item.getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			"PromotionCategory",
			null,
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.promtionCategory</code> attribute.
	 * @return the promtionCategory
	 */
	public Collection<PromotionCategory> getPromtionCategory(final Product item)
	{
		return getPromtionCategory( getSession().getSessionContext(), item );
	}
	
	public long getPromtionCategoryCount(final SessionContext ctx, final Product item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			"PromotionCategory",
			null
		);
	}
	
	public long getPromtionCategoryCount(final Product item)
	{
		return getPromtionCategoryCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.promtionCategory</code> attribute. 
	 * @param value the promtionCategory
	 */
	public void setPromtionCategory(final SessionContext ctx, final Product item, final Collection<PromotionCategory> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.promtionCategory</code> attribute. 
	 * @param value the promtionCategory
	 */
	public void setPromtionCategory(final Product item, final Collection<PromotionCategory> value)
	{
		setPromtionCategory( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to promtionCategory. 
	 * @param value the item to add to promtionCategory
	 */
	public void addToPromtionCategory(final SessionContext ctx, final Product item, final PromotionCategory value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to promtionCategory. 
	 * @param value the item to add to promtionCategory
	 */
	public void addToPromtionCategory(final Product item, final PromotionCategory value)
	{
		addToPromtionCategory( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from promtionCategory. 
	 * @param value the item to remove from promtionCategory
	 */
	public void removeFromPromtionCategory(final SessionContext ctx, final Product item, final PromotionCategory value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from promtionCategory. 
	 * @param value the item to remove from promtionCategory
	 */
	public void removeFromPromtionCategory(final Product item, final PromotionCategory value)
	{
		removeFromPromtionCategory( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.protocols</code> attribute.
	 * @return the protocols
	 */
	public List<Protocol> getProtocols(final SessionContext ctx, final B2BCustomer item)
	{
		final List<Protocol> items = item.getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			"Protocol",
			null,
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.protocols</code> attribute.
	 * @return the protocols
	 */
	public List<Protocol> getProtocols(final B2BCustomer item)
	{
		return getProtocols( getSession().getSessionContext(), item );
	}
	
	public long getProtocolsCount(final SessionContext ctx, final B2BCustomer item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			"Protocol",
			null
		);
	}
	
	public long getProtocolsCount(final B2BCustomer item)
	{
		return getProtocolsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.protocols</code> attribute. 
	 * @param value the protocols
	 */
	public void setProtocols(final SessionContext ctx, final B2BCustomer item, final List<Protocol> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			null,
			value,
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PROTOCOL2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.protocols</code> attribute. 
	 * @param value the protocols
	 */
	public void setProtocols(final B2BCustomer item, final List<Protocol> value)
	{
		setProtocols( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to protocols. 
	 * @param value the item to add to protocols
	 */
	public void addToProtocols(final SessionContext ctx, final B2BCustomer item, final Protocol value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PROTOCOL2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to protocols. 
	 * @param value the item to add to protocols
	 */
	public void addToProtocols(final B2BCustomer item, final Protocol value)
	{
		addToProtocols( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from protocols. 
	 * @param value the item to remove from protocols
	 */
	public void removeFromProtocols(final SessionContext ctx, final B2BCustomer item, final Protocol value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PROTOCOL2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from protocols. 
	 * @param value the item to remove from protocols
	 */
	public void removeFromProtocols(final B2BCustomer item, final Protocol value)
	{
		removeFromProtocols( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.provider</code> attribute.
	 * @return the provider
	 */
	public String getProvider(final SessionContext ctx, final ZoneDeliveryMode item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.PROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.provider</code> attribute.
	 * @return the provider
	 */
	public String getProvider(final ZoneDeliveryMode item)
	{
		return getProvider( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.provider</code> attribute. 
	 * @param value the provider
	 */
	public void setProvider(final SessionContext ctx, final ZoneDeliveryMode item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.PROVIDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.provider</code> attribute. 
	 * @param value the provider
	 */
	public void setProvider(final ZoneDeliveryMode item, final String value)
	{
		setProvider( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.quantityByCase</code> attribute.
	 * @return the quantityByCase - Private label Case Quantity
	 */
	public Integer getQuantityByCase(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Integer)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.QUANTITYBYCASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.quantityByCase</code> attribute.
	 * @return the quantityByCase - Private label Case Quantity
	 */
	public Integer getQuantityByCase(final AbstractOrderEntry item)
	{
		return getQuantityByCase( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.quantityByCase</code> attribute. 
	 * @return the quantityByCase - Private label Case Quantity
	 */
	public int getQuantityByCaseAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Integer value = getQuantityByCase( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.quantityByCase</code> attribute. 
	 * @return the quantityByCase - Private label Case Quantity
	 */
	public int getQuantityByCaseAsPrimitive(final AbstractOrderEntry item)
	{
		return getQuantityByCaseAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.quantityByCase</code> attribute. 
	 * @param value the quantityByCase - Private label Case Quantity
	 */
	public void setQuantityByCase(final SessionContext ctx, final AbstractOrderEntry item, final Integer value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.QUANTITYBYCASE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.quantityByCase</code> attribute. 
	 * @param value the quantityByCase - Private label Case Quantity
	 */
	public void setQuantityByCase(final AbstractOrderEntry item, final Integer value)
	{
		setQuantityByCase( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.quantityByCase</code> attribute. 
	 * @param value the quantityByCase - Private label Case Quantity
	 */
	public void setQuantityByCase(final SessionContext ctx, final AbstractOrderEntry item, final int value)
	{
		setQuantityByCase( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.quantityByCase</code> attribute. 
	 * @param value the quantityByCase - Private label Case Quantity
	 */
	public void setQuantityByCase(final AbstractOrderEntry item, final int value)
	{
		setQuantityByCase( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.receiveConsultationFee</code> attribute.
	 * @return the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public Boolean isReceiveConsultationFee(final SessionContext ctx, final B2BUnit item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.RECEIVECONSULTATIONFEE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.receiveConsultationFee</code> attribute.
	 * @return the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public Boolean isReceiveConsultationFee(final B2BUnit item)
	{
		return isReceiveConsultationFee( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.receiveConsultationFee</code> attribute. 
	 * @return the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public boolean isReceiveConsultationFeeAsPrimitive(final SessionContext ctx, final B2BUnit item)
	{
		Boolean value = isReceiveConsultationFee( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.receiveConsultationFee</code> attribute. 
	 * @return the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public boolean isReceiveConsultationFeeAsPrimitive(final B2BUnit item)
	{
		return isReceiveConsultationFeeAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.receiveConsultationFee</code> attribute. 
	 * @param value the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public void setReceiveConsultationFee(final SessionContext ctx, final B2BUnit item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.RECEIVECONSULTATIONFEE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.receiveConsultationFee</code> attribute. 
	 * @param value the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public void setReceiveConsultationFee(final B2BUnit item, final Boolean value)
	{
		setReceiveConsultationFee( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.receiveConsultationFee</code> attribute. 
	 * @param value the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public void setReceiveConsultationFee(final SessionContext ctx, final B2BUnit item, final boolean value)
	{
		setReceiveConsultationFee( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.receiveConsultationFee</code> attribute. 
	 * @param value the receiveConsultationFee - Flag whether receive consultation fee
	 */
	public void setReceiveConsultationFee(final B2BUnit item, final boolean value)
	{
		setReceiveConsultationFee( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.recommendation</code> attribute.
	 * @return the recommendation
	 */
	public List<Recommendation> getRecommendation(final SessionContext ctx, final B2BCustomer item)
	{
		return (List<Recommendation>)CUSTOMER2RECOMMENDATIONRECOMMENDATIONHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.recommendation</code> attribute.
	 * @return the recommendation
	 */
	public List<Recommendation> getRecommendation(final B2BCustomer item)
	{
		return getRecommendation( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.recommendation</code> attribute. 
	 * @param value the recommendation
	 */
	public void setRecommendation(final SessionContext ctx, final B2BCustomer item, final List<Recommendation> value)
	{
		CUSTOMER2RECOMMENDATIONRECOMMENDATIONHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.recommendation</code> attribute. 
	 * @param value the recommendation
	 */
	public void setRecommendation(final B2BCustomer item, final List<Recommendation> value)
	{
		setRecommendation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to recommendation. 
	 * @param value the item to add to recommendation
	 */
	public void addToRecommendation(final SessionContext ctx, final B2BCustomer item, final Recommendation value)
	{
		CUSTOMER2RECOMMENDATIONRECOMMENDATIONHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to recommendation. 
	 * @param value the item to add to recommendation
	 */
	public void addToRecommendation(final B2BCustomer item, final Recommendation value)
	{
		addToRecommendation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from recommendation. 
	 * @param value the item to remove from recommendation
	 */
	public void removeFromRecommendation(final SessionContext ctx, final B2BCustomer item, final Recommendation value)
	{
		CUSTOMER2RECOMMENDATIONRECOMMENDATIONHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from recommendation. 
	 * @param value the item to remove from recommendation
	 */
	public void removeFromRecommendation(final B2BCustomer item, final Recommendation value)
	{
		removeFromRecommendation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.recommendation</code> attribute.
	 * @return the recommendation - Subscription for Updated
	 */
	public Recommendation getRecommendation(final SessionContext ctx, final StoreFrontCustomerProcess item)
	{
		return (Recommendation)item.getProperty( ctx, WholescriptscoreConstants.Attributes.StoreFrontCustomerProcess.RECOMMENDATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.recommendation</code> attribute.
	 * @return the recommendation - Subscription for Updated
	 */
	public Recommendation getRecommendation(final StoreFrontCustomerProcess item)
	{
		return getRecommendation( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontCustomerProcess.recommendation</code> attribute. 
	 * @param value the recommendation - Subscription for Updated
	 */
	public void setRecommendation(final SessionContext ctx, final StoreFrontCustomerProcess item, final Recommendation value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.StoreFrontCustomerProcess.RECOMMENDATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontCustomerProcess.recommendation</code> attribute. 
	 * @param value the recommendation - Subscription for Updated
	 */
	public void setRecommendation(final StoreFrontCustomerProcess item, final Recommendation value)
	{
		setRecommendation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.reconciliationAcct</code> attribute.
	 * @return the reconciliationAcct - reconciliation Acct
	 */
	public String getReconciliationAcct(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.RECONCILIATIONACCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.reconciliationAcct</code> attribute.
	 * @return the reconciliationAcct - reconciliation Acct
	 */
	public String getReconciliationAcct(final SAPConfiguration item)
	{
		return getReconciliationAcct( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.reconciliationAcct</code> attribute. 
	 * @param value the reconciliationAcct - reconciliation Acct
	 */
	public void setReconciliationAcct(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.RECONCILIATIONACCT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.reconciliationAcct</code> attribute. 
	 * @param value the reconciliationAcct - reconciliation Acct
	 */
	public void setReconciliationAcct(final SAPConfiguration item, final String value)
	{
		setReconciliationAcct( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.referralAddress</code> attribute.
	 * @return the referralAddress
	 */
	public ReferralCodes getReferralAddress(final SessionContext ctx, final Address item)
	{
		return (ReferralCodes)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Address.REFERRALADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.referralAddress</code> attribute.
	 * @return the referralAddress
	 */
	public ReferralCodes getReferralAddress(final Address item)
	{
		return getReferralAddress( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Address.referralAddress</code> attribute. 
	 * @param value the referralAddress
	 */
	public void setReferralAddress(final SessionContext ctx, final Address item, final ReferralCodes value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Address.REFERRALADDRESS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Address.referralAddress</code> attribute. 
	 * @param value the referralAddress
	 */
	public void setReferralAddress(final Address item, final ReferralCodes value)
	{
		setReferralAddress( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.referralCode</code> attribute.
	 * @return the referralCode
	 */
	public String getReferralCode(final SessionContext ctx, final B2BCustomer item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.REFERRALCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.referralCode</code> attribute.
	 * @return the referralCode
	 */
	public String getReferralCode(final B2BCustomer item)
	{
		return getReferralCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.referralCode</code> attribute. 
	 * @param value the referralCode
	 */
	public void setReferralCode(final SessionContext ctx, final B2BCustomer item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.REFERRALCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.referralCode</code> attribute. 
	 * @param value the referralCode
	 */
	public void setReferralCode(final B2BCustomer item, final String value)
	{
		setReferralCode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.referralCodes</code> attribute.
	 * @return the referralCodes
	 */
	public Set<ReferralCodes> getReferralCodes(final SessionContext ctx, final B2BUnit item)
	{
		return (Set<ReferralCodes>)B2BUNIT2REFERRALCODESREFERRALCODESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.referralCodes</code> attribute.
	 * @return the referralCodes
	 */
	public Set<ReferralCodes> getReferralCodes(final B2BUnit item)
	{
		return getReferralCodes( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.referralCodes</code> attribute. 
	 * @param value the referralCodes
	 */
	public void setReferralCodes(final SessionContext ctx, final B2BUnit item, final Set<ReferralCodes> value)
	{
		B2BUNIT2REFERRALCODESREFERRALCODESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.referralCodes</code> attribute. 
	 * @param value the referralCodes
	 */
	public void setReferralCodes(final B2BUnit item, final Set<ReferralCodes> value)
	{
		setReferralCodes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to referralCodes. 
	 * @param value the item to add to referralCodes
	 */
	public void addToReferralCodes(final SessionContext ctx, final B2BUnit item, final ReferralCodes value)
	{
		B2BUNIT2REFERRALCODESREFERRALCODESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to referralCodes. 
	 * @param value the item to add to referralCodes
	 */
	public void addToReferralCodes(final B2BUnit item, final ReferralCodes value)
	{
		addToReferralCodes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from referralCodes. 
	 * @param value the item to remove from referralCodes
	 */
	public void removeFromReferralCodes(final SessionContext ctx, final B2BUnit item, final ReferralCodes value)
	{
		B2BUNIT2REFERRALCODESREFERRALCODESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from referralCodes. 
	 * @param value the item to remove from referralCodes
	 */
	public void removeFromReferralCodes(final B2BUnit item, final ReferralCodes value)
	{
		removeFromReferralCodes( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.referralName</code> attribute.
	 * @return the referralName
	 */
	public String getReferralName(final SessionContext ctx, final B2BCustomer item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BCustomer.REFERRALNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.referralName</code> attribute.
	 * @return the referralName
	 */
	public String getReferralName(final B2BCustomer item)
	{
		return getReferralName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.referralName</code> attribute. 
	 * @param value the referralName
	 */
	public void setReferralName(final SessionContext ctx, final B2BCustomer item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BCustomer.REFERRALNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.referralName</code> attribute. 
	 * @param value the referralName
	 */
	public void setReferralName(final B2BCustomer item, final String value)
	{
		setReferralName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.retailName</code> attribute.
	 * @return the retailName
	 */
	public String getRetailName(final SessionContext ctx, final Category item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Category.RETAILNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.retailName</code> attribute.
	 * @return the retailName
	 */
	public String getRetailName(final Category item)
	{
		return getRetailName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.retailName</code> attribute. 
	 * @param value the retailName
	 */
	public void setRetailName(final SessionContext ctx, final Category item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Category.RETAILNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.retailName</code> attribute. 
	 * @param value the retailName
	 */
	public void setRetailName(final Category item, final String value)
	{
		setRetailName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.salesPerson</code> attribute.
	 * @return the salesPerson
	 */
	public String getSalesPerson(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SALESPERSON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.salesPerson</code> attribute.
	 * @return the salesPerson
	 */
	public String getSalesPerson(final B2BUnit item)
	{
		return getSalesPerson( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.salesPerson</code> attribute. 
	 * @param value the salesPerson
	 */
	public void setSalesPerson(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SALESPERSON,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.salesPerson</code> attribute. 
	 * @param value the salesPerson
	 */
	public void setSalesPerson(final B2BUnit item, final String value)
	{
		setSalesPerson( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute.
	 * @return the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public Boolean isSalesPolicyTermsAgreed(final SessionContext ctx, final AbstractOrder item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.SALESPOLICYTERMSAGREED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute.
	 * @return the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public Boolean isSalesPolicyTermsAgreed(final AbstractOrder item)
	{
		return isSalesPolicyTermsAgreed( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute. 
	 * @return the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public boolean isSalesPolicyTermsAgreedAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Boolean value = isSalesPolicyTermsAgreed( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute. 
	 * @return the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public boolean isSalesPolicyTermsAgreedAsPrimitive(final AbstractOrder item)
	{
		return isSalesPolicyTermsAgreedAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute. 
	 * @param value the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public void setSalesPolicyTermsAgreed(final SessionContext ctx, final AbstractOrder item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.SALESPOLICYTERMSAGREED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute. 
	 * @param value the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public void setSalesPolicyTermsAgreed(final AbstractOrder item, final Boolean value)
	{
		setSalesPolicyTermsAgreed( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute. 
	 * @param value the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public void setSalesPolicyTermsAgreed(final SessionContext ctx, final AbstractOrder item, final boolean value)
	{
		setSalesPolicyTermsAgreed( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.salesPolicyTermsAgreed</code> attribute. 
	 * @param value the salesPolicyTermsAgreed - Flag for terms and condition in Review Order Page
	 */
	public void setSalesPolicyTermsAgreed(final AbstractOrder item, final boolean value)
	{
		setSalesPolicyTermsAgreed( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapBasicMaterial</code> attribute.
	 * @return the sapBasicMaterial - Indicates the date the Basic Material product
	 */
	public String getSapBasicMaterial(final SessionContext ctx, final Product item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPBASICMATERIAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapBasicMaterial</code> attribute.
	 * @return the sapBasicMaterial - Indicates the date the Basic Material product
	 */
	public String getSapBasicMaterial(final Product item)
	{
		return getSapBasicMaterial( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapBasicMaterial</code> attribute. 
	 * @param value the sapBasicMaterial - Indicates the date the Basic Material product
	 */
	public void setSapBasicMaterial(final SessionContext ctx, final Product item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPBASICMATERIAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapBasicMaterial</code> attribute. 
	 * @param value the sapBasicMaterial - Indicates the date the Basic Material product
	 */
	public void setSapBasicMaterial(final Product item, final String value)
	{
		setSapBasicMaterial( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapBlocked</code> attribute.
	 * @return the sapBlocked
	 */
	public String getSapBlocked(final SessionContext ctx, final B2BUnit item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPBLOCKED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapBlocked</code> attribute.
	 * @return the sapBlocked
	 */
	public String getSapBlocked(final B2BUnit item)
	{
		return getSapBlocked( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapBlocked</code> attribute. 
	 * @param value the sapBlocked
	 */
	public void setSapBlocked(final SessionContext ctx, final B2BUnit item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPBLOCKED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapBlocked</code> attribute. 
	 * @param value the sapBlocked
	 */
	public void setSapBlocked(final B2BUnit item, final String value)
	{
		setSapBlocked( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Employee.sapBusinessPartnerID</code> attribute.
	 * @return the sapBusinessPartnerID - SAP Business Partner ID
	 */
	public String getSapBusinessPartnerID(final SessionContext ctx, final Employee item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Employee.SAPBUSINESSPARTNERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Employee.sapBusinessPartnerID</code> attribute.
	 * @return the sapBusinessPartnerID - SAP Business Partner ID
	 */
	public String getSapBusinessPartnerID(final Employee item)
	{
		return getSapBusinessPartnerID( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Employee.sapBusinessPartnerID</code> attribute. 
	 * @param value the sapBusinessPartnerID - SAP Business Partner ID
	 */
	public void setSapBusinessPartnerID(final SessionContext ctx, final Employee item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Employee.SAPBUSINESSPARTNERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Employee.sapBusinessPartnerID</code> attribute. 
	 * @param value the sapBusinessPartnerID - SAP Business Partner ID
	 */
	public void setSapBusinessPartnerID(final Employee item, final String value)
	{
		setSapBusinessPartnerID( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.sapCarrier</code> attribute.
	 * @return the sapCarrier
	 */
	public String getSapCarrier(final SessionContext ctx, final ZoneDeliveryMode item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.SAPCARRIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.sapCarrier</code> attribute.
	 * @return the sapCarrier
	 */
	public String getSapCarrier(final ZoneDeliveryMode item)
	{
		return getSapCarrier( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.sapCarrier</code> attribute. 
	 * @param value the sapCarrier
	 */
	public void setSapCarrier(final SessionContext ctx, final ZoneDeliveryMode item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.SAPCARRIER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.sapCarrier</code> attribute. 
	 * @param value the sapCarrier
	 */
	public void setSapCarrier(final ZoneDeliveryMode item, final String value)
	{
		setSapCarrier( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.sapCarrierShippingOption</code> attribute.
	 * @return the sapCarrierShippingOption
	 */
	public String getSapCarrierShippingOption(final SessionContext ctx, final ZoneDeliveryMode item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.SAPCARRIERSHIPPINGOPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.sapCarrierShippingOption</code> attribute.
	 * @return the sapCarrierShippingOption
	 */
	public String getSapCarrierShippingOption(final ZoneDeliveryMode item)
	{
		return getSapCarrierShippingOption( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.sapCarrierShippingOption</code> attribute. 
	 * @param value the sapCarrierShippingOption
	 */
	public void setSapCarrierShippingOption(final SessionContext ctx, final ZoneDeliveryMode item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.SAPCARRIERSHIPPINGOPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.sapCarrierShippingOption</code> attribute. 
	 * @param value the sapCarrierShippingOption
	 */
	public void setSapCarrierShippingOption(final ZoneDeliveryMode item, final String value)
	{
		setSapCarrierShippingOption( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCentralDeliveryBlock</code> attribute.
	 * @return the sapCentralDeliveryBlock - Indicator if item's sapCentralDeliveryBlock
	 */
	public EnumerationValue getSapCentralDeliveryBlock(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCENTRALDELIVERYBLOCK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCentralDeliveryBlock</code> attribute.
	 * @return the sapCentralDeliveryBlock - Indicator if item's sapCentralDeliveryBlock
	 */
	public EnumerationValue getSapCentralDeliveryBlock(final B2BUnit item)
	{
		return getSapCentralDeliveryBlock( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCentralDeliveryBlock</code> attribute. 
	 * @param value the sapCentralDeliveryBlock - Indicator if item's sapCentralDeliveryBlock
	 */
	public void setSapCentralDeliveryBlock(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCENTRALDELIVERYBLOCK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCentralDeliveryBlock</code> attribute. 
	 * @param value the sapCentralDeliveryBlock - Indicator if item's sapCentralDeliveryBlock
	 */
	public void setSapCentralDeliveryBlock(final B2BUnit item, final EnumerationValue value)
	{
		setSapCentralDeliveryBlock( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCustomerClassification</code> attribute.
	 * @return the sapCustomerClassification - Indicator if item's sapCustomerClassification
	 */
	public EnumerationValue getSapCustomerClassification(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCUSTOMERCLASSIFICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCustomerClassification</code> attribute.
	 * @return the sapCustomerClassification - Indicator if item's sapCustomerClassification
	 */
	public EnumerationValue getSapCustomerClassification(final B2BUnit item)
	{
		return getSapCustomerClassification( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCustomerClassification</code> attribute. 
	 * @param value the sapCustomerClassification - Indicator if item's sapCustomerClassification
	 */
	public void setSapCustomerClassification(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCUSTOMERCLASSIFICATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCustomerClassification</code> attribute. 
	 * @param value the sapCustomerClassification - Indicator if item's sapCustomerClassification
	 */
	public void setSapCustomerClassification(final B2BUnit item, final EnumerationValue value)
	{
		setSapCustomerClassification( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCustomerGroup</code> attribute.
	 * @return the sapCustomerGroup - Indicator if item's sapCustomerGroup
	 */
	public EnumerationValue getSapCustomerGroup(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCUSTOMERGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCustomerGroup</code> attribute.
	 * @return the sapCustomerGroup - Indicator if item's sapCustomerGroup
	 */
	public EnumerationValue getSapCustomerGroup(final B2BUnit item)
	{
		return getSapCustomerGroup( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCustomerGroup</code> attribute. 
	 * @param value the sapCustomerGroup - Indicator if item's sapCustomerGroup
	 */
	public void setSapCustomerGroup(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCUSTOMERGROUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCustomerGroup</code> attribute. 
	 * @param value the sapCustomerGroup - Indicator if item's sapCustomerGroup
	 */
	public void setSapCustomerGroup(final B2BUnit item, final EnumerationValue value)
	{
		setSapCustomerGroup( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.sapCustomerGroupCode</code> attribute.
	 * @return the sapCustomerGroupCode
	 */
	public String getSapCustomerGroupCode(final SessionContext ctx, final ZoneDeliveryModeValue item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryModeValue.SAPCUSTOMERGROUPCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryModeValue.sapCustomerGroupCode</code> attribute.
	 * @return the sapCustomerGroupCode
	 */
	public String getSapCustomerGroupCode(final ZoneDeliveryModeValue item)
	{
		return getSapCustomerGroupCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryModeValue.sapCustomerGroupCode</code> attribute. 
	 * @param value the sapCustomerGroupCode
	 */
	public void setSapCustomerGroupCode(final SessionContext ctx, final ZoneDeliveryModeValue item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryModeValue.SAPCUSTOMERGROUPCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryModeValue.sapCustomerGroupCode</code> attribute. 
	 * @param value the sapCustomerGroupCode
	 */
	public void setSapCustomerGroupCode(final ZoneDeliveryModeValue item, final String value)
	{
		setSapCustomerGroupCode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCustomerPriceGroup</code> attribute.
	 * @return the sapCustomerPriceGroup - Indicator if item's sapCustomerPriceGroup
	 */
	public EnumerationValue getSapCustomerPriceGroup(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCUSTOMERPRICEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapCustomerPriceGroup</code> attribute.
	 * @return the sapCustomerPriceGroup - Indicator if item's sapCustomerPriceGroup
	 */
	public EnumerationValue getSapCustomerPriceGroup(final B2BUnit item)
	{
		return getSapCustomerPriceGroup( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCustomerPriceGroup</code> attribute. 
	 * @param value the sapCustomerPriceGroup - Indicator if item's sapCustomerPriceGroup
	 */
	public void setSapCustomerPriceGroup(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPCUSTOMERPRICEGROUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapCustomerPriceGroup</code> attribute. 
	 * @param value the sapCustomerPriceGroup - Indicator if item's sapCustomerPriceGroup
	 */
	public void setSapCustomerPriceGroup(final B2BUnit item, final EnumerationValue value)
	{
		setSapCustomerPriceGroup( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapCustomerPriceGroup</code> attribute.
	 * @return the sapCustomerPriceGroup - Sap Customer Price Group
	 */
	public String getSapCustomerPriceGroup(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.SAPCUSTOMERPRICEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapCustomerPriceGroup</code> attribute.
	 * @return the sapCustomerPriceGroup - Sap Customer Price Group
	 */
	public String getSapCustomerPriceGroup(final SAPConfiguration item)
	{
		return getSapCustomerPriceGroup( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapCustomerPriceGroup</code> attribute. 
	 * @param value the sapCustomerPriceGroup - Sap Customer Price Group
	 */
	public void setSapCustomerPriceGroup(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.SAPCUSTOMERPRICEGROUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapCustomerPriceGroup</code> attribute. 
	 * @param value the sapCustomerPriceGroup - Sap Customer Price Group
	 */
	public void setSapCustomerPriceGroup(final SAPConfiguration item, final String value)
	{
		setSapCustomerPriceGroup( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapDChainSpecStatus</code> attribute.
	 * @return the sapDChainSpecStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapDChainSpecStatus(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPDCHAINSPECSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapDChainSpecStatus</code> attribute.
	 * @return the sapDChainSpecStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapDChainSpecStatus(final Product item)
	{
		return getSapDChainSpecStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapDChainSpecStatus</code> attribute. 
	 * @param value the sapDChainSpecStatus - SAP Sales Units of measure
	 */
	public void setSapDChainSpecStatus(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPDCHAINSPECSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapDChainSpecStatus</code> attribute. 
	 * @param value the sapDChainSpecStatus - SAP Sales Units of measure
	 */
	public void setSapDChainSpecStatus(final Product item, final EnumerationValue value)
	{
		setSapDChainSpecStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapDeliveryBlock</code> attribute.
	 * @return the sapDeliveryBlock - Indicator if item's sapPaymentTerms
	 */
	public EnumerationValue getSapDeliveryBlock(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPDELIVERYBLOCK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapDeliveryBlock</code> attribute.
	 * @return the sapDeliveryBlock - Indicator if item's sapPaymentTerms
	 */
	public EnumerationValue getSapDeliveryBlock(final B2BUnit item)
	{
		return getSapDeliveryBlock( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapDeliveryBlock</code> attribute. 
	 * @param value the sapDeliveryBlock - Indicator if item's sapPaymentTerms
	 */
	public void setSapDeliveryBlock(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPDELIVERYBLOCK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapDeliveryBlock</code> attribute. 
	 * @param value the sapDeliveryBlock - Indicator if item's sapPaymentTerms
	 */
	public void setSapDeliveryBlock(final B2BUnit item, final EnumerationValue value)
	{
		setSapDeliveryBlock( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapIncoterms</code> attribute.
	 * @return the sapIncoterms - Indicator if item's sapIncoterms
	 */
	public EnumerationValue getSapIncoterms(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPINCOTERMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapIncoterms</code> attribute.
	 * @return the sapIncoterms - Indicator if item's sapIncoterms
	 */
	public EnumerationValue getSapIncoterms(final B2BUnit item)
	{
		return getSapIncoterms( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapIncoterms</code> attribute. 
	 * @param value the sapIncoterms - Indicator if item's sapIncoterms
	 */
	public void setSapIncoterms(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPINCOTERMS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapIncoterms</code> attribute. 
	 * @param value the sapIncoterms - Indicator if item's sapIncoterms
	 */
	public void setSapIncoterms(final B2BUnit item, final EnumerationValue value)
	{
		setSapIncoterms( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapMaterialGroup</code> attribute.
	 * @return the sapMaterialGroup - Indicator if item's SAP Material Group
	 */
	public EnumerationValue getSapMaterialGroup(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPMATERIALGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapMaterialGroup</code> attribute.
	 * @return the sapMaterialGroup - Indicator if item's SAP Material Group
	 */
	public EnumerationValue getSapMaterialGroup(final Product item)
	{
		return getSapMaterialGroup( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapMaterialGroup</code> attribute. 
	 * @param value the sapMaterialGroup - Indicator if item's SAP Material Group
	 */
	public void setSapMaterialGroup(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPMATERIALGROUP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapMaterialGroup</code> attribute. 
	 * @param value the sapMaterialGroup - Indicator if item's SAP Material Group
	 */
	public void setSapMaterialGroup(final Product item, final EnumerationValue value)
	{
		setSapMaterialGroup( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapOnCreditHold</code> attribute.
	 * @return the sapOnCreditHold - Indicator if item's sapOnCreditHold
	 */
	public EnumerationValue getSapOnCreditHold(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPONCREDITHOLD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapOnCreditHold</code> attribute.
	 * @return the sapOnCreditHold - Indicator if item's sapOnCreditHold
	 */
	public EnumerationValue getSapOnCreditHold(final B2BUnit item)
	{
		return getSapOnCreditHold( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapOnCreditHold</code> attribute. 
	 * @param value the sapOnCreditHold - Indicator if item's sapOnCreditHold
	 */
	public void setSapOnCreditHold(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPONCREDITHOLD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapOnCreditHold</code> attribute. 
	 * @param value the sapOnCreditHold - Indicator if item's sapOnCreditHold
	 */
	public void setSapOnCreditHold(final B2BUnit item, final EnumerationValue value)
	{
		setSapOnCreditHold( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapPaymentTerms</code> attribute.
	 * @return the sapPaymentTerms - Indicator if item's sapPaymentTerms
	 */
	public EnumerationValue getSapPaymentTerms(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPPAYMENTTERMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapPaymentTerms</code> attribute.
	 * @return the sapPaymentTerms - Indicator if item's sapPaymentTerms
	 */
	public EnumerationValue getSapPaymentTerms(final B2BUnit item)
	{
		return getSapPaymentTerms( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapPaymentTerms</code> attribute. 
	 * @param value the sapPaymentTerms - Indicator if item's sapPaymentTerms
	 */
	public void setSapPaymentTerms(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPPAYMENTTERMS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapPaymentTerms</code> attribute. 
	 * @param value the sapPaymentTerms - Indicator if item's sapPaymentTerms
	 */
	public void setSapPaymentTerms(final B2BUnit item, final EnumerationValue value)
	{
		setSapPaymentTerms( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapSalesOffice</code> attribute.
	 * @return the sapSalesOffice - Indicator if item's sapSalesOffice
	 */
	public EnumerationValue getSapSalesOffice(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPSALESOFFICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapSalesOffice</code> attribute.
	 * @return the sapSalesOffice - Indicator if item's sapSalesOffice
	 */
	public EnumerationValue getSapSalesOffice(final B2BUnit item)
	{
		return getSapSalesOffice( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapSalesOffice</code> attribute. 
	 * @param value the sapSalesOffice - Indicator if item's sapSalesOffice
	 */
	public void setSapSalesOffice(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPSALESOFFICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapSalesOffice</code> attribute. 
	 * @param value the sapSalesOffice - Indicator if item's sapSalesOffice
	 */
	public void setSapSalesOffice(final B2BUnit item, final EnumerationValue value)
	{
		setSapSalesOffice( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapSalesUnits</code> attribute.
	 * @return the sapSalesUnits - SAP Sales Units
	 */
	public List<Unit> getSapSalesUnits(final SessionContext ctx, final Product item)
	{
		List<Unit> coll = (List<Unit>)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPSALESUNITS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapSalesUnits</code> attribute.
	 * @return the sapSalesUnits - SAP Sales Units
	 */
	public List<Unit> getSapSalesUnits(final Product item)
	{
		return getSapSalesUnits( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapSalesUnits</code> attribute. 
	 * @param value the sapSalesUnits - SAP Sales Units
	 */
	public void setSapSalesUnits(final SessionContext ctx, final Product item, final List<Unit> value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPSALESUNITS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapSalesUnits</code> attribute. 
	 * @param value the sapSalesUnits - SAP Sales Units
	 */
	public void setSapSalesUnits(final Product item, final List<Unit> value)
	{
		setSapSalesUnits( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapShippingCond</code> attribute.
	 * @return the sapShippingCond - Indicator if item's sapShippingCond
	 */
	public EnumerationValue getSapShippingCond(final SessionContext ctx, final B2BUnit item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPSHIPPINGCOND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.sapShippingCond</code> attribute.
	 * @return the sapShippingCond - Indicator if item's sapShippingCond
	 */
	public EnumerationValue getSapShippingCond(final B2BUnit item)
	{
		return getSapShippingCond( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapShippingCond</code> attribute. 
	 * @param value the sapShippingCond - Indicator if item's sapShippingCond
	 */
	public void setSapShippingCond(final SessionContext ctx, final B2BUnit item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BUnit.SAPSHIPPINGCOND,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.sapShippingCond</code> attribute. 
	 * @param value the sapShippingCond - Indicator if item's sapShippingCond
	 */
	public void setSapShippingCond(final B2BUnit item, final EnumerationValue value)
	{
		setSapShippingCond( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.sapShippingCond</code> attribute.
	 * @return the sapShippingCond
	 */
	public String getSapShippingCond(final SessionContext ctx, final ZoneDeliveryMode item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.SAPSHIPPINGCOND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ZoneDeliveryMode.sapShippingCond</code> attribute.
	 * @return the sapShippingCond
	 */
	public String getSapShippingCond(final ZoneDeliveryMode item)
	{
		return getSapShippingCond( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.sapShippingCond</code> attribute. 
	 * @param value the sapShippingCond
	 */
	public void setSapShippingCond(final SessionContext ctx, final ZoneDeliveryMode item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.ZoneDeliveryMode.SAPSHIPPINGCOND,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ZoneDeliveryMode.sapShippingCond</code> attribute. 
	 * @param value the sapShippingCond
	 */
	public void setSapShippingCond(final ZoneDeliveryMode item, final String value)
	{
		setSapShippingCond( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapStatus</code> attribute.
	 * @return the sapStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapStatus(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapStatus</code> attribute.
	 * @return the sapStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapStatus(final Product item)
	{
		return getSapStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapStatus</code> attribute. 
	 * @param value the sapStatus - SAP Sales Units of measure
	 */
	public void setSapStatus(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapStatus</code> attribute. 
	 * @param value the sapStatus - SAP Sales Units of measure
	 */
	public void setSapStatus(final Product item, final EnumerationValue value)
	{
		setSapStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapTemplateId</code> attribute.
	 * @return the sapTemplateId - The templateId is for Protocol Reference in Recommendations
	 */
	public String getSapTemplateId(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.SAPTEMPLATEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapTemplateId</code> attribute.
	 * @return the sapTemplateId - The templateId is for Protocol Reference in Recommendations
	 */
	public String getSapTemplateId(final AbstractOrderEntry item)
	{
		return getSapTemplateId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapTemplateId</code> attribute. 
	 * @param value the sapTemplateId - The templateId is for Protocol Reference in Recommendations
	 */
	public void setSapTemplateId(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.SAPTEMPLATEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapTemplateId</code> attribute. 
	 * @param value the sapTemplateId - The templateId is for Protocol Reference in Recommendations
	 */
	public void setSapTemplateId(final AbstractOrderEntry item, final String value)
	{
		setSapTemplateId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapUnitOfMeasure</code> attribute.
	 * @return the sapUnitOfMeasure - SAP Sales Units of measure
	 */
	public List<SapUnitOfMeasure> getSapUnitOfMeasure(final SessionContext ctx, final Product item)
	{
		List<SapUnitOfMeasure> coll = (List<SapUnitOfMeasure>)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPUNITOFMEASURE);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapUnitOfMeasure</code> attribute.
	 * @return the sapUnitOfMeasure - SAP Sales Units of measure
	 */
	public List<SapUnitOfMeasure> getSapUnitOfMeasure(final Product item)
	{
		return getSapUnitOfMeasure( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapUnitOfMeasure</code> attribute. 
	 * @param value the sapUnitOfMeasure - SAP Sales Units of measure
	 */
	public void setSapUnitOfMeasure(final SessionContext ctx, final Product item, final List<SapUnitOfMeasure> value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPUNITOFMEASURE,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapUnitOfMeasure</code> attribute. 
	 * @param value the sapUnitOfMeasure - SAP Sales Units of measure
	 */
	public void setSapUnitOfMeasure(final Product item, final List<SapUnitOfMeasure> value)
	{
		setSapUnitOfMeasure( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapXdistrChainStatus</code> attribute.
	 * @return the sapXdistrChainStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapXdistrChainStatus(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPXDISTRCHAINSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapXdistrChainStatus</code> attribute.
	 * @return the sapXdistrChainStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapXdistrChainStatus(final Product item)
	{
		return getSapXdistrChainStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapXdistrChainStatus</code> attribute. 
	 * @param value the sapXdistrChainStatus - SAP Sales Units of measure
	 */
	public void setSapXdistrChainStatus(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPXDISTRCHAINSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapXdistrChainStatus</code> attribute. 
	 * @param value the sapXdistrChainStatus - SAP Sales Units of measure
	 */
	public void setSapXdistrChainStatus(final Product item, final EnumerationValue value)
	{
		setSapXdistrChainStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapXPlantMatlStatus</code> attribute.
	 * @return the sapXPlantMatlStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapXPlantMatlStatus(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SAPXPLANTMATLSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.sapXPlantMatlStatus</code> attribute.
	 * @return the sapXPlantMatlStatus - SAP Sales Units of measure
	 */
	public EnumerationValue getSapXPlantMatlStatus(final Product item)
	{
		return getSapXPlantMatlStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapXPlantMatlStatus</code> attribute. 
	 * @param value the sapXPlantMatlStatus - SAP Sales Units of measure
	 */
	public void setSapXPlantMatlStatus(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SAPXPLANTMATLSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.sapXPlantMatlStatus</code> attribute. 
	 * @param value the sapXPlantMatlStatus - SAP Sales Units of measure
	 */
	public void setSapXPlantMatlStatus(final Product item, final EnumerationValue value)
	{
		setSapXPlantMatlStatus( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.selectedAutoShip</code> attribute.
	 * @return the selectedAutoShip - Selected  AutoShip for the Entry
	 */
	public SupportedAutoShip getSelectedAutoShip(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (SupportedAutoShip)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.SELECTEDAUTOSHIP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.selectedAutoShip</code> attribute.
	 * @return the selectedAutoShip - Selected  AutoShip for the Entry
	 */
	public SupportedAutoShip getSelectedAutoShip(final AbstractOrderEntry item)
	{
		return getSelectedAutoShip( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.selectedAutoShip</code> attribute. 
	 * @param value the selectedAutoShip - Selected  AutoShip for the Entry
	 */
	public void setSelectedAutoShip(final SessionContext ctx, final AbstractOrderEntry item, final SupportedAutoShip value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.SELECTEDAUTOSHIP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.selectedAutoShip</code> attribute. 
	 * @param value the selectedAutoShip - Selected  AutoShip for the Entry
	 */
	public void setSelectedAutoShip(final AbstractOrderEntry item, final SupportedAutoShip value)
	{
		setSelectedAutoShip( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnBanners</code> attribute.
	 * @return the showOnBanners - flag to display on homepage
	 */
	public Boolean isShowOnBanners(final SessionContext ctx, final AbstractPromotion item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.SHOWONBANNERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnBanners</code> attribute.
	 * @return the showOnBanners - flag to display on homepage
	 */
	public Boolean isShowOnBanners(final AbstractPromotion item)
	{
		return isShowOnBanners( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnBanners</code> attribute. 
	 * @return the showOnBanners - flag to display on homepage
	 */
	public boolean isShowOnBannersAsPrimitive(final SessionContext ctx, final AbstractPromotion item)
	{
		Boolean value = isShowOnBanners( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnBanners</code> attribute. 
	 * @return the showOnBanners - flag to display on homepage
	 */
	public boolean isShowOnBannersAsPrimitive(final AbstractPromotion item)
	{
		return isShowOnBannersAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnBanners</code> attribute. 
	 * @param value the showOnBanners - flag to display on homepage
	 */
	public void setShowOnBanners(final SessionContext ctx, final AbstractPromotion item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.SHOWONBANNERS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnBanners</code> attribute. 
	 * @param value the showOnBanners - flag to display on homepage
	 */
	public void setShowOnBanners(final AbstractPromotion item, final Boolean value)
	{
		setShowOnBanners( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnBanners</code> attribute. 
	 * @param value the showOnBanners - flag to display on homepage
	 */
	public void setShowOnBanners(final SessionContext ctx, final AbstractPromotion item, final boolean value)
	{
		setShowOnBanners( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnBanners</code> attribute. 
	 * @param value the showOnBanners - flag to display on homepage
	 */
	public void setShowOnBanners(final AbstractPromotion item, final boolean value)
	{
		setShowOnBanners( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnPLP</code> attribute.
	 * @return the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public Boolean isShowOnPLP(final SessionContext ctx, final AbstractPromotion item)
	{
		return (Boolean)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.SHOWONPLP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnPLP</code> attribute.
	 * @return the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public Boolean isShowOnPLP(final AbstractPromotion item)
	{
		return isShowOnPLP( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnPLP</code> attribute. 
	 * @return the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public boolean isShowOnPLPAsPrimitive(final SessionContext ctx, final AbstractPromotion item)
	{
		Boolean value = isShowOnPLP( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotion.showOnPLP</code> attribute. 
	 * @return the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public boolean isShowOnPLPAsPrimitive(final AbstractPromotion item)
	{
		return isShowOnPLPAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnPLP</code> attribute. 
	 * @param value the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public void setShowOnPLP(final SessionContext ctx, final AbstractPromotion item, final Boolean value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractPromotion.SHOWONPLP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnPLP</code> attribute. 
	 * @param value the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public void setShowOnPLP(final AbstractPromotion item, final Boolean value)
	{
		setShowOnPLP( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnPLP</code> attribute. 
	 * @param value the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public void setShowOnPLP(final SessionContext ctx, final AbstractPromotion item, final boolean value)
	{
		setShowOnPLP( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractPromotion.showOnPLP</code> attribute. 
	 * @param value the showOnPLP - flag to indicate whether this promotion display on Products related pages
	 */
	public void setShowOnPLP(final AbstractPromotion item, final boolean value)
	{
		setShowOnPLP( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sortkey</code> attribute.
	 * @return the sortkey - sort key
	 */
	public String getSortkey(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.SORTKEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sortkey</code> attribute.
	 * @return the sortkey - sort key
	 */
	public String getSortkey(final SAPConfiguration item)
	{
		return getSortkey( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sortkey</code> attribute. 
	 * @param value the sortkey - sort key
	 */
	public void setSortkey(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.SORTKEY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sortkey</code> attribute. 
	 * @param value the sortkey - sort key
	 */
	public void setSortkey(final SAPConfiguration item, final String value)
	{
		setSortkey( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.sourceApplication</code> attribute.
	 * @return the sourceApplication
	 */
	public EnumerationValue getSourceApplication(final SessionContext ctx, final AbstractOrder item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.SOURCEAPPLICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.sourceApplication</code> attribute.
	 * @return the sourceApplication
	 */
	public EnumerationValue getSourceApplication(final AbstractOrder item)
	{
		return getSourceApplication( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.sourceApplication</code> attribute. 
	 * @param value the sourceApplication
	 */
	public void setSourceApplication(final SessionContext ctx, final AbstractOrder item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.SOURCEAPPLICATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.sourceApplication</code> attribute. 
	 * @param value the sourceApplication
	 */
	public void setSourceApplication(final AbstractOrder item, final EnumerationValue value)
	{
		setSourceApplication( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.specProcId</code> attribute.
	 * @return the specProcId
	 */
	public String getSpecProcId(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.SPECPROCID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.specProcId</code> attribute.
	 * @return the specProcId
	 */
	public String getSpecProcId(final AbstractOrder item)
	{
		return getSpecProcId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.specProcId</code> attribute. 
	 * @param value the specProcId
	 */
	public void setSpecProcId(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.SPECPROCID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.specProcId</code> attribute. 
	 * @param value the specProcId
	 */
	public void setSpecProcId(final AbstractOrder item, final String value)
	{
		setSpecProcId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.subTotal</code> attribute.
	 * @return the subTotal
	 */
	public Double getSubTotal(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Double)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.SUBTOTAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.subTotal</code> attribute.
	 * @return the subTotal
	 */
	public Double getSubTotal(final AbstractOrderEntry item)
	{
		return getSubTotal( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.subTotal</code> attribute. 
	 * @return the subTotal
	 */
	public double getSubTotalAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Double value = getSubTotal( ctx,item );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.subTotal</code> attribute. 
	 * @return the subTotal
	 */
	public double getSubTotalAsPrimitive(final AbstractOrderEntry item)
	{
		return getSubTotalAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.subTotal</code> attribute. 
	 * @param value the subTotal
	 */
	public void setSubTotal(final SessionContext ctx, final AbstractOrderEntry item, final Double value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.SUBTOTAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.subTotal</code> attribute. 
	 * @param value the subTotal
	 */
	public void setSubTotal(final AbstractOrderEntry item, final Double value)
	{
		setSubTotal( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.subTotal</code> attribute. 
	 * @param value the subTotal
	 */
	public void setSubTotal(final SessionContext ctx, final AbstractOrderEntry item, final double value)
	{
		setSubTotal( ctx, item, Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.subTotal</code> attribute. 
	 * @param value the subTotal
	 */
	public void setSubTotal(final AbstractOrderEntry item, final double value)
	{
		setSubTotal( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.supportedAutoShip</code> attribute.
	 * @return the supportedAutoShip - List supported AutoShip
	 */
	public List<SupportedAutoShip> getSupportedAutoShip(final SessionContext ctx, final Product item)
	{
		List<SupportedAutoShip> coll = (List<SupportedAutoShip>)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.SUPPORTEDAUTOSHIP);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.supportedAutoShip</code> attribute.
	 * @return the supportedAutoShip - List supported AutoShip
	 */
	public List<SupportedAutoShip> getSupportedAutoShip(final Product item)
	{
		return getSupportedAutoShip( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.supportedAutoShip</code> attribute. 
	 * @param value the supportedAutoShip - List supported AutoShip
	 */
	public void setSupportedAutoShip(final SessionContext ctx, final Product item, final List<SupportedAutoShip> value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.SUPPORTEDAUTOSHIP,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.supportedAutoShip</code> attribute. 
	 * @param value the supportedAutoShip - List supported AutoShip
	 */
	public void setSupportedAutoShip(final Product item, final List<SupportedAutoShip> value)
	{
		setSupportedAutoShip( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.taxClass</code> attribute.
	 * @return the taxClass - tax Class
	 */
	public String getTaxClass(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.TAXCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.taxClass</code> attribute.
	 * @return the taxClass - tax Class
	 */
	public String getTaxClass(final SAPConfiguration item)
	{
		return getTaxClass( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.taxClass</code> attribute. 
	 * @param value the taxClass - tax Class
	 */
	public void setTaxClass(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.SAPConfiguration.TAXCLASS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.taxClass</code> attribute. 
	 * @param value the taxClass - tax Class
	 */
	public void setTaxClass(final SAPConfiguration item, final String value)
	{
		setTaxClass( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.templateId</code> attribute.
	 * @return the templateId - The templateId is for Protocol Reference in Recommendations
	 */
	public String getTemplateId(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.TEMPLATEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.templateId</code> attribute.
	 * @return the templateId - The templateId is for Protocol Reference in Recommendations
	 */
	public String getTemplateId(final AbstractOrderEntry item)
	{
		return getTemplateId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.templateId</code> attribute. 
	 * @param value the templateId - The templateId is for Protocol Reference in Recommendations
	 */
	public void setTemplateId(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrderEntry.TEMPLATEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.templateId</code> attribute. 
	 * @param value the templateId - The templateId is for Protocol Reference in Recommendations
	 */
	public void setTemplateId(final AbstractOrderEntry item, final String value)
	{
		setTemplateId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.w9Form</code> attribute.
	 * @return the w9Form - w9 form
	 */
	public W9Form getW9Form(final SessionContext ctx, final StoreFrontCustomerProcess item)
	{
		return (W9Form)item.getProperty( ctx, WholescriptscoreConstants.Attributes.StoreFrontCustomerProcess.W9FORM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.w9Form</code> attribute.
	 * @return the w9Form - w9 form
	 */
	public W9Form getW9Form(final StoreFrontCustomerProcess item)
	{
		return getW9Form( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontCustomerProcess.w9Form</code> attribute. 
	 * @param value the w9Form - w9 form
	 */
	public void setW9Form(final SessionContext ctx, final StoreFrontCustomerProcess item, final W9Form value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.StoreFrontCustomerProcess.W9FORM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontCustomerProcess.w9Form</code> attribute. 
	 * @param value the w9Form - w9 form
	 */
	public void setW9Form(final StoreFrontCustomerProcess item, final W9Form value)
	{
		setW9Form( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.webinar</code> attribute.
	 * @return the webinar
	 */
	public List<Webinar> getWebinar(final SessionContext ctx, final Category item)
	{
		final List<Webinar> items = item.getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			"Webinar",
			null,
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.webinar</code> attribute.
	 * @return the webinar
	 */
	public List<Webinar> getWebinar(final Category item)
	{
		return getWebinar( getSession().getSessionContext(), item );
	}
	
	public long getWebinarCount(final SessionContext ctx, final Category item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			"Webinar",
			null
		);
	}
	
	public long getWebinarCount(final Category item)
	{
		return getWebinarCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.webinar</code> attribute. 
	 * @param value the webinar
	 */
	public void setWebinar(final SessionContext ctx, final Category item, final List<Webinar> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			null,
			value,
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2CATEGORY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.webinar</code> attribute. 
	 * @param value the webinar
	 */
	public void setWebinar(final Category item, final List<Webinar> value)
	{
		setWebinar( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to webinar. 
	 * @param value the item to add to webinar
	 */
	public void addToWebinar(final SessionContext ctx, final Category item, final Webinar value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2CATEGORY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to webinar. 
	 * @param value the item to add to webinar
	 */
	public void addToWebinar(final Category item, final Webinar value)
	{
		addToWebinar( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from webinar. 
	 * @param value the item to remove from webinar
	 */
	public void removeFromWebinar(final SessionContext ctx, final Category item, final Webinar value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2CATEGORY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from webinar. 
	 * @param value the item to remove from webinar
	 */
	public void removeFromWebinar(final Category item, final Webinar value)
	{
		removeFromWebinar( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.webOrderNumber</code> attribute.
	 * @return the webOrderNumber
	 */
	public String getWebOrderNumber(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.WEBORDERNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.webOrderNumber</code> attribute.
	 * @return the webOrderNumber
	 */
	public String getWebOrderNumber(final AbstractOrder item)
	{
		return getWebOrderNumber( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.webOrderNumber</code> attribute. 
	 * @param value the webOrderNumber
	 */
	public void setWebOrderNumber(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.WEBORDERNUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.webOrderNumber</code> attribute. 
	 * @param value the webOrderNumber
	 */
	public void setWebOrderNumber(final AbstractOrder item, final String value)
	{
		setWebOrderNumber( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.webSite</code> attribute.
	 * @return the webSite
	 */
	public String getWebSite(final SessionContext ctx, final PointOfService item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.PointOfService.WEBSITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.webSite</code> attribute.
	 * @return the webSite
	 */
	public String getWebSite(final PointOfService item)
	{
		return getWebSite( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.webSite</code> attribute. 
	 * @param value the webSite
	 */
	public void setWebSite(final SessionContext ctx, final PointOfService item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.PointOfService.WEBSITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.webSite</code> attribute. 
	 * @param value the webSite
	 */
	public void setWebSite(final PointOfService item, final String value)
	{
		setWebSite( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.weight</code> attribute.
	 * @return the weight - Indicates the weight of product
	 */
	public String getWeight(final SessionContext ctx, final Product item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.WEIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.weight</code> attribute.
	 * @return the weight - Indicates the weight of product
	 */
	public String getWeight(final Product item)
	{
		return getWeight( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.weight</code> attribute. 
	 * @param value the weight - Indicates the weight of product
	 */
	public void setWeight(final SessionContext ctx, final Product item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.WEIGHT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.weight</code> attribute. 
	 * @param value the weight - Indicates the weight of product
	 */
	public void setWeight(final Product item, final String value)
	{
		setWeight( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.weightUnit</code> attribute.
	 * @return the weightUnit - Indicates the weight of product
	 */
	public EnumerationValue getWeightUnit(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.WEIGHTUNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.weightUnit</code> attribute.
	 * @return the weightUnit - Indicates the weight of product
	 */
	public EnumerationValue getWeightUnit(final Product item)
	{
		return getWeightUnit( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.weightUnit</code> attribute. 
	 * @param value the weightUnit - Indicates the weight of product
	 */
	public void setWeightUnit(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.WEIGHTUNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.weightUnit</code> attribute. 
	 * @param value the weightUnit - Indicates the weight of product
	 */
	public void setWeightUnit(final Product item, final EnumerationValue value)
	{
		setWeightUnit( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.wholescriptsBrand</code> attribute.
	 * @return the wholescriptsBrand - Attribute is used to store brand details
	 */
	public WholescriptsBrand getWholescriptsBrand(final SessionContext ctx, final Product item)
	{
		return (WholescriptsBrand)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.WHOLESCRIPTSBRAND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.wholescriptsBrand</code> attribute.
	 * @return the wholescriptsBrand - Attribute is used to store brand details
	 */
	public WholescriptsBrand getWholescriptsBrand(final Product item)
	{
		return getWholescriptsBrand( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.wholescriptsBrand</code> attribute. 
	 * @param value the wholescriptsBrand - Attribute is used to store brand details
	 */
	public void setWholescriptsBrand(final SessionContext ctx, final Product item, final WholescriptsBrand value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.WHOLESCRIPTSBRAND,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.wholescriptsBrand</code> attribute. 
	 * @param value the wholescriptsBrand - Attribute is used to store brand details
	 */
	public void setWholescriptsBrand(final Product item, final WholescriptsBrand value)
	{
		setWholescriptsBrand( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.wholescriptsIntegrationsRecords</code> attribute.
	 * @return the wholescriptsIntegrationsRecords
	 */
	public List<WholescriptsIntegrations> getWholescriptsIntegrationsRecords(final SessionContext ctx, final B2BCustomer item)
	{
		return (List<WholescriptsIntegrations>)CUSTOMER2WHOLESCRIPTSINTEGRATIONWHOLESCRIPTSINTEGRATIONSRECORDSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BCustomer.wholescriptsIntegrationsRecords</code> attribute.
	 * @return the wholescriptsIntegrationsRecords
	 */
	public List<WholescriptsIntegrations> getWholescriptsIntegrationsRecords(final B2BCustomer item)
	{
		return getWholescriptsIntegrationsRecords( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.wholescriptsIntegrationsRecords</code> attribute. 
	 * @param value the wholescriptsIntegrationsRecords
	 */
	public void setWholescriptsIntegrationsRecords(final SessionContext ctx, final B2BCustomer item, final List<WholescriptsIntegrations> value)
	{
		CUSTOMER2WHOLESCRIPTSINTEGRATIONWHOLESCRIPTSINTEGRATIONSRECORDSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BCustomer.wholescriptsIntegrationsRecords</code> attribute. 
	 * @param value the wholescriptsIntegrationsRecords
	 */
	public void setWholescriptsIntegrationsRecords(final B2BCustomer item, final List<WholescriptsIntegrations> value)
	{
		setWholescriptsIntegrationsRecords( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to wholescriptsIntegrationsRecords. 
	 * @param value the item to add to wholescriptsIntegrationsRecords
	 */
	public void addToWholescriptsIntegrationsRecords(final SessionContext ctx, final B2BCustomer item, final WholescriptsIntegrations value)
	{
		CUSTOMER2WHOLESCRIPTSINTEGRATIONWHOLESCRIPTSINTEGRATIONSRECORDSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to wholescriptsIntegrationsRecords. 
	 * @param value the item to add to wholescriptsIntegrationsRecords
	 */
	public void addToWholescriptsIntegrationsRecords(final B2BCustomer item, final WholescriptsIntegrations value)
	{
		addToWholescriptsIntegrationsRecords( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from wholescriptsIntegrationsRecords. 
	 * @param value the item to remove from wholescriptsIntegrationsRecords
	 */
	public void removeFromWholescriptsIntegrationsRecords(final SessionContext ctx, final B2BCustomer item, final WholescriptsIntegrations value)
	{
		CUSTOMER2WHOLESCRIPTSINTEGRATIONWHOLESCRIPTSINTEGRATIONSRECORDSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from wholescriptsIntegrationsRecords. 
	 * @param value the item to remove from wholescriptsIntegrationsRecords
	 */
	public void removeFromWholescriptsIntegrationsRecords(final B2BCustomer item, final WholescriptsIntegrations value)
	{
		removeFromWholescriptsIntegrationsRecords( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.width</code> attribute.
	 * @return the width - Indicates the weight of product
	 */
	public String getWidth(final SessionContext ctx, final Product item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.Product.WIDTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.width</code> attribute.
	 * @return the width - Indicates the weight of product
	 */
	public String getWidth(final Product item)
	{
		return getWidth( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.width</code> attribute. 
	 * @param value the width - Indicates the weight of product
	 */
	public void setWidth(final SessionContext ctx, final Product item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.Product.WIDTH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.width</code> attribute. 
	 * @param value the width - Indicates the weight of product
	 */
	public void setWidth(final Product item, final String value)
	{
		setWidth( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.wsContactUs</code> attribute.
	 * @return the wsContactUs - Contact Us Form
	 */
	public WSContactUsForm getWsContactUs(final SessionContext ctx, final StoreFrontCustomerProcess item)
	{
		return (WSContactUsForm)item.getProperty( ctx, WholescriptscoreConstants.Attributes.StoreFrontCustomerProcess.WSCONTACTUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.wsContactUs</code> attribute.
	 * @return the wsContactUs - Contact Us Form
	 */
	public WSContactUsForm getWsContactUs(final StoreFrontCustomerProcess item)
	{
		return getWsContactUs( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontCustomerProcess.wsContactUs</code> attribute. 
	 * @param value the wsContactUs - Contact Us Form
	 */
	public void setWsContactUs(final SessionContext ctx, final StoreFrontCustomerProcess item, final WSContactUsForm value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.StoreFrontCustomerProcess.WSCONTACTUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StoreFrontCustomerProcess.wsContactUs</code> attribute. 
	 * @param value the wsContactUs - Contact Us Form
	 */
	public void setWsContactUs(final StoreFrontCustomerProcess item, final WSContactUsForm value)
	{
		setWsContactUs( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.wsDescription</code> attribute.
	 * @return the wsDescription
	 */
	public String getWsDescription(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.WSDESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.wsDescription</code> attribute.
	 * @return the wsDescription
	 */
	public String getWsDescription(final AbstractOrder item)
	{
		return getWsDescription( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.wsDescription</code> attribute. 
	 * @param value the wsDescription
	 */
	public void setWsDescription(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.WSDESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.wsDescription</code> attribute. 
	 * @param value the wsDescription
	 */
	public void setWsDescription(final AbstractOrder item, final String value)
	{
		setWsDescription( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.wsName</code> attribute.
	 * @return the wsName
	 */
	public String getWsName(final SessionContext ctx, final AbstractOrder item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.AbstractOrder.WSNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.wsName</code> attribute.
	 * @return the wsName
	 */
	public String getWsName(final AbstractOrder item)
	{
		return getWsName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.wsName</code> attribute. 
	 * @param value the wsName
	 */
	public void setWsName(final SessionContext ctx, final AbstractOrder item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.AbstractOrder.WSNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.wsName</code> attribute. 
	 * @param value the wsName
	 */
	public void setWsName(final AbstractOrder item, final String value)
	{
		setWsName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.wsNotes</code> attribute.
	 * @return the wsNotes
	 */
	public String getWsNotes(final SessionContext ctx, final B2BComment item)
	{
		return (String)item.getProperty( ctx, WholescriptscoreConstants.Attributes.B2BComment.WSNOTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BComment.wsNotes</code> attribute.
	 * @return the wsNotes
	 */
	public String getWsNotes(final B2BComment item)
	{
		return getWsNotes( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.wsNotes</code> attribute. 
	 * @param value the wsNotes
	 */
	public void setWsNotes(final SessionContext ctx, final B2BComment item, final String value)
	{
		item.setProperty(ctx, WholescriptscoreConstants.Attributes.B2BComment.WSNOTES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BComment.wsNotes</code> attribute. 
	 * @param value the wsNotes
	 */
	public void setWsNotes(final B2BComment item, final String value)
	{
		setWsNotes( getSession().getSessionContext(), item, value );
	}
	
}
