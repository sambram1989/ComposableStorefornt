package com.wholescripts.core.process.email.context;

import com.wholescripts.core.enums.ContactSelectedCategoryEnum;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ContactUsEmailContext extends AbstractEmailContext<StoreFrontCustomerProcessModel> {
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
    private static final Map<String, String> CONTACT_EMAIL_MAP = new HashMap<>();
    public ContactUsEmailContext(EnumerationService enumerationService) {
        this.enumerationService = enumerationService;
        initializeEmailMap();
    }
    private void initializeEmailMap() {
        List<ContactSelectedCategoryEnum> categoryList = enumerationService
                .getEnumerationValues(ContactSelectedCategoryEnum._TYPECODE);
        for (ContactSelectedCategoryEnum category : categoryList) {
            String email = Config.getParameter("contactus.email." + category.getCode());
            if (StringUtils.isNotEmpty(email)) {
                CONTACT_EMAIL_MAP.put(category.getCode(), email);
            }
        }
    }
    @Override
    public void init(StoreFrontCustomerProcessModel businessProcessModel, EmailPageModel emailPageModel) {
        super.init(businessProcessModel, emailPageModel);
        // Fetch category
        ContactSelectedCategoryEnum contactCategoryEnum = businessProcessModel.getWsContactUs().getContactSelectCat();
        String categoryCode = contactCategoryEnum != null ? contactCategoryEnum.getCode() : "GENERAL_INQUIRY";
        String categoryDisplayName = "General Inquiry"; // Default fallback
        if (contactCategoryEnum != null) {
            categoryDisplayName = enumerationService.getEnumerationName(contactCategoryEnum);
        }
        // Retrieve email from the preloaded map
        String email = CONTACT_EMAIL_MAP.getOrDefault(categoryCode, Config.getParameter("contactus.email.GENERAL_INQUIRY"));
        put("contactFirstName", businessProcessModel.getWsContactUs().getContactFirstName());
        put("contactLastName", businessProcessModel.getWsContactUs().getContactLastName());
        put("contactEmail", businessProcessModel.getWsContactUs().getContactEmail());
        put("contactMessage", businessProcessModel.getWsContactUs().getContactMesssage());
        put("contactAccountNumber",businessProcessModel.getWsContactUs().getContactAccountNumber());
        put("contactPhone",businessProcessModel.getWsContactUs().getContactPhone());
        put("contactSelectCat",categoryDisplayName);
        put(EMAIL, email);
        put(FROM_EMAIL, businessProcessModel.getWsContactUs().getContactEmail());
        put(FROM_DISPLAY_NAME, businessProcessModel.getWsContactUs().getContactFirstName());
        put(DISPLAY_NAME, businessProcessModel.getWsContactUs().getContactFirstName());
    }
}
