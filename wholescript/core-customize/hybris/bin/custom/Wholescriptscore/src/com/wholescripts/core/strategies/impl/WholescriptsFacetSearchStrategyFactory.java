package com.wholescripts.core.strategies.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategyFactory;

public class WholescriptsFacetSearchStrategyFactory extends DefaultFacetSearchStrategyFactory {
	
	private FacetSearchStrategy wholescriptsFacetSearchStrategy;

	private FacetSearchStrategy wholescriptsWebinarFacetSearchStrategy;
	
	@Override
	public FacetSearchStrategy createStrategy(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType) {
      
		if(facetSearchConfig.getSearchConfig().isLegacyMode()) {
      	return getLegacyFacetSearchStrategy();
      } else if(indexedType.getIdentifier().equalsIgnoreCase("wholescriptsProductType")) {
      	return getWholescriptsFacetSearchStrategy();
      }
		else if(indexedType.getIdentifier().equalsIgnoreCase("wholescriptsWebinarType"))
		{
			return getWholescriptsWebinarFacetSearchStrategy();
		}
		return getDefaultFacetSearchStrategy();
		
   }
	
	public FacetSearchStrategy getWholescriptsFacetSearchStrategy() {
		return wholescriptsFacetSearchStrategy;
	}

	
	public void setWholescriptsFacetSearchStrategy(FacetSearchStrategy wholescriptsFacetSearchStrategy) {
		this.wholescriptsFacetSearchStrategy = wholescriptsFacetSearchStrategy;
	}

	public FacetSearchStrategy getWholescriptsWebinarFacetSearchStrategy() {
		return wholescriptsWebinarFacetSearchStrategy;
	}

	public void setWholescriptsWebinarFacetSearchStrategy(FacetSearchStrategy wholescriptsWebinarFacetSearchStrategy) {
		this.wholescriptsWebinarFacetSearchStrategy = wholescriptsWebinarFacetSearchStrategy;
	}
}
