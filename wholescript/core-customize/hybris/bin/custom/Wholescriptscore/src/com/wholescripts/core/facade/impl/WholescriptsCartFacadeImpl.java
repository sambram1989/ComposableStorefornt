/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import static de.hybris.platform.b2bacceleratorfacades.constants.B2BAcceleratorFacadesConstants.ACTING_USER_UID;
import static de.hybris.platform.util.localization.Localization.getLocalizedString;
import static de.hybris.platform.webservicescommons.util.YSanitizer.sanitize;

import de.hybris.order.calculation.exception.CalculationException;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bacceleratorfacades.exception.DomainException;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.commercefacades.order.CartQuoteUpdateValidator;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.exception.CartQuoteAccessException;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartAddressException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.ProtocolEntryModel;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.facade.WholescriptsCartFacade;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.SapUnitOfMeasureModel;
import com.wholescripts.core.service.WholescriptsCalculationService;
import com.wholescripts.core.service.WholescriptsCartService;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsMedPaxService;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.core.service.WholescriptsRecommendService;
import com.wholescripts.core.strategies.WholescriptsCommerceUpdateCartStrategy;
import com.wholescripts.data.AddtocartRequestData;
import com.wholescripts.data.CartOrderItemsItemData;
import com.wholescripts.data.CartOrderItemsReponseData;
import com.wholescripts.data.OrderSimulateItemData;
import com.wholescripts.data.OrderSimulateItemListData;
import com.wholescripts.data.OrderSimulateResponseData;
import com.wholescripts.dto.ConsentUpdateBrandListWsDTO;
import com.wholescripts.dto.DosingWsDTO;
import com.wholescripts.dto.ItemTimeWsDTO;
import com.wholescripts.dto.MedPaxWsDTO;
import com.wholescripts.dto.MedpaxEntryWsDTO;
import com.wholescripts.dto.WsAddtoCartParam;
import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public class WholescriptsCartFacadeImpl extends DefaultCartFacade implements WholescriptsCartFacade
{
	private static final Logger LOG = LogManager.getLogger(WholescriptsCartFacadeImpl.class);
	public static final String CART_QUOTE_STATUS_VALIDATION_TOGGLE = "toggle.cart.quoteStatusValidation.enabled";
	public static final String MEDAX_PRODUCT = "medpax.product.medpax";
	public static final String PRIVATELABEL_MEDAX_PRODUCT="privatelabel.medpax.product.medpax";
	public static final String MEDAX_CARTON = "medpax.product.carton";
	public static final String MEDAX_PACKET = "medpax.product.packet";
	@Resource(name = "wholescriptsMedPaxDao")
	private WholescriptsMedPaxDao wholescriptsMedPaxDao;
	@Resource(name = "wsCommerceCartParameterConverter")
	private Converter<WsAddtoCartParam, WsCommerceCartParameter> wsCommerceCartParameterConverter;
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;
	@Resource(name = "wholescriptsProtocolService")
	private WholescriptsProtocolService wholescriptsProtocolService;
	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;

	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "wholescriptsDeliveryAddressValidator")
	private Validator wsDeliveryAddressValidator;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "wholescriptsCalculationService")
	private WholescriptsCalculationService wholescriptsCalculationService;
	@Resource(name = "wholescriptsRecommendService")
	private WholescriptsRecommendService wholescriptsRecommendService;

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "cartQuoteUpdateValidator")
	private CartQuoteUpdateValidator cartQuoteUpdateValidator;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "wholescriptsCartService")
	private WholescriptsCartService wholescriptsCartService;
	@Resource(name = "wholescriptsMedPaxService")
	WholescriptsMedPaxService wholescriptsMedPaxService;
	@Resource(name = "wholescriptsMedpaxReverseConverter")
	private Converter<MedPaxWsDTO, MedPaxModel> wholescriptsMedpaxReverseConverter;
	@Resource(name = "wholescriptsCoreService")
	private WholescriptsCoreService wholescriptsCoreService;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;
	@Resource(name = "wholescriptsItemTimeDao")
	private WholescriptsItemTimeDao wholescriptsItemTimeDao;
	@Resource(name = "wsCommerceUpdateCartStrategy")
	WholescriptsCommerceUpdateCartStrategy wsCommerceUpdateCartStrategy;
	

	private static final Integer MINIMUM_SINGLE_SKU_ADD_CART = 0;
	private static final String BASKET_QUANTITY_ERROR_KEY = "basket.error.quantity.invalid";
	private static final String BASKET_QUANTITY_NOITEMADDED_ERROR_PREFIX_KEY = "basket.information.quantity.noItemsAdded";
	private static final String BASKET_QUANTITY_REDUCED_NUMBER_PREFIX_KEY = "basket.information.quantity.reducedNumberOfItemsAdded";
	private static final String CART_MODIFICATION_ERROR = "basket.error.occurred";
	public static final String BASKET_QUANTITY_REMOVE_SUCCESS = "basket.page.message.remove";

	@Override
	public CartData addToCart(final String medpaxId, final String protocolId, final String recommendationId,final boolean isWSPage)
			throws CommerceCartModificationException
	{
		MedPaxModel medpax = null;
		if (StringUtils.isNotEmpty(medpaxId))
		{
			String templateId="medpax-" + medpaxId;
			 addMedpaxInSessionCart(medpaxId,templateId);
		}
		else if (StringUtils.isNotEmpty(protocolId))
		{
			final Optional<ProtocolModel> protocol = wholescriptsProtocolService.getProtocolModelForID(protocolId, isWSPage);
			if (protocol.isPresent())
			{
				for (final ProtocolEntryModel entry : protocol.get().getProtocolEntry())
				{
					final WsAddtoCartParam params = new WsAddtoCartParam();
					params.setProductCode(entry.getProduct().getCode());
					params.setQuantity(entry.getQuantity());
					params.setIsRequestForCart(true);
					params.setTemplateId("protocol-" + protocolId);
					params.setSapTemplateId("protocol-" + protocolId);	
					params.setMedpaxEntryNumber(0);
					StringBuilder doseNote = new StringBuilder();
					doseNote.append(entry.getDosing().stream().map(d -> d.getQuantity()+" in "+ d.getItemTimes().get(0).getCode()).collect(Collectors.joining("|")));
					if(Objects.nonNull(entry.getItemDuration())) {
						doseNote.append("|");
						doseNote.append(enumerationService.getEnumerationName(entry.getItemDuration()));
					}
					params.setDoseNote(doseNote.toString());
					addToCartInternal(params);
				}
				final MedPaxModel medpaxForProtocol = wholescriptsMedPaxDao.getMedPaxForOwner(protocol.get());
				if (Objects.nonNull(medpaxForProtocol))
				{
					AbstractOrderEntryModel entry =isMedpaxAlreadyInCart(medpaxForProtocol);
					if(Objects.nonNull(entry)) {
						String qty = String.valueOf(entry.getAbstractOrderQty()+1);
						updateMedpaxCartEntry(medpaxForProtocol.getCode(),String.valueOf(entry.getEntryNumber()), qty);
					}else {
						String templateId="protocol-" + protocolId;
						addMedpaxInSessionCart(medpaxForProtocol.getCode(),templateId);
					}	
				}
			}
			else
			{
				throw new RequestParameterException(String.format("Model not found for this id:-" + protocolId),
						RequestParameterException.UNKNOWN_IDENTIFIER);
			}
		}
		else if (StringUtils.isNotEmpty(recommendationId))
		{
			final RecommendationModel recommendation = wholescriptsRecommendService.getRecommendation(recommendationId);
			if (Objects.nonNull(recommendation))
			{
				for (final AbstractOrderEntryModel entry : recommendation.getEntries())
				{
					final WsAddtoCartParam params = new WsAddtoCartParam();
					params.setProductCode(entry.getProduct().getCode());
					params.setQuantity(entry.getQuantity());
					params.setIsRequestForCart(true);
					params.setTemplateId("recommendation-" + recommendationId);
					params.setSapTemplateId("recommendation-" + recommendationId);			
					params.setMedpaxEntryNumber(0);
					RecommendationEntryModel e= (RecommendationEntryModel) entry;
					StringBuilder doseNote = new StringBuilder();
					doseNote.append(e.getDosing().stream().map(d -> d.getQuantity()+" in "+ d.getItemTimes().get(0).getCode()).collect(Collectors.joining("|")));
					if(Objects.nonNull(e.getItemDuration())) {
						doseNote.append("|");
						doseNote.append(enumerationService.getEnumerationName(e.getItemDuration()));
					}
					params.setDoseNote(doseNote.toString());
					addToCartInternal(params);
				}
				final MedPaxModel medpaxForRecommendation = wholescriptsMedPaxDao.getMedPaxForOwner(recommendation);
				if (Objects.nonNull(medpaxForRecommendation))
				{
					AbstractOrderEntryModel entry =isMedpaxAlreadyInCart(medpaxForRecommendation);
					if(Objects.nonNull(entry)) {
						String qty = String.valueOf(entry.getAbstractOrderQty()+1);
						updateMedpaxCartEntry(medpaxForRecommendation.getCode(),String.valueOf(entry.getEntryNumber()), qty);
					}else {
						String templateId="recommendation-" + recommendationId;
						addMedpaxInSessionCart(medpaxForRecommendation.getCode(),templateId);
					}
					
				}
			}
		}
		else
		{
			throw new RequestParameterException(
					String.format("Model not found for this id:-" + medpaxId + protocolId + recommendationId),
					RequestParameterException.UNKNOWN_IDENTIFIER);
		}
		return getSessionCart();
	}

	private AbstractOrderEntryModel isMedpaxAlreadyInCart(MedPaxModel medpax)
	{
		final CartModel cart = cartService.getSessionCart();
		final List<AbstractOrderEntryModel> entries = cart.getEntries().stream()
				.filter(e -> StringUtils.isNotEmpty(e.getTemplateId()) && e.getTemplateId().split("-")[1].equals(medpax.getCode()))
				.collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(entries)) {
			return entries.stream().filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers())).findFirst().orElse(null);
		}
		return null;
	}

	private void addMedpaxNameAndCode(final CartData cartData)
	{
		List<EntryGroupData> medpaxEntryGroupDataList = cartData.getRootGroups().stream()
				.filter(eg -> StringUtils.isNotEmpty(eg.getExternalReferenceId())).collect(Collectors.toList());
		for(EntryGroupData entryGroup : medpaxEntryGroupDataList) {
			MedPaxModel medpax =	wholescriptsMedPaxDao.getMedPaxByCode(entryGroup.getExternalReferenceId());
			if(Objects.nonNull(medpax)) {
				final OrderEntryData orderEntryData = cartData.getEntries().stream().filter(e -> e.getEntryNumber().equals(entryGroup.getGroupNumber()))
						.findFirst().orElse(null);
				if(Objects.nonNull(orderEntryData)) {
					orderEntryData.setMedpaxId(medpax.getCode());
					orderEntryData.setMedpaxName(medpax.getName());
				}	
			}
		}
	}
		
	private void addMedpaxInSessionCart(final String medpaxId,String sapTemplateId) throws CommerceCartModificationException
	{
		final MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxByCode(medpaxId);
		final Set<DosingModel> amDoisngList = new HashSet<>();
		final Set<DosingModel> pmDoisngList = new HashSet<>();
		final Set<DosingModel> dailyDoisngList = new HashSet<>();
		final Set<DosingModel> bedTimeDoisngList = new HashSet<>();
		if (Objects.nonNull(medpax)) {
			int entryNumber = 0;
			final CartModel cart = cartService.getSessionCart();
			entryNumber = checkIfMedpaxAlreadyAddedInCart(medpax, entryNumber, cart);
			boolean isPacketAdded = false;

			Map<String, DosingModel> medPaxEntriesMap = new HashMap<>();
			createDosingListPacketWise(medpax, amDoisngList, pmDoisngList, dailyDoisngList, bedTimeDoisngList, medPaxEntriesMap);

			final WsAddtoCartParam params = new WsAddtoCartParam();
			//adding medpax in cart
			String medpaxProduct= MEDAX_PRODUCT;
			if(medpax.getCarton() != null && !configurationService.getConfiguration().getString("medpax.product.carton").equals(medpax.getCarton().getCode())) {
				medpaxProduct= PRIVATELABEL_MEDAX_PRODUCT;
			}
			params.setProductCode(Config.getParameter(medpaxProduct));
			params.setQuantity(1);
			params.setMedpaxName(medpax.getName());
			params.setMedpaxId(medpaxId);
			params.setIsRequestForCart(true);
			params.setTemplateId("medpax-" + medpaxId);
			params.setSapTemplateId(sapTemplateId);
			params.setMedpaxEntryNumber(entryNumber);
			params.setAbstractOrderQty(medpax.getAbstractOrderQty());
			addToCartInternal(params);
			// adding carton in medpax
			final WsAddtoCartParam params1 = new WsAddtoCartParam();
			params1.setProductCode(Config.getParameter(MEDAX_CARTON));
			if (Objects.nonNull(medpax.getCarton())) {
				params1.setProductCode(medpax.getCarton().getCode());
			}
			params1.setQuantity(1);
			params1.setCartonName("Medpax printed Exterior");
			if (StringUtils.isNotEmpty(medpax.getCartonDescription())) {
				params1.setCartonName(medpax.getCartonDescription());
			}
			params1.setMedpaxId(medpaxId);
			params1.setIsRequestForCart(true);
			params1.setTemplateId("medpax-" + medpaxId);
			params.setSapTemplateId(sapTemplateId);
			entryNumber = updateMedpaxEntryNumber(entryNumber);
			params1.setMedpaxEntryNumber(entryNumber);
			params1.setHidden(Boolean.TRUE);
			params1.setAbstractOrderQty(medpax.getAbstractOrderQty());
			addToCartInternal(params1);
			final WsAddtoCartParam medpaxParam = new WsAddtoCartParam();
			medpaxParam.setMedpaxEntryNumber(entryNumber);
         boolean isPrivateLabelCarton=true;
			if(configurationService.getConfiguration().getString("medpax.product.carton").equals(medpax.getCarton().getCode())) {
				isPrivateLabelCarton=false;
			}
			for (final DosingModel dosing : amDoisngList)
			{
				String MedpxEntryName = getMedpaxEntryName(medPaxEntriesMap, dosing);
				isPacketAdded = addPacketEntryInCart(isPacketAdded, medpaxParam, dosing, medpaxId,
						medpaxParam.getMedpaxEntryNumber(),medpax.getAbstractOrderQty(),sapTemplateId);
				addMedpaxToCart(dosing, medpaxId, medpaxParam, medpaxParam.getMedpaxEntryNumber(),MedpxEntryName,medpax.getAbstractOrderQty(),isPrivateLabelCarton,sapTemplateId);
			}
			isPacketAdded = false;
			for (final DosingModel dosing : pmDoisngList)
			{
				String MedpxEntryName = getMedpaxEntryName(medPaxEntriesMap, dosing);
				isPacketAdded = addPacketEntryInCart(isPacketAdded, medpaxParam, dosing, medpaxId,
						medpaxParam.getMedpaxEntryNumber(),medpax.getAbstractOrderQty(),sapTemplateId);
				addMedpaxToCart(dosing, medpaxId, medpaxParam, medpaxParam.getMedpaxEntryNumber(),MedpxEntryName,medpax.getAbstractOrderQty(),isPrivateLabelCarton,sapTemplateId);
			}
			isPacketAdded = false;
			for (final DosingModel dosing : dailyDoisngList)
			{
				String MedpxEntryName = getMedpaxEntryName(medPaxEntriesMap, dosing);
				isPacketAdded = addPacketEntryInCart(isPacketAdded, medpaxParam, dosing, medpaxId,
						medpaxParam.getMedpaxEntryNumber(),medpax.getAbstractOrderQty(),sapTemplateId);
				addMedpaxToCart(dosing, medpaxId, medpaxParam, medpaxParam.getMedpaxEntryNumber(),MedpxEntryName,medpax.getAbstractOrderQty(),isPrivateLabelCarton,sapTemplateId);
			}
			isPacketAdded = false;
			for (final DosingModel dosing : bedTimeDoisngList)
			{
				String MedpxEntryName = getMedpaxEntryName(medPaxEntriesMap, dosing);
				isPacketAdded = addPacketEntryInCart(isPacketAdded, medpaxParam, dosing, medpaxId,
						medpaxParam.getMedpaxEntryNumber(),medpax.getAbstractOrderQty(),sapTemplateId);
				addMedpaxToCart(dosing, medpaxId, medpaxParam, medpaxParam.getMedpaxEntryNumber(),MedpxEntryName,medpax.getAbstractOrderQty(),isPrivateLabelCarton,sapTemplateId);
			}
			isPacketAdded = false;
			
			if (Objects.nonNull(cart) && medpax.getMedPaxOwner() instanceof B2BCustomerModel)
			{
				medpax.setMedPaxOwner(cart);
				getModelService().save(medpax);
			}
			entryNumber = 0;
		}
		else
		{
			throw new RequestParameterException(String.format("Model not found for this id:-" + medpaxId),
					RequestParameterException.UNKNOWN_IDENTIFIER);
		}
	}

	private String getMedpaxEntryName(Map<String, DosingModel> medPaxEntriesMap, final DosingModel dosing)
	{
		String MedpxEntryName = medPaxEntriesMap.entrySet()
		       .stream()
		       .filter(entry -> entry.getValue().equals(dosing)).map(d -> d.getKey()).findFirst().orElse("");
		return MedpxEntryName;
	}

	private int checkIfMedpaxAlreadyAddedInCart(final MedPaxModel medpax, int entryNumber, final CartModel cart)
	{
		final List<AbstractOrderEntryModel> entries = cart.getEntries().stream()
				.filter(e -> StringUtils.isNotEmpty(e.getTemplateId()) && e.getTemplateId().split("-")[1].equals(medpax.getCode()))
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(entries))
		{
			final List<AbstractOrderEntryModel> orderEntries = new ArrayList<>(cart.getEntries());
			final List<EntryGroup> filteredEntryGroupList = new ArrayList<>(cart.getEntryGroups());
			final AbstractOrderEntryModel orderEntry = entries.stream()
					.filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers())).findFirst().orElse(null);
			if (Objects.nonNull(orderEntry))
			{
				entryNumber = orderEntry.getEntryNumber();
			}
			final List<Integer> entryNumbers = entries.stream().map(e -> e.getEntryNumber()).collect(Collectors.toList());
			final List<EntryGroup> childEntryGroupList = new ArrayList<>();
			for (final Integer entryNum : entryNumbers)
			{
				final EntryGroup group = cart.getEntryGroups().stream().filter(eg -> eg.getGroupNumber().equals(entryNum)).findFirst()
						.orElse(null);
				if (Objects.nonNull(group))
				{
					childEntryGroupList.add(group);
				}
			}
			filteredEntryGroupList.removeAll(childEntryGroupList);
			orderEntries.removeAll(entries);
			cart.setEntries(orderEntries);
			cart.setEntryGroups(filteredEntryGroupList);
			getModelService().save(cart);
		}
		return entryNumber;
	}

	private int updateMedpaxEntryNumber(int entryNumber)
	{
		if (entryNumber > 0)
		{
			entryNumber += 2;
		}
		return entryNumber;
	}

	private boolean addPacketEntryInCart(boolean isPacketAdded, final WsAddtoCartParam params, final DosingModel dosing,
			final String medpaxId, int entryNumber, Long abstractOrderQty,String sapTemplateId) throws CommerceCartModificationException
	{
		if (!isPacketAdded)
		{
			final WsAddtoCartParam params2 = new WsAddtoCartParam();
			params2.setProductCode(Config.getParameter(MEDAX_PACKET));
			params2.setQuantity(1);
			params2.setPacketName(enumerationService.getEnumerationName(dosing.getItemTimes().get(0).getTimeOfTheDay()));
			params2.setMedpaxId(medpaxId);
			params2.setIsRequestForCart(true);
			params2.setTemplateId("medpax-" + medpaxId);
			params2.setSapTemplateId(sapTemplateId);
			params2.setAbstractOrderQty(abstractOrderQty);
			entryNumber = updateMedpaxEntryNumber(entryNumber);
			params2.setMedpaxEntryNumber(entryNumber);
			params.setMedpaxEntryNumber(entryNumber);
			addToCartInternal(params2);
			isPacketAdded = true;
		}
		return isPacketAdded;
	}

	private void createDosingListPacketWise(final MedPaxModel medpax, final Set<DosingModel> amDoisngList,
			final Set<DosingModel> pmDoisngList, final Set<DosingModel> dailyDoisngList, final Set<DosingModel> bedTimeDoisngList,Map<String,DosingModel> medPaxEntriesMap)
	{
		final List<MedpaxEntryModel> medpaxEntries = medpax.getMedpaxEntry();

		for (final MedpaxEntryModel medpaxEntry : medpaxEntries)
		{
			for (final DosingModel dosing : medpaxEntry.getDosing())
			{
				final Optional<ItemTimeModel> amItemTime = dosing.getItemTimes().stream()
						.filter(item -> item.getTimeOfTheDay().getCode().startsWith("AM")).findFirst();
				if (amItemTime.isPresent())
				{
					amDoisngList.add(dosing);
					medPaxEntriesMap.put(medpaxEntry.getEntryNumber(),dosing);
				}
				final Optional<ItemTimeModel> pmItemTime = dosing.getItemTimes().stream()
						.filter(item -> item.getTimeOfTheDay().getCode().startsWith("PM")).findFirst();
				if (pmItemTime.isPresent())
				{
					pmDoisngList.add(dosing);
					medPaxEntriesMap.put(medpaxEntry.getEntryNumber(),dosing);
				}
				final Optional<ItemTimeModel> dailyItemTime = dosing.getItemTimes().stream()
						.filter(item -> item.getTimeOfTheDay().getCode().startsWith("DAILY")).findFirst();
				if (dailyItemTime.isPresent())
				{
					dailyDoisngList.add(dosing);
					medPaxEntriesMap.put(medpaxEntry.getEntryNumber(),dosing);
				}
				final Optional<ItemTimeModel> bedTimeItemTime = dosing.getItemTimes().stream()
						.filter(item -> item.getTimeOfTheDay().getCode().startsWith("BEDTIME")).findFirst();
				if (bedTimeItemTime.isPresent())
				{
					bedTimeDoisngList.add(dosing);
					medPaxEntriesMap.put(medpaxEntry.getEntryNumber(),dosing);
				}
			}

		}
	}

	private void addMedpaxToCart(final DosingModel dosing, final String medpaxId, final WsAddtoCartParam medpaxParams,
			int entryNumber, String medPaxEntryKey,Long abstractOrderQty,boolean isPrivateLabelCarton,String sapTemplateId) throws CommerceCartModificationException
	{
		final WsAddtoCartParam params = new WsAddtoCartParam();
		params.setProductCode(dosing.getProduct().getCode());
		params.setQuantity(dosing.getQuantity());
		params.setMedpaxId(medpaxId);
		params.setIsRequestForCart(true);
		params.setTemplateId("medpax-" + medpaxId+"-"+medPaxEntryKey);
		params.setSapTemplateId(sapTemplateId);
		params.setAbstractOrderQty(abstractOrderQty);
		entryNumber = updateMedpaxEntryNumber(entryNumber);
		params.setMedpaxEntryNumber(entryNumber);
		medpaxParams.setMedpaxEntryNumber(entryNumber);
		params.setDoseNote(dosing.getQuantity() +" in "+ dosing.getItemTimes().get(0).getCode());
		params.setIsPrivateLabelCarton(isPrivateLabelCarton);
		addToCartInternal(params);
	}

	private void addToCartInternal(final WsAddtoCartParam addToCartParams) throws CommerceCartModificationException
	{
		final WsCommerceCartParameter parameter = wsCommerceCartParameterConverter.convert(addToCartParams);
		final CommerceCartModification modification = commerceCartService.addToCart(parameter);
	}


	@Override
	public CartData setCartDeliveryAddressInternal(final String addressId)
	{
		final AddressData address = new AddressData();
		address.setId(addressId);
		final Errors errors = new BeanPropertyBindingResult(address, "addressData");
		wsDeliveryAddressValidator.validate(address, errors);
		if (errors.hasErrors())
		{
			throw new CartAddressException("Address given by id " + sanitize(addressId) + " is not valid",
					CartAddressException.NOT_VALID, addressId);
		}
		if (checkoutFacade.setDeliveryAddress(address))
		{
			return getSessionCart();
		}
		throw new CartAddressException(
				"Address given by id " + sanitize(addressId) + " cannot be set as delivery address in this cart",
				CartAddressException.CANNOT_SET, addressId);
	}

	@Override
	public OrderSimulateResponseData orderSimulate(final CartModel cart, final BaseStoreModel baseStore) throws Exception {
		final OrderSimulateResponseData simulateResponse=wholescriptsIntegrationsService.orderSimulateORCreate(cart, true);
		if(Objects.nonNull(simulateResponse)) {
		final OrderSimulateItemListData itemList = simulateResponse.getItems();
		final String currency = cart.getSite().getStores().get(0).getDefaultCurrency().getIsocode();
		final List<String> excludedProducts = Arrays.asList(Config.getParameter("medpax.product.exclude").split(","));
		if (Objects.nonNull(itemList))
		{
			List<String> discountItemNumbers= List.of(configurationService.getConfiguration().getString("wholescripts.promotion.discount.cash.item.number").split(","));
			double totalCashDiscount=  itemList.getItem().stream().filter(i -> discountItemNumbers.contains(i.getItemNumber()) && StringUtils.isNotEmpty(i.getSapDiscount())).map(i -> Double.valueOf(i.getSapDiscount())).map(Math::abs).collect(Collectors.summingDouble(Double::doubleValue));
			for (final OrderSimulateItemData item : itemList.getItem())
			{
				for (final AbstractOrderEntryModel entry : cart.getEntries())
				{
					final String entryNumber = String.format("%06d", entry.getEntryNumber());
					if (!excludedProducts.contains(item.getMaterial())
							&& Integer.valueOf(entryNumber).equals(Integer.valueOf(item.getItemNumber())))
					{
						entry.setBasePrice(Double.valueOf(item.getSapPrice()));
						entry.setTotalPrice(Double.valueOf(item.getSapTotal()));
						final DiscountValue discount = DiscountValue.createAbsolute("cartDiscount",
								Math.abs(Double.valueOf(item.getSapDiscount())), currency);
						entry.setDiscountValues(List.of(discount));
						final TaxValue tax = new TaxValue("cartTax",
								Math.abs(Double.valueOf(item.getSapTax())), true, currency);
						entry.setTaxValues(List.of(tax));
						entry.setSubTotal(Double.valueOf(item.getSapSubtotal()));
						getModelService().save(entry);
					}
				}
			}
			try
			{
				wholescriptsCalculationService.recalculate(cart, false);
				if(Objects.nonNull(totalCashDiscount) && totalCashDiscount > 0.0) {
					double totalPrice =	cart.getTotalPrice()-totalCashDiscount;
					cart.setTotalPrice(totalPrice);
					totalCashDiscount +=cart.getTotalDiscounts();
					cart.setTotalDiscounts(totalCashDiscount);
					getModelService().save(cart);
				}

			}
			catch (final CalculationException ex)
			{
           LOG.info(ex.getMessage());
			}
		}
		}
		return simulateResponse;
	}
	
	@Override
	public CartData getSessionCart()
	{
		final CartData cartData;
		final CartModel cart = getCartService().getSessionCart();
		cartData = getCartConverter().convert(cart);
		addMedpaxNameAndCode(cartData);
		return cartData;
	}

	@Override
	public CartModificationData addToCart(String code, long quantity,String autoShipCode) throws CommerceCartModificationException {
		final AddToCartParams params = new AddToCartParams();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setAutoShipCode(autoShipCode);
		return addToCart(params);
	}

	@Override
	public CartModificationData addOrderEntry(OrderEntryData cartEntry) {
		validateIfCartCanBeUpdated();

		if (!isValidEntry(cartEntry))
		{
			throw new EntityValidationException(getLocalizedString(BASKET_QUANTITY_ERROR_KEY));
		}
		CartModificationData cartModification = null;
		try
		{
			cartModification = this.addToCart(cartEntry.getProduct().getCode(), cartEntry.getQuantity(),cartEntry.getAutoShipCode());
		}
		catch (final CommerceCartModificationException e)
		{
			throw new DomainException(getLocalizedString(CART_MODIFICATION_ERROR), e);
		}
		setAddStatusMessage(cartEntry, cartModification);
		return cartModification;
	}
	protected void validateIfCartCanBeUpdated()
	{
		final String currentUserId = getUserService().getCurrentUser().getUid();
		final String actingUserId = getSessionService().getAttribute(ACTING_USER_UID);
		final boolean updateAllowed = cartQuoteUpdateValidator.isCartQuoteUpdateAllowed(getSessionCart(), currentUserId,
				actingUserId);

		if (!updateAllowed)
		{
			if (cartQuoteStatusValidationEnabled())
			{
				throw new CartQuoteAccessException("Update is NOT allowed to cart with current quote.");
			}
			else
			{
				throw new DomainException(getLocalizedString(CART_MODIFICATION_ERROR));
			}
		}
	}
	private boolean cartQuoteStatusValidationEnabled()
	{
		return getConfigurationService().getConfiguration().getBoolean(CART_QUOTE_STATUS_VALIDATION_TOGGLE, false);
	}
	protected void setAddStatusMessage(final OrderEntryData orderEntry, final CartModificationData cartModification)
	{
		if ( cartModification == null ){
			return;
		}
		if (cartModification.getQuantityAdded() <= MINIMUM_SINGLE_SKU_ADD_CART)
		{
			cartModification.setStatusMessage(
					getLocalizedString(BASKET_QUANTITY_NOITEMADDED_ERROR_PREFIX_KEY + "." + cartModification.getStatusCode(), new Object[]
							{ cartModification.getEntry().getProduct().getName() }));
		}
		else if (cartModification.getQuantityAdded() < orderEntry.getQuantity())
		{
			cartModification.setStatusMessage(
					getLocalizedString(BASKET_QUANTITY_REDUCED_NUMBER_PREFIX_KEY + "." + cartModification.getStatusCode(), new Object[]
							{ cartModification.getEntry().getProduct().getName() }));
		}
	}
	protected boolean isValidEntry(final OrderEntryData cartEntry)
	{
		return (cartEntry.getProduct() != null && cartEntry.getProduct().getCode() != null) && cartEntry.getQuantity() != null
				&& cartEntry.getQuantity() >= MINIMUM_SINGLE_SKU_ADD_CART;
	}

	@Override
	public CartModificationData updateCartEntry(long entryNumber, long quantity,String autoShipCode) throws CommerceCartModificationException {
		validateCartQuote();

		AddToCartParams dto = new AddToCartParams();
		dto.setQuantity(quantity);
		if (StringUtils.isNotBlank(autoShipCode)) {
			dto.setAutoShipCode(autoShipCode);
		}
			final CommerceCartParameter parameter = getCommerceCartParameterConverter().convert(dto);


			if (parameter != null) {
				parameter.setEnableHooks(true);
				parameter.setEnableHooks(true);
				parameter.setEntryNumber(entryNumber);
			}
			final CommerceCartModification modification = getCommerceCartService().updateQuantityForCartEntry(parameter);
			return getCartModificationConverter().convert(modification);
	}

	@Override
	public void updateCartEntry(String entryNumbers) throws CommerceCartModificationException {
		List<Integer> entries = new ArrayList<>(Stream.of(entryNumbers.split(","))
				.map(Integer::parseInt)
				.toList());

		if(CollectionUtils.isNotEmpty(entries))
		{
			for (final Integer entryNumber : entries) {
					super.updateCartEntry(entryNumber, 0);
			}
			CartModel cart = getCartService().getSessionCart();
			if(Objects.nonNull(cart)) {
				wsCommerceUpdateCartStrategy.normalizeEntryNumbers(cart);
			}	
		}
	}

	@Override
	public CartModificationData updateCartEntry(long entryNumber, String storeId) throws CommerceCartModificationException {
		return super.updateCartEntry(entryNumber, storeId);
	}
	protected static void getCartEntryForNumber(final CartData cart, final long number)
	{
		final Integer requestedEntryNumber = (int) number;
		CollectionUtils.emptyIfNull(cart.getEntries()).stream()
				.filter(entry -> entry != null && requestedEntryNumber.equals(entry.getEntryNumber())).findFirst()
				.orElseThrow(() -> new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number)));
	}
	
	@Override
	public CartModificationData updateMedpaxCartEntry(String medpaxId, String entryNumber, String qty)
			throws CommerceCartModificationException
	{
		CartModificationData cartModeficationData= new CartModificationData();
		final CommerceCartModification modification = wholescriptsCartService.updateMedpaxCartEntry(medpaxId, entryNumber, qty);
		cartModeficationData.setStatusCode(modification.getStatusCode());
		cartModeficationData.setQuantity(modification.getQuantity());
		cartModeficationData.setQuantityAdded(modification.getQuantity());
		return cartModeficationData;
	}

	@Override
	public void deleteMedPaxEntryFromCart(String medpaxId, String entryNumber, String itemTimeCode)
	{
		wholescriptsCartService.deleteMedPaxEntryFromCart(medpaxId, entryNumber, itemTimeCode);
		try
		{
			addToCart(medpaxId,StringUtils.EMPTY,StringUtils.EMPTY,false);
		}
		catch (CommerceCartModificationException e)
		{
			throw new RequestParameterException(String.format("Model not found for this id:-" + medpaxId),
					RequestParameterException.UNKNOWN_IDENTIFIER);
		}
	}

	@Override
	public CartData addToCart(AddtocartRequestData orderDetails) throws Exception
	{
		CartModel cart= getCartService().getSessionCart();
		CartOrderItemsItemData itemData = orderDetails.getOrderItem();
		List<CartOrderItemsReponseData> items =itemData.getItems();
		List<CartOrderItemsReponseData> filtereditems = new ArrayList<>(items);
		List<CartOrderItemsReponseData> filteredMedpaxHeaderItems = items.stream().filter(i -> i.getMaterial().equals(configurationService.getConfiguration().getString(MEDAX_PRODUCT))).collect(Collectors.toList());
		List<CartOrderItemsReponseData> filteredPrivateLabelMedpaxHeaderItems = items.stream().filter(i -> i.getMaterial().equals(configurationService.getConfiguration().getString(PRIVATELABEL_MEDAX_PRODUCT))).collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(filteredPrivateLabelMedpaxHeaderItems)) {
			filteredMedpaxHeaderItems.addAll(filteredPrivateLabelMedpaxHeaderItems);
		}
		if(CollectionUtils.isNotEmpty(filteredMedpaxHeaderItems)) {
			MedPaxWsDTO medPaxWsDTO= new MedPaxWsDTO();
			for(CartOrderItemsReponseData item : filteredMedpaxHeaderItems) {
				medPaxWsDTO.setName(item.getDescription());
				medPaxWsDTO.setDescription(item.getDescription());
				medPaxWsDTO.setAbstractOrderQty((long) Double.parseDouble(item.getQuantity()));
				List<CartOrderItemsReponseData> childMedpaxItems =	items.stream().filter(i -> item.getSalesItem().equals(i.getSalesItemHigh())).collect(Collectors.toList());
				List<CartOrderItemsReponseData> childFormulaMedpaxItems = new ArrayList<>();
				String cartonItem="";
				List<MedpaxEntryWsDTO> medpaxEntry = new ArrayList<>();
				for(CartOrderItemsReponseData childItem :childMedpaxItems) {
				   List<CartOrderItemsReponseData> packetItems=items.stream().filter(i -> childItem.getSalesItem().equals(i.getSalesItemHigh())).collect(Collectors.toList());
				   if(CollectionUtils.isNotEmpty(packetItems)) {
					   createFormulaEntries(childFormulaMedpaxItems, medpaxEntry, childItem, packetItems,medPaxWsDTO.getAbstractOrderQty().intValue());   
				   }
				   else {
				   	cartonItem = childItem.getMaterial();
				   }
				}
				
				medPaxWsDTO.setMedpaxEntry(medpaxEntry);
				addToCartMedpax(cart, medPaxWsDTO, cartonItem);
				
				filtereditems.removeAll(childMedpaxItems);
				filtereditems.removeAll(childFormulaMedpaxItems);
			}
			filtereditems.removeAll(filteredMedpaxHeaderItems);
		}
		addToCartNonMedpax(filtereditems);	
		return getSessionCart();
	}

	@Override
	public void updateBrandConsentForCustomer(ConsentUpdateBrandListWsDTO request) throws Exception {
		wholescriptsCartService.updateBrandConsentForCustomer(request);
	}


	private void addToCartNonMedpax(List<CartOrderItemsReponseData> filtereditems)
	{
		filtereditems.forEach( i -> {
         long qty = getPrivateLabelQty(i);
			OrderEntryData orderEntryData =getOrderEntryData(qty,i.getMaterial());
			addOrderEntry(orderEntryData);
			
		});
	}

	private long getPrivateLabelQty(CartOrderItemsReponseData i)
	{
		long qty =0L;
		if(StringUtils.isNotEmpty(i.getCustMaterial())) {
		ProductModel product=	productService.getProductForCode(i.getMaterial());
		final SapUnitOfMeasureModel sapUnit = product.getSapUnitOfMeasure().stream().filter(s -> "CS".equals(s.getUnitType()))
				.findFirst().orElse(null);
		if(Objects.nonNull(sapUnit) && Objects.nonNull(sapUnit.getConversion()))
		{
			qty= (long) Double.parseDouble(i.getQuantity()) /	sapUnit.getConversion().longValue();
		}
		}else {
			qty = (long) Double.parseDouble(i.getQuantity());
		}
		return qty;
	}

	private void addToCartMedpax(CartModel cart, MedPaxWsDTO medPaxWsDTO, String cartonItem)
	{
		MedPaxModel medpax=wholescriptsMedPaxService.createMedPax(wholescriptsMedpaxReverseConverter.convert(medPaxWsDTO));
		try {
			medpax.setMedPaxOwner(cart);
			if(StringUtils.isNotEmpty(cartonItem)) {
				ProductModel product=productService.getProductForCode(cartonItem);
				medpax.setCarton(product);
			}
		   getModelService().save(medpax);
		   addMedpaxInSessionCart(medpax.getCode(),StringUtils.EMPTY);
		}catch (Exception e) {
			medpax.setMedPaxOwner(cart);
			getModelService().save(medpax);
		}
	}

	private void createFormulaEntries(List<CartOrderItemsReponseData> childFormulaMedpaxItems, List<MedpaxEntryWsDTO> medpaxEntry,
			CartOrderItemsReponseData childItem, List<CartOrderItemsReponseData> packetItems,final int qty)
	{
		for(CartOrderItemsReponseData packetItem : packetItems) {
			MedpaxEntryWsDTO medpaxEntryWsDTO= new MedpaxEntryWsDTO();
			medpaxEntryWsDTO.setQuantity((int) Double.parseDouble(packetItem.getQuantity())/(30*qty));
			medpaxEntryWsDTO.setPrice(StringUtils.isNotEmpty(packetItem.getUnitPrice()) ? Double.valueOf(packetItem.getUnitPrice()) : 0.0);
			ProductWsDTO product = new ProductWsDTO();
			product.setCode(packetItem.getMaterial());
			medpaxEntryWsDTO.setProduct(product);
			DosingWsDTO dosingWsDTO= new DosingWsDTO();
			dosingWsDTO.setQuantity((int) Double.parseDouble(packetItem.getQuantity())/(30*qty));
			dosingWsDTO.setProduct(product);
			ItemTimeWsDTO itemTimes = new ItemTimeWsDTO(); 
			final ItemTimeModel itemtimeModel = wholescriptsItemTimeDao.getItemTimeByName(childItem.getDescription());
			if(Objects.nonNull(itemtimeModel)) {
				itemTimes.setCode(itemtimeModel.getCode());
			}
			dosingWsDTO.setItemTimes(itemTimes);
			medpaxEntryWsDTO.setDosing(List.of(dosingWsDTO));
			medpaxEntry.add(medpaxEntryWsDTO);
			childFormulaMedpaxItems.add(packetItem);
		}
	}
	
	protected OrderEntryData getOrderEntryData(long quantity, String productCode) {
		final OrderEntryData orderEntry = new OrderEntryData();
		orderEntry.setQuantity(quantity);
		orderEntry.setProduct(new ProductData());
		orderEntry.getProduct().setCode(productCode);
		orderEntry.setEntryNumber(null);
		return orderEntry;
	}
}

