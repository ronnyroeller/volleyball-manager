<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0">

	<xsl:import href="pdf-generics.xsl" />

	<xsl:template match="fop-tournament">

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

			<xsl:call-template name="writeMeta" />

			<fo:page-sequence master-reference="all" format="i">

				<xsl:call-template name="writePageHeaderFooter" />

				<fo:flow flow-name="xsl-region-body">

					<!--
						Only render matches that are played (e.g. have teams and winners)
					-->
					<xsl:apply-templates select="blocks/block/fop-match[@field]" />

				</fo:flow>
			</fo:page-sequence>

		</fo:root>
	</xsl:template>

	<xsl:template match="fop-match">

		<!-- Break the page after every 3 entries and add header -->
		<xsl:if test="(position() mod 3 = 1)">
			<!-- Don't break page for the first entry (to avoid an empty page) -->
			<xsl:if test="(position() > 1)">
				<fo:block break-after="page">
				</fo:block>
			</xsl:if>

			<xsl:call-template name="writeBodyHeader">
				<xsl:with-param name="title">
					pdf_schiedsrichter_title
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<fo:block space-after.optimum="10pt">
			<!-- Header with match information -->
			<fo:table table-layout="fixed" line-height="12pt" font-size="8pt"
				border-collapse="collapse" width="100%">

				<fo:table-column column-width="proportional-column-width(1)" />
				<fo:table-column column-width="proportional-column-width(4)" />
				<fo:table-column column-width="proportional-column-width(1)" />
				<fo:table-column column-width="proportional-column-width(4)" />

				<fo:table-body>
					<fo:table-row vertical-align="middle">
						<xsl:call-template name="writeMatchInfoEntry">
							<xsl:with-param name="title">
								pdf_schiedsrichter_gamenr
							</xsl:with-param>
							<xsl:with-param name="value">
								<xsl:value-of select="position()" />
								/
								<xsl:value-of select="count(//fop-match[@winner])" /> #
								<xsl:value-of select="position() mod 3" />
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="writeMatchInfoEntry">
							<xsl:with-param name="title">
								pdf_schiedsrichter_field
							</xsl:with-param>
							<xsl:with-param name="value" select="./@field" />
						</xsl:call-template>
					</fo:table-row>
					<fo:table-row vertical-align="middle">
						<xsl:call-template name="writeMatchInfoEntry">
							<xsl:with-param name="title">
								pdf_schiedsrichter_time
							</xsl:with-param>
							<xsl:with-param name="value">
								<xsl:value-of select="../@start-time" />
								-
								<xsl:value-of select="../@end-time" />
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="writeMatchInfoEntry">
							<xsl:with-param name="title">
								pdf_schiedsrichter_group
							</xsl:with-param>
							<xsl:with-param name="value" select="./@group" />
						</xsl:call-template>
					</fo:table-row>
				</fo:table-body>
			</fo:table>

			<!-- Table to fill out results -->
			<fo:table space-before.optimum="5pt" table-layout="fixed"
				line-height="12pt" font-size="8pt" border-collapse="collapse" width="100%">

				<fo:table-column column-width="proportional-column-width(1)" />
				<fo:table-column column-width="proportional-column-width(6)" />
				<fo:table-column column-width="proportional-column-width(1)" />
				<fo:table-column column-width="proportional-column-width(6)" />
				<fo:table-column column-width="proportional-column-width(1)" />

				<fo:table-header>
					<fo:table-row font-weight="bold" text-align="center"
						vertical-align="middle" background-color="rgb(220,220,255)">
						<xsl:call-template name="writePointsHeaderCell">
							<xsl:with-param name="title">
								pdf_schiedsrichter_set
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="writePointsHeaderCell">
							<xsl:with-param name="title">
								pdf_schiedsrichter_list
							</xsl:with-param>
							<xsl:with-param name="additional_title" select="./team1/@rel" />
						</xsl:call-template>
						<xsl:call-template name="writePointsHeaderCell">
							<xsl:with-param name="title">
								pdf_schiedsrichter_points
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="writePointsHeaderCell">
							<xsl:with-param name="title">
								pdf_schiedsrichter_list
							</xsl:with-param>
							<xsl:with-param name="additional_title" select="./team2/@rel" />
						</xsl:call-template>
						<xsl:call-template name="writePointsHeaderCell">
							<xsl:with-param name="title">
								pdf_schiedsrichter_points
							</xsl:with-param>
						</xsl:call-template>
					</fo:table-row>
				</fo:table-header>

				<fo:table-body>
					<xsl:call-template name="writeResultRow">
						<xsl:with-param name="rowNr">
							1
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="writeResultRow">
						<xsl:with-param name="rowNr">
							2
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="writeResultRow">
						<xsl:with-param name="rowNr">
							3
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="writeResultRow">
						<xsl:with-param name="rowNr">
							4
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="writeResultRow">
						<xsl:with-param name="rowNr">
							5
						</xsl:with-param>
					</xsl:call-template>
				</fo:table-body>
			</fo:table>

			<!-- Add referee -->
			<fo:block space-before.optimum="5pt" line-height="12pt"
				font-size="8pt">
				<xsl:value-of
					select="/fop-tournament/fop-messages/message[@key='pdf_schiedsrichter_referee']" />
				:
				<xsl:value-of select="./referee/@rel" />
			</fo:block>
		</fo:block>

	</xsl:template>

	<!-- Writes an information cell about the match, e.g. time, field -->
	<xsl:template name="writeMatchInfoEntry">
		<xsl:param name="title" />
		<xsl:param name="value" />
		<fo:table-cell font-weight="bold">
			<fo:block space-before.optimum="1pt" space-after.optimum="1pt">
				<xsl:value-of
					select="/fop-tournament/fop-messages/message[@key=normalize-space($title)]" />
				:
			</fo:block>
		</fo:table-cell>
		<fo:table-cell>
			<fo:block space-before.optimum="1pt" space-after.optimum="1pt">
				<xsl:value-of select="$value" />
			</fo:block>
		</fo:table-cell>
	</xsl:template>

	<!--
		Writes a header cell for the table, in which the referee fills out
		points
	-->
	<xsl:template name="writePointsHeaderCell">
		<xsl:param name="title" />
		<xsl:param name="additional_title" />
		<fo:table-cell border-style="solid" border-width="1pt"
			border-color="black">
			<fo:block space-before.optimum="1pt" space-after.optimum="1pt">
				<xsl:value-of select="/fop-tournament/fop-messages/message[@key=$title]" />
				<xsl:value-of select="$additional_title" />
			</fo:block>
		</fo:table-cell>
	</xsl:template>

	<!-- Writes a row, in which the referee fills out points -->
	<xsl:template name="writeResultRow">
		<xsl:param name="rowNr" />
		<fo:table-row>
			<fo:table-cell border-style="solid" border-width="1pt"
				padding-left="3pt">
				<fo:block space-before.optimum="3pt" space-after.optimum="30pt"
					text-align="center">
					<xsl:value-of select="$rowNr" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" border-width="1pt">
				<fo:block />
			</fo:table-cell>
			<fo:table-cell border-style="solid" border-width="1pt">
				<fo:block />
			</fo:table-cell>
			<fo:table-cell border-style="solid" border-width="1pt">
				<fo:block />
			</fo:table-cell>
			<fo:table-cell border-style="solid" border-width="1pt">
				<fo:block />
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

</xsl:stylesheet>
