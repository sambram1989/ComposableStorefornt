package com.wholescripts.core.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import javax.annotation.Resource;

import com.wholescripts.core.dao.WholescriptsEmailDao;
import com.wholescripts.core.model.WholescriptsUnsubscridbedEmailsModel;

public class WholescriptsEmailDaoImpl implements WholescriptsEmailDao {

	private final static String UNSUBSCRIBED_EMAILS_QUERY = "select {pk} from {WholescriptsUnsubscridbedEmails}";

	@Resource(name = "flexibleSearchService")
   private FlexibleSearchService flexibleSearchService;

	@Override
	public List<WholescriptsUnsubscridbedEmailsModel> getUnsubscribedEmails() {
		 final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(UNSUBSCRIBED_EMAILS_QUERY);
		final SearchResult<WholescriptsUnsubscridbedEmailsModel> result = flexibleSearchService.search(searchQuery);
       return result.getResult().isEmpty() ? null : result.getResult();
	}

}
