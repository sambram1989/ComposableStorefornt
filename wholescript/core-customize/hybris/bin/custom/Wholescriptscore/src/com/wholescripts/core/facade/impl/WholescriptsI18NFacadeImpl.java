package com.wholescripts.core.facade.impl;

import de.hybris.platform.commercefacades.i18n.impl.DefaultI18NFacade;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;


import java.util.*;
import java.util.stream.Collectors;


public class WholescriptsI18NFacadeImpl extends DefaultI18NFacade {


    @Override
    public List<RegionData> getRegionsForCountryIso(final String countryIso) {
        final CountryModel countryModel = getCommonI18NService().getCountry(countryIso);

        final List<RegionModel> regions = countryModel.getRegions().stream()
                .filter(RegionModel::getActive)
                .sorted(Comparator.comparing(RegionModel::getName, String::compareToIgnoreCase))
                .collect(Collectors.toList());
        return Converters.convertAll(regions, getRegionConverter());
    }

    public void setRegionConverter(final Converter<RegionModel, RegionData> regionConverter) {
        super.setRegionConverter(regionConverter);
    }

    public void setCountryConverter(final Converter<CountryModel, CountryData> countryConverter) {
        super.setCountryConverter(countryConverter);
    }
}
