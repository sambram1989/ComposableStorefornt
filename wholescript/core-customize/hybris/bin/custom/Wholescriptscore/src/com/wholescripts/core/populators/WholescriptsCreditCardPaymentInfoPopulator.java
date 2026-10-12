package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.order.converters.populator.CreditCardPaymentInfoPopulator;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import org.apache.commons.lang3.StringUtils;

public class WholescriptsCreditCardPaymentInfoPopulator extends CreditCardPaymentInfoPopulator {
    @Override
    public void populate(CreditCardPaymentInfoModel source, CCPaymentInfoData target) {

        super.populate(source,target);
        target.setCardNumber(WholescriptsCoreUtils.getMaskedCardNumber(source.getNumber()));
        if(StringUtils.isNotBlank(source.getCardId()))
        {
            target.setCardid(source.getCardId());
        }

    }
}
