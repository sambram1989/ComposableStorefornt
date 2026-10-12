package com.wholescripts.core.service.impl;

import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.service.WholescriptsCategoryService;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.core.service.WholescriptsPageRenderingService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.dto.RenderingPageValidationDto;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.rendering.impl.DefaultPageRenderingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.function.Predicate;

public class WholescriptsPageRenderingServiceImpl extends DefaultPageRenderingService implements WholescriptsPageRenderingService {
    @Resource(name = "wholescriptsCategoryService")
    private WholescriptsCategoryService wholescriptsCategoryService;
    @Resource(name = "wholescriptsCoreService")
    private WholescriptsCoreService wholescriptsCoreService;
    /**
     * Returns {@link AbstractPageData} object based on pageLabelOrId or url.
     *
     * @param pageTypeCode  the page type
     * @param pageLabelOrId the page label or id. This field is used only when the page type is ContentPage.
     * @param url           the url depends on the page type. If the page type is ProductPage then the url should be a product
     *                      url. If the page type is CategoryPage then the url should be a category url. If the page type is
     *                      CatalogPage then the url should be a catalog page.
     * @return the {@link AbstractPageData} object.
     * @throws CMSItemNotFoundException if the page does not exists.
     */
    @Override
    public AbstractPageData getPageRenderingDataForUrl(final String pageTypeCode, final String pageLabelOrId, final String url)
            throws CMSItemNotFoundException
    {
        final String categoryCode = validateParametersWithUrl(pageTypeCode, pageLabelOrId, url);

        final String pageQualifier = getPageQualifier(pageLabelOrId, categoryCode);
        final RestrictionData restrictionData = getRestrictionData(pageTypeCode, categoryCode);
        final AbstractPageModel pageModel = getPageModel(pageTypeCode, pageQualifier);
        return getPageData(pageModel, restrictionData);
    }
    /**
     * Validates input parameters.
     *
     * @param pageTypeCode
     *           the page type code.
     * @param pageLabelOrId
     *           the page label or id.
     * @param url
     *           the code (brand url or category url)
     * @throws CMSItemNotFoundException
     *            when the page label or id is not valid
     */
    protected String validateParametersWithUrl(final String pageTypeCode, final String pageLabelOrId, final String url)
            throws CMSItemNotFoundException
    {
        WholescriptsBrandModel brand;
        final RenderingPageValidationDto validationDto = new RenderingPageValidationDto();
        validationDto.setPageTypeCode(pageTypeCode);
        validationDto.setUrl(url);
        validationDto.setPageLabelOrId(pageLabelOrId);
        try {
      	  if (StringUtils.equals("/catalog", url) || StringUtils.containsAny("/category", url) && !url.contains("/brand")) {
              final CategoryModel category = wholescriptsCategoryService.getCategoryForUrl(url);
              if(Objects.nonNull(category)) {
                  validationDto.setCategoryCode(category.getCode() != null ? category.getCode() : String.valueOf("catalog"));
                  validationDto.setCode(category.getCode() != null ? category.getCode() : String.valueOf("catalog"));
              } else {
              	 throw new CMSItemNotFoundException(getErrorMessage(pageLabelOrId));
              }
          } else if (StringUtils.isNotEmpty(url) && StringUtils.containsAny("/brand", url)) {
              brand = wholescriptsCoreService.getBrandCodeByBrandUrl(url);
              if(Objects.nonNull(brand)) {
                  validationDto.setCategoryCode(String.valueOf("catalog"));
                  validationDto.setCode(String.valueOf("catalog"));
              } else {
             	 throw new CMSItemNotFoundException(getErrorMessage(pageLabelOrId));
             }
          }
        } catch (final Exception e) {
      	  throw new CMSItemNotFoundException(getErrorMessage(pageLabelOrId));
        }
        try {
            getFacadeValidationService().validate(getRenderingPageValidator(), validationDto);
            if (StringUtils.equals("/catalog", url) || StringUtils.containsAny("/category", url) && !url.contains("/brand")) {
                final CategoryModel category = wholescriptsCategoryService.getCategoryForUrl(url);
                if(Objects.nonNull(category)) {
                    return category.getCode() != null ? category.getCode() : String.valueOf("catalog");
                }
            } else if (StringUtils.isNotEmpty(url) && StringUtils.containsAny("/brand", url)) {
                brand = wholescriptsCoreService.getBrandCodeByBrandUrl(url);
                if(Objects.nonNull(brand)) {
                    return String.valueOf("catalog");
                }
            }
        }
        catch (final ValidationException e)
        {
            final Predicate<FieldError> isPageNotFoundError = error -> "pageLabelOrId".equals(error.getField())
                    && CmsfacadesConstants.INVALID_PAGE_LABEL_OR_ID.equals(error.getCode());

            if (Objects.nonNull(e.getValidationObject()) //
                    && e.getValidationObject().getFieldErrors().stream().anyMatch(isPageNotFoundError))
            {
                throw new CMSItemNotFoundException(getErrorMessage(pageLabelOrId), e);
            }
            else {
                throw e;
            }
        }
        return String.valueOf("");
    }
    private static String getErrorMessage(final String pageLabelOrId)
    {
        String errorMessage = "No content page found matching the provided label or id: ";
        if (pageLabelOrId.startsWith(Cms2Constants.PAGE_ID_PREFIX))
        {
            errorMessage += pageLabelOrId.substring(Cms2Constants.PAGE_ID_PREFIX.length());
        }
        else if (pageLabelOrId.startsWith(Cms2Constants.PAGE_LABEL_PREFIX))
        {
            errorMessage += pageLabelOrId.substring(Cms2Constants.PAGE_LABEL_PREFIX.length());
        }
        else
        {
            errorMessage += pageLabelOrId;
        }
        return errorMessage;
    }
}
