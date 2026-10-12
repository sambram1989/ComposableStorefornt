/**
 *
 */
package com.wholescripts.occ.controllers;

import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commercefacades.promotion.CommercePromotionFacade;
import de.hybris.platform.commercefacades.promotion.PromotionOption;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservices.core.product.data.PromotionDataList;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.util.Sanitizer;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wholescripts.core.facade.WholescriptsCommercePromotionRestrictionFacade;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;


@Controller
@RequestMapping(value = "/{baseSiteId}/promotions")
@Tag(name = "WholeScripts Promotions")
public class WholeScriptsPromotionsController
{
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	private static final String ORDER_PROMOTION = "order";
	private static final String PRODUCT_PROMOTION = "product";
	private static final String ALL_PROMOTIONS = "all";
	private static final EnumSet<PromotionOption> OPTIONS = EnumSet.allOf(PromotionOption.class);

	@Resource(name = "commercePromotionFacade")
	private CommercePromotionFacade commercePromotionFacade;
	@Resource(name = "dataMapper")
	private DataMapper dataMapper;
	@Resource(name = "wsCommercePromotionRestrictionFacade")
	private WholescriptsCommercePromotionRestrictionFacade wsCommercePromotionRestrictionFacade;

	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(method = RequestMethod.GET)
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.promotions.priority")
	@ResponseBody
	@Operation(operationId = "getPromotions", summary = "Retrieves the promotions.", description = "Retrieves the promotions defined for a current base site. Requests pertaining to promotions have been developed "
			+ "for the previous version of promotions and vouchers and therefore some of them are currently not compatible with the new promotion engine.")
	@ApiBaseSiteIdParam
	public PromotionListWsDTO getPromotions(
			@Parameter(description = "Type of promotions that should be returned. Possible values are: <ul><li>all: All available promotions are "
					+ "returned. </li><li>product: Only product promotions are returned. </li><li>order: Only order promotions are returned. </li></ul>", schema = @Schema(allowableValues =
			{ "all", "product", "order" }), required = true)
			@RequestParam
			final String type, @Parameter(description = "Only promotions from this group are returned")
			@RequestParam(required = false)
			final String promotionGroup, @ApiFieldsParam(defaultValue = DEFAULT_FIELD_SET)
			@RequestParam(defaultValue = DEFAULT_FIELD_SET)
			final String fields)
	{
		validateTypeParameter(type);

		final PromotionDataList promotionDataList = new PromotionDataList();
		promotionDataList.setPromotions(getPromotionList(type, promotionGroup));
		return dataMapper.map(promotionDataList, PromotionListWsDTO.class, fields);
	}

	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.promotions.priority")
	@ResponseBody
	@Operation(operationId = "getPromotion", summary = "Retrieves the promotion.", description = "Retrieves the details of a promotion using the specified code. Requests pertaining to "
			+ "promotions have been developed for the previous version of promotions and vouchers and therefore some of them are currently not compatible with the new promotion engine.")
	@ApiBaseSiteIdParam
	public PromotionWsDTO getPromotion(@Parameter(description = "Promotion identifier (code)", required = true)
	@PathVariable
	final String code, @ApiFieldsParam(defaultValue = DEFAULT_FIELD_SET)
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields)
	{
		final PromotionData promotionData = wsCommercePromotionRestrictionFacade.getPromotion(code, OPTIONS);
		return dataMapper.map(promotionData, PromotionWsDTO.class, fields);
	}

	protected void validateTypeParameter(final String type)
	{
		if (!ORDER_PROMOTION.equals(type) && !PRODUCT_PROMOTION.equals(type) && !ALL_PROMOTIONS.equals(type))
		{
			throw new RequestParameterException(
					"Parameter type=" + sanitize(type)
							+ " is not supported. Permitted values for this parameter are : 'order', 'product' or 'all'",
					RequestParameterException.INVALID, "type");
		}
	}

	protected List<PromotionData> getPromotionList(final String type, final String promotionGroup)
	{
		if (promotionGroup == null || promotionGroup.isEmpty())
		{
			return getPromotionList(type);
		}

		List<PromotionData> promotions = null;
		if (ORDER_PROMOTION.equals(type))
		{
			promotions = getCommercePromotionFacade().getOrderPromotions(promotionGroup);
		}
		else if (PRODUCT_PROMOTION.equals(type))
		{
			promotions = getCommercePromotionFacade().getProductPromotions(promotionGroup);
		}
		else if (ALL_PROMOTIONS.equals(type))
		{
			promotions = getCommercePromotionFacade().getProductPromotions(promotionGroup);
			promotions.addAll(getCommercePromotionFacade().getOrderPromotions(promotionGroup));
			promotions = promotions.stream().filter(p -> p.getEnabled()).collect(Collectors.toList());
		}
		return promotions;

	}

	protected List<PromotionData> getPromotionList(final String type)
	{
		List<PromotionData> promotions = null;
		if (ORDER_PROMOTION.equals(type))
		{
			promotions = getCommercePromotionFacade().getOrderPromotions();
		}
		else if (PRODUCT_PROMOTION.equals(type))
		{
			promotions = getCommercePromotionFacade().getProductPromotions();
		}
		else if (ALL_PROMOTIONS.equals(type))
		{
			promotions = getCommercePromotionFacade().getProductPromotions();
			promotions.addAll(getCommercePromotionFacade().getOrderPromotions());
			promotions = promotions.stream().filter(p -> p.getEnabled()).collect(Collectors.toList());
		}
		return promotions;
	}

	protected CommercePromotionFacade getCommercePromotionFacade()
	{
		return commercePromotionFacade;
	}

	protected static String sanitize(final String input)
	{
		return Sanitizer.sanitize(input);
	}
}
