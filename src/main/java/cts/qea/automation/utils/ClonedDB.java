package cts.qea.automation.utils;

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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import cts.qea.automation.DataProvider;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class ClonedDB extends DbHelper {

	/**
	 * <b>Description</b> Returns the number of row a query returns
	 * 
	 * @param query
	 *            Query to Execute
	 * @return boolean flag Returns TRUE if Record Present in DB else FALSE
	 * @exception Exception
	 *                Exception
	 * 
	 */
	public int getResultSetSize(ResultSet rs) throws SQLException {
		rs.last();
		return rs.getRow();
	}

	public void CloneValidation(DataProvider dp, String query) throws Exception {

		connectDatabase();
		ResultSet rs;
		String firstRow, secondRow;
		Report.log("Validating Single Row Query ", Status.BUSINESSSTEP);
		rs = executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		String tablename = query.substring(query.indexOf("FROM") + 5 , query.indexOf("WHERE")-1);
		if (getResultSetSize(rs) == 2) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				rs.first();
				firstRow = rs.getString(rsmd.getColumnName(i));
				dp.set("First", rsmd.getColumnName(i), firstRow);
				rs.last();
				secondRow = rs.getString(rsmd.getColumnName(i));
				dp.set("Second", rsmd.getColumnName(i), secondRow);
				compareRows(firstRow, secondRow, rsmd.getColumnName(i), tablename);
				
			}
		} else
			Report.log(tablename +" : Table Returned rows are not equal to 2 it is "
					+ getResultSetSize(rs) + ". Please check the query",
					Status.fail);

		closeDatabase();

	}

	private void compareRows(String firstRow, String secondRow, String rsmd, String tablename) {
		try {
			if (firstRow.equals(secondRow)) {
				Report.log(tablename+"."+rsmd + " : Value in OriginalRow: " + firstRow + " , Value in ClonedRow: " + secondRow, Status.PASS);
			} else {
				Report.log(tablename+"."+rsmd + " : Value in OriginalRow: " + firstRow + " , Value in ClonedRow: " + secondRow, Status.fail);
			}
		} catch (Exception ex) {
			if (firstRow == null && secondRow == null) {
				Report.log(tablename+"."+rsmd + " : Both rows has NULL values", Status.PASS);
			}else if (firstRow != null && secondRow == null) {
				Report.log(tablename+"."+rsmd + " : Value in OriginalRow: " + firstRow + " , Value in ClonedRow: NULL", Status.fail);
			}else if (firstRow == null && secondRow != null) {
				Report.log(tablename+"."+rsmd + " : Value in OriginalRow: NULL  , Value in ClonedRow: " + secondRow, Status.fail);
			}else {
				Report.log("Some NullPointerException happened : " + ex, Status.fail);
			}
		}

	}
	
	public void MultilineCloneValidation(String query) throws Exception {

		connectDatabase();
		ResultSet rs;
		String FirstRow, SecondRow;
		Report.log("Validating MultiRow Query ", Status.BUSINESSSTEP);
		rs = executeQuery(query);
		String tablename = query.substring(query.indexOf("FROM") + 5 , query.indexOf("WHERE")-1);
		ResultSetMetaData rsmd = rs.getMetaData();
		if ((getResultSetSize(rs) & 1) == 0) {
			int j=1;
			while ((j+1)<=getResultSetSize(rs)){
				Report.log("Comparing Row "+j +" and Row " +(j+1), Status.DONE);
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					rs.absolute(j);
					FirstRow = rs.getString(rsmd.getColumnName(i));
					rs.absolute(j+1);
					SecondRow = rs.getString(rsmd.getColumnName(i));
					compareRows(FirstRow, SecondRow, rsmd.getColumnName(i), tablename);
					
				}
				
				j=j+2;
			}
			
		} else
			Report.log(tablename +" : Table Returned Odd number of rows it is "
					+ getResultSetSize(rs) + ". Please check the query",
					Status.fail);

		closeDatabase();

	}
	
	public void getIdentifiers(DataProvider dp, String query) throws Exception {

		connectDatabase();
		ResultSet rs;
		String FirstRow, SecondRow;
		Report.log("Getting Identifiers for the tables ", Status.BUSINESSSTEP);
		rs = executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		if (getResultSetSize(rs) == 2) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				rs.first();
				FirstRow = rs.getString(rsmd.getColumnName(i));
				dp.set("First", rsmd.getColumnName(i), FirstRow);
				rs.last();
				SecondRow = rs.getString(rsmd.getColumnName(i));
				dp.set("Second", rsmd.getColumnName(i), SecondRow);
				
			}
		} else
			Report.log("Returned rows are not equal to 2 it is "
					+ getResultSetSize(rs) + ". Please check the query",
					Status.fail);

		closeDatabase();

	}
}
