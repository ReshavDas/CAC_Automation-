package com.CAC.pageobjects;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import cts.qea.automation.Assert;
import cts.qea.automation.DataProvider;
import cts.qea.automation.PageObject;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC04_ValidateCompleteAndUploadSameAssessmentInCAC extends PageObject
{
	/*
	CAC-TestCase
	-To Validate Complete And Upload Same Assessment In CAC Application
	*/
	public TC04_ValidateCompleteAndUploadSameAssessmentInCAC(WebDriver adriver)
	{
		super(adriver);
	}
	public HashMap<String,String> TestCase4(ValidationType vtype, DataProvider dp, HashMap<String, String> MemberDataList) throws Exception
	{
		OpenCAC Find=new OpenCAC(driver);	//Constructor of OpenCAC class to load elements locator
		MemberDataList.put("CAC Complete And Upload Assessment Status", "True");
		if(Find.element("Search_Members_TxtArea").isEnabled())			//Validate Search Members Text Area
		{
			Assert.assertTrue(vtype,"Search Members Text Area Is Enabled", Find.element("Search_Members_TxtArea").isEnabled());
			//To check CCA members count greater than 0
			if(Integer.valueOf(MemberDataList.get("CCA_Members_Count"))>0)
			{
				for(int r=1;r<2;r++) 	//Iterate each CCA member counts and validate member information
				{	
					String Member_Name_FirstName="";
					String Member_Name_LastName="";
					String Member_Subscriber_ID="";
					String Member_Subscriber_Suffix="";
					String Member_Gender="";
					String Member_DOB="";
					String Member_City="";
					String Member_State="";
					Find.element("Search_Members_TxtArea").clear();
					Report.log("Screenshot For Search Members Text Area Is Empty", Status.Pass);		//For Screenshot
					Thread.sleep(2000);
					Find.element("Search_Members_TxtArea").sendKeys(MemberDataList.get("Last_Name_For_CCA_Member_"+r));	//Enter CCA member Name
					Report.log("Screenshot For Search Assigned Members In Text Area", Status.Pass);		//For Screenshot
					if(Find.element("Assigned_Header").getText().trim().equals("Assigned"))		//To check the Assigned Header Text
					{
						Report.log("Assigned Header Text Is Matched", Status.PASS);
						try 
						{
							if(Find.element("Assigned_Search_Member").isEnabled())			//Validate Assigned Search Member
							{
								Find.element("Assigned_Search_Member").click();		//Click Assigned Search Member
								Thread.sleep(5000);
								Report.log("Screenshot For Search Assigned Member Is Present", Status.Pass);		//For Screenshot
								
								//Fetching all CAC member info one by one in separate string
								Member_Name_FirstName=Find.element("Member_Name").getText().trim().substring(0, Find.element("Member_Name").getText().trim().indexOf(" "));
								Member_Name_LastName=Find.element("Member_Name").getText().trim().substring(Find.element("Member_Name").getText().trim().lastIndexOf(" ")+1);;
								Member_Gender=Find.element("Member_Gender").getText().trim().substring(0, 1);
								Member_DOB=Find.element("Member_DOB").getText().trim().substring(Find.element("Member_DOB").getText().trim().indexOf("(")+1, Find.element("Member_DOB").getText().trim().indexOf(")"));
								Member_City=Find.element("Member_Address").getText().trim().substring(Find.element("Member_Address").getText().trim().lastIndexOf("\n")+1, Find.element("Member_Address").getText().trim().lastIndexOf(","));
								Member_State=Find.element("Member_Address").getText().trim().substring(Find.element("Member_Address").getText().trim().lastIndexOf(",")+2, Find.element("Member_Address").getText().trim().lastIndexOf(" "));
								
								if(!(Find.element("Coverages").getText().trim().equals("Coverages (0)")))	//Validate Coverages section
								{
									Find.element("Coverages").click();		//Click on Coverages
									Thread.sleep(3000);
									Report.log("Screenshot For Search Assigned Member Subscriber ID Is Present", Status.Pass);
									//Fetching CAC member Subscriber ID info in separate string
									Member_Subscriber_ID=Find.element("Member_Subscriber_ID").getText().trim().substring(0, Find.element("Member_Subscriber_ID").getText().trim().indexOf("-"));
									Member_Subscriber_Suffix=Find.element("Member_Subscriber_ID").getText().trim().substring(Find.element("Member_Subscriber_ID").getText().trim().indexOf("-")+1);
								}
								else
								{
									Report.log("Search Assigned Member Subscriber ID Is Not Present", Status.WARN);
								}
								//Add capturing CAC member all data in Hashmap By Unique Names or Key
								MemberDataList.put("First_Name_For_CAC_Member_"+r, Member_Name_FirstName);
								MemberDataList.put("Last_Name_For_CAC_Member_"+r, Member_Name_LastName);
								MemberDataList.put("Subscriber_ID_For_CAC_Member_"+r, Member_Subscriber_ID);
								MemberDataList.put("Subscriber_Suffix_For_CAC_Member_"+r, Member_Subscriber_Suffix);
								MemberDataList.put("Gender_For_CAC_Member_"+r, Member_Gender);
								MemberDataList.put("Date_Of_Birth_For_CAC_Member_"+r, Member_DOB);
								MemberDataList.put("City_For_CAC_Member_"+r, Member_City);
								MemberDataList.put("State_For_CAC_Member_"+r, Member_State);
								
								//Printing Captured Data of CAC Member in Report log
								Report.log("CAC Member "+r+" Information:\n\nCAC Member First Name : "+MemberDataList.get("First_Name_For_CAC_Member_"+r)
								+"\nCAC Member Last Name : "+MemberDataList.get("Last_Name_For_CAC_Member_"+r)
								+"\nCAC Member Subscriber ID : "+MemberDataList.get("Subscriber_ID_For_CAC_Member_"+r)
								+"\nCAC Member Subscriber Suffix : "+MemberDataList.get("Subscriber_Suffix_For_CAC_Member_"+r)
								+"\nCAC Member Gender : "+MemberDataList.get("Gender_For_CAC_Member_"+r)
								+"\nCAC Member Date Of Birth : "+MemberDataList.get("Date_Of_Birth_For_CAC_Member_"+r)
								+"\nCAC Member City : "+MemberDataList.get("City_For_CAC_Member_"+r)
								+"\nCAC Member State : "+MemberDataList.get("State_For_CAC_Member_"+r), Status.DONE);
								
								//Comparing CCA and CAC Search Assigned Member Information
								if(MemberDataList.get("First_Name_For_CCA_Member_"+r).equals(MemberDataList.get("First_Name_For_CAC_Member_"+r)))
								{
									Report.log("First Name Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									Report.log("First Name Of The Member "+r+" Is Not Same", Status.Fail);
								}
								if(MemberDataList.get("Last_Name_For_CCA_Member_"+r).equals(MemberDataList.get("Last_Name_For_CAC_Member_"+r)))
								{
									Report.log("Last Name Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									Report.log("Last Name Of The Member "+r+" Is Not Same", Status.Fail);
								}
								if(MemberDataList.get("Subscriber_ID_For_CCA_Member_"+r).equals(MemberDataList.get("Subscriber_ID_For_CAC_Member_"+r)))
								{
									Report.log("Subscriber ID Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									if(MemberDataList.get("Subscriber_ID_For_CAC_Member_"+r).equals(""))
									{
										Report.log("Subscriber ID Of The CAC Member "+r+" Is Not Present", Status.WARN);
									}
									else
									{
										Report.log("Subscriber ID Of The Member "+r+" Is Not Same", Status.Fail);
									}
								}
								if(MemberDataList.get("Subscriber_Suffix_For_CCA_Member_"+r).equals(MemberDataList.get("Subscriber_Suffix_For_CAC_Member_"+r)))
								{
									Report.log("Subscriber Suffix Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									if(MemberDataList.get("Subscriber_Suffix_For_CAC_Member_"+r).equals(""))
									{
										Report.log("Subscriber Suffix Of The CAC Member "+r+" Is Not Present", Status.WARN);
									}
									else
									{
										Report.log("Subscriber Suffix Of The Member "+r+" Is Not Same", Status.Fail);
									}
								}
								if(MemberDataList.get("Gender_For_CCA_Member_"+r).equals(MemberDataList.get("Gender_For_CAC_Member_"+r)))
								{
									Report.log("Gender Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									Report.log("Gender Of The Member "+r+" Is Not Same", Status.Fail);
								}
								if(MemberDataList.get("Date_Of_Birth_For_CCA_Member_"+r).equals(MemberDataList.get("Date_Of_Birth_For_CAC_Member_"+r)))
								{
									Report.log("Date Of Birth Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									Report.log("Date Of Birth Of The Member "+r+" Is Not Same", Status.Fail);
								}
								if(MemberDataList.get("City_For_CCA_Member_"+r).equals(MemberDataList.get("City_For_CAC_Member_"+r)))
								{
									Report.log("City Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									Report.log("City Of The Member "+r+" Is Not Same", Status.Fail);
								}
								if(MemberDataList.get("State_For_CCA_Member_"+r).equals(MemberDataList.get("State_For_CAC_Member_"+r)))
								{
									Report.log("State Of The Member "+r+" Is The Same As Expected", Status.PASS);
								}
								else
								{
									Report.log("State Of The Member "+r+" Is Not Same", Status.Fail);
								}
							}
							
							Report.log("Screenshot For Before Click On Download Button", Status.Pass);		//Screenshot
							if(Find.element("Download_Search_Member").isEnabled())		//To check if Download Button Enabled
							{
								Assert.assertTrue(vtype,"Search Member Download Button Is Enabled", Find.element("Download_Search_Member").isEnabled());
								Find.element("Download_Search_Member").click();		//To Click Download Button
								//To Give Wait till member downloaded
								WebDriverWait wait=new WebDriverWait(driver, 1200);
								wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/div/button[2]/span[2]")));
								//To Check Remove Button Displayed
								Assert.assertTrue(vtype,"Remove Search Member Button Is Enabled", Find.element("Remove_Search_Member").isDisplayed());
								Report.log("Screenshot For After Click On Download Button", Status.Pass);		//Screenshot
								if(Find.element("Add_Assessment_Button").isEnabled())		//To check if Add Assessment Button Enabled
								{
									Assert.assertTrue(vtype,"Add Assessment Button Is Enabled", Find.element("Add_Assessment_Button").isEnabled());
									Find.element("Add_Assessment_Button").click();			//To Click Add Assessment Button
									if(Find.element("Assessment_Button").isEnabled())		//To check if Assessment Button Enabled
									{
										Assert.assertTrue(vtype,"Assessment Button Is Enabled", Find.element("Assessment_Button").isEnabled());
										Report.log("Screenshot For Before Click On Assessment Button", Status.Pass);		//Screenshot
										Find.element("Assessment_Button").click();			//To Click Assessment Button
										Thread.sleep(2000);
										if(Find.element("Choose_Assessment_Header").getText().trim().contains("Choose Assessment"))		//To check if choose Assessment Header is Correct
										{
											Assert.assertTrue(vtype,"Choose Assessment Header Text Is Matched As Expected", Find.element("Choose_Assessment_Header").getText().trim().contains("Choose Assessment"));
											Report.log("Screenshot For Choose Assessment Dialog Box", Status.Pass);		//Screenshot
											Find.element("Search_Assessment_TxtArea").sendKeys("CAD Initial Assessment");		//To enter CAD Initial Assessment in Search Assessment Text Area
											if(Find.element("Search_CAD_Initial_Assessment").getText().trim().equals("CAD Initial Assessment"))		//To check if CAD Initial Assessment appear correctly In search options
											{
												Assert.assertTrue(vtype,"CAD Initial Assessment Is Available In CAC Application", Find.element("Search_CAD_Initial_Assessment").getText().trim().equals("CAD Initial Assessment"));
												Report.log("Screenshot For Search CAD Initial Assessment In Dialog Box", Status.Pass);		//Screenshot
												Find.element("Search_CAD_Initial_Assessment").click();		//To Click CAD Initial Assessment Button in search options
												driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);	//Wait certain amount of time for next element or page to appear 
												if(Find.element("CAD_Initial_Assessment_Header").getText().trim().equals("CAD Initial Assessment"))		//To check if CAD Initial Assessment Header appear correctly
												{
													Report.log("Screenshot For CAD Initial Assessment Page", Status.Pass);		//Screenshot
													Assert.assertTrue(vtype,"CAD Initial Assessment Header Is Matched As Expected", Find.element("CAD_Initial_Assessment_Header").getText().trim().equals("CAD Initial Assessment"));
													if(Find.element("CAD_Initial_Assessment_Start").isEnabled())		//To check if CAD Initial Assessment Start Button Enabled
													{
														Assert.assertTrue(vtype,"CAD Initial Assessment Start Button Is Enabled", Find.element("CAD_Initial_Assessment_Start").isEnabled());
														Find.element("CAD_Initial_Assessment_Start").click();		//To Click CAD Initial Assessment Start Button
														driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear 
														Report.log("Screenshot For Start CAD Initial Assessment Page", Status.Pass);		//Screenshot
														
														if(Find.element("CAC_CIA_Disclosure").getText().trim().equals("Disclosure"))
														{	
															//To check CIA Disclosure Answers Enabled
															Assert.assertTrue(vtype,"CAC CIA Disclosure Question One Answer 'Yes' Is Enabled", Find.element("CAC_CIA_Disclosure_A1_RB1_lbl").isEnabled());		
															Assert.assertTrue(vtype,"CAC CIA Disclosure Question One Answer 'No' Is Enabled", Find.element("CAC_CIA_Disclosure_A1_RB2_lbl").isEnabled());		
															Assert.assertTrue(vtype,"CAC CIA Disclosure Question One Answer 'Not Required By Plan' Is Enabled", Find.element("CAC_CIA_Disclosure_A1_RB3_lbl").isEnabled());
															Find.element("CAC_CIA_Disclosure_A1_RB1_lbl").click();		//To select Answer Yes
															Thread.sleep(1000);
															Report.log("Screenshot For CAC CIA Disclosure Question One Answer 'Yes' Is Selected", Status.Pass);		//For Screenshot
															//Placeholder To Select Answer No
															//Placeholder To Select Answer Not Required By Plan
															
															//To Scroll till provided element
															driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear 
															WebElement Scroll1=driver.findElement(By.id("P1"));
															JavascriptExecutor je=(JavascriptExecutor) driver;				
															je.executeScript("arguments[0].scrollIntoView(true);", Scroll1);
															
															if(Find.element("CAC_CIA_Disease_Process").getText().trim().equals("Disease Process"))
															{
																Report.log("Screenshot For CAC CIA Disease Process Question One", Status.Pass);
																
																//To check CIA Disease_Process Q1 Answers Enabled
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Atherosclerosis' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB1_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Coronary artery disease (CAD)' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB2_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Hardening of the arteries' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB3_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Heart disease' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB4_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Ischemic heart disease' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB5_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Narrowing of the arteries' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB6_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Other' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB7_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'None' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB8_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Atrial fibrillation/arrhythmia' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB9_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question One Answer 'Hypertensive heart disease' Is Enabled", Find.element("CAC_CIA_Disease_Process_A1_CB10_lbl").isEnabled());
																
																//Placeholder To Select Answer Atherosclerosis	
																//Placeholder To Select Answer Coronary artery disease (CAD)
																Find.element("CAC_CIA_Disease_Process_A1_CB1_lbl").click();		//To select Answer Atherosclerosis
																Thread.sleep(1000);
																Report.log("Screenshot For CAC CIA Disease Process Question One Answer 'Atherosclerosis' Is Selected", Status.Pass);		//For Screenshot
																Find.element("CAC_CIA_Disease_Process_A1_CB2_lbl").click();		//To select Answer Coronary artery disease (CAD)
																Thread.sleep(1000);
																Report.log("Screenshot For CAC CIA Disease Process Question One Answer 'Coronary artery disease (CAD)' Is Selected", Status.Pass);		//For Screenshot
																//Placeholder To Select Answer Hardening of the arteries
																//Placeholder To Select Answer Heart disease
																//Placeholder To Select Answer Ischemic heart disease
																//Placeholder To Select Answer Narrowing of the arteries
																//Placeholder To Select Answer Other
																//Placeholder To Select Answer None
																//Placeholder To Select Answer Atrial fibrillation/arrhythmia
																//Placeholder To Select Answer Hypertensive heart disease
																
																//To Scroll till provided element
																driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																WebElement Scroll2=driver.findElement(By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[10]/div/label"));
																je.executeScript("arguments[0].scrollIntoView(true);", Scroll2);
																Report.log("Screenshot For CAC CIA Disease Process Question Two and Question Three", Status.Pass);
																//To check CIA Disease_Process Q2 Answers Enabled
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Two Text Area Is Enabled", Find.element("CAC_CIA_Disease_Process_A2_TxtArea").isEnabled());
																
																//Placeholder To Enter Text In Question Two Text Area
																Find.element("CAC_CIA_Disease_Process_A2_TxtArea").sendKeys("Testing1");			//Enter Some Text Eg: Testing1
																Report.log("Screenshot For CAC CIA Disease Process Question Two Text Area", Status.Pass);		//For Screenshot
																
																//To check CIA Disease_Process Q3 Answers Enabled
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Three Answer 'Yes, Hyperthyroidism' Is Enabled", Find.element("CAC_CIA_Disease_Process_A3_RB1_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Three Answer 'Yes, Hypothyroidism' Is Enabled", Find.element("CAC_CIA_Disease_Process_A3_RB2_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Three Answer 'No' Is Enabled", Find.element("CAC_CIA_Disease_Process_A3_RB3_lbl").isEnabled());
																
																//Placeholder To Select Answer Yes, Hyperthyroidism
																//Placeholder To Select Answer Yes, Hypothyroidism
																//Placeholder To Select Answer No
																Find.element("CAC_CIA_Disease_Process_A3_RB3_lbl").click();		//To select Answer No
																Thread.sleep(1000);
																Report.log("Screenshot For CAC CIA Disease Process Question Three Answer 'No' Is Selected", Status.Pass);		//For Screenshot
																
																//To Scroll till provided element
																driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																WebElement Scroll3=driver.findElement(By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[3]/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[3]/div/label"));
																je.executeScript("arguments[0].scrollIntoView(true);", Scroll3);
																Report.log("Screenshot For CAC CIA Disease Process Question Four", Status.Pass);
																//To check CIA Disease_Process Q4 Answers Enabled
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Four Answer 'Yes' Is Enabled", Find.element("CAC_CIA_Disease_Process_A4_RB1_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Four Answer 'No' Is Enabled", Find.element("CAC_CIA_Disease_Process_A4_RB2_lbl").isEnabled());
																
																//Placeholder To Select Answer Yes
																Find.element("CAC_CIA_Disease_Process_A4_RB1_lbl").click();		//To select Answer Yes
																Thread.sleep(1000);
																Report.log("Screenshot For CAC CIA Disease Process Question Four Answer 'Yes' Is Selected", Status.Pass);		//For Screenshot
																//Placeholder To Select Answer No
																
																//To Scroll till provided element
																driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																WebElement Scroll4=driver.findElement(By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[4]/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[2]/div/label"));
																je.executeScript("arguments[0].scrollIntoView(true);", Scroll4);
																Report.log("Screenshot For CAC CIA Disease Process Question Five", Status.Pass);
																
																//To check CIA Disease_Process Q5 Answers Enabled
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Following a heart healthy diet' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB1_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Maintaining a healthy weight' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB2_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Being physically active' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB3_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Stopping use of tobacco products' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB4_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Managing stress' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB5_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Use of medicine' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB6_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Cardiac procedures or surgery' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB7_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Cardiac rehabilitation' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB8_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Dental care including brushing twice daily,flossing, and regular check ups' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB9_lbl").isEnabled());
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Five Answer 'Other' Is Enabled", Find.element("CAC_CIA_Disease_Process_A5_CB10_lbl").isEnabled());
																
																//Placeholder To Select Answer Following a heart healthy diet
																Find.element("CAC_CIA_Disease_Process_A5_CB1_lbl").click();		//To select Answer Following a heart healthy diet
																Thread.sleep(1000);
																Report.log("Screenshot For CAC CIA Disease Process Question Five Answer 'Following a heart healthy diet' Is Selected", Status.Pass);		//For Screenshot
																//Placeholder To Select Answer Maintaining a healthy weight
																//Placeholder To Select Answer Being physically active
																//Placeholder To Select Answer Stopping use of tobacco products
																//Placeholder To Select Answer Managing stress
																//Placeholder To Select Answer Use of medicine
																//Placeholder To Select Answer Cardiac procedures or surgery
																//Placeholder To Select Answer Cardiac rehabilitation
																//Placeholder To Select Answer Dental care including brushing twice daily,flossing, and regular check ups
																//Placeholder To Select Answer Other
																
																//To Scroll till provided element
																driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																WebElement Scroll5=driver.findElement(By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[10]/div/label"));
																je.executeScript("arguments[0].scrollIntoView(true);", Scroll5);
																Report.log("Screenshot For CAC CIA Disease Process Question Six", Status.Pass);
																
																//To check CIA Disease_Process Q6 Answers Enabled
																Assert.assertTrue(vtype,"CAC CIA Disease Process Question Six Text Area Is Enabled", Find.element("CAC_CIA_Disease_Process_A6_TxtArea").isEnabled());
																
																//Placeholder To Enter Text In Question Six Text Area
																Find.element("CAC_CIA_Disease_Process_A6_TxtArea").sendKeys("Testing2");			//Enter Some Text Eg: Testing2
																Report.log("Screenshot For CAC CIA Disease Process Question Six Text Area", Status.Pass);		//For Screenshot
																
																//Placeholder To Update Components in Cardiac Procedures Section 
																//Placeholder To Update Components in Heart Attack, CABG, and Cardiac Rehab Section
																//Placeholder To Update Components in Family History Section 
																//Placeholder To Update Components in General Symptoms Section 
																//Placeholder To Update Components in Physical Activity Section 
																//Placeholder To Update Components in Medication Section 
																//Placeholder To Update Components in Medication List Section 
																//Placeholder To Update Components in Medication Understanding Section 
																//Placeholder To Update Components in Health Maintenance Section 
																//Placeholder To Update Components in Tobacco Usage/Exposure Section 
																//Placeholder To Update Components in Management of CAD Risk Factors Section 
																//Placeholder To Update Components in Diet Section 
																//Placeholder To Update Components in Managing Stress Section 
																//Placeholder To Update Components in Optional PHQ-2 PHQ-9 Assessment Section 
																//Placeholder To Update Components in Utilization Section 
																//Placeholder To Update Components in Optional Utilization of Hospitals and Urgent Cares Assessment Section 
																//Placeholder To Update Components in Program Consent Section 
																//Placeholder To Update Components in Mutuality/Self-Management Section 
																
																//To check Complete Button Enabled
																Assert.assertTrue(vtype,"Complete Button Is Enabled", Find.element("CAC_CIA_Complete_Button").isEnabled());
																Find.element("CAC_CIA_Complete_Button").click();		//To click on Discard Button
																Thread.sleep(3000);				//Give wait for few seconds till pop up displayed
																if(Find.element("CAC_CIA_Complete_Assessment_Header").getText().trim().equals("Complete Assessment"))		//To check if Complete Assessment appear correctly In Complete Assessment Dialog box
																{
																	Report.log("Complete Assessment Dialog Box Header Is Matched As Expected", Status.PASS);
																	if(Find.element("CAC_CIA_Complete_Assessment_Body").getText().trim().equals("Marking the assessment as complete will make it ready for upload to Care Advance."))		//To check if Complete Assessment Body Infomation appear correctly In Complete Assessment Dialog box
																	{
																		Report.log("Complete Assessment Dialog Box Information Is Matched As Expected", Status.Pass);
																		Assert.assertTrue(vtype,"Complete Button Assessment Is Enabled", Find.element("CAC_CIA_Complete_Assessment").isEnabled());
																		Find.element("CAC_CIA_Complete_Assessment").click();
																		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear \
																		
																		if(Find.element("MyWork").getText().trim().equals("My Work"))	//Validate My Work section
																		{
																			Report.log("Screenshot For CAD Initial Assessment Is Completed", Status.Pass);
																			//Capture Complete Assessment Date
		//																	MemberDataList.put("CAC_Complete_CIA_Assessment_Date", Find.element("CAC_Completed_CIA_Assessment_Date").getText().trim());
																			Assert.assertTrue(vtype,"Upload_Assessment Is Enabled", Find.element("Upload_Assessment").isEnabled());
																			Find.element("Upload_Assessment").click();		//Click on Upload Assessment
																			Thread.sleep(5000);
																			if(Find.element("Uploaded_Assessment").getAttribute("title").equals("uploaded"))		//If CAD Initial Assessment Is Uploaded
																			{
																				Report.log("Screenshot For CAD Initial Assessment Is Uploaded", Status.Pass);
																				Thread.sleep(3000);
																			}
																			else		//If CAD Initial Assessment Is Not Uploaded
																			{
																				Report.log("CAD Initial Assessment Is Not Uploaded", Status.Fail);
																			}
																		}
																		else		//If My Work Section Is Not Present
																		{
																			Report.log("My Work Section Is Not Present", Status.Fail);
																		}
																	}
																	else		//If Complete Assessment Dialog Box Information Is Not Matched
																	{
																		Report.log("Complete Assessment Dialog Box Information Is Not Matched", Status.Fail);
																	}
																}
																else		//If Complete Assessment Dialog Box Header Is Not Matched
																{
																	Report.log("Complete Assessment Dialog Box Header Is Not Matched", Status.Fail);
																}
															}
															else
															{
																Report.log("CAC CIA Disease Process Page Is Not Available Which Is Not Expected", Status.Fail);
																MemberDataList.put("CAC Complete And Upload Assessment Status", "False");
																//To check Discard Button Enabled
																Assert.assertTrue(vtype,"Discard Button Is Enabled", Find.element("CAC_CIA_Discard_Button").isEnabled());
																Find.element("CAC_CIA_Discard_Button").click();		//To click on Discard Button
																Thread.sleep(3000);				//Give wait for few seconds till pop up displayed
																//Using Java script executor to find and perform action on element
																JavascriptExecutor je1=(JavascriptExecutor) driver;	
																WebElement Discard_Assessment= (WebElement) je1.executeScript("return document.querySelector(\"#mat-mdc-dialog-1 > div > div > cac-discard-assessment-dialog > div.mat-mdc-dialog-actions.mdc-dialog__actions.mat-mdc-dialog-actions-align-center > button.mdc-button.mdc-button--outlined.mat-mdc-outlined-button.mat-warn.mat-mdc-button-base > span.mdc-button__label\")");
																Discard_Assessment.click();							//To click on Discard Button
																driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear 
															}
														}
														else
														{
															Report.log("CAC CIA Disclosure Page Is Not Available Which Is Not Expected", Status.Fail);
															MemberDataList.put("CAC Complete And Upload Assessment Status", "False");
															//To check Discard Button Enabled
															Assert.assertTrue(vtype,"Discard Button Is Enabled", Find.element("CAC_CIA_Discard_Button").isEnabled());
															Find.element("CAC_CIA_Discard_Button").click();		//To click on Discard Button
															Thread.sleep(3000);				//Give wait for few seconds till pop up displayed
															//Using Java script executor to find and perform action on element
															JavascriptExecutor je2=(JavascriptExecutor) driver;	
															WebElement Discard_Assessment= (WebElement) je2.executeScript("return document.querySelector(\"#mat-mdc-dialog-1 > div > div > cac-discard-assessment-dialog > div.mat-mdc-dialog-actions.mdc-dialog__actions.mat-mdc-dialog-actions-align-center > button.mdc-button.mdc-button--outlined.mat-mdc-outlined-button.mat-warn.mat-mdc-button-base > span.mdc-button__label\")");
															Discard_Assessment.click();							//To click on Discard Button
															driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear 
														}
													}
													else		//If CAD Initial Assessment Start Button Is Not Enabled
													{
														Report.log("CAD Initial Assessment Start Button Is Not Enabled", Status.Fail);
													}
												}
												else		//If CAD Initial Assessment Header Is Not Matched
												{
													Report.log("CAD Initial Assessment Header Is Not Matched", Status.Fail);
												}
											}
											else if(Find.element("No_Search_CAD_Initial_Assessment").getText().trim().equals("No matching assessments"))
											{	
												Report.log("No Matching Assessments Is Available In CAC Application", Status.Fail);
											}
											else		//If CAD Initial Assessment Is Not Available In CAC Application
											{
												Report.log("CAD Initial Assessment Is Not Available In CAC Application", Status.Fail);
											}
										}
										else		//If Choose Assessment Header Text Is Not Matched
										{
											Report.log("Choose Assessment Header Text Is Not Matched", Status.Fail);
										}
									}
									else		//If Assessment Button Is Not Enabled
									{
										Report.log("Assessment Button Is Not Enabled", Status.Fail);
									}
								}
								else		//If Add Assessment Button Is Not Enabled
								{
									Report.log("Add Assessment Button Is Not Enabled", Status.Fail);
								}
								if(Find.element("Remove_Search_Member").isEnabled())		//If Remove Search Member Button Is Enabled
								{
									Assert.assertTrue(vtype,"Remove Search Member Button Is Enabled", Find.element("Remove_Search_Member").isEnabled());
									Find.element("Remove_Search_Member").click();			//To click on Remove Button
									Thread.sleep(10000);
								}
								else		//If Remove Search Member Button Is Not Enabled
								{
									Report.log("Remove Search Member Button Is Not Enabled", Status.Fail);
								}
							}
							else		//If Search Member Download Button Is Not Enabled
							{
								Report.log("Search Member Download Button Is Not Enabled", Status.Fail);
							}
						}
						catch(NoSuchElementException e)
						{
							if((Find.element("Assigned_Member_List").getText().trim().equals("All assigned members downloaded")))
							{
								Report.log("Search Assigned Member "+MemberDataList.get("Last_Name_For_CCA_Member_"+r)+" Not Present", Status.Fail);
							}
						}
					}
					else		//If Assigned Header Text Is Not Matched
					{
						Report.log("Assigned Header Text Is Not Matched", Status.Fail);
					}
				}
			}
			else		//If CCA Members Are Not Present In The My Work Assignment For Validation
			{
				Report.log("Warning: CCA Members Are Not Present In The My Work Assignment For Validation",Status.WARN);
			}
		}
		else		//If Search Members Text Area Is Not Enabled
		{
			Report.log("Search Members Text Area Is Not Enabled", Status.Fail);
		}
		return MemberDataList;
	}
	@Override
	public void validatePageExists(ValidationType vType) {
		// TODO Auto-generated method stub
		
	}
}