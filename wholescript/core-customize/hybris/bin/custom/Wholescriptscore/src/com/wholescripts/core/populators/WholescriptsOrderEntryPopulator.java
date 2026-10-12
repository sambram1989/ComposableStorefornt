package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsCartValidationDao;
import com.wholescripts.core.model.RegionProductAgeRestrictionModel;
import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import com.wholescripts.core.service.WholescriptsCartValidationService;
import com.wholescripts.core.service.WholescriptsProductsService;
import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class WholescriptsOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData> {

	private final MathContext MONEY_HALF_UP = new MathContext(16, RoundingMode.HALF_UP);
	private final BigDecimal ZERO = (new BigDecimal("0", MONEY_HALF_UP)).setScale(2);
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "wholescriptsCartValidationService")
	private WholescriptsCartValidationService wholescriptsCartValidationService;
	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

    @Resource(name = "wholescriptsProductsService")
    WholescriptsProductsService wholescriptsProductsService;

    @Override
    public void populate(AbstractOrderEntryModel source, OrderEntryData target) {
        if(Objects.nonNull(source.getSelectedAutoShip()))
        {
            target.setAutoShipCode(source.getSelectedAutoShip().getCode());
        }

        try{
            RegionProductSwapRestrictionModel swapModel =wholescriptsCartValidationService.updateProductFromSwapRestriction(source);
            if(Objects.nonNull(swapModel))
            {
                target.setRegionSwapDescription(configurationService.getConfiguration().getString("wholescripts.swap.region.warning.description"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Objects.nonNull(source.getIsHidden())) {
      	  target.setHidden(source.getIsHidden());
        }

        if(StringUtils.isNotEmpty(source.getTemplateId()) && source.getTemplateId().contains("-")){
            String[] tempateIds = source.getTemplateId().split("-");
            if( tempateIds.length > 2) {
                target.setMedPaxEntryNumber(tempateIds[2]);
            }
        }
        target.setAbstractOrderQty(source.getAbstractOrderQty());

        BigDecimal totalTax = this.ZERO;
        BigDecimal totaldDiscount = this.ZERO;
        if(CollectionUtils.isNotEmpty(source.getTaxValues())) {
      	  for (final TaxValue taxValue : source.getTaxValues())
           {
         	  totalTax = totalTax.add(BigDecimal.valueOf(taxValue.getValue()), this.MONEY_HALF_UP);
           }
        }
        if(CollectionUtils.isNotEmpty(source.getDiscountValues())) {
        for(DiscountValue discount : source.getDiscountValues()) {
      	  totaldDiscount = totaldDiscount.add(BigDecimal.valueOf(discount.getValue()), this.MONEY_HALF_UP);
        }
        }
        if(Objects.nonNull(source.getOrder()))
        {
            if(Objects.nonNull(source.getSubTotal())) {
                target.setSubTotal(createPrice(source.getOrder(), Double.valueOf(source.getSubTotal())));
            }
            target.setTotalDiscounts(createPrice(source.getOrder(),totaldDiscount.doubleValue()));
            target.setTotalTax(createPrice(source.getOrder(), totalTax.doubleValue()));
        }
        if(Objects.nonNull(source.getQuantityByCase()) && Objects.nonNull(source.getProduct()))
        {
            boolean isPrivateLabel=wholescriptsProductsService.isPrivateLabel(source.getProduct());
            if(isPrivateLabel)
            {
                target.setQuantityByCase(source.getQuantityByCase());
            }

        }
        if(StringUtils.isNotEmpty(source.getProductDescription())) {
      	  target.setProductDescription(source.getProductDescription());
        }

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
