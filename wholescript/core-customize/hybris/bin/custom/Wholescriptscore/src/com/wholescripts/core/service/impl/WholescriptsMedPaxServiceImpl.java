/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import com.wholescripts.core.model.*;
import com.wholescripts.dto.UpdateMedPaxPacketsInfoWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.service.WholescriptsCalculationService;
import com.wholescripts.core.service.WholescriptsMedPaxService;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.core.service.WholescriptsRecommendService;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.MedpaxWebKitData;
import com.wholescripts.dto.MedPaxWsDTO;
import com.wholescripts.dto.MedpaxEntryWsDTO;
import com.wholescripts.dto.UpdateMedPaxPacketsWsDTO;

/**
 *
 */
public class WholescriptsMedPaxServiceImpl implements WholescriptsMedPaxService
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsMedPaxServiceImpl.class);
	private static final String BASE_STORE = "wholescripts.base.store";
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "wholescriptsMedPaxDao")
	private WholescriptsMedPaxDao wholescriptsMedPaxDao;
	@Resource(name = "wholescriptsMedpaxEntryReverseConverter")
	private Converter<MedpaxEntryWsDTO, MedpaxEntryModel> wholescriptsMedpaxEntryReverseConverter;

	@Resource(name = "wholescriptsItemTimeDao")
	private WholescriptsItemTimeDao wholescriptsItemTimeDaoImpl;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "wholescriptsProtocolService")
	private WholescriptsProtocolService wholescriptsProtocolService;
	@Resource(name = "wholescriptsRecommendService")
	private WholescriptsRecommendService wholescriptsRecommendService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "wholescriptsCalculationService")
	private WholescriptsCalculationService wholescriptsCalculationService;

	@Resource(name = "wsMedPaxEntryCodeGenerator")
	private KeyGenerator wsMedPaxEntryCodeGenerator;

	@Resource(name = "wsMedPaxCodeGenerator")
	private KeyGenerator wsMedPaxCodeGenerator;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "mediaModelConverter")
   private Converter<MediaModel, MediaData> mediaModelConverter;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "stockService")
	private StockService stockService;
	@Resource(name = "warehouseSelectionStrategy")
	private WarehouseSelectionStrategy warehouseSelectionStrategy;

	@Resource(name = "wholescriptsMedPaxService")
	WholescriptsMedPaxService wholescriptsMedPaxService;

	@Override
	public MedPaxModel createMedPax(final MedPaxModel medPaxModel)
	{
		modelService.saveAll(medPaxModel);
		return medPaxModel;
	}

	@Override
	public MedPaxModel updateMedPaxName(final MedPaxWsDTO medPaxWsDTO)
	{
		final MedPaxModel medPaxModel = wholescriptsMedPaxDao.getMedPaxByCode(medPaxWsDTO.getCode());
		medPaxModel.setName(medPaxWsDTO.getName());
		modelService.save(medPaxModel);
		return medPaxModel;
	}

	@Override
	public MedPaxModel updateMedPaxNotes(final MedPaxWsDTO medPaxWsDTO)
	{
		final MedPaxModel medPaxModel = wholescriptsMedPaxDao.getMedPaxByCode(medPaxWsDTO.getCode());
		medPaxModel.setNotes(medPaxWsDTO.getNotes());
		modelService.save(medPaxModel);
		return medPaxModel;
	}


	@Override
	public MedPaxModel getMedPaxForCurrentUser() {
		final B2BCustomerModel currentCustomer = b2bCustomerService.getCurrentB2BCustomer();
		MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxForOwner(currentCustomer);

		if (Objects.nonNull(medpax)) {
				try {
					wholescriptsCalculationService.calculateMedPax(medpax);
				} catch (CalculationException e) {
					throw new RuntimeException("Error calculating MedPax for current user", e);
				}
		}
		return medpax;
	}


	@Override
	public MedPaxModel getMedPaxByCode(final String code)
	{
		MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxByCode(code);
		if (Objects.nonNull(medpax)) {
			try {
				wholescriptsCalculationService.calculateMedPax(medpax);
			} catch (CalculationException e) {
				throw new RuntimeException("Error calculating MedPax for current user", e);
			}
	}
		return medpax;
	}

	@Override
	public MedPaxModel addEntryToMedpax(final MedpaxEntryModel MedPaxEntryModel, final String code)
	{
		final MedPaxModel medPaxModel = wholescriptsMedPaxDao.getMedPaxByCode(code);
		if (null == medPaxModel)
		{
			throw new ModelNotFoundException(code);
		}
		List<MedpaxEntryModel> entries = medPaxModel.getMedpaxEntry();
		if (entries == null)
		{
			entries = new ArrayList<>();
		}
		else
		{
			entries = new ArrayList<>(entries);
		}
		modelService.save(MedPaxEntryModel);
		entries.add(MedPaxEntryModel);
		medPaxModel.setMedpaxEntry(entries);
		modelService.save(medPaxModel);
		return medPaxModel;
	}

	@Override
	public MedpaxEntryModel updateMedpax(final MedpaxEntryWsDTO medpaxEntryWsDTO, final String entryNumber)
	{
		final MedpaxEntryModel medpaxEntryModel = wholescriptsMedPaxDao.getMedPaxEntryByNumber(entryNumber);
		if (null == medpaxEntryModel)
		{
			throw new ModelNotFoundException(entryNumber);
		}
		wholescriptsMedpaxEntryReverseConverter.convert(medpaxEntryWsDTO, medpaxEntryModel);
		modelService.save(medpaxEntryModel);
		return medpaxEntryModel;
	}

	@Override
	public MedPaxModel updateMedpaxPackets(final UpdateMedPaxPacketsWsDTO updateMedPaxPacketsWsDTO)
	{
		updateMedPaxPacketsWsDTO.getEntryNumber().forEach(entryNumber -> {
			final MedpaxEntryModel medpaxEntryModel = wholescriptsMedPaxDao.getMedPaxEntryByNumber(entryNumber);
			if (medpaxEntryModel == null)
			{
				throw new ModelNotFoundException(entryNumber);
			}
			// Retrieve the new ItemTimeModel once for reuse
			final ItemTimeModel newItemTimeModel = wholescriptsItemTimeDaoImpl
					.getItemTimeByCode(updateMedPaxPacketsWsDTO.getChangeItemTimeTo());
			if (newItemTimeModel == null)
			{
				throw new ModelNotFoundException(
						"ItemTimeModel not found for code: " + updateMedPaxPacketsWsDTO.getChangeItemTimeTo());
			}
			medpaxEntryModel.getDosing().forEach(dosingModel -> {
				// Set the new item time directly
				dosingModel.setItemTimes(List.of(newItemTimeModel));
				modelService.save(dosingModel);
			});

			// Save the updated MedpaxEntryModel
		});
		// Return the updated MedPaxModel
		return wholescriptsMedPaxDao.getMedPaxByCode(updateMedPaxPacketsWsDTO.getMedPaxId());
	}

	@Override
	public MedPaxModel updateMedpaxPacketsInfo(final UpdateMedPaxPacketsInfoWsDTO updateMedPaxPacketsInfoWsDTO) {

		final MedPaxModel medPaxModel = wholescriptsMedPaxService.getMedPaxByCode(updateMedPaxPacketsInfoWsDTO.getMedPaxId());
		List<MedpaxEntryModel> medpaxEntryModelList = medPaxModel.getMedpaxEntry();

		final ItemTimeModel newItemTimeModel = wholescriptsItemTimeDaoImpl
				.getItemTimeByCode(updateMedPaxPacketsInfoWsDTO.getChangeItemTimeTo());

		if (CollectionUtils.isNotEmpty(medpaxEntryModelList)) {
			medpaxEntryModelList.forEach(medpaxEntryModel -> {
				Optional.ofNullable(medpaxEntryModel.getDosing())
						.ifPresent(dosingModels -> dosingModels.forEach(dosingModel ->
								Optional.ofNullable(dosingModel.getItemTimes())
										.ifPresent(itemTimes -> itemTimes.forEach(itemTime -> {

											if (StringUtils.equalsIgnoreCase(updateMedPaxPacketsInfoWsDTO.getChangeItemTimeFrom(), itemTime.getCode())) {
												dosingModel.setItemTimes(Arrays.asList(newItemTimeModel));
												modelService.save(dosingModel);
											}
										}))
						));
			});
		}
		return medPaxModel;
	}

	@Override
	public MedPaxModel getMedPaxForOwner(final String protocolId, final String recommendationId, boolean isWSProtocol)
	{
		ItemModel item=null;
		if(StringUtils.isNotEmpty(protocolId)) {
			final Optional<ProtocolModel> protocol =wholescriptsProtocolService.getProtocolModelForCode(protocolId);
			if(protocol.isPresent()) {
				item = protocol.get();
			}
		}else if(StringUtils.isNotEmpty(recommendationId)) {
			item= wholescriptsRecommendService.getRecommendation(recommendationId);
		}
		final MedPaxModel medpax =wholescriptsMedPaxDao.getMedPaxForOwner(item);
		if(Objects.nonNull(medpax) && (medpax.getMedPaxOwner() instanceof RecommendationModel || medpax.getMedPaxOwner() instanceof ProtocolModel)) {
			try {
				wholescriptsCalculationService.calculateMedPax(medpax);
			} catch (CalculationException e) {
          LOG.debug(e.getMessage());
			}	
		}
		return  medpax;
	}

	@Override
	public void updateMedpaxHeader(final MedPaxModel medpax, final Integer box, final Integer packets, final Integer formulas)
	{
		medpax.setBoxes(box);
		medpax.setPacketsCount(packets);
		medpax.setFormula(formulas);
		modelService.save(medpax);
	}

	@Override
	public void removeEntryFromMedpax(final String medPaxEntryNumber, final String code) {

		final MedPaxModel medPaxModel = wholescriptsMedPaxDao.getMedPaxByCode(code);
		if (CollectionUtils.isNotEmpty(medPaxModel.getMedpaxEntry())) {

			List<MedpaxEntryModel> entries = new ArrayList<>(medPaxModel.getMedpaxEntry());

			final Optional<MedpaxEntryModel> entryToRemove = entries.stream().filter(entry -> StringUtils.equals(entry.getEntryNumber(), medPaxEntryNumber)).findFirst();
			if (entryToRemove.isPresent()) {
				entries.remove(entryToRemove.get());
				medPaxModel.setMedpaxEntry(entries);
				modelService.remove(entryToRemove.get());

				if (entries.isEmpty()) {
					modelService.remove(medPaxModel);
				} else {
					modelService.save(medPaxModel);
					modelService.refresh(medPaxModel);
				}

			}
		}
	}

	@Override
	public MedPaxModel cloneMedpax(MedPaxModel medPaxModel, CartModel cartModel) throws CommerceCartModificationException{
		MedPaxModel clonedMedPax = new MedPaxModel();
		clonedMedPax.setCode(wsMedPaxCodeGenerator.generate().toString());
		clonedMedPax.setName(medPaxModel.getName());
		clonedMedPax.setDescription(medPaxModel.getDescription());
		clonedMedPax.setMedPaxOwner(cartModel); // CartModel Could be either RecommendationModel or ProtocolModel.
		clonedMedPax.setBoxes(medPaxModel.getBoxes());
		clonedMedPax.setPacketsCount(medPaxModel.getPacketsCount());
		clonedMedPax.setFormula(medPaxModel.getFormula());
		clonedMedPax.setNotes(medPaxModel.getNotes());
		clonedMedPax.setMsrpTotalPrice(medPaxModel.getMsrpTotalPrice());
		clonedMedPax.setTotalPrice(medPaxModel.getTotalPrice());
		List<MedpaxEntryModel> medpaxEntryModelList = new ArrayList<>();
		for(MedpaxEntryModel medpaxEntryModel: medPaxModel.getMedpaxEntry())
		{
			cloneMedpaxEntry(medpaxEntryModelList,medpaxEntryModel);
		}
		if(Objects.nonNull(medPaxModel.getCarton()))
		{
			clonedMedPax.setCarton(medPaxModel.getCarton());
		}
		clonedMedPax.setMedpaxEntry(medpaxEntryModelList);
		modelService.save(clonedMedPax);
		modelService.refresh(clonedMedPax);
		return clonedMedPax;
	}

	private List<MedpaxEntryModel> cloneMedpaxEntry(List<MedpaxEntryModel> medpaxEntryModelList, MedpaxEntryModel source){
		MedpaxEntryModel target = modelService.create(MedpaxEntryModel.class);
		target.setPrice(source.getPrice());
		target.setQuantity(source.getQuantity());
		target.setMsrpPrice(source.getMsrpPrice());
		target.setEntryNumber(wsMedPaxEntryCodeGenerator.generate().toString());
		target.setProduct(source.getProduct());

		if (source.getDosing() != null)
		{
			target.setDosing(source.getDosing());
		}
		modelService.save(target);
		medpaxEntryModelList.add(target);
		return medpaxEntryModelList;
	}
	
	public Map<ProductModel,String> getCartonsFromCMIR()
	{
		Map<ProductModel,String> cartonMap = new HashMap<>();
		B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if(Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
			String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
			List<CustomerMaterialInformationModel> cmirList =wholescriptsMedPaxDao.getCMIRList(accountNumber);
			
			ProductModel defaultCarton = productService.getProductForCode(configurationService.getConfiguration().getString("medpax.product.carton"));
			cartonMap.put(defaultCarton, configurationService.getConfiguration().getString("medpax.product.carton.name"));
			for(CustomerMaterialInformationModel  cmir : cmirList) {
				if(StringUtils.isNotEmpty(cmir.getProductCode())) {
					try {
						ProductModel product =productService.getProductForCode(cmir.getProductCode());
						if(Objects.nonNull(product.getSapMaterialGroup()) && configurationService.getConfiguration().getString("wholescripts.carton.cmir.group").equals(product.getSapMaterialGroup().getCode())) {
							cartonMap.put(product, cmir.getProductDescription());
						}
					}catch (UnknownIdentifierException e) {

					}
				}
			}
		}
		return cartonMap;
	}

	@Override
	public void updateMedpaxForCarton(String productCode, String medpaxCode, String cartonName)
	{
		MedPaxModel medpax = getMedPaxByCode(medpaxCode);
		if(Objects.nonNull(medpax)) {
			ProductModel product =productService.getProductForCode(productCode);
			medpax.setCarton(product);
			medpax.setCartonDescription(cartonName);
			modelService.save(medpax);
		}
	}

	@Override
	public List<MedpaxWebKitData> getWebKitsFromCMIR()
	{
		List<MedpaxWebKitData> kitList = new ArrayList<>();
		B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if(Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
			String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
			List<CustomerMaterialInformationModel> cmirList =wholescriptsMedPaxDao.getCMIRList(accountNumber);
			for(CustomerMaterialInformationModel  cmir : cmirList) {
				if(StringUtils.isNotEmpty(cmir.getProductCode())) {
					MedpaxWebKitData kitData = new MedpaxWebKitData();
					try {
						ProductModel kitProduct =productService.getProductForCode(cmir.getProductCode());
						if(Objects.nonNull(kitProduct.getSapMaterialGroup()) && configurationService.getConfiguration().getString("wholescripts.webkit.cmir.group").equals(kitProduct.getSapMaterialGroup().getCode())) {
							kitData.setSku(kitProduct.getCode());
							kitData.setKitName(cmir.getProductDescription());
							if(StringUtils.isNotEmpty(cmir.getBlueLabel())) {
								ProductModel blueLabelProduct =productService.getProductForCode(cmir.getBlueLabel());
								if(Objects.nonNull(blueLabelProduct.getPicture())) {
									kitData.setImage(mediaModelConverter.convert(blueLabelProduct.getPicture()));
								}
								kitData.setBlueLabelSku(blueLabelProduct.getCode());
								kitData.setBluelabelName(blueLabelProduct.getName());
								kitData.setBluelabelQty(1);
								final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(configurationService.getConfiguration().getString(BASE_STORE));
								final Long stockLevel = totalUnit(
										stockService.getStockLevels(blueLabelProduct, warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore)));
								if(Objects.nonNull(stockLevel) && stockLevel > 0) {
									kitData.setAvilablityStatus(Boolean.TRUE);
								}
							}
							kitList.add(kitData);
						}
						
					}catch (UnknownIdentifierException e) {

					}
				}
			}
		}
		return kitList;
	}

	private Long totalUnit(final Collection<StockLevelModel> stockLevels)
	{
		long totalActualAmount = 0;
		for (final StockLevelModel stockLevel : stockLevels)
		{
			// If any stock level is flagged as FORCEINSTOCK then return null to indicate in stock
			if (InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()))
			{
				final long availableToSellQuantity = getAvailableToSellQuantity(stockLevel);
				if (availableToSellQuantity > 0 || !stockLevel.isTreatNegativeAsZero())
				{
					totalActualAmount += availableToSellQuantity;
				}
			}
		}
		return Long.valueOf(totalActualAmount);
	}
	
	protected long getAvailableToSellQuantity(final StockLevelModel stockLevel)
	{
		return stockLevel.getAvailable() - stockLevel.getReserved() + (long) stockLevel.getOverSelling();
	}
}
