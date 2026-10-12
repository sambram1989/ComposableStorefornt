/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-May-2025, 5:34:01 pm                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.paymetric.jalo;

import de.hybris.platform.cronjob.jalo.PaymetricScheduleBatchCronJob;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.payment.jalo.PaymentTransactionEntry;
import de.hybris.platform.paymetric.constants.PaymetricConstants;
import de.hybris.platform.util.PartOfHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>PaymetricManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedPaymetricManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("TR_TRANS_MSGTYPE", AttributeMode.INITIAL);
		tmp.put("IN_TRANS_VISATRANSID", AttributeMode.INITIAL);
		tmp.put("IN_TRANS_VISATRANSID2", AttributeMode.INITIAL);
		tmp.put("IN_TRANS_BANKNETDATE", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.payment.PaymentInfo", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("TR_FRAUD_RESULTSTATUS", AttributeMode.INITIAL);
		tmp.put("TR_FRAUD_SCORE", AttributeMode.INITIAL);
		tmp.put("DUPLICATE_ORDER", AttributeMode.INITIAL);
		tmp.put("XiPayResponseCode", AttributeMode.INITIAL);
		tmp.put("AUTRA", AttributeMode.INITIAL);
		tmp.put("AUTWR", AttributeMode.INITIAL);
		tmp.put("AUNUM", AttributeMode.INITIAL);
		tmp.put("REACT", AttributeMode.INITIAL);
		tmp.put("RCAVR", AttributeMode.INITIAL);
		tmp.put("RCAVA", AttributeMode.INITIAL);
		tmp.put("RCAVZ", AttributeMode.INITIAL);
		tmp.put("RCRSP", AttributeMode.INITIAL);
		tmp.put("RCCVV", AttributeMode.INITIAL);
		tmp.put("RTEXT", AttributeMode.INITIAL);
		tmp.put("MERCH", AttributeMode.INITIAL);
		tmp.put("CCINS", AttributeMode.INITIAL);
		tmp.put("CCWAE", AttributeMode.INITIAL);
		tmp.put("TR_ECOMM_3DSVERSION", AttributeMode.INITIAL);
		tmp.put("TR_ECOMM_IND", AttributeMode.INITIAL);
		tmp.put("TR_ECOMM_PARESSTATUS", AttributeMode.INITIAL);
		tmp.put("TR_ECOMM_VBVCAVV", AttributeMode.INITIAL);
		tmp.put("TR_ECOMM_VBVXID", AttributeMode.INITIAL);
		tmp.put("TR_ECOMM_XID", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.payment.jalo.PaymentTransactionEntry", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.AUNUM</code> attribute.
	 * @return the AUNUM - AUNUM - Authorization code
	 */
	public String getAUNUM(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.AUNUM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.AUNUM</code> attribute.
	 * @return the AUNUM - AUNUM - Authorization code
	 */
	public String getAUNUM(final PaymentTransactionEntry item)
	{
		return getAUNUM( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.AUNUM</code> attribute. 
	 * @param value the AUNUM - AUNUM - Authorization code
	 */
	public void setAUNUM(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.AUNUM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.AUNUM</code> attribute. 
	 * @param value the AUNUM - AUNUM - Authorization code
	 */
	public void setAUNUM(final PaymentTransactionEntry item, final String value)
	{
		setAUNUM( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.AUTRA</code> attribute.
	 * @return the AUTRA - AUTRA - Authorization reference number, XiPay transaction id
	 */
	public String getAUTRA(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.AUTRA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.AUTRA</code> attribute.
	 * @return the AUTRA - AUTRA - Authorization reference number, XiPay transaction id
	 */
	public String getAUTRA(final PaymentTransactionEntry item)
	{
		return getAUTRA( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.AUTRA</code> attribute. 
	 * @param value the AUTRA - AUTRA - Authorization reference number, XiPay transaction id
	 */
	public void setAUTRA(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.AUTRA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.AUTRA</code> attribute. 
	 * @param value the AUTRA - AUTRA - Authorization reference number, XiPay transaction id
	 */
	public void setAUTRA(final PaymentTransactionEntry item, final String value)
	{
		setAUTRA( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.AUTWR</code> attribute.
	 * @return the AUTWR - AUTWR - Authorized amount
	 */
	public String getAUTWR(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.AUTWR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.AUTWR</code> attribute.
	 * @return the AUTWR - AUTWR - Authorized amount
	 */
	public String getAUTWR(final PaymentTransactionEntry item)
	{
		return getAUTWR( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.AUTWR</code> attribute. 
	 * @param value the AUTWR - AUTWR - Authorized amount
	 */
	public void setAUTWR(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.AUTWR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.AUTWR</code> attribute. 
	 * @param value the AUTWR - AUTWR - Authorized amount
	 */
	public void setAUTWR(final PaymentTransactionEntry item, final String value)
	{
		setAUTWR( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.CCINS</code> attribute.
	 * @return the CCINS - CCINS - Authorization card type
	 */
	public String getCCINS(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.CCINS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.CCINS</code> attribute.
	 * @return the CCINS - CCINS - Authorization card type
	 */
	public String getCCINS(final PaymentTransactionEntry item)
	{
		return getCCINS( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.CCINS</code> attribute. 
	 * @param value the CCINS - CCINS - Authorization card type
	 */
	public void setCCINS(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.CCINS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.CCINS</code> attribute. 
	 * @param value the CCINS - CCINS - Authorization card type
	 */
	public void setCCINS(final PaymentTransactionEntry item, final String value)
	{
		setCCINS( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.CCWAE</code> attribute.
	 * @return the CCWAE - CCWAE - Authorization currency code
	 */
	public String getCCWAE(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.CCWAE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.CCWAE</code> attribute.
	 * @return the CCWAE - CCWAE - Authorization currency code
	 */
	public String getCCWAE(final PaymentTransactionEntry item)
	{
		return getCCWAE( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.CCWAE</code> attribute. 
	 * @param value the CCWAE - CCWAE - Authorization currency code
	 */
	public void setCCWAE(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.CCWAE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.CCWAE</code> attribute. 
	 * @param value the CCWAE - CCWAE - Authorization currency code
	 */
	public void setCCWAE(final PaymentTransactionEntry item, final String value)
	{
		setCCWAE( getSession().getSessionContext(), item, value );
	}
	
	public PaymetricScheduleBatchCronJob createPaymetricScheduleBatchCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( PaymetricConstants.TC.PAYMETRICSCHEDULEBATCHCRONJOB );
			return (PaymetricScheduleBatchCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating PaymetricScheduleBatchCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public PaymetricScheduleBatchCronJob createPaymetricScheduleBatchCronJob(final Map attributeValues)
	{
		return createPaymetricScheduleBatchCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute.
	 * @return the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public Boolean isDUPLICATE_ORDER(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (Boolean)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.DUPLICATE_ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute.
	 * @return the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public Boolean isDUPLICATE_ORDER(final PaymentTransactionEntry item)
	{
		return isDUPLICATE_ORDER( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute. 
	 * @return the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public boolean isDUPLICATE_ORDERAsPrimitive(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		Boolean value = isDUPLICATE_ORDER( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute. 
	 * @return the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public boolean isDUPLICATE_ORDERAsPrimitive(final PaymentTransactionEntry item)
	{
		return isDUPLICATE_ORDERAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute. 
	 * @param value the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public void setDUPLICATE_ORDER(final SessionContext ctx, final PaymentTransactionEntry item, final Boolean value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.DUPLICATE_ORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute. 
	 * @param value the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public void setDUPLICATE_ORDER(final PaymentTransactionEntry item, final Boolean value)
	{
		setDUPLICATE_ORDER( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute. 
	 * @param value the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public void setDUPLICATE_ORDER(final SessionContext ctx, final PaymentTransactionEntry item, final boolean value)
	{
		setDUPLICATE_ORDER( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.DUPLICATE_ORDER</code> attribute. 
	 * @param value the DUPLICATE_ORDER - Identifier for possible duplicate orders
	 */
	public void setDUPLICATE_ORDER(final PaymentTransactionEntry item, final boolean value)
	{
		setDUPLICATE_ORDER( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return PaymetricConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.IN_TRANS_BANKNETDATE</code> attribute.
	 * @return the IN_TRANS_BANKNETDATE - For MIT MC transactions, First Data requires that MasterCard MITs also supply the BankNET Date.
	 */
	public String getIN_TRANS_BANKNETDATE(final SessionContext ctx, final PaymentInfo item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentInfo.IN_TRANS_BANKNETDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.IN_TRANS_BANKNETDATE</code> attribute.
	 * @return the IN_TRANS_BANKNETDATE - For MIT MC transactions, First Data requires that MasterCard MITs also supply the BankNET Date.
	 */
	public String getIN_TRANS_BANKNETDATE(final PaymentInfo item)
	{
		return getIN_TRANS_BANKNETDATE( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.IN_TRANS_BANKNETDATE</code> attribute. 
	 * @param value the IN_TRANS_BANKNETDATE - For MIT MC transactions, First Data requires that MasterCard MITs also supply the BankNET Date.
	 */
	public void setIN_TRANS_BANKNETDATE(final SessionContext ctx, final PaymentInfo item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentInfo.IN_TRANS_BANKNETDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.IN_TRANS_BANKNETDATE</code> attribute. 
	 * @param value the IN_TRANS_BANKNETDATE - For MIT MC transactions, First Data requires that MasterCard MITs also supply the BankNET Date.
	 */
	public void setIN_TRANS_BANKNETDATE(final PaymentInfo item, final String value)
	{
		setIN_TRANS_BANKNETDATE( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.IN_TRANS_VISATRANSID</code> attribute.
	 * @return the IN_TRANS_VISATRANSID - Network ID 1 is the reference information for the CIT-MIT transactions.
	 */
	public String getIN_TRANS_VISATRANSID(final SessionContext ctx, final PaymentInfo item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentInfo.IN_TRANS_VISATRANSID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.IN_TRANS_VISATRANSID</code> attribute.
	 * @return the IN_TRANS_VISATRANSID - Network ID 1 is the reference information for the CIT-MIT transactions.
	 */
	public String getIN_TRANS_VISATRANSID(final PaymentInfo item)
	{
		return getIN_TRANS_VISATRANSID( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.IN_TRANS_VISATRANSID</code> attribute. 
	 * @param value the IN_TRANS_VISATRANSID - Network ID 1 is the reference information for the CIT-MIT transactions.
	 */
	public void setIN_TRANS_VISATRANSID(final SessionContext ctx, final PaymentInfo item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentInfo.IN_TRANS_VISATRANSID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.IN_TRANS_VISATRANSID</code> attribute. 
	 * @param value the IN_TRANS_VISATRANSID - Network ID 1 is the reference information for the CIT-MIT transactions.
	 */
	public void setIN_TRANS_VISATRANSID(final PaymentInfo item, final String value)
	{
		setIN_TRANS_VISATRANSID( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.IN_TRANS_VISATRANSID2</code> attribute.
	 * @return the IN_TRANS_VISATRANSID2 - Network ID 2 is the reference information for the CIT-MIT transactions.
	 */
	public String getIN_TRANS_VISATRANSID2(final SessionContext ctx, final PaymentInfo item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentInfo.IN_TRANS_VISATRANSID2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.IN_TRANS_VISATRANSID2</code> attribute.
	 * @return the IN_TRANS_VISATRANSID2 - Network ID 2 is the reference information for the CIT-MIT transactions.
	 */
	public String getIN_TRANS_VISATRANSID2(final PaymentInfo item)
	{
		return getIN_TRANS_VISATRANSID2( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.IN_TRANS_VISATRANSID2</code> attribute. 
	 * @param value the IN_TRANS_VISATRANSID2 - Network ID 2 is the reference information for the CIT-MIT transactions.
	 */
	public void setIN_TRANS_VISATRANSID2(final SessionContext ctx, final PaymentInfo item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentInfo.IN_TRANS_VISATRANSID2,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.IN_TRANS_VISATRANSID2</code> attribute. 
	 * @param value the IN_TRANS_VISATRANSID2 - Network ID 2 is the reference information for the CIT-MIT transactions.
	 */
	public void setIN_TRANS_VISATRANSID2(final PaymentInfo item, final String value)
	{
		setIN_TRANS_VISATRANSID2( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.MERCH</code> attribute.
	 * @return the MERCH - MERCH - XiPay routing merchant id
	 */
	public String getMERCH(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.MERCH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.MERCH</code> attribute.
	 * @return the MERCH - MERCH - XiPay routing merchant id
	 */
	public String getMERCH(final PaymentTransactionEntry item)
	{
		return getMERCH( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.MERCH</code> attribute. 
	 * @param value the MERCH - MERCH - XiPay routing merchant id
	 */
	public void setMERCH(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.MERCH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.MERCH</code> attribute. 
	 * @param value the MERCH - MERCH - XiPay routing merchant id
	 */
	public void setMERCH(final PaymentTransactionEntry item, final String value)
	{
		setMERCH( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCAVA</code> attribute.
	 * @return the RCAVA - RCAVA - AVS address response code
	 */
	public String getRCAVA(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCAVA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCAVA</code> attribute.
	 * @return the RCAVA - RCAVA - AVS address response code
	 */
	public String getRCAVA(final PaymentTransactionEntry item)
	{
		return getRCAVA( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCAVA</code> attribute. 
	 * @param value the RCAVA - RCAVA - AVS address response code
	 */
	public void setRCAVA(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCAVA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCAVA</code> attribute. 
	 * @param value the RCAVA - RCAVA - AVS address response code
	 */
	public void setRCAVA(final PaymentTransactionEntry item, final String value)
	{
		setRCAVA( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCAVR</code> attribute.
	 * @return the RCAVR - RCAVR - AVS check response code
	 */
	public String getRCAVR(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCAVR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCAVR</code> attribute.
	 * @return the RCAVR - RCAVR - AVS check response code
	 */
	public String getRCAVR(final PaymentTransactionEntry item)
	{
		return getRCAVR( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCAVR</code> attribute. 
	 * @param value the RCAVR - RCAVR - AVS check response code
	 */
	public void setRCAVR(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCAVR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCAVR</code> attribute. 
	 * @param value the RCAVR - RCAVR - AVS check response code
	 */
	public void setRCAVR(final PaymentTransactionEntry item, final String value)
	{
		setRCAVR( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCAVZ</code> attribute.
	 * @return the RCAVZ - RCAVZ - AVS Zip code response code
	 */
	public String getRCAVZ(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCAVZ);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCAVZ</code> attribute.
	 * @return the RCAVZ - RCAVZ - AVS Zip code response code
	 */
	public String getRCAVZ(final PaymentTransactionEntry item)
	{
		return getRCAVZ( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCAVZ</code> attribute. 
	 * @param value the RCAVZ - RCAVZ - AVS Zip code response code
	 */
	public void setRCAVZ(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCAVZ,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCAVZ</code> attribute. 
	 * @param value the RCAVZ - RCAVZ - AVS Zip code response code
	 */
	public void setRCAVZ(final PaymentTransactionEntry item, final String value)
	{
		setRCAVZ( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCCVV</code> attribute.
	 * @return the RCCVV - RCCVV - CVV response code
	 */
	public String getRCCVV(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCCVV);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCCVV</code> attribute.
	 * @return the RCCVV - RCCVV - CVV response code
	 */
	public String getRCCVV(final PaymentTransactionEntry item)
	{
		return getRCCVV( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCCVV</code> attribute. 
	 * @param value the RCCVV - RCCVV - CVV response code
	 */
	public void setRCCVV(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCCVV,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCCVV</code> attribute. 
	 * @param value the RCCVV - RCCVV - CVV response code
	 */
	public void setRCCVV(final PaymentTransactionEntry item, final String value)
	{
		setRCCVV( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCRSP</code> attribute.
	 * @return the RCRSP - RCRSP - Authorization response code
	 */
	public String getRCRSP(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCRSP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RCRSP</code> attribute.
	 * @return the RCRSP - RCRSP - Authorization response code
	 */
	public String getRCRSP(final PaymentTransactionEntry item)
	{
		return getRCRSP( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCRSP</code> attribute. 
	 * @param value the RCRSP - RCRSP - Authorization response code
	 */
	public void setRCRSP(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RCRSP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RCRSP</code> attribute. 
	 * @param value the RCRSP - RCRSP - Authorization response code
	 */
	public void setRCRSP(final PaymentTransactionEntry item, final String value)
	{
		setRCRSP( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.REACT</code> attribute.
	 * @return the REACT - REACT - Authorization status code: A=authorized, B=communication error, C=declined
	 */
	public String getREACT(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.REACT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.REACT</code> attribute.
	 * @return the REACT - REACT - Authorization status code: A=authorized, B=communication error, C=declined
	 */
	public String getREACT(final PaymentTransactionEntry item)
	{
		return getREACT( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.REACT</code> attribute. 
	 * @param value the REACT - REACT - Authorization status code: A=authorized, B=communication error, C=declined
	 */
	public void setREACT(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.REACT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.REACT</code> attribute. 
	 * @param value the REACT - REACT - Authorization status code: A=authorized, B=communication error, C=declined
	 */
	public void setREACT(final PaymentTransactionEntry item, final String value)
	{
		setREACT( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RTEXT</code> attribute.
	 * @return the RTEXT - RTEXT - Authorization response message
	 */
	public String getRTEXT(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.RTEXT</code> attribute.
	 * @return the RTEXT - RTEXT - Authorization response message
	 */
	public String getRTEXT(final PaymentTransactionEntry item)
	{
		return getRTEXT( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RTEXT</code> attribute. 
	 * @param value the RTEXT - RTEXT - Authorization response message
	 */
	public void setRTEXT(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.RTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.RTEXT</code> attribute. 
	 * @param value the RTEXT - RTEXT - Authorization response message
	 */
	public void setRTEXT(final PaymentTransactionEntry item, final String value)
	{
		setRTEXT( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_3DSVERSION</code> attribute.
	 * @return the TR_ECOMM_3DSVERSION - The 3DS version used during Payer Authentication
	 */
	public String getTR_ECOMM_3DSVERSION(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_3DSVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_3DSVERSION</code> attribute.
	 * @return the TR_ECOMM_3DSVERSION - The 3DS version used during Payer Authentication
	 */
	public String getTR_ECOMM_3DSVERSION(final PaymentTransactionEntry item)
	{
		return getTR_ECOMM_3DSVERSION( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_3DSVERSION</code> attribute. 
	 * @param value the TR_ECOMM_3DSVERSION - The 3DS version used during Payer Authentication
	 */
	public void setTR_ECOMM_3DSVERSION(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_3DSVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_3DSVERSION</code> attribute. 
	 * @param value the TR_ECOMM_3DSVERSION - The 3DS version used during Payer Authentication
	 */
	public void setTR_ECOMM_3DSVERSION(final PaymentTransactionEntry item, final String value)
	{
		setTR_ECOMM_3DSVERSION( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_IND</code> attribute.
	 * @return the TR_ECOMM_IND - The raw ECI data from payer authentication, if you received any for the transaction.
	 */
	public String getTR_ECOMM_IND(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_IND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_IND</code> attribute.
	 * @return the TR_ECOMM_IND - The raw ECI data from payer authentication, if you received any for the transaction.
	 */
	public String getTR_ECOMM_IND(final PaymentTransactionEntry item)
	{
		return getTR_ECOMM_IND( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_IND</code> attribute. 
	 * @param value the TR_ECOMM_IND - The raw ECI data from payer authentication, if you received any for the transaction.
	 */
	public void setTR_ECOMM_IND(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_IND,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_IND</code> attribute. 
	 * @param value the TR_ECOMM_IND - The raw ECI data from payer authentication, if you received any for the transaction.
	 */
	public void setTR_ECOMM_IND(final PaymentTransactionEntry item, final String value)
	{
		setTR_ECOMM_IND( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_PARESSTATUS</code> attribute.
	 * @return the TR_ECOMM_PARESSTATUS - Payer authentication response status received in response to the consumer's 3D secure authentication attempt.
	 */
	public String getTR_ECOMM_PARESSTATUS(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_PARESSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_PARESSTATUS</code> attribute.
	 * @return the TR_ECOMM_PARESSTATUS - Payer authentication response status received in response to the consumer's 3D secure authentication attempt.
	 */
	public String getTR_ECOMM_PARESSTATUS(final PaymentTransactionEntry item)
	{
		return getTR_ECOMM_PARESSTATUS( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_PARESSTATUS</code> attribute. 
	 * @param value the TR_ECOMM_PARESSTATUS - Payer authentication response status received in response to the consumer's 3D secure authentication attempt.
	 */
	public void setTR_ECOMM_PARESSTATUS(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_PARESSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_PARESSTATUS</code> attribute. 
	 * @param value the TR_ECOMM_PARESSTATUS - Payer authentication response status received in response to the consumer's 3D secure authentication attempt.
	 */
	public void setTR_ECOMM_PARESSTATUS(final PaymentTransactionEntry item, final String value)
	{
		setTR_ECOMM_PARESSTATUS( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVCAVV</code> attribute.
	 * @return the TR_ECOMM_VBVCAVV - Cardholder authentication verification value (CAVV).
	 */
	public String getTR_ECOMM_VBVCAVV(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_VBVCAVV);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVCAVV</code> attribute.
	 * @return the TR_ECOMM_VBVCAVV - Cardholder authentication verification value (CAVV).
	 */
	public String getTR_ECOMM_VBVCAVV(final PaymentTransactionEntry item)
	{
		return getTR_ECOMM_VBVCAVV( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVCAVV</code> attribute. 
	 * @param value the TR_ECOMM_VBVCAVV - Cardholder authentication verification value (CAVV).
	 */
	public void setTR_ECOMM_VBVCAVV(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_VBVCAVV,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVCAVV</code> attribute. 
	 * @param value the TR_ECOMM_VBVCAVV - Cardholder authentication verification value (CAVV).
	 */
	public void setTR_ECOMM_VBVCAVV(final PaymentTransactionEntry item, final String value)
	{
		setTR_ECOMM_VBVCAVV( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVXID</code> attribute.
	 * @return the TR_ECOMM_VBVXID - Defines the Verified by Visa Transaction Id.
	 */
	public String getTR_ECOMM_VBVXID(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_VBVXID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVXID</code> attribute.
	 * @return the TR_ECOMM_VBVXID - Defines the Verified by Visa Transaction Id.
	 */
	public String getTR_ECOMM_VBVXID(final PaymentTransactionEntry item)
	{
		return getTR_ECOMM_VBVXID( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVXID</code> attribute. 
	 * @param value the TR_ECOMM_VBVXID - Defines the Verified by Visa Transaction Id.
	 */
	public void setTR_ECOMM_VBVXID(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_VBVXID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_VBVXID</code> attribute. 
	 * @param value the TR_ECOMM_VBVXID - Defines the Verified by Visa Transaction Id.
	 */
	public void setTR_ECOMM_VBVXID(final PaymentTransactionEntry item, final String value)
	{
		setTR_ECOMM_VBVXID( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_XID</code> attribute.
	 * @return the TR_ECOMM_XID - Transaction Id for 3DS2
	 */
	public String getTR_ECOMM_XID(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_XID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_ECOMM_XID</code> attribute.
	 * @return the TR_ECOMM_XID - Transaction Id for 3DS2
	 */
	public String getTR_ECOMM_XID(final PaymentTransactionEntry item)
	{
		return getTR_ECOMM_XID( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_XID</code> attribute. 
	 * @param value the TR_ECOMM_XID - Transaction Id for 3DS2
	 */
	public void setTR_ECOMM_XID(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_ECOMM_XID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_ECOMM_XID</code> attribute. 
	 * @param value the TR_ECOMM_XID - Transaction Id for 3DS2
	 */
	public void setTR_ECOMM_XID(final PaymentTransactionEntry item, final String value)
	{
		setTR_ECOMM_XID( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_FRAUD_RESULTSTATUS</code> attribute.
	 * @return the TR_FRAUD_RESULTSTATUS - This field contains the overall results from the fraud call: (0=Approved, 1=Review, 2=Reject, -1=Fraud Service Unavailable, -2=Fraud Service was not able to perform its fraud check)
	 */
	public String getTR_FRAUD_RESULTSTATUS(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_FRAUD_RESULTSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_FRAUD_RESULTSTATUS</code> attribute.
	 * @return the TR_FRAUD_RESULTSTATUS - This field contains the overall results from the fraud call: (0=Approved, 1=Review, 2=Reject, -1=Fraud Service Unavailable, -2=Fraud Service was not able to perform its fraud check)
	 */
	public String getTR_FRAUD_RESULTSTATUS(final PaymentTransactionEntry item)
	{
		return getTR_FRAUD_RESULTSTATUS( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_FRAUD_RESULTSTATUS</code> attribute. 
	 * @param value the TR_FRAUD_RESULTSTATUS - This field contains the overall results from the fraud call: (0=Approved, 1=Review, 2=Reject, -1=Fraud Service Unavailable, -2=Fraud Service was not able to perform its fraud check)
	 */
	public void setTR_FRAUD_RESULTSTATUS(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_FRAUD_RESULTSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_FRAUD_RESULTSTATUS</code> attribute. 
	 * @param value the TR_FRAUD_RESULTSTATUS - This field contains the overall results from the fraud call: (0=Approved, 1=Review, 2=Reject, -1=Fraud Service Unavailable, -2=Fraud Service was not able to perform its fraud check)
	 */
	public void setTR_FRAUD_RESULTSTATUS(final PaymentTransactionEntry item, final String value)
	{
		setTR_FRAUD_RESULTSTATUS( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_FRAUD_SCORE</code> attribute.
	 * @return the TR_FRAUD_SCORE - The total transaction fraud score.
	 */
	public String getTR_FRAUD_SCORE(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_FRAUD_SCORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.TR_FRAUD_SCORE</code> attribute.
	 * @return the TR_FRAUD_SCORE - The total transaction fraud score.
	 */
	public String getTR_FRAUD_SCORE(final PaymentTransactionEntry item)
	{
		return getTR_FRAUD_SCORE( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_FRAUD_SCORE</code> attribute. 
	 * @param value the TR_FRAUD_SCORE - The total transaction fraud score.
	 */
	public void setTR_FRAUD_SCORE(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.TR_FRAUD_SCORE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.TR_FRAUD_SCORE</code> attribute. 
	 * @param value the TR_FRAUD_SCORE - The total transaction fraud score.
	 */
	public void setTR_FRAUD_SCORE(final PaymentTransactionEntry item, final String value)
	{
		setTR_FRAUD_SCORE( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.TR_TRANS_MSGTYPE</code> attribute.
	 * @return the TR_TRANS_MSGTYPE
	 */
	public String getTR_TRANS_MSGTYPE(final SessionContext ctx, final PaymentInfo item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentInfo.TR_TRANS_MSGTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfo.TR_TRANS_MSGTYPE</code> attribute.
	 * @return the TR_TRANS_MSGTYPE
	 */
	public String getTR_TRANS_MSGTYPE(final PaymentInfo item)
	{
		return getTR_TRANS_MSGTYPE( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.TR_TRANS_MSGTYPE</code> attribute. 
	 * @param value the TR_TRANS_MSGTYPE
	 */
	public void setTR_TRANS_MSGTYPE(final SessionContext ctx, final PaymentInfo item, final String value)
	{
		new PartOfHandler<String>()
		{
			@Override
			protected String doGetValue(final SessionContext ctx)
			{
				return getTR_TRANS_MSGTYPE( ctx, item );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final String _value)
			{
				final String value = _value;
				item.setProperty(ctx, PaymetricConstants.Attributes.PaymentInfo.TR_TRANS_MSGTYPE,value);
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentInfo.TR_TRANS_MSGTYPE</code> attribute. 
	 * @param value the TR_TRANS_MSGTYPE
	 */
	public void setTR_TRANS_MSGTYPE(final PaymentInfo item, final String value)
	{
		setTR_TRANS_MSGTYPE( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.XiPayResponseCode</code> attribute.
	 * @return the XiPayResponseCode - This field contains the XiPay gateway response code (status code)
	 */
	public String getXiPayResponseCode(final SessionContext ctx, final PaymentTransactionEntry item)
	{
		return (String)item.getProperty( ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.XIPAYRESPONSECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentTransactionEntry.XiPayResponseCode</code> attribute.
	 * @return the XiPayResponseCode - This field contains the XiPay gateway response code (status code)
	 */
	public String getXiPayResponseCode(final PaymentTransactionEntry item)
	{
		return getXiPayResponseCode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.XiPayResponseCode</code> attribute. 
	 * @param value the XiPayResponseCode - This field contains the XiPay gateway response code (status code)
	 */
	public void setXiPayResponseCode(final SessionContext ctx, final PaymentTransactionEntry item, final String value)
	{
		item.setProperty(ctx, PaymetricConstants.Attributes.PaymentTransactionEntry.XIPAYRESPONSECODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PaymentTransactionEntry.XiPayResponseCode</code> attribute. 
	 * @param value the XiPayResponseCode - This field contains the XiPay gateway response code (status code)
	 */
	public void setXiPayResponseCode(final PaymentTransactionEntry item, final String value)
	{
		setXiPayResponseCode( getSession().getSessionContext(), item, value );
	}
	
}
