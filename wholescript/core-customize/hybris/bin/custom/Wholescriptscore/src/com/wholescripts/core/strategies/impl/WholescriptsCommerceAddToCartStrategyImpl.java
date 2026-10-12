/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;


import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.ProtocolEntryModel;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.order.impl.DefaultAbstractOrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.enums.ItemDuration;
import com.wholescripts.core.enums.ItemType;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.SapUnitOfMeasureModel;
import com.wholescripts.core.model.SupportedAutoShipModel;
import com.wholescripts.core.service.WholescriptsAbstractOrderService;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.core.strategies.WholescriptsCommerceAddToCartStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.CommerceProtocolParameter;
import com.wholescripts.dto.DosingWsDTO;
import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public class WholescriptsCommerceAddToCartStrategyImpl extends DefaultCommerceAddToCartStrategy
		implements WholescriptsCommerceAddToCartStrategy
{
	public static final String MEDAX_PACKET = "medpax.product.packet";
	private static final String PRODUCT_NAME = "wholescripts.pdp.productname.classification.code";
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "wsRecommendOrderService")
	DefaultAbstractOrderService<RecommendationModel, RecommendationEntryModel> wsRecommendOrderService;
	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;
	@Resource(name = "wsProtocolOrderService")
	private DefaultAbstractOrderService<ProtocolModel, ProtocolEntryModel> wsProtocolOrderService;
	@Resource(name = "wsDosingCodeGenerator")
	private KeyGenerator wsDosingCodeGenerator;
	@Resource(name = "wsCodeGenerator")
	private KeyGenerator keyGenerator;
	@Resource(name = "commentService")
	private CommentService commentService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "wholescriptsItemTimeDao")
	private WholescriptsItemTimeDao WholescriptsItemTimeDaoImpl;
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "entryGroupService")
	private EntryGroupService entryGroupService;
	@Resource(name = "wholescriptsAbstractOrderService")
	private WholescriptsAbstractOrderService wholescriptsAbstractOrderService;

	@Resource(name = "wholescriptsCoreDao")
	private WholescriptsCoreDao wholescriptsCoreDao;

	@Resource(name = "wholescriptsProductsService")
	WholescriptsProductsService wholescriptsProductsService;
	@Resource(name = "classificationService")
	private ClassificationService classificationService;
	private List<EntryMergeFilter> entryMergeFilters = Collections.emptyList();
	@Resource(name = "timeService")
	private TimeService timeService;

	@Override
	public CommerceCartModification addToRecommend(final WsCommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		CommerceCartModification modification;

		final RecommendationModel recommendModel = (RecommendationModel) parameter.getCart();
		final ProductModel productModel = parameter.getProduct();
		final List<DosingModel> dosingModel = parameter.getDosing();
		final List<CommentModel> commentModel = parameter.getComment();

		final long quantityToAdd = parameter.getQuantity();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();

		this.beforeAddToCart(parameter);
		//		validateAddToCart(parameter);

		if (this.isProductForCode(parameter).booleanValue())
		{
			// So now work out what the maximum allowed to be added is (note that this may be negative!)
			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(recommendModel, productModel, quantityToAdd,
					deliveryPointOfService);
			final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
			final long cartLevel = checkCartLevel(productModel, recommendModel, deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			if (actualAllowedQuantityChange > 0)
			{
				// We are allowed to add items to the cart
				final RecommendationEntryModel entryModel = this.addRecommendEntry(parameter, actualAllowedQuantityChange);
				entryModel.setDosing(dosingModel);
				entryModel.setComments(commentModel);
				entryModel.setItemDuration(enumerationService.getEnumerationValue(ItemDuration.class, parameter.getItemDuration()));
				modelService.save(entryModel);
				if (StringUtils.isNotEmpty(parameter.getProductItemType()))
				{
					entryModel
							.setProductItemType(enumerationService.getEnumerationValue(ItemType.class, parameter.getProductItemType()));
				}

				final String statusCode = getStatusCodeAllowedQuantityChange(actualAllowedQuantityChange, maxOrderQuantity,
						quantityToAdd, cartLevelAfterQuantityChange);

				modification = createAddToCartResp(parameter, statusCode, entryModel, actualAllowedQuantityChange);
			}
			else
			{
				// Not allowed to add any quantity, or maybe even asked to reduce the quantity
				// Do nothing!
				final String status = getStatusCodeForNotAllowedQuantityChange(maxOrderQuantity, maxOrderQuantity);

				modification = createAddToCartResp(parameter, status, createEmptyCartEntry(parameter), 0);

			}
		}
		else
		{
			modification = createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE,
					createEmptyCartEntry(parameter), 0);
		}
		afterAddToCart(parameter, modification);
		// Here the entry is fully populated, so we can search for a similar one and merge.
		mergeEntry(modification, parameter);
		recommendModel.setDate(timeService.getCurrentTime());
		modelService.save(recommendModel);
		getCommerceCartCalculationStrategy().calculateCart(parameter);

		return modification;
	}


	protected Boolean isProductForCode(final WsCommerceCartParameter parameter)
	{

		final ProductModel productModel = parameter.getProduct();
		try
		{
			productService.getProductForCode(productModel.getCode());
		}
		catch (final UnknownIdentifierException e)
		{
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	protected RecommendationEntryModel addRecommendEntry(final WsCommerceCartParameter parameter,
			final long actualAllowedQuantityChange) throws CommerceCartModificationException
	{
		if (parameter.getUnit() == null)
		{
			parameter.setUnit(getUnit(parameter));
		}

		final RecommendationEntryModel cartEntryModel = wsRecommendOrderService.addNewEntry(
				(RecommendationModel) parameter.getCart(), parameter.getProduct(), actualAllowedQuantityChange, parameter.getUnit(),
				APPEND_AS_LAST, false);
		cartEntryModel.setDeliveryPointOfService(parameter.getPointOfService());

		return cartEntryModel;
	}

	@Override
	public CommerceCartModification addToProtocol(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException
	{
		CommerceCartModification modification;
		final ProtocolModel protocolModel = (ProtocolModel) parameter.getCart();
		final ProductModel productModel = parameter.getProduct();
		final long quantityToAdd = parameter.getQuantity();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();
		if (isProductForCode(parameter).booleanValue())
		{
			// So now work out what the maximum allowed to be added is (note that this may be negative!)
			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(protocolModel, productModel, quantityToAdd,
					deliveryPointOfService);
			final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
			final long cartLevel = checkCartLevel(productModel, protocolModel, deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;
			if (actualAllowedQuantityChange > 0)
			{
				final ProtocolEntryModel entryModel = this.addProtocolEntry(parameter, actualAllowedQuantityChange);
				getModelService().save(entryModel);
				final List<ProtocolEntryModel> protocolEntries = new ArrayList<>(protocolModel.getProtocolEntry());
				protocolEntries.add(entryModel);
				protocolModel.setProtocolEntry(protocolEntries);
				protocolModel.setDate(timeService.getCurrentTime());
				getModelService().save(protocolModel);

				final String statusCode = getStatusCodeAllowedQuantityChange(actualAllowedQuantityChange, maxOrderQuantity,
						quantityToAdd, cartLevelAfterQuantityChange);
				modification = createAddToCartResp(parameter, statusCode, entryModel, actualAllowedQuantityChange);
			}
			else
			{
				final String status = getStatusCodeForNotAllowedQuantityChange(maxOrderQuantity, maxOrderQuantity);
				modification = createAddToCartResp(parameter, status, createEmptyCartEntry(parameter), 0);
			}
		}
		else
		{
			modification = createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE,
					createEmptyCartEntry(parameter), 0);
		}

		afterAddToCart(parameter, modification);
		// Here the entry is fully populated, so we can search for a similar one and merge.
		mergeEntry(modification, parameter);
		getCommerceCartCalculationStrategy().calculateCart(parameter);
		return modification;
	}

	@Override
	protected long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel)
	{
		final long cartLevel = checkCartLevel(productModel, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(productModel, pointOfServiceModel);

		// How many will we have in our cart if we add quantity
		final long newTotalQuantity = cartLevel + quantityToAdd;

		// Now limit that to the total available in stock
		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();

		if (isMaxOrderQuantitySet(maxOrderQuantity))
		{
			final long newTotalQuantityAfterProductMaxOrder = Math.min(newTotalQuantityAfterStockLimit,
					maxOrderQuantity.longValue());
			return newTotalQuantityAfterProductMaxOrder - cartLevel;
		}
		return newTotalQuantityAfterStockLimit - cartLevel;
	}

	@Override
	protected long getAvailableStockLevel(final ProductModel productModel, final PointOfServiceModel pointOfServiceModel)
	{
		final Long availableStockLevel;

		if (pointOfServiceModel == null)
		{
			return getForceInStockMaxQuantity();
		}
		else
		{
			availableStockLevel = getCommerceStockService().getStockLevelForProductAndPointOfService(productModel,
					pointOfServiceModel);
		}

		if (availableStockLevel == null)
		{
			return getForceInStockMaxQuantity();
		}
		else
		{
			return availableStockLevel.longValue();
		}


	}


	protected ProtocolEntryModel addProtocolEntry(final CommerceProtocolParameter parameter,
			final long actualAllowedQuantityChange) throws CommerceCartModificationException
	{
		if (parameter.getUnit() == null)
		{
			parameter.setUnit(getUnit(parameter));
		}
		final ProtocolEntryModel protocolEntryModel = wsProtocolOrderService.addNewEntry((ProtocolModel) parameter.getCart(),
				parameter.getProduct(), actualAllowedQuantityChange, parameter.getUnit(), APPEND_AS_LAST, false);
		protocolEntryModel.setDeliveryPointOfService(parameter.getPointOfService());
		protocolEntryModel.setItemDuration(ItemDuration.ON_GOING);
		if (StringUtils.isNotEmpty(parameter.getItemDuration()))
		{
			protocolEntryModel
					.setItemDuration(enumerationService.getEnumerationValue(ItemDuration.class, parameter.getItemDuration()));
		}
		protocolEntryModel
				.setProductItemType(enumerationService.getEnumerationValue(ItemType.class, parameter.getProductItemType()));
		final List<DosingModel> dosingList = new ArrayList<>();
		parameter.getDosingList().stream().forEach(d -> {
			addDosingDetails(dosingList, d, parameter);
		});
		protocolEntryModel.setDosing(dosingList);
		if (StringUtils.isNotEmpty(parameter.getNotes()))
		{
			addComments(parameter, protocolEntryModel);
		}
		return protocolEntryModel;
	}

	private void addDosingDetails(final List<DosingModel> dosingList, final DosingWsDTO d,
			final CommerceProtocolParameter parameter)
	{
		final DosingModel dosingModel = getModelService().create(DosingModel.class);
		final List<ItemTimeModel> itemTimeList = new ArrayList<>();
		if (StringUtils.isNotEmpty(d.getItemTimes().getCode()))
		{
			final ItemTimeModel itemtimeModel = WholescriptsItemTimeDaoImpl.getItemTimeByCode(d.getItemTimes().getCode());
			itemTimeList.add(itemtimeModel);
		}
		dosingModel.setId(wsDosingCodeGenerator.generate().toString());
		dosingModel.setQuantity(d.getQuantity());
		dosingModel.setProduct(parameter.getProduct());
		dosingModel.setItemTimes(itemTimeList);
		dosingModel.setB2bCustomer(b2bCustomerService.getCurrentB2BCustomer());
		getModelService().save(dosingModel);
		dosingList.add(dosingModel);
	}

	private void addComments(final CommerceProtocolParameter parameters, final ProtocolEntryModel entry)
	{
		final DomainModel domainModel = commentService.getDomainForCode("ticketSystemDomain");
		final ComponentModel componentModel = commentService.getComponentForCode(domainModel, "ticketSystem");
		final CommentTypeModel commentTypeModel = commentService.getCommentTypeForCode(componentModel, "customerNote");

		final CommentModel commentModel = modelService.create(CommentModel.class);
		commentModel.setCode(keyGenerator.generate().toString());
		commentModel.setText(parameters.getNotes());
		commentModel.setAuthor(userService.getCurrentUser());
		commentModel.setComponent(componentModel);
		commentModel.setCommentType(commentTypeModel);
		modelService.save(commentModel);
		if (CollectionUtils.isNotEmpty(entry.getComments()))
		{
			final List<CommentModel> comments = new ArrayList<CommentModel>(entry.getComments());
			comments.add(commentModel);
			entry.setComments(comments);
		}
		else
		{
			entry.setComments(Arrays.asList(commentModel));
		}
	}

	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final CommerceCartModification modification = doAddToCart(parameter);
		afterAddToCart(parameter, modification);
		// Here the entry is fully populated, so we can search for a similar one and merge.
		boolean mergeEntry = true;
		if (parameter instanceof WsCommerceCartParameter)
		{
			final WsCommerceCartParameter wsPara = (WsCommerceCartParameter) parameter;
			if (StringUtils.isNotEmpty(wsPara.getMedpaxId()))
			{
				mergeEntry = false;
			}
		}

		if (parameter.isExternalCart())
		{
			mergeEntry = false;
		}
		if (mergeEntry)
		{
			mergeEntry(modification, parameter);
		}
		getCommerceCartCalculationStrategy().calculateCart(parameter);
		return modification;
	}

	protected Boolean canMerge(@Nonnull
	final AbstractOrderEntryModel mergeCandidate, @Nonnull
	final AbstractOrderEntryModel newEntry)
	{
		return getEntryMergeFilters().stream().map(filter -> filter.apply(mergeCandidate, newEntry)).filter(Boolean.FALSE::equals)
				.findAny().orElse(Boolean.TRUE);
	}


	@Override
	protected void mergeEntry(@Nonnull
	final CommerceCartModification modification, @Nonnull
	final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("modification", modification);
		if (modification.getEntry() == null || Objects.equals(modification.getEntry().getQuantity(), Long.valueOf(0L)))
		{
			// nothing to merge
			return;
		}
		ServicesUtil.validateParameterNotNullStandardMessage("parameter", parameter);
		if (parameter.isCreateNewEntry())
		{
			return;
		}
		final AbstractOrderModel cart = modification.getEntry().getOrder();
		if (cart == null)
		{
			// The entry is not in cart (most likely it's a stub)
			return;
		}

		AbstractOrderEntryModel mergeTarget = null;
		if (Objects.isNull(modification.getEntry().getSelectedAutoShip()))
		{
			mergeTarget = getEntryMergeStrategy().getEntryToMerge(cart.getEntries(), modification.getEntry());
		}
		else
		{
			final List<AbstractOrderEntryModel> mergeEntries = cart.getEntries().stream().filter(Objects::nonNull)
					.filter(e -> !modification.getEntry().equals(e))
					.filter(entry -> canMerge(entry, modification.getEntry()).booleanValue()).collect(Collectors.toList());
			final String autoshipCode = modification.getEntry().getSelectedAutoShip().getCode();
			final Optional<AbstractOrderEntryModel> mergeEntry = mergeEntries.stream()
					.filter(e -> Objects.nonNull(e.getSelectedAutoShip()) && e.getSelectedAutoShip().getCode().equals(autoshipCode))
					.findAny();

			if (mergeEntry.isEmpty())
			{
				return;
			}
			else
			{
				mergeTarget = mergeEntry.get();
			}
		}
		if (mergeTarget == null)
		{
			if (parameter.getEntryNumber() != CommerceCartParameter.DEFAULT_ENTRY_NUMBER)
			{
				throw new CommerceCartModificationException(
						"The new entry can not be merged into the entry #" + parameter.getEntryNumber() + ". Give a correct value or "
								+ CommerceCartParameter.DEFAULT_ENTRY_NUMBER + " to accept any suitable entry.");
			}
		}
		else
		{
			// Merge the original entry into the merge target and remove the original entry.
			final Map<Integer, Long> entryQuantities = new HashMap<>(2);
			entryQuantities.put(mergeTarget.getEntryNumber(),
					Long.valueOf(modification.getEntry().getQuantity().longValue() + mergeTarget.getQuantity().longValue()));
			entryQuantities.put(modification.getEntry().getEntryNumber(), Long.valueOf(0L));
			getCartService().updateQuantities(parameter.getCart(), entryQuantities);
			if (!MapUtils.isEmpty(entryQuantities))
			{
				final Map.Entry<Integer, Long> entry = entryQuantities.entrySet().iterator().next();
				updateQuantityByCase((CartEntryModel) mergeTarget, parameter.getProduct(), entry.getValue());
			}
			modification.setEntry(mergeTarget);
		}
	}

	@Override
	protected CommerceCartModification doAddToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		CommerceCartModification modification;
		final CartModel cartModel = parameter.getCart();
		final ProductModel productModel = parameter.getProduct();
		final long quantityToAdd = parameter.getQuantity();
		final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();
		this.beforeAddToCart(parameter);
		validateAddToCart(parameter);

		if (isProductForCode(parameter).booleanValue())
		{
			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
					deliveryPointOfService);
			final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
			final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			if (actualAllowedQuantityChange > 0)
			{
				final CartEntryModel entryModel = addCartEntry(parameter, actualAllowedQuantityChange);
				//create entry group and nested groups for medpax
				createEntryGroupAndNumberForMedpax(parameter, cartModel, entryModel);
				updateQuantityByCase(entryModel, parameter.getProduct(), quantityToAdd);
				getModelService().save(entryModel);
				final String statusCode = getStatusCodeAllowedQuantityChange(actualAllowedQuantityChange, maxOrderQuantity,
						quantityToAdd, cartLevelAfterQuantityChange);
				modification = createAddToCartResp(parameter, statusCode, entryModel, actualAllowedQuantityChange);
			}
			else
			{
				final String status = getStatusCodeForNotAllowedQuantityChange(maxOrderQuantity, maxOrderQuantity);
				modification = createAddToCartResp(parameter, status, createEmptyCartEntry(parameter), 0);
			}
		}
		else
		{
			modification = createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE,
					createEmptyCartEntry(parameter), 0);
		}
		return modification;
	}

	private void updateQuantityByCase(final CartEntryModel entryModel, final ProductModel product, final long quantityToAdd)
	{
		if (Objects.nonNull(product))
		{
			final boolean isPrivateLabel = wholescriptsProductsService.isPrivateLabel(product);
			if (isPrivateLabel)
			{
				setProductDescription(entryModel, product);
			}
			if (isPrivateLabel && Objects.nonNull(product.getSapUnitOfMeasure()))
			{
				final SapUnitOfMeasureModel sapUnit = product.getSapUnitOfMeasure().stream().filter(s -> "CS".equals(s.getUnitType()))
						.findFirst().orElse(null);
				if (Objects.nonNull(sapUnit) && Objects.nonNull(sapUnit.getConversion()))
				{
					entryModel.setQuantityByCase((int) (quantityToAdd * sapUnit.getConversion()));
				}
				else
				{
					entryModel.setQuantityByCase((int) (quantityToAdd));
				}
			}
			else
			{
				entryModel.setQuantityByCase((int) quantityToAdd);
			}
			getModelService().save(entryModel);
			getModelService().refresh(entryModel);

		}

	}

	private void setProductDescription(final CartEntryModel entryModel, final ProductModel product)
	{
		entryModel.setProductDescription(product.getGenericPLName());
		final String genericPLName = wholescriptsProductsService.getGenericPLNameFromCMI(product.getCode());
		if (StringUtils.isNotEmpty(genericPLName))
		{
			entryModel.setProductDescription(genericPLName);
		}
	}

	private void createEntryGroupAndNumberForMedpax(final CommerceCartParameter parameter, final CartModel cartModel,
			final CartEntryModel entryModel)
	{

		entryModel.setProductDescription(getProductName(entryModel.getProduct()));
		if (parameter instanceof WsCommerceCartParameter)
		{
			final WsCommerceCartParameter wsparameter = (WsCommerceCartParameter) parameter;
			entryModel.setTemplateId(wsparameter.getTemplateId());
			entryModel.setSapTemplateId(wsparameter.getSapTemplateId());			
			entryModel.setIsHidden(Boolean.FALSE);
			if (Objects.nonNull(wsparameter.getAbstractOrderQty()))
			{
				entryModel.setAbstractOrderQty(wsparameter.getAbstractOrderQty());
			}
			if (wsparameter.isHidden())
			{
				entryModel.setIsHidden(Boolean.TRUE);
			}

			if (wsparameter.isIsPrivateLabelCarton())
			{
				setProductDescription(entryModel, entryModel.getProduct());
			}
			createEntryGroupForMedpax(cartModel, wsparameter, entryModel);
			if (StringUtils.isNotEmpty(wsparameter.getCartonName()) || StringUtils.isNotEmpty(wsparameter.getPacketName()))
			{
				final Optional<EntryGroup> entryOptional = cartModel.getEntryGroups().stream()
						.filter(eg -> StringUtils.isNotEmpty(eg.getExternalReferenceId())
								&& eg.getExternalReferenceId().equals(wsparameter.getMedpaxId()))
						.findFirst();
				entryModel.setEntryGroupNumbers(Set.of(entryOptional.get().getGroupNumber()));
			}
			else if (StringUtils.isEmpty(wsparameter.getMedpaxName()) && StringUtils.isNotEmpty(wsparameter.getMedpaxId()))
			{
				final Integer entryNumber = cartModel.getEntries().stream()
						.filter(e -> e.getProduct().getCode().equals(Config.getParameter(MEDAX_PACKET))
								&& e.getTemplateId().split("-")[1].equals(wsparameter.getMedpaxId()))
						.map(AbstractOrderEntryModel::getEntryNumber).max(Integer::compareTo).orElse(0);
				entryModel.setEntryGroupNumbers(Set.of(entryNumber));
			}
		}
	}


	private String getProductName(final ProductModel productModel)
	{
		final String classificationKey = Config.getParameter(PRODUCT_NAME);
		final FeatureList featureList = classificationService.getFeatures(productModel);
		if (Objects.nonNull(featureList))
		{
			final Feature productNameFeature = featureList.getFeatureByCode(classificationKey);
			if (Objects.nonNull(productNameFeature) && productNameFeature.getValue() != null)
			{
				final FeatureValue productNameValue = productNameFeature.getValue();
				if (Objects.nonNull(productNameValue) && Objects.nonNull(productNameValue.getValue()))
				{
					final String productNameData = productNameValue.getValue().toString();
					if (StringUtils.isNotBlank(productNameData))
					{
						return productNameData;
					}
				}
			}
		}
		return productModel.getName();
	}

	@Override
	protected CartEntryModel addCartEntry(final CommerceCartParameter parameter, final long actualAllowedQuantityChange)
			throws CommerceCartModificationException
	{
		if (parameter.getUnit() == null)
		{
			parameter.setUnit(getUnit(parameter));
		}
		String medpaxId = StringUtils.EMPTY;
		String autoShipCode = StringUtils.EMPTY;
		int medpaxEntryNumber = 0;
		if (parameter instanceof WsCommerceCartParameter)
		{
			final WsCommerceCartParameter wsParameter = (WsCommerceCartParameter) parameter;
			medpaxId = wsParameter.getMedpaxId();
			medpaxEntryNumber = wsParameter.getMedpaxEntryNumber();
		}

		CartEntryModel cartEntryModel = null;
		if (parameter.isExternalCart())
		{

			cartEntryModel = (CartEntryModel) wholescriptsAbstractOrderService.addNewEntry(parameter.getCart(),
					parameter.getProduct(), actualAllowedQuantityChange, parameter.getUnit(),
					Math.toIntExact(parameter.getEntryNumber()), false, parameter.getEntryGroupNumbers().iterator().next());
			cartEntryModel.setDescription(parameter.getDescription());
		}
		else
		{

			cartEntryModel = (CartEntryModel) wholescriptsAbstractOrderService.addNewEntry(parameter.getCart(),
					parameter.getProduct(), actualAllowedQuantityChange, parameter.getUnit(), APPEND_AS_LAST, false, medpaxId,
					medpaxEntryNumber);
		}
		cartEntryModel.setDoseNote(StringUtils.isNotEmpty(parameter.getDoseNote()) ? parameter.getDoseNote() : StringUtils.EMPTY);
		cartEntryModel.setDeliveryPointOfService(parameter.getPointOfService());
		autoShipCode = parameter.getAutoShipCode();
		if (StringUtils.isNotBlank(autoShipCode))
		{
			final SupportedAutoShipModel autoShipModel = wholescriptsCoreDao.getAutoShipByCode(autoShipCode);
			if (Objects.nonNull(autoShipModel) && CollectionUtils.isNotEmpty(parameter.getProduct().getSupportedAutoShip()))
			{
				cartEntryModel.setSelectedAutoShip(autoShipModel);

			}
		}
		return cartEntryModel;
	}

	private void createEntryGroupForMedpax(final CartModel cartModel, final WsCommerceCartParameter wsparameter,
			final CartEntryModel entryModel)
	{
		if (StringUtils.isNotEmpty(wsparameter.getMedpaxName()) || StringUtils.isNotEmpty(wsparameter.getCartonName())
				|| StringUtils.isNotEmpty(wsparameter.getPacketName()))
		{
			final List<EntryGroup> entryGroupList = new ArrayList<>(cartModel.getEntryGroups());
			final EntryGroup entryGroup = new EntryGroup();
			entryGroup.setGroupNumber(entryModel.getEntryNumber());
			if (StringUtils.isNotEmpty(wsparameter.getMedpaxName()))
			{
				entryGroup.setLabel(wsparameter.getMedpaxName());
				entryGroup.setExternalReferenceId(wsparameter.getMedpaxId());
			}
			else if (StringUtils.isNotEmpty(wsparameter.getCartonName()))
			{
				entryGroup.setLabel(wsparameter.getCartonName());
			}
			else if (StringUtils.isNotEmpty(wsparameter.getPacketName()))
			{
				entryGroup.setLabel(wsparameter.getPacketName());
			}
			entryGroup.setGroupType(GroupType.MEDPAX);
			entryGroupList.add(entryGroup);
			cartModel.setEntryGroups(entryGroupList);
			modelService.save(cartModel);
			modelService.refresh(cartModel);
		}
	}




	/**
	 * @return the entryMergeFilters
	 */
	public List<EntryMergeFilter> getEntryMergeFilters()
	{
		return entryMergeFilters;
	}


	/**
	 * @param entryMergeFilters
	 *           the entryMergeFilters to set
	 */
	public void setEntryMergeFilters(final List<EntryMergeFilter> entryMergeFilters)
	{
		this.entryMergeFilters = entryMergeFilters;
	}
}



