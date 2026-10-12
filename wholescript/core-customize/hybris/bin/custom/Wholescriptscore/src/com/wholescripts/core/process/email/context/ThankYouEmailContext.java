package com.wholescripts.core.process.email.context;

import com.wholescripts.core.enums.ContactSelectedCategoryEnum;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;

import javax.annotation.Resource;

public class ThankYouEmailContext extends AbstractEmailContext<StoreFrontCustomerProcessModel> {
    @Override
    protected BaseSiteModel getSite(StoreFrontCustomerProcessModel businessProcessModel) {
        return businessProcessModel.getSite();
    }
    @Override
    protected CustomerModel getCustomer(StoreFrontCustomerProcessModel businessProcessModel) {
        return businessProcessModel.getCustomer();
    }
    @Override
    protected LanguageModel getEmailLanguage(StoreFrontCustomerProcessModel businessProcessModel) {
        return businessProcessModel.getLanguage();
    }
    @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;
    @Override
    public void init(StoreFrontCustomerProcessModel businessProcessModel, EmailPageModel emailPageModel) {
        super.init(businessProcessModel, emailPageModel);
        ContactSelectedCategoryEnum contactCategoryEnum = businessProcessModel.getWsContactUs().getContactSelectCat();
        String categoryDisplayName = "General Inquiry"; // Default fallback
        if (contactCategoryEnum != null) {
            categoryDisplayName = enumerationService.getEnumerationName(contactCategoryEnum);
        }
        put("contactSelectCat",categoryDisplayName);
        put(EMAIL, businessProcessModel.getWsContactUs().getContactEmail());
        put(FROM_DISPLAY_NAME, "info@wholescripts.com");
        put(DISPLAY_NAME, "info@wholescripts.com");
    }
}
