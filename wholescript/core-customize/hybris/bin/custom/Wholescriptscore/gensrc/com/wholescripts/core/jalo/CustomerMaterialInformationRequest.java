/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.WholescriptsOutboundRequest;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.sap.sapmodel.jalo.SAPSalesOrganization;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type CustomerMaterialInformationRequest.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class CustomerMaterialInformationRequest extends WholescriptsOutboundRequest
{
	/** Qualifier of the <code>CustomerMaterialInformationRequest.account</code> attribute **/
	public static final String ACCOUNT = "account";
	/** Qualifier of the <code>CustomerMaterialInformationRequest.salesOrg</code> attribute **/
	public static final String SALESORG = "salesOrg";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(WholescriptsOutboundRequest.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(ACCOUNT, AttributeMode.INITIAL);
		tmp.put(SALESORG, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformationRequest.account</code> attribute.
	 * @return the account
	 */
	public String getAccount(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "account".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformationRequest.account</code> attribute.
	 * @return the account
	 */
	public String getAccount()
	{
		return getAccount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformationRequest.account</code> attribute. 
	 * @param value the account
	 */
	public void setAccount(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "account".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformationRequest.account</code> attribute. 
	 * @param value the account
	 */
	public void setAccount(final String value)
	{
		setAccount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformationRequest.salesOrg</code> attribute.
	 * @return the salesOrg
	 */
	public SAPSalesOrganization getSalesOrg(final SessionContext ctx)
	{
		return (SAPSalesOrganization)getProperty( ctx, "salesOrg".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformationRequest.salesOrg</code> attribute.
	 * @return the salesOrg
	 */
	public SAPSalesOrganization getSalesOrg()
	{
		return getSalesOrg( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformationRequest.salesOrg</code> attribute. 
	 * @param value the salesOrg
	 */
	public void setSalesOrg(final SessionContext ctx, final SAPSalesOrganization value)
	{
		setProperty(ctx, "salesOrg".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformationRequest.salesOrg</code> attribute. 
	 * @param value the salesOrg
	 */
	public void setSalesOrg(final SAPSalesOrganization value)
	{
		setSalesOrg( getSession().getSessionContext(), value );
	}
	
}
