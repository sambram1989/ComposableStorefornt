package com.wholescripts.core.actions;

import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.actions.SendEmailAction;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.task.RetryLaterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsEmailService;


public class WholescriptsSendEmailAction extends SendEmailAction
{

	private static final String ALLOWED_DOMAIN = "wholescripts.allowed.emails.domain";
	@Autowired
	private WholescriptsEmailService wholescriptsEmailService;

	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public void executeAction(final BusinessProcessModel businessProcessModel) throws RetryLaterException {

		if (businessProcessModel instanceof StoreFrontCustomerProcessModel) {
			final StoreFrontCustomerProcessModel processModel = (StoreFrontCustomerProcessModel) businessProcessModel;
			final BaseStoreModel baseStore = processModel.getStore();
			if(baseStore != null && baseStore.getIsNotificationSettingsEnabled()) {

				final List<EmailMessageModel> modifiedEmailMessages = new ArrayList<EmailMessageModel>();

				final List<String> unsubscribedEmails = wholescriptsEmailService.getUnsubscribedEmails();
				final List<EmailMessageModel> emailMessages = businessProcessModel.getEmails();

				for(final EmailMessageModel emailMessage : emailMessages) {
					for(final EmailAddressModel emailAddress :emailMessage.getToAddresses()) {
						if(!unsubscribedEmails.stream().anyMatch(emailAddress.getEmailAddress()::equalsIgnoreCase)) {
							modifiedEmailMessages.add(emailMessage);
						}
					}
				}

				for (final EmailMessageModel email : modifiedEmailMessages){
					getEmailService().send(email);

					final RecommendationModel recommendation = ((StoreFrontCustomerProcessModel) businessProcessModel).getRecommendation();
					if (recommendation != null) {
						recommendation.setSendEmail(true);
						modelService.save(recommendation);
					}
				}
			}else {
				List<String> allowedDomainList = new ArrayList<>();
				final String allowedDomain= configurationService.getConfiguration().getString(ALLOWED_DOMAIN);
				  if(StringUtils.isNotEmpty(allowedDomain) && allowedDomain.contains(",")) {
					  allowedDomainList.addAll( Arrays.asList(allowedDomain.split(",")));
				    }
				  final List<EmailMessageModel> modifiedEmailMessages = new ArrayList<EmailMessageModel>();
				  final List<EmailMessageModel> emailMessages = businessProcessModel.getEmails();
					for(final EmailMessageModel emailMessage : emailMessages) {
						for(final EmailAddressModel emailAddress :emailMessage.getToAddresses()) {
				      Optional<String> email=allowedDomainList.stream().filter(d  -> d.equalsIgnoreCase(emailAddress.getEmailAddress().split("@")[1])).findAny();
							if(email.isPresent()) {
								modifiedEmailMessages.add(emailMessage);
							}
						}
					}
					for (final EmailMessageModel email : modifiedEmailMessages){
						getEmailService().send(email);
					}
			}
		} else {
			super.executeAction(businessProcessModel);
		}
	}

}
