package com.wholescripts.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.wholescripts.core.dao.WholescriptsEmailDao;
import com.wholescripts.core.model.WholescriptsUnsubscridbedEmailsModel;
import com.wholescripts.core.service.WholescriptsEmailService;

public class WholescriptsEmailServiceImpl implements WholescriptsEmailService {

	@Autowired
	private WholescriptsEmailDao wholescriptsEmailDao;

	@Override
	public List<String> getUnsubscribedEmails() {
		final List<WholescriptsUnsubscridbedEmailsModel> unsubscribedEmailModels = wholescriptsEmailDao.getUnsubscribedEmails();
		final List<String> emails = new ArrayList<String>();
		if (unsubscribedEmailModels != null) {
			unsubscribedEmailModels.forEach(unsubscribedEmail -> emails.add(unsubscribedEmail.getEmailAddress()));
		}
		return emails;
	}

}
