<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:svg="http://www.w3.org/2000/svg"
	version="2.0">

	<xsl:import href="pdf-generics.xsl" />

	<!-- Main page -->
	<xsl:template match="fop-tournament">

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

			<xsl:call-template name="writeMeta" />

			<fo:page-sequence master-reference="all" format="i">

				<xsl:call-template name="writePageHeaderFooter" />

				<fo:flow flow-name="xsl-region-body">

					<xsl:call-template name="writeBodyHeader">
						<xsl:with-param name="title">
							pdf_spielplan_title
						</xsl:with-param>
					</xsl:call-template>

					<!-- Main table -->
					<fo:table line-height="12pt" table-layout="fixed" width="100%"
						font-size="8pt" border-collapse="collapse">

						<!-- Table header -->
						<fo:table-column column-width="30mm" />
						<xsl:for-each select="./fields/fop-field">
							<fo:table-column column-width="proportional-column-width(1)" />
						</xsl:for-each>

						<fo:table-header>
							<fo:table-row font-weight="bold" text-align="center"
								vertical-align="middle" background-color="rgb(220,220,255)">
								<!-- Time column -->
								<fo:table-cell border-style="solid" border-width="1pt"
									border-color="black">
									<fo:block space-before.optimum="1pt"
										space-after.optimum="1pt">
										<xsl:value-of
											select="/fop-tournament/fop-messages/message[@key='pdf_spielplan_time']" />
									</fo:block>
								</fo:table-cell>

								<!-- Column for each field -->
								<xsl:for-each select="./fields/fop-field">
									<fo:table-cell border-style="solid" border-width="1pt"
										border-color="black">
										<fo:block space-before.optimum="1pt"
											space-after.optimum="1pt">
											<xsl:value-of select="./@name" />
										</fo:block>
									</fo:table-cell>
								</xsl:for-each>

							</fo:table-row>
						</fo:table-header>

						<!-- Table body -->
						<fo:table-body>
							<xsl:apply-templates select="blocks/block" />

							<!--
								Write empty row in case no blocks are known (to avoid FOP
								error)
							-->
							<xsl:if test="not(blocks/block)">
								<xsl:call-template name="writeEmptyRow" />
							</xsl:if>
						</fo:table-body>
					</fo:table>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<!-- Output one block -->
	<xsl:template match="block">
		<!-- Ensure that the page doesn't break in the middle of a row -->
		<fo:table-row border-color="black" keep-together="always" >
			<fo:table-cell border-style="solid" border-width="1pt"
				padding-left="3pt">
				<fo:block space-before.optimum="3pt" space-after.optimum="3pt" >
					<xsl:value-of select="./@start-time" />
					-
					<xsl:value-of select="./@end-time" />
				</fo:block>
			</fo:table-cell>

			<xsl:apply-templates select="fop-match" />
		</fo:table-row>
	</xsl:template>

	<!-- Output one match -->
	<xsl:template match="fop-match">
		<fo:table-cell border-style="solid" border-width="1pt"
			border-color="black" padding-left="3pt">
			<xsl:choose>
				<xsl:when test="./team1">
					<!-- If match is played -> render details -->
					<fo:block space-before.optimum="3pt">
						<!-- Add image in the group color -->
						<fo:instream-foreign-object>
							<svg:svg width="10" height="7">
								<xsl:element name="svg:g">
									<xsl:attribute name="fill"><xsl:value-of
										select="./@color" /></xsl:attribute>
									<xsl:attribute name="stroke">black</xsl:attribute>
									<svg:rect x="0" y="0" width="7" height="7" />
								</xsl:element>
							</svg:svg>
						</fo:instream-foreign-object>

						<!-- Teams of the match (in bold the winner) -->
						<xsl:call-template name="writeBoldElement">
							<xsl:with-param name="str" select="./team1/@rel" />
							<xsl:with-param name="isBold" select="./@winner!='team2'" />
						</xsl:call-template>
						-
						<xsl:call-template name="writeBoldElement">
							<xsl:with-param name="str" select="./team2/@rel" />
							<xsl:with-param name="isBold" select="./@winner!='team1'" />
						</xsl:call-template>
					</fo:block>

					<!-- Referee of the match -->
					<xsl:if test="./referee">
						<fo:block>
							(
							<xsl:value-of select="./referee/@rel" />
							)
						</fo:block>
					</xsl:if>

					<!-- Sets of the match (in bold the winner) -->
					<fo:block space-after.optimum="3pt">
						<xsl:for-each select="./sets/fop-set">
							<xsl:call-template name="writeBoldElement">
								<xsl:with-param name="str" select="./@points1" />
								<xsl:with-param name="isBold" select="./@winner='team1'" />
							</xsl:call-template>
							:
							<xsl:call-template name="writeBoldElement">
								<xsl:with-param name="str" select="./@points2" />
								<xsl:with-param name="isBold" select="./@winner='team2'" />
							</xsl:call-template>
							<xsl:if test="position() != last()">
								,
							</xsl:if>
						</xsl:for-each>
					</fo:block>
				</xsl:when>
				<xsl:otherwise>
					<!--
						Write empty cell if no match is played on this field for this
						block (to avoid FOP error of empty cell)
					-->
					<fo:block />
				</xsl:otherwise>
			</xsl:choose>
		</fo:table-cell>
	</xsl:template>

</xsl:stylesheet>
