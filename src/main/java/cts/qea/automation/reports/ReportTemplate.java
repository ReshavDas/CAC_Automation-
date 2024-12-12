package cts.qea.automation.reports;

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

public interface ReportTemplate {
	
	public enum Caller {
		SUITE,
		TEST,
		APPLICATION
	}
	
	public void startSuite(Caller caller, String rootFolder);
	public void endSuite(Caller caller);
	public void startTest(TestCase tc) ;
	public void endTest(TestCase tc) ;
	public void addAttachement(String message, String filepath);
	public void step(String message, Status status);
	public void addToFooter(String name, String value);
	public void setTittle(String tittle);
}
