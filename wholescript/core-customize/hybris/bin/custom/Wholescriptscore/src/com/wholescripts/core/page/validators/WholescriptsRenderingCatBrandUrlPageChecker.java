package com.wholescripts.core.page.validators;


import com.wholescripts.core.common.predicate.WholescriptsCatBrandUrlExistsPredicate;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.dto.RenderingPageValidationDto;
import de.hybris.platform.cmsfacades.rendering.validators.page.RenderingPageChecker;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

import java.util.function.Predicate;

/**
 * Implementation of {@link RenderingPageChecker} to validate Category page attributes of {@link RenderingPageValidationDto}.
 */

public class WholescriptsRenderingCatBrandUrlPageChecker implements RenderingPageChecker {
    private Predicate<String> pagePredicate;
    private WholescriptsCatBrandUrlExistsPredicate cmsWholescriptsCatBrandUrlExistsPredicate;

    /**
     * Predicate to test if a given page type code matches the page supplier.
     * <p>
     * Returns <tt>TRUE</tt> if the supplier exists; <tt>FALSE</tt> otherwise.
     * </p>
     */
    @Override
    public Predicate<String> getConstrainedBy() {
        return pagePredicate;
    }

    @Override
    public void verify(RenderingPageValidationDto validationDto, Errors errors) {
        if (getCmsWholescriptsCatBrandUrlExistsPredicate().negate().test(validationDto.getUrl())) {
            errors.rejectValue("url", CmsfacadesConstants.FIELD_NOT_ALLOWED);
        }
    }

    protected Predicate<String> getPagePredicate() {
        return pagePredicate;
    }

    @Required
    public void setPagePredicate(Predicate<String> pagePredicate) {
        this.pagePredicate = pagePredicate;
    }
    public WholescriptsCatBrandUrlExistsPredicate getCmsWholescriptsCatBrandUrlExistsPredicate() {
        return cmsWholescriptsCatBrandUrlExistsPredicate;
    }

    public void setCmsWholescriptsCatBrandUrlExistsPredicate(WholescriptsCatBrandUrlExistsPredicate cmsWholescriptsCatBrandUrlExistsPredicate) {
        this.cmsWholescriptsCatBrandUrlExistsPredicate = cmsWholescriptsCatBrandUrlExistsPredicate;
    }


}
