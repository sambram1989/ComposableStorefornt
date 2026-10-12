package com.wholescripts.core.facade;

import com.wholescripts.dto.WholeScriptsPractitionerWsDTo;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.storelocator.GPS;
import org.springframework.web.multipart.MultipartFile;

public interface  WholescriptsPractitionerFacade {

    GPS geocodeAddress(WholeScriptsPractitionerWsDTo practitioner);
    PointOfServiceData savePractitionerDetails(WholeScriptsPractitionerWsDTo practitioner,GPS gps);

    PointOfServiceData getPractitionerDetails();

    PointOfServiceData   updatePractitionerDirectoryOptIn(String posName,  Boolean optOutPractice);

    PointOfServiceData updatePractitionerDetails(WholeScriptsPractitionerWsDTo practitioner, String posName);

    MediaData saveProfileImage(String posName, MultipartFile customer);

    void deletePractitionerProfileImage(String posName);
}
