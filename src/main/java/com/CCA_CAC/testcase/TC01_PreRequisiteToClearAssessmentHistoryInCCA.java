package com.CCA_CAC.testcase;

import org.junit.After;
import org.junit.Before;
import com.CCA.pageobjects.OpenCCA;
import cts.qea.automation.TestCase;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC01_PreRequisiteToClearAssessmentHistoryInCCA extends TestCase
{
	@Before
	public void setup()
	{
			iteration = "1";
	}
	@Override
	public void test() throws Exception
	{
		//Launching CCA Application
		Report.log("Launching CCA Application", Status.BUSINESSSTEP);
		OpenCCA CCA = new OpenCCA(driver);
		CCA.Launch(ValidationType.ASSERT);	//To launch CCA
		CCA.Login(ValidationType.ASSERT);	//To login CCA
		Report.closeBusinessStep();
		
		/*
		CCA-TestCase 
		PreRequisite To Clear The CAD Initial Assessment History In CCA Before validating Fresh CAD Initial assessment.
		*/
		Report.log("PreRequisite To Clear The CAD Initial Assessment History In CCA Application", Status.BUSINESSSTEP);
		com.CCA.pageobjects.TC01_PreRequisiteToClearAssessmentHistoryInCCA CCA_TC=new com.CCA.pageobjects.TC01_PreRequisiteToClearAssessmentHistoryInCCA(driver);		//Create constructor for CCA Testcase 1 pageobject 
		CCA_TC.TestCase(ValidationType.VERIFY);		
		Report.closeBusinessStep();
	}
	@After
	public void teardown()
	{
		OpenCCA URL = new OpenCCA(driver);
		URL.closeApp();
	}
}
