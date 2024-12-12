/**
 * 
 */
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import cts.qea.automation.Config;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

/**
 * 
 *
 */
@Deprecated
public class DBUtils {

	public static Connection getSqlServerConnection(String section) {

		String host = Config.getEnvDetails(section, "DB_HOST");
//		String userName = Config.getEnvDetails(section, "DB_USER");
//		String passWord = Config.getEnvDetails(section, "DB_PWD");
		String portNumber = Config.getEnvDetails(section, "DB_PORT");
		String dbName = Config.getEnvDetails(section, "DB_NAME");

		String connectionUrl = "jdbc:sqlserver://" + host + ":" + portNumber + ";" + "databaseName=" + dbName + ";"
//		+"user="+ userName + ";password=" + passWord + ";"
				+ "integratedSecurity=true;";
		Connection con = null;
		try {
			con = DriverManager.getConnection(connectionUrl);
		} catch (SQLException e) {
			Report.log(e);
			e.printStackTrace();
		}

		return con;
	}

	/**
	 * <b>Description</b> Returns the number of row a query returns
	 * 
	 * @param query Query to Execute
	 * @return boolean flag Returns TRUE if Record Present in DB else FALSE
	 * @exception Exception Exception
	 * 
	 */
	public static int getResultSetSize(ResultSet rs) throws SQLException {
		rs.last();
		return rs.getRow();
	}

	/**
	 * <b>Description</b> Executes Query and returns ResultSet. Returns null if
	 * fails
	 * 
	 * @param connection
	 * @param query      - Query to Execute
	 * @return
	 * @throws Exception
	 */
	public static ResultSet executeQuery(Connection connection, String query) throws Exception {
		ResultSet rs = null;
		if (query == null || query.equalsIgnoreCase("")) {
			Report.log("No Query is available", Status.DONE);
			return null;
		}
		try {
			PreparedStatement s = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);// 390661
			Report.log("SQL Query used is: " + query, Status.DONE);
			rs = s.executeQuery();
			if (rs == null) {
				Report.log("Query-> returned with null values", Status.DONE);
			}
		} catch (Exception e) {
			Report.log("Query-> Due to Syntax error, Query :" + query + "has been failed with Exception: " + e,
					Status.Fail);
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * <b>Description</b> Close Database Connection
	 * 
	 * @throws Exception Exception
	 * 
	 */
	public static void close(Connection connection) throws Exception {
		connection.close();
	}

	public static ArrayList<String> getColumnNames(ResultSet rs) {
		ArrayList<String> columnname = new ArrayList<String>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				columnname.add(rsmd.getColumnName(i));
			}
		} catch (SQLException e) {
			Report.log(e);
			e.printStackTrace();
		} catch (NullPointerException e) {
			Report.log("Column name specified in the query doesnot exist!", Status.FAIL);
		}
		return columnname;

	}

	public static ArrayList<String> getValuesByColumnName(ResultSet rs, String columnname) {

		ArrayList<String> columnvalue = new ArrayList<String>();
		try {
			rs.beforeFirst();
			while (rs.next()) {
				columnvalue.add(rs.getString(columnname));
			}
		} catch (SQLException e) {
			Report.log(e.getMessage(), Status.Fail);
		}
		return columnvalue;
	}

	public static String getValue(ResultSet rs) {
		String columnvalue = "";
		try {
			rs.beforeFirst();
			while (rs.next()) {
				columnvalue = rs.getString(1);
			}
		} catch (SQLException e) {
//			Report.log(e.getMessage(), Status.Fail);
		}
		return columnvalue;
	}

	public static ArrayList<String> getValues(ResultSet rs) {

		ArrayList<String> columnvalue = new ArrayList<String>();
		try {
			rs.beforeFirst();
			while (rs.next()) {
				columnvalue.add(rs.getString(1));
			}
		} catch (SQLException e) {
			Report.log(e.getMessage(), Status.Fail);
		}
		return columnvalue;
	}
}
