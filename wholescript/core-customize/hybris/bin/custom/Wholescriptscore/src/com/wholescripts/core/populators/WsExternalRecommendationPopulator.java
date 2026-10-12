package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.model.*;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.WsExternalRecommendationData;
import com.wholescripts.data.WsExternalRecommendationEntryData;
import com.wholescripts.data.WsMedPaxPillData;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WsExternalRecommendationPopulator implements Populator<RecommendationModel, WsExternalRecommendationData> {

	public static final String MEDAX_CARTON = "medpax.product.carton";
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "flexibleSearchService")
    public FlexibleSearchService flexibleSearchService;
    @Resource(name = "wholescriptsMedPaxDao")
    public WholescriptsMedPaxDao wholescriptsMedPaxDao;

    @Resource(name = "wsExternalRecommendationEntryConverter")
    private Converter<AbstractOrderEntryModel, WsExternalRecommendationEntryData> wsExternalRecommendationEntryConverter;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;
    
    @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;
    
    @Override
    public void populate(RecommendationModel source, WsExternalRecommendationData target) throws ConversionException {

        if (StringUtils.isNotEmpty(source.getCode())) {
            target.setId(source.getCode());
        }
        if (source.getUser() != null && StringUtils.isNotEmpty(source.getUser().getUid())) {
            target.setPractitionerEmail(source.getUser().getUid());
        }
        if (source.getPatient() != null && StringUtils.isNotEmpty(source.getPatient().getEmail())) {
            target.setPatientEmail(source.getPatient().getEmail());
            final B2BUnitModel unit = source.getPatient().getDefaultB2BUnit();
            final String defaultWSUnit = configurationService.getConfiguration().getString("wholescripts.default.b2b.unit.code");
            if (unit != null && !unit.getUid().equalsIgnoreCase(defaultWSUnit)) {
            	final String unitId = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(unit.getUid());
            	target.setPatientSAPId(unitId);
            }
        }
        
        target.setExternalOrder(source.getExternalOrder());
        target.setSourceId(source.getSourceId());
        target.setSourceName(source.getSourceName());
        
        target.setSendEmail(source.getSendEmail());

        String fullName = source.getPatient().getName();
        if (fullName != null && !fullName.isEmpty()) {
            int spaceIndex = fullName.indexOf(' '); // Find the index of the first space
            if (spaceIndex != -1) {
                String firstName = fullName.substring(0, spaceIndex); // From start to the first space
                String lastName = fullName.substring(spaceIndex + 1); // From after the space to the end
                target.setPatientFirstName(firstName);
                target.setPatientLastName(lastName);
            } else {
                // No space found, set full name as first name and leave last name empty
                target.setPatientFirstName(fullName);
            }
        }
        
        
        if (source.getCreationtime() != null) {
      	  final Date creationDate = source.getCreationtime();
      	  target.setDateSentUtc(WholescriptsDateUtils.convertDateInUTCFormat(creationDate));
        }

        if (CollectionUtils.isNotEmpty(source.getComments())) {
            String notes = source.getComments().stream()
                    .filter(comment -> "customerNote".equalsIgnoreCase(comment.getCommentType().getCode())) // Filter by commentType
                    .map(CommentModel::getText)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(",")); // Concatenate with comma separator

            target.setNotes(notes);
        }

        if (Objects.nonNull(source.getRecommendationStatus())) {
            target.setStatus(source.getRecommendationStatus().getCode());
        }
        
        if (source.getReferralCode() != null) {
      	  target.setReferralCode(source.getReferralCode());
        }
        
        if (source.getReferralName() != null) {
      	  target.setReferralName(source.getReferralName());
        }

        List<WsExternalRecommendationEntryData> entryDataList = new ArrayList<>();

        if (source.getEntries() != null) {
            entryDataList.addAll(wsExternalRecommendationEntryConverter.convertAll(source.getEntries()));
        }
        // Add MedPax as a separate item
        MedPaxModel medPaxModel = wholescriptsMedPaxDao.getMedPaxForOwner(source);
        if (Objects.nonNull(medPaxModel)) {
            WsExternalRecommendationEntryData medPaxEntry = new WsExternalRecommendationEntryData();
           // medPaxEntry.setSku(Config.getParameter(MEDAX_CARTON)); // Hardcoded SKU
            if(Objects.nonNull(medPaxModel.getCarton())) {
            	medPaxEntry.setSku(medPaxModel.getCarton().getCode());
            }
            medPaxEntry.setMedPaxName(medPaxModel.getName());
            medPaxEntry.setQuantity(1L); // Default 1
            medPaxEntry.setDosing(null); // Null for MedPax
            if (CollectionUtils.isNotEmpty(medPaxModel.getComments())) {
                String notes = medPaxModel.getComments().stream()
                        .filter(comment -> "customerNote".equalsIgnoreCase(comment.getCommentType().getCode())) // Filter by commentType
                        .map(CommentModel::getText)
                        .filter(Objects::nonNull)
                        .collect(Collectors.joining(","));

                if (StringUtils.isNotEmpty(notes)) {
               	 medPaxEntry.setNotes(notes);
                }
            }
            
            //medPaxEntry.setItemDuration("4 Weeks");
            
            if (Objects.nonNull(medPaxModel.getItemDuration())) {
               if (StringUtils.isNotEmpty(medPaxModel.getItemDuration().getCode())) {
               	medPaxEntry.setItemDuration(enumerationService.getEnumerationName(medPaxModel.getItemDuration()));
               }
           }

            // Map MedPax Pills
            if (medPaxModel.getMedpaxEntry() != null) {
                List<WsMedPaxPillData> pillDataList = mapMedPaxPillsToEntry(medPaxModel.getMedpaxEntry());
                medPaxEntry.setMedPaxPills(pillDataList);
            }

            entryDataList.add(medPaxEntry);
        }
        // Set items to the target
        target.setItems(entryDataList);
    }

    private List<WsMedPaxPillData> mapMedPaxPillsToEntry(List<MedpaxEntryModel> medpaxEntryModels) {
        List<WsMedPaxPillData> pillDataList = new ArrayList<>();
        for (MedpaxEntryModel entry : medpaxEntryModels) {
            WsMedPaxPillData pillData = new WsMedPaxPillData();
            pillData.setSku(entry.getProduct().getCode());
            pillData.setName(entry.getProduct().getName());
            for (DosingModel dosing : entry.getDosing()) {
                pillData.setQuantity(dosing.getQuantity());
                // Combine item times into a comma-separated string
                StringBuilder itemTimes = new StringBuilder();
                for (ItemTimeModel itemTime : dosing.getItemTimes()) {
                    if (!itemTimes.isEmpty()) {
                        itemTimes.append(", ");
                    }
                    itemTimes.append(itemTime.getName());
                }
                pillData.setItemTime(itemTimes.toString());
                pillDataList.add(pillData);
            }
        }
        return pillDataList;
    }
}