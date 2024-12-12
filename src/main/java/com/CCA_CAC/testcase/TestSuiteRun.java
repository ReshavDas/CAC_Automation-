package com.CCA_CAC.testcase;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.CAC.pageobjects.OpenCAC;
import com.CCA.pageobjects.OpenCCA;
import cts.qea.automation.TestCase;
import cts.qea.automation.ValidationType;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.runners.TestRunner;

public class TestSuiteRun extends TestRunner
{
	
	
	public static void main(String[] args) 
	{	
		WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
		try 
		{
		String filePath = "C:\\Input\\InputSheet.xlsx"; // Replace with your actual file path
        String sheetName = "Data"; // Replace with your actual sheet name
       // InputFetch inputFetch = new InputFetch(filePath);
       // List<Map<String, String>> answers = inputFetch.fetchAnswers(sheetName);

        driver.get("https://t3ode-cae-003.tzhealthcare.com:8502");
		}
		catch(Exception e)
		{
			Report.log(e);
			e.printStackTrace();
		}
		TestRunner.runSeqential();
		
	}
}