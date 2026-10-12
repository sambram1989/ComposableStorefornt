package com.wholescripts.core.event.listener;

import com.wholescripts.core.model.WSContactUsFormModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class WholescriptsThankYouEvent extends AbstractCommerceUserEvent {
    private WSContactUsFormModel contactUsForm;
    public WholescriptsThankYouEvent(WSContactUsFormModel contactUsForm) {
        this.contactUsForm = contactUsForm;
    }
    public void setContactUsForm(WSContactUsFormModel contactUsForm) {
        this.contactUsForm = contactUsForm;
    }
    public WSContactUsFormModel getContactUsForm() {
        return contactUsForm;
    }
}
