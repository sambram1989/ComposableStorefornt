package com.wholescripts.core.service;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.store.BaseStoreModel;

public interface WholeScriptsStoreFinderService <ITEM extends PointOfServiceDistanceData, RESULT extends StoreFinderSearchPageData<ITEM>>{


    RESULT locationSearch(BaseStoreModel currentBaseStore, String locationText, double maxRadius, PageableData pageableData);

    RESULT positionSearch(BaseStoreModel currentBaseStore, GeoPoint geoPoint, PageableData pageableData, double maxRadius);

    RESULT getAllPos(BaseStoreModel baseStore, PageableData pageableData);
}
