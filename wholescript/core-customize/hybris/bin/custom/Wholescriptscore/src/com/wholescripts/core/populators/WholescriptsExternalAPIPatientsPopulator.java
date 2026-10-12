package com.wholescripts.core.populators;

import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.WholescriptsExternalAPIPatientData;
import com.wholescripts.data.WholescriptsExternalAPIReferralCodeData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.util.CollectionUtil;

import javax.annotation.Resource;

import java.util.Objects;
import java.util.Set;



public class WholescriptsExternalAPIPatientsPopulator implements Populator<B2BUnitModel, WholescriptsExternalAPIPatientData> {

	@Resource(name = "wholescriptsHelper")
	protected WholescriptsHelper wholescriptsHelper;

	@Resource(name = "b2bUnitService")
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;


	@Override
	public void populate(B2BUnitModel source, WholescriptsExternalAPIPatientData target) throws ConversionException {

		if (null != source) {

			final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(source);
			if (CollectionUtils.isNotEmpty(customers)) {

				final B2BCustomerModel customer = customers.iterator().next();

				target.setSAPCustomerId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(source.getUid()));

				if (CollectionUtils.isNotEmpty(source.getReferralCodes())) {
					WholescriptsExternalAPIReferralCodeData wholescriptsExternalAPIReferralCodeData = new WholescriptsExternalAPIReferralCodeData();

					ReferralCodesModel referralCodesModel = source.getReferralCodes().stream().findFirst().get();
					wholescriptsExternalAPIReferralCodeData.setReferralCode(referralCodesModel.getReferralCode());
					wholescriptsExternalAPIReferralCodeData.setReferralName(referralCodesModel.getLastName());
					target.setReferralCode(wholescriptsExternalAPIReferralCodeData);
				}

				target.setEmail(customer.getUid());

				AddressModel addressModel = WholescriptsHelper.returnValidAddress(source);
				if (addressModel != null) {
					target.setFirstName(addressModel.getFirstname());
					target.setLastName(addressModel.getLastname());
				}

				if (StringUtils.isBlank(target.getFirstName())) {
					target.setFirstName(source.getName());
				}
			}

		}
	}

}
