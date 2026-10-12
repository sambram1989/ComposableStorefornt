package com.wholescripts.core.service;

import com.wholescripts.dto.WholeScriptsPractitionerWsDTo;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import org.springframework.web.multipart.MultipartFile;

public interface WholescriptsPractitionerService {
    B2BCustomerModel savePractitionerDetails(WholeScriptsPractitionerWsDTo practitioner, GPS gps);

    PointOfServiceModel updatePractitionerDirectoryOptIn(String posName,Boolean optOutPractice,B2BCustomerModel customer);

    PointOfServiceModel updatePractitionerDetails(String posName, B2BCustomerModel customer, WholeScriptsPractitionerWsDTo practitioner, GPS gps);

    PointOfServiceModel saveProfileImage(String posName, B2BCustomerModel customer, MultipartFile file);

    PointOfServiceModel getPractitionerDetails(String posName, B2BCustomerModel customer);

    PointOfServiceModel getPosDetailsbyUid(String uid);

    void deletePractitionerProfileImage(String posName);
}
