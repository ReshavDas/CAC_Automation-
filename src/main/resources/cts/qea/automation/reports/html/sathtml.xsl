<?xml version="1.0"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<style>
					body { font-size:12px; background-color:#F6F4F0;
					margin:0px;font-family:arial;}
					table.container_footer{border:1px
					solid
					black;border-collapse:collapse;font-size:12px;margin-top:40px;background-color:AntiqueWhite;width:30%;margin-left:35%;margin-right:30%;}

					a:hover {color:red;}
					a:active {color:black;}
					.container {
					display:table; border:1px solid black; width:95%;margin-left:2%;
					margin-top:40px; background-color:AntiqueWhite;padding:5px;}
					.repheader { padding:5px; background-color:sienna;
					color:white;text-align:center; font-size:120%;}
					.tcpage { width:90%;
					margin:auto;}
					.topmenu { position: fixed; left: 0; top: 0; width:
					100%;}
					.tc { padding:2px; margin-bottom:4px; margin-top:4px;
					display:table;width:99%; }
					.tc .navlink { position:absolute;
					left:35px; width:25px; margin-left:3px; }
					.tc .name {
					position:absolute; font-size:15px; margin-left:4px;}
					.tc .day {
					float:right; margin-right:25px; }
					.tc .time { float:right;
					margin-right:5px; }
					.tc .stpcnt { float:right; margin-right:60px; }
					.tc .desc { margin-top:25px; margin-left:4px;}
					.tc .pass {
					border-left:4px solid #006100;
					color:#006100;background-color:Wheat; margin-left:25px}
					/*border:1px solid #006100;background-color:#C6EFCE; }*/
					.tc .fail {
					border-left:4px solid #9c0006;
					color:#9c0006;background-color:Wheat; margin-left:25px}
					/*border:1px solid #9c0006;background-color:#FFC7CE;}*/
					.tc .warning
					{ border-left:4px solid #9c6500;
					color:#9c6500;background-color:D0D0D0; margin-left:25px}
					/*border:1px solid #9c6500;background-color:#FFEB9C; }*/
					.tc .done {
					border-left:4px solid black; color:black;background-color:D0D0D0;
					margin-left:25px} /* border:1px solid black; */
					.step { padding:0px;
					margin-bottom:0px; margin-top:0px; width:99%; }
					.step .sno {
					float:left; margin-left:2px; }
					.step .status { float:right;
					width:50px; text-align:left;}
					.step .time { float:right; width:85px;
					margin-right:2px; }
					.step .desc { display:block; margin-right:1px;
					margin-left:40px; }
					.step .sat { display:block; margin-right:142px;
					margin-left:1px; color:blue; }
					.step .businessstep {color:black;
					background-color:tan;margin-top:5px; padding:3px; font-size:13px;}
					/*background-color:white; }*/
					.step .keyword {color:white;
					background-color:996633;margin-top:5px; padding:4px;
					font-size:14px;}
					.step .pass
					{color:#006100; background-color:White;
					padding:3px;}/*background-color:#C6EFCE; }*/
					.step .fail
					{color:#9c0006; background-color:White; padding:3px;
					}/*background-color:#FFC7CE; }*/
					.step .warning {color:#9c6500;
					background-color:White; padding:3px; }/*background-color:#FFEB9C;
					}*/
					.step .done {color:black; background-color:White; padding:3px;
					}/*background-color:white; }*/
					.step .sslink {color:black;}
					.batch {
					margin:5px; width:600px; padding:3px; }
					.batch .header { float:left;
					margin:2px; width:250px; padding:3px;color:white;
					background-color:#4BACC6; }
					.batch .header2 { float:left;
					margin:2px; width:50px; text-align:center;padding:3px; color:white;
					background-color:#4BACC6; }
					.batch .pass { float:left; margin:2px;
					width:50px; text-align:center;padding:3px; color:#006100;
					background-color:#C6EFCE; }
					.batch .fail { float:left; margin:2px;
					width:50px; text-align:center;padding:3px; color:#9c0006;
					background-color:#FFC7CE; }
					.batch .pending { float:left;
					margin:2px; width:50px; text-align:center;padding:3px; color:white;
					background-color:#A6A6A6; }
					.batch .noscript{ float:left;
					margin:2px; width:50px; text-align:center;padding:3px; color:white;
					background-color:white; }
					.menu { padding:5px; margin:10px;
					display:table-row; }
					.button { padding:5px; color:white;
					background-color:sienna; }
					.button a { color:white; }
					.meta {
					display:table; margin-left:50px; margin-bottom:2px;margin-top:5px;
					}
					.meta .key {display:inline; width: 150px; font-weight:bold; }
					.meta .value {display:inline; left:15px; position:relative; }
					td.meta_key{}
					td.meta_value{text-align:center;font-weight:bold;}
					td.meta_value_pass{text-align:center;font-weight:bold;color:green}
					td.meta_value_fail{text-align:center;font-weight:bold;color:red}
					.screenshot .container {position:absolute;height:100%;
					width:100%;bottom:0; left:0;
					margin:auto;text-align:center;background:rgba(0,0,0,.7);}
				</style>
			</head>
			<body>
				<div class="topmenu">
					<xsl:if test="/report/@doctype='summary'">
						<div class="repheader">
							<xsl:value-of select="report/summary/title" />
						</div>
					</xsl:if>
					<xsl:if test="/report/@doctype='testcase'">
						<div class="button">
							<a href="../suite.html">&lt; Back To Test Case Summary</a>
						</div>
					</xsl:if>
				</div>

				<div class="container">

					<div class="res">
						<xsl:apply-templates select="report/testcase" />
						<xsl:if test="report/step">
							<xsl:apply-templates select="report/step" />
						</xsl:if>
					</div>
				</div>
				<xsl:if test="report/summary">
					<table class="container_footer">
						<tr>
							<td class="repheader" colspan="2"> Test Execution Summary</td>
						</tr>
						<tr>
							<td class="meta_key">Environment</td>
							<td class="meta_value">
								<xsl:value-of select="report/summary/environment" />
							</td>
						</tr>
						<tr>
							<td class="meta_key">Build</td>
							<td class="meta_value">
								<xsl:value-of select="report/summary/build" />
							</td>
						</tr>
						<tr>
							<td class="meta_key">Test Cases Executed</td>
							<td class="meta_value">
								<xsl:value-of select="report/summary/tctotal" />
							</td>
						</tr>
						<tr>
							<td class="meta_key">Total Pass Count</td>
							<td class="meta_value_pass">
								<xsl:value-of select="report/summary/tcpass" />
							</td>
						</tr>
						<tr>
							<td class="meta_key">Total Fail Count</td>
							<td class="meta_value_fail">
								<xsl:value-of select="report/summary/tcfail" />
							</td>
						</tr>
						<tr>
							<td class="meta_key">Total Warning Count</td>
							<td class="meta_value">
								<xsl:value-of select="report/summary/tcwarning" />
							</td>
						</tr>
						<tr>
							<td class="meta_key">Start Time</td>
							<td class="meta_value">
								<xsl:value-of select="report/summary/startTime" />
							</td>
						</tr>
						<tr>
							<td class="meta_key">End Time</td>
							<td class="meta_value">
								<xsl:value-of select="report/summary/endTime" />
							</td>
						</tr>
					</table>
				</xsl:if>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="step">
		<div class='step' id="{@stepid}">
			<div class="{status}">
				<xsl:if test="status != 'KEYWORD'">
					<div class='sno'>
						<xsl:value-of select="@stepid" />
					</div>
				</xsl:if>
				<xsl:if test="status != 'BUSINESSSTEP'">
					<xsl:if test="screenshot">
						<xsl:if test="status = 'Pass'">
							<div class='status'>
								<a class="sslink" href="{screenshot}" target="_blank" style="color:#006100">
									<xsl:value-of select="status" />
								</a>
							</div>
						</xsl:if>
						<xsl:if test="status = 'Fail'">
							<div class='status'>
								<a class="sslink" href="{screenshot}" target="_blank" style="color:#9c0006">
									<xsl:value-of select="status" />
								</a>
							</div>
						</xsl:if>
						<xsl:if test="status = 'Done'">
							<div class='status'>
								<a class="sslink" href="{screenshot}" target="_blank" style="color:black">
									File Link
								</a>
							</div>
						</xsl:if>
					</xsl:if>
					<xsl:if test="status != 'KEYWORD'">
						<xsl:if test="not(screenshot)">
							<div class='status'>
								<xsl:value-of select="status" />
							</div>
						</xsl:if>
						<div class='time'>
							<xsl:value-of select="time" />
						</div>
					</xsl:if>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="contains(description, 'SAT ID:')">
						<xsl:variable name="message" select="description" />
						<xsl:variable name="desc_message" select="substring-before($message, 'SAT ID:')"/> 
						<xsl:variable name="satId" select="substring-after($message, 'SAT ID:')" />
						<xsl:variable name="satId1" select="concat('SAT ID:',$satId )" />
						<div class='desc' style="display:inline"><xsl:value-of select="$desc_message"/></div>
						<div class='sat' style="display:inline"><xsl:value-of select="$satId1" /></div>
					</xsl:when>
					<xsl:otherwise>
						<div class='desc'>
							<xsl:value-of select="description" />
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="testcase">
		<div class="tc">
			<xsl:if test="/report/@doctype='summary'">
				<div class='navlink'>
					<a href="{resultFolderPath}/{resultFileName}.html" style="text-decoration:none">[+]</a>
				</div>
			</xsl:if>
			<xsl:if test="/report/@doctype='testcase'">
				<div class='navlink'>
					<a href="../suite.html" style="text-decoration:none">[-]</a>
				</div>
			</xsl:if>

			<div class="{status}">
				<div class='name'>
					<xsl:value-of select="@uname" />
				</div>
				<div class='time'>
					<xsl:value-of select="starttime" />
					to
					<xsl:value-of select="endtime" />
				</div>
				<div class='stpcnt'>
					Passed:
					<xsl:value-of select="pass" />
					Failed:
					<xsl:value-of select="fail" />
				</div>
				<div class='day'>
					Day:
					<xsl:value-of select="day" />
					of
					<xsl:value-of select="totaldays" />
				</div>
				<br />
				<div class='desc'>
					<xsl:value-of select="description" />
				</div>
				<xsl:apply-templates select="metainfo" />
			</div>
		</div>
	</xsl:template>

	<xsl:template match="metainfo">
		<div class="meta">
			<div class='key'>
				<xsl:value-of select="key" />
			</div>
			<div class='value'>
				<xsl:value-of select="value" />
			</div>
		</div>
	</xsl:template>
</xsl:stylesheet>