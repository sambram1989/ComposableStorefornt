/**
 *
 */
package com.wholescripts.core.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import com.wholescripts.core.model.PatientDirectHubModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BUserPopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.codec.binary.StringUtils;

import com.wholescripts.core.model.PatientDirectHubModel;
import com.wholescripts.core.model.ReferralCodesModel;
import org.apache.commons.collections.CollectionUtils;


/**
 * @author Manoharc
 *
 */
public class WholescriptsUserPopulator extends B2BUserPopulator
{
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "pointOfServiceConverter")
	Converter pointOfServiceConverter;

	@Override
	public void populate(final B2BCustomerModel source, final CustomerData target)
	{
		//		super.populate(source, target);
		validateParameterNotNull(source, "Parameter source cannot be null.");
		validateParameterNotNull(target, "Parameter target cannot be null.");

		Set<ReferralCodesModel> b2bUnitReferralCodes = source.getDefaultB2BUnit().getReferralCodes();
		if (b2bUnitReferralCodes != null && !b2bUnitReferralCodes.isEmpty()) {
			List<ReferralCodesModel> referralCodesList = new ArrayList<>(b2bUnitReferralCodes);
			String firstReferralCode = referralCodesList.get(0).getReferralCode();
			String lastName = referralCodesList.get(0).getLastName();
			target.setReferralCode(firstReferralCode.concat("|").concat(lastName));
		}

		target.setEmail(source.getUid());
		target.setName(source.getName());
		final B2BUnitModel defaultB2BUnit = source.getDefaultB2BUnit();
		if (Objects.nonNull(defaultB2BUnit)) {
			//final PatientDirectHubModel disp = defaultB2BUnit.getPatientDirectHub();
			target.setDiscount(defaultB2BUnit.getDiscount());
		}
		target.setModifiedTime(source.getModifiedtime().toString());

		if(CollectionUtils.isNotEmpty(source.getPractitionerDetails()))
		{
			target.setPosData(pointOfServiceConverter.convertAll(source.getPractitionerDetails()));

		}
	}
}