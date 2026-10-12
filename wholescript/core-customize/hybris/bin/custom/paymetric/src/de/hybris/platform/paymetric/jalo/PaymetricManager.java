/*
 *  
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.paymetric.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.paymetric.constants.PaymetricConstants;
import org.apache.log4j.Logger;

public class PaymetricManager extends GeneratedPaymetricManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( PaymetricManager.class.getName() );
	
	public static final PaymetricManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PaymetricManager) em.getExtension(PaymetricConstants.EXTENSIONNAME);
	}
	
}
