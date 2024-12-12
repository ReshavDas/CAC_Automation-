package com.CCA_CAC.testcase;

import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import com.CAC.pageobjects.OpenCAC;
import com.CCA.pageobjects.OpenCCA;
import cts.qea.automation.TestCase;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC03_ValidateAssessmentPageComponentsAndItemsInCACAndCCA extends TestCase
{
	@Before
	public void setup()
	{
			iteration = "1";
	}
	@Override
	public void test() throws Exception
	{
		/*
		Prerequisite:
		-Execute PreRequisite Test Case 1 To clear CAD Initial Assessment History In CCA
		-Right now we could not able to By-Pass credentials in CAC URL directly so for now we logged in manually
		*/
		
		//Launching CCA Application
		Report.log("Launching CCA Application", Status.BUSINESSSTEP);
		OpenCCA CCA = new OpenCCA(driver);
		CCA.Launch(ValidationType.ASSERT);	//To launch CCA
		CCA.Login(ValidationType.ASSERT);	//To login CCA
		Report.closeBusinessStep();
		
		/*
		CCA-TestCase
		-To validate and capture CAD Initial Assessment Page Components And Items in CCA
		*/
		Report.log("Validate CAD Initial Assessment Page Components And Items In CCA Application", Status.BUSINESSSTEP);
		com.CCA.pageobjects.TC03_ValidateAssessmentPageComponentsAndItemsInCCA CCA_TC3=new com.CCA.pageobjects.TC03_ValidateAssessmentPageComponentsAndItemsInCCA(driver);		//Create constructor for CCA Testcase 1 pageobject 
		HashMap<String, String> MemberDataList=CCA_TC3.TestCase3(ValidationType.VERIFY, dp);		
		Report.closeBusinessStep();
		
		//Launching CAC Application
		Report.log("Launching CAC Application", Status.BUSINESSSTEP);
		OpenCAC CAC_URL = new OpenCAC(driver);
		CAC_URL.Launch(ValidationType.ASSERT);	//To launch CAC
		Report.closeBusinessStep();
		
		/*
		CAC-TestCase
		-To validate and capture CAD Initial Assessment Page Components in CAC and compare CIA assessment components in both CCA and CAC application
		*/
		Report.log("Validate CAD Initial Assessment Page Components And Items In CAC Application Matches With CCA Application", Status.BUSINESSSTEP);
		com.CAC.pageobjects.TC03_ValidateAssessmentPageComponentsAndItemsInCAC CAC_TC3=new com.CAC.pageobjects.TC03_ValidateAssessmentPageComponentsAndItemsInCAC(driver);		//Create constructor for CCA Testcase 1 pageobject 
		MemberDataList=CAC_TC3.TestCase3(ValidationType.VERIFY, dp, MemberDataList);
		Report.closeBusinessStep();
	}
	@After
	public void teardown()
	{
		OpenCCA URL = new OpenCCA(driver);
		URL.closeApp();
	}
}
