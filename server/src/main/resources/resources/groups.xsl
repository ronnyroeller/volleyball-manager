<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:svg="http://www.w3.org/2000/svg"
	version="1.0">

	<xsl:import href="pdf-generics.xsl" />

	<xsl:template match="fop-tournament">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

			<xsl:call-template name="writeMeta" />

			<xsl:apply-templates select="groups/fop-group" />

		</fo:root>
	</xsl:template>

	<xsl:template match="fop-group">

		<fo:page-sequence master-reference="all" format="i">

			<xsl:call-template name="writePageHeaderFooter" />

			<fo:flow flow-name="xsl-region-body">

				<xsl:call-template name="writeBodyHeader">
					<xsl:with-param name="title">
						pdf_gruppen_title
					</xsl:with-param>
				</xsl:call-template>

				<!-- Group color and name -->
				<fo:block font-size="14pt" line-height="20pt" font-weight="bold"
					padding-top="3pt">
					<fo:instream-foreign-object>
						<svg:svg width="10" height="7" stroke="black">
							<xsl:element name="svg:g">
								<xsl:attribute name="fill">
					<xsl:value-of select="./@color" />
	      		</xsl:attribute>
								<svg:rect x="0" y="0" width="7" height="7" />
							</xsl:element>
						</svg:svg>
					</fo:instream-foreign-object>

					<xsl:value-of select="./@name" />
				</fo:block>

				<!-- Tournament system of the group -->
				<fo:block font-size="10pt" line-height="20pt" padding-top="3pt">
					<xsl:value-of
						select="/fop-tournament/fop-messages/message[@key='pdf_gruppen_mode']" />
					:
					<xsl:value-of select="./@system" />
				</fo:block>

				<!-- List all teams with their results -->
				<fo:table table-layout="fixed" line-height="12pt"
					font-size="8pt" border-collapse="collapse" width="100%">

					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(2)" />
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-column column-width="proportional-column-width(1)" />

					<!-- Table header -->
					<fo:table-header>
						<fo:table-row font-weight="bold" text-align="center"
							vertical-align="middle" background-color="rgb(220,220,255)">
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_platz
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_team
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_ga
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_pga
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_nga
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_pset
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_nset
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_ppo
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_npo
								</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="writeStandardCellMessage">
								<xsl:with-param name="key">
									jsp_gruppe_diffpo
								</xsl:with-param>
							</xsl:call-template>
						</fo:table-row>
					</fo:table-header>

					<!-- Results for each team in the group -->
					<fo:table-body>
						<xsl:apply-templates select="fop-team" />

						<!--
							Write empty row in case no teams are registered (to avoid FOP
							error)
						-->
						<xsl:if test="not(fop-team)">
							<xsl:call-template name="writeEmptyRow" />
						</xsl:if>
					</fo:table-body>
				</fo:table>

			</fo:flow>

		</fo:page-sequence>

	</xsl:template>


	<!-- Renders the results for a specific team -->
	<xsl:template match="fop-team">
		<fo:table-row border-color="black">
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@rank" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@name" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@total-matches" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@won-matches" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@lost-matches" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@won-sets" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@lost-sets" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@won-points" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@lost-points" />
			</xsl:call-template>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value" select="./@diff-points" />
			</xsl:call-template>
		</fo:table-row>

	</xsl:template>

</xsl:stylesheet>
