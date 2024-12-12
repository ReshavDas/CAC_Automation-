package com.CCA.pageobjects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import cts.qea.automation.Assert;
import cts.qea.automation.DataProvider;
import cts.qea.automation.PageObject;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC04A_CaptureFirstMemberInformationInCCA extends PageObject
{
	/*
	CCA-TestCase 
	-To capture first member information From CCA Application
	*/
	public TC04A_CaptureFirstMemberInformationInCCA(WebDriver bdriver)
	{
		super(bdriver);
	}
	public HashMap<String,String> TestCase4A(ValidationType vtype, DataProvider dp) throws Exception
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
						for(int r=1;r<2;r++)     //Iteration for all rows in one Table one by one
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
									Thread.sleep(5000);
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
									
								}
							}
						}	
					}
					else				//If Members Are Not Present In The My Work Assignment For Validation
					{
						Report.log("Warning: Members Are Not Present In The My Work Assignment For Validation",Status.WARN);
					}
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