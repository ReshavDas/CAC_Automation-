package com.CCA.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.cts.caffe360.encry.util.SecurityUtils;
import cts.qea.automation.Assert;
import cts.qea.automation.Config;
import cts.qea.automation.PageObject;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class OpenCCA extends PageObject
{
	//Launching and Login in CCA Application
	public OpenCCA(WebDriver pdriver )//All page objects to be defined in the constructor
	{
		super(pdriver);
		/*
		 *  Naming Convention 
		 	Acronyms : Expansions
		   	CCA      : Clinical Care Advance
			CIA      : CAD Initial Assessment
			Q 	     : Question   	Example Q1: Question One
			A 	     : Answer       Example A1: Answer One
			CB 	     : Check Box   	Example CB1: Check Box One
			RB     	 : Radio Button Example RB1: Radio Button One
			lbl 	 : Label
			TxtArea  : Text Area
		 */
		
		//Login Page
		element("txt_UsrNm", By.id("LoginCtrl1_txtUserID"));
		element("txt_Pwd", By.id("LoginCtrl1_txtPassword"));
		element("Btn_Submit", By.id("LoginCtrl1_btnSubmit"));
		//Home Page
		element("My_Work_Assignment_Icon", By.xpath("//*[@id=\"menu_caeMenu_My_Work_Assignments\"]/span/div/div[2]"));
		element("My_Work_Assignment_Page_Header", By.xpath("//*[@id=\"_pageHeader\"]/tbody/tr/td/span[1]"));
		element("My_Work_Assignment_Table_Header", By.id("ucPatients_ucRadGrid_tzgRadGrid_GridHeader"));
		element("My_Work_Assignment_Table_Data", By.id("ucPatients_ucRadGrid_tzgRadGrid_GridData"));
		element("My_Work_Assignment_Patients", By.xpath("/html/body/form/div[4]/div[2]/div/ul/li[1]"));
		element("My_Work_Assignment_Table_Grid", By.id("ucPatients_ucRadGrid_tzgRadGrid_ctl00_ctl03_ctl01_tdPageDescription"));
		element("My_Work_Assignment_Table_Next_Page", By.id("ucPatients_ucRadGrid_tzgRadGrid_ctl00_ctl03_ctl01_btnNextPage"));
		//Assessment Icon Page
		element("CCA_Assessment_Icon", By.id("AssessmentsHolder"));
		element("CCA_Assessment_Header", By.xpath("/html/body/form/div[3]/div[1]/h1"));
		element("CCA_Search_Assessment_Name", By.id("ctl00_ctl00_cphBody_cphRadGrid_ucRadGrid_tzgRadGrid_ctl00_ctl02_ctl02_FilterTextBox_hra_name"));
		element("CCA_CIA_Assessment_Status", By.xpath("/html/body/form/div[3]/div[4]/div[3]/div[3]/div/div[2]/div[2]/table/tbody/tr/td[2]"));
		element("CCA_Select_CIA_Assessment", By.id("ctl00_ctl00_cphBody_cphRadGrid_ucRadGrid_tzgRadGrid_ctl00__0"));
		element("CCA_Take_Assessment_Button", By.id("cphBody_cphButtonsTop_btnTakeHRATop"));
		element("CCA_View_History_Button", By.id("cphBody_cphActionLinks_btnViewHistory"));
		element("CCA_ReTake_Assessment_Button", By.id("cphBody_cphButtonsTop_btnRetakeHRATop"));
		//CAD Initial Assessment View History Page
		element("CAD_Initial_Assessment_History_Table", By.className("rgMasterTable"));
		element("Void_Assessment_Button", By.id("cphBody_cphActionLinks_btnVoidHraLink"));
		element("Confirm_Void_Assessment_Button", By.id("cphBody_cphButtonsTop_btnVoidHraTop"));
		element("Go_To_Assessment_List_Button", By.id("cphBody_cphModuleMenu_moduleMenuHraHsitory_btnGoToHRAList"));
		//CCA CAD Initial Assessment Page
		element("CCA_CIA_Assessment_Page_Header", By.xpath("/html/body/form/div[3]/div[1]/div/table/tbody/tr/td[2]/h1"));
		//Disclosure Section
		element("CCA_CIA_Disclosure", By.id("cphBody_cphContent_HRAWizard1_HeaderContainer_lblHeader"));
		element("CCA_CIA_Disclosure_Text", By.xpath("/html/body/form/div[3]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[2]/div"));
		element("CCA_CIA_Disclosure_Q1", By.id("cphBody_cphContent_HRAWizard1_26_P_13_q_29_lblQuestion_29"));
		element("CCA_CIA_Disclosure_A1_RB1", By.xpath("/html/body/form/div[3]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[2]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span/input"));
		element("CCA_CIA_Disclosure_A1_RB2", By.xpath("/html/body/form/div[3]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[2]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[2]/td/span/input"));
		element("CCA_CIA_Disclosure_A1_RB3", By.xpath("/html/body/form/div[3]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[2]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[3]/td/span/input"));
		element("CCA_CIA_Disclosure_A1_RB1_lbl", By.xpath("/html/body/form/div[3]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[2]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span/label"));
		element("CCA_CIA_Disclosure_A1_RB2_lbl", By.xpath("/html/body/form/div[3]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[2]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[2]/td/span/label"));
		element("CCA_CIA_Disclosure_A1_RB3_lbl", By.xpath("/html/body/form/div[3]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[2]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[3]/td/span/label"));
		//Disease Process Section
		element("CCA_CIA_Disease_Process", By.id("cphBody_cphContent_HRAWizard1_HeaderContainer_lblHeader"));
		element("CCA_CIA_Disease_Process_Q1", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_78_lblQuestion_78"));
		element("CCA_CIA_Disease_Process_A1_CB1", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[1]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB2", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[2]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB3", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[3]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB4", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[4]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB5", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[5]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB6", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[6]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB7", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[7]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB8", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[8]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB9", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[9]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB10", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[10]/span/input"));
		element("CCA_CIA_Disease_Process_A1_CB1_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[1]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB2_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[2]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB3_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[3]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB4_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[4]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB5_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[5]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB6_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[6]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB7_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[7]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB8_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[8]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB9_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[9]/span/label"));
		element("CCA_CIA_Disease_Process_A1_CB10_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[1]/div[3]/table[1]/tbody/tr/td/div/div[10]/span/label"));
		element("CCA_CIA_Disease_Process_Q2", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_79_lblQuestion_79"));
		element("CCA_CIA_Disease_Process_A2_TxtArea", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_79_ctrlQuestion_txt_79"));
		element("CCA_CIA_Disease_Process_Q3", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_335_lblQuestion_335"));
		element("CCA_CIA_Disease_Process_A3_RB1", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[3]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span/input"));
		element("CCA_CIA_Disease_Process_A3_RB2", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[3]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[2]/td/span/input"));
		element("CCA_CIA_Disease_Process_A3_RB3", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[3]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[3]/td/span/input"));
		element("CCA_CIA_Disease_Process_A3_RB1_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[3]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span/label"));
		element("CCA_CIA_Disease_Process_A3_RB2_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[3]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[2]/td/span/label"));
		element("CCA_CIA_Disease_Process_A3_RB3_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[3]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[3]/td/span/label"));
		element("CCA_CIA_Disease_Process_Q4", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_1_lblQuestion_1"));
		element("CCA_CIA_Disease_Process_A4_RB1", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[4]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span/input"));
		element("CCA_CIA_Disease_Process_A4_RB2", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[4]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[2]/td/span/input"));
		element("CCA_CIA_Disease_Process_A4_RB1_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[4]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[1]/td/span/label"));
		element("CCA_CIA_Disease_Process_A4_RB2_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[4]/div[3]/table[1]/tbody/tr/td/table/tbody/tr[2]/td/span/label"));
		element("CCA_CIA_Disease_Process_Q5", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_80_lblQuestion_80"));
		element("CCA_CIA_Disease_Process_A5_CB1", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[1]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB2", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[2]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB3", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[3]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB4", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[4]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB5", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[5]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB6", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[6]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB7", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[7]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB8", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[8]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB9", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[9]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB10", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[10]/span/input"));
		element("CCA_CIA_Disease_Process_A5_CB1_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[1]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB2_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[2]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB3_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[3]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB4_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[4]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB5_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[5]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB6_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[6]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB7_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[7]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB8_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[8]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB9_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[9]/span/label"));
		element("CCA_CIA_Disease_Process_A5_CB10_lbl", By.xpath("/html/body/form/div[8]/div[4]/div[2]/div[3]/table/tbody/tr[2]/td/div/div/div[5]/div[3]/table[1]/tbody/tr/td/div/div[10]/span/label"));
		element("CCA_CIA_Disease_Process_Q6", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_81_lblQuestion_81"));
		element("CCA_CIA_Disease_Process_A6_TxtArea", By.id("cphBody_cphContent_HRAWizard1_26_P_1_q_81_ctrlQuestion_txt_81"));
		element("CCA_CIA_Continue_Button", By.id("cphBody_cphButtonsTop_btnContinue"));
		element("CCA_CIA_Continue_Assessment_Button", By.xpath("/html/body/div/div/div[1]/div/a[2]"));
		element("CCA_CIA_Abort_Button", By.id("cphBody_cphButtonsBottom_btnAbort2"));
		
	}
	//Create this method to launch CCA Application
	public void Launch(ValidationType vType) throws Exception
	{
		String url = Config.getEnvDetails("CCA","URL");	//Load CCA URL from env config sheet
		driver.navigate().to(url);	//Navigate CCA Url
		driver.manage().window().maximize();	//Maximize browser
		if(driver.getTitle().equals("Login:"))		//Validate Title of the page after navigate
		{
			Assert.assertTrue(vType, "CCA Application Title Matched", driver.getTitle().equals("Login:"));
			Report.log("CCA Application Launched Successfully", Status.Pass);
		}
		else
		{
			Report.log("CCA URL Not Working", Status.Fail);
		}
	}
	//Create this method to Login CCA Application
	public void Login(ValidationType vType) throws Exception
	{
		String UsrNm = Config.getEnvDetails("CCA", "UserName");
		String Pwd = Config.getEnvDetails("CCA", "UserPassword");
		String DecryptPwd = SecurityUtils.getDecodedValue(Pwd);		//Decode Encrypt password
		element("txt_UsrNm").sendKeys(UsrNm);		//Give Username to Usernmae textfield
		element("txt_Pwd").sendKeys(DecryptPwd);	//Give password to password textfield
		element("Btn_Submit").click();
		Thread.sleep(8000);	//Click on submit
		//Giving wait till next page load after click on submit
		WebDriverWait wait=new WebDriverWait(driver, 15);
		//wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.tagName("html"))));
		if(driver.getTitle().equals("Clinical CareAdvance"))		//Validate Title of the page after navigate
		{
			Report.log("CCA Application Logged In Successfully",Status.Pass);
		}
		else
		{
			Report.log("CCA Application Not Logged In",Status.Fail);
		}
	}
	public void closeApp()
	{
		driver.quit();
	}
	@Override
	public void validatePageExists(ValidationType vType) {
		// TODO Auto-generated method stub
	}


}
