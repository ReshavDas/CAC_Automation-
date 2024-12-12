package cts.qea.automation.reports.html;

/**********************************************************************
* COGNIZANT CONFIDENTIAL OR TRADE SECRET
*
* Copyright 2020 - 2023 Cognizant.  All rights reserved.
*
* NOTICE:  This unpublished material is proprietary to Cognizant and 
* its suppliers, if any.  The methods, techniques and technical 
* concepts herein are considered Cognizant confidential or trade 
* secret information.  This material may also be covered by U.S. or
* foreign patents or patent applications.  Use, distribution or 
* copying of these materials, in whole or in part, is forbidden, 
* except by express written permission of Cognizant.
***********************************************************************/

import java.util.List;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.StepElement;
import cts.qea.automation.reports.SummaryElement;
import cts.qea.automation.reports.TestCaseElement;

public class HTMLReport {
	
	public static void endOfSuite(String rootPath, SummaryElement summary, List<TestCaseElement> testCases) {
		SuiteReport suite = new SuiteReport();
		suite.summary = summary;
		suite.testcase = testCases;

		try {
			Report.writeToHTML(suite.getClass(), suite, rootPath + "/suite.html", HTMLReport.class.getResource("html.xsl").toString());
			Report.writeToXML(suite.getClass(), suite, rootPath + "/suite.xml", "../html.xsl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void endOfTest(String rootPath, TestCaseElement tcInfo, List<StepElement> steps) {
		String fileName = rootPath + "/" + tcInfo.resultFolderPath + "/" + tcInfo.resultFileName + ".html";
		
		TestCaseReport tcReport = new TestCaseReport();
		tcReport.addTc(tcInfo);
		tcReport.addSteps(steps);
		
		try {
			Report.writeToHTML(tcReport.getClass(), tcReport, fileName, HTMLReport.class.getResource("html.xsl").toString());
			Report.writeToXML(tcReport.getClass(), tcReport, fileName.replace(".html", ".xml"), "../../html.xsl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
