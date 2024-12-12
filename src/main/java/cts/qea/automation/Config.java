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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cts.qea.automation.excel.ExcelHelper;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class Config {
	private static final Logger LOG = LoggerFactory.getLogger(Config.class);
	public static volatile String env = null;
	private static LinkedHashMap<String, LinkedHashMap<String, String>> envDetails = null;

	/**
	 * <b>Description</b> Initializes the Details from Config.xlsx Sheet
	 */
	public static synchronized void init() throws Exception {
		envDetails = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		
		ExcelHelper excel = new ExcelHelper();
		LOG.info("Envronment Configuration Loading...."+Parameter.ENV_CONFIG_WORKBOOK.getKey());
		XSSFSheet mySheet = excel.getsheet(Parameter.ENV_CONFIG_WORKBOOK.getKey(), Parameter.COMMON_DETAILS_SHEET.getKey());
		// Load the common details that are present in the CommonDetails sheet to configure the base data
		for (int row = 0; row < 100; row++) {
			String app_val = excel.getCellData(mySheet, row, 0).toLowerCase();

			if (!envDetails.containsKey(app_val)) {
				envDetails.put(app_val, new LinkedHashMap<String, String>());
			}

			String var_val = excel.getCellData(mySheet, row, 1).toLowerCase();
			envDetails.get(app_val).put(var_val, excel.getCellData(mySheet, row, 2));
		}
		if(env == null)
			env =  getEnvDetails("aut", "env");
		
		// Load the Environment details that are present in the EnvDetails sheet
		mySheet = excel.getsheet(Parameter.ENV_CONFIG_WORKBOOK.getKey(), "EnvDetails");
		for (int row = 0; row < 100; row++) {
			String env_val = excel.getCellData(mySheet, row, 1);

			if(env_val.equalsIgnoreCase(env)){
				String app_val = excel.getCellData(mySheet, row, 0).toLowerCase();

				if (!envDetails.containsKey(app_val)) {
					envDetails.put(app_val, new LinkedHashMap<String, String>());
				}

				String var_val = excel.getCellData(mySheet, row, 2).toLowerCase();
				envDetails.get(app_val).put(var_val, excel.getCellData(mySheet, row, 3));
			}
		}

		mySheet = excel.getsheet(Parameter.ENV_CONFIG_WORKBOOK.getKey(), "UserDetails");
		for (int row = 0; row < 100; row++) {
			String env_val = excel.getCellData(mySheet, row, 1);
			String app_val = excel.getCellData(mySheet, row, 0).toLowerCase();
			
			if(env_val.equalsIgnoreCase(env)){
				String userRole = excel.getCellData(mySheet, row, 2).toLowerCase();
				String userId = excel.getCellData(mySheet, row, 3);
				String password = excel.getCellData(mySheet, row, 4);
				if(!userRole.equals("")
						&& !userId.equals("")
						&& !password.equals("")){
					UserInfo userDetails = new UserInfo();
					userDetails.setUserId(userId);
					userDetails.setPassword(password);
					UserList.putUser(app_val, userRole, userDetails);
				}
			}
		}

		// close the workbook
		mySheet.getWorkbook().close();
	}

	public static synchronized String getEnvDetails(String app, String var){
		String val = StringUtils.EMPTY;
		if(envDetails == null){
			try {  
				init();
			} catch (Exception e) {
				LOG.error("Exception: " + e.getMessage(), e);
			}
		}
		app = app.toLowerCase();
		try {
			if (envDetails.containsKey(app)
					&& envDetails.get(app).containsKey(var.toLowerCase())) {
				val = envDetails.get(app).get(var.toLowerCase()).toString();
			} 
		}catch(Exception e) {
			LOG.error("Exception: " + e.getMessage(), e);
		}
		return val;
	}
	/**
	 * <b>Description</b> Add iteration to the test case specified
	 * 
	 * @param          tc  Name of the Test case 
	 * @param          itr Iteration for the test case 
	 */
	public final static synchronized void setStateEnv(String state, String env) {
		if(env != null)
			Config.env = env;
	}

	public static synchronized void displayDataHashMap() {
		Set<String> groupNames = envDetails.keySet();
		Iterator<String> groupNamesIterator = groupNames.iterator();
		while (groupNamesIterator.hasNext()) {
			String groupName = (String) groupNamesIterator.next();
			Set<String> columnNames = envDetails.get(groupName).keySet();
			Iterator<String> columnNamesIterator = columnNames.iterator();
			while (columnNamesIterator.hasNext()) {
				String columnName = (String) columnNamesIterator.next();
				String dataValue = (String) envDetails.get(groupName).get(columnName);
				Report.log(dataValue, Status.DONE);
			}
		}
	}

	/**
	 * Saves the input app/var combination with the input value
	 * 
	 * @param app   the app
	 * @param var   the var
	 * @param value the value
	 */
	public static synchronized void setEnvDetails(String app, String var, String value) {

		if (envDetails == null) {
			try {
				init();
			} catch (Exception e) {
				LOG.error("Exception: " + e.getMessage(), e);
			}
		}

		app = app.toLowerCase();
		var = var.toLowerCase();

		if (!envDetails.containsKey(app)) {
			envDetails.put(app, new LinkedHashMap<String, String>());
		}

		envDetails.get(app).put(var, value);
	}
}