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
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import cts.qea.automation.exception.TASExcelDataProcessException;

public class DataFactoryValidator {

	/**
	 * Validates the data factory file.
	 * 
	 * @param fileName  the file which contains the data factory configuration
	 * @param sheetName the sheet name of the file
	 * @throws Exception when error occurs
	 */
	public static void validateDataFactoryFile(String fileName, String sheetName) throws Exception {
		List<String> validationErrors = new ArrayList<String>();

		// the ExcelDataAccess expects the filename without the extension name
		ExcelDataAccess dataAccess = new ExcelDataAccess(StringUtils.EMPTY, FilenameUtils.removeExtension(fileName));

		try {
			// This method will throw an exception if the input sheetName is missing from the input fileName
			dataAccess.setDatasheetName(sheetName);

			// Validate 1st column header is GenericSqlFactory
			String genericSqlFactoryCell = dataAccess.getValue(0, 0);
			if (StringUtils.isBlank(genericSqlFactoryCell) || 
					!"GenericSqlFactory".toLowerCase().equals(genericSqlFactoryCell.toLowerCase())) {
				validationErrors.add("GenericSqlFactory header is missing from 1st column of " + sheetName + " sheet.");
			}

			// Validate 2nd column header is Iteration
			String iterationCell = dataAccess.getValue(0, 1);
			if (StringUtils.isBlank(iterationCell) || 
					!"Iteration".toLowerCase().equals(iterationCell.toLowerCase())) {
				validationErrors.add("Iteration header is missing from 2nd column of " + sheetName + " sheet.");
			}

			// Validate Query Group header exists on first row
			int columnNum = dataAccess.getColumnNum("Query Group", 0);
			if (columnNum == -1) {
				validationErrors.add("Query Group header is missing from 1st row of " + sheetName + " sheet.");
			}

			// if validationErrors is not empty, construct the exception message and throw the exception
			if (CollectionUtils.isNotEmpty(validationErrors)) {
				String exceptionMessage = "Please fix below error(s) found on the sheet \"" + sheetName
						+ "\" within the workbook " + fileName + "\n";
				exceptionMessage = exceptionMessage
						.concat(validationErrors.stream().collect(Collectors.joining("\n- ", "- ", "")));

				throw new TASExcelDataProcessException(exceptionMessage);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			dataAccess.close();
		}
	}
}