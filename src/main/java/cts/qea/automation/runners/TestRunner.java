package cts.qea.automation.runners;

/**********************************************************************
* COGNIZANT CONFIDENTIAL OR TRADE SECRET
*
* Copyright 2020 - 2023 Cognizant.  All rights reserved.
*
* NOTICE:  This unpublished material is proprietary to Cognizant and 
* its suppliers, if any.  The methods, techniques and technical 
* concepts herein are considered Cognizant confidential or trade 
* secret information.  This material may also be covered by U.S. or
* foreign patents or patent applications.  Use, distribution or 
* copying of these materials, in whole or in part, is forbidden, 
* except by express written permission of Cognizant.
***********************************************************************/

import cts.qea.automation.Config;
import cts.qea.automation.TestCase;
import cts.qea.automation.annotations.Tags;
import cts.qea.automation.excel.ExcelHelper;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.reports.ReportTemplate.Caller;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class TestRunner {
	private static LinkedHashMap <String, TestCaseThread> testCases = new LinkedHashMap<String, TestCaseThread>();

	/**
	 * <b>Description</b>Returns the class(testCase) instance for the argument tcname
	 * @param		tcName Test case Name
	 */
	@SuppressWarnings("unchecked")
	private static Class<TestCase> getTestClass(String tcName) {
		try {
			Class<TestCase> test;
			test = (Class<TestCase>) Class.forName(tcName);
			return test;
		} catch (Exception e) {
			Report.log(" Reporting  : Test case  "+tcName  +"   is not avaiable in the workspace. Please verify once", Status.FAIL);
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * <b>Description</b>adds the test case with appending the argument iteration
	 * @param		tcName Test case Name
	 * @param		itr   iteration of the test case
	 */
	// For time being addTC(String tcName, int itr) method will be available till iteration is changed to String datatype
	private static void addTC(String tcName, int itr){	
		addTC(tcName, Integer.toString(itr));
	}

	/**
	 * <b>Description</b>adds the test case with appending the argument iteration
	 * @param		tcName Test case Name
	 * @param		itr   iteration of the test case
	 */
	private static void addTC(String tcName, String itr){	
		if(!testCases.containsKey(tcName+"_"+itr))
			testCases.put(tcName+"_"+itr, new TestCaseThread(getTestClass(tcName), itr));
	}

	/*	*//**
	 * <b>Description</b>adds the test case with appending the argument iteration depending upon execute flag
	 * @param		tcName Test case Name
	 * @param		startItr   iteration of the test case
	 * @param		execute   boolean to add/ignore for adding to test cases hash map
	 *//*
	public static void addTC(String tcName, int startItr, boolean execute){
		if(execute){
			addTC(tcName, startItr);
		}
	}*/

	/**
	 * <b>Description</b>adds the test case with appending the argument iteration depending upon execute flag
	 * @param		tcName Test case Name
	 * @param		itr   iteration of the test case
	 * @param		execute   boolean to add/ignore for adding to test cases hash map
	 */
	public static void addTC(String tcName, String itr, boolean execute){
		if(execute){
			addTC(tcName, itr);
		}
	}

	/**
	 * <b>Description</b>adds the test case with appending the argument iteration depending upon execute flag
	 * @param		tcName Test case Name
	 * @param		startItr   iteration of the test case
	 * @param		endItr   iteration of the test case
	 * @param		execute   boolean to add/ignore for adding to test cases hash map
	 */
	public static void addTC(String tcName, int startItr, int endItr, boolean execute){
		if(execute){
			for(;startItr <= endItr; startItr++){
				addTC(tcName, startItr);
			}
		}
	}

	/**
	 * <b>Description</b>Boolean will return if the test case is available in existing hashmap. 
	 * @param		testCase Test case Name
	 */
	public static boolean contains(String testCase){
		if(testCases.containsKey(testCase))
			return true;
		else 
			return false;    	
	}

	/**
	 * <b>Description</b> Adds the test cases in the excel sheet to testCases hash map 
	 * @param		filter class instance of TestCaseFilter
	 * @param		fileName Excel workbook file name
	 * @param		sheetName  Excel workbook Sheet name
	 */
	public static void addTCFromXL(TestCaseFilter filter, String fileName, String sheetName) throws Exception{    	
		ExcelHelper ex = new ExcelHelper();	
		XSSFSheet mySheet = ex.getsheet( fileName,  sheetName);
		for(int i=0;i<ex.testDataNoOfRows;i++){    	
			if(!ex.getCellData(mySheet, i, ex.executeTestCasecolumn).toLowerCase().equals("y"))
				continue;
			String tcName=ex.getCellData(mySheet, i, ex.testCaseColumn);
			if(tcName.equals(""))
				continue;
			String iteration = ex.getCellData(mySheet, i, ex.iterationColumn);
			if(iteration.equals(""))
				continue;			
			Class<TestCase> test = getTestClass(tcName);
			if(!test.getClass().isAnnotationPresent(Tags.class))
				continue;
			if((!testCases.containsKey(tcName))){
				if(filter.isMatching(test)) 
					addTC(tcName,iteration);					
			}else{
				addTC(tcName,iteration);
			}
		}
	}

	/**
	 * <b>Description</b>Add Test cases to testCases hashMap from the test data sheet which are marked under excuteIteration cell.
	 *                   All the Test cases iterations which are marked as Yes, YES, Y, TRUE , true, yes are added to the 
	 *                   testCases HashMap which in turn participates in Batch Execution.
	 *
	 */
	public static void addTCFromXL() throws Exception{    
		String fileName  =  Config.getEnvDetails("testData", "dataFileName");
		String sheetName  =  Config.getEnvDetails("testData", "dataSheetName");

		ExcelHelper ex = new ExcelHelper();	
		XSSFSheet mySheet = ex.getsheet( fileName,  sheetName);
		for(int i=0;i<ex.testDataNoOfRows;i++){ 

			String excuteItr = ex.getCellData(mySheet, i, ex.executeTestCasecolumn);
			if(! (excuteItr.equalsIgnoreCase("y") || excuteItr.equalsIgnoreCase("yes") ||
					excuteItr.equalsIgnoreCase("TRUE") || excuteItr.equalsIgnoreCase("true") ||
					excuteItr.equalsIgnoreCase("YES") ||  excuteItr.equalsIgnoreCase("Y")))
				continue;
			String tcName=ex.getCellData(mySheet, i, ex.testCaseColumn);
			if(tcName.equalsIgnoreCase(""))
				continue;
			String iteration = ex.getCellData(mySheet, i, ex.iterationColumn);
			if(iteration.equalsIgnoreCase(""))
				continue;			
			addTC(tcName,iteration);			
		}
	}

	/**
	 * <b>Description</b> Runs parallely
	 */
	public static void runParallel(){
		Report.startSuite();
		Iterator<TestCaseThread> tcList1 = testCases.values().iterator();
		//		UserList.ConfigureUsers();
		int maxThreads = 0;
		if(!Config.getEnvDetails("browser", "grid_max_threads").equals("")){
			try{
				maxThreads = Integer.parseInt(Config.getEnvDetails("browser", "grid_max_threads"));
			}catch(Exception e){
				// All other cases treat the user is not looking to limit the threads
			}
		}
		while(tcList1.hasNext()){
			TestCaseThread tc = tcList1.next(); 
			tc.start();	//does not wait for thread to complete
			while (maxThreads !=0 && TestCaseThread.activeCount()> maxThreads) {		
			}
		}
		//wait for all test cases to be completed
		Iterator<TestCaseThread> tcList2 = testCases.values().iterator();
		while(tcList2.hasNext()){
			try{
				tcList2.next().join();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Report.endSuite();

		// open report if caller is TestRunner and not the Application
		if (Report.getCaller() == Caller.SUITE) {
			Report.openReport();
		}
	}

	/**
	 * <b>Description</b> Runs parallely
	 */
	public static void runParallel(String args[]){
		String state = null, env = null;
		try {
			state = args[0];
			env = args[1];
		} catch (Exception e) {
		}
		Config.setStateEnv(state, env);
		runParallel();
	}

	/**
	 * <b>Description</b> Runs sequentially
	 */
	public static void runSeqential(){
		Report.startSuite();
		try{
			Iterator<TestCaseThread> tcList1 = testCases.values().iterator();
			while(tcList1.hasNext()){
				Thread tc = tcList1.next();
				try{
					System.out.println("Test Case Name: " + tc.getName());
					tc.run();	//run method waits for thread to complete
				}catch(Exception e){
					System.out.println("Thread Execution Error. Thread Name: " +tc.getName() + "\t Error Message: " + e.getMessage());
				}
				//Enhancement
				try {
					System.gc();
				}catch (Exception e) {
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Report.endSuite();

		// open report if caller is TestRunner and not the Application
		if (Report.getCaller() == Caller.SUITE) {
			Report.openReport();
		}
	}
	
	public static void runTestCase(String testCaseClass, String iteration) {
		try {
			addTC(testCaseClass, iteration);
			runSeqential();
		}catch(Exception e) {
			System.out.println("Test Case run Error: "+e.getMessage());
			e.printStackTrace();
		}
	}
	public static void clearTestRunner() {
		testCases.clear();
	}
}
