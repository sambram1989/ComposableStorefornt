package com.wholescripts.core.dao.impl;

import com.wholescripts.core.dao.WholescriptsPractitionerDao;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import javax.annotation.Resource;
import java.util.List;

public class WholescriptsPractitionerDaoImpl implements WholescriptsPractitionerDao {


    private static final String GET_POS_DETAILS_ID = "SELECT {p.pk} FROM {PointOfService as p join B2BCustomer as bc on {p.practitioner}={bc.PK}} WHERE {p.practitionerDirectoryOptIn}=?practitionerDirectoryOptIn and {p.type}=?type and {p.basestore}=?baseStore and {bc.UID}=?uid  ORDER BY {p.modifiedtime} desc" ;

    private static final String GET_POS_DETAILS_POS_NAME="SELECT {p.pk} FROM {PointOfService as p join B2BCustomer as bc on {p.practitioner}={bc.pk}} where {p.practitionerDirectoryOptIn}=?practitionerDirectoryOptIn and {p.type}=?type and {p.basestore}=?baseStore and {bc.uid}=?uid and {name}=?name";

    private static final String GET_POS_DETAILS_BY_STORE_TYPE="SELECT {p.pk} FROM {PointOfService as p join  PointOfServiceTypeEnum as pt  on {p.type}={pt.pk} }  where {p.practitionerDirectoryOptIn}=?practitionerDirectoryOptIn and {p.type}=?type and {p.basestore}=?baseStore";


    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Resource(name = "pagedFlexibleSearchService")
    private PagedFlexibleSearchService pagedFlexibleSearchService;

    @Override
    public PointOfServiceModel getPosDetailsbyUid(String uid,BaseStoreModel baseStore) {

        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_POS_DETAILS_ID);
        searchQuery.addQueryParameter("practitionerDirectoryOptIn", Boolean.TRUE);
        searchQuery.addQueryParameter("type", PointOfServiceTypeEnum.PRACTICE);
        searchQuery.addQueryParameter("baseStore", baseStore);
        searchQuery.addQueryParameter("uid", uid);
        final SearchResult<PointOfServiceModel> result = flexibleSearchService.search(searchQuery);
        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    @Override
    public PointOfServiceModel getPosDetailsByPosName (String posName, B2BCustomerModel customer, BaseStoreModel baseStore) {

        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_POS_DETAILS_POS_NAME);
        searchQuery.addQueryParameter("practitionerDirectoryOptIn", Boolean.TRUE);
        searchQuery.addQueryParameter("type", PointOfServiceTypeEnum.PRACTICE);
        searchQuery.addQueryParameter("baseStore", baseStore);
        searchQuery.addQueryParameter("name", posName);
        searchQuery.addQueryParameter("uid", customer.getUid());
        final PointOfServiceModel result = flexibleSearchService.searchUnique(searchQuery);
        return result;
    }

    @Override
    public SearchPageData<PointOfServiceModel> findAllPractitionerPosDetails(BaseStoreModel baseStore, PageableData pageableData) {
        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_POS_DETAILS_BY_STORE_TYPE);
        searchQuery.addQueryParameter("practitionerDirectoryOptIn", Boolean.TRUE);
        searchQuery.addQueryParameter("type", PointOfServiceTypeEnum.PRACTICE);
        searchQuery.addQueryParameter("baseStore", baseStore);
        SearchPageData<PointOfServiceModel> results = pagedFlexibleSearchService.search(searchQuery,pageableData);
        return results;
    }

    @Override
    public List<PointOfServiceModel> findNearestPOSDetailsByAddress(GPS centerPoint, double radiusKm, BaseStoreModel baseStore) {

        FlexibleSearchQuery fQuery = buildQuery(centerPoint, radiusKm, baseStore);
        SearchResult<PointOfServiceModel> result = flexibleSearchService.search(fQuery);
        return result.getResult();

    }

    private FlexibleSearchQuery buildQuery(GPS centerPoint, double radiusKm, BaseStoreModel baseStore) {
        try {
            List<GPS> corners = GeometryUtils.getSquareOfTolerance(centerPoint, radiusKm);
            if (corners != null && !corners.isEmpty() && corners.size() == 2) {
                Double latMax = ((GPS)corners.get(1)).getDecimalLatitude();
                Double lonMax = ((GPS)corners.get(1)).getDecimalLongitude();
                Double latMin = ((GPS)corners.get(0)).getDecimalLatitude();
                Double lonMin = ((GPS)corners.get(0)).getDecimalLongitude();
                StringBuilder query = new StringBuilder();
                query.append("SELECT {PK} FROM {").append("PointOfService").append("} WHERE {").append("latitude").append("} is not null AND {").append("longitude").append("} is not null AND {").append("latitude").append("} >= ?latMin AND {").append("latitude").append("} <= ?latMax AND {").append("longitude").append("} >= ?lonMin AND {").append("longitude").append("} <= ?lonMax");
                if (baseStore != null) {
                    query.append(" AND {").append("baseStore").append("} = ?baseStore");
                }
                query.append(" AND {").append("type").append("} = ?type");
                query.append(" AND {").append("practitionerDirectoryOptIn").append("} = ?practitionerDirectoryOptIn");
                FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
                fQuery.addQueryParameter("latMax", latMax);
                fQuery.addQueryParameter("latMin", latMin);
                fQuery.addQueryParameter("lonMax", lonMax);
                fQuery.addQueryParameter("lonMin", lonMin);
                if (baseStore != null) {
                    fQuery.addQueryParameter("baseStore", baseStore);
                }
                fQuery.addQueryParameter("type", PointOfServiceTypeEnum.PRACTICE);
                fQuery.addQueryParameter("practitionerDirectoryOptIn", Boolean.TRUE);
                return fQuery;
            } else {
                throw new PointOfServiceDaoException("Could not fetch locations from database. Unexpected neighborhood");
            }
        } catch (GeoLocatorException var12) {
            GeoLocatorException e = var12;
            throw new PointOfServiceDaoException("Could not fetch locations from database, due to :" + e.getMessage(), e);
        }
    }

}
