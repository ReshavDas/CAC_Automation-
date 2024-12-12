package com.CCA.pageobjects;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import cts.qea.automation.Assert;
import cts.qea.automation.DataProvider;
import cts.qea.automation.PageObject;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC04B_ValidateUploadAndCompleteSameAssessmentInCCA extends PageObject
{
	/*
	CCA-TestCase 
	-To Validate Upload And Complete Same Assessment In CCA Application
	*/
	public TC04B_ValidateUploadAndCompleteSameAssessmentInCCA(WebDriver bdriver)
	{
		super(bdriver);
	}
	public HashMap<String,String> TestCase4B(ValidationType vtype, DataProvider dp, HashMap<String, String> MemberDataList, String Registrar) throws Exception
	{
		OpenCCA Find=new OpenCCA(driver);	//Constructor of OpenCCA class to load elements locator
		boolean FirstNameFound=false;
		int First_Name=0;
		boolean StatusHeaderFound=false;
		int Status_Header=0;
		boolean RegistrarHeaderFound=false;
		int Registrar_Header=0;
		boolean SourceHeaderFound=false;
		int Source_Header=0;
		boolean LastUpdatedDateHeaderFound=false;
//		int Last_Updated_Date_Header=0;
		String CIA_History_Status="";
		String CIA_History_Registrar="";
		String CIA_History_Source="";
//		String CIA_History_Last_Updated_Date="";
		if(Find.element("My_Work_Assignment_Icon").isEnabled())			//Validate My Member Assignment
		{
			Assert.assertTrue(vtype,"My Work Assignment Icon Is Enabled", Find.element("My_Work_Assignment_Icon").isEnabled());
			Find.element("My_Work_Assignment_Icon").click();	//Click on My Member Assignment
			driver.switchTo().frame("RightPanel");			//Navigate Right side frame in CCA application to validate My Work Assignment Page
			if(Find.element("My_Work_Assignment_Page_Header").getText().trim().equals("My Work Assignments"))
			{
				Report.log("My Work Assignment Page Header Is Matched As Expected", Status.PASS);
				WebElement table=Find.element("My_Work_Assignment_Table_Header");		//Validate Member Header table 
				Thread.sleep(1000);
				List<WebElement> rows=table.findElements(By.tagName("tr"));		//Iterate rows
				Thread.sleep(1000);
				List<WebElement> cols=rows.get(0).findElements(By.tagName("th"));	//Iterate columns
				Br:
				for(int FirstNameHeader=0;FirstNameHeader<cols.size();FirstNameHeader++)        //Iterate Member Table Columns
				{
					if(cols.get(FirstNameHeader).getText().trim().equals("First Name"))   //Verify for the column Header- First Name
					{
						FirstNameFound=true;
						First_Name=FirstNameHeader;
						break Br;
					}
				}
				if(FirstNameFound==true)		//To check If First Name Header found
				{
					Report.log("First Name Column Is Present In Table", Status.DONE);
					WebElement table1=Find.element("My_Work_Assignment_Table_Data");	//Validate Member Information table
					Thread.sleep(1000);
					List<WebElement> rows1=table1.findElements(By.tagName("tr"));		//Iterate rows
					Thread.sleep(1000);
					if(rows1.size()>0)			//To check if members are present in a table
					{	
						Report.log("Screenshot For "+(rows1.size()-1)+" Members Are Present In CCA Application", Status.Pass);	//Screenshot
						for(int r=1;r<2;r++)     //Iteration for all rows in one Table one by one
						{
							driver.manage().timeouts().implicitlyWait(250, TimeUnit.SECONDS);
							List<WebElement> cols1=rows1.get(r).findElements(By.tagName("td"));		
							for(int k=0;k<cols1.size();k++)			//Iteration for all columns in one row one by one
							{
								if(k==First_Name)
								{
									Thread.sleep(2000);
									cols1.get(k).click();	//click on first row first member
																		
									driver.switchTo().parentFrame();		//Switch Back to the Parent Frame
									driver.switchTo().frame(0);		//Switch to Top Frame of CCA Homepage
									if(Find.element("CCA_Assessment_Icon").isEnabled())
									{
										Assert.assertTrue(vtype,"Assessment Icon Is Enabled", Find.element("CCA_Assessment_Icon").isEnabled());		//To check if Assessment Icon Enabled
										Find.element("CCA_Assessment_Icon").click();				//To Click Assessment Icon Button
										//To navigate new browser tab 
										Set<String> windowHandles= driver.getWindowHandles();
										for(String string : windowHandles)
										{
											driver.switchTo().window(string);			//Switch to new browser tab
										}
										driver.manage().window().maximize();		//To maximize new browser tab
										if(driver.getTitle().trim().equals(MemberDataList.get("Last_Name_For_CCA_Member_"+r)+", "+MemberDataList.get("First_Name_For_CCA_Member_"+r)+" - Assessments"))		//To validate new browser tab title
										{
											Assert.assertTrue(vtype,"CCA Assessment Page Title Is Matched", driver.getTitle().trim().equals(MemberDataList.get("Last_Name_For_CCA_Member_"+r)+", "+MemberDataList.get("First_Name_For_CCA_Member_"+r)+" - Assessments"));
											if(Find.element("CCA_Assessment_Header").getText().trim().equals("Assessments"))					//To check if CCA Assessment Header is Correct	
											{
												Assert.assertTrue(vtype,"CCA Assessment Page Header Is Matched", Find.element("CCA_Assessment_Header").getText().trim().equals("Assessments"));
												Find.element("CCA_Search_Assessment_Name").sendKeys("CAD Initial Assessment");			//To enter CAD Initial Assessment in CCA Search Assessment Name
												//Used Robot class to perform keyboard action like press enter
												Robot robot=new Robot();
												robot.keyPress(KeyEvent.VK_ENTER);
												robot.keyRelease(KeyEvent.VK_ENTER);
												Thread.sleep(5000);		//Hold script for 5 seconds
												
												if(Find.element("CCA_Select_CIA_Assessment").isEnabled())		//To check CCA CAD Initial Assessment Is Available
												{
													Report.log("Screenshot For CCA CAD Initial Assessment Is Available", Status.Pass);
													Find.element("CCA_Select_CIA_Assessment").click();						//To select Available CAD Initial Assessment
													if(Find.element("CCA_View_History_Button").isEnabled())					//To check if View History Button Enabled
													{
														Assert.assertTrue(vtype,"CCA View History Button Is Enabled", Find.element("CCA_View_History_Button").isEnabled());	
														Find.element("CCA_View_History_Button").click();					//To Click View History Button
														
														WebElement table2=Find.element("CAD_Initial_Assessment_History_Table");	//Validate CAD Initial Assessments History table
														Thread.sleep(1000);
														List<WebElement> rows2=table2.findElements(By.tagName("tr"));		//Iterate rows
														Thread.sleep(1000);
														List<WebElement> cols2=rows2.get(0).findElements(By.tagName("th"));	//Iterate columns
														for(int Header=0;Header<cols2.size();Header++)        //Iterate Member Table Columns
														{
															if(cols2.get(Header).getText().trim().equals("STATUS"))   //Verify for the column Header- STATUS
															{
																StatusHeaderFound=true;
																Status_Header=Header;
															}
															if(cols2.get(Header).getText().trim().equals("REGISTRAR"))   //Verify for the column Header- REGISTRAR
															{
																RegistrarHeaderFound=true;	
																Registrar_Header=Header;
															}
															if(cols2.get(Header).getText().trim().equals("SOURCE"))   //Verify for the column Header- SOURCE
															{
																SourceHeaderFound=true;
																Source_Header=Header;
															}
															if(cols2.get(Header).getText().trim().equals("LAST UPDATED"))   //Verify for the column Header- SOURCE
															{
																LastUpdatedDateHeaderFound=true;
//																Last_Updated_Date_Header=Header;
															}
														}
														if(StatusHeaderFound==true)		//To check if column Header- STATUS Found
														{
															if(RegistrarHeaderFound==true)		//To check if column Header- REGISTRAR Found
															{
																if(SourceHeaderFound==true)		//To check if column Header- SOURCE Found
																{
																	if(LastUpdatedDateHeaderFound==true)		//To check if column Header- LAST UPDATED Found
																	{
																		if(rows2.size()>1)			//To check if Assessment History Is present in View History table
																		{
																			Report.log("Screenshot For Assessment History Is Present In View History Table", Status.Pass);
																			for(int R=1;R<2;R++)     //Iteration for all rows in one Table one by one
																			{
																				Thread.sleep(1000);
																				List<WebElement> cols3=rows2.get(R).findElements(By.tagName("td"));		
																				for(int K=0;K<cols3.size();K++)			//Iteration for all columns in one row one by one
																				{
																					if(K==Status_Header)
																					{
																						CIA_History_Status=cols3.get(K).getText().trim();				//To capture Assessment Status
																					}
																					if(K==Registrar_Header)
																					{
																						CIA_History_Registrar=cols3.get(K).getText().trim();			//To capture Assessment Registrar
																					}
																					if(K==Source_Header)
																					{
																						CIA_History_Source=cols3.get(K).getText().trim();				//To capture Assessment Source
																					}
//																					if(K==Last_Updated_Date_Header)
//																					{
//																						CIA_History_Last_Updated_Date=cols3.get(K).getText().trim().substring(0, cols3.get(K).getText().trim().indexOf(" "));				//To capture Assessment Source
//																					}
																				}
																			}
																			if((CIA_History_Status.equals("Completed"))&&(CIA_History_Registrar.equals(Registrar))&&(CIA_History_Source.equals("TriZetto CareAdvance Connect")))		//To check if Status, Registrar and Source Are Matched As expected
																			{
																				Report.log("CAD Initial Assessment Status, Registrar And Source Are Correct As Expected\nStatus : "+CIA_History_Status+"\nRegistrar : "+CIA_History_Registrar+"\nSource : "+CIA_History_Source,Status.Pass);
																				if(Find.element("Go_To_Assessment_List_Button").isEnabled())		//To check if Go To Assessment List Button Enabled
																				{
																					Assert.assertTrue(vtype,"Go To Assessment List Button Is Enabled", Find.element("Go_To_Assessment_List_Button").isEnabled());
																					Find.element("Go_To_Assessment_List_Button").click();					//To click Go To Assessment List Button
																					driver.manage().timeouts().implicitlyWait(250, TimeUnit.SECONDS);
																					if(Find.element("CCA_ReTake_Assessment_Button").isEnabled())			//To check if ReTake Assessment Button Enabled
																					{
																						Report.log("ReTake Assessment Button Is Enabled", Status.PASS);
																						Find.element("CCA_ReTake_Assessment_Button").click();				//To Click ReTake Assessment Button
																						
																						if(Find.element("CCA_CIA_Assessment_Page_Header").getText().trim().equals("CAD Initial Assessment"))		//To check if CAD Initial Assessment Page Header is Correct	
																						{
																							Report.log("CCA CAD Initial Assessment Page Header Is Matched", Status.Pass);
																							
																							driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear 
																							
																							if(Find.element("CCA_CIA_Disclosure").getText().trim().equals("Disclosure"))
																							{
																								Report.log("Screenshot For CCA CIA Disclosure Question One", Status.Pass);		//For Screenshot
																								//To check CIA Disclosure Answers 
																								Assert.assertTrue(vtype,"CCA CIA Disclosure Question One Answer 'Yes' Is Selected As Expected From CAC", Find.element("CCA_CIA_Disclosure_A1_RB1").isSelected());		
																								Assert.assertFalse(vtype,"CCA CIA Disclosure Question One Answer 'No' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disclosure_A1_RB2").isSelected());		
																								Assert.assertFalse(vtype,"CCA CIA Disclosure Question One Answer 'Not Required By Plan' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disclosure_A1_RB3").isSelected());															
																								
																								Assert.assertTrue(vtype,"CCA CIA Continue Button Is Enabled", Find.element("CCA_CIA_Continue_Button").isEnabled());			//To check if Continue Button Enabled
																								Find.element("CCA_CIA_Continue_Button").click();			//To Click Continue Button
																								driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
																								
																								if(Find.element("CCA_CIA_Disease_Process").getText().trim().equals("Disease Process"))
																								{	
																									Report.log("Screenshot For CCA CIA Disease Process Question One and Question Two", Status.Pass);			//Screenshot
																									
																									//To check CIA Disease_Process Q1 Answers 
																									Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Atherosclerosis' Is Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB1").isSelected());	
																									Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Coronary artery disease (CAD)' Is Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB2").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'Hardening of the arteries' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB3").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'Heart disease' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB4").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'Ischemic heart disease' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB5").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'Narrowing of the arteries' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB6").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'Other' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB7").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'None' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB8").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'Atrial fibrillation/arrhythmia' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB9").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question One Answer 'Hypertensive heart disease' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A1_CB10").isSelected());	
																									
																									//To check CIA Disease_Process Q2 Answers Enabled
																									Assert.assertTrue(vtype,"CCA CIA Disease Process Question Two Text Area Is Enabled", Find.element("CCA_CIA_Disease_Process_A2_TxtArea").isEnabled());
																									//To Enter Text In Question Two Text Area
																									if(Find.element("CCA_CIA_Disease_Process_A2_TxtArea").getAttribute("value").equals("Testing1"))
																									{
																										Report.log("CCA CIA Disease Process Question Two Text Area Data Is Matched As Expected From CAC: "+Find.element("CCA_CIA_Disease_Process_A2_TxtArea").getAttribute("value"), Status.PASS);
																									}
																									else
																									{
																										Report.log("CCA CIA Disease Process Question Two Text Area Data Is Not Matched", Status.Fail);
																									}
																									
																									//To Scroll till provided element locator
																									driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																									WebElement Scroll1=driver.findElement(By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_79_ctrlQuestion_txt_79"));
																									JavascriptExecutor je=(JavascriptExecutor) driver;	
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll1);
																									Report.log("Screenshot For CCA CIA Disease Process Question Three and Question Four", Status.Pass);
																									
																									//To check CIA Disease_Process Q3 Answers 
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Three Answer 'Yes, Hyperthyroidism' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A3_RB1").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Three Answer 'Yes, Hypothyroidism' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A3_RB2").isSelected());	
																									Assert.assertTrue(vtype,"CCA CIA Disease Process Question Three Answer 'No' Is Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A3_RB3").isSelected());	
																									
																									//To check CIA Disease_Process Q4 Answers 
																									Assert.assertTrue(vtype,"CCA CIA Disease Process Question Four Answer 'Yes' Is Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A4_RB1").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Four Answer 'No' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A4_RB2").isSelected());	
																									
																									//To Scroll till provided element locator
																									driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																									WebElement Scroll2=driver.findElement(By.id("cphBody_cphButtonsBottom_btnContinue2"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll2);
																									Report.log("Screenshot For CCA CIA Disease Process Question Five and Question Six", Status.Pass);
																									
																									//To check CIA Disease_Process Q5 Answers 
																									Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Following a heart healthy diet' Is Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB1").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Maintaining a healthy weight' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB2").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Being physically active' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB3").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Stopping use of tobacco products' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB4").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Managing stress' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB5").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Use of medicine' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB6").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Cardiac procedures or surgery' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB7").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Cardiac rehabilitation' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB8").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Dental care including brushing twice daily,flossing, and regular check ups' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB9").isSelected());	
																									Assert.assertFalse(vtype,"CCA CIA Disease Process Question Five Answer 'Other' Is Not Selected As Expected From CAC", Find.element("CCA_CIA_Disease_Process_A5_CB10").isSelected());	
																									
																									//To check CIA Disease_Process Q6 Answers Enabled
																									Assert.assertTrue(vtype,"CCA CIA Disease Process Question Six Text Area Is Enabled", Find.element("CCA_CIA_Disease_Process_A6_TxtArea").isEnabled());
																									//To Enter Text In Question Six Text Area
																									if(Find.element("CCA_CIA_Disease_Process_A6_TxtArea").getAttribute("value").equals("Testing2"))
																									{
																										Report.log("CCA CIA Disease Process Question Six Text Area Data Is Matched As Expected As Expected From CAC: "+Find.element("CCA_CIA_Disease_Process_A6_TxtArea").getAttribute("value"), Status.PASS);
																									}
																									else
																									{
																										Report.log("CCA CIA Disease Process Question Six Text Area Data Is Not Matched", Status.Fail);
																									}
																									
																									//To Click On Continue Button In Disease Process Section Page
																									WebElement Scroll3=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll3);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Cardiac Procedures Section 
																									
																									//To Click On Continue Button In Cardiac Procedures Section Page
																									Report.log("Screenshot For Cardiac Procedures Page", Status.Pass);
																									WebElement Scroll4=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));	
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll4);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Heart Attack, CABG, and Cardiac Rehab Section
																									
																									//To Click On Continue Button In Heart Attack, CABG, and Cardiac Rehab Section Page
																									Report.log("Screenshot For Heart Attack, CABG, and Cardiac Rehab Page", Status.Pass);
																									WebElement Scroll5=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll5);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Family History Section 
																									
																									//To Click On Continue Button In Family History Section Page
																									Report.log("Screenshot For Family History Page", Status.Pass);
																									WebElement Scroll6=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll6);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Symptoms Specific to Females Section 
																									
																									//To Click On Continue Button In Symptoms Specific to Females Section Page
																									Report.log("Screenshot For Symptoms Specific to Females Page", Status.Pass);
																									WebElement Scroll7=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll7);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in General Symptoms Section 
																									
																									//To Click On Continue Button In General Symptoms Section Page
																									Report.log("Screenshot For General Symptoms Page", Status.Pass);
																									WebElement Scroll8=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll8);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Physical Activity Section 
																									
																									//To Click On Continue Button In Physical Activity Section Page
																									Report.log("Screenshot For Physical Activity Page", Status.Pass);
																									WebElement Scroll9=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll9);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Medication Section 
																									
																									//To Click On Continue Button In Medication Section Page
																									Report.log("Screenshot For Medication Page", Status.Pass);
																									WebElement Scroll10=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll10);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Medication List Section 
																									
																									//To Click On Continue Button In Medication List Section Page
																									Report.log("Screenshot For Medication List Page", Status.Pass);
																									Find.element("CCA_CIA_Continue_Assessment_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);					//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Medication Understanding Section 
																									
																									//To Click On Continue Button In Medication Understanding Section Page
																									Report.log("Screenshot For Medication Understanding Page", Status.Pass);
																									WebElement Scroll11=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll11);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Health Maintenance Section 
																									
																									//To Click On Continue Button In Health Maintenance Section Page
																									Report.log("Screenshot For Health Maintenance Page", Status.Pass);
																									WebElement Scroll12=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll12);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Tobacco Usage/Exposure Section
																									
																									//To Click On Continue Button In Tobacco Usage/Exposure Section Page
																									Report.log("Screenshot For Tobacco Usage/Exposure Page", Status.Pass);
																									WebElement Scroll13=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll13);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Management of CAD Risk Factors Section 
																									
																									//To Click On Continue Button In Management of CAD Risk Factors Section Page
																									Report.log("Screenshot For Management of CAD Risk Factors Page", Status.Pass);
																									WebElement Scroll14=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll14);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Diet Section 
																									
																									//To Click On Continue Button In Diet Section Page
																									Report.log("Screenshot For Diet Page", Status.Pass);
																									WebElement Scroll15=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll15);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Managing Stress Section 
																									
																									//To Click On Continue Button In Managing Stress Section Page
																									Report.log("Screenshot For Managing Stress Page", Status.Pass);
																									WebElement Scroll16=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll16);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Optional PHQ-2 PHQ-9 Assessment Section 
																									
																									//To Click On Continue Button In Optional PHQ-2 PHQ-9 Assessment Section Page
																									Report.log("Screenshot For Optional PHQ-2 PHQ-9 Assessment Page", Status.Pass);
																									WebElement Scroll17=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll17);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Utilization Section 
																									
																									//To Click On Continue Button In Utilization Section Page
																									Report.log("Screenshot For Utilization Page", Status.Pass);
																									WebElement Scroll18=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll18);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);						//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Optional Utilization of Hospitals and Urgent Cares Assessment Section
																									
																									//To Click On Continue Button In Optional Utilization of Hospitals and Urgent Cares Assessment Section Page
																									Report.log("Screenshot For Optional Utilization of Hospitals and Urgent Cares Assessment Page", Status.Pass);
																									WebElement Scroll19=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll19);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Program Consent Section 
																									
																									//To Click On Continue Button In Program Consent Section Page
																									Report.log("Screenshot For Program Consent Page", Status.Pass);
																									WebElement Scroll20=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll20);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);						//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Mutuality/Self-Management Section
																									
																									//To Click On Continue Button In Mutuality/Self-Management Section Page
																									Report.log("Screenshot For Mutuality/Self-Management Page", Status.Pass);
																									WebElement Scroll21=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll21);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									//Placeholder To Validate Components in Final Section 
																									
																									//To Click On Continue Button In Final Section Page
																									Report.log("Screenshot For Final Page", Status.Pass);
																									WebElement Scroll22=driver.findElement(By.id("cphBody_cphButtonsTop_btnContinue"));
																									je.executeScript("arguments[0].scrollIntoView(true);", Scroll22);
																									Find.element("CCA_CIA_Continue_Button").click();
																									driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);				//Wait certain amount of time for next element or page to appear 
																									
																									if(Find.element("CCA_CIA_Assessment_Status").getText().trim().equals("Completed"))	//To check CCA CAD Initial Assessment Is Completed
																									{
																										Report.log("Screenshot For CCA CAD Initial Assessment Is Completed", Status.Pass);
																									}
																									else
																									{
																										Report.log("Screenshot For CCA Completed CAD Initial Assessment Is Not Completed", Status.Fail);
																									}
																								}
																								else
																								{
																									Report.log("CCA CIA Disease Process Page Is Not Available Which Is Not Expected", Status.Fail);	
																									//To check Abort Button Enabled
																									Assert.assertTrue(vtype,"Abort Button Is Enabled", Find.element("CCA_CIA_Abort_Button").isEnabled());
																									Find.element("CCA_CIA_Abort_Button").click();		//To click on Discard Button
																									Thread.sleep(3000);				//Give wait for few seconds till pop up displayed
																								}
																							}
																							else
																							{
																								Report.log("CCA CIA Disclosure Page Is Not Available Which Is Not Expected", Status.Fail);	
																								//To check Abort Button Enabled
																								Assert.assertTrue(vtype,"Abort Button Is Enabled", Find.element("CCA_CIA_Abort_Button").isEnabled());
																								Find.element("CCA_CIA_Abort_Button").click();		//To click on Discard Button
																								Thread.sleep(3000);				//Give wait for few seconds till pop up displayed
																							}
																						}
																						else		//If CCA CAD Initial Assessment Page Header Is Not Matched
																						{
																							Report.log("CCA CAD Initial Assessment Page Header Is Not Matched", Status.Fail);
																						}
																					}
																					else		//If ReTake Assessment Button Is Not Enabled
																					{
																						Report.log("ReTake Assessment Button Is Not Enabled", Status.Fail);
																					}
																				}
																				else		//If Go To Assessment List Button Is Not Enabled
																				{
																					Report.log("Go To Assessment List Button Is Not Enabled", Status.Fail);
																				}
																			}
																			else		//If CAD Initial Assessment Status, Registrar And Source Are Incorrect
																			{
																				Report.log("CAD Initial Assessment Status, Registrar And Source Are Incorrect",Status.Fail);
																			}
																		}
																		else		//If CAD Initial Assessment History Is Not Present In View History Table
																		{
																			Report.log("CAD Initial Assessment History Is Not Present In View History Table",Status.Fail);
																		}
																	}
																	else		//If Last Updated Column Is Not Present In View History Table
																	{
																		Report.log("Last Updated Column Is Not Present In View History Table", Status.Fail);
																	}
																}
																else		//If Source Column Is Not Present In View History Table
																{
																	Report.log("Source Column Is Not Present In View History Table", Status.Fail);
																}
															}
															else		//If Registrar Column Is Not Present In View History Table
															{
																Report.log("Registrar Column Is Not Present In View History Table", Status.Fail);
															}
														}
														else		//If Status Column Is Not Present In View History Table
														{
															Report.log("Status Column Is Not Present In View History Table", Status.Fail);
														}
													}
													else		//If CCA View History Button Is Not Enabled
													{
														Report.log("CCA View History Button Is Not Enabled", Status.Fail);
													}
												}
												else		//If CCA CAD Initial Assessment Is Not Available
												{
													Report.log("CCA CAD Initial Assessment Is Not Available", Status.Fail);
												}
											}
											else		//If CCA Assessment Page Header Is Not Matched
											{
												Report.log("CCA Assessment Page Header Is Not Matched", Status.Fail);
											}
										}
										else		//If CCA Assessment Page Title Is Not Matched
										{
											Report.log("CCA Assessment Page Title Is Not Matched", Status.Fail);
										}
									}
									else		//If Assessment Icon Is Not Enabled
									{
										Report.log("Assessment Icon Is Not Enabled", Status.Fail);
									}
									
								}
							}
						}	
					}
					else			//If Members Are Not Present In The My Work Assignment For Validation
					{
						Report.log("Warning: Members Are Not Present In The My Work Assignment For Validation",Status.WARN);
					}
				}
				else		//If First Name Column Is Not Present In My Work Assignment Table
				{
					Report.log("First Name Column Is Not Present In My Work Assignment Table", Status.Fail);
				}
			}
			else		//If My Work Assignment Page Header Is Not Matched
			{
				Report.log("My Work Assignment Page Header Is Not Matched", Status.Fail);
			}
		}
		else		//If My Work Assignment Icon Is Not Enabled
		{
			Report.log("My Work Assignment Icon Is Not Enabled", Status.Fail);
		}
		return MemberDataList;		//Return Collection Hashmap with all member necessary data
	}
	@Override
	public void validatePageExists(ValidationType vType) {
		// TODO Auto-generated method stub
		
	}
}