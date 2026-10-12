/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import com.wholescripts.core.model.PromotionCategoryModel;

/**
 *
 */
public interface WholescriptsPromotionCategoryDao
{
  public PromotionCategoryModel getPromotionCategoryByCode(String code);
}
