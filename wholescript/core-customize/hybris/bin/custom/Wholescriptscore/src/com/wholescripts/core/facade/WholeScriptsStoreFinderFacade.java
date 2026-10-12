package com.wholescripts.core.facade;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;

public interface WholeScriptsStoreFinderFacade {

    StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String query, final Double latitude, final Double longitude,
                                              final int currentPage, final int pageSize, final String sort, final double radius, final double accuracy,
                                              final String fields);
}
