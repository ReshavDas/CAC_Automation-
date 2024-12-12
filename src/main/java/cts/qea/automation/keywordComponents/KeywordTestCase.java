package cts.qea.automation.keywordComponents;

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

import java.io.File;
import java.util.*;
import org.apache.poi.xssf.usermodel.*;
import org.junit.*;
import cts.qea.automation.*;
import cts.qea.automation.excel.*;
import cts.qea.automation.reports.*;

public class KeywordTestCase extends TestCase{

	static volatile Queue<String> scenariosIterator = new LinkedList<String>();

	@Before
	public void before() throws Exception{
		scenarioName = getSceanrio();
		iteration ="1";
	}

	@Override
	@SuppressWarnings("unchecked")
	public void test() throws Exception{
		Class<ComponentTestCase> test = null;
		ExcelHelper excel = new ExcelHelper();
		XSSFSheet mySheet = excel.getsheet("keyword"+File.separator+"Keywords.xlsx", "Keywords");
		// Load the common details that are present in the CommonDetails sheet to configure the base data
		for (int row = 0; row < 100; row++) {
			String scenario = excel.getCellData(mySheet, row, 0).toLowerCase();
			String execute = excel.getCellData(mySheet, row, 1).toLowerCase();
			if(scenario.equals(""))
				break;
			if(!scenario.equals("") &&
					scenario.equalsIgnoreCase(scenarioName) &&
					!execute.equals("") &&
					execute.equalsIgnoreCase("true")){
				for (int columns = 2; columns < 100; columns++) {
					String component = excel.getCellData(mySheet, row, columns);
					if(component.equals(""))
						break;
					int index = component.split("\\.").length;
					String comp = component.split("\\.")[index-1];
					Report.log("Start of Keyword: "+comp, Status.KEYWORD);
					test = (Class<ComponentTestCase>) Class.forName(component);
					ComponentTestCase.setDataProvider(dp);
					ComponentTestCase.setWebDriver(driver);
					org.junit.runner.JUnitCore.runClasses(test);
				}
			}
		}

		//		End the TestCase 
			end();
	}

	public final static synchronized void addSceanrios(String scenarios){
		scenariosIterator.add(scenarios);
	}

	public final static synchronized String getSceanrio(){
		return scenariosIterator.poll();
	}
}