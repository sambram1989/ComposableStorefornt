package com.wholescripts.core.populators;

import com.wholescripts.core.model.DosingModel;
import com.wholescripts.data.WsExternalRecommendationEntryData;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

public class WsExternalRecommendationEntryPopulator implements Populator<RecommendationEntryModel, WsExternalRecommendationEntryData> {

	 @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;
	 
    @Override
    public void populate(RecommendationEntryModel source, WsExternalRecommendationEntryData target) throws ConversionException {

        if (StringUtils.isNotEmpty(source.getProduct().getCode())) {
            target.setSku(source.getProduct().getCode());
        }
        if (StringUtils.isNotEmpty(source.getProduct().getName())) {
            target.setName(source.getProduct().getName());
        }
        
        if (source.getQuantity() != null) {
            target.setQuantity(source.getQuantity());
        }
        if (CollectionUtils.isNotEmpty(source.getComments())) {
            String notes = source.getComments().stream()
                    .filter(comment -> "customerNote".equalsIgnoreCase(comment.getCommentType().getCode())) // Filter by commentType
                    .map(CommentModel::getText)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(",")); // Concatenate with comma separator

            if (StringUtils.isNotEmpty(notes)) {
            	target.setNotes(notes);
            }
        }
        
        if (Objects.nonNull(source.getItemDuration())) {
            if (StringUtils.isNotEmpty(source.getItemDuration().getCode())) {
                target.setItemDuration(enumerationService.getEnumerationName(source.getItemDuration()));
            }
        }

        List<DosingModel> dosingModels = source.getDosing();
        if (Objects.nonNull(dosingModels)) {
            String dosing = dosingModels.stream()
                    .flatMap(dosingModel -> dosingModel.getItemTimes().stream()
                            .map(itemTime -> dosingModel.getQuantity() + "-" + itemTime.getCode())) // Combine quantity and itemTime.code
                    .collect(Collectors.joining("|")); // Concatenate with '|' separator
            target.setDosing(dosing);
        }

        target.setMedPaxPills(Collections.emptyList());

    }

}
