/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.constants.WholescriptsstoreConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = WholescriptsstoreConstants.EXTENSIONNAME)
public class InitialDataSystemSetup extends AbstractSystemSetup
{
	private static final String IMPORT_HOME = "/Wholescriptsstore/import";
	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	private static final String WSSTOREID = "wholescripts";
	private static final String EXTENSIONNAME = "Wholescriptsstore";
	private static final String catalog_1710_10 = "catalog_1710_10_";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		// Add more Parameters here as you require

		return params;
	}

	/**
	 *
	 * @param context
	 *                   the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.INIT)
	public void createEssentialDataForInitialization(final SystemSetupContext context)
	{

		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/catalog.impex");
		;
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-content-powertools.impex");
		importImpexFile(context, IMPORT_HOME
				+ "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-responsive-content-spartacusSampleData.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/common/essential-data.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-content.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-responsive-content.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/site.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/site-responsive.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/site_en.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/store.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/store-responsive.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/store_en.impex");
		//product catalog
		importImpexFile(context, IMPORT_HOME + "/coredata/productCatalogs/catalog_1710_10_ProductCatalog/catalog.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/store.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/store-responsive.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/searchservices.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/searchservicestrigger.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/site.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/site-responsive.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/site_en.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/site-responsive.impex");
		//Sample data
		importImpexFile(context, IMPORT_HOME + "/sampledata/productCatalogs/catalogName/categories.impex");
		importImpexFile(context, IMPORT_HOME + "/sampledata/productCatalogs/catalogName/products.impex");
		importImpexFile(context, IMPORT_HOME + "/sampledata/productCatalogs/catalogName/products-media.impex");
		importImpexFile(context, IMPORT_HOME + "/sampledata/productCatalogs/catalogName/products-prices.impex");
		importImpexFile(context, IMPORT_HOME + "/sampledata/productCatalogs/catalogName/products-stocklevels.impex");
		importImpexFile(context, IMPORT_HOME + "/sampledata/productCatalogs/catalogName/products-classifications.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/solr.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/solr_en.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/solrtrigger.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/solr_webinar.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/solr_PrivateLabel.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-content-restrictions.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/SEOTitlesForAllPages.impex");

		getCoreDataImportService().synchronizeContentCatalog(this, context, "wholescripts", true);
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during update and
	 * system update. Be sure that this method can be called repeatedly.
	 *
	 * @param context
	 *                   the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.UPDATE)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/catalogName/cms-responsive-content.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/storeName/site.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/storeName/site-responsive.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/storeName/site_en.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/storeName/store.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/storeName/store-responsive.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/storeName/store_en.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/common/essentialData-InboundIntegrationObjects.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/common/essentialData-OutboundIntegrationObjects.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-content.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-responsive-content.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/common/essential-data.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/solr_webinar.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/stores/wholescripts/solr_PrivateLabel.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/cms-content-restrictions.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/contentCatalogs/wholescriptsContentCatalog/SEOTitlesForAllPages.impex");

		importImpexFile(context, IMPORT_HOME + "/coredata/common/essentialData-InboundChannelConfiguration.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/common/essentialData-DynamicProcessDefinition.impex");

		importImpexFile(context, IMPORT_HOME + "/sampledata/contentCatalogs/wholescriptsContentCatalog/email-content.impex");
		importImpexFile(context, IMPORT_HOME + "/coredata/productCatalogs/catalog_1710_10_ProductCatalog/categories.impex");

		getCoreDataImportService().synchronizeContentCatalog(this, context, "wholescripts", true);
	}

	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system
	 * initialization. <br>
	 * Add import data for each site you have configured
	 *
	 * <pre>
	 * final List<ImportData> importData = new ArrayList<ImportData>();
	 *
	 * final ImportData sampleImportData = new ImportData();
	 * sampleImportData.setProductCatalogName(SAMPLE_PRODUCT_CATALOG_NAME);
	 * sampleImportData.setContentCatalogNames(Arrays.asList(SAMPLE_CONTENT_CATALOG_NAME));
	 * sampleImportData.setStoreNames(Arrays.asList(SAMPLE_STORE_NAME));
	 * importData.add(sampleImportData);
	 *
	 * getCoreDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
	 *
	 * getSampleDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
	 * </pre>
	 *
	 * @param context
	 *                   the context provides the selected parameters and values
	 */


	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData wholescriptsImportData = new ImportData();
		wholescriptsImportData.setProductCatalogName(catalog_1710_10);
		wholescriptsImportData.setContentCatalogNames(Arrays.asList(WSSTOREID));
		wholescriptsImportData.setStoreNames(Arrays.asList(WSSTOREID));
		importData.add(wholescriptsImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}
