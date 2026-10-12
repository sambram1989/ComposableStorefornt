package com.wholescripts.core.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.util.List;

public class WholescriptsSingleLineAddressFormatPopulator implements Populator<AddressModel, StringBuilder> {

    private static final Logger LOG = Logger.getLogger(WholescriptsSingleLineAddressFormatPopulator.class);

    private List<String> addressFormatList;

    @Override
    public void populate(final AddressModel addressModel, final StringBuilder addressLine) throws ConversionException {
        for (final String field : addressFormatList) {
            try {
                final Object value = PropertyUtils.getProperty(addressModel, field);
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    addressLine.append((String) value);
                    addressLine.append(", ");
                }

            } catch (final Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error accessing property: " + field, e);
                }
            }

        }


        if (addressLine.length() > 2) {
            addressLine.setLength(addressLine.length() - 2);
        }
    }

    public void setAddressFormatList(final List<String> addressFormatList) {
        this.addressFormatList = addressFormatList;
    }

    public List<String> getAddressFormatList() {
        return addressFormatList;
    }
}
