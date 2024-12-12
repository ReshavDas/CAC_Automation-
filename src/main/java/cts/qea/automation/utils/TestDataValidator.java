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

package cts.qea.automation.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataValidator {

	private static final Logger logger = LoggerFactory.getLogger(TestDataValidator.class);

	/**
	 * Validates that the sheetName exists from the input fileName
	 * 
	 * @param fileName  the file
	 * @param sheetName the sheet name
	 * @throws Exception when error occurs
	 */
	public static void validateTestDataFile(String fileName, String sheetName) throws Exception {

		// the ExcelDataAccess expects the filename without the extension name
		ExcelDataAccess dataAccess = new ExcelDataAccess(StringUtils.EMPTY, FilenameUtils.removeExtension(fileName));

		try {
			// This method will throw an exception if the input sheetName is missing from the input fileName
			dataAccess.setDatasheetName(sheetName);
		} catch (Exception e) {
			throw e;
		} finally {
			dataAccess.close();
		}
	}

	/**
	 * Validates the input testCase within the input workbook and sheet. The
	 * following validations are checked:
	 * <ul>
	 * <li>Metadata/iteration exists on 2nd column</li>
	 * <li>Metadata/Credentials exists</li>
	 * <li>Metadata/ExecuteFlag exists</li>
	 * <li>Metadata/AppName</li>
	 * <li>Metadata/Description</li>
	 * <li>Flags/executeSql</li>
	 * <li>Flags/Credentials</li>
	 * </ul>
	 * 
	 * @param workbook the workbook as {@link XSSFWorkbook}
	 * @param sheet    the sheet as {@link XSSFSheet}
	 * @param testCase the test case
	 * @return <code>true</code> if there are no validation errors, otherwise
	 *         <code>false</code>.
	 * @throws Exception when error occurs
	 */
	public static boolean validateTestCase(XSSFWorkbook workbook, XSSFSheet sheet, String testCase) throws Exception {
		List<String> validationErrors = new ArrayList<String>();
		String metadata;
		String flags;

		ExcelDataAccess dataAccess = new ExcelDataAccess(workbook, sheet);

		int row = dataAccess.getRowNum(testCase, 0);

		// check Metadata/iteration exists on 2nd column for the testCase
		metadata = dataAccess.getValue(row, 1);
		String iteration = dataAccess.getValue(row + 1, 1);
		if (StringUtils.isBlank(metadata) || StringUtils.isBlank(iteration)
				|| (!"Iteration".toLowerCase().equals(iteration.toLowerCase())
						&& !"Metadata".toLowerCase().equals(metadata.toLowerCase()))) {
			validationErrors.add("Metadata/iteration");
		}

		// check Metadata/Credentials exists for the testCase
		int credentialsColumn = dataAccess.getColumnNum("Credentials", row + 1);
		if (credentialsColumn == -1) {
			validationErrors.add("Metadata/Credentials");
		} else {
			metadata = dataAccess.getValue(row, credentialsColumn);
			if (StringUtils.isBlank(metadata) || !"Metadata".toLowerCase().equals(metadata.toLowerCase())) {
				validationErrors.add("Metadata/Credentials");
			}
		}

		// check Metadata/ExecuteFlag exists for the testCase
		int executeFlagColumn = dataAccess.getColumnNum("ExecuteFlag", row + 1);
		if (executeFlagColumn == -1) {
			validationErrors.add("Metadata/ExecuteFlag");
		} else {
			metadata = dataAccess.getValue(row, executeFlagColumn);
			if (StringUtils.isBlank(metadata) || !"Metadata".toLowerCase().equals(metadata.toLowerCase())) {
				validationErrors.add("Metadata/ExecuteFlag");
			}
		}

		// check Metadata/AppName exists for the testCase
		int appNameColumn = dataAccess.getColumnNum("AppName", row + 1);
		if (appNameColumn == -1) {
			validationErrors.add("Metadata/AppName");
		} else {
			metadata = dataAccess.getValue(row, appNameColumn);
			if (StringUtils.isBlank(metadata) || !"Metadata".toLowerCase().equals(metadata.toLowerCase())) {
				validationErrors.add("Metadata/AppName");
			}
		}

		// check Metadata/Description exists for the testCase
		int descriptionColumn = dataAccess.getColumnNum("Description", row + 1);
		if (descriptionColumn == -1) {
			validationErrors.add("Metadata/Description");
		} else {
			metadata = dataAccess.getValue(row, descriptionColumn);
			if (StringUtils.isBlank(metadata) || !"Metadata".toLowerCase().equals(metadata.toLowerCase())) {
				validationErrors.add("Metadata/Description");
			}
		}

		// check Flags/executeSql exists for the testCase
		int executeSqlColumn = dataAccess.getColumnNum("executeSql", row + 1);
		if (executeSqlColumn == -1) {
			validationErrors.add("Flags/executeSql");
		} else {
			flags = dataAccess.getValue(row, executeSqlColumn);
			if (StringUtils.isBlank(flags) || !"Flags".toLowerCase().equals(flags.toLowerCase())) {
				validationErrors.add("Flags/executeSql");
			}
		}

		// check Flags/Credentials exists for the testCase
		int tdmCreateColumn = dataAccess.getColumnNum("tdmCreate", row + 1);
		if (tdmCreateColumn == -1) {
			validationErrors.add("Flags/Credentials");
		} else {
			flags = dataAccess.getValue(row, tdmCreateColumn);
			if (StringUtils.isBlank(flags) || !"Flags".toLowerCase().equals(flags.toLowerCase())) {
				validationErrors.add("Flags/Credentials");
			}
		}

		// if validationErrors is not empty, construct the message and send to log file
		if (CollectionUtils.isNotEmpty(validationErrors)) {
			logger.info("\"" + testCase + "\" test case is missing the following Group/Element combinations: "
					+ validationErrors.toString());

			return false;
		}

		return true;
	}
}