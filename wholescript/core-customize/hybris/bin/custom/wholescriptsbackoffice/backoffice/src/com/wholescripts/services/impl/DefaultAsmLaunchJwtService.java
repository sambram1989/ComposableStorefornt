package com.wholescripts.services.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wholescripts.services.AsmLaunchJwtService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

public class DefaultAsmLaunchJwtService implements AsmLaunchJwtService {

    private static final Logger LOG = LogManager.getLogger(DefaultAsmLaunchJwtService.class);
    private static final String MOBILE_ANDROID = "mobile_android";
    private static final String PASSWORD = "password";
    private static final String READ = "read";
    private static final String write = "write";
    private static final String SCOPE = "scope";
    private static final String SCOPE_VALUE = "";
    private static final String CLIENT_ID = "client_id";
    private static final String GRANT_TYPE = "grant_type";
    private static final String USERNAME = "username";
    static final String OAUTH_URL = "ccv2.services.api.url.0";
    static final String TOKEN_API = "/authorizationserver/oauth/token";

    @Resource(name="configurationService")
    private ConfigurationService configurationService;

    @Resource(name="userService")
    private UserService userService;

    @Resource(name="backofficeUserDetailsService")
    private UserDetailsService userDetailsService;

    @Resource(name="defaultOauthTokenServices")
    private DefaultTokenServices tokenServices;

    @Override
    public String generateJwtToken() {
        String accessToken = null;
        UserModel user = userService.getCurrentUser();
        String uid = user.getUid();
        String name = user.getName();

        try {
            accessToken = getAccesstoken();
        } catch (IOException e) {
            LOG.error("Error generating JWT token: Failed to retrieve access token", e);
            return null;
        }catch (Exception e){
            LOG.error("Error generating JWT token" +e.getCause());
            return null;
        }
        String secretKey = configurationService.getConfiguration()
                .getString("wholescripts.asm.jwt.secret");

        long expirationTime = configurationService.getConfiguration()
                .getLong("wholescripts.asm.jwt.expiration", 900000);

            return io.jsonwebtoken.Jwts.builder()
                .claim("access_token", accessToken)
                .claim("uid", uid)
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    protected String getAccesstoken() throws IOException {

        UserModel user = userService.getCurrentUser();
        String uid = user.getUid();

        final HashMap<String, String> authorizationParameters = new HashMap<String, String>();
        authorizationParameters.put(SCOPE, SCOPE_VALUE);
        authorizationParameters.put(USERNAME, uid);
        authorizationParameters.put(CLIENT_ID, MOBILE_ANDROID);
        authorizationParameters.put(GRANT_TYPE, PASSWORD);

        final Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetailsService.loadUserByUsername(uid)
                .getAuthorities();

        final Set<String> responseType = new HashSet<String>();
        responseType.add(PASSWORD);

        final Set<String> scopes = new HashSet<String>();
        scopes.add(READ);
        scopes.add(write);

        final OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, MOBILE_ANDROID, authorities, true,
                scopes, null, "", responseType, null);

        final User userPrincipal = new User(uid, "", true, true, true, true, authorities);

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, null,
                authorities);

        final OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);

        final OAuth2AccessToken accessToken = tokenServices.createAccessToken(authenticationRequest);
        return accessToken.getValue();
    }
}

