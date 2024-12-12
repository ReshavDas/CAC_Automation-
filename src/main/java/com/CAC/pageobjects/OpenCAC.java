package com.CAC.pageobjects;

import java.util.concurrent.TimeUnit;

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

public class OpenCAC extends PageObject
{
	//Launching and Login in CAC Application
	public OpenCAC(WebDriver driver )
	{
		super(driver);
		/*
		 *  Naming Convention 
		 	Acronyms : Expansions
		   	CAC      : Care Advance Connect
			CIA      : Asthma Initial Assessment
			Q 	     : Question   	Example Q1: Question One
			A 	     : Answer       Example A1: Answer One
			CB 	     : Check Box   	Example CB1: Check Box One
			RB     	 : Radio Button Example RB1: Radio Button One
			lbl 	 : Label
			TxtArea : Text Area
		 */
		//Passphrase Logged In Page
		element("Enter_Passphrase", By.id("passphrase"));
		element("Confirm_Passphrase", By.id("confirm"));
		element("Btn_Go", By.id("submit"));
		
		//CAC Home Page
		element("Search_Members_TxtArea", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer/div/mat-form-field/div[1]/div/div[2]/input"));
		element("Assigned_Header", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer/div/cac-member-list/mat-nav-list/div[2]"));
		element("Assigned_Member_List", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer/div/cac-member-list/mat-nav-list/div[3]"));
		element("Assigned_Search_Member", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer/div/cac-member-list/mat-nav-list/mat-list-item"));
		element("Member_Name",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/cac-demographics/div[1]/h3"));
		element("Member_Gender",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/cac-demographics/div[1]/span[1]"));
		element("Member_DOB",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/cac-demographics/div[1]/span[2]"));
		element("Member_Address",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/cac-demographics/div[2]/div[1]"));
		element("Coverages",By.id("mat-expansion-panel-header-10"));
		element("Member_Subscriber_ID",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[2]/div[5]/cac-dashboard-item/mat-expansion-panel/div/div/cac-coverage-list/table/tbody/tr/td[2]"));
		element("Download_Search_Member", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/div/button[1]/span[2]"));
		element("Refresh_Search_Member", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/div/button[1]/span[2]"));
		element("Remove_Search_Member", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/div[1]/div/button[2]/span[2]"));
		element("Add_Assessment_Button", By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/cac-work-items/mat-expansion-panel/mat-expansion-panel-header/span/button/span[2]"));
		element("Assessment_Button", By.xpath("/html/body/div[3]/div[2]/div/div/div/button[1]/span"));
		element("MyWork",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/cac-work-items/mat-expansion-panel/mat-expansion-panel-header/span/mat-panel-title"));
		element("Upload_Assessment",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/cac-work-items/mat-expansion-panel/div/div/table/tbody/tr/td[1]/button/span[3]"));
		element("Uploaded_Assessment",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/cac-work-items/mat-expansion-panel/div/div/table/tbody/tr/td[1]/mat-icon"));
		element("CAC_Completed_CIA_Assessment_Date",By.xpath("/html/body/cac-root/main/cac-member/mat-drawer-container/mat-drawer-content/section/cac-member-dashboard/cac-work-items/mat-expansion-panel/div/div/table/tbody/tr/td[4]"));
		
		//Choose Assessment Pop Up Window
		element("Choose_Assessment_Header", By.xpath("/html/body/cac-root/main/cac-select-temp/div[2]/h1"));
		element("Search_Assessment_TxtArea", By.xpath("/html/body/cac-root/main/cac-select-temp/div[2]/div[1]/mat-form-field/div[1]/div/div[2]/input"));
		element("Search_Asthma Outcomes Assessment", By.xpath("//a[@class='mat-mdc-list-item-title mdc-list-item__primary-text clickable']"));
		element("No_Search_Asthma Outcomes Assessment", By.xpath("/html/body/div[2]/div[2]/div/mat-dialog-container/div/div/cac-select-form/div[3]/div"));
		
		//CAC Initial Assessment Page
		element("Asthma Outcomes Assessment_Header", By.xpath("//h1[@class='mat-mdc-tooltip-trigger']"));
		element("Asthma Outcomes Assessment_Start", By.xpath("//span[normalize-space()='Start']"));
		element("CAC_CIA_Discard_Button", By.xpath("//span[normalize-space()='Discard']"));
		element("CAC_CIA_Discard_Assessment", By.xpath("/html/body/div[3]/div[2]/div/mat-dialog-container/div/div/cac-discard-assessment-dialog/div[2]/button[1]/span[2]"));
		element("CAC_CIA_Complete_Button", By.xpath("/html/body/cac-root/main/cac-assessment/div/cac-assessment-actions/div/button[3]/span[2]"));
		element("CAC_CIA_Complete_Assessment_Header", By.xpath("/html/body/div[2]/div[2]/div/mat-dialog-container/div/div/cac-complete-assessment-dialog/div[1]"));
		element("CAC_CIA_Complete_Assessment_Body", By.xpath("/html/body/div[2]/div[2]/div/mat-dialog-container/div/div/cac-complete-assessment-dialog/span"));
		element("CAC_CIA_Complete_Assessment", By.xpath("/html/body/div[2]/div[2]/div/mat-dialog-container/div/div/cac-complete-assessment-dialog/div[2]/button[1]/span[2]"));
		
		//Disclosure Section
		element("CAC_CIA_Disclosure", By.id("P13"));
		element("CAC_CIA_Disclosure_Text", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form/cac-content/div"));
		element("CAC_CIA_Disclosure_Q1", By.id("LP13+Q29"));
		element("CAC_CIA_Disclosure_A1_RB1_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form/cac-question/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[1]/div/label"));
		element("CAC_CIA_Disclosure_A1_RB2_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form/cac-question/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[2]/div/label"));
		element("CAC_CIA_Disclosure_A1_RB3_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form/cac-question/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[3]/div/label"));
		//Disease Process Section
		element("CAC_CIA_Disease_Process", By.id("P1"));
		element("CAC_CIA_Disease_Process_Q1", By.id("LP1+Q78"));
		element("CAC_CIA_Disease_Process_A1_CB1_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[1]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB2_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[2]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB3_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[3]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB4_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[4]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB5_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[5]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB6_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[6]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB7_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[7]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB8_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[8]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB9_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[9]/div/label"));
		element("CAC_CIA_Disease_Process_A1_CB10_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[1]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[10]/div/label"));
		element("CAC_CIA_Disease_Process_Q2", By.id("LP1+Q79"));
		element("CAC_CIA_Disease_Process_A2_TxtArea", By.id("P1+Q79"));
		element("CAC_CIA_Disease_Process_Q3", By.id("LP1+Q335"));
		element("CAC_CIA_Disease_Process_A3_RB1_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[3]/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[1]/div/label"));
		element("CAC_CIA_Disease_Process_A3_RB2_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[3]/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[2]/div/label"));
		element("CAC_CIA_Disease_Process_A3_RB3_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[3]/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[3]/div/label"));
		element("CAC_CIA_Disease_Process_Q4", By.id("LP1+Q1"));
		element("CAC_CIA_Disease_Process_A4_RB1_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[4]/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[1]/div/label"));
		element("CAC_CIA_Disease_Process_A4_RB2_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[4]/div/div[2]/cac-answer/mat-radio-group/mat-radio-button[2]/div/label"));
		element("CAC_CIA_Disease_Process_Q5", By.id("LP1+Q80"));
		element("CAC_CIA_Disease_Process_A5_CB1_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[1]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB2_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[2]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB3_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[3]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB4_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[4]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB5_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[5]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB6_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[6]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB7_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[7]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB8_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[8]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB9_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[9]/div/label"));
		element("CAC_CIA_Disease_Process_A5_CB10_lbl", By.xpath("/html/body/cac-root/main/cac-assessment/mat-drawer-container/mat-drawer-content/form/cac-render-form/cac-render-form[2]/cac-question[5]/div/div[2]/cac-answer/fieldset/div/mat-checkbox[10]/div/label"));
		element("CAC_CIA_Disease_Process_Q6", By.id("LP1+Q81"));
		element("CAC_CIA_Disease_Process_A6_TxtArea", By.id("P1+Q81"));
		
	}
	public void Launch(ValidationType vType) throws Exception
	{
		String URL = Config.getEnvDetails("CAC","URL");
		driver.navigate().to(URL);
		driver.manage().window().maximize();
		//To wait till next pass phrase page load
		//WebDriverWait wait=new WebDriverWait(driver,2000);
//		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("/html/body/cac-root/header/a/img"))));		//If we have to enter different pass phrase everytime
		//wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("/html/body/cac-root/cac-authorization/main/div/img"))));
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		//String PassPhrase = Config.getEnvDetails("CAC", "UserPassPhrase");
		//String DecryptPassPhrase = SecurityUtils.getDecodedValue(PassPhrase);		//Decode Encrypt password
		//element("Enter_Passphrase").sendKeys(DecryptPassPhrase);	//Give password to textfield
		/*element("Confirm_Passphrase").sendKeys(DecryptPassPhrase);	//Give password to textfield
		element("Btn_Go").click();*/
		Thread.sleep(6000);				//To wait till CAC application load
		if(driver.getTitle().equals("CareAdvance Connect"))		//Validate Title of the page after navigate
		{
			Assert.assertTrue(vType, "CAC Application Title Is Matched", driver.getTitle().equals("CareAdvance Connect"));
			Report.log("CAC Application Launched Successfully", Status.Pass);
		}
		else
		{
			Report.log("CAC URL Is Not Working", Status.Fail);
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
