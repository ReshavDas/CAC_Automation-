package cts.qea.automation.runners;

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

import cts.qea.automation.TestCase;

public class TestCaseThread extends Thread {

	final Class<TestCase> testClass;

	/**
	 * <b>Description</b>sets the iteration to test case object with test case id as the key
	 * @param		testClass test case wrapped class
	 * @param		itr iteration of the test case
	 */
	public TestCaseThread(Class<TestCase> testClass, String itr) {
		this.testClass = testClass;
		TestCase.addIteration(testClass.getSimpleName(), itr);
	}

	/**
	 * <b>Description</b>runs the thread
	 */
	public void run() {
		org.junit.runner.JUnitCore.runClasses(testClass);
	}
}
