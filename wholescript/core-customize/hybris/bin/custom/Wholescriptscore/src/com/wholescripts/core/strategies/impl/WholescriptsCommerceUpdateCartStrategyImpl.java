/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.ProtocolEntryModel;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.enums.ItemDuration;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.SapUnitOfMeasureModel;
import com.wholescripts.core.model.SupportedAutoShipModel;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.core.strategies.WholescriptsCommerceUpdateCartStrategy;
import com.wholescripts.data.CommerceProtocolParameter;
import com.wholescripts.dto.DosingWsDTO;
import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public class WholescriptsCommerceUpdateCartStrategyImpl extends DefaultCommerceUpdateCartEntryStrategy
		implements WholescriptsCommerceUpdateCartStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsCommerceUpdateCartStrategyImpl.class);
	public static final String MEDAX_PACKET = "medpax.product.packet";
	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;
	@Resource(name = "wsDosingCodeGenerator")
	private KeyGenerator wsDosingCodeGenerator;
	@Resource(name = "modelService")
	private ModelService modelService;
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

	@Resource(name = "wholescriptsCoreDao")
	private WholescriptsCoreDao wholescriptsCoreDao;

	@Resource(name = "wholescriptsProductsService")
	WholescriptsProductsService wholescriptsProductsService;
	@Resource(name = "wsCommerceAddToCartStrategy")
	WholescriptsCommerceAddToCartStrategyImpl wsCommerceAddToCartStrategy;
	@Resource(name = "timeService")
	private TimeService timeService;

	@Override
	public CommerceCartModification updateQuantityForRecommendEntry(final WsCommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		beforeUpdateCartEntry(parameter);
		final RecommendationModel recommendationModel = (RecommendationModel) parameter.getCart();
		final long newQuantity = parameter.getQuantity();
		final long entryNumber = parameter.getEntryNumber();

		validateParameterNotNull(recommendationModel, "Cart model cannot be null");
		CommerceCartModification modification;

		final RecommendationEntryModel entryToUpdate = (RecommendationEntryModel) this.getEntryForNumber(recommendationModel,
				(int) entryNumber);

		if (parameter.getDosing() != null)
		{
			final List<DosingModel> filteredDosingList = new ArrayList<>();
			for (final DosingModel dosing : parameter.getDosing())
			{
				final DosingModel oldDosing = entryToUpdate.getDosing().stream().filter(d -> d.getId().equals(dosing.getId()))
						.findFirst().orElse(null);
				if (Objects.nonNull(oldDosing))
				{
					oldDosing.setItemTimes(dosing.getItemTimes());
					oldDosing.setB2bCustomer(dosing.getB2bCustomer());
					oldDosing.setProduct(dosing.getProduct());
					oldDosing.setQuantity(dosing.getQuantity());
					modelService.save(oldDosing);
					filteredDosingList.add(oldDosing);
				}
				else
				{
					filteredDosingList.add(dosing);
				}
			}
			entryToUpdate.setDosing(filteredDosingList);
		}
		if (parameter.getComment() != null)
		{
			entryToUpdate.setComments(parameter.getComment());
		}
		if (parameter.getItemDuration() != null)
		{
			entryToUpdate.setItemDuration(enumerationService.getEnumerationValue(ItemDuration.class, parameter.getItemDuration()));
		}
		validateEntryBeforeModification(newQuantity, entryToUpdate);
		final Integer maxOrderQuantity = entryToUpdate.getProduct().getMaxOrderQuantity();
		// Work out how many we want to add (could be negative if we are
		// removing items)
		final long quantityToAdd = newQuantity - entryToUpdate.getQuantity().longValue();

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(recommendationModel, entryToUpdate.getProduct(),
				quantityToAdd, entryToUpdate.getDeliveryPointOfService());
		//Now do the actual cartModification
		modification = this.modifyEntry(recommendationModel, entryToUpdate, actualAllowedQuantityChange, newQuantity,
				maxOrderQuantity);
		afterUpdateCartEntry(parameter, modification);
		recommendationModel.setDate(timeService.getCurrentTime());
		modelService.save(recommendationModel);
		return modification;

	}

	protected AbstractOrderEntryModel getEntryForNumber(final RecommendationModel order, final int number)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null && !entries.isEmpty())
		{
			final Integer requestedEntryNumber = Integer.valueOf(number);
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
				{
					return entry;
				}
			}
		}
		return null;
	}


	protected CommerceCartModification modifyEntry(final RecommendationModel cartModel,
			final RecommendationEntryModel entryToUpdate, final long actualAllowedQuantityChange, final long newQuantity,
			final Integer maxOrderQuantity)
	{
		// Now work out how many that leaves us with on this entry
		final long entryNewQuantity = entryToUpdate.getQuantity().longValue() + actualAllowedQuantityChange;


		if (entryNewQuantity <= 0)
		{
			final RecommendationEntryModel entry = new RecommendationEntryModel()
			{
				@Override
				public Double getBasePrice()
				{
					return null;
				}

				@Override
				public Double getTotalPrice()
				{
					return null;
				}
			};
			entry.setOrder(cartModel);
			entry.setEntryGroupNumbers(new HashSet<>(entryToUpdate.getEntryGroupNumbers()));
			entry.setProduct(entryToUpdate.getProduct());

			// The allowed new entry quantity is zero or negative
			// just remove the entry
			modelService.remove(entryToUpdate);
			cartModel.setDate(timeService.getCurrentTime());
			modelService.save(cartModel);
			modelService.refresh(cartModel);
			normalizeEntryNumbers(cartModel);

			final WsCommerceCartParameter parameter = new WsCommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			getCommerceCartCalculationStrategy().calculateCart(parameter);

			// Return an empty modification
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setEntry(entry);
			modification.setQuantity(0);
			// We removed all the quantity from this row
			modification.setQuantityAdded(-entryToUpdate.getQuantity().longValue());

			if (newQuantity == 0)
			{
				modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			}
			else
			{
				modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			}

			return modification;
		}
		else
		{
			// Adjust the entry quantity to the new value
			entryToUpdate.setQuantity(Long.valueOf(entryNewQuantity));
			modelService.save(entryToUpdate);
			modelService.refresh(cartModel);
			final WsCommerceCartParameter parameter = new WsCommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			getCommerceCartCalculationStrategy().calculateCart(parameter);
			modelService.refresh(entryToUpdate);

			// Return the modification data
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setQuantityAdded(actualAllowedQuantityChange);
			modification.setEntry(entryToUpdate);
			modification.setQuantity(entryNewQuantity);

			if (isMaxOrderQuantitySet(maxOrderQuantity) && entryNewQuantity == maxOrderQuantity.longValue())
			{
				modification.setStatusCode(CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED);
			}
			else if (newQuantity == entryNewQuantity)
			{
				modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			}
			else
			{
				modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			}

			return modification;
		}
	}

	@Override
	public CommerceCartModification updateQuantityForProtocolEntry(final CommerceProtocolParameter parameters)
			throws CommerceCartModificationException
	{
		beforeUpdateCartEntry(parameters);
		final ProtocolModel protocolModel = (ProtocolModel) parameters.getCart();
		final long newQuantity = parameters.getQuantity();
		final long entryNumber = parameters.getEntryNumber();

		validateParameterNotNull(protocolModel, "protocol model cannot be null");
		CommerceCartModification modification;

		final ProtocolEntryModel entryToUpdate = getEntryForNumber(protocolModel, (int) entryNumber);
		validateEntryBeforeModification(newQuantity, entryToUpdate);
		final Integer maxOrderQuantity = entryToUpdate.getProduct().getMaxOrderQuantity();
		// Work out how many we want to add (could be negative if we are
		// removing items)
		final long quantityToAdd = newQuantity - entryToUpdate.getQuantity().longValue();

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(protocolModel, entryToUpdate.getProduct(),
				quantityToAdd, entryToUpdate.getDeliveryPointOfService());
		//Now do the actual cartModification
		modification = modifyEntry(protocolModel, entryToUpdate, actualAllowedQuantityChange, newQuantity, maxOrderQuantity);

		entryToUpdate.setItemDuration(enumerationService.getEnumerationValue(ItemDuration.class, parameters.getItemDuration()));
		if (StringUtils.isNotEmpty(parameters.getNotes()))
		{
			addComments(parameters, entryToUpdate);
		}
		final List<DosingModel> updatedDosingList = new ArrayList<>();
		parameters.getDosingList().stream().forEach(d -> {
			updateDosing(entryToUpdate.getDosing(), d, parameters, updatedDosingList);
		});
		entryToUpdate.setDosing(updatedDosingList);
		getModelService().save(entryToUpdate);
		protocolModel.setDate(timeService.getCurrentTime());
		getModelService().save(protocolModel);
		afterUpdateCartEntry(parameters, modification);
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

	private void addComments(final CommerceProtocolParameter parameters, final ProtocolEntryModel entryToUpdate)
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
		if (CollectionUtils.isNotEmpty(entryToUpdate.getComments()))
		{
			final List<CommentModel> comments = new ArrayList<CommentModel>(entryToUpdate.getComments());
			comments.add(commentModel);
			entryToUpdate.setComments(comments);
		}
		else
		{
			entryToUpdate.setComments(Arrays.asList(commentModel));
		}
	}

	protected ProtocolEntryModel getEntryForNumber(final ProtocolModel protocol, final int number)
	{
		final List<ProtocolEntryModel> entries = protocol.getProtocolEntry();
		if (entries != null && !entries.isEmpty())
		{
			final Integer requestedEntryNumber = Integer.valueOf(number);
			for (final ProtocolEntryModel entry : entries)
			{
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
				{
					return entry;
				}
			}
		}
		return null;
	}

	private void updateDosing(final List<DosingModel> dosingList, final DosingWsDTO d, final CommerceProtocolParameter parameter,
			final List<DosingModel> updatedDosingList)
	{
		final Optional<DosingModel> dosing = dosingList.stream().filter(dos -> dos.getId().equals(d.getId())).findFirst();
		DosingModel dosingModel;
		if (dosing.isPresent())
		{
			dosingModel = dosing.get();
			final List<ItemTimeModel> itemTimeList = new ArrayList<>();
			if (StringUtils.isNotEmpty(d.getItemTimes().getCode()))
			{
				final ItemTimeModel itemtimeModel = WholescriptsItemTimeDaoImpl.getItemTimeByCode(d.getItemTimes().getCode());
				itemTimeList.add(itemtimeModel);
			}
			dosingModel.setQuantity(d.getQuantity());
			dosingModel.setItemTimes(itemTimeList);
			getModelService().save(dosingModel);
			updatedDosingList.add(dosingModel);
		}
		else
		{
			dosingModel = getModelService().create(DosingModel.class);
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
			updatedDosingList.add(dosingModel);
		}
	}

	@Override
	public CommerceCartModification updateToShippingModeForProtocolEntry(final CommerceProtocolParameter parameters)
			throws CommerceCartModificationException
	{
		final ProtocolModel protocolModel = (ProtocolModel) parameters.getCart();
		final ProtocolEntryModel entryToUpdate = getEntryForNumber(protocolModel, (int) parameters.getEntryNumber());
		final CommerceCartModification modification = new CommerceCartModification();
		validateEntryBeforeSetShippingMode(entryToUpdate);

		final ProtocolEntryModel matchingShippingModeEntry = (ProtocolEntryModel) getEntryMergeStrategy()
				.getEntryToMerge(protocolModel.getEntries(), entryToUpdate);
		if (matchingShippingModeEntry != null)
		{
			return mergeEntries(entryToUpdate, matchingShippingModeEntry, protocolModel);
		}
		else
		{
			final long quantityBeforeChange = entryToUpdate.getQuantity().longValue();
			final long stockLevel = getAvailableStockLevel(entryToUpdate.getProduct(), parameters.getPointOfService());
			// Now limit that to the total available in stock
			final long newTotalQuantityAfterStockLimit = Math.min(quantityBeforeChange, stockLevel);
			entryToUpdate.setDeliveryPointOfService(null);
			entryToUpdate.setQuantity(Long.valueOf(newTotalQuantityAfterStockLimit));
			getModelService().save(entryToUpdate);
			protocolModel.setDate(timeService.getCurrentTime());
			getModelService().save(protocolModel);
			getModelService().refresh(protocolModel);
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(protocolModel);
			getCommerceCartCalculationStrategy().calculateCart(parameter);

			if (quantityBeforeChange == newTotalQuantityAfterStockLimit)
			{
				modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			}
			else
			{
				modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			}
			modification.setQuantity(entryToUpdate.getQuantity().longValue());
			modification.setEntry(entryToUpdate);
		}
		return modification;
	}

	@Override
	public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		boolean isMerge = true;
		if (parameters.getQuantity() == 0)
		{
			isMerge = false;
		}
		beforeUpdateCartEntry(parameters);
		final CartModel cartModel = parameters.getCart();
		final long newQuantity = parameters.getQuantity();
		final long entryNumber = parameters.getEntryNumber();

		validateParameterNotNull(cartModel, "Cart model cannot be null");
		CommerceCartModification modification;

		final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) entryNumber);
		validateEntryBeforeModification(newQuantity, entryToUpdate);
		final Integer maxOrderQuantity = entryToUpdate.getProduct().getMaxOrderQuantity();
		// Work out how many we want to add (could be negative if we are
		// removing items)
		final long quantityToAdd = newQuantity - entryToUpdate.getQuantity().longValue();

		// So now work out what the maximum allowed to be added is (note that
		// this may be negative!)
		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, entryToUpdate.getProduct(),
				quantityToAdd, entryToUpdate.getDeliveryPointOfService());
		if (StringUtils.isNotBlank(parameters.getAutoShipCode()))
		{
			final SupportedAutoShipModel autoShipModel = wholescriptsCoreDao.getAutoShipByCode(parameters.getAutoShipCode());
			if (CollectionUtils.isNotEmpty(cartModel.getEntryGroups()))
			{
				if (Objects.nonNull(autoShipModel) && CollectionUtils.isNotEmpty(entryToUpdate.getProduct().getSupportedAutoShip()))
				{
					final EntryGroup entryGroup = cartModel.getEntryGroups().stream()
							.filter(eg -> eg.getGroupNumber().equals(entryToUpdate.getEntryNumber())).findFirst().orElse(null);
					if (Objects.nonNull(entryGroup))
					{
						isMerge = false;
						final List<AbstractOrderEntryModel> entries = cartModel.getEntries().stream()
								.filter(e -> StringUtils.isNotEmpty(e.getTemplateId())
										&& e.getTemplateId().split("-")[1].equals(entryGroup.getExternalReferenceId()))
								.collect(Collectors.toList());
						for (final AbstractOrderEntryModel entry : entries)
						{
							entry.setSelectedAutoShip(autoShipModel);
							modelService.save(entry);
						}

					}
					else
					{
						entryToUpdate.setSelectedAutoShip(autoShipModel);
					}
				}
			}
			else
			{
				if (Objects.nonNull(autoShipModel) && CollectionUtils.isNotEmpty(entryToUpdate.getProduct().getSupportedAutoShip()))
				{
					entryToUpdate.setSelectedAutoShip(autoShipModel);

				}

			}

		}
		updateQuantityByCase(entryToUpdate, parameters);
		if (cartModel instanceof ProtocolModel)
		{
			cartModel.setDate(timeService.getCurrentTime());
			modelService.save(cartModel);
		}
		//Now do the actual cartModification
		modification = modifyEntry(cartModel, entryToUpdate, actualAllowedQuantityChange, newQuantity, maxOrderQuantity);
		if (isMerge)
		{
			try
			{
				parameters.setProduct(entryToUpdate.getProduct());
				wsCommerceAddToCartStrategy.mergeEntry(modification, parameters);
			}
			catch (final Exception ex)
			{
				LOG.error(ex.getMessage());
			}
		}
		afterUpdateCartEntry(parameters, modification);
		return modification;

	}

	private void updateQuantityByCase(final AbstractOrderEntryModel entryModel, final CommerceCartParameter parameter)
	{
		final ProductModel product = entryModel.getProduct();
		if (Objects.nonNull(product))
		{
			final boolean isPrivateLabel = wholescriptsProductsService.isPrivateLabel(product);
			if (isPrivateLabel && Objects.nonNull(product.getSapUnitOfMeasure()))
			{
				final SapUnitOfMeasureModel sapUnit = product.getSapUnitOfMeasure().stream().filter(s -> "CS".equals(s.getUnitType()))
						.findFirst().orElse(null);
				if (Objects.nonNull(sapUnit) && Objects.nonNull(sapUnit.getConversion()))
				{
					entryModel.setQuantityByCase((int) (parameter.getQuantity() * sapUnit.getConversion()));
				}
				else
				{
					entryModel.setQuantityByCase((int) (parameter.getQuantity()));
				}
			}
			else
			{
				entryModel.setQuantityByCase((int) parameter.getQuantity());
			}


		}


	}

	@Override
	protected CommerceCartModification modifyEntry(final CartModel cartModel, final AbstractOrderEntryModel entryToUpdate,
			final long actualAllowedQuantityChange, final long newQuantity, final Integer maxOrderQuantity)
	{
		// Now work out how many that leaves us with on this entry
		final long entryNewQuantity = entryToUpdate.getQuantity().longValue() + actualAllowedQuantityChange;

		final ModelService modelService = getModelService();

		if (entryNewQuantity <= 0)
		{
			final CartEntryModel entry = new CartEntryModel()
			{
				@Override
				public Double getBasePrice()
				{
					return null;
				}

				@Override
				public Double getTotalPrice()
				{
					return null;
				}
			};
			entry.setOrder(cartModel);
			entry.setEntryGroupNumbers(new HashSet<>(entryToUpdate.getEntryGroupNumbers()));
			entry.setProduct(entryToUpdate.getProduct());

			// The allowed new entry quantity is zero or negative
			// just remove the entry
			final EntryGroup entryGroup = cartModel.getEntryGroups().stream()
					.filter(eg -> eg.getGroupNumber().equals(entryToUpdate.getEntryNumber())).findFirst().orElse(null);
			if (Objects.nonNull(entryGroup))
			{
				final List<AbstractOrderEntryModel> deleteCartEntryList = new ArrayList<>();
				final List<EntryGroup> deleteEntryGroupList = new ArrayList<>();
				final List<EntryGroup> filteredEntryGroupList = new ArrayList<>(cartModel.getEntryGroups());
				deleteCartEntryList.add(entryToUpdate);
				final List<AbstractOrderEntryModel> entries = cartModel.getEntries().stream()
						.filter(e -> StringUtils.isNotEmpty(e.getTemplateId())
								&& e.getTemplateId().split("-")[1].equals(entryGroup.getExternalReferenceId())
								&& !entryToUpdate.getEntryNumber().equals(e.getEntryNumber()))
						.collect(Collectors.toList());
				final List<Integer> entryNumbers = entries.stream().map(e -> e.getEntryNumber()).collect(Collectors.toList());
				deleteCartEntryList.addAll(entries);
				final List<EntryGroup> childEntryGroupList = new ArrayList<>();
				for (final Integer entryNum : entryNumbers)
				{
					final EntryGroup group = cartModel.getEntryGroups().stream().filter(eg -> eg.getGroupNumber().equals(entryNum))
							.findFirst().orElse(null);
					if (Objects.nonNull(group))
					{
						childEntryGroupList.add(group);
					}
				}
				deleteEntryGroupList.add(entryGroup);
				deleteEntryGroupList.addAll(childEntryGroupList);
				filteredEntryGroupList.removeAll(deleteEntryGroupList);
				cartModel.setEntryGroups(filteredEntryGroupList);
				modelService.save(cartModel);
				modelService.removeAll(deleteCartEntryList);
			}
			else
			{
				modelService.remove(entryToUpdate);
			}
			modelService.refresh(cartModel);
			//normalizeEntryNumbers(cartModel);

			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			getCommerceCartCalculationStrategy().calculateCart(parameter);

			// Return an empty modification
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setEntry(entry);
			modification.setQuantity(0);
			// We removed all the quantity from this row
			modification.setQuantityAdded(-entryToUpdate.getQuantity().longValue());

			if (newQuantity == 0)
			{
				modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			}
			else
			{
				modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			}

			return modification;
		}
		else
		{
			final double eachPrice = entryToUpdate.getBasePrice() / entryToUpdate.getQuantity();
			entryToUpdate.setBasePrice(eachPrice * entryNewQuantity);
			if (CollectionUtils.isNotEmpty(entryToUpdate.getDiscountValues()))
			{
				entryToUpdate.getDiscountValues()
						.forEach(discountValue -> applyDiscount(entryToUpdate, discountValue, entryNewQuantity));
			}
			// Adjust the entry quantity to the new value
			entryToUpdate.setQuantity(Long.valueOf(entryNewQuantity));
			modelService.save(entryToUpdate);
			modelService.refresh(cartModel);
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			getCommerceCartCalculationStrategy().calculateCart(parameter);
			modelService.refresh(entryToUpdate);

			// Return the modification data
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setQuantityAdded(actualAllowedQuantityChange);
			modification.setEntry(entryToUpdate);
			modification.setQuantity(entryNewQuantity);

			if (isMaxOrderQuantitySet(maxOrderQuantity) && entryNewQuantity == maxOrderQuantity.longValue())
			{
				modification.setStatusCode(CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED);
			}
			else if (newQuantity == entryNewQuantity)
			{
				modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
			}
			else
			{
				modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
			}

			return modification;
		}
	}

	protected void applyDiscount(final AbstractOrderEntryModel entry, final DiscountValue discountValue, final long newQty)
	{
		final double eachDiscount = discountValue.getValue() / entry.getQuantity();
		final DiscountValue discount = DiscountValue.createAbsolute("cartDiscount", Math.abs(Double.valueOf(eachDiscount * newQty)),
				entry.getOrder().getSite().getStores().get(0).getDefaultCurrency().getIsocode());
		entry.setDiscountValues(List.of(discount));
	}

	@Override
	public void normalizeEntryNumbers(final CartModel cartModel)
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>(cartModel.getEntries());
		Collections.sort(entries, new BeanComparator(AbstractOrderEntryModel.ENTRYNUMBER, new ComparableComparator()));
		final List<AbstractOrderEntryModel> filteredEntries = entries.stream()
				.filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers())).collect(Collectors.toList());
		final List<AbstractOrderEntryModel> newEntries = new ArrayList<>();
		final List<EntryGroup> entryGroupList = new ArrayList<>();
		int entryNumber = 100;
		for (final AbstractOrderEntryModel entry : filteredEntries)
		{
			final EntryGroup entryGroup = cartModel.getEntryGroups().stream()
					.filter(eg -> eg.getGroupNumber().equals(entry.getEntryNumber())).findFirst().orElse(null);
			if (Objects.nonNull(entryGroup))
			{
				if (CollectionUtils.isEmpty(newEntries))
				{
					entry.setEntryNumber(entryNumber);
				}
				else
				{
					entryNumber += newEntries.stream().filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers()))
							.map(AbstractOrderEntryModel::getEntryNumber).max(Integer::compareTo).orElse(0);
					entry.setEntryNumber(entryNumber);
					entryNumber = 100;
				}
				createEntryGroupForMedpax(cartModel, entry, entryGroup, entryGroupList);
				modelService.save(entry);
				newEntries.add(entry);
				int childEntryNumber = entry.getEntryNumber();
				final List<AbstractOrderEntryModel> medpaxEntries = cartModel.getEntries().stream()
						.filter(e -> StringUtils.isNotEmpty(e.getTemplateId())
								&& e.getTemplateId().split("-")[1].equals(entryGroup.getExternalReferenceId())
								&& !entry.getEntryNumber().equals(e.getEntryNumber()))
						.collect(Collectors.toList());
				final List<Integer> entryNumbers = medpaxEntries.stream().map(e -> e.getEntryNumber()).collect(Collectors.toList());
				final List<EntryGroup> childEntryGroupList = new ArrayList<>();
				for (final Integer entryNum : entryNumbers)
				{
					final EntryGroup group = cartModel.getEntryGroups().stream().filter(eg -> eg.getGroupNumber().equals(entryNum))
							.findFirst().orElse(null);
					if (Objects.nonNull(group))
					{
						childEntryGroupList.add(group);
					}
				}
				for (final EntryGroup childEntryGroup : childEntryGroupList)
				{
					childEntryNumber += 2;
					final Integer requestedEntryNumber = Integer.valueOf(childEntryGroup.getGroupNumber());
					final AbstractOrderEntryModel childEntry = cartModel.getEntries().stream()
							.filter(e -> requestedEntryNumber.equals(e.getEntryNumber())).findFirst().orElse(null);

					final int oldEntryNumber = childEntry.getEntryNumber();
					childEntry.setEntryNumber(childEntryNumber);
					childEntry.setEntryGroupNumbers(Set.of(entry.getEntryNumber()));
					newEntries.add(childEntry);
					createEntryGroupForMedpax(cartModel, childEntry, childEntryGroup, entryGroupList);
					modelService.save(childEntry);
					if (Config.getParameter(MEDAX_PACKET).equals(childEntry.getProduct().getCode()))
					{
						final List<AbstractOrderEntryModel> childEntries = cartModel.getEntries().stream()
								.filter(e -> CollectionUtils.isNotEmpty(e.getEntryGroupNumbers())
										&& e.getEntryGroupNumbers().contains(oldEntryNumber))
								.collect(Collectors.toList());
						int doisingChildEntryNum = childEntry.getEntryNumber();
						for (final AbstractOrderEntryModel medpaxEntry : childEntries)
						{
							doisingChildEntryNum += 2;
							medpaxEntry.setEntryNumber(doisingChildEntryNum);
							medpaxEntry.setEntryGroupNumbers(Set.of(childEntry.getEntryNumber()));
							newEntries.add(medpaxEntry);
							modelService.save(medpaxEntry);
							childEntryNumber = medpaxEntry.getEntryNumber();
						}
					}
				}
			}
			else
			{
				if (CollectionUtils.isEmpty(newEntries))
				{
					entry.setEntryNumber(entryNumber);
				}
				else
				{
					entryNumber += newEntries.stream().filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers()))
							.map(AbstractOrderEntryModel::getEntryNumber).max(Integer::compareTo).orElse(0);
					entry.setEntryNumber(entryNumber);
					entryNumber = 100;
				}
				modelService.save(entry);
				newEntries.add(entry);
			}
		}
		cartModel.setEntryGroups(entryGroupList);
		modelService.save(cartModel);
		modelService.refresh(cartModel);
	}

	private void createEntryGroupForMedpax(final CartModel cartModel, final AbstractOrderEntryModel entryModel,
			final EntryGroup oldEntryGroup, final List<EntryGroup> entryGroupList)
	{
		final EntryGroup entryGroup = new EntryGroup();
		entryGroup.setGroupNumber(entryModel.getEntryNumber());
		entryGroup.setLabel(oldEntryGroup.getLabel());
		entryGroup.setExternalReferenceId(oldEntryGroup.getExternalReferenceId());
		entryGroup.setGroupType(GroupType.MEDPAX);
		entryGroupList.add(entryGroup);
	}
}
