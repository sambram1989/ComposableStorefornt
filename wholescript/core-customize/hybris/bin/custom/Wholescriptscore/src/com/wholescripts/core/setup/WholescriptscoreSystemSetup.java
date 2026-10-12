/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.setup;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.service.WholescriptsCoreService;
import de.hybris.platform.core.initialization.SystemSetup;


@SystemSetup(extension = WholescriptscoreConstants.EXTENSIONNAME)
public class WholescriptscoreSystemSetup
{
	private final WholescriptsCoreService WholescriptscoreService;

	public WholescriptscoreSystemSetup(final WholescriptsCoreService WholescriptscoreService)
	{
		this.WholescriptscoreService = WholescriptscoreService;
	}
}
