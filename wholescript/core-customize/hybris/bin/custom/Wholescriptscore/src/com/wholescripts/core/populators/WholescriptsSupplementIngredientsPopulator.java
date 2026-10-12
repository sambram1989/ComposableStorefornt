package com.wholescripts.core.populators;

import com.wholescripts.core.model.IngredientModel;
import com.wholescripts.data.IngredientData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;


public class WholescriptsSupplementIngredientsPopulator implements Populator<IngredientModel, IngredientData> {

    @Override
    public void populate(final IngredientModel ingredientModel, final IngredientData ingredientData) {
        ingredientData.setCode(ingredientModel.getCode());
        ingredientData.setName(ingredientModel.getIngredientName());
        ingredientData.setRda(ingredientModel.getRda());
        ingredientData.setPriority(ingredientModel.getPriority());
        ingredientData.setUnit(String.valueOf(ingredientModel.getUnit()));
        ingredientData.setComments(String.valueOf(ingredientModel.getComments()));
    }

}
