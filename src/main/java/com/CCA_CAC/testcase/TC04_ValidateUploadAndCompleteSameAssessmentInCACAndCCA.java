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

public class TC04_ValidateUploadAndCompleteSameAssessmentInCACAndCCA extends TestCase
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
		-To capture first member information From CCA Application
		*/
		Report.log("Capture First Member Information From CCA Application", Status.BUSINESSSTEP);
		com.CCA.pageobjects.TC04A_CaptureFirstMemberInformationInCCA CCA_TC4A=new com.CCA.pageobjects.TC04A_CaptureFirstMemberInformationInCCA(driver);		//Create constructor for CCA Testcase 1 pageobject 
		HashMap<String, String> MemberDataList=CCA_TC4A.TestCase4A(ValidationType.VERIFY, dp);		
		Report.closeBusinessStep();
		
		//Launching CAC Application
		Report.log("Launching CAC Application", Status.BUSINESSSTEP);
		OpenCAC CAC_URL = new OpenCAC(driver);
		CAC_URL.Launch(ValidationType.ASSERT);	//To launch CAC
		Report.closeBusinessStep();
		
		/*
		CAC-TestCase
		-To Validate Complete And Upload Same Assessment In CAC Application
		*/
		Report.log("Validate Complete And Upload Same Assessment In CAC Application", Status.BUSINESSSTEP);
		com.CAC.pageobjects.TC04_ValidateCompleteAndUploadSameAssessmentInCAC CAC_TC4=new com.CAC.pageobjects.TC04_ValidateCompleteAndUploadSameAssessmentInCAC(driver);		//Create constructor for CCA Testcase 1 pageobject 
		MemberDataList=CAC_TC4.TestCase4(ValidationType.VERIFY, dp, MemberDataList);
		Report.closeBusinessStep();
		
		if(MemberDataList.get("CAC Complete And Upload Assessment Status").equals("True"))
		{
			//Launching CCA Application
			Report.log("Launching CCA Application", Status.BUSINESSSTEP);
			CCA.Launch(ValidationType.ASSERT);	//To launch CCA
			CCA.Login(ValidationType.ASSERT);	//To login CCA
			Report.closeBusinessStep();
			String Registrar=dp.get("AssessmentHistoryRegistrar", "Registrar");
			
			/*
			CCA-TestCase 
			-To Validate Upload And Complete Same Assessment In CCA Application
			*/
			Report.log("Validate Upload And Complete Same Assessment In CCA Application", Status.BUSINESSSTEP);
			com.CCA.pageobjects.TC04B_ValidateUploadAndCompleteSameAssessmentInCCA CCA_TC4B=new com.CCA.pageobjects.TC04B_ValidateUploadAndCompleteSameAssessmentInCCA(driver);		//Create constructor for CCA Testcase 1 pageobject 
			MemberDataList=CCA_TC4B.TestCase4B(ValidationType.VERIFY, dp, MemberDataList, Registrar);		
			Report.closeBusinessStep();
		}
	}
	@After
	public void teardown()
	{
		OpenCCA URL = new OpenCCA(driver);
		URL.closeApp();
	}
}
