<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.paymetric.sdk.AccessTokenUtility"%>
<%@page import="com.paymetric.sdk.models.AccessTokenResponse.AccessTokenResponsePacket"%>
<%@page import="com.paymetric.sdk.models.MerchantHtmlPacket.IFrameMerchantHtmlPacket"%>
<%@page import="com.paymetric.sdk.models.MerchantHtmlPacket.Cardinal3DSAdditionalField"%>
<%@page import="com.paymetric.sdk.XIConfig"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>


<%
try
{
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Forward-Declare global variables
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	String strLocation = null;
	String strStore = null;
	XIConfig xiConfig = null;
	IFrameMerchantHtmlPacket packetRQ = null;
	AccessTokenResponsePacket packetRS = null;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Load the XiIntercept configuration file
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	xiConfig = new XIConfig();
	xiConfig.Config(request, "XiIntercept.properties");

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Initialize the IFRAME Template or Custom XML to provide to XiIntercept
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	String strSite = xiConfig.getSiteURL(request);
	packetRQ = xiConfig.getIFramePacket(request);

	if(XIConfig.is3DS.get())
	{
		String strRS = xiConfig.getResponse(request);
		String strOrderNo = request.getParameter("OrderNo");
		BigDecimal dAmount = new BigDecimal(request.getParameter("Amount"));
		Integer iCurrency = new Integer(request.getParameter("Currency"));
				
		List<Cardinal3DSAdditionalField> additionalFields = new ArrayList<Cardinal3DSAdditionalField> ();
   
     	Cardinal3DSAdditionalField field = new Cardinal3DSAdditionalField();
        field.setName("BillingState");
        field.setValue("");
        additionalFields.add(field);
        
        Cardinal3DSAdditionalField field2  = new Cardinal3DSAdditionalField();
        field2.setName("ShippingState");
        field2.setValue("");
        additionalFields.add(field2);
	
		if(strRS.length() == 0)
			packetRQ.SetCardinal3DSProperties(dAmount, iCurrency, strSite + "XiInterceptIFrameRS.jsp", strOrderNo,"2.2.0",additionalFields);
		else 
			packetRQ.SetCardinal3DSProperties(dAmount, iCurrency, strSite + "XiInterceptIFrameRS.jsp?RS=" + strRS, strOrderNo,"2.2.0", additionalFields);
	}

	packetRS = AccessTokenUtility.GetIFrameAccessToken(XIConfig.CC_GUID.get(), XIConfig.CC_PSK.get(), XIConfig.URL.get(), packetRQ);
	if (!(packetRS.getRequestError() + "").equals("null")) 
	{
		String strError = packetRS.getRequestError().getStatusCode() + "," + packetRS.getRequestError().getMessage();
	    request.setAttribute("errormessage", strError);
	    request.setAttribute("errorstyle", "class='errorMsg'");
	} 
	else
	{
	   // out.println("######################################"+XIConfig.URL.get());
	    String accessToken = packetRS.getResponsePacket().getAccessToken();
	    request.setAttribute("XIeCommUrl", XIConfig.URL.get());
	    request.setAttribute("AutoSizeHeight", XIConfig.AutoSizeHeight.get());
	    request.setAttribute("AutoSizeWidth", XIConfig.AutoSizeWidth.get());
	    request.setAttribute("accessToken", accessToken);
	    request.setAttribute("signature", packetRS.getResponsePacket().getSignature());
	    request.setAttribute("iFrameUrl", XIConfig.URL.get() + "/view/iframe/" + XIConfig.CC_GUID.get() + "/" + accessToken + "/true");
	    request.setAttribute("site", strSite);
	    request.setAttribute("secureresponse", strSite + "XiInterceptIFrameRS.jsp");
	}
}
catch(Exception ex)
{
    request.setAttribute("errormessage", ex.getMessage());
    request.setAttribute("errorstyle", "class='errorMsg'");
}

%>

<!DOCTYPE>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="cache-control" content="max-age=0" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta charset="ISO-8859-1" />
		
		<title>XiIntercept v3 - HOSTED-IFRAME Request</title>
		
	    <script src="${XIeCommUrl}/scripts/XIFrame/XIFrame-1.2.0.js" type="text/javascript"></script>
	    <script src="${XIeCommUrl}/scripts/XIPlugin/XIPlugin-1.2.0.js" type="text/javascript"></script>
	    <script type="text/javascript" src="/paymetric/XiIntercept3/Alipay/jquery-3.4.1.min.js"></script>
	    <script type="text/javascript" src="/paymetric/XiIntercept3/3DS2/paymetric-3ds2.js"></script>
	    
	    <script type="text/javascript">
	    	var strError = "${errormessage}";
	    	
	    	if(strError.length != 0)
	    	{
            	if(typeof window.parent.DisplayMessage === "function")
            	{
                	window.parent.DisplayMessage("XiInterceptIFrameRQ","ERROR", strError);
            	}
	    	}
	    	
	        function submitform()
	        {

		        var jsonData=window.parent.get3DSDataFromHybris();

		        //var obj = JSON.parse(jsonData);		        
		      
	            var iframe = document.getElementsByName('xieCommFrame');

	            var accessToken = document.getElementById('AccessToken');
                var signedToken = document.getElementById('SignedToken');
                var merchantGuid = "${MerchantGUID}";
				
	            var myData = $XIPlugin.createJSRequestPacket(merchantGuid, accessToken.value);

	            myData.addAdditional3DSField("Amount", "150");
	            myData.addAdditional3DSField("OrderNumber", "TestOrderNum");
	            myData.addAdditional3DSField("CurrencyCode", "840");
	            myData.addAdditional3DSField("Email", "sanket@fisglobal.com");
	            myData.addAdditional3DSField("MobilePhone", "4692697713");
	            myData.addAdditional3DSField("BillingFirstName", "Sanket"); 
				myData.addAdditional3DSField("BillingLastName", "Kulkarni");
	            myData.addAdditional3DSField("BillingAddress1", "9889 Cypresswood Dr");
	            myData.addAdditional3DSField("BillingCity", "Houston");
	           // conditional if state is present.
	         //   myData.addAdditional3DSField("BillingState", "TX");
	            myData.addAdditional3DSField("BillingPostalCode", "75206");
	            myData.addAdditional3DSField("BillingCountryCode", "840"); 
				myData.addAdditional3DSField("ShippingAddress1", "9889 Cypresswood Dr");
	            myData.addAdditional3DSField("ShippingCity", "Houston");
	         // conditional if state is present.
	           // myData.addAdditional3DSField("ShippingState", "TX");
	            myData.addAdditional3DSField("ShippingPostalCode", "75206");
	            myData.addAdditional3DSField("ShippingCountryCode", "840");
	            myData.addAdditional3DSField("BillingPhone", "4692697713"); 
	            
	            if (iframe) 
	            {
	                $XIFrame.submit({
	                	iFrameId: 'xieCommFrame',
	                    targetUrl: iframe[0].getAttribute("src"),
	                    data: myData,
	                    onSuccess: function (msg)
	                    {
	                    	var message = JSON.parse(msg);
	                        if (message && message.data.HasPassed)
	                        {
	                            var accessToken = document.getElementById('AccessToken');
	                            var signedToken = document.getElementById('SignedToken');

	                            alert('Access Token :'+accessToken);
	                            
	                            window.location = "${secureresponse}?id=" + accessToken.value + "&s=" + signedToken.value;
	                        }
	                        else
	                        {
	                        	if(typeof window.parent.DisplayMessage === "function")
	                        	{
		                        	window.parent.DisplayMessage("XiInterceptIFrameRQ","ERROR",message.data.Message);
	                        	}
	                        }
	                    },
	                    onError: function (msg)
	                    {
                        	if(typeof window.parent.DisplayMessage === "function")
                        	{
    	                    	window.parent.DisplayMessage("XiInterceptIFrameRQ","ERROR",msg);
                        	}
	                    },
	                    
	                   // intcp3DSecure: render3dsPopup,
	                    threeDSVersion: "2.2.0"
	                    
	                });
	            }
	        }

	        function IFrame_OnLoad()
	        {
                var xiTokenize = document.getElementById('xiTokenize');
                var xiStatus = document.getElementById('Status');
                if (xiTokenize)
                {
                	if(typeof window.parent.InitForTokenization == 'undefined')
                	{
                		xiTokenize.style.display = 'block';
                		xiStatus.style.display = 'block';
                	}
                	else
                	{
                		xiTokenize.style.display = 'none';
                		xiStatus.style.display = 'none';
                	}
		        }
                
                var iframe = document.getElementsByName('xieCommFrame');
                if (iframe)
                {
                    $XIFrame.onload({
                        iFrameId: 'xieCommFrame',
                        targetUrl: iframe[0].getAttribute("src"),
                        autosizeheight: true,
                        autosizewidth: true,
                        onSuccess: function (msg) 
                        {
                        	if(typeof window.parent.DisplayMessage === "undefined")
                        	{
                        		xiStatus.innerText = "A form for the merchant guid and access token combination is loading in the iFrame successfully.";
                        	}
                        }
                    });
                }
	        }
	    </script>
	</head>
	<body>
	    <div class="sampleBody">
	        <div class="payment-content">
                <div class="billing-info">
                    <div id="iframewrapper">
                        <iframe id="xieCommFrame" name="xieCommFrame" style="border:none;" src="${iFrameUrl}" onload="IFrame_OnLoad();return false;"></iframe>
                    </div>
					<input id="xiTokenize" type="button" value="Tokenize..." onclick="submitform(); return false;" style="display: none" />
	            	<input type="hidden" id="AccessToken" value="${accessToken}" />
	            	<input type="hidden" id="SignedToken" value="${signature}" />
	        	</div>
	        </div>
	        <div id="Status" ${errorstyle}>${errormessage}</div>
	    </div>
	</body>
</html>