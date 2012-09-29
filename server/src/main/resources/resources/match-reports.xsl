<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" version="2.0">

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
							pdf_spielbericht_title
						</xsl:with-param>
					</xsl:call-template>

				</fo:flow>
			</fo:page-sequence>

			<!--
				Only render matches that are played (e.g. have teams and winners).
				Each match goes to a separated page.
			-->
			<xsl:apply-templates select="blocks/block/fop-match[@field]" />

		</fo:root>
	</xsl:template>

	<xsl:template match="fop-match">

		<fo:page-sequence master-reference="all" format="i">

			<xsl:call-template name="writePageHeaderFooter" />

			<fo:flow flow-name="xsl-region-body">

				<xsl:call-template name="writeBodyHeader" />

				<fo:block space-after.optimum="0pt">

					<!-- Header with match information -->
					<fo:table table-layout="fixed" line-height="12pt"
						font-size="8pt" border-collapse="collapse" width="100%">

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
										<xsl:value-of select="count(//fop-match[@winner])" />
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

					<xsl:call-template name="table-of-sets" />

					<fo:table table-layout="fixed" width="100%"
						space-before.optimum="10pt" font-size="8pt">
						<fo:table-column column-width="proportional-column-width(2)" />
						<fo:table-column column-width="proportional-column-width(1)" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<xsl:call-template name="table-of-players" />
								</fo:table-cell>

								<fo:table-cell>
									<xsl:call-template name="signatures" />
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>
				</fo:block>

			</fo:flow>
		</fo:page-sequence>

	</xsl:template>

	<!-- Writes boxes in a loop -->
	<xsl:template name="boxloop">
		<xsl:param name="repeat">
			0
		</xsl:param>
		<xsl:if test="number($repeat) >= 1">
			<fo:table-cell border-style="solid" border-width="1pt">
				<!-- Add none-breaking space so that the cell is shown -->
				<fo:block space-before.optimum="3pt" space-after.optimum="7pt">&#160;
				</fo:block>
			</fo:table-cell>
			<xsl:call-template name="boxloop">
				<xsl:with-param name="repeat" select="$repeat - 1" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- Table with all the sets -->
	<xsl:template name="table-of-sets">
		<fo:table space-before.optimum="5pt" table-layout="fixed"
			line-height="12pt" font-size="8pt" border-collapse="collapse" width="100%">

			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1.5)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(1)" />

			<!-- Header row with the team names -->
			<fo:table-header>
				<fo:table-row font-weight="bold" text-align="center"
					vertical-align="middle" background-color="rgb(220,220,255)">
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value" select="./team1/@rel" />
						<xsl:with-param name="numberColumnsSpanned">
							7
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value"
							select="/fop-tournament/fop-messages/message[@key='pdf_schiedsrichter_points']" />
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value" select="./team2/@rel" />
						<xsl:with-param name="numberColumnsSpanned">
							7
						</xsl:with-param>
					</xsl:call-template>
				</fo:table-row>
			</fo:table-header>

			<!-- Rows for each set -->
			<fo:table-body>
				<xsl:call-template name="row-set">
					<xsl:with-param name="rowcount" select="1" />
				</xsl:call-template>
				<xsl:call-template name="row-set">
					<xsl:with-param name="rowcount" select="2" />
				</xsl:call-template>
				<xsl:call-template name="row-set">
					<xsl:with-param name="rowcount" select="3" />
				</xsl:call-template>
				<xsl:call-template name="row-set">
					<xsl:with-param name="rowcount" select="4" />
				</xsl:call-template>
				<xsl:call-template name="row-set">
					<xsl:with-param name="rowcount" select="5" />
				</xsl:call-template>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- Renders one row for a set -->
	<xsl:template name="row-set">
		<!-- Number of rows that should be rendered (recursive function) -->
		<xsl:param name="rowcount">
			0
		</xsl:param>

		<!-- Header for the balls -->
		<fo:table-row>
			<fo:table-cell border-style="solid" border-width="1pt"
				padding-left="3pt" number-columns-spanned="6" number-rows-spanned="2">
				<fo:block space-before.optimum="3pt" space-after.optimum="2pt">
					1
					2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26
					27
				</fo:block>
				<fo:block space-after.optimum="3pt">
					28 29 30 31 32 33 34 35 36 37
					38 39 40 41 42 43 44 45 46 47 48 49 50
  	        </fo:block>
			</fo:table-cell>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value">
					T
				</xsl:with-param>
				<xsl:with-param name="numberRowsSpanned">
					2
				</xsl:with-param>
			</xsl:call-template>
			<fo:table-cell border-style="solid" border-width="1pt"
				padding-left="3pt">
				<fo:block space-before.optimum="3pt" space-after.optimum="3pt"
					text-align="center">
					<xsl:value-of
						select="/fop-tournament/fop-messages/message[@key='pdf_schiedsrichter_set']" />
					<xsl:text> </xsl:text>
					<xsl:value-of select="$rowcount" />
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border-style="solid" border-width="1pt"
				padding-left="3pt" number-columns-spanned="6" number-rows-spanned="2">
				<fo:block space-before.optimum="3pt" space-after.optimum="2pt">
					1
					2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26
					27
	  	        </fo:block>
				<fo:block space-after.optimum="3pt">
					28 29 30 31 32 33 34 35 36 37
					38 39 40 41 42 43 44 45 46 47 48 49 50
  		        </fo:block>
			</fo:table-cell>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value">
					T
				</xsl:with-param>
				<xsl:with-param name="numberRowsSpanned">
					2
				</xsl:with-param>
			</xsl:call-template>
		</fo:table-row>

		<!-- Column in the middle of each set -->
		<fo:table-row>
			<xsl:call-template name="writeStandardCell">
				<xsl:with-param name="value">
					:
				</xsl:with-param>
				<xsl:with-param name="numberRowsSpanned">
					3
				</xsl:with-param>
			</xsl:call-template>
		</fo:table-row>

		<!-- Two rows of boxes for the points -->
		<fo:table-row>
			<xsl:call-template name="boxloop">
				<xsl:with-param name="repeat" select="number(14)" />
			</xsl:call-template>
		</fo:table-row>
		<fo:table-row>
			<xsl:call-template name="boxloop">
				<xsl:with-param name="repeat" select="number(14)" />
			</xsl:call-template>
		</fo:table-row>
	</xsl:template>

	<!-- Table for the players of each team -->
	<xsl:template name="table-of-players">
		<fo:table space-before.optimum="25pt" table-layout="fixed"
			line-height="12pt" font-size="8pt" border-collapse="collapse" width="95%">

			<fo:table-column column-width="proportional-column-width(0.5)" />
			<fo:table-column column-width="proportional-column-width(2)" />
			<fo:table-column column-width="proportional-column-width(1)" />
			<fo:table-column column-width="proportional-column-width(0.5)" />
			<fo:table-column column-width="proportional-column-width(2)" />
			<fo:table-column column-width="proportional-column-width(1)" />

			<fo:table-header>
				<fo:table-row font-weight="bold" text-align="center"
					vertical-align="middle" background-color="rgb(220,220,255)">
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value" select="./team1/@rel" />
						<xsl:with-param name="numberColumnsSpanned">
							3
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value" select="./team2/@rel" />
						<xsl:with-param name="numberColumnsSpanned">
							3
						</xsl:with-param>
					</xsl:call-template>
				</fo:table-row>
				<fo:table-row font-weight="bold" text-align="center"
					vertical-align="middle" background-color="rgb(220,220,255)">
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value"
							select="/fop-tournament/fop-messages/message[@key='pdf_spielbericht_number']" />
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value"
							select="/fop-tournament/fop-messages/message[@key='pdf_spielbericht_name']" />
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value"
							select="/fop-tournament/fop-messages/message[@key='pdf_spielbericht_passnumber']" />
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value"
							select="/fop-tournament/fop-messages/message[@key='pdf_spielbericht_number']" />
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value"
							select="/fop-tournament/fop-messages/message[@key='pdf_spielbericht_name']" />
					</xsl:call-template>
					<xsl:call-template name="writeStandardCell">
						<xsl:with-param name="value"
							select="/fop-tournament/fop-messages/message[@key='pdf_spielbericht_passnumber']" />
					</xsl:call-template>
				</fo:table-row>
			</fo:table-header>

			<fo:table-body>
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
				<xsl:call-template name="row-player" />
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
	</xsl:template>

	<!-- Writes one row for player -->
	<xsl:template name="row-player">
		<fo:table-row>
			<xsl:call-template name="boxloop">
				<xsl:with-param name="repeat" select="number(6)" />
			</xsl:call-template>
		</fo:table-row>
	</xsl:template>


	<!-- Table for the signatures -->
	<xsl:template name="signatures">

		<!-- Remark row -->
		<fo:block font-size="8pt">
			<xsl:value-of
				select="/fop-tournament/fop-messages/message[@key='pdf_spielbericht_remark']" />
		</fo:block>

		<!-- Signatures -->
		<fo:table table-layout="fixed" width="100%"
			font-size="8pt">
			<fo:table-body>

				<xsl:call-template name="writeSignature">
					<xsl:with-param name="value" select="./team1/@rel" />
				</xsl:call-template>
				<xsl:call-template name="writeSignature">
					<xsl:with-param name="value" select="./team2/@rel" />
				</xsl:call-template>
				<xsl:call-template name="writeSignature">
					<xsl:with-param name="value">
						<xsl:value-of
							select="/fop-tournament/fop-messages/message[@key='pdf_schiedsrichter_secretary']" />
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="writeSignature">
					<xsl:with-param name="value">
						<xsl:value-of
							select="/fop-tournament/fop-messages/message[@key='pdf_schiedsrichter_referee']" />
						2
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="writeSignature">
					<xsl:with-param name="value">
						<xsl:value-of
							select="/fop-tournament/fop-messages/message[@key='pdf_schiedsrichter_referee']" />
						1
					</xsl:with-param>
				</xsl:call-template>
			</fo:table-body>
		</fo:table>
	</xsl:template>


	<!-- Writes a signature entry -->
	<xsl:template name="writeSignature">
		<xsl:param name="value" />
		<fo:table-row line-height="24pt">
			<fo:table-cell>
				<fo:block>&#160;</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row border-top-style="solid">
			<fo:table-cell text-align="center">
				<fo:block>
					<xsl:value-of select="$value" />
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
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

</xsl:stylesheet>
