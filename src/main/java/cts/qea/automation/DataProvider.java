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

import java.io.File;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import cts.qea.automation.excel.ExcelHelper;
import cts.qea.automation.utils.IniHelper;

/**
 * <b>Author</b> Mallikarjuna Rao<br>
 * <b>Description</b> Reading and Writing Data to Data Provider <br>
 */

public class DataProvider {

	// Test Data HashMap
	private HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();

	/**
	 * <b>Description</b> Method will return a Group Name Iterator for Data Provider
	 * @return groupNamesIterator An Iterator for Group Names in Data Provider
	 */
	public Iterator<String> groupNameIterator() {
		Set<String> groupNames = data.keySet();
		Iterator<String> groupNamesIterator = groupNames.iterator();
		return groupNamesIterator;
	}

	/**
	 * <b>Description</b> Method will merge argument Data Provider to the caller Data Provider
	 * @param dp	      Argument Data Provider which merges with caller Data Provider
	 */
	public void merge(DataProvider dp) {
		Iterator<String> groupNameIt = dp.groupNameIterator();
		while (groupNameIt.hasNext()) {
			String groupName = groupNameIt.next().toString();
			Iterator<String> columnIte = dp.columnIterator(groupName);
			while (columnIte.hasNext()) {
				String columnName = columnIte.next().toString();
				String value = dp.get(groupName, columnName.toLowerCase());
				set(groupName, columnName, value);
			}
		}
	}

	/**
	 * <b>Description</b> Method will return a Column Name Iterator for a Group in Data Provider
	 * @param             groupName   Group Name for which Column Iterator requested
	 * @return            columnNameIterator  An Iterator for Column Names in a Group 
	 */
	public Iterator<String> columnIterator(String groupName) {
		HashMap<String, String> tmp = data.get(groupName);
		Set<String> keyNames = tmp.keySet();
		Iterator<String> columnNameIterator = keyNames.iterator();
		return columnNameIterator;
	}

	public ArrayList<String> getColumnNames(String groupName) {
		ArrayList<String> columnNames = new ArrayList<String>();
		Iterator<String> keys = columnIterator(groupName);
		while(keys.hasNext()) {
			columnNames.add(WordUtils.capitalizeFully(keys.next(), ' ', '_'));
		}
		return columnNames;
	}
	/**
	 * <b>Description</b> Get a Value for Corresponding Group, Column from Data Provider.
	 *                    If Group or Column are not present, returns an empty string
	 * @param             groupName   Group Name in Data Provider
	 * @param             columnName  Column Name in a Group
	 * @return            value   String representation of a Value from Data Provider 
	 */
	public String get(String groupName, String columnName) {
		String val = StringUtils.EMPTY;
		if (data.containsKey(groupName) && data.get(groupName).containsKey(columnName.toLowerCase())) {
			//System.out.println("Query Column Name: "+ columnName + "\t Value: " + data.get(groupName).get(columnName.toLowerCase()));
			if (data.get(groupName).get(columnName.toLowerCase()) != null) {
				val = data.get(groupName).get(columnName.toLowerCase()).toString();
			} 
		}
		return val;
	}
	
	/**
	 * <b>Description</b> Returns a Clone of the Data Provider
	 *  
	 * @return           DataProvider    Clone of Data Provider
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataProvider clone() {
		DataProvider tmp = new DataProvider();
		tmp.data = (HashMap<String, HashMap<String, String>>) this.data.clone();
		return tmp;
	}

	/**
	 * <b>Description</b> Get a Data Provider with the Group Name supplied
	 *                                                      
	 * @param             grpName   Group Name in Data Provider
	 * @return            DataProvider   Data Provider having only requested Group Name
	 */
	@SuppressWarnings("unchecked")
	public DataProvider getGroup(String grpName) {
		DataProvider tmp = new DataProvider();
		if (data.containsKey(grpName)) {
			tmp.setGroup(grpName, (HashMap<String, String>) data.get(grpName).clone());
		} else {
			tmp.setGroup(grpName, new HashMap<String, String>());
		}
		return tmp;
	}

	/**
	 * <b>Description</b> Sets Data Provider with passed group and its respective Hash Map of Key, Values
	 *                                                      
	 * @param             grpName   Group Name to set in to Data Provider
	 * @param             group  A Hash Map consisting of Column and Values ( Key , Value)
	 */
	@SuppressWarnings("unchecked")
	public synchronized void setGroup(String grpName, HashMap<String, String> group) {
		if (data.containsKey(grpName)) {
			data.get(grpName).putAll(group);
		} else {
			data.put(grpName, (HashMap<String, String>) group.clone());
		}
	}

	/**
	 * <b>Description</b> Sets Data Provider with the Group Name , Data Provider supplied
	 * @param             grpName   Group Name in Data Provider
	 * @param            tmp   Data Provider having only requested Group Name
	 */
	@SuppressWarnings("unchecked")
	public synchronized void setGroup(String grpName, DataProvider tmp) {
		HashMap<String, String> tmpGroup = tmp.getgroup(grpName);
		if (tmpGroup != null) {
			if (data.containsKey(grpName)) {
				data.get(grpName).putAll(tmpGroup);
			} else {
				data.put(grpName, (HashMap<String, String>) tmpGroup.clone());
			}
		}
	}

	/**
	 * <b>Description</b> Returns a HashMap of Key, Value attributes ( Column, Value) from Data Provider, with the Group Name supplied 
	 *                    If Group Name is not present returns a HashMap with null                       
	 * @param             grpName   Group Name in Data Provider
	 * @return            HashMap (Key, Value)  with the requested Group Name
	 */
	private synchronized HashMap<String, String> getgroup(String groupName) {
		HashMap<String, String> tmpGroup = null;
		if (data.containsKey(groupName)) {
			tmpGroup = data.get(groupName);
		}
		return tmpGroup;
	}

	/**
	 * <b>Description</b> Set a Value under respective Group and Column in Data Provider
	 * @param             group   Group Name 
	 * @param             column    Column Name
	 * @param             value     Value
	 */
	public synchronized void set(String group, String column, String value) {
		if (!data.containsKey(group)) {
			data.put(group, new HashMap<String, String>());
		}
		data.get(group).put(column.toLowerCase(), value);
	}

	/**
	 * <b>Description</b> Load Data Provider from Excel sheet for respective Test case Id and Iteration Id
	 *                    If Test case or Iteration is not present in Excel sheet then throws Exception
	 *                                                      
	 * @param             fileName   File Name 
	 * @param             testCase   Test Case Id 
	 * @param             iteration  Iteration Id
	 * @throws            Exception  
	 */
	public synchronized void loadFromExcel(String fileName, String sheetName,
			String testCase, String iteration) throws Exception {
		ExcelHelper ex = new ExcelHelper();
		XSSFSheet mySheet = ex.getsheet(fileName, sheetName);
		int rowNum = ex.findRow(mySheet, testCase.toLowerCase().trim());
		if (rowNum == -1) {
			throw new Exception(
			"There is no such Test Case entry in the data sheet ");
		}
		int displacementrownumber = ex.findDisplaceMentrow(mySheet, rowNum,
				testCase.toLowerCase().trim(), iteration.toLowerCase().trim());
		if (displacementrownumber == -1) {
			throw new Exception(
			"There is no such Iteration entry in the data sheet ");
		}
		if (rowNum > -1 && displacementrownumber > -1) {
			merge(ex.load(mySheet, rowNum, displacementrownumber - 1).clone());
		}
	}

	/*
	 * // To load Excel data in to test Data HashMap public synchronized void
	 * loadFromExcel(String fileName, String sheetName, String testCase,int
	 * iteration ) { ExcelHelper ex = new ExcelHelper(); XSSFSheet mySheet =
	 * ex.getsheet(fileName, sheetName); int rowNum = ex.findRow(mySheet,
	 * testCase); if(rowNum>-1){
	 * merge(ex.load(mySheet,rowNum,iteration).clone());
	 * 
	 * } }
	 */


	/**
	 * <b>Description</b> Verifies group name is present in Data Provider and returns TRUE / FALSE respectively
	 *                                                      
	 * @param             groupName   Group Name to verify in Data Provider
	 * @return            contains    Boolean value TRUE / FALSE
	 */
	public boolean containsGroup(String groupName) {
		boolean contains = false;
		Set<String> keys = data.keySet();
		Iterator<String> keysIterator = keys.iterator();
		while (keysIterator.hasNext()) {
			if (groupName.contains(keysIterator.next().toString())) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	/**
	 * <b>Description</b> Load and merge Ini data to the Data Provider
	 * @param             fileName   Ini file Name
	 * @throws            Exception   Exception
	 */
	public void loadFromIni(String fileName) throws Exception {
		merge(IniHelper.load(fileName));
	}

	/**
	 * <b>Description</b>Save Data Provider to A Ini file. Creates Directory and file if not present
	 * @param             folderName   Folder Name 
	 * @param             fileName     File Name
	 * @throws            Exception    Exception
	 * 
	 */
	public void saveToIni(String folderName, String fileName) throws Exception {
		if (!new File(folderName).isDirectory()){
			new File(folderName).mkdirs();
		}
		IniHelper.save(folderName + File.separator + fileName, clone());
	}

}
