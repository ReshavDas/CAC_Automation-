package com.CAC.pageobjects;

import java.util.HashMap;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import cts.qea.automation.Assert;
import cts.qea.automation.DataProvider;
import cts.qea.automation.PageObject;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class TC02_ValidateMemberInformationInCAC extends PageObject
{
	/*
	CAC-TestCase 
	-To capture all members information In CAC Application and Matches With CCA
	*/
	public TC02_ValidateMemberInformationInCAC(WebDriver adriver)
	{
		super(adriver);
	}
	public HashMap<String,String> TestCase2(ValidationType vtype, DataProvider dp, HashMap<String, String> MemberDataList) throws Exception
	{
		OpenCAC Find=new OpenCAC(driver);	//Constructor of OpenCAC class to load elements locator
		
		if(Find.element("Search_Members_TxtArea").isEnabled())			//Validate Search Members Area
		{
			Assert.assertTrue(vtype,"Search Members Area Is Enabled", Find.element("Search_Members_TxtArea").isEnabled());
			//To check CCA members count greater than 0
			if(Integer.valueOf(MemberDataList.get("CCA_Members_Count"))>0)
			{
				for(int r=1;r<=Integer.valueOf(MemberDataList.get("CCA_Members_Count"));r++) 	//Iterate each CCA member counts and validate member information
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
					Thread.sleep(2000);
					Find.element("Search_Members_TxtArea").sendKeys(MemberDataList.get("Last_Name_For_CCA_Member_"+r));	//Enter CCA member Name
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
						}
						catch(NoSuchElementException e)
						{
							if((Find.element("Assigned_Member_List").getText().trim().equals("All assigned members downloaded")))
							{
								Report.log("Search Assigned Member "+MemberDataList.get("Last_Name_For_CCA_Member_"+r)+" Is Not Present", Status.Fail);
							}
						}
					}
					else			//if Assigned Header Text Is Not Matched
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