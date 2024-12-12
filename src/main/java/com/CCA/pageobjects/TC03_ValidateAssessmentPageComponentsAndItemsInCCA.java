package com.CCA.pageobjects;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import cts.qea.automation.Assert;
import cts.qea.automation.DataProvider;
import cts.qea.automation.PageObject;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC03_ValidateAssessmentPageComponentsAndItemsInCCA extends PageObject
{
	/*
	CCA TestCase Definition
	-To validate and capture CAD Initial Assessment Page Components in CCA
	*/
	public TC03_ValidateAssessmentPageComponentsAndItemsInCCA(WebDriver bdriver)
	{
		super(bdriver);
	}
	public HashMap<String,String> TestCase3(ValidationType vtype, DataProvider dp) throws Exception
	{
		OpenCCA Find=new OpenCCA(driver);	//Constructor of OpenCCA class to load elements locator
		boolean FirstNameFound=false;
		int First_Name=0;
		HashMap<String, String> MemberDataList=new HashMap<String,String>();	//Using Hashmap collection to get all member information with seperate names
		
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
						MemberDataList.put("CCA_Members_Count", String.valueOf(rows1.size()));
						for(int r=1;r<2;r++)     		//Iteration for all rows in one Table one by one
						{
							String MemberTableData[]= null;			//To store member information from Hover Table of CCA
							String Member_Name_FirstName="";
							String Member_Name_LastName="";
							String Member_Subscriber_ID="";
							String Member_Subscriber_Suffix="";
							String Member_Relationship="";
							String Member_Gender="";
							String Member_Group_ID="";
							String Member_DOB="";
							String Member_City="";
							String Member_State="";
							driver.manage().timeouts().implicitlyWait(250, TimeUnit.SECONDS);
							List<WebElement> cols1=rows1.get(r).findElements(By.tagName("td"));		
							for(int k=0;k<cols1.size();k++)			//Iteration for all columns in one row one by one
							{
								if(k==First_Name)
								{
//									Find.element("My_Work_Assignment_Patients").click();
									Thread.sleep(2000);
									cols1.get(k).click();	//click on first row first member
									//To Validate the Members Details in the Hover Table
									Actions action=new Actions(driver);
									action.moveToElement(cols1.get(k)).perform();
									Thread.sleep(3000);
									WebElement MemberTable=driver.findElement(By.id("EnhancedContent_EnhancedTooltip1"));	//Validate Hover Member table information
									MemberTableData=MemberTable.getText().split("\n");		//Capture all table rows in a string array split by next line
									
									//Fetching all string array actual info one by one in separate string
									Member_Name_FirstName=MemberTableData[0].trim().substring(0,MemberTableData[0].indexOf(" "));
									Member_Name_LastName=MemberTableData[0].trim().substring(MemberTableData[0].indexOf(" ")+1);
									Member_Subscriber_ID=MemberTableData[1].substring(MemberTableData[1].indexOf(":")+1).trim();
									Member_Subscriber_Suffix=MemberTableData[2].substring(MemberTableData[2].indexOf(":")+1).trim();
									Member_Relationship=MemberTableData[3].substring(MemberTableData[3].indexOf(":")+1).trim();
									Member_Gender=MemberTableData[4].substring(MemberTableData[4].indexOf(":")+1).trim();
									Member_Group_ID=MemberTableData[5].substring(MemberTableData[5].indexOf(":")+1).trim();
									String Member_DateOfBirth=MemberTableData[6].substring(MemberTableData[6].indexOf(":")+1).trim();
									SimpleDateFormat inputFormat=new SimpleDateFormat("MM/dd/yyyy");
									SimpleDateFormat outputFormat= new SimpleDateFormat("M/d/yyyy");
									Date date= inputFormat.parse(Member_DateOfBirth);
									Member_DOB=outputFormat.format(date);
									Member_City=MemberTableData[7].substring(MemberTableData[7].indexOf(":")+1).trim();
									Member_State=MemberTableData[8].substring(MemberTableData[8].indexOf(":")+1).trim();
									
									//Add capturing member all data in Hashmap By Unique Names or Key
									MemberDataList.put("First_Name_For_CCA_Member_"+r, Member_Name_FirstName);
									MemberDataList.put("Last_Name_For_CCA_Member_"+r, Member_Name_LastName);
									MemberDataList.put("Subscriber_ID_For_CCA_Member_"+r, Member_Subscriber_ID);
									MemberDataList.put("Subscriber_Suffix_For_CCA_Member_"+r, Member_Subscriber_Suffix);
									MemberDataList.put("Relationship_For_CCA_Member_"+r, Member_Relationship);
									MemberDataList.put("Gender_For_CCA_Member_"+r, Member_Gender);
									MemberDataList.put("Group_ID_For_CCA_Member_"+r, Member_Group_ID);
									MemberDataList.put("Date_Of_Birth_For_CCA_Member_"+r, Member_DOB);
									MemberDataList.put("City_For_CCA_Member_"+r, Member_City);
									MemberDataList.put("State_For_CCA_Member_"+r, Member_State);
									
									Report.log("CCA Member "+r+" Information:\n\nCCA Member First Name : "+MemberDataList.get("First_Name_For_CCA_Member_"+r)
									+"\nCCA Member Last Name : "+MemberDataList.get("Last_Name_For_CCA_Member_"+r)
									+"\nCCA Member Subscriber ID : "+MemberDataList.get("Subscriber_ID_For_CCA_Member_"+r)
									+"\nCCA Member Subscriber Suffix : "+MemberDataList.get("Subscriber_Suffix_For_CCA_Member_"+r)
									+"\nCCA Member Relationship : "+MemberDataList.get("Relationship_For_CCA_Member_"+r)
									+"\nCCA Member Gender : "+MemberDataList.get("Gender_For_CCA_Member_"+r)
									+"\nCCA Member Group ID : "+MemberDataList.get("Group_ID_For_CCA_Member_"+r)
									+"\nCCA Member Date Of Birth : "+MemberDataList.get("Date_Of_Birth_For_CCA_Member_"+r)
									+"\nCCA Member City : "+MemberDataList.get("City_For_CCA_Member_"+r)+
									"\nCCA Member State : "+MemberDataList.get("State_For_CCA_Member_"+r), Status.DONE);
									
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
													if(Find.element("CCA_Take_Assessment_Button").isEnabled())			//To check if Take Assessment Button Enabled
													{
														Report.log("Take Assessment Button Is Enabled", Status.PASS);
														Find.element("CCA_Take_Assessment_Button").click();				//To Click Take Assessment Button
														
														if(Find.element("CCA_CIA_Assessment_Page_Header").getText().trim().equals("CAD Initial Assessment"))		//To check if CAD Initial Assessment Page Header is Correct	
														{
															Report.log("CCA CAD Initial Assessment Page Header Is Matched", Status.Pass);
															
															driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);		//Wait certain amount of time for next element or page to appear
															ClickCCAAnswers CCAPrinter = new ClickCCAAnswers(driver);
															CCAPrinter.printAssessment();
															
															/* if(Find.element("CCA_CIA_Disclosure").getText().trim().equals("Disclosure"))
															{
																//Add capturing CCA CIA Disclosure Content all data in Hashmap By Unique Names or Key
																MemberDataList.put("CCA_CIA_Disclosure", Find.element("CCA_CIA_Disclosure").getText().trim());
																MemberDataList.put("CCA_CIA_Disclosure_Text", Find.element("CCA_CIA_Disclosure_Text").getText().trim());
																MemberDataList.put("CCA_CIA_Disclosure_Q1", Find.element("CCA_CIA_Disclosure_Q1").getText().trim());
																MemberDataList.put("CCA_CIA_Disclosure_A1_RB1_lbl", Find.element("CCA_CIA_Disclosure_A1_RB1_lbl").getText().trim());		
																MemberDataList.put("CCA_CIA_Disclosure_A1_RB2_lbl", Find.element("CCA_CIA_Disclosure_A1_RB2_lbl").getText().trim());
																MemberDataList.put("CCA_CIA_Disclosure_A1_RB3_lbl", Find.element("CCA_CIA_Disclosure_A1_RB3_lbl").getText().trim());
																//To check CIA Disclosure Answers Enabled
																Assert.assertTrue(vtype,"CCA CIA Disclosure Question One Answer 'Yes' Is Enabled", Find.element("CCA_CIA_Disclosure_A1_RB1_lbl").isEnabled());
																Assert.assertTrue(vtype,"CCA CIA Disclosure Question One Answer 'No' Is Enabled", Find.element("CCA_CIA_Disclosure_A1_RB2_lbl").isEnabled());		
																Assert.assertTrue(vtype,"CCA CIA Disclosure Question One Answer 'Not Required By Plan' Is Enabled", Find.element("CCA_CIA_Disclosure_A1_RB3_lbl").isEnabled());																
																Find.element("CCA_CIA_Disclosure_A1_RB1_lbl").click();		//To select Answer Yes
																Report.log("Screenshot For CCA CIA Disclosure Question One Answer 'Yes' Is Selected", Status.Pass);		//For Screenshot
																//Placeholder To Select Answer No
																//Placeholder To Select Answer Not Required By Plan
																
																Assert.assertTrue(vtype,"CCA CIA Continue Button Is Enabled", Find.element("CCA_CIA_Continue_Button").isEnabled());	
																Find.element("CCA_CIA_Continue_Button").click();
																driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);	
																
																if(Find.element("CCA_CIA_Disease_Process").getText().trim().equals("Disease Process"))
																{
																	Report.log("Screenshot For CCA CIA Disease Process Question One and Question Two", Status.Pass);
																	
																	//Add capturing CCA CIA Disease_Process Q1 Content all data in Hashmap By Unique Names or Key
																	MemberDataList.put("CCA_CIA_Disease_Process", Find.element("CCA_CIA_Disease_Process").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_Q1", Find.element("CCA_CIA_Disease_Process_Q1").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB1_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB1_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB2_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB2_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB3_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB3_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB4_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB4_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB5_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB5_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB6_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB6_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB7_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB7_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB8_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB8_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB9_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB9_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A1_CB10_lbl", Find.element("CCA_CIA_Disease_Process_A1_CB10_lbl").getText().trim());
																	//To check CIA Disease_Process Q1 Answers Enabled
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Atherosclerosis' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB1_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Coronary artery disease (CAD)' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB2_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Hardening of the arteries' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB3_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Heart disease' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB4_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Ischemic heart disease' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB5_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Narrowing of the arteries' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB6_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Other' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB7_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'None' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB8_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Atrial fibrillation/arrhythmia' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB9_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question One Answer 'Hypertensive heart disease' Is Enabled", Find.element("CCA_CIA_Disease_Process_A1_CB10_lbl").isEnabled());
																	
																	//Placeholder To Select Answer Atherosclerosis
																	//Placeholder To Select Answer Coronary artery disease (CAD)
																	//Placeholder To Select Answer Hardening of the arteries
																	//Placeholder To Select Answer Heart disease
																	//Placeholder To Select Answer Ischemic heart disease
																	//Placeholder To Select Answer Narrowing of the arteries
																	//Placeholder To Select Answer Other
																	//Placeholder To Select Answer None
																	//Placeholder To Select Answer Atrial fibrillation/arrhythmia
																	//Placeholder To Select Answer Hypertensive heart disease
																	
																	//Add capturing CCA CIA Disease_Process Q2 Content all data in Hashmap By Unique Names or Key
																	MemberDataList.put("CCA_CIA_Disease_Process_Q2", Find.element("CCA_CIA_Disease_Process_Q2").getText().trim());
																	//To check CIA Disease_Process Q2 Answers Enabled
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Two Text Area Is Enabled", Find.element("CCA_CIA_Disease_Process_A2_TxtArea").isEnabled());
																	
																	//Placeholder To Enter Text In Question Two Text Area
																	
																	//To Scroll till provided element
																	driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																	WebElement Scroll1=driver.findElement(By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_79_ctrlQuestion_txt_79"));
																	JavascriptExecutor je=(JavascriptExecutor) driver;	
																	je.executeScript("arguments[0].scrollIntoView(true);", Scroll1);
																	Report.log("Screenshot For CCA CIA Disease Process Question Three and Question Four", Status.Pass);
																	
																	//Add capturing CCA CIA Disease_Process Q3 Content all data in Hashmap By Unique Names or Key
																	MemberDataList.put("CCA_CIA_Disease_Process_Q3", Find.element("CCA_CIA_Disease_Process_Q3").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A3_RB1_lbl", Find.element("CCA_CIA_Disease_Process_A3_RB1_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A3_RB2_lbl", Find.element("CCA_CIA_Disease_Process_A3_RB2_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A3_RB3_lbl", Find.element("CCA_CIA_Disease_Process_A3_RB3_lbl").getText().trim());
																	//To check CIA Disease_Process Q3 Answers Enabled
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Three Answer 'Yes, Hyperthyroidism' Is Enabled", Find.element("CCA_CIA_Disease_Process_A3_RB1_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Three Answer 'Yes, Hypothyroidism' Is Enabled", Find.element("CCA_CIA_Disease_Process_A3_RB2_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Three Answer 'No' Is Enabled", Find.element("CCA_CIA_Disease_Process_A3_RB3_lbl").isEnabled());
																	
																	//Placeholder To Select Answer Yes, Hyperthyroidism
																	//Placeholder To Select Answer Yes, Hypothyroidism
																	//Placeholder To Select Answer No
																	
																	//Add capturing CCA CIA Disease_Process Q4 Content all data in Hashmap By Unique Names or Key
																	MemberDataList.put("CCA_CIA_Disease_Process_Q4", Find.element("CCA_CIA_Disease_Process_Q4").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A4_RB1_lbl", Find.element("CCA_CIA_Disease_Process_A4_RB1_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A4_RB2_lbl", Find.element("CCA_CIA_Disease_Process_A4_RB2_lbl").getText().trim());
																	//To check CIA Disease_Process Q4 Answers Enabled
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Four Answer 'Yes' Is Enabled", Find.element("CCA_CIA_Disease_Process_A4_RB1_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Four Answer 'No' Is Enabled", Find.element("CCA_CIA_Disease_Process_A4_RB2_lbl").isEnabled());
																	
																	//Placeholder To Select Answer Yes
																	//Placeholder To Select Answer No
																	
																	//To Scroll till provided element
																	driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
																	WebElement Scroll2=driver.findElement(By.id("cphBody_cphButtonsBottom_btnContinue2"));
																	je.executeScript("arguments[0].scrollIntoView(true);", Scroll2);
																	Report.log("Screenshot For CCA CIA Disease Process Question Five and Question Six", Status.Pass);
																	
																	//Add capturing CCA CIA Disease_Process Q5 Content all data in Hashmap By Unique Names or Key
																	MemberDataList.put("CCA_CIA_Disease_Process_Q5", Find.element("CCA_CIA_Disease_Process_Q5").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB1_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB1_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB2_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB2_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB3_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB3_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB4_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB4_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB5_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB5_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB6_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB6_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB7_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB7_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB8_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB8_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB9_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB9_lbl").getText().trim());
																	MemberDataList.put("CCA_CIA_Disease_Process_A5_CB10_lbl", Find.element("CCA_CIA_Disease_Process_A5_CB10_lbl").getText().trim());
																	//To check CIA Disease_Process Q5 Answers Enabled
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Following a heart healthy diet' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB1_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Maintaining a healthy weight' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB2_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Being physically active' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB3_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Stopping use of tobacco products' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB4_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Managing stress' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB5_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Use of medicine' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB6_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Cardiac procedures or surgery' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB7_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Cardiac rehabilitation' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB8_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Dental care including brushing twice daily,flossing, and regular check ups' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB9_lbl").isEnabled());
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Five Answer 'Other' Is Enabled", Find.element("CCA_CIA_Disease_Process_A5_CB10_lbl").isEnabled());
																	
																	//Placeholder To Select Answer Following a heart healthy diet
																	//Placeholder To Select Answer Maintaining a healthy weight
																	//Placeholder To Select Answer Being physically active
																	//Placeholder To Select Answer Stopping use of tobacco products
																	//Placeholder To Select Answer Managing stress
																	//Placeholder To Select Answer Use of medicine
																	//Placeholder To Select Answer Cardiac procedures or surgery
																	//Placeholder To Select Answer Cardiac rehabilitation
																	//Placeholder To Select Answer Dental care including brushing twice daily,flossing, and regular check ups
																	//Placeholder To Select Answer Other
																	
																	//Add capturing CCA CIA Disease_Process Q6 Content all data in Hashmap By Unique Names or Key
																	MemberDataList.put("CCA_CIA_Disease_Process_Q6", Find.element("CCA_CIA_Disease_Process_Q6").getText().trim());
																	//To check CIA Disease_Process Q6 Answers Enabled
																	Assert.assertTrue(vtype,"CCA CIA Disease Process Question Six Text Area Is Enabled", Find.element("CCA_CIA_Disease_Process_A6_TxtArea").isEnabled());
																	
																	//Placeholder To Enter Text In Question Six Text Area
																}
																else
																{
																	Report.log("CCA CIA Disease Process Page Is Not Available Which Is Not Expected", Status.Fail);	
																}
															}
															else
															{
																Report.log("CCA CIA Disclosure Page Is Not Available Which Is Not Expected", Status.Fail);	
															} 
															*/
														} 
														else		//if CCA CAD Initial Assessment Page Header Is Not Matched
														{
															Report.log("CCA CAD Initial Assessment Page Header Is Not Matched", Status.Fail);
														}
														//To check Abort Button Enabled
														Assert.assertTrue(vtype,"Abort Button Is Enabled", Find.element("CCA_CIA_Abort_Button").isEnabled());
														Find.element("CCA_CIA_Abort_Button").click();		//To click on Discard Button
														Thread.sleep(3000);				//Give wait for few seconds till pop up displayed
													}
													else			//if Take Assessment Button Is Not Enabled
													{
														Report.log("Take Assessment Button Is Not Enabled", Status.Fail);
													}
												}
												else			//if CCA CAD Initial Assessment Is Not Available
												{
													Report.log("CCA CAD Initial Assessment Is Not Available", Status.Fail);
												}
											}
											else			//if CCA Assessment Page Header Is Not Matched
											{
												Report.log("CCA Assessment Page Header Is Not Matched", Status.Fail);
											}
										}
										else			//if CCA Assessment Page Title Is Not Matched
										{
											Report.log("CCA Assessment Page Title Is Not Matched", Status.Fail);
										}
									}
									else		//if Assessment Icon Is Not Enabled
									{
										Report.log("Assessment Icon Is Not Enabled", Status.Fail);
									}
									
								}
							}
						}	
					}
					else			//if Members Are Not Present In The My Work Assignment For Validation
					{
						Report.log("Warning: Members Are Not Present In The My Work Assignment For Validation",Status.WARN);
					}
				}
			}
			else			//if My Work Assignment Page Header Is Not Matched
			{
				Report.log("My Work Assignment Page Header Is Not Matched", Status.Fail);
			}
		}
		else				//if My Work Assignment Icon Is Not Enabled
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