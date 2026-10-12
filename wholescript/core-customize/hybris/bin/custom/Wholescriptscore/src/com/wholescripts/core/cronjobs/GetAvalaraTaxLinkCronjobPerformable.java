package com.wholescripts.core.cronjobs;


import com.wholescripts.core.constants.WholescriptsIntegrationConstants;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.model.GetAvalaraTaxLinkCronjobModel;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsUserService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author viraldoshi
 * The Cronjob that processes the logic for retrieving the Avalara Tax link
 * for the Practitioner Reselling.
 */
public class GetAvalaraTaxLinkCronjobPerformable extends AbstractJobPerformable<GetAvalaraTaxLinkCronjobModel> {

    private static final Logger LOG = Logger.getLogger(GetAvalaraTaxLinkCronjobPerformable.class.getName());

    @Resource
    private WholescriptsUserService wholescriptsUserService;

    @Resource
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    Map<String, String> configParams = new HashMap<>();

    @Override
    public PerformResult perform(final GetAvalaraTaxLinkCronjobModel cronJobModel) {

        setConfigurationForAvalaraIntegration(configParams);

        LOG.info("Entered GetAvalaraTaxLinkCronjobPerformable");
        SearchResult<B2BCustomerModel> searchResult = wholescriptsUserService.getUserByCustomerProfile(String.valueOf(CustomerProfileType.PRACTITIONER));

        if (searchResult.getResult() != null && searchResult.getResult().size() > 0) {
            List<B2BCustomerModel> b2bPractitioners = searchResult.getResult();
            for (B2BCustomerModel b2bPractitioner : b2bPractitioners) {
                String avalaraTaxLink = b2bPractitioner.getAvalaraTaxLink();
                LOG.info(b2bPractitioner.getUid() + ":AVALARA TAX LINK:" + avalaraTaxLink);

                if (StringUtils.isEmpty(avalaraTaxLink)) {
                    wholescriptsIntegrationsService.integrateWith(configParams, b2bPractitioner);
                } else { // If Avalara Link is already set, then check its expiry date
                    LocalDateTime avalaraTaxLinkExpiryDate= null;
                    if(b2bPractitioner.getAvalaraTaxLinkExpiryDate()!=null) {
                         avalaraTaxLinkExpiryDate = b2bPractitioner.getAvalaraTaxLinkExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                    LocalDateTime today = LocalDateTime.now();
                    if (avalaraTaxLinkExpiryDate!=null && (today.isAfter(avalaraTaxLinkExpiryDate) || today.equals(avalaraTaxLinkExpiryDate))) {
                        wholescriptsIntegrationsService.integrateWith(configParams, b2bPractitioner);
                    }
                }
            }
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    private void setConfigurationForAvalaraIntegration(Map<String, String> configParams) {
        configParams.put("destination", WholescriptsIntegrationConstants.AVALARA);
        configParams.put("username",configurationService.getConfiguration().getString("avalara.userName") );
        configParams.put("password", configurationService.getConfiguration().getString("avalara.password"));
        configParams.put("avalaraURL", configurationService.getConfiguration().getString("avalara.url"));
        configParams.put("defaultAvalaraLink", configurationService.getConfiguration().getString("avalara.link.default.url"));
        configParams.put("authType", configurationService.getConfiguration().getString("avalara.authentication.type","Basic"));
        configParams.put("expiryPeriod", configurationService.getConfiguration().getString("avalara.default.expiry.period.indays","60"));
    }
}