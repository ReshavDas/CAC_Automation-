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

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cts.qea.automation.Config;
import cts.qea.automation.DataProvider;
import cts.qea.automation.exception.TASDBConnectionException;
import cts.qea.automation.exception.TASDBQueryExecutionException;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.security.SecurityUtils;

public class DbHelper {

	private Connection connection = null;
	private static final Logger LOG = LoggerFactory.getLogger(DbHelper.class);

	public DbHelper() {
		super();
		connectDatabase();
	}

	/**
	 * <b>Description</b> Connect to the Database with Config parameter values
	 * serverName, host, username, password, portNumber, url , schema
	 * 
	 * @throws TASDBConnectionException
	 * 
	 * @throws Exception
	 * 
	 */
	public void connectDatabase() {
		if (connection != null) {
			LOG.info("\n DB connection establesed already...!!!\n");
			return;
		}
		String section = Config.getEnvDetails("config", "appname");
		String host = Config.getEnvDetails(section, "DB_HOST");
		String userName = Config.getEnvDetails(section, "DB_USER");
		String portNumber = Config.getEnvDetails(section, "DB_PORT");
		String dbName = Config.getEnvDetails(section, "DB_NAME");
		String dbType = Config.getEnvDetails(section, "DB_TYPE");
		String schema = Config.getEnvDetails(section, "DB_SCHEMA");
		String serviceId = Config.getEnvDetails(section, "DB_SID");
		String tns = Config.getEnvDetails(section, "TNS");
		String wallet = Config.getEnvDetails(section, "WALLET");
		String sqlServerAddtlParams = Config.getEnvDetails(section, "SQLSERVER_ADDTL_PARAMS");
		String passWord;
		try {
			LOG.info("\nDB Connection Started... \nDB Server: " + host + "DB Name: " + dbName + "Port: " + portNumber
					+ "\tUser Name: " + userName + "\t");
			passWord = SecurityUtils.getDecodedValue(Config.getEnvDetails(section, "DB_PWD"));
			connectDatabase(host, userName, passWord, portNumber, dbName, dbType, schema, serviceId, tns, wallet,
					sqlServerAddtlParams);
			LOG.info("DB Connection success.....\n");
		} catch (Exception e) {
			LOG.error("\nError on DB connection and Error Message: " + e.getMessage());
			e.printStackTrace();
			throw new TASDBConnectionException(e);
		}
	}

	public void connectDatabase(String section) throws Exception {
		// Create a connection to the database
		String host = Config.getEnvDetails(section, "DB_HOST");
		String userName = Config.getEnvDetails(section, "DB_USER");
		String passWord = SecurityUtils.getDecodedValue(Config.getEnvDetails(section, "DB_PWD"));
		String portNumber = Config.getEnvDetails(section, "DB_PORT");
		String dbName = Config.getEnvDetails(section, "DB_NAME");
		String dbType = Config.getEnvDetails(section, "DB_TYPE");
		String schema = Config.getEnvDetails(section, "DB_SCHEMA");
		String serviceId = Config.getEnvDetails(section, "DB_SID");
		String tns = Config.getEnvDetails(section, "TNS");
		String wallet = Config.getEnvDetails(section, "WALLET");
		String sqlServerAddtlParams = Config.getEnvDetails(section, "SQLSERVER_ADDTL_PARAMS");
		connectDatabase(host, userName, passWord, portNumber, dbName, dbType, schema, serviceId, tns, wallet,
				sqlServerAddtlParams);
	}

	/**
	 * <b>Description</b> Connect to the Database with formal parameters
	 * 
	 * @param host                 - Host Name
	 * @param userName             - User Name
	 * @param passWord             - Password
	 * @param portNumber           - Port Number
	 * @param dbName               - Database Name
	 * @param dbType               - Database Type
	 * @param schema               - Schema Name
	 * @param serviceId            - Service ID
	 * @param tns                  - TNS Path
	 * @param wallet               - Wallet Path
	 * @param sqlServerAddtlParams - if not blank, value is appended to SQL server
	 *                             connection string
	 * @throws Exception when error occurs
	 */
	protected void connectDatabase(String host, String userName, String passWord, String portNumber, String dbName,
			String dbType, String schema, String serviceId, String tns, String wallet, String sqlServerAddtlParams)
			throws Exception {

		String connectionUrl = StringUtils.EMPTY;

		if (dbType.toLowerCase().trim().equals("sqlserver")) {
			if (host.trim().equals(StringUtils.EMPTY)) {
				LOG.info("Host Name is null....******** required attention ********");
				connectionUrl = "jdbc:h2:./" + dbName + ";";
			} else {
				connectionUrl = "jdbc:sqlserver://" + host + ":" + portNumber + ";" + "databaseName=" + dbName + ";";
			}
			//Bug 1306812
			if (StringUtils.isNotBlank(sqlServerAddtlParams)) {
				connectionUrl = connectionUrl + sqlServerAddtlParams;
			}

			LOG.info("DB Connection String.....: " + connectionUrl);
			if (userName.equals("")) {
				connectionUrl = connectionUrl + "integratedSecurity=true;";
			} else {
				connectionUrl = connectionUrl + "user=" + userName + ";password=" + passWord + ";";
			}
			connection = DriverManager.getConnection(connectionUrl);
		} else if (dbType.toLowerCase().trim().equals("sybase")) {
			try {
				Class.forName("com.sybase.jdbc.SybDriver");
				connection = DriverManager.getConnection("jdbc:sybase:Tds:" + host + ": " + portNumber, userName,
						passWord);
				if (null != connection) {
					System.out.println("Sucessfully " + "connected to Sybase database!!!");
				} else {
					System.out.println("Connection " + "to Sybase database failed!!!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (dbType.toLowerCase().trim().equals("oracle")) {
			if (StringUtils.isBlank(tns)) {
				connectionUrl = "jdbc:oracle:thin:@//" + host + ":" + portNumber + "/" + serviceId;

			} else {
				System.setProperty("oracle.net.tns_admin", tns);
				System.setProperty("oracle.net.wallet_location", wallet);
				connectionUrl = "jdbc:oracle:thin:@" + serviceId ;
			}

			connection = DriverManager.getConnection(connectionUrl, userName, passWord);
			connection.setSchema(schema);
		}
	}

	/**
	 * <b>Description</b> Executes Query and returns ResultSet. Returns null if
	 * fails
	 * 
	 * @param query Query to Execute
	 * @return ResultSet Current ResultSet
	 * @throws Exception
	 * 
	 */
	public ResultSet executeQuery(String query) throws Exception {
		return getResultSet(query);
	}

	/**
	 * <b>Description</b> Gets current Result Set after executing Query. Returns
	 * null if fails
	 * 
	 * @param query: Query to Execute
	 * @return ResultSet Current ResultSet
	 * @exception Exception
	 * 
	 */
	private ResultSet getResultSet(String query) {
		ResultSet rs = null;
		if (query == null || query.equalsIgnoreCase("")) {
			Report.log("No Query is available", Status.DONE);
			return null;
		}
		try {
			PreparedStatement s = getPreparedStatementForQuery(query);// 390661
			Report.log("query", query);
			rs = s.executeQuery();
			if (rs == null) {
				Report.log("Query-> returned with null values", Status.DONE);
			}
		} catch (Exception e) {
			Report.log("Query-> Due to Syntax error, Query :" + query + "has been failed with Exception: " + e,
					Status.Fail);
			e.printStackTrace();
			throw new TASDBQueryExecutionException(e);
		}
		return rs;
	}

	/**
	 * This method used to execute query with multiple query parameter value
	 * 
	 * @param query     - Query string
	 * @param queryParm -> String argument values in order of query parameter
	 * @return
	 * @throws Exception
	 */
	public ResultSet executeQuery(String query, List<String> queryParm) throws Exception {
		if (query == null || query.equalsIgnoreCase("")) {
			Report.log("No Query is available", Status.DONE);
			return null;
		}
		Report.log("query", query);
		AtomicInteger i = new AtomicInteger(0);
		PreparedStatement ps = getPreparedStatementForQuery(query);
		queryParm.forEach(a -> {
			try {
				ps.setString(i.getAndIncrement(), a);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new TASDBQueryExecutionException(e);
			}
		});
		return ps.executeQuery();
	}

	/**
	 * TODO Not completed all testing This method used to execute query with
	 * multiple query parameter value
	 * 
	 * @param query     - Query string
	 * @param queryParm -> multiple type of parameters
	 * @return
	 * @throws Exception
	 */
	public ResultSet executeQuery(String query, Map<String, Object> queryParm) throws Exception {
		if (query == null || query.equalsIgnoreCase("")) {
			Report.log("No Query is available", Status.DONE);
			return null;
		}
		Report.log("query", query);
//		AtomicInteger i = new AtomicInteger(0);
//		PreparedStatement ps = getPreparedStatementForQuery(query);
		NamedParameterStatement ps = new NamedParameterStatement(connection, query);
		queryParm.keySet().forEach(a -> {
			setQueryParameterForType(ps, a, queryParm.get(a));
		});

		return ps.executeQuery();
	}

	@SuppressWarnings("rawtypes")
	private void setQueryParameterForType(NamedParameterStatement nps, String qurParam, Object o) {
		try {
			switch (o.getClass().getName()) {
			case "java.lang.String":
			case "java.lang.Character":
				nps.setString(qurParam, o.toString());
				break;
			case "java.lang.Integer":
				nps.setInt(qurParam, Integer.valueOf(o.toString()));
				break;
			case "java.util.List":
				nps.setArray(qurParam, getArrayValue((List) o));
				break;
			case "java.util.Set":
				nps.setArray(qurParam, getArrayValue((Set) o));
				break;
			default:
				LOG.info("Type Not supported: " + o.getClass().getName());
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private Array getArrayValue(List o) {
		return (Array) Arrays.asList(o.toArray());
	}

	@SuppressWarnings("rawtypes")
	private Array getArrayValue(Set o) {
		return (Array) Arrays.asList(o.toArray());
	}

	private PreparedStatement getPreparedStatementForQuery(String query) throws SQLException {
		return connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * <b>Description</b> Close Database Connection
	 * 
	 * @throws Exception Exception
	 * 
	 */
	public void closeDatabase() throws Exception {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * <b>Description</b> Gets current Result Set after executing Query. Returns
	 * false if fails
	 * 
	 * @param dp:        Data Provider
	 * @param rs:        Current ResultSet
	 * @param groupName: Group Name
	 * @throws Exception: Exception
	 * 
	 */
	public boolean saveResultSet(DataProvider dp, ResultSet rs, String groupName) throws Exception {
		CachedRowSet metaData = null;
		if (rs != null) {
			metaData = RowSetProvider.newFactory().createCachedRowSet();
			metaData.populate(rs);
			int colCount = metaData.getMetaData().getColumnCount();
			if (metaData.next()) {
				for (int i = 1; i <= colCount; i++) {
					String columnName = metaData.getMetaData().getColumnName(i);
					String value = metaData.getString(i);
					dp.set(groupName, columnName, value);
					Report.log(columnName + " retrieved  from DB is " + value, Status.DONE);
				}
			} else {
				Report.log("Query-> returned with null values", Status.DONE);
				metaData.close();
				return false;
			}
		} else {
			Report.log("Query-> returned with null values", Status.DONE);
			return false;
		}
		metaData.close();
		return true;
	}

	/**
	 * <b>Description</b> Executes Query and Returns TRUE or FALSE for a Record
	 * Present in DB or not respectively
	 * 
	 * @param query Query to Execute
	 * @return boolean flag Returns TRUE if Record Present in DB else FALSE
	 * @exception Exception Exception
	 * 
	 */
	public boolean isRecordPresent(String query) throws Exception {
		return isPresent(query);
	}

	/**
	 * <b>Description</b> Executes Query and Returns TRUE or FALSE for a Record
	 * Present in DB or not respectively
	 * 
	 * @param query Query to Execute
	 * @return boolean flag Returns TRUE if Record Present in DB else FALSE
	 * @exception Exception Exception
	 * 
	 */
	private boolean isPresent(String query) {
		ResultSet rs = null;
		if (query == null || query.equalsIgnoreCase("")) {
			Report.log("No Query is available", Status.DONE);
			return true;
		}
		try {
			PreparedStatement s = connection.prepareStatement(query);
			rs = s.executeQuery();
			if (!rs.next()) {
				Report.log("Query-> returned Zero Records from DB", Status.DONE);
				return false;
			}
		} catch (Exception e) {
			Report.log("Query-> Due to Syntax error, Query :" + query + "has been failed with Exception: " + e,
					Status.Fail);
			e.printStackTrace();
		}
		return true;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
