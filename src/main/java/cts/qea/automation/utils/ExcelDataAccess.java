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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cts.qea.automation.exception.TASExcelDataProcessException;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

/**
 * Class to encapsulate the excel data access layer of the framework
 * 
 * @author Cognizant
 */
public class ExcelDataAccess {
	private static final Logger LOG = LoggerFactory.getLogger(ExcelDataAccess.class);
	private final String filePath, fileName;

	private String datasheetName;
	private XSSFSheet worksheet;
	private XSSFWorkbook workbook;

	/**
	 * Function to get the Excel sheet name
	 * 
	 * @return The Excel sheet name
	 */
	public String getDatasheetName() {
		return datasheetName;
	}

	/**
	 * Function to set the Excel sheet name
	 * 
	 * @param datasheetName The Excel sheet name
	 * @throws Exception
	 */
	public void setDatasheetName(String datasheetName) throws Exception {
		this.datasheetName = datasheetName;
		checkPreRequisites();
		this.workbook = openFileForReading();
		this.worksheet = getWorkSheet(workbook);
	}

	/**
	 * Constructor to initialize the excel data filepath and filename
	 * 
	 * @param filePath The absolute path where the excel data file is stored
	 * @param fileName The name of the excel data file (without the extension). Note
	 *                 that .xlsx files are not supported, only .xls files are
	 *                 supported
	 */
	public ExcelDataAccess(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}

	/**
	 * Constructor to initialize the excel data workbook and worksheet
	 * 
	 * @param workbook  the workbook as {@link XSSFWorkbook}
	 * @param worksheet the worksheet as {@link XSSFSheet}
	 */
	public ExcelDataAccess(XSSFWorkbook workbook, XSSFSheet worksheet ) {
		this.filePath = null;
		this.fileName = null;
		this.workbook = workbook;
		this.worksheet = worksheet;
	}

	private void checkPreRequisites() throws Exception {
		if (datasheetName == null) {
			throw new TASExcelDataProcessException("ExcelDataAccess.datasheetName is not set!");
		}
	}

	private XSSFWorkbook openFileForReading() throws Exception {

		String absoluteFilePath = filePath + File.separator + fileName + ".xlsx";
		
		if (filePath.equals("")) {
			absoluteFilePath = fileName + ".xlsx";
		}
		
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			LOG.error("Error Message: " + e.getMessage());
			throw new TASExcelDataProcessException("The specified file \"" + absoluteFilePath + "\" does not exist!");
		}
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(fileInputStream);
			// fileInputStream.close();
		} catch (IOException e) {
			LOG.error("Error Message: " + e.getMessage());
			throw new TASExcelDataProcessException("Error while opening the specified Excel workbook \"" + absoluteFilePath + "\"");
		}

		return workbook;
	}

	private void writeIntoFile(XSSFWorkbook workbook) throws Exception {
		String absoluteFilePath = filePath + File.separator + fileName + ".xlsx";

		if (filePath.equals("")) {
			absoluteFilePath = fileName;
		}
		
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new TASExcelDataProcessException("The specified file \"" + absoluteFilePath + "\" does not exist!");
		}

		try {
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e) {
			LOG.error("Error Message: " + e.getMessage());
			throw new TASExcelDataProcessException("Error while writing into the specified Excel workbook \"" + absoluteFilePath + "\"");
		}
	}

	private XSSFSheet getWorkSheet(XSSFWorkbook workbook) throws Exception {
		XSSFSheet worksheet = workbook.getSheet(datasheetName);
		if (worksheet == null) {
			throw new TASExcelDataProcessException("The specified sheet \"" + datasheetName + "\""
					+ " does not exist within the workbook \"" + fileName + ".xlsx\"");
		}

		return worksheet;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key         The value being searched for
	 * @param columnNum   The column number in which the key should be searched
	 * @param startRowNum The row number from which the search should start
	 * @return The row number in which the specified key is found (-1 if the key is
	 *         not found)
	 * @throws Exception
	 */
	public int getRowNum(String key, int columnNum, int startRowNum) throws Exception {
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		String currentValue;
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet.getLastRowNum(); currentRowNum++) {

			XSSFRow row = worksheet.getRow(currentRowNum);
			if (row == null) {
				continue;
			}
			XSSFCell cell = row.getCell(columnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				return currentRowNum;
			}
		}

		return -1;
	}

	private String getCellValueAsString(XSSFCell cell, FormulaEvaluator formulaEvaluator) throws Exception {
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return "";
		} else {
			if (formulaEvaluator.evaluate(cell).getCellType() == CellType.ERROR) {
				throw new TASExcelDataProcessException("Error in formula within this cell! " + "Error code: " + cell.getErrorCellValue());
			}

			DataFormatter dataFormatter = new DataFormatter();
			return dataFormatter.formatCellValue(formulaEvaluator.evaluateInCell(cell));
		}
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key       The value being searched for
	 * @param columnNum The column number in which the key should be searched
	 * @return The row number in which the specified key is found (-1 if the key is
	 *         not found)
	 * @throws Exception
	 */
	public int getRowNum(String key, int columnNum) throws Exception {
		return getRowNum(key, columnNum, 0);
	}

	/**
	 * Function to get the last row number within the worksheet
	 * 
	 * @return The last row number within the worksheet
	 * @throws Exception
	 */
	public int getLastRowNum() throws Exception {
		return worksheet.getLastRowNum();
	}

	/**
	 * Function to get the last row number within the worksheet
	 * 
	 * @return The last row number within the worksheet
	 * @throws Exception
	 */
	public int getLastColumnNum() throws Exception {
		return worksheet.getRow(0).getLastCellNum();
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key         The value being searched for
	 * @param columnNum   The column number in which the key should be searched
	 * @param startRowNum The row number from which the search should start
	 * @return The occurence count of the specified key
	 * @throws Exception
	 */
	public int getRowCount(String key, int columnNum, int startRowNum) throws Exception {
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		int rowCount = 0;
		boolean keyFound = false;

		String currentValue;
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet.getLastRowNum(); currentRowNum++) {

			XSSFRow row = worksheet.getRow(currentRowNum);
			XSSFCell cell = row.getCell(columnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				rowCount++;
				keyFound = true;
			} else {
				if (keyFound) {
					break; // Assumption: Keys always appear contiguously
				}
			}
		}

		return rowCount;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key       The value being searched for
	 * @param columnNum The column number in which the key should be searched
	 * @return The occurence count of the specified key
	 * @throws Exception
	 */
	public int getRowCount(String key, int columnNum) throws Exception {
		return getRowCount(key, columnNum, 0);
	}

	/**
	 * Function to search for a specified key within a row, and return the
	 * corresponding column number
	 * 
	 * @param key    The value being searched for
	 * @param rowNum The row number in which the key should be searched
	 * @return The column number in which the specified key is found (-1 if the key
	 *         is not found)
	 * @throws Exception
	 */
	public int getColumnNum(String key, int rowNum) throws Exception {
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(rowNum);
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(key)) {
				return currentColumnNum;
			}
		}

		return -1;
	}

	/**
	 * Function to get the value in the cell identified by the specified row and
	 * column numbers
	 * 
	 * @param rowNum    The row number of the cell
	 * @param columnNum The column number of the cell
	 * @return The value present in the cell
	 * @throws Exception
	 */
	public String getValue(int rowNum, int columnNum) throws Exception {
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(rowNum);
		XSSFCell cell = row.getCell(columnNum);
		return getCellValueAsString(cell, formulaEvaluator);
	}

	/**
	 * Function to get the value in the cell identified by the specified row number
	 * and column header
	 * 
	 * @param rowNum       The row number of the cell
	 * @param columnHeader The column header of the cell
	 * @return The value present in the cell
	 * @throws Exception
	 */
	public String getValue(int rowNum, String columnHeader) throws Exception {
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int columnNum = -1;
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}

		if (columnNum == -1) {
			throw new TASExcelDataProcessException("The specified column header \"" + columnHeader + "\"" + "is not found in the sheet \""
					+ datasheetName + "\"!");
		} else {
			row = worksheet.getRow(rowNum);
			XSSFCell cell = row.getCell(columnNum);
			return getCellValueAsString(cell, formulaEvaluator);
		}
	}

	/**
	 * Function to get the value in the cell identified by the specified row number
	 * and column header
	 * 
	 * @param rowNum       The row number of the cell
	 * @param columnHeader The column header of the cell
	 * @return The value present in the cell
	 * @throws Exception
	 */
	public String getValue(String rowName, String columnHeader) throws Exception {

		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		// Start at row 1, skipping the header row
		int rowNum = getRowNum(rowName, 0, 1);

		if (rowNum == -1) {
			throw new TASExcelDataProcessException("The row value \"" + rowName + "\"" + "is not found in the test data sheet \""
					+ datasheetName + "\"!");
		}

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int columnNum = -1;
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}

		if (columnNum == -1) {
			throw new TASExcelDataProcessException("The specified column header \"" + columnHeader + "\"" + "is not found in the sheet \""
					+ datasheetName + "\"!");
		} else {
			row = worksheet.getRow(rowNum);
			XSSFCell cell = row.getCell(columnNum);
			return getCellValueAsString(cell, formulaEvaluator);
		}
	}

	private XSSFCellStyle applyCellStyle(XSSFWorkbook workbook, ExcelCellFormatting cellFormatting) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		if (cellFormatting.centred) {
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
		}
		cellStyle.setFillForegroundColor(cellFormatting.getBackColorIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = workbook.createFont();
		font.setFontName(cellFormatting.getFontName());
		font.setFontHeightInPoints(cellFormatting.getFontSize());
		if (cellFormatting.bold) {
			font.setBold(true);
		}
		font.setColor(cellFormatting.getForeColorIndex());
		cellStyle.setFont(font);

		return cellStyle;
	}

	/**
	 * Function to set the specified value in the cell identified by the specified
	 * row and column numbers
	 * 
	 * @param rowNum    The row number of the cell
	 * @param columnNum The column number of the cell
	 * @param value     The value to be set in the cell
	 * @throws Exception
	 */
	public void setValue(int rowNum, int columnNum, String value) throws Exception {
		setValue(rowNum, columnNum, value, null);
	}

	/**
	 * Function to set the specified value in the cell identified by the specified
	 * row and column numbers
	 * 
	 * @param rowNum         The row number of the cell
	 * @param columnNum      The column number of the cell
	 * @param value          The value to be set in the cell
	 * @param cellFormatting The {@link ExcelCellFormatting} to be applied to the
	 *                       cell
	 * @throws Exception
	 */
	public void setValue(int rowNum, int columnNum, String value, ExcelCellFormatting cellFormatting) throws Exception {

		XSSFRow row = worksheet.getRow(rowNum);
		XSSFCell cell = row.createCell(columnNum);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(value);

		if (cellFormatting != null) {
			XSSFCellStyle cellStyle = applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellStyle);
		}
		
		writeIntoFile(workbook);
	}

	/**
	 * Function to set the specified value in the cell identified by the specified
	 * row number and column header
	 * 
	 * @param rowNum       The row number of the cell
	 * @param columnHeader The column header of the cell
	 * @param value        The value to be set in the cell
	 * @throws Exception
	 */
	public void setValue(int rowNum, String columnHeader, String value) throws Exception {
		setValue(rowNum, columnHeader, value, null);
		Report.log("Wrote the value: "+value + " into row: "+ rowNum + " : Column: "+ columnHeader, Status.DONE);
	}

	/**
	 * Function to set the specified value in the cell identified by the specified
	 * row number and column header
	 * 
	 * @param rowNum       The row Header of the cell
	 * @param columnHeader The column header of the cell
	 * @param value        The value to be set in the cell
	 * @throws Exception
	 */
	public void setValue(String row, String columnHeader, String value) throws Exception {
		setValue(getRowNum(row, 0), columnHeader, value, null);
		Report.log("Wrote the value: "+value + " into row: "+ row + " : Column: "+ columnHeader, Status.DONE);
	}
	
	/**
	 * Function to set the specified value in the cell identified by the specified
	 * row number and column header
	 * 
	 * @param rowNum         The row number of the cell
	 * @param columnHeader   The column header of the cell
	 * @param value          The value to be set in the cell
	 * @param cellFormatting The {@link ExcelCellFormatting} to be applied to the
	 *                       cell
	 * @throws Exception
	 */
	public void setValue(int rowNum, String columnHeader, String value, ExcelCellFormatting cellFormatting)
			throws Exception {

		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int columnNum = -1;
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}

		if (columnNum == -1) {
			throw new TASExcelDataProcessException("The specified column header \"" + columnHeader + "\"" + "is not found in the sheet \""
					+ datasheetName + "\"!");
		} else {
			row = worksheet.getRow(rowNum);
			XSSFCell cell = row.createCell(columnNum);
			cell.setCellType(CellType.STRING);
			cell.setCellValue(value);

			if (cellFormatting != null) {
				XSSFCellStyle cellStyle = applyCellStyle(workbook, cellFormatting);
				cell.setCellStyle(cellStyle);
			}

			writeIntoFile(workbook);
		}
	}

	/**
	 * Function to set a hyperlink in the cell identified by the specified row and
	 * column numbers
	 * 
	 * @param rowNum      The row number of the cell
	 * @param columnNum   The column number of the cell
	 * @param linkAddress The link address to be set
	 * @throws Exception
	 */
	public void setHyperlink(int rowNum, int columnNum, String linkAddress) throws Exception {

		XSSFRow row = worksheet.getRow(rowNum);
		XSSFCell cell = row.getCell(columnNum);
		if (cell == null) {
			throw new TASExcelDataProcessException("Specified cell is empty! " + "Please set a value before including a hyperlink...");
		}

		setCellHyperlink(workbook, cell, linkAddress);

		writeIntoFile(workbook);
	}

	private void setCellHyperlink(XSSFWorkbook workbook, XSSFCell cell, String linkAddress) {
		XSSFCellStyle cellStyle = cell.getCellStyle();
		XSSFFont font = cellStyle.getFont();
		font.setUnderline(XSSFFont.U_SINGLE);
		cellStyle.setFont(font);

		CreationHelper creationHelper = workbook.getCreationHelper();
		Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
		hyperlink.setAddress(linkAddress);

		cell.setCellStyle(cellStyle);
		cell.setHyperlink(hyperlink);
	}

	/**
	 * Function to set a hyperlink in the cell identified by the specified row
	 * number and column header
	 * 
	 * @param rowNum       The row number of the cell
	 * @param columnHeader The column header of the cell
	 * @param linkAddress  The link address to be set
	 * @throws Exception
	 */
	public void setHyperlink(int rowNum, String columnHeader, String linkAddress) throws Exception {

		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int columnNum = -1;
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}

		if (columnNum == -1) {
			throw new TASExcelDataProcessException("The specified column header \"" + columnHeader + "\"" + "is not found in the sheet \""
					+ datasheetName + "\"!");
		} else {
			row = worksheet.getRow(rowNum);
			XSSFCell cell = row.getCell(columnNum);
			if (cell == null) {
				throw new TASExcelDataProcessException("Specified cell is empty! " + "Please set a value before including a hyperlink...");
			}

			setCellHyperlink(workbook, cell, linkAddress);

			writeIntoFile(workbook);
		}
	}

	/**
	 * Function to create a new Excel workbook
	 * 
	 * @throws Exception
	 */
	public void createWorkbook() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();

		writeIntoFile(workbook);
	}

	/**
	 * Function to add a sheet to the Excel workbook
	 * 
	 * @param sheetName The sheet name to be added
	 * @throws Exception
	 */
	public void addSheet(String sheetName) throws Exception {
		XSSFWorkbook workbook = openFileForReading();

		XSSFSheet worksheet = workbook.createSheet(sheetName);
		worksheet.createRow(0); // include a blank row in the sheet created
		
		writeIntoFile(workbook);

		this.datasheetName = sheetName;
	}

	/**
	 * Function to add a new row to the Excel worksheet
	 * 
	 * @return The row number of the newly added row
	 * @throws Exception
	 */
	public int addRow() throws Exception {

		int newRowNum = worksheet.getLastRowNum() + 1;
		worksheet.createRow(newRowNum);

		writeIntoFile(workbook);

		return newRowNum;
	}

	/**
	 * Function to add a new column to the Excel worksheet
	 * 
	 * @param columnHeader The column header to be added
	 * @throws Exception
	 */
	public void addColumn(String columnHeader) throws Exception {
		addColumn(columnHeader, null);
	}

	/**
	 * Function to add a new column to the Excel worksheet
	 * 
	 * @param columnHeader   The column header to be added
	 * @param cellFormatting The {@link ExcelCellFormatting} to be applied to the
	 *                       column header
	 * @throws Exception
	 */
	public void addColumn(String columnHeader, ExcelCellFormatting cellFormatting) throws Exception {

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int lastCellNum = row.getLastCellNum();
		if (lastCellNum == -1) {
			lastCellNum = 0;
		}

		XSSFCell cell = row.createCell(lastCellNum);
		cell.setCellType(CellType.STRING);
		cell.setCellValue(columnHeader);

		if (cellFormatting != null) {
			XSSFCellStyle cellStyle = applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellStyle);
		}

		writeIntoFile(workbook);
	}

	/**
	 * Function to merge the specified range of cells (all inputs are 0-based)
	 * 
	 * @param firstRow The first row
	 * @param lastRow  The last row
	 * @param firstCol The first column
	 * @param lastCol  The last column
	 * @throws Exception
	 */
	public void mergeCells(int firstRow, int lastRow, int firstCol, int lastCol) throws Exception {

		CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		worksheet.addMergedRegion(cellRangeAddress);

		writeIntoFile(workbook);
	}

	/**
	 * Function to specify whether the row summaries appear below the detail within
	 * an outline (grouped set of rows)
	 * 
	 * @param rowSumsBelow Boolean value to specify row summaries below detail
	 *                     within an outline
	 * @throws Exception
	 */
	public void setRowSumsBelow(boolean rowSumsBelow) throws Exception {

		worksheet.setRowSumsBelow(rowSumsBelow);

		writeIntoFile(workbook);
	}

	/**
	 * Function to outline (i.e., group together) the specified rows
	 * 
	 * @param firstRow The first row
	 * @param lastRow  The last row
	 * @throws Exception
	 */
	public void groupRows(int firstRow, int lastRow) throws Exception {

		worksheet.groupRow(firstRow, lastRow);

		writeIntoFile(workbook);
	}

	/**
	 * Function to automatically adjust the column width to fit the contents for the
	 * specified range of columns (all inputs are 0-based)
	 * 
	 * @param firstCol The first column
	 * @param lastCol  The last column
	 * @throws Exception
	 */
	public void autoFitContents(int firstCol, int lastCol) throws Exception {

		if (firstCol < 0) {
			firstCol = 0;
		}

		if (firstCol > lastCol) {
			throw new TASExcelDataProcessException("First column cannot be greater than last column!");
		}

		for (int currentColumn = firstCol; currentColumn <= lastCol; currentColumn++) {
			worksheet.autoSizeColumn(currentColumn);
		}

		writeIntoFile(workbook);
	}

	public List<String> getWorkSheets() throws Exception {
		XSSFWorkbook workbook = openFileForReading();
		List<String> workbooklist = new ArrayList<String>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			workbooklist.add(workbook.getSheetName(i));
		}
		return workbooklist;
	}
	
	public boolean checkExistance() {
		String absoluteFilePath = filePath + File.separator + fileName + ".xlsx";
		if (filePath.equals("")) {
			absoluteFilePath = fileName;
		}
		return new File(absoluteFilePath).exists();
	}
	
	public List<String> getValues(String rowName, String columnHeader) throws Exception {

		List<String> values = new ArrayList<String>();
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int columnNum = -1;
		String currentValue;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); currentColumnNum++) {

			XSSFCell cell = row.getCell(currentColumnNum);
			currentValue = getCellValueAsString(cell, formulaEvaluator);

			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}
		if (columnNum == -1) {
			throw new Exception("The specified column header \"" + columnHeader + "\"" + "is not found in the sheet \""
					+ datasheetName + "\"!");
		}
		
		// Start at row 1, skipping the header row
		int rowNum =1;
		int currentrowNum = 1;
		System.out.println(worksheet.getLastRowNum());
		do {
			rowNum = getRowNum(rowName, 0, rowNum);
//			System.out.println(rowNum);
			if (rowNum != -1) {
				row = worksheet.getRow(rowNum++);
				XSSFCell xcell = row.getCell(columnNum);
				values.add(getCellValueAsString(xcell, formulaEvaluator));
				//currentrowNum =  rowNum;
			}else {
				rowNum = currentrowNum;
			}
			currentrowNum =  currentrowNum +1;
		}while (currentrowNum <= worksheet.getLastRowNum());
//		for (int currentrowNum = 1; currentrowNum < row.getLastCellNum(); currentrowNum++) {
//			rowNum = getRowNum(rowName, 0, rowNum+1);
////			System.out.println(rowNum);
//			if (rowNum != -1) {
//				row = worksheet.getRow(rowNum);
//				XSSFCell xcell = row.getCell(columnNum);
//				values.add(getCellValueAsString(xcell, formulaEvaluator));
//			}
//		}

		if (values.size()== 0) {
			throw new Exception("The row value \"" + rowName + "\"" + "is not found in the test data sheet \""
					+ datasheetName + "\"!");
		}
		
		return values;
		
	}

	/**
	 * Closes the workbook
	 * 
	 * @throws Exception when error occurs
	 */
	public void close() throws Exception {

		if (null != this.workbook) {
			this.workbook.close();
		}
	}
}