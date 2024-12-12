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

public class TC02_ValidateMemberInformationInCACMatchesWithCCA extends TestCase
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
		-To capture all members information In CCA Application
		*/
		Report.log("Validate Member Information In CCA Application", Status.BUSINESSSTEP);
		com.CCA.pageobjects.TC02_ValidateMemberInformationInCCA CCA_TC2=new com.CCA.pageobjects.TC02_ValidateMemberInformationInCCA(driver);		//Create constructor for CCA Testcase 1 pageobject 
		HashMap<String, String> MemberDataList=CCA_TC2.TestCase2(ValidationType.VERIFY, dp);		
		Report.closeBusinessStep();
		
		//Launching CAC Application
		Report.log("Launching CAC Application", Status.BUSINESSSTEP);
		OpenCAC CAC_URL = new OpenCAC(driver);
		CAC_URL.Launch(ValidationType.ASSERT);	//To launch CAC
		Report.closeBusinessStep();
		
		/*
		CAC-TestCase 
		-To capture all members information In CAC Application and Matches With CCA
		*/
		Report.log("Validate Member Information In CAC Application Matches With CCA Application", Status.BUSINESSSTEP);
		com.CAC.pageobjects.TC02_ValidateMemberInformationInCAC CAC_TC2=new com.CAC.pageobjects.TC02_ValidateMemberInformationInCAC(driver);		//Create constructor for CCA Testcase 1 pageobject 
		MemberDataList=CAC_TC2.TestCase2(ValidationType.VERIFY, dp, MemberDataList);
		Report.closeBusinessStep();
		
	}
	@After
	public void teardown()
	{
		OpenCCA URL = new OpenCCA(driver);
		URL.closeApp();
	}
}
