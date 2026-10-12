package com.wholescritps.external.webservices.jalo;

import com.wholescritps.external.webservices.constants.WholescritpsextwebservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class WholescritpsextwebservicesManager extends GeneratedWholescritpsextwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( WholescritpsextwebservicesManager.class.getName() );
	
	public static final WholescritpsextwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (WholescritpsextwebservicesManager) em.getExtension(WholescritpsextwebservicesConstants.EXTENSIONNAME);
	}
	
}
