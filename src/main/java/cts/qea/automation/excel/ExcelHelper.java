package cts.qea.automation.excel;

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
import cts.qea.automation.DataProvider;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.utils.DateHelper;
import cts.qea.automation.utils.DbHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.FileInputStream;
import java.sql.ResultSet;

public class ExcelHelper {	
private static final Logger LOG = LoggerFactory.getLogger(ExcelHelper.class);
	public final int testDataNoOfRows = 60000;
	public final int testDataColumncount = 999;
	public final int testCaseColumn = 0;
	public final int testDataColumn = 0;
	public final int executeTestCasecolumn = 3;
//	public final int packageName = 2;
	public final int iterationColumn = 1;
	/**
	 * <b>Description</b> Finds Iteration Row Number for a Test Case
	 * @param             sheet Excel tab of test cases to identify
	 * @param             testcaseRow  Test case Row Number in the Excel Sheet
	 * @param             testCase   Test Case Id 
	 * @param             iterationRow   Iteration Id
	 * @return            int (Iteration Id Row number in a Excel sheet)
	 */
	public int findDisplaceMentrow(XSSFSheet sheet, int testcaseRow, String testCase, String iterationRow){	
		for (int row = testcaseRow; row < testDataNoOfRows; row++) {	
			String tstCase = getCellData(sheet, row,testCaseColumn).toLowerCase().trim();
			if(!tstCase.equals(testCase))
				return -1;
			String iteration = getCellData(sheet, row,iterationColumn).toLowerCase().trim();
			if (!iteration.equals(iterationRow))	continue;
			return  row - testcaseRow;
		}
		return -1;
	}

	/**
	 * <b>Description</b> Gets a Cell data reference by ROW and COLUMN number. Empty String if fails
	 * @param             mySheet Excel tab of test cases to identify
	 * @param             row  Row number of the excel sheet
	 * @param             col  Column number of the excel sheet
	 * @return            returnVal  Value of the CELL referenced by ROW and COLUMN number
	 * 
	 */

	public synchronized String getCellData(XSSFSheet mySheet, int row, int col){
		String returnVal=StringUtils.EMPTY;
		try{
			XSSFCell cell = mySheet.getRow(row).getCell(col);
			switch(cell.getCellType()){			
			case STRING:			
				returnVal = cell.getStringCellValue();
				if(returnVal.length()>4){
					if(returnVal.substring(0,4).equalsIgnoreCase("sql:")){
						String [] sqlQuery = returnVal.split("SQL:");
						DbHelper dbHelper = new DbHelper();
						dbHelper.connectDatabase();
						ResultSet rs = dbHelper.executeQuery(sqlQuery[1]);
						if(rs != null && rs.next()){
							returnVal = rs.getString(1);
						}else{
							Report.log("Unable to retrive data from DB for the query  :"+mySheet.getRow(row).getCell(col).getStringCellValue(), Status.FAIL);
						}
						dbHelper.closeDatabase();	
					}
				}
				break;
			case NUMERIC:				
				returnVal = Double.toString(cell.getNumericCellValue());
				break;
			case BOOLEAN:				
				returnVal = Boolean.toString(cell.getBooleanCellValue());
				break;
			default:
				returnVal = StringUtils.EMPTY;
			}
		} catch(Exception e){
			returnVal = StringUtils.EMPTY;
		}

		return returnVal;
	}

	/**
	 * <b>Description</b>Load Data Provider from the Test Data Sheet for the respective Test case and Iteration Id
	 * @param             mySheet Excel tab of test cases to identify
	 * @param             row  Row number of the excel sheet
	 * @param             iteration   Iteration Id
	 * @return            dp  Data Provider for the test case
	 * 
	 */
	public synchronized DataProvider load(XSSFSheet mySheet, int row, int iteration) {
		DataProvider dp = new DataProvider();		
		for (int column = testDataColumn; column < testDataColumncount; column++) {	
			String groupName = getCellData(mySheet, row, column);
			String columnName = getCellData(mySheet, row+1, column).toLowerCase();
			String data = StringUtils.EMPTY;
			// To get Date related data
			if((!columnName.isEmpty()) && (columnName.startsWith("dt_") || columnName.startsWith("req_dt_"))){
				data = getDateCellData(mySheet, row+1+iteration,column,dp);
			}else{
				data = getCellData(mySheet, row+1+iteration,column);
			}

			if(!groupName.isEmpty() && !columnName.isEmpty()){
				dp.set(groupName, columnName, data);
			}
		}
		return dp;
	}

	/**
	 * <b>Description</b> Gets Date Cell data from the referenced Row and column number 
	 *                    PCDate refers to local Desktop Date,  APPDate Application deployed server Date,
	 *                    
	 * @param             sheet Excel tab of test cases to identify
	 * @param             row  Row number of the excel sheet
	 * @param             col  Column number of the excel sheet
	 * @param             iterationRow   Iteration Id
	 * @return            dp  Data Provider for the test case
	 * 
	 */

	private String getDateCellData(XSSFSheet mySheet,int row , int col,DataProvider dp){		
		String data = StringUtils.EMPTY;		
		try{	
			if(mySheet.getRow(row)!=null){
				if(mySheet.getRow(row).getCell(col)!=null){
					if(mySheet.getRow(row).getCell(col).getCellType() == CellType.STRING){

						if(mySheet.getRow(row).getCell(col).getStringCellValue().contains("PCDate")){
							data =  DateHelper.calculateDate(mySheet.getRow(row).getCell(col).getStringCellValue(),"PCDate"); 

						}else if(mySheet.getRow(row).getCell(col).getStringCellValue().contains("APPDate")) {
							data = DateHelper.calculateDate(mySheet.getRow(row).getCell(col).getStringCellValue(),"APPDate");

						}else if (dp.containsGroup(mySheet.getRow(row).getCell(col).getStringCellValue())&&
								mySheet.getRow(row).getCell(col).getStringCellValue().contains("|")){
							data = DateHelper.calculateGroupDate(mySheet.getRow(row).getCell(col).getStringCellValue(),dp.clone());

						}else{			    	
							data = DateHelper.dateConverter(getCellData(mySheet, row,col),Config.getEnvDetails("testData", "dateFormat"));
						}		
					}else if((DateUtil.isCellDateFormatted(mySheet.getRow(row).getCell(col)))){			
						data = DateHelper.dateConverter(mySheet.getRow(row).getCell(col).getDateCellValue().toString(),Config.getEnvDetails("testData", "dateFormat"));				
					}else{
						data = StringUtils.EMPTY;
					}
				}
			}
		}catch(Exception e){
			data = StringUtils.EMPTY;
		}
		return data;
	}

	/**
	 * <b>Description</b> Finds a Row for the respective Test cases Id supplied. Returns -1 if fails
	 * @param             sheet Excel tab of test cases to identify
	 * @param             testcaseID  Test case Id 
	 * @return            int   Test case Id Row Number from the excle sheet
	 */

	public int findRow(XSSFSheet sheet, String testcaseID){		
		for (int row = 0; row < testDataNoOfRows; row++) {
			String xlTCName = getCellData(sheet, row,testCaseColumn);
			if (!xlTCName.toLowerCase().trim().equals(testcaseID))	continue;
			return row;
		}
		return -1;
	}

	/**
	 * <b>Description</b> Gets Sheet from the Excel file 
	 * @param             fileName   File Name 
	 * @param             sheetName  Sheet Name
	 * @return            XSSFSheet  Sheet
	 * @throws            Exception
	 */	
	public synchronized XSSFSheet getsheet(String fileName,String sheetName) throws Exception{
		XSSFWorkbook workbook;
		XSSFSheet mySheet = null;
		LOG.info("File Name: " + fileName + "\t Sheet Name:" + sheetName);
		try {
			workbook = new XSSFWorkbook(new FileInputStream(fileName));
			mySheet = workbook.getSheet(sheetName);
		}catch(Exception e) {
			LOG.error("\nError or reading configuration files\n\n");
			e.printStackTrace();
		}
		
		return mySheet;
	}

}
