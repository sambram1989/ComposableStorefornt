package com.wholescripts.core.cronjobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholescripts.core.model.InventoryOutboundRequestModel;
import com.wholescripts.core.model.WholescriptsIntegrationsModel;
import com.wholescripts.core.model.WholescriptsOutboundCronjobModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.integrationservices.populator.ItemToMapConversionContext;
import de.hybris.platform.integrationservices.service.IntegrationObjectAndItemMismatchException;
import de.hybris.platform.integrationservices.service.IntegrationObjectConversionService;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorContextFactory;
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.outboundservices.facade.SyncParameters;
import de.hybris.platform.outboundservices.service.DestinationSearchService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

public class WholescriptsInvetoryJob extends AbstractJobPerformable<WholescriptsOutboundCronjobModel> {

    @Resource(name = "outboundServiceFacade")
    private OutboundServiceFacade outboundServiceFacade;
    @Resource(name = "destinationSearchService")
    private DestinationSearchService destinationSearchService;
    @Resource(name = "integrationObjectService")
    private IntegrationObjectService ioService;
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "integrationObjectConversionService")
    private IntegrationObjectConversionService integrationObjectConversionService;
    private static final String OUTBOUND_INVENTORY_DESTINATION = "scpiInventoryDestination";
    @Resource(name = "outboundServiceDefaultDecoratorContextFactory")
    private DecoratorContextFactory contextFactory;
    private static final String OUTBOUND_INVENTORY_REQUEST ="OutboundInventoryRequest";

    @Override
    public PerformResult perform(final WholescriptsOutboundCronjobModel job)
    {
        final Logger LOG = LoggerFactory.getLogger(WholescriptsInvetoryJob.class);
        final ConsumedDestinationModel destination = destinationSearchService.findDestination(OUTBOUND_INVENTORY_DESTINATION);
        final InventoryOutboundRequestModel wsRequest = (InventoryOutboundRequestModel) job.getJob()
                .getWholescriptsOutboundRequest();
        final WholescriptsIntegrationsModel integration = modelService.create(WholescriptsIntegrationsModel.class);
        integration.setUserId(job.getSessionUser());
        final SyncParameters params = SyncParameters.syncParametersBuilder().withPayloadObject(wsRequest)
                .withIntegrationObject(ioService.findIntegrationObject(OUTBOUND_INVENTORY_REQUEST)).withDestination(destination).build();
        outboundServiceFacade.send(params, String.class).subscribe(
                // onNext
                response -> {
                    integration.setResponse(response.getBody());
                    job.setResult(CronJobResult.SUCCESS);
                    job.setStatus(CronJobStatus.FINISHED);
                    LOG.info(String.format("Inventory Request has been successfully completed " + response.getBody()));

                }, error -> {
                    job.setResult(CronJobResult.ERROR);
                    job.setStatus(CronJobStatus.FINISHED);
                    integration.setResponse(error.getMessage());
                    LOG.info(String.format("Inventory Request failed with message [%n%s]", error.getMessage()));
                });
        saveRequest(integration, params, job);
        modelService.save(integration);
        return new PerformResult(job.getResult(), job.getStatus());
    }

    /**
     *
     */
    private void saveRequest(final WholescriptsIntegrationsModel integration, final SyncParameters params,
                             final WholescriptsOutboundCronjobModel job)
    {
        final DecoratorContext context = contextFactory.createContext(params);
        final ItemToMapConversionContext conversionContext = toConversionContext(context);
        final Map<String, Object> entity = integrationObjectConversionService.convert(conversionContext);

        try
        {
            final StringBuilder request = new StringBuilder();
            request.append(new ObjectMapper().writeValueAsString(entity)).append("-").append(job.getStartTime().toString());
            integration.setRequest(request.toString());
        }
        catch (final JsonProcessingException e)
        {

        }
    }

    private ItemToMapConversionContext toConversionContext(final DecoratorContext ctx)
    {
        return ctx.getIntegrationObjectItem().map(type -> new ItemToMapConversionContext(ctx.getPayloadObject(), type))
                .orElseThrow(() -> new IntegrationObjectAndItemMismatchException(ctx.getPayloadObject(), ctx.getIntegrationObject()));
    }
}
