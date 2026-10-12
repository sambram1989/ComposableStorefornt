package com.wholescripts.core.cronjobs;

import com.wholescripts.core.service.KlaviyoService;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.core.service.impl.KlaviyoServiceImpl;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

public class KlaviyoOrderedProductCronjob extends AbstractJobPerformable<CronJobModel> {

    private static final Logger LOG = LogManager.getLogger(KlaviyoServiceImpl.class);
    private static final int DELAY_IN_MILLISECONDS = 10_000;

    @Resource(name = "klaviyoService")
    private KlaviyoService klaviyoService;

    @Resource(name = "wholescriptsCoreService")
    private WholescriptsCoreService wholescriptsCoreService;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        List<OrderModel> orders =  wholescriptsCoreService.getOrdersForKlaviyo();

        for (OrderModel order : orders) {
            for (AbstractOrderEntryModel  abstractEntry : order.getEntries()) {
                try {
                    OrderEntryModel entry = (OrderEntryModel) abstractEntry;

                    if (CollectionUtils.isEmpty(entry.getEntryGroupNumbers())) {
                        klaviyoService.sendOrderedProductDetails(order, entry);

                        LOG.debug(String.format("Sent product details for order: %s, entry: %s", order.getCode(), entry.getEntryNumber()));
                        Thread.sleep(DELAY_IN_MILLISECONDS);
                    }

                } catch (InterruptedException e) {
                    LOG.error("Cronjob interrupted while waiting between requests.", e);
                    Thread.currentThread().interrupt();
                    return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
                } catch (Exception ex) {
                    LOG.error("Failed to send product details to Klaviyo.", ex);
                }
            }
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }
}
