package com.wholescripts.core.dao;

import java.util.List;

import com.wholescripts.core.model.WholescriptsUnsubscridbedEmailsModel;

public interface WholescriptsEmailDao {

	public List<WholescriptsUnsubscridbedEmailsModel> getUnsubscribedEmails();

}
