<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0">

	<!--
		=====================================================================
		This XSL files contains functions that are reused across all PDF
		generators
		=====================================================================
	-->

	<!-- Write meta data (general layout and PDF meta data) -->
	<xsl:template name="writeMeta">
		<!-- General page configuration -->
		<fo:layout-master-set>
			<fo:simple-page-master master-name="all"
				page-height="8.5in" page-width="11.5in" margin-top="0.5in"
				margin-bottom="0.5in" margin-left="0.75in" margin-right="0.75in">
				<fo:region-body margin-top="0.25in" margin-bottom="0.25in" />
				<fo:region-before extent="0.25in" />
				<fo:region-after extent="0.25in" />
			</fo:simple-page-master>
		</fo:layout-master-set>

		<!-- Add meta data to the PDF -->
		<fo:declarations>
			<x:xmpmeta xmlns:x="adobe:ns:meta/">
				<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
					<rdf:Description rdf:about=""
						xmlns:dc="http://purl.org/dc/elements/1.1/">
						<!-- Dublin Core properties go here -->
						<dc:title>
							<xsl:value-of select="/fop-tournament/info/name" />
						</dc:title>
						<dc:creator>Volleyball Manager</dc:creator>
					</rdf:Description>
					<rdf:Description rdf:about=""
						xmlns:xmp="http://ns.adobe.com/xap/1.0/">
						<!-- XMP properties go here -->
						<xmp:CreatorTool>Volleyball Manager</xmp:CreatorTool>
					</rdf:Description>
				</rdf:RDF>
			</x:xmpmeta>
		</fo:declarations>
	</xsl:template>


	<!-- Write page header and footer -->
	<xsl:template name="writePageHeaderFooter">
		<fo:static-content flow-name="xsl-region-before">
			<fo:block line-height="14pt" font-size="8pt" text-align="end">
				Volleyball Manager -
				<xsl:value-of select="/fop-tournament/info/name" />
			</fo:block>
		</fo:static-content>
		<fo:static-content flow-name="xsl-region-after">
			<fo:block line-height="14pt" font-size="8pt" text-align="end">
				<!-- Write license information for footer -->
				<xsl:param name="license" />
				<xsl:param name="fop-messages" />
				<xsl:value-of
					select="/fop-tournament/fop-messages/message[@key='licence_licencedfor']" />
				<xsl:value-of select="/fop-tournament/info/license/organisation" />
				(
				<xsl:value-of select="/fop-tournament/info/license/city" />
				,
				<xsl:value-of select="/fop-tournament/info/license/country" />
				)
				-
				<xsl:value-of
					select="/fop-tournament/fop-messages/message[@key='pdf_page']" />
				<fo:page-number />
			</fo:block>
		</fo:static-content>
	</xsl:template>


	<!-- Write header for the body section -->
	<xsl:template name="writeBodyHeader">
		<xsl:param name="title" />

		<!-- Write page title -->
		<xsl:if test="$title">
			<fo:block font-size="18pt" line-height="24pt" font-weight="bold"
				space-after.optimum="15pt" background-color="rgb(235,235,255)"
				color="black" text-align="center" padding-top="3pt">
				<xsl:value-of
					select="/fop-tournament/fop-messages/message[@key=normalize-space($title)]" />
			</fo:block>
		</xsl:if>

		<!-- Write error message if version is not registered -->
		<xsl:if test="/fop-tournament/info/license/type='licence_demo'">
			<fo:block-container z-index="100"
				block-progression-dimension="3in" inline-progression-dimension="7.5in div 2"
				display-align="center" text-align="center" absolute-position="absolute"
				left="3in" top="2.5in" background-color="rgb(235,150,150)"
				padding-top="3pt">
				<fo:block font-weight="bold" font-size="36pt" color="black"
					line-height="120%">
					<xsl:value-of
						select="/fop-tournament/fop-messages/message[@key='licence_notregistered']" />
				</fo:block>
			</fo:block-container>
		</xsl:if>
	</xsl:template>


	<!-- Writes an element in bold (depending on parameter) -->
	<xsl:template name="writeBoldElement">
		<xsl:param name="str" />
		<xsl:param name="isBold" />
		<xsl:choose>
			<xsl:when test="$isBold='true'">
				<fo:inline font-weight="bold">
					<xsl:value-of select="$str" />
				</fo:inline>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$str" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<!-- Writes a standard cell as mostly used in the PDFs -->
	<xsl:template name="writeStandardCell">
		<!-- Use &nbsp; if no value is passed -->
		<xsl:param name="value">&#160;</xsl:param>
		<xsl:param name="numberColumnsSpanned">
			1
		</xsl:param>
		<xsl:param name="numberRowsSpanned">
			1
		</xsl:param>
		<fo:table-cell border-style="solid" border-width="1pt"
			border-color="black" text-align="center">
			<xsl:if test="$numberColumnsSpanned > 1">
				<xsl:attribute name="number-columns-spanned"><xsl:value-of
					select="normalize-space($numberColumnsSpanned)" /></xsl:attribute>
			</xsl:if>
			<xsl:if test="$numberRowsSpanned > 1">
				<xsl:attribute name="number-rows-spanned"><xsl:value-of
					select="normalize-space($numberRowsSpanned)" /></xsl:attribute>
			</xsl:if>
			<fo:block space-before.optimum="1pt" space-after.optimum="1pt">
				<xsl:value-of select="$value" />
			</fo:block>
		</fo:table-cell>
	</xsl:template>


	<!--
		Writes a standard cell as mostly used in the PDFs with message key
	-->
	<xsl:template name="writeStandardCellMessage">
		<xsl:param name="key" />
		<xsl:call-template name="writeStandardCell">
			<xsl:with-param name="value"
				select="/fop-tournament/fop-messages/message[@key=normalize-space($key)]" />
		</xsl:call-template>
	</xsl:template>

	<!--
		Writes an empty row (used for tables without content in the body)
	-->
	<xsl:template name="writeEmptyRow">
		<fo:table-row>
			<fo:table-cell>
				<fo:block></fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

</xsl:stylesheet>
