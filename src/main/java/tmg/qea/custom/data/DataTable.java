package tmg.qea.custom.data;

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

/**
 * Class to encapsulate the datatable related functions of the framework
 * 
 * @author Cognizant
 */
@Deprecated
public class DataTable {
	private final String datatablePath, datatableName;
	private String dataReferenceIdentifier = "#";

	/**
	 * Constructor to initialize the {@link CraftDataTable} object
	 * 
	 * @param datatablePath
	 *            The path where the datatable is stored
	 * @param datatableName
	 *            The name of the datatable file
	 */
	public DataTable(String datatablePath, String datatableName) {
		this.datatablePath = datatablePath;
		this.datatableName = datatableName;
	}

	/**
	 * Function to set the data reference identifier character
	 * 
	 * @param dataReferenceIdentifier
	 *            The data reference identifier character
	 * @throws Exception 
	 */
	public void setDataReferenceIdentifier(String dataReferenceIdentifier) throws Exception {
		if (dataReferenceIdentifier.length() != 1) {
			throw new Exception(
					"The data reference identifier must be a single character!");
		}

		this.dataReferenceIdentifier = dataReferenceIdentifier;
	}

	/**
	 * Function to return the test data value corresponding to the sheet name
	 * and field name passed
	 * 
	 * @param datasheetName
	 *            The name of the sheet in which the data is present
	 * @param fieldName
	 *            The name of the field whose value is required
	 * @return The test data present in the field name specified
	 * @throws Exception 
	 * @see #putData(String, String, String)
	 * @see #getExpectedResult(String)
	 */
	public String getData(String datasheetName, String fieldName, String rowName) throws Exception {
		
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath,
				datatableName);
		testDataAccess.setDatasheetName(datasheetName);

		int rowNum = testDataAccess.getRowNum(rowName, 0, 1); // Start
																		// at
																		// row
																		// 1,
																		// skipping
																		// the
																		// header
																		// row
		if (rowNum == -1) {
			throw new Exception("The row value \"" + rowName
					+ "\"" + "is not found in the test data sheet \""
					+ datasheetName + "\"!");
		}
		
		String dataValue = testDataAccess.getValue(rowNum, fieldName);

		if (dataValue.startsWith(dataReferenceIdentifier)) {
			dataValue = getCommonData(fieldName, dataValue);
		}

		return dataValue;
	}

	private String getCommonData(String fieldName, String dataValue) throws Exception {
		ExcelDataAccess commonDataAccess = new ExcelDataAccess(datatablePath,
				"Common Testdata");
		commonDataAccess.setDatasheetName("Common_Testdata");

		String dataReferenceId = dataValue.split(dataReferenceIdentifier)[1];

		int rowNum = commonDataAccess.getRowNum(dataReferenceId, 0, 1); // Start
																		// at
																		// row
																		// 1,
																		// skipping
																		// the
																		// header
																		// row
		if (rowNum == -1) {
			throw new Exception(
					"The common test data row identified by \""
							+ dataReferenceId + "\""
							+ "is not found in the common test data sheet!");
		}

		return commonDataAccess.getValue(rowNum, fieldName);
	}

	/**
	 * Function to output intermediate data (output values) into the specified
	 * sheet
	 * 
	 * @param datasheetName
	 *            The name of the sheet into which the data is to be written
	 * @param fieldName
	 *            The name of the field into which the data is to be written
	 * @param dataValue
	 *            The value to be written into the field specified
	 * @throws Exception 
	 * @see #getData(String, String)
	 */
	public void putData(String datasheetName, String fieldName, String rowName, String dataValue) throws Exception {
		
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath,
				datatableName);
		testDataAccess.setDatasheetName(datasheetName);

		int rowNum = testDataAccess.getRowNum(rowName, 0, 1); // Start
																		// at
																		// row
																		// 1,
																		// skipping
																		// the
																		// header
																		// row
		if (rowNum == -1) {
			throw new Exception("The row name \"" + rowName
					+ "\"" + "is not found in the test data sheet \""
					+ datasheetName + "\"!");
		}
		
		synchronized (DataTable.class) {
			testDataAccess.setValue(rowNum, fieldName, dataValue);
		}
	}
}