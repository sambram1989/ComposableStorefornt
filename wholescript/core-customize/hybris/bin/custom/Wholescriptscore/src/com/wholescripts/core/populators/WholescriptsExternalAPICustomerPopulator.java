package com.wholescripts.core.populators;

import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.WholescriptsExternalAPIAgeRestrictionData;
import com.wholescripts.data.WholescriptsExternalAPICreditInfoData;
import com.wholescripts.data.WholescriptsExternalAPICustomerData;
import com.wholescripts.data.WholescriptsExternalAPIReferralCodeData;
import de.hybris.platform.b2b.model.B2BCreditLimitModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class WholescriptsExternalAPICustomerPopulator implements Populator<B2BUnitModel, WholescriptsExternalAPICustomerData> {


    @Resource(name = "wholescriptsHelper")
    protected WholescriptsHelper wholescriptsHelper;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Override
    public void populate(B2BUnitModel source, WholescriptsExternalAPICustomerData target) throws ConversionException {


        AddressModel addressModel = WholescriptsHelper.returnValidAddress(source);

        if (null != addressModel) {
            target.setFirstName(addressModel.getFirstname());
            target.setLastName(addressModel.getLastname());
        }

        if (StringUtils.isBlank(target.getFirstName())) {
            target.setFirstName(source.getName());
        }

        if (source.getSapCustomerGroup() != null) {
            target.setAccessLevel("1");
        }
        if (source.getSapCustomerGroup() != null) {
            target.setCustomerGroup(source.getSapCustomerGroup().getCode());
        }

        target.setCustomerSAPId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(source.getUid()));

        Set<PrincipalModel> members = source.getMembers();
        if (CollectionUtils.isNotEmpty(members)) {
            for (PrincipalModel member : members) {
                if (member instanceof CustomerModel) {
                    B2BCustomerModel customer = (B2BCustomerModel) member;
                    target.setEmailOptedIn(customer.getIsEmailNotificationEnabled());
                    target.setSmsOptedIn(customer.getIsSmsNotificationEnabled());
                    target.setEmail(customer.getEmail());
                    break;
                }
            }
        }

        if (null != source.getCreationtime()) {
            target.setCreateDate(WholescriptsDateUtils.convertDateInUTCFormat(source.getCreationtime()));
        }
        if (null != source.getModifiedtime()) {
            target.setUpdateDate(WholescriptsDateUtils.convertDateInUTCFormat(source.getModifiedtime()));
        }


        B2BCreditLimitModel creditLimitModel = source.getCreditLimit();
        WholescriptsExternalAPICreditInfoData wholescriptsExternalAPICreditInfoData = new WholescriptsExternalAPICreditInfoData();
        if (null != creditLimitModel) {
            if (creditLimitModel.getAmount() != null) {
                wholescriptsExternalAPICreditInfoData.setCreditLimit(creditLimitModel.getAmount().toString());
            }
            wholescriptsExternalAPICreditInfoData.setCommissionTotal(creditLimitModel.getCreditUsed());
        }
        target.setCreditInfo(wholescriptsExternalAPICreditInfoData);

        if (source.getSapCustomerClassification() != null) {
            target.setCustomerClass(source.getSapCustomerClassification().getCode());
        }

        if (source.getSapBlocked() != null) {
            target.setBlock(source.getSapBlocked());
        }

        if (source.getSapDeliveryBlock() != null) {
            target.setDeliveryBlock(source.getSapDeliveryBlock().getCode());
        }
        if (source.getSapOnCreditHold() != null) {
            target.setOnCreditHold(source.getSapOnCreditHold().getCode());
        }

        if (source.getDiscount() != null) {
            target.setDiscount(source.getDiscount());
        }

        if (source.getSapPaymentTerms() != null) {
            target.setPaymentTerms(source.getSapPaymentTerms().getCode());
        }

        if (source.getSalesPerson() != null) {
            target.setSalesPerson(source.getSalesPerson());
        }

        if (null != source.getSapSalesOffice()) {
            target.setSalesTerritory(source.getSapSalesOffice().getCode());
        }

        if (source.getSapShippingCond() != null) {
            target.setShippingCondition(source.getSapShippingCond().getCode());
        }


        Set<ReferralCodesModel> refferalCodes = source.getReferralCodes();
        List<WholescriptsExternalAPIReferralCodeData> wholescriptsExternalAPIReferralCodeDataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(refferalCodes)) {
            WholescriptsExternalAPIReferralCodeData wholescriptsExternalAPIReferralCodeData;
            for (ReferralCodesModel referralCodesModel : refferalCodes) {

                wholescriptsExternalAPIReferralCodeData = new WholescriptsExternalAPIReferralCodeData();
                wholescriptsExternalAPIReferralCodeData.setReferralCode(referralCodesModel.getReferralCode());
                wholescriptsExternalAPIReferralCodeData.setReferralName(referralCodesModel.getLastName());

                if (source.getSapCustomerGroup() != null
                        && !StringUtils.equalsIgnoreCase(source.getSapCustomerGroup().getCode(), "02")) {
                    wholescriptsExternalAPIReferralCodeData.setAccessLevel("1");
                    if (referralCodesModel.getDiscount() != null) {
                        wholescriptsExternalAPIReferralCodeData.setDiscount(referralCodesModel.getDiscount().toString());
                    }
                }
                wholescriptsExternalAPIReferralCodeDataList.add(wholescriptsExternalAPIReferralCodeData);
            }
        }
        target.setReferralCodes(wholescriptsExternalAPIReferralCodeDataList);

        target.setPlDropShip(false);

        WholescriptsExternalAPIAgeRestrictionData wholescriptsExternalAPIAgeRestrictionData = new WholescriptsExternalAPIAgeRestrictionData();
        wholescriptsExternalAPIAgeRestrictionData.setBlockedUntil(source.getAgeBlockedDate());
        wholescriptsExternalAPIAgeRestrictionData.setVerifiedUntil(source.getAgeExpirationDate());
        target.setAgeRestrictionData(wholescriptsExternalAPIAgeRestrictionData);

    }

}
