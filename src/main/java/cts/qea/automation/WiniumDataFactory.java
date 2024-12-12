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

package cts.qea.automation;

import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.util.Iterator;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.utils.DbHelper;

public abstract class WiniumDataFactory {

	/**
	 * <b>Description</b> Wrapper method for Loading TDM class and its respective Data Provider
	 *                    and Execute SQL / Create TDM.              
	 *                                                      
	 * @param             className    TDM Class Name
	 * @param             dataId       TDM Class Iteration Id
	 * @param             tcDP         Test Case Data Provider 
	 * @param             driver       WebDriver 
	 * @return            boolean      Boolean 
	 */
	public static boolean loadData(String className, String dataId, DataProvider tcDP, WrappedWiniumDriver driver) throws Exception{
		boolean executeSql = tcDP.get("Flags","executeSql").toLowerCase().equalsIgnoreCase("y"); 
		boolean createTDM = tcDP.get("Flags","tdmCreate").toLowerCase().equalsIgnoreCase("y");

		return loadData(className, dataId, tcDP, driver, executeSql, createTDM);
	}
	
	/**
	 * <b>Description</b> Load TDM Class, Respective Data Provider and execute 
	 *                    SQL / Create TDM flow           
	 *                                                      
	 * @param             className        TDM Class Name
	 * @param             dataId           TDM Class Iteration Id
	 * @param             tcDP             Test Case Data Provider 
	 * @param             executeSql       execute SQL flag 
	 * @param             createTDM        createTDM  flag 
	 * @param             driver           WebDriver 
	 * @return            boolean          Boolean 
	 */
	public static boolean loadData(String className, String dataId, DataProvider tcDP, WrappedWiniumDriver driver, boolean executeSql, boolean createTDM) throws Exception {
		boolean recordsFound = false;

		String fileName = Config.getEnvDetails("testData", "tdmFileName");
		String sheetName = Config.getEnvDetails("testData", "tdmSheetName");

		WiniumDataFactory dataClass = null;
		dataId = tcDP.get("TdmRowId", dataId);

		if(dataId.trim().equals("")){
			Report.log("Failed while performing Data factory: dataId should not be empty", Status.FAIL);
			throw new InvalidParameterException("dataId should not be empty");
		}

		try{
			dataClass = (WiniumDataFactory) Class.forName(className).getDeclaredConstructor().newInstance();
			sheetName = Class.forName(className).getSimpleName();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			Report.log("Exception Occured due to :"+e, Status.FAIL);
			return false;
		}

		//		option1 class name comparison
		if(Class.forName(className).getSimpleName().equalsIgnoreCase("GenericSqlFactory")){
			executeSql = true;
			createTDM = false;
		}

		DataProvider dpPreReq = new DataProvider();
		dpPreReq.loadFromExcel(fileName, sheetName, dataClass.getClass().getSimpleName(), dataId);

		dpPreReq.setGroup("PreRequisite", tcDP.getGroup("PreRequisite"));
		dpPreReq.setGroup("credentials", tcDP.getGroup("credentials"));
		dpPreReq.setGroup("DFSub", tcDP.getGroup("DFSub"));
		
		if(executeSql){
			Report.log("Execute Sql for "+(Class.forName(className).getSimpleName())+" Data Factory whose DataRowId is: " +dataId, Status.BUSINESSSTEP);
			recordsFound = dataClass.executeSQL(dpPreReq);
			Report.closeBusinessStep();
		}

		if(!recordsFound && createTDM){
			Report.log("Create TDM for "+(Class.forName(className).getSimpleName())+" Data Factory whose DataRowId is: " +dataId, Status.BUSINESSSTEP);
			recordsFound = dataClass.executeTDM(driver,dpPreReq);
			Report.closeBusinessStep();
		}

		if(recordsFound){
			tcDP.setGroup("PreRequisite",dpPreReq);

		} else{
			Report.log("Failed while performing datafactory: "+(Class.forName(className).getSimpleName()), Status.FAIL);
			throw new Exception("Exception while performing datafactory");
		}

		return recordsFound;
	}

	/**
	 * <b>Description</b> Executes SQL method for each Data Factory
	 * @param             dp          TDM Data Provider
	 * @return            boolean     Boolean 
	 * @throws Exception 
	 */
	@SuppressWarnings({"rawtypes"})
	public boolean executeSQL(DataProvider dp) throws Exception{

		try{ 
			DbHelper dataBase = new DbHelper();
			dataBase.connectDatabase();
			Iterator it = dp.columnIterator("Query Group");
			while (it.hasNext()){
				String  columnName = it.next().toString();
				String query = dp.get("Query Group", columnName);

				if(query.equalsIgnoreCase("")){
					Report.log("No Query is available", Status.DONE);
					return false;
				}
				if(!executeDependentQuery(query, dp))
					return false;                                       
			}  
			dataBase.closeDatabase();
		}catch (Exception e) {
			throw e;                                    
		}              
		return true;
	}

	/**
	 * <b>Description</b> Abstract method for Creating TDM
	 *                                                      
	 * @param             dp          TDM Data Provider
	 * @param             driver      WebDriver
	 * @return            boolean     Boolean 
	 */
	public abstract boolean executeTDM(WrappedWiniumDriver wdriver,DataProvider dp) throws Exception;

	public boolean executeDependentQuery(String query, DataProvider dp) throws Exception{
		DbHelper dataBase = new DbHelper();
		dataBase.connectDatabase();
		boolean flag = true;
		String queryFinal = query;
		Iterator <String> it = dp.columnIterator("DFSub");
		while (it.hasNext()){
			String columnName = it.next().toString().trim();
			if(queryFinal.toLowerCase().contains(("{"+ columnName +"}").toLowerCase())){	
				String value = dp.get("DFSub", columnName);
				queryFinal = queryFinal.replace(("{"+ columnName +"}").toLowerCase(), value);
			}
		}
		ResultSet rs = dataBase.executeQuery(queryFinal);
		if(!saveResultSet(dp, rs, "PreRequisite"))
			flag = false;
		dataBase.closeDatabase();
		return flag;
	}

	/**
	 * <b>Description</b> Gets current Result Set after executing Query.  Returns false if fails
	 *  
	 * @param             dp          Data Provider 
	 * @param             rs          Current ResultSet
	 * @param             groupName   Group Name
	 * @throws            Exception   Exception
	 * 
	 */	
	public boolean saveResultSet(DataProvider dp, ResultSet rs,String groupName) throws Exception {
		CachedRowSet metaData = null;
		if(rs != null){
			metaData = RowSetProvider.newFactory().createCachedRowSet();
			metaData.populate(rs);
			int colCount = metaData.getMetaData().getColumnCount();
			if(metaData.next()){
				for(int i = 1; i <= colCount; i++){				
					String columnName = metaData.getMetaData().getColumnName(i);
					String value = metaData.getString(i);
					dp.set(groupName, columnName, value);
					Report.log(columnName+" retrieved  from DB is "+value, Status.DONE);
				}
			}else{
				Report.log("Query-> returned with null values", Status.DONE);
				return false;
			}			
		}else{
			Report.log("Query-> returned with null values", Status.DONE);
			return false;
		}
		return true;
	}

}
