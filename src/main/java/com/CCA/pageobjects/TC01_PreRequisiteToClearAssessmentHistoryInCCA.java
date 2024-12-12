package com.CCA.pageobjects;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import cts.qea.automation.Assert;
import cts.qea.automation.PageObject;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC01_PreRequisiteToClearAssessmentHistoryInCCA extends PageObject
{
	/*
	CCA-TestCase 
	PreRequisite To Clear The CAD Initial Assessment History In CCA Before validating Fresh CAD Initial assessment
	*/
	public TC01_PreRequisiteToClearAssessmentHistoryInCCA(WebDriver bdriver)
	{
		super(bdriver);
	}
	public void TestCase(ValidationType vtype) throws Exception
	{
		OpenCCA Find=new OpenCCA(driver);	//Constructor of OpenCCA class to load elements locator
		boolean FirstNameFound=false;
		int First_Name=0;
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
										if(Find.element("CCA_Assessment_Header").getText().trim().equals("Assessments"))					//To check if CCA Assessment Header is Correct	
										{
											Assert.assertTrue(vtype,"CCA Assessment Page Header Is Matched", Find.element("CCA_Assessment_Header").getText().trim().equals("Assessments"));
											Find.element("CCA_Search_Assessment_Name").sendKeys("CAD Initial Assessment");			//To enter CAD Initial Assessment in CCA Search Assessment Name
											//Used Robot class to perform keyboard action like press enter
											Robot robot=new Robot();
											robot.keyPress(KeyEvent.VK_ENTER);
											robot.keyRelease(KeyEvent.VK_ENTER);
											Thread.sleep(5000);		//Hold script for 5 seconds
											
											if(!(Find.element("CCA_CIA_Assessment_Status").getText().trim().equals("Never Taken"))) 	//To check CCA CAD Initial Assessment Never Taken
											{	
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
														if(rows2.size()>1)			//To check if Assessment History Is present in View History table
														{
															Report.log("Screenshot For Existing CIA Assessment History Is Present In View History Table", Status.Pass);
															for(int R=1;R<rows2.size();R++)     //Iteration for all rows in one Table one by one
															{
																Thread.sleep(1000);
																if(Find.element("Void_Assessment_Button").isEnabled())					//To check if Void Assessment Button Enabled
																{
																	Assert.assertTrue(vtype,"Void Assessment Button Is Enabled", Find.element("Void_Assessment_Button").isEnabled());
																	Find.element("Void_Assessment_Button").click();
																	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
																	if(Find.element("Confirm_Void_Assessment_Button").isEnabled())					//To check if Confirm Void Assessment Button Enabled
																	{
																		Assert.assertTrue(vtype,"Confirm Void Assessment Button Is Enabled", Find.element("Confirm_Void_Assessment_Button").isEnabled());
																		Find.element("Confirm_Void_Assessment_Button").click();
																		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
																		Report.log("Screenshot For Existing CIA Assessment "+R+" History Is Removed From View History Table", Status.Pass);
																	}
																	else
																	{
																		Report.log("Confirm Void Assessment Button Is Not Enabled", Status.Fail);
																	}
																}
																else		//if Void Assessment Button Is Not Enabled
																{
																	Report.log("Void Assessment Button Is Not Enabled", Status.Fail);
																}
															}
														}			
														else				//if Assessment History Is Not present in View History table
														{
															Report.log("CAD Initial Assessment History Is Not Present In View History Table",Status.Fail);
														}
													}
													else			//if if View History Button Is Not Enabled
													{
														Report.log("CCA View History Button Is Not Enabled", Status.Fail);
													}
												}
												else	//if if CCA CAD Initial Assessment Is Not Available
												{
													Report.log("CCA CAD Initial Assessment Is Not Available", Status.Fail);
												}
											}
											else		//if CCA View History Button Is Not Displayed
											{
												Report.log("Screenshot For CCA View History Button Is Not Displayed", Status.Pass);
											}
										}
										else			//if CCA Assessment Page Header Is Not Matched
										{
											Report.log("CCA Assessment Page Header Is Not Matched", Status.Fail);
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
					else		
					{
						Report.log("Warning: Members Are Not Present In The My Work Assignment For Validation",Status.WARN);
					}
				}
				else		//if First Name Column Is Not Present In My Work Assignment Table
				{
					Report.log("First Name Column Is Not Present In My Work Assignment Table", Status.Fail);
				}
			}
			else		//if My Work Assignment Page Header Is Not Matched
			{
				Report.log("My Work Assignment Page Header Is Not Matched", Status.Fail);
			}
		}
		else		//if My Work Assignment Icon Is Not Enabled
		{
			Report.log("My Work Assignment Icon Is Not Enabled", Status.Fail);
		}
	}
	@Override
	public void validatePageExists(ValidationType vType) {
		// TODO Auto-generated method stub
		
	}
}