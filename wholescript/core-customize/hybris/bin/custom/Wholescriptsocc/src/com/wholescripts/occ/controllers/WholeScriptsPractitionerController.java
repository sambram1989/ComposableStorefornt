package com.wholescripts.occ.controllers;

import com.wholescripts.core.facade.WholescriptsPractitionerFacade;
import com.wholescripts.dto.WholeScriptsPractitionerWsDTo;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.cmssmarteditwebservices.dto.MediaWsDTO;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/stores")
@Tag(name = "Wholescripts Practitioners Point of Services ")
public class WholeScriptsPractitionerController extends BaseController {

    private static final Logger LOG = LogManager.getLogger(WholeScriptsPractitionerController.class);

    @Resource(name="wholescriptsPractitionerFacade")
    private WholescriptsPractitionerFacade WsPractitionerFacade;




    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT,WholescriptsSecuredAccessConstants.ROLE_CLIENT})
    @RequestMapping(value = "/practice",method = RequestMethod.POST)
    @ResponseStatus(value=HttpStatus.CREATED)
    @Operation(operationId = "addPractice", summary = "It will create a Practitioner")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public PointOfServiceWsDTO addPOSForPractitioner(@Parameter(description="Request body parameter that contains details such as the practitioner name,address ,image ") @RequestBody final WholeScriptsPractitionerWsDTo practitioner, @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            { "BASIC", "DEFAULT", "FULL" }))
    @RequestParam(defaultValue = "DEFAULT")
    final String fields)
    {
        final PointOfServiceWsDTO posWsDTO=new   PointOfServiceWsDTO() ;
       try{
           final GPS gps = WsPractitionerFacade.geocodeAddress(practitioner);
               if (gps != null ) {
                   LOG.info("Successfully fetched Latitude Longitude  for the given address" + gps.getDecimalLatitude() +gps.getDecimalLongitude() );
                   final PointOfServiceData PointOfServiceData =WsPractitionerFacade.savePractitionerDetails(practitioner,gps);
                   return getDataMapper().map(PointOfServiceData, PointOfServiceWsDTO.class, fields);

           }

       }
     catch(final ModelSavingException e){
         throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
	  }

        return posWsDTO;
    }
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT,WholescriptsSecuredAccessConstants.ROLE_CLIENT})
    @RequestMapping(value = "/getPractice",method = RequestMethod.GET)
    @Operation(operationId = "getPractice", summary = "It will fetch the  Practitioner Details")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public PointOfServiceWsDTO getPractitionerDetails(@Parameter( schema = @Schema(allowableValues =
            { "BASIC", "DEFAULT", "FULL" }))
    @RequestParam(defaultValue = "DEFAULT")
    final String fields)
    {
        try{
            final PointOfServiceData PointOfServiceData = WsPractitionerFacade.getPractitionerDetails();
            return getDataMapper().map(PointOfServiceData, PointOfServiceWsDTO.class, fields);
        }
        catch(final ModelSavingException e){
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }

    }
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT,WholescriptsSecuredAccessConstants.ROLE_CLIENT})
    @RequestMapping(value = "/practice",method = RequestMethod.PATCH)
    @Operation(operationId = "updatePractice", summary = "It will Update  the  Practitioner Details")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public PointOfServiceWsDTO updatePractitionerDetails(@Parameter(description="Request body parameter that contains details such as the practitioner name,address ,image ") @RequestBody final WholeScriptsPractitionerWsDTo practitioner,@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            { "BASIC", "DEFAULT", "FULL" }))
    @RequestParam(defaultValue = "DEFAULT")
    final String fields,@Parameter(description = "POS name to Update the Details", required = true)
    @RequestParam(required = true)  final String posName)
    {
        try{
            final PointOfServiceData PointOfServiceData =WsPractitionerFacade.updatePractitionerDetails(practitioner,posName);
            return getDataMapper().map(PointOfServiceData, PointOfServiceWsDTO.class, fields);
        }
        catch(final ModelSavingException e){
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }

    }
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT,WholescriptsSecuredAccessConstants.ROLE_CLIENT})
    @RequestMapping(value = "/pointOfService/optOutPractice",method = RequestMethod.PATCH)
    @Operation(operationId = "optOutPractice", summary = "It will remove   the  Practitioner from Directory")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public PointOfServiceWsDTO RemoveFromPractitionersDirectory (@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            { "BASIC", "DEFAULT", "FULL" }))
    @RequestParam(defaultValue = "DEFAULT")
    final String fields,@RequestParam(required = true) final Boolean optOutPractice,@Parameter(description = "POS name to Update the Flag", required = true)
    @RequestParam(required = true)
    final String posName)
    {
        try{
            PointOfServiceData pointOfServiceData= WsPractitionerFacade.updatePractitionerDirectoryOptIn(posName,optOutPractice);
            return getDataMapper().map(pointOfServiceData, PointOfServiceWsDTO.class, fields);
        }
        catch(final ModelSavingException e){
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }

    }
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT,WholescriptsSecuredAccessConstants.ROLE_CLIENT})
    @RequestMapping(value = "/uploadProfileImage",method = RequestMethod.POST)
    @Operation(operationId = "uploadProfileImage", summary = "It will Updates the Profile Image for the Practitioner")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public MediaWsDTO UploadProfileImageforPractitioner (@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            { "BASIC", "DEFAULT", "FULL" }))
                                                         @RequestParam(defaultValue = "DEFAULT")
                                                         final String fields, @Parameter(description = "POS name to Update the Image", required = true)
                                                         @RequestParam(required = true)
                                                         final String posName, @Parameter(description = "The file representing the actual binary contents of the media to be created.", required = true)
                                                         @RequestParam("file") final MultipartFile multiPart)
    {
        try{
            MediaData MediaData= WsPractitionerFacade.saveProfileImage(posName,multiPart);
            return getDataMapper().map(MediaData, MediaWsDTO.class, fields);
        }
        catch(final ModelSavingException e){
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }

    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @DeleteMapping(value = "/deleteProfileImage")
    @Operation(operationId = "deleteProfileImage", summary = "It will remove the Profile Image for the Practitioner")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public void deleteProfileImageforPractitioner(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            {"BASIC", "DEFAULT", "FULL"}))
                                                  @RequestParam(defaultValue = "DEFAULT") final String fields, @RequestParam(required = true) final String posName) {
        try{
            WsPractitionerFacade.deletePractitionerProfileImage(posName);
        }catch (final ModelSavingException e) {
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }
    }




}
