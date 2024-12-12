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

import org.junit.Test;

import cts.qea.automation.DataProvider;
import cts.qea.automation.WrappedWebDriver;

public abstract class ComponentTestCase {

	protected static WrappedWebDriver driver = null;
	protected static DataProvider dp = null;

	public static void setDataProvider(DataProvider scenariodp){
		dp = scenariodp;
	}

	public static void setWebDriver(WrappedWebDriver scenariodriver){
		driver = scenariodriver;
	}

	@Test
	public void initialiseObjects() throws Exception{
		test();
	}

	public abstract void test() throws Exception;

}
