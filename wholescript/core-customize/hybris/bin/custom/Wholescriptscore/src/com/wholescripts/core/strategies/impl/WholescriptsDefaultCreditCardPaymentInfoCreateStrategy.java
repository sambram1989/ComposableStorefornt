package com.wholescripts.core.strategies.impl;

import com.wholescripts.core.strategies.WholescriptsCreditCardPaymentInfoCreateStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.WholescriptsADDCCInSapResponseData;
import com.wholescripts.data.WholescriptsAuthResponseData;
import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.acceleratorservices.payment.cybersource.enums.CardTypeEnum;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.*;

public class WholescriptsDefaultCreditCardPaymentInfoCreateStrategy  implements WholescriptsCreditCardPaymentInfoCreateStrategy {

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    @Override
    public CreditCardPaymentInfoModel saveSubscription(Map<String, String> parameters, B2BCustomerModel customerModel, SopPaymentDetailsWsDTO sopPaymentDetails, WholescriptsADDCCInSapResponseData sapPaymentResponse) {
        final AddressModel billingAddress = modelService.create(AddressModel.class);
        billingAddress.setFirstname(sopPaymentDetails.getBillTo_firstName());
        billingAddress.setLastname(sopPaymentDetails.getBillTo_lastName());
        billingAddress.setLine1(sopPaymentDetails.getBillTo_street1());
        billingAddress.setLine2(sopPaymentDetails.getBillTo_street2());
        billingAddress.setTown(sopPaymentDetails.getBillTo_city());
        billingAddress.setPostalcode(sopPaymentDetails.getBillTo_postalCode());
        billingAddress.setPhone1(sopPaymentDetails.getBillTo_phoneNumber());
        billingAddress.setBillingAddress(true);
        final B2BUnitModel unit = customerModel.getDefaultB2BUnit();

        if(Objects.nonNull(unit)) {
           String addressNumber= WholescriptsCoreUtils.getShipToOrPayerForDefaultB2bUnit(sopPaymentDetails.getAddressNumber());
            billingAddress.setSapPublicKey(unit.getUid() + "|" + addressNumber + "|KNA1|RE");
        }

        final CountryModel country = commonI18NService.getCountry(sopPaymentDetails.getBillTo_country());
        billingAddress.setCountry(country);
        if (StringUtils.isNotEmpty(sopPaymentDetails.getBillTo_region()))
        {
            billingAddress.setRegion(
                    commonI18NService.getRegion(country, country.getIsocode() + "-" + sopPaymentDetails.getBillTo_region()));
        }
        if(StringUtils.isNotBlank(sopPaymentDetails.getBillTo_email()))
        {
            billingAddress.setEmail(sopPaymentDetails.getBillTo_email());
        }


        final CreditCardPaymentInfoModel cardPaymentInfoModel = this
                .createCreditCardPaymentInfo(parameters, billingAddress, customerModel, sopPaymentDetails,sapPaymentResponse);

        billingAddress.setOwner(cardPaymentInfoModel);

        modelService.saveAll(cardPaymentInfoModel, billingAddress);
        modelService.refresh(customerModel);

        final List<PaymentInfoModel> paymentInfoModels = new ArrayList<PaymentInfoModel>(customerModel.getPaymentInfos());
        if (!paymentInfoModels.contains(cardPaymentInfoModel))
        {
            paymentInfoModels.add(cardPaymentInfoModel);
            if (sopPaymentDetails.isSavePaymentInfo())
            {
                customerModel.setPaymentInfos(paymentInfoModels);
                modelService.save(customerModel);
            }

            modelService.save(cardPaymentInfoModel);
            modelService.refresh(customerModel);
        }

        return cardPaymentInfoModel;
    }
    CreditCardPaymentInfoModel createCreditCardPaymentInfo(Map<String, String> parameters , AddressModel billingAddress, B2BCustomerModel customerModel, SopPaymentDetailsWsDTO sopPaymentDetails, WholescriptsADDCCInSapResponseData sapPaymentResponse)
    {
        final CreditCardPaymentInfoModel cardPaymentInfoModel = modelService.create(CreditCardPaymentInfoModel.class);
        cardPaymentInfoModel.setBillingAddress(billingAddress);
        cardPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
        cardPaymentInfoModel.setUser(customerModel);

        cardPaymentInfoModel.setNumber(parameters.get("card_accountNumber"));
        if(sopPaymentDetails.isSavePaymentInfo() && Objects.nonNull(sapPaymentResponse))
        {
            cardPaymentInfoModel.setCardId(sapPaymentResponse.getCardId());

        }

        String cardType=parameters.get("card_cardType");
        final Map<String, String> CYBERSOURCE_SOP_CARD_TYPES = new HashMap<>();

       CYBERSOURCE_SOP_CARD_TYPES.put("vi", "001");
        CYBERSOURCE_SOP_CARD_TYPES.put("mc", "002");
        CYBERSOURCE_SOP_CARD_TYPES.put("ax", "003");
        CYBERSOURCE_SOP_CARD_TYPES.put("di", "004");

        CardTypeEnum cardTypeEnum = CardTypeEnum.get(CYBERSOURCE_SOP_CARD_TYPES.get(cardType));
        if (cardTypeEnum != null)
        {
            cardPaymentInfoModel.setType(CreditCardType.valueOf(cardTypeEnum.name().toUpperCase()));

        }

        cardPaymentInfoModel.setCcOwner(getCCOwner(parameters, billingAddress));
        if (parameters.get("card_expirationMonth") != null)
        {
            cardPaymentInfoModel.setValidToMonth(String.valueOf(parameters.get("card_expirationMonth")));
        }
        if (parameters.get("card_expirationYear") != null)
        {
            cardPaymentInfoModel.setValidToYear(String.valueOf(parameters.get("card_expirationYear")));
        }
        if (parameters.get("cvv") != null)
        {
            cardPaymentInfoModel.setCvvNumber(parameters.get("cvv"));
        }
        cardPaymentInfoModel.setSaved(sopPaymentDetails.isSavePaymentInfo());

        return cardPaymentInfoModel;
    }

    private String getCCOwner(Map<String, String> parameters, AddressModel billingAddress) {
        if (parameters.get("card_nameOnCard") != null)
        {
            return parameters.get("card_nameOnCard");
        }
        else
        {
            return billingAddress.getFirstname() + " " + billingAddress.getLastname();
        }
    }


}
