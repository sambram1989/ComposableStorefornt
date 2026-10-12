/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.wholescripts.core.facade.WholescriptsDelegoFacade;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class WholescriptsDelegoFacadeImpl implements WholescriptsDelegoFacade {

	private static final Logger LOG = Logger.getLogger(WholescriptsDelegoFacadeImpl.class);
	private static final String DELEGO_PASSWORD = "wholescripts.delego.password";
	private static final String DELEGO_KID = "wholescripts.delego.kid";
	private static final String DELEGO_CERTIFICATE_NAME = "wholescripts.delego.certificate.name";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Override
	public String getJWTToken() throws Exception {

		final String configDir = configurationService.getConfiguration().getString("HYBRIS_CONFIG_DIR");
		final String certificateFileName = configurationService.getConfiguration().getString(DELEGO_CERTIFICATE_NAME);
		final String fileLocation = configDir + "/security/" + certificateFileName;

		final String delegoPassword = configurationService.getConfiguration().getString(DELEGO_PASSWORD);

		final RSAPrivateKey privateKey = getPrivateKey(fileLocation, delegoPassword);

		String jwtToken = null;
		final long issuedTime = (new Date().getTime()) / 1000;
		final UUID jti = UUID.randomUUID();
		final String kId = configurationService.getConfiguration().getString(DELEGO_KID);

		// headers
		final Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("kid", kId);

		// payload
		final Map<String, Object> payload = new HashMap<>();
		payload.put("iat", issuedTime);
		payload.put("exp", issuedTime + 6000);
		payload.put("jti", jti.toString());


		if (privateKey != null) {
			
			jwtToken = Jwts.builder().setClaims(payload)
					.setHeader(headers)
					.signWith(SignatureAlgorithm.RS256, privateKey).compact();
		}

		return jwtToken;
	}


	protected RSAPrivateKey getPrivateKey(final String certPath, final String password) throws Exception {

		RSAPrivateKey privateKey = null;
		final KeyStore keyStore = KeyStore.getInstance("PKCS12");
		final FileInputStream fis = new FileInputStream(certPath);

		keyStore.load(fis, password.toCharArray());

		final Enumeration<String> aliasEnum = keyStore.aliases();

		while (aliasEnum.hasMoreElements()) {
			String keyName = (String) aliasEnum.nextElement();
			Key key = keyStore.getKey(keyName, password.toCharArray());
			if (key instanceof RSAPrivateKey) {
				privateKey = (RSAPrivateKey) key;
			}
			else {
				LOG.error("No PrivateKey found under alias: " + keyName);
			}
		}
		
		
		return privateKey;

	}

	

}
