package com.wholescripts.core.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
import java.util.List;

public interface WholescriptsPractitionerDao {

    PointOfServiceModel getPosDetailsbyUid(String uid,final BaseStoreModel baseStore);

    PointOfServiceModel getPosDetailsByPosName(String posName, B2BCustomerModel customer, BaseStoreModel currentBaseStore);

    SearchPageData<PointOfServiceModel> findAllPractitionerPosDetails(final BaseStoreModel baseStore, PageableData pageableData);

    List<PointOfServiceModel> findNearestPOSDetailsByAddress(final GPS centerPoint, final double radiusKm,
                                                             final BaseStoreModel baseStore);
}
