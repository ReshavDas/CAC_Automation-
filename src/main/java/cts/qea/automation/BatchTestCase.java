package cts.qea.automation;

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

import java.lang.reflect.Method;
import org.apache.http.MethodNotSupportedException;
import cts.qea.automation.reports.*;

public abstract class BatchTestCase extends TestCase {

	boolean isMethodExists = false;
	/**
	 * <b>Description</b> Invoking Batch Test cases
	 */
	@Override
	public final void test() throws Exception {
		String lastDayStatus = dp.get("DayWiseResult", this.getAttribute("nameWithDayIteration")+"_Verdict");

		if(lastDayStatus.equalsIgnoreCase("PASS") || lastDayStatus.equalsIgnoreCase("DONE")){
			day++;
		}
		Method m[] = this.getClass().getDeclaredMethods();	//Returns an array of Method objects reflecting all the methods declared by the class or interface represented by this Class object.
		//		Method m[]=this.getClass().getMethods();	//Returns an array containing Method objects reflecting all the public member methods 
															//of the class or interface represented by this Class object, including those declared 
															//by the class or interface and those inherited from super classes and super interfaces.
		
		int totalDays = 0;
		//calculate the number of days
		for (int i = 0; i < m.length; i++) {
			if(m[i].getName().contains("day_0"))
				totalDays += 1;
		}
		for (int i = 0; i < m.length; i++) {
			if(m[i].getName().contains("day_0"+day)){
				isMethodExists = true;
				break;
			}
		}
		if(!isMethodExists) {
			Report.log("All the days available in the test case have been Passed, to re-run the entire testcase please delete the entire Ini", Status.Pass);
			--day;
			return;
		}

		Report.addMetaInfo("Day", day+" of "+totalDays);
		switch (day) {
		case 1:
			executeDay_01();
			break;
		case 2:
			executeDay_02();
			break;
		case 3:
			executeDay_03();
			break;
		case 4:
			executeDay_04();
			break;	
		case 5:
			executeDay_05();
			break;	
		default:
			Report.log("Log for Day_05 is available so quiting the execution", Status.FAIL);
			throw new MethodNotSupportedException("Maximum days execution has been completed");
		}
	}

	/**
	 * <b>Description</b> Day 1 Method 
	 */
	protected abstract void day_01() throws Exception;

	/**
	 * <b>Description</b> Day 2 Method
	 */
	protected abstract void day_02()throws Exception;

	/**
	 * <b>Description</b> Day 3 Method
	 */	
	protected void day_03() throws Exception {}

	/**
	 * <b>Description</b> Day 4 Method
	 */
	protected void day_04() throws Exception {}

	/**
	 * <b>Description</b> Day 5 Method 
	 */
	protected void day_05() throws Exception {}

	/**
	 * <b>Description</b> Wrapper for Day 1 Method
	 */
	protected void executeDay_01() throws Exception {
		setAttribute("day","1");
		day_01();
	}

	/**
	 * <b>Description</b> Wrapper for Day 2 Method
	 */
	protected void executeDay_02() throws Exception {
		setAttribute("day","2");
		day_02();
	}

	/**
	 * <b>Description</b> Wrapper for Day 3 Method
	 */
	protected void executeDay_03() throws Exception {
		setAttribute("day","3");
		day_03();
	}

	/**
	 * <b>Description</b> Wrapper for Day 4 Method
	 */
	protected void executeDay_04() throws Exception {
		setAttribute("day","4");
		day_04();
	}

	/**
	 * <b>Description</b> Wrapper for Day 5 Method
	 */
	protected void executeDay_05() throws Exception {
		setAttribute("day","5");
		day_05();
	}

}