package com.wholescripts.jalo;

import com.wholescripts.constants.WholescriptsbackofficeConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class WholescriptsbackofficeManager extends GeneratedWholescriptsbackofficeManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( WholescriptsbackofficeManager.class.getName() );
	
	public static final WholescriptsbackofficeManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (WholescriptsbackofficeManager) em.getExtension(WholescriptsbackofficeConstants.EXTENSIONNAME);
	}
	
}
