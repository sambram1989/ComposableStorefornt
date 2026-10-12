package com.wholescripts.core.facade.impl;

import com.wholescripts.core.facade.WholeScriptsStoreFinderFacade;
import com.wholescripts.core.service.WholeScriptsStoreFinderService;
import de.hybris.platform.commercefacades.storefinder.impl.DefaultStoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderSearchPageWsDTO;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.paginated.util.PaginatedSearchUtils;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class WholeScriptsStoreFinderFacadeImpl extends DefaultStoreFinderFacade implements WholeScriptsStoreFinderFacade  {
    private BaseStoreService baseStoreService;
    private PointOfServiceService pointOfServiceService;

    private Converter<StoreFinderSearchPageData<PointOfServiceDistanceData>, StoreFinderSearchPageData<PointOfServiceData>> searchPagePointOfServiceDistanceConverter;
    private Converter<PointOfServiceDistanceData, PointOfServiceData> pointOfServiceDistanceConverter;
    private WholeScriptsStoreFinderService wholeScriptsStoreFinderService;


    @Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'DTO',#query,#latitude,#longitude,#currentPage,#pageSize,#sort,#radius,#accuracy,#fields)")
    @Override
    public StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String query, final Double latitude,
                                                     final Double longitude, final int currentPage, final int pageSize, final String sort, final double radius,
                                                     final double accuracy,final String fields) {
        return locationSearch(query, latitude, longitude, currentPage,
                pageSize, sort, radius, accuracy);

    }
    @Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'Data',#query,#latitude,#longitude,#currentPage,#pageSize,#sort,#radius,#accuracy)")
    public StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String query, final Double latitude,
                                                                        final Double longitude, final int currentPage, final int pageSize, final String sort, final double radius,
                                                                        final double accuracy) {
        final double baseStoreRadius=getBaseStoreService().getCurrentBaseStore().getMaxRadiusForPoSSearch();
        final double radiusToSearch = getInKilometres(baseStoreRadius, accuracy);
        StoreFinderSearchPageData<PointOfServiceData> result = new StoreFinderSearchPageData<>();
        final PageableData pageData =createPaginationData(currentPage,pageSize,sort);
        if (StringUtils.isNotBlank(query)) {
            result = getlocationSearchBasedOnAddress(query, radiusToSearch,pageData);
            if(Objects.nonNull(result))
            {
                final List<PointOfServiceData>  pointOfServices = result.getResults();
                final List<PointOfServiceData>  filteredPointOfServices = new ArrayList<PointOfServiceData>();
                if(CollectionUtils.isNotEmpty(pointOfServices))
                {
                    for (PointOfServiceData pointOfService : pointOfServices) {
                        if((pointOfService.getAddress() != null
                                && StringUtils.containsIgnoreCase(pointOfService.getAddress().getFormattedAddress(), query))
                                || (pointOfService.getDisplayName() != null && StringUtils.containsIgnoreCase(pointOfService.getDisplayName(), query))
                                || (pointOfService.getPractitionerName() != null && StringUtils.containsIgnoreCase(pointOfService.getPractitionerName(), query))) {
                            filteredPointOfServices.add(pointOfService);
                        }
                    }
                    result.setResults(filteredPointOfServices);
                }


            }

        }
        else if (latitude != null && longitude != null) {
            final GeoPoint geoPoint = new GeoPoint();
            geoPoint.setLatitude(latitude.doubleValue());
            geoPoint.setLongitude(longitude.doubleValue());
            result = positionSearch(geoPoint, pageData,radiusToSearch);
        }
       /* else {
            result = getAllPointOfServices(pageData);
        }*/
        return result;

    }

    private PageableData createPaginationData(int currentPage, int pageSize, String sort) {
        final PageableData pageable = new PageableData();
        pageable.setCurrentPage(currentPage);
        pageable.setPageSize(pageSize);
        pageable.setSort(sort);
        return pageable;
    }



    private  StoreFinderSearchPageData<PointOfServiceData> getlocationSearchBasedOnAddress(String locationText, double maxRadius,final PageableData pageableData) {
        final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
        final StoreFinderSearchPageData<PointOfServiceDistanceData>  searchPData =wholeScriptsStoreFinderService.locationSearch(currentBaseStore, locationText, maxRadius,pageableData);
        if(Objects.nonNull(searchPData))
        {
            return getSearchPagePointOfServiceDistanceConverter().convert(searchPData);
        }

        return new StoreFinderSearchPageData<PointOfServiceData>();
    }

    @Override
    public StoreFinderSearchPageData<PointOfServiceData> positionSearch(final GeoPoint geoPoint, final PageableData pageableData,
                                                                        final double maxRadius)
    {
        final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
        final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = wholeScriptsStoreFinderService
                .positionSearch(currentBaseStore, geoPoint, pageableData, maxRadius);
        return getSearchPagePointOfServiceDistanceConverter().convert(searchPageData);
    }

    @Override
    public StoreFinderSearchPageData<PointOfServiceData> getAllPointOfServices(final PageableData pageableData)
    {
        final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
        final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = wholeScriptsStoreFinderService
                .getAllPos(currentBaseStore, pageableData);
        return getSearchPagePointOfServiceDistanceConverter().convert(searchPageData);
    }
    protected double getInKilometres(final double radius, final double accuracy)
    {
        return (radius + accuracy) / 1000.0;
    }

    public WholeScriptsStoreFinderService getWholeScriptsStoreFinderService() {
        return wholeScriptsStoreFinderService;
    }

    public void setWholeScriptsStoreFinderService(WholeScriptsStoreFinderService wholeScriptsStoreFinderService) {
        this.wholeScriptsStoreFinderService = wholeScriptsStoreFinderService;
    }

    @Override
    public PointOfServiceService getPointOfServiceService() {
        return pointOfServiceService;
    }

    @Override
    public void setPointOfServiceService(PointOfServiceService pointOfServiceService) {
        this.pointOfServiceService = pointOfServiceService;
    }

    @Override
    public Converter<StoreFinderSearchPageData<PointOfServiceDistanceData>, StoreFinderSearchPageData<PointOfServiceData>> getSearchPagePointOfServiceDistanceConverter() {
        return searchPagePointOfServiceDistanceConverter;
    }

    @Override
    public void setSearchPagePointOfServiceDistanceConverter(Converter<StoreFinderSearchPageData<PointOfServiceDistanceData>, StoreFinderSearchPageData<PointOfServiceData>> searchPagePointOfServiceDistanceConverter) {
        this.searchPagePointOfServiceDistanceConverter = searchPagePointOfServiceDistanceConverter;
    }

    @Override
    public Converter<PointOfServiceDistanceData, PointOfServiceData> getPointOfServiceDistanceConverter() {
        return pointOfServiceDistanceConverter;
    }

    @Override
    public void setPointOfServiceDistanceConverter(Converter<PointOfServiceDistanceData, PointOfServiceData> pointOfServiceDistanceConverter) {
        this.pointOfServiceDistanceConverter = pointOfServiceDistanceConverter;
    }

    @Override
    public BaseStoreService getBaseStoreService() {
        return baseStoreService;
    }

    @Override
    public void setBaseStoreService(BaseStoreService baseStoreService) {
        this.baseStoreService = baseStoreService;
    }
}


