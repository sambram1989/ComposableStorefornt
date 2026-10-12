/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationClassesResolverStrategy;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsCalculationService;
import com.wholescripts.core.strategies.WholescriptsFindPriceStrategy;
import com.wholescripts.core.utils.WholeScriptsPriceUtils;
import org.apache.log4j.Logger;


/**
 *
 */
public class WholescriptsCalculationServiceImpl extends DefaultCalculationService implements WholescriptsCalculationService {

    private static final String PRODUCT_COUNT="wholescripts.pdp.productcount.classification.code";
    private final MathContext MONEY_HALF_UP = new MathContext(16, RoundingMode.HALF_UP);
    private final BigDecimal ZERO = (new BigDecimal("0", MONEY_HALF_UP)).setScale(2);
    private static final Logger LOG = Logger.getLogger(WholescriptsCalculationServiceImpl.class);

    private static final ThreadLocal<Boolean> saveOrderEntryUnneeded = new ThreadLocal<>();
    @Resource(name = "orderRequiresCalculationStrategy")
    private OrderRequiresCalculationStrategy orderRequiresCalculationStrategy;
    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;
    @Resource(name = "classificationClassesResolverStrategy")
    private ClassificationClassesResolverStrategy classificationClassesResolverStrategy;

    @Resource(name = "wholescriptsFindPriceStrategy")
    private WholescriptsFindPriceStrategy wholescriptsFindPriceStrategy;

    @Resource(name = "classificationService")
    private ClassificationService classificationService;

    @Resource(name = "wholescriptsMedPaxDao")
    private WholescriptsMedPaxDao wholescriptsMedPaxDao;


    private static void markSaveOrderEntryUnneeded() {
        saveOrderEntryUnneeded.set(Boolean.TRUE);
    }

    private static void unsetSaveOrderEntryUnneeded() {
        saveOrderEntryUnneeded.remove();
    }

    private static boolean isSaveOrderEntryUnneeded() {
        return Boolean.TRUE.equals(saveOrderEntryUnneeded.get());
    }

    private boolean isMedPaxEntry(final AbstractOrderEntryModel entry) {
        return Boolean.TRUE.equals(entry.getProduct().getIsMedpax());
    }


    @Override
    public void recalculate(AbstractOrderModel order, boolean isResetRequired) throws CalculationException {
        try {
            markSaveOrderEntryUnneeded();
            // -----------------------------
            // first force calc entries
          //  calculateEntries(order, true, isResetRequired);
            // -----------------------------
            // Calculate MSRP total
           // calculateMSRPTotal(order);
            // reset own values
          //  final Map taxValueMap = resetAllValues(order);
            calculateCartTotals(order);
            // notify discounts -- needed?
            //	notifyDiscountsAboutCalculation();
        } finally {
            unsetSaveOrderEntryUnneeded();
        }

    }

    private void calculateCartTotals(AbstractOrderModel order) {
   	 double totalPrice= order.getEntries().stream().map(e -> e.getTotalPrice()).collect(Collectors.summingDouble(Double::doubleValue));
   	 double subTotal= order.getEntries().stream().map(e -> e.getBasePrice()).collect(Collectors.summingDouble(Double::doubleValue));
   	 BigDecimal totalTax = this.ZERO;
   	 BigDecimal totaldDiscount = this.ZERO;
   	 for(AbstractOrderEntryModel orderEntry:order.getEntries()) {
   		 for (final TaxValue taxValue : orderEntry.getTaxValues())
   		 {
   			 totalTax = totalTax.add(BigDecimal.valueOf(taxValue.getValue()), this.MONEY_HALF_UP);
   		 }
   		 for(DiscountValue discount : orderEntry.getDiscountValues()) {
   			 totaldDiscount = totaldDiscount.add(BigDecimal.valueOf(discount.getValue()), this.MONEY_HALF_UP);
   		 }
   	 }
   	 double total = totalPrice + order.getPaymentCost().doubleValue() + order.getDeliveryCost().doubleValue();
   	 order.setTotalPrice(total);
   	 order.setSubtotal(subTotal);
   	 order.setTotalDiscounts(totaldDiscount.doubleValue());
   	 order.setTotalTax(totalTax.doubleValue());
   	 order.setCalculated(true);
   	 getModelService().save(order);
    }
    
    @Override
    public void calculateMedPax(MedPaxModel medPaxModel) throws CalculationException {
        double totalPrice = 0.0;
        double totalMsrpPrice = 0.0;

        for (MedpaxEntryModel me : medPaxModel.getMedpaxEntry()) {
            calculateMedPaxEntryPrices(me,medPaxModel);
            if (me.getPrice() != null) {
                totalPrice += me.getPrice();
            }
            if (me.getMsrpPrice() != null) {
                totalMsrpPrice += me.getMsrpPrice();
            }
        }

        medPaxModel.setTotalPrice(totalPrice);
        medPaxModel.setMsrpTotalPrice(totalMsrpPrice);
        getModelService().save(medPaxModel);
        if(Objects.nonNull(medPaxModel.getMedPaxOwner()) && (medPaxModel.getMedPaxOwner() instanceof RecommendationModel || medPaxModel.getMedPaxOwner() instanceof ProtocolModel))
        {
            AbstractOrderModel abstractOrderModel = (AbstractOrderModel) medPaxModel.getMedPaxOwner();
            abstractOrderModel.setCalculated(Boolean.FALSE);
            getModelService().save(abstractOrderModel);
            this.calculate(abstractOrderModel);
        }
    }

    @Override
    public void calculate(final AbstractOrderModel order) throws CalculationException {
        if (orderRequiresCalculationStrategy.requiresCalculation(order)) {
            try {
                markSaveOrderEntryUnneeded();
                // -----------------------------

                // first calc all entries
                calculateEntries(order, true);
                // -----------------------------
                // Calculate MSRP total
                calculateMSRPTotal(order);
                // reset own values
                final Map taxValueMap = resetAllValues(order);
                // -----------------------------
                // now calculate all totals
                calculateTotals(order, false, taxValueMap);
            } finally {
                unsetSaveOrderEntryUnneeded();
            }
            // notify manual discouns - needed?
            //notifyDiscountsAboutCalculation();
        }
    }

    protected void calculateTotals(final AbstractOrderModel order, final boolean recalculate,
                                   final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap) throws CalculationException {
        if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(order)) {
            final CurrencyModel curr = order.getCurrency();
            final int digits = curr.getDigits().intValue();
            // subtotal
            final double subtotal = order.getSubtotal().doubleValue();
            // discounts

            final double totalDiscounts = calculateDiscountValues(order, recalculate);
            final double roundedTotalDiscounts = commonI18NService.roundCurrency(totalDiscounts, digits);
            Double entryDiscount = 0.0;
            for (AbstractOrderEntryModel entry : order.getEntries()) {
                entryDiscount += entry.getDiscountValues().stream().map(d -> d.getValue()).collect(Collectors.summingDouble(Double::doubleValue));
            }
            final double roundedTotalEntryDiscounts = commonI18NService.roundCurrency(totalDiscounts, digits);
            order.setTotalDiscounts(Double.valueOf(roundedTotalDiscounts) + entryDiscount);
            // set total
             double total = subtotal + order.getPaymentCost().doubleValue() + order.getDeliveryCost().doubleValue()
                    - roundedTotalDiscounts;
            final MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxForOwner(order);
            if(Objects.nonNull(medpax) && (medpax.getMedPaxOwner() instanceof RecommendationModel || medpax.getMedPaxOwner() instanceof ProtocolModel))
            {
                total += medpax.getTotalPrice();
            }

            final double totalRounded = commonI18NService.roundCurrency(total, digits);
            order.setTotalPrice(Double.valueOf(totalRounded));
            // taxes
            final double totalTaxes = calculateTotalTaxValues(//
                    order, recalculate, //
                    digits, //
                    getTaxCorrectionFactor(taxValueMap, subtotal, total, order), //
                    taxValueMap);//
            final double totalRoundedTaxes = commonI18NService.roundCurrency(totalTaxes, digits);
            order.setTotalTax(Double.valueOf(totalRoundedTaxes));
            setCalculatedStatus(order);
            saveOrder(order);
        }
    }

    public void calculateEntries(final AbstractOrderModel order, final boolean forceRecalculate, final boolean isResetRequired) throws CalculationException {
        double subtotal = 0.0;
        for (final AbstractOrderEntryModel e : order.getEntries()) {
            recalculateOrderEntryIfNeeded(e, forceRecalculate, isResetRequired);
            subtotal += e.getTotalPrice().doubleValue();
        }
        order.setTotalPrice(Double.valueOf(subtotal));
        saveOrder(order);
    }


    public void calculateEntries(final AbstractOrderModel order, final boolean forceRecalculate) throws CalculationException
    {
        double subtotal = 0.0;

        for (final AbstractOrderEntryModel e : order.getEntries())
        {

            recalculateOrderEntryIfNeeded(e, forceRecalculate);
            subtotal += e.getTotalPrice().doubleValue();
        }
        order.setTotalPrice(Double.valueOf(subtotal));

    }

    public void calculateMSRPTotal(final AbstractOrderModel order) throws CalculationException {
        double msrpSubtotal = 0.0;
        for (final AbstractOrderEntryModel e : order.getEntries()) {
            if (e.getMsrpTotalPrice() != null) {
                msrpSubtotal += e.getMsrpTotalPrice();
            }
        }
        final MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxForOwner(order);
        if(Objects.nonNull(medpax) && Objects.nonNull(medpax.getMsrpTotalPrice()) && (medpax.getMedPaxOwner() instanceof RecommendationModel || medpax.getMedPaxOwner() instanceof ProtocolModel))
        {
            msrpSubtotal += medpax.getMsrpTotalPrice();
        }
        order.setMsrpSubTotalPrice(msrpSubtotal);
        saveOrder(order);
    }



    protected void recalculateOrderEntryIfNeeded(final AbstractOrderEntryModel entry, final boolean forceRecalculation, final boolean isResetRequired)
            throws CalculationException {
        if (forceRecalculation || orderRequiresCalculationStrategy.requiresCalculation(entry)) {
            if (isResetRequired) {
                resetAllValues(entry);
            }
            calculateTotals(entry, true);
        }
    }

    @Override
    protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException {
   	 // taxes
   	 final Collection<TaxValue> entryTaxes = findTaxValues(entry);
   	 entry.setTaxValues(entryTaxes);
   	 final AbstractOrderModel order = entry.getOrder();
   	 try {
   		 final PriceValue pv = findBasePrice(entry);
   		 final PriceValue basePrice = convertPriceIfNecessary(pv, order.getNet().booleanValue(), order.getCurrency(), entryTaxes);
   		 entry.setBasePrice(Double.valueOf(basePrice.getValue()));
   	 }catch (CalculationException e) {
   		 entry.setBasePrice(Double.valueOf(0.0));
   	 }
   	 try {
   		 final PriceValue mrspBasePrice = findMRSPBasePrice(entry);
      	 if (mrspBasePrice != null) {
      		 entry.setMsrpBasePrice(Double.valueOf(mrspBasePrice.getValue()));
      	 } else {
      		 entry.setMsrpBasePrice(0.0);
      	 }
   	 }catch (CalculationException e) {
   		 entry.setMsrpBasePrice(Double.valueOf(0.0));
   	 }
   

   	 final List<DiscountValue> entryDiscounts = findDiscountValues(entry);
   	 entry.setDiscountValues(entryDiscounts);

    }

    @Override
    public void calculateTotals(final AbstractOrderEntryModel entry, final boolean recalculate) {
        if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(entry)) {
            final AbstractOrderModel order = entry.getOrder();
            final CurrencyModel curr = order.getCurrency();
            final int digits = curr.getDigits().intValue();
            long entryQty=entry.getQuantity().longValue();
            double quantity = entry.getQuantity().doubleValue();
            if(Objects.nonNull(entry.getAbstractOrderQty())) {
            	entryQty = entryQty * entry.getAbstractOrderQty().intValue();
            	quantity = entry.getQuantity().doubleValue() * entry.getAbstractOrderQty().intValue();
            }
            
            if(CollectionUtils.isNotEmpty(entry.getEntryGroupNumbers())) {
            	entryQty = entryQty * 30;
            	quantity = quantity * 30;
            }
            final double totalPriceWithoutDiscount = commonI18NService
                    .roundCurrency(entry.getBasePrice().doubleValue() * entryQty, digits);
          

            // Apply discounts (rounded per discount), converting values if needed.
            final List appliedDiscounts = DiscountValue.apply(quantity, totalPriceWithoutDiscount, digits,
                    convertDiscountValues(order, entry.getDiscountValues()),
                    curr.getIsocode());
            entry.setDiscountValues(appliedDiscounts);
            double totalPrice = totalPriceWithoutDiscount;
            for (final Iterator it = appliedDiscounts.iterator(); it.hasNext(); ) {
                totalPrice -= ((DiscountValue) it.next()).getAppliedValue();
            }
            entry.setTotalPrice(Double.valueOf(totalPrice));

            // Calculate MSRP total price
            PriceValue msrpBasePrice = new PriceValue("USD", 0.0, false);
            try {
                msrpBasePrice = findMRSPBasePrice(entry);
            } catch (CalculationException e) {
                LOG.error(e.getStackTrace());
                LOG.error(e.getMessage());
            }
            final double msrpTotalPrice = commonI18NService.roundCurrency(msrpBasePrice.getValue() * entry.getQuantity().longValue(), digits);
            entry.setMsrpTotalPrice(Double.valueOf(msrpTotalPrice));

            // Apply tax values too
            calculateTotalTaxValues(entry);
            setCalculatedStatus(entry);

            if (!isSaveOrderEntryUnneeded() || hasJaloStrategies()) {
                getModelService().save(entry);
            }
        }
    }

    private void calculateMedPaxEntryPrices(final MedpaxEntryModel entry,MedPaxModel medPaxModel) throws CalculationException {

        final double wholesalePrice = WholeScriptsPriceUtils.getWholeSalePrice(entry.getProduct());
        final double retailPrice = WholeScriptsPriceUtils.getRetailPrice(entry.getProduct());
        final String materialProductCountStr = getMaterialProductCount(entry);
        final double materialProductCount = StringUtils.isNotEmpty(materialProductCountStr) ? Double.valueOf(materialProductCountStr) : 0.0;
        final int totalQty = Objects.nonNull(entry.getQuantity()) ? entry.getQuantity() : 0;
        final int defaultDays = Integer.parseInt(Config.getParameter("medpax.default.days"));

        double perPillCost =wholesalePrice;
        double msrpPerPillCost = retailPrice;

        if(materialProductCount>0) {
            perPillCost = wholesalePrice / materialProductCount;
            msrpPerPillCost = retailPrice / materialProductCount;
        }
        // if medpax updated in cart
        int abstractOrderQty=1;
        if(Objects.nonNull(medPaxModel.getAbstractOrderQty()) && medPaxModel.getAbstractOrderQty() > 0L) {
      	  abstractOrderQty =  medPaxModel.getAbstractOrderQty().intValue();
        }
        final double price = totalQty * abstractOrderQty * defaultDays * perPillCost;
        final double msrpPrice = totalQty * abstractOrderQty * defaultDays * msrpPerPillCost ;

        entry.setPrice(commonI18NService.roundCurrency(price, 2));
        entry.setMsrpPrice(commonI18NService.roundCurrency(msrpPrice, 2));
        getModelService().save(entry);
    }



    public PriceValue findMRSPBasePrice(final AbstractOrderEntryModel entry) throws CalculationException {
        return wholescriptsFindPriceStrategy.findMSRPBasePrice(entry);
    }


    public String getMaterialProductCount(final MedpaxEntryModel entry) {

        final FeatureList featureList = classificationService.getFeatures(entry.getProduct());
        final Feature feature = featureList.getFeatureByCode(Config.getParameter(PRODUCT_COUNT));
        if(Objects.nonNull(feature)) {
            final List<FeatureValue> productCountFeatureValues = feature.getValues();
            if (CollectionUtils.isNotEmpty(productCountFeatureValues)) {
                final Object attributeValue = productCountFeatureValues.get(0).getValue();
                String featureValue = ((ClassificationAttributeValueModel) attributeValue).getName();
                if (Objects.nonNull(featureValue)) {
                    return featureValue;
                }
            }
        }
        return StringUtils.EMPTY;
    }
    

 	@Override
 	protected Map resetAllValues(final AbstractOrderModel order) throws CalculationException
 	{
 		// -----------------------------
 		// set subtotal and get tax value map
 		final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = calculateSubtotal(order, false);
 		/*
 		 * filter just relative tax values - payment and delivery prices might require conversion using taxes -> absolute
 		 * taxes do not apply here TODO: ask someone for absolute taxes and how they apply to delivery cost etc. - this
 		 * implementation might be wrong now
 		 */
 		final Collection<TaxValue> relativeTaxValues = new LinkedList<TaxValue>();
 		for (final Map.Entry<TaxValue, ?> e : taxValueMap.entrySet())
 		{
 			final TaxValue taxValue = e.getKey();
 			if (!taxValue.isAbsolute())
 			{
 				relativeTaxValues.add(taxValue);
 			}
 		}

 		//PLA-10914
 		final boolean setAdditionalCostsBeforeDiscounts = Config
 				.getBoolean("ordercalculation.reset.additionalcosts.before.discounts", true);
 		if (setAdditionalCostsBeforeDiscounts)
 		{
 			resetAdditionalCost(order, relativeTaxValues);

 		}
 		order.setGlobalDiscountValues(findGlobalDiscounts(order));
 		if (!setAdditionalCostsBeforeDiscounts)
 		{
 			resetAdditionalCost(order, relativeTaxValues);
 		}

 		return taxValueMap;

 	}

 	private void resetAdditionalCost(final AbstractOrderModel order, final Collection<TaxValue> relativeTaxValues)
 	{
 		if (Objects.nonNull(order.getDeliveryMode()) && order.getDeliveryMode() instanceof ZoneDeliveryModeModel)
 		{
 			final ZoneDeliveryModeModel zoneDeilvery = (ZoneDeliveryModeModel) order.getDeliveryMode();
 			if (!"UPS".equals(zoneDeilvery.getProvider()) && !"USPS".equals(zoneDeilvery.getProvider()))
 			{
 				resetAdditionalCosts(order, relativeTaxValues);
 			}
 		}
 	}
 	
	protected void recalculateOrderEntryIfNeeded(final AbstractOrderEntryModel entry, final boolean forceRecalculation)
			throws CalculationException
	{
		if (forceRecalculation || orderRequiresCalculationStrategy.requiresCalculation(entry))
		{
			resetAllValues(entry);
			calculateTotals(entry, true);
		}
	}
}
