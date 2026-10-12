package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WholescriptsPractitionerDao;
import com.wholescripts.core.service.WholeScriptsStoreFinderService;
import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commerceservices.storefinder.impl.DefaultStoreFinderService;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.exception.LocationServiceException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.*;

public class WholeScriptsStoreFinderServiceImpl <ITEM extends PointOfServiceDistanceData>
        implements WholeScriptsStoreFinderService<ITEM, StoreFinderSearchPageData<ITEM>> {

    private static final Logger LOG = Logger.getLogger(WholeScriptsStoreFinderServiceImpl.class);


    private PointOfServiceDao pointOfServiceDao;
    private GeoWebServiceWrapper geoWebServiceWrapper;
    private GenericDao<PointOfServiceModel> pointOfServiceGenericDao;

    @Resource(name = "wholescriptsPractitionerDao")
    private WholescriptsPractitionerDao wholescriptsPractitionerDao;


    private StoreFinderSearchPageData<ITEM> doSearch(BaseStoreModel baseStore, String locationText, GeoPoint geoPoint, Double maxRadiusKm, final PageableData pageableData) {
        Collection<PointOfServiceModel> posResults = Collections.EMPTY_LIST;
        final int resultRangeStart = pageableData.getCurrentPage() * pageableData.getPageSize();
        final int resultRangeEnd = (pageableData.getCurrentPage() + 1) * pageableData.getPageSize();

        if (maxRadiusKm != null)
        {
            posResults = getPointsOfServiceNear(geoPoint, maxRadiusKm.doubleValue(), baseStore);
        }

        if (CollectionUtils.isNotEmpty(posResults))
        {
            // Sort all the POS
            final List<ITEM> orderedResults = calculateDistances(geoPoint, posResults);

            final PaginationData paginationData = createPagination(pageableData, posResults.size());
            final List<ITEM> orderedResultsWindow = orderedResults
                    .subList(Math.min(orderedResults.size(), resultRangeStart), Math.min(orderedResults.size(), resultRangeEnd));

            return createSearchResult(locationText, geoPoint, orderedResultsWindow, paginationData);
        }
        return createSearchResult(locationText, geoPoint, Collections.<ITEM>emptyList(), createPagination(pageableData, 0));
    }

    protected StoreFinderSearchPageData<ITEM> createSearchResult(final String locationText, final GeoPoint centerPoint,
                                                                 final List<ITEM> results, final PaginationData paginationData)
    {
        final StoreFinderSearchPageData<ITEM> searchPageData = createStoreFinderSearchPageData();
        searchPageData.setResults(results);
        searchPageData.setPagination(paginationData);

        searchPageData.setLocationText(locationText);
        searchPageData.setSourceLatitude(centerPoint.getLatitude());
        searchPageData.setSourceLongitude(centerPoint.getLongitude());

        return searchPageData;
    }
    protected StoreFinderSearchPageData<ITEM> createStoreFinderSearchPageData()
    {
        return new StoreFinderSearchPageData<ITEM>();
    }


    private PaginationData createPagination(PageableData pageableData, int size) {
        final PaginationData paginationData = createPaginationData();

        // Set the page size and and don't allow it to be less than 1
        paginationData.setPageSize(Math.max(1, pageableData.getPageSize()));

        paginationData.setTotalNumberOfResults(size);

        // Calculate the number of pages
        paginationData.setNumberOfPages((int) Math.ceil(((double) size) / paginationData.getPageSize()));

        // Work out the current page, fixing any invalid page values
        paginationData.setCurrentPage(Math.max(0, Math.min(paginationData.getNumberOfPages(), pageableData.getCurrentPage())));

        return paginationData;
    }


    protected PaginationData createPaginationData()
    {
        return new PaginationData();
    }
    protected Collection<PointOfServiceModel> getPointsOfServiceNear(final GeoPoint centerPoint, final double radiusKm,
                                                                     final BaseStoreModel baseStore) throws PointOfServiceDaoException
    {
        final GPS referenceGps = new DefaultGPS(centerPoint.getLatitude(), centerPoint.getLongitude());
        return wholescriptsPractitionerDao.findNearestPOSDetailsByAddress(referenceGps,radiusKm,baseStore);
    }

    protected List<ITEM> calculateDistances(final GeoPoint centerPoint, final Collection<PointOfServiceModel> pointsOfService)
    {
        final List<ITEM> result = new ArrayList<ITEM>();

        for (final PointOfServiceModel pointOfService : pointsOfService)
        {
            final ITEM storeFinderResultData = createStoreFinderResultData();
            storeFinderResultData.setPointOfService(pointOfService);
            storeFinderResultData.setDistanceKm(calculateDistance(centerPoint, pointOfService));
            result.add(storeFinderResultData);
        }

        Collections.sort(result, DefaultStoreFinderService.StoreFinderResultDataComparator.INSTANCE);

        return result;
    }

    private ITEM createStoreFinderResultData() {
        return (ITEM) new PointOfServiceDistanceData();
    }

    protected double calculateDistance(final GeoPoint centerPoint, final PointOfServiceModel posModel)
            throws GeoLocatorException, LocationServiceException
    {
        if (posModel.getLatitude() != null && posModel.getLongitude() != null)
        {
            final GPS positionGPS = new DefaultGPS(posModel.getLatitude().doubleValue(), posModel.getLongitude().doubleValue());
            final GPS referenceGps = new DefaultGPS(centerPoint.getLatitude(), centerPoint.getLongitude());
            return GeometryUtils.getElipticalDistanceKM(referenceGps, positionGPS);
        }
        throw new LocationServiceException(
                "Unable to calculate a distance for PointOfService(" + posModel + ") due to missing  latitude, longitude value");
    }

    @Override
    public StoreFinderSearchPageData<ITEM> locationSearch(BaseStoreModel baseStore, String locationText, double maxRadius, PageableData pageableData) {
        final GeoPoint geoPoint = new GeoPoint();
        if (StringUtils.isNotEmpty(locationText))
        {
            try
            {
                final AddressData addressData = new AddressData();
                addressData.setCity(locationText);
                addressData.setZip(locationText);
                addressData.setCountryCode("US");
                final GPS resolvedPoint = getGeoWebServiceWrapper()
                        .geocodeAddress(addressData);
                if(resolvedPoint!=null)
                {
                    geoPoint.setLatitude(resolvedPoint.getDecimalLatitude());
                    geoPoint.setLongitude(resolvedPoint.getDecimalLongitude());
                }

                return doSearch(baseStore, locationText, geoPoint, Double.valueOf(maxRadius),pageableData);
            }
            catch (final GeoServiceWrapperException ex)
            {
                LOG.info("Failed to resolve location for [" + locationText + "]", ex);
            }
        }

        return null ;
    }

    @Override
    public StoreFinderSearchPageData<ITEM> positionSearch(BaseStoreModel currentBaseStore, GeoPoint geoPoint, PageableData pageableData, double maxRadius) {
        return doSearch(currentBaseStore, null, geoPoint, Double.valueOf(maxRadius),pageableData);
    }

    @Override
    public StoreFinderSearchPageData<ITEM> getAllPos(BaseStoreModel baseStore, PageableData pageableData) {


        final SearchPageData<PointOfServiceModel> posResults = wholescriptsPractitionerDao.findAllPractitionerPosDetails(baseStore,pageableData);

        final List<ITEM> results = new ArrayList<ITEM>();

        if(CollectionUtils.isNotEmpty(posResults.getResults()))
        {
            for (final PointOfServiceModel pointOfService : posResults.getResults())
            {
                final ITEM storeFinderResultData = createStoreFinderResultData();
                storeFinderResultData.setPointOfService(pointOfService);
                results.add(storeFinderResultData);
            }
        }

        final StoreFinderSearchPageData<ITEM> searchPageData = createStoreFinderSearchPageData();
        searchPageData.setResults(results);
       searchPageData.setPagination(posResults.getPagination());
        searchPageData.setSorts(posResults.getSorts());

        return searchPageData;
    }

    public PointOfServiceDao getPointOfServiceDao() {
        return pointOfServiceDao;
    }

    public void setPointOfServiceDao(PointOfServiceDao pointOfServiceDao) {
        this.pointOfServiceDao = pointOfServiceDao;
    }

    public GeoWebServiceWrapper getGeoWebServiceWrapper() {
        return geoWebServiceWrapper;
    }

    public void setGeoWebServiceWrapper(GeoWebServiceWrapper geoWebServiceWrapper) {
        this.geoWebServiceWrapper = geoWebServiceWrapper;
    }

    public GenericDao<PointOfServiceModel> getPointOfServiceGenericDao() {
        return pointOfServiceGenericDao;
    }

    public void setPointOfServiceGenericDao(GenericDao<PointOfServiceModel> pointOfServiceGenericDao) {
        this.pointOfServiceGenericDao = pointOfServiceGenericDao;
    }
}
