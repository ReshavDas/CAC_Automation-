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

public class TC03_ValidateAssessmentPageComponentsAndItemsInCAC extends PageObject
{
	/*
	CAC-TestCase
	-To validate and capture Asthma Outcomes Assessment Page Components in CAC and compare CIA assessment components in both CCA and CAC application
	*/
	public TC03_ValidateAssessmentPageComponentsAndItemsInCAC(WebDriver adriver)
	{
		super(adriver);
	}
	public HashMap<String,String> TestCase3(ValidationType vtype, DataProvider dp, HashMap<String, String> MemberDataList) throws Exception
	{
		OpenCAC Find=new OpenCAC(driver);	//Constructor of OpenCAC class to load elements locator
		
		if(Find.element("Search_Members_TxtArea").isEnabled())			//Validate Search Members Area
		{
			Assert.assertTrue(vtype,"Search Members Area Is Enabled", Find.element("Search_Members_TxtArea").isEnabled());
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
									Report.log("Search Assigned Member Subscriber ID Not Present", Status.WARN);
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
										if(Find.element("Choose_Assessment_Header").getText().contains("Choose Assessment"))		//To check if choose Assessment Header is Correct
										{
											Assert.assertTrue(vtype,"Choose Assessment Header Text Is Matched As Expected", Find.element("Choose_Assessment_Header").getText().contains("Choose Assessment"));
											Report.log("Screenshot For Choose Assessment Dialog Box", Status.Pass);		//Screenshot
											Find.element("Search_Assessment_TxtArea").sendKeys("Asthma Outcomes Assessment");		//To enter Asthma Outcomes Assessment in Search Assessment Area
											if(Find.element("Search_Asthma Outcomes Assessment").getText().trim().equals("Asthma Outcomes Assessment"))		//To check if Asthma Outcomes Assessment appear correctly In search options
											{
												Assert.assertTrue(vtype,"Asthma Outcomes Assessment Is Available In CAC Application", Find.element("Search_Asthma Outcomes Assessment").getText().trim().equals("Asthma Outcomes Assessment"));
												Report.log("Screenshot For Search Asthma Outcomes Assessment In Dialog Box", Status.Pass);		//Screenshot
												Find.element("Search_Asthma Outcomes Assessment").click();		//To Click Asthma Outcomes Assessment Button in search options
												driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);	//Wait certain amount of time for next element or page to appear 
												if(Find.element("Asthma Outcomes Assessment_Header").getText().trim().equals("Asthma Outcomes Assessment"))		//To check if Asthma Outcomes Assessment Header appear correctly
												{
													Report.log("Screenshot For Asthma Outcomes Assessment Page", Status.Pass);		//Screenshot
													Assert.assertTrue(vtype,"Asthma Outcomes Assessment Header Is Matched As Expected", Find.element("Asthma Outcomes Assessment_Header").getText().trim().equals("Asthma Outcomes Assessment"));
													if(Find.element("Asthma Outcomes Assessment_Start").isEnabled())		//To check if Asthma Outcomes Assessment Start Button Enabled
													{
														Assert.assertTrue(vtype,"Asthma Outcomes Assessment Start Button Is Enabled", Find.element("Asthma Outcomes Assessment_Start").isEnabled());
														Find.element("Asthma Outcomes Assessment_Start").click();		//To Click Asthma Outcomes Assessment Start Button
														driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear
														Report.log("Screenshot For Start Asthma Outcomes Assessment Page", Status.Pass);		//Screenshot
														  //TakeAssessment takeAssessment = new TakeAssessment(driver);

												            // Specify the path to the input Excel file and the sheet name
												            //String filePath = "C:\\Input\\InputSheet.xlsx";
												           // String sheetName = "Data";

												            // Call the answerAssessment method to process the assessment
														PrintAssessmentOptions assessmentPrinter = new PrintAssessmentOptions(driver);
													        assessmentPrinter.fillAssessment();
														
														//To check Discard Button Enabled
														Assert.assertTrue(vtype,"Discard Button Is Enabled", Find.element("CAC_CIA_Discard_Button").isEnabled());
														Find.element("CAC_CIA_Discard_Button").click();		//To click on Discard Button
														Thread.sleep(3000);				//Give wait for few seconds till pop up displayed
														//Using Java script executor to find and perform action on element
														JavascriptExecutor je=(JavascriptExecutor) driver;
														WebElement Discard_Assessment= (WebElement) je.executeScript("return document.querySelector(\"#mat-mdc-dialog-1 > div > div > cac-discard-assessment-dialog > div.mat-mdc-dialog-actions.mdc-dialog__actions.mat-mdc-dialog-actions-align-center > button.mdc-button.mdc-button--outlined.mat-mdc-outlined-button.mat-warn.mat-mdc-button-base > span.mdc-button__label\")");
														Discard_Assessment.click();							//To click on Discard Button
														driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear 
													}
													else		//if Asthma Outcomes Assessment Start Button Is Not Enabled
													{
														Report.log("Asthma Outcomes Assessment Start Button Is Not Enabled", Status.Fail);
													}
												}
												else		//if Asthma Outcomes Assessment Header Is Not Matched
												{
													Report.log("Asthma Outcomes Assessment Header Is Not Matched", Status.Fail);
												}
											}
											else if(Find.element("No_Search_Asthma Outcomes Assessment").getText().trim().equals("No matching assessments"))
											{	
												Report.log("No Matching Assessment Is Available In CAC Application", Status.Fail);
											}
											else		//if Asthma Outcomes Assessment Is Not Available In CAC Application
											{
												Report.log("Asthma Outcomes Assessment Is Not Available In CAC Application", Status.Fail);
											}
										}
										else		//if Choose Assessment Header Text Is Not Matched
										{
											Report.log("Choose Assessment Header Text Is Not Matched", Status.Fail);
										}
									}
									else		//if Assessment Button Is Not Enabled
									{
										Report.log("Assessment Button Is Not Enabled", Status.Fail);
									}
								}
								else		//if Add Assessment Button Is Not Enabled
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
							else		//if Search Member Download Button Is Not Enabled
							{
								Report.log("Search Member Download Button Is Not Enabled", Status.Fail);
							}
						}
						catch(NoSuchElementException e)
						{
							if((Find.element("Assigned_Member_List").getText().trim().equals("All assigned members downloaded")))
							{
								Report.log("Search Assigned Member "+MemberDataList.get("Last_Name_For_CCA_Member_"+r)+" Is Not Present", Status.Fail);
							}
						}
					}
					else		//if Assigned Header Text Is Not Matched
					{
						Report.log("Assigned Header Text Is Not Matched", Status.Fail);
					}
				}
			}
			else		//if CCA Members Are Not Present In The My Work Assignment For Validation
			{
				Report.log("Warning: CCA Members Are Not Present In The My Work Assignment For Validation",Status.WARN);
			}
		}
		else		//if Search Members Area Is Not Enabled
		{
			Report.log("Search Members Area Is Not Enabled", Status.Fail);
		}
		return MemberDataList;
	}
	@Override
	public void validatePageExists(ValidationType vType) {
		// TODO Auto-generated method stub
		
	}
}