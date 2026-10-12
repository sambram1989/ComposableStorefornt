<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- ########################################################################################
	# Houghton
	############################################################################################# -->


	<!-- ########################################################################################
	# This template sets the processor-specific PreAuthorization Amount
	############################################################################################# -->
	<xsl:template name="PreAuthAmount">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
		<xsl:choose>
			<xsl:when test="contains('AX-DI', $cardType)">
				<xsl:value-of select="'1.00'"/>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="'0.00'"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>  

	<!-- ########################################################################################
	# This template sets the processor-specific Expiration Date
	############################################################################################# -->
	<xsl:template name="ExpiryDate">
	    <xsl:param name="xipayMID"/>
		<xsl:param name="expMonth"/>
		<xsl:param name="expYear"/>
		<xsl:choose>
			<xsl:when test="string-length($expMonth) = 1"><xsl:value-of select="concat('0', $expMonth)"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$expMonth"/></xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="number($expYear) - 2000"/>
	</xsl:template> 

	<!-- ########################################################################################
	# This template sets the processor-specific CVV Response Info-Item Name
	############################################################################################# -->
	<xsl:template name="cvvResponseInfoItem">
	 	<xsl:param name="xipayMID"/>
		<xsl:value-of select="'TR_CARD_CIDRESPCODE'"/>
	</xsl:template>

	<!-- ########################################################################################
	# This template adds/sets any processor-specific header data
	############################################################################################# -->
	<xsl:template name="CustomHeader">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:param name="op"/>
	    <xsl:param name="nodes"/>
	    
	    <xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
				
				<PONumber>
						<xsl:value-of select="$nodes/entry[string[1]='ponumber']/string[2]"/>
				</PONumber>
				<salesDocNumber>
						<xsl:value-of select="$nodes/entry[string[1]='salesDocNumber']/string[2]"/>
				</salesDocNumber>

				<xsl:variable name="orderSource">
					<xsl:value-of select="$nodes/entry[string[1]='orderSource']/string[2]" />
				</xsl:variable>
				<cardDataSource>
					<xsl:choose>
						<xsl:when test="$orderSource = 'telephone'"><xsl:value-of select="'M'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'E'" /></xsl:otherwise>
					</xsl:choose>
				</cardDataSource>
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
				

				<xsl:variable name="orderSource">
					<xsl:choose>
						<xsl:when test="$nodes/customFields/orderSource"><xsl:value-of select="$nodes/customFields/orderSource" /></xsl:when>
						<xsl:when test="$nodes/cronJob and $nodes/cronJob = 'true'"><xsl:value-of select="'recurring'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'ecommerce'" /></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<cardDataSource>
					<xsl:choose>
						<xsl:when test="$orderSource = 'ecommerce'"><xsl:value-of select="'E'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'M'" /></xsl:otherwise>
					</xsl:choose>
				</cardDataSource>
				
				<PONumber>
					<xsl:value-of select="$nodes/customFields/ponumber"/>
				</PONumber>
				<salesDocNumber>
					<xsl:value-of select="$nodes/customFields/salesDocNumber"/>
				</salesDocNumber>
			</xsl:when>

			<xsl:when test="$op = '3'"> <!-- Capture -->
			</xsl:when>

			<xsl:when test="$op = '4'"> <!-- Refunds -->
				

				<xsl:variable name="orderSource">
					<xsl:choose>
						<xsl:when test="$nodes/customFields/orderSource"><xsl:value-of select="$nodes/customFields/orderSource" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'ecommerce'" /></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<cardDataSource>
					<xsl:choose>
						<xsl:when test="$orderSource = 'ecommerce'"><xsl:value-of select="'E'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'M'" /></xsl:otherwise>
					</xsl:choose>
				</cardDataSource>
			</xsl:when>

			<xsl:when test="$op = '5'"> <!-- Voids -->
			</xsl:when>

			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ########################################################################################
	# This template adds/sets any processor-specific info-item data
	############################################################################################# -->
	<xsl:template name="CustomInfoItems">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:param name="op"/>
	    <xsl:param name="nodes"/>

		<xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
						
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
				
			</xsl:when>

			<xsl:when test="$op = '3'"> <!-- Capture -->
			</xsl:when>

			<xsl:when test="$op = '4'"> <!-- Refunds -->
							
			</xsl:when>

			<xsl:when test="$op = '5'"> <!-- Voids -->
			</xsl:when>

			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ########################################################################################
	# This template adds/sets any processor-specific Line Item info-items data
	############################################################################################# -->
	<xsl:template name="CustomLineInfoItems">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:param name="op"/>
	    <xsl:param name="nodes"/>

	
		<xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
			</xsl:when>

			<xsl:when test="$op = '3'"> <!-- Capture -->
			</xsl:when>

			<xsl:when test="$op = '4'"> <!-- Refunds -->
			</xsl:when>

			<xsl:when test="$op = '5'"> <!-- Voids -->
			</xsl:when>

			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ########################################################################################
	# This template determines the XiPay Routing MID
	############################################################################################# -->
	<xsl:template name="XiPayMID">
	    <xsl:param name="cardType"/>
	    <xsl:param name="currency"/>
	    <xsl:param name="country"/>
	    <xsl:param name="business"/>
		<xsl:choose>
			<xsl:when test="contains('*', $business) and contains('usd-USD-*', $currency) and contains('VI-MC-AX-*', $cardType)"><xsl:value-of select="'NSLOANVALVE'"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="'NSVCMARKET'"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template> 

	<!-- ########################################################################################
	# This template determines the successful AVS response code by card type
	############################################################################################# -->
	<xsl:template name="SuccessfulAVSCodes">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
		<xsl:choose>
			<xsl:when test="contains('VI-MC-AX-*', $cardType)"><xsl:value-of select="'?;?;?'"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="'*'"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template> 

	<!-- ########################################################################################
	# This template determines the successful CVV response code by card type
	############################################################################################# -->
	<xsl:template name="SuccessfulCVVCodes">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
		<xsl:choose>
			<xsl:when test="contains('VI-MC-AX-*', $cardType)"><xsl:value-of select="'M;P;U;X'"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="'*'"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template> 

	<!-- ########################################################################################
	# This template determines the authorization response code produced by the processor
	############################################################################################# -->
	<xsl:template name="AuthorizationResponseCode">
	    <xsl:param name="packet"/>
			<xsl:value-of select="$packet/ResponseCode"/>
	</xsl:template> 

</xsl:stylesheet>
