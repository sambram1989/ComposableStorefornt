package com.wholescripts.core.populators;

import com.wholescripts.core.model.MedPaxModel;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;


public class WholescriptsOrdersPopulator  implements Populator<AbstractOrderModel, AbstractOrderData> {
	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;
	
    @Override
    public void populate(AbstractOrderModel source, AbstractOrderData target) {

        target.setTotalUnitCount(calcTotalUnitCount(source));
        target.setTotalDiscounts(createPrice(source, Double.valueOf(source.getTotalDiscounts())));
        target.setSubTotal(createPrice(source, Double.valueOf(source.getSubtotal())));
    }


    protected Integer calcTotalUnitCount(final AbstractOrderModel source) {
   	 int totalUnitCount = 0;

   	 // Filter only those entries where entryGroupNumbers is empty
   	 List<AbstractOrderEntryModel> filteredEntries = source.getEntries().stream()
   			 .filter(e -> CollectionUtils.isEmpty(e.getEntryGroupNumbers()))
   			 .collect(Collectors.toList());
   	 for (AbstractOrderEntryModel orderEntryModel : filteredEntries) {
   		 Optional<EntryGroup> entryGroup=  source.getEntryGroups().stream().filter(eg -> eg.getGroupNumber().equals(orderEntryModel.getEntryNumber())).findAny();
   		 if(entryGroup.isPresent()) {
   			 totalUnitCount += Objects.nonNull(orderEntryModel.getAbstractOrderQty()) ? orderEntryModel.getAbstractOrderQty().intValue() : 1;
   		 }else {
   			 totalUnitCount += orderEntryModel.getQuantityByCase().intValue();
   		 }
   	 }
   	 return Integer.valueOf(totalUnitCount);
    }

 	protected PriceData createPrice(final AbstractOrderModel source, final Double val)
 	{
 		if (source == null)
 		{
 			throw new IllegalArgumentException("source order must not be null");
 		}

 		final CurrencyModel currency = source.getCurrency();
 		if (currency == null)
 		{
 			throw new IllegalArgumentException("source order currency must not be null");
 		}

 		// Get double value, handle null as zero
 		final double priceValue = val != null ? val.doubleValue() : 0d;

 		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
 	}

}
