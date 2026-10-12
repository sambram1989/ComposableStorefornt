package com.wholescripts.core.facade.impl;

import static java.util.stream.Collectors.toList;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.wholescripts.core.constants.GeneratedWholescriptscoreConstants.Enumerations.CustomerProfileType;
import com.wholescripts.core.facade.WholescriptsProtocolFacade;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.core.service.WholescriptsRecommendService;
import com.wholescripts.data.AddtoProtocolParam;
import com.wholescripts.data.CommerceProtocolParameter;
import com.wholescripts.data.protocol.ProtocolData;
import com.wholescripts.dto.protocol.ProtocolEntryWsDto;


public class WholescriptsProtocolFacadeImpl extends DefaultCartFacade implements WholescriptsProtocolFacade
{
	@Resource(name = "wholescriptsProtocolService")
	private WholescriptsProtocolService wholescriptsProtocolService;
	@Resource(name = "wsProtocolsConverter")
	private Converter<ProtocolModel, ProtocolData> wsProtocolsConverter;
	@Resource(name = "wsProtocolCodeGenerator")
	private KeyGenerator wsProtocolCodeGenerator;
	@Resource(name = "wsDosingCodeGenerator")
	private KeyGenerator wsDosingCodeGenerator;
	@Resource(name = "wsCommerceProtcolParameterConverter")
	private Converter<AddtoProtocolParam, CommerceProtocolParameter> wsCommerceProtcolParameterConverter;
	@Resource(name = "wholescriptsRecommendService")
	private WholescriptsRecommendService wholescriptsRecommendService;
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	public SearchPageData<ProtocolData> getProtocolListData(final SearchPageData searchPageData,final String protocolName,final boolean isWSPage,final boolean isNonDraft)
	{
		final SearchPageData<ProtocolModel> searchData = wholescriptsProtocolService.getProtocolsList(searchPageData,protocolName,isWSPage,isNonDraft);
		final SearchPageData<ProtocolData> result = new SearchPageData();
		final List<ProtocolData> protocolDataList = searchData.getResults().stream()
				.map(protocolModel -> getProtocolData(protocolModel)).filter(Optional::isPresent).map(Optional::get)
				.collect(toList());
		result.setResults(protocolDataList);
		result.setPagination(searchData.getPagination());
		result.setSorts(searchData.getSorts());
		return result;
	}

	@Override
	public Optional<ProtocolData> getProtocolDetailsData(final String protocolId,final boolean isWSPage)
	{
		final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.getProtocolModelForID(protocolId,isWSPage);
		if (protocolModel.isPresent())
		{
			return Optional.of(getProtocolData(protocolModel.get()).get());
		}
		return Optional.empty();
	}

	public Optional<ProtocolData> getProtocolData(final ProtocolModel protocol)
	{
		return Optional.of(wsProtocolsConverter.convert(protocol));
	}

	@Override
	public Optional<ProtocolData> addNewProtocol(final CommerceProtocolParameter parameter)
	{
		final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.createProtocol(parameter);

		if (protocolModel.isPresent())
		{
			return Optional.of(wsProtocolsConverter.convert(protocolModel.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ProtocolData> addDuplicateProtocol(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException
	{
		final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.duplicateProtocol(parameter);

		if (protocolModel.isPresent())
		{
			return Optional.of(wsProtocolsConverter.convert(protocolModel.get()));
		}
		return Optional.empty();
	}

	@Override
	public CartModificationData createProtocolEntryForStore(final String productCode, final long quantity, final String protocolId,
			final ProtocolEntryWsDto entry, final String storeId) throws CommerceCartModificationException
	{
		final AddtoProtocolParam params = new AddtoProtocolParam();
		params.setProductCode(productCode);
		params.setQuantity(quantity);
		params.setProtocolId(protocolId);
		params.setStoreId(storeId);
		params.setDosingList(entry.getDosing());
		params.setItemDuration(entry.getItemDuration());
		params.setProductItemType(entry.getProductItemType());
		params.setNotes(entry.getNotes());
		return addToProtocol(params);
	}

	@Override
	public CartModificationData addToProtocol(final AddtoProtocolParam addToProtocolParams)
			throws CommerceCartModificationException
	{
		final CommerceProtocolParameter parameter = wsCommerceProtcolParameterConverter.convert(addToProtocolParams);
		final CommerceCartModification modification = wholescriptsProtocolService.addToProtocol(parameter);
		return getCartModificationConverter().convert(modification);
	}

	@Override
	public CartModificationData createProtocolEntry(final String productCode, final long quantity, final String protocolId,
			final ProtocolEntryWsDto entry) throws CommerceCartModificationException
	{
		final AddtoProtocolParam params = new AddtoProtocolParam();
		params.setProductCode(productCode);
		params.setQuantity(quantity);
		params.setProtocolId(protocolId);
		params.setDosingList(entry.getDosing());
		params.setItemDuration(entry.getItemDuration());
		params.setProductItemType(entry.getProductItemType());
		params.setNotes(entry.getNotes());
		return addToProtocol(params);
	}

	@Override
	public CartModificationData updateProtocolEntry(final long entryNumber, final long quantity,final String protocolId, final ProtocolEntryWsDto entry)
			throws CommerceCartModificationException
	{
		final AddtoProtocolParam params = new AddtoProtocolParam();
		params.setQuantity(quantity);
		params.setProtocolId(protocolId);
		params.setDosingList(entry.getDosing());
		params.setItemDuration(entry.getItemDuration());
		params.setNotes(entry.getNotes());
		params.setProductCode(entry.getProduct().getCode());

		final CommerceProtocolParameter parameter = wsCommerceProtcolParameterConverter.convert(params);
		if (parameter != null)
		{
			parameter.setEnableHooks(true);
			parameter.setEnableHooks(true);
			parameter.setEntryNumber(entryNumber);
		}
		final CommerceCartModification modification = wholescriptsProtocolService.updateQuantityForProtocolEntry(parameter);
		return getCartModificationConverter().convert(modification);
	}

	@Override
	public CartModificationData updateProtocolEntry(final long entryNumber, final String storeId)
			throws CommerceCartModificationException
	{
		final AddtoProtocolParam param = new AddtoProtocolParam();
		param.setStoreId(storeId);
		final CommerceProtocolParameter parameter = wsCommerceProtcolParameterConverter.convert(param);
		if (parameter != null)
		{
			parameter.setEnableHooks(true);
			parameter.setEntryNumber(entryNumber);
		}
		final CommerceCartModification commerceCartModification;
		if (parameter != null && parameter.getPointOfService() == null)
		{
			commerceCartModification = wholescriptsProtocolService.updateToShippingModeForProtocolEntry(parameter);
		}
		else
		{
			commerceCartModification = getCommerceCartService().updatePointOfServiceForCartEntry(parameter);
		}
		return getCartModificationConverter().convert(commerceCartModification);
	}

	@Override
	public Optional<ProtocolData> deleteProtocol(final String protocolId)
	{
		final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.deleteProtocol(protocolId);

		if (protocolModel.isPresent())
		{
			return Optional.of(wsProtocolsConverter.convert(protocolModel.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ProtocolData> saveProtocol(final CommerceProtocolParameter parameter)
	{
		final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.saveProtocol(parameter);

		if (protocolModel.isPresent())
		{
			return Optional.of(wsProtocolsConverter.convert(protocolModel.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ProtocolData> shareProtocol(final CommerceProtocolParameter parameter)
	{
		final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.shareProtocol(parameter);
		if (protocolModel.isPresent())
		{
			return Optional.of(wsProtocolsConverter.convert(protocolModel.get()));
		}
		return Optional.empty();
	}
	@Override
	public SearchPageData<ProtocolData> getPatientProtocolListData(final SearchPageData searchPageData)
	{
		final SearchPageData<ProtocolModel> searchData = wholescriptsProtocolService.getPatientProtocolList(searchPageData);
		final SearchPageData<ProtocolData> result = new SearchPageData();
		if (searchData != null) {
			final List<ProtocolData> protocolDataList = searchData.getResults().stream()
					.map(protocolModel -> getProtocolData(protocolModel)).filter(Optional::isPresent).map(Optional::get)
					.collect(toList());
			result.setResults(protocolDataList);
			result.setPagination(searchData.getPagination());
			result.setSorts(searchData.getSorts());
			return result;
		}
		return result;
	}

	@Override
	public Optional<ProtocolModel> stopSharingProtocol(final String protocolId)
	{
		return wholescriptsProtocolService.stopSharingProtocol(protocolId);
	}

	@Override
	public CartModificationData deleteProtocolEntry(final long entryNumber, final long quantity,
			final String protocolId) throws CommerceCartModificationException
	{
		final AddtoProtocolParam params = new AddtoProtocolParam();
		params.setQuantity(quantity);
		params.setProtocolId(protocolId);
		final CommerceProtocolParameter parameter = wsCommerceProtcolParameterConverter.convert(params);
		if (parameter != null)
		{
			parameter.setEnableHooks(true);
			parameter.setEntryNumber(entryNumber);
		}
		final CommerceCartModification modification = getCommerceCartService().updateQuantityForCartEntry(parameter);;
		return getCartModificationConverter().convert(modification);
	}

	@Override
	public Optional<ProtocolData> clone(final String recommendationId, final String name) throws CommerceCartModificationException
	{
		final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
		if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PRACTITIONER))
		{
			final RecommendationModel recommendationModel = wholescriptsRecommendService.getRecommendation(recommendationId);
			final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.clone(recommendationModel, name);
			return Optional.of(wsProtocolsConverter.convert(protocolModel.get()));
		}
		return Optional.empty();
	}


}
