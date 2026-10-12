/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WholescriptsTopic.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsTopic extends GenericItem
{
	/** Qualifier of the <code>WholescriptsTopic.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>WholescriptsTopic.page</code> attribute **/
	public static final String PAGE = "page";
	/** Qualifier of the <code>WholescriptsTopic.childTopic</code> attribute **/
	public static final String CHILDTOPIC = "childTopic";
	/** Qualifier of the <code>WholescriptsTopic.headline</code> attribute **/
	public static final String HEADLINE = "headline";
	/** Qualifier of the <code>WholescriptsTopic.mainContent</code> attribute **/
	public static final String MAINCONTENT = "mainContent";
	/** Qualifier of the <code>WholescriptsTopic.specs</code> attribute **/
	public static final String SPECS = "specs";
	/** Qualifier of the <code>WholescriptsTopic.images</code> attribute **/
	public static final String IMAGES = "images";
	/** Qualifier of the <code>WholescriptsTopic.requestURL</code> attribute **/
	public static final String REQUESTURL = "requestURL";
	/** Qualifier of the <code>WholescriptsTopic.requestBody</code> attribute **/
	public static final String REQUESTBODY = "requestBody";
	/** Qualifier of the <code>WholescriptsTopic.response</code> attribute **/
	public static final String RESPONSE = "response";
	/** Qualifier of the <code>WholescriptsTopic.requestValues</code> attribute **/
	public static final String REQUESTVALUES = "requestValues";
	/** Qualifier of the <code>WholescriptsTopic.responseValues</code> attribute **/
	public static final String RESPONSEVALUES = "responseValues";
	/** Qualifier of the <code>WholescriptsTopic.curlURL</code> attribute **/
	public static final String CURLURL = "curlURL";
	/** Qualifier of the <code>WholescriptsTopic.authorization</code> attribute **/
	public static final String AUTHORIZATION = "authorization";
	/** Qualifier of the <code>WholescriptsTopic.authorizationURL</code> attribute **/
	public static final String AUTHORIZATIONURL = "authorizationURL";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(PAGE, AttributeMode.INITIAL);
		tmp.put(CHILDTOPIC, AttributeMode.INITIAL);
		tmp.put(HEADLINE, AttributeMode.INITIAL);
		tmp.put(MAINCONTENT, AttributeMode.INITIAL);
		tmp.put(SPECS, AttributeMode.INITIAL);
		tmp.put(IMAGES, AttributeMode.INITIAL);
		tmp.put(REQUESTURL, AttributeMode.INITIAL);
		tmp.put(REQUESTBODY, AttributeMode.INITIAL);
		tmp.put(RESPONSE, AttributeMode.INITIAL);
		tmp.put(REQUESTVALUES, AttributeMode.INITIAL);
		tmp.put(RESPONSEVALUES, AttributeMode.INITIAL);
		tmp.put(CURLURL, AttributeMode.INITIAL);
		tmp.put(AUTHORIZATION, AttributeMode.INITIAL);
		tmp.put(AUTHORIZATIONURL, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.authorization</code> attribute.
	 * @return the authorization
	 */
	public Map<String,String> getAllAuthorization(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "authorization".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.authorization</code> attribute.
	 * @return the authorization
	 */
	public Map<String,String> getAllAuthorization()
	{
		return getAllAuthorization( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.authorization</code> attribute. 
	 * @param value the authorization
	 */
	public void setAllAuthorization(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "authorization".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.authorization</code> attribute. 
	 * @param value the authorization
	 */
	public void setAllAuthorization(final Map<String,String> value)
	{
		setAllAuthorization( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.authorizationURL</code> attribute.
	 * @return the authorizationURL
	 */
	public Map<String,String> getAllAuthorizationURL(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "authorizationURL".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.authorizationURL</code> attribute.
	 * @return the authorizationURL
	 */
	public Map<String,String> getAllAuthorizationURL()
	{
		return getAllAuthorizationURL( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.authorizationURL</code> attribute. 
	 * @param value the authorizationURL
	 */
	public void setAllAuthorizationURL(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "authorizationURL".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.authorizationURL</code> attribute. 
	 * @param value the authorizationURL
	 */
	public void setAllAuthorizationURL(final Map<String,String> value)
	{
		setAllAuthorizationURL( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.childTopic</code> attribute.
	 * @return the childTopic
	 */
	public String getChildTopic(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "childTopic".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.childTopic</code> attribute.
	 * @return the childTopic
	 */
	public String getChildTopic()
	{
		return getChildTopic( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.childTopic</code> attribute. 
	 * @param value the childTopic
	 */
	public void setChildTopic(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "childTopic".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.childTopic</code> attribute. 
	 * @param value the childTopic
	 */
	public void setChildTopic(final String value)
	{
		setChildTopic( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.curlURL</code> attribute.
	 * @return the curlURL
	 */
	public String getCurlURL(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "curlURL".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.curlURL</code> attribute.
	 * @return the curlURL
	 */
	public String getCurlURL()
	{
		return getCurlURL( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.curlURL</code> attribute. 
	 * @param value the curlURL
	 */
	public void setCurlURL(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "curlURL".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.curlURL</code> attribute. 
	 * @param value the curlURL
	 */
	public void setCurlURL(final String value)
	{
		setCurlURL( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.headline</code> attribute.
	 * @return the headline
	 */
	public Map<String,String> getAllHeadline(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "headline".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.headline</code> attribute.
	 * @return the headline
	 */
	public Map<String,String> getAllHeadline()
	{
		return getAllHeadline( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.headline</code> attribute. 
	 * @param value the headline
	 */
	public void setAllHeadline(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "headline".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.headline</code> attribute. 
	 * @param value the headline
	 */
	public void setAllHeadline(final Map<String,String> value)
	{
		setAllHeadline( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.images</code> attribute.
	 * @return the images
	 */
	public Collection<Media> getImages(final SessionContext ctx)
	{
		Collection<Media> coll = (Collection<Media>)getProperty( ctx, "images".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.images</code> attribute.
	 * @return the images
	 */
	public Collection<Media> getImages()
	{
		return getImages( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.images</code> attribute. 
	 * @param value the images
	 */
	public void setImages(final SessionContext ctx, final Collection<Media> value)
	{
		setProperty(ctx, "images".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.images</code> attribute. 
	 * @param value the images
	 */
	public void setImages(final Collection<Media> value)
	{
		setImages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.mainContent</code> attribute.
	 * @return the mainContent - Main Content.
	 */
	public String getMainContent(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "mainContent".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.mainContent</code> attribute.
	 * @return the mainContent - Main Content.
	 */
	public String getMainContent()
	{
		return getMainContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.mainContent</code> attribute. 
	 * @param value the mainContent - Main Content.
	 */
	public void setMainContent(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "mainContent".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.mainContent</code> attribute. 
	 * @param value the mainContent - Main Content.
	 */
	public void setMainContent(final String value)
	{
		setMainContent( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.page</code> attribute.
	 * @return the page - page
	 */
	public String getPage(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "page".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.page</code> attribute.
	 * @return the page - page
	 */
	public String getPage()
	{
		return getPage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.page</code> attribute. 
	 * @param value the page - page
	 */
	public void setPage(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "page".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.page</code> attribute. 
	 * @param value the page - page
	 */
	public void setPage(final String value)
	{
		setPage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.requestBody</code> attribute.
	 * @return the requestBody
	 */
	public Map<String,String> getAllRequestBody(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "requestBody".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.requestBody</code> attribute.
	 * @return the requestBody
	 */
	public Map<String,String> getAllRequestBody()
	{
		return getAllRequestBody( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.requestBody</code> attribute. 
	 * @param value the requestBody
	 */
	public void setAllRequestBody(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "requestBody".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.requestBody</code> attribute. 
	 * @param value the requestBody
	 */
	public void setAllRequestBody(final Map<String,String> value)
	{
		setAllRequestBody( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.requestURL</code> attribute.
	 * @return the requestURL
	 */
	public Map<String,String> getAllRequestURL(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "requestURL".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.requestURL</code> attribute.
	 * @return the requestURL
	 */
	public Map<String,String> getAllRequestURL()
	{
		return getAllRequestURL( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.requestURL</code> attribute. 
	 * @param value the requestURL
	 */
	public void setAllRequestURL(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "requestURL".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.requestURL</code> attribute. 
	 * @param value the requestURL
	 */
	public void setAllRequestURL(final Map<String,String> value)
	{
		setAllRequestURL( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.requestValues</code> attribute.
	 * @return the requestValues
	 */
	public Map<String,String> getAllRequestValues(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "requestValues".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.requestValues</code> attribute.
	 * @return the requestValues
	 */
	public Map<String,String> getAllRequestValues()
	{
		return getAllRequestValues( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.requestValues</code> attribute. 
	 * @param value the requestValues
	 */
	public void setAllRequestValues(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "requestValues".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.requestValues</code> attribute. 
	 * @param value the requestValues
	 */
	public void setAllRequestValues(final Map<String,String> value)
	{
		setAllRequestValues( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.response</code> attribute.
	 * @return the response
	 */
	public Map<String,String> getAllResponse(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "response".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.response</code> attribute.
	 * @return the response
	 */
	public Map<String,String> getAllResponse()
	{
		return getAllResponse( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.response</code> attribute. 
	 * @param value the response
	 */
	public void setAllResponse(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "response".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.response</code> attribute. 
	 * @param value the response
	 */
	public void setAllResponse(final Map<String,String> value)
	{
		setAllResponse( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.responseValues</code> attribute.
	 * @return the responseValues
	 */
	public Map<String,String> getAllResponseValues(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "responseValues".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.responseValues</code> attribute.
	 * @return the responseValues
	 */
	public Map<String,String> getAllResponseValues()
	{
		return getAllResponseValues( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.responseValues</code> attribute. 
	 * @param value the responseValues
	 */
	public void setAllResponseValues(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "responseValues".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.responseValues</code> attribute. 
	 * @param value the responseValues
	 */
	public void setAllResponseValues(final Map<String,String> value)
	{
		setAllResponseValues( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.specs</code> attribute.
	 * @return the specs
	 */
	public Map<String,String> getAllSpecs(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, "specs".intern());
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsTopic.specs</code> attribute.
	 * @return the specs
	 */
	public Map<String,String> getAllSpecs()
	{
		return getAllSpecs( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.specs</code> attribute. 
	 * @param value the specs
	 */
	public void setAllSpecs(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, "specs".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsTopic.specs</code> attribute. 
	 * @param value the specs
	 */
	public void setAllSpecs(final Map<String,String> value)
	{
		setAllSpecs( getSession().getSessionContext(), value );
	}
	
}
