package com.wholescripts.core.populators;

import com.wholescripts.core.model.SupportedAutoShipModel;
import com.wholescripts.data.SupportedAutoShipData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class WsAutoShipFrequnceyPopulator implements Populator<SupportedAutoShipModel, SupportedAutoShipData> {

    @Override
    public void populate(SupportedAutoShipModel source, SupportedAutoShipData target) throws ConversionException {
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setPriority(source.getPriority());
    }
}
