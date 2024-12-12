package cts.qea.automation.alm;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cts.qea.automation.Config;
import cts.qea.automation.security.SecurityUtils;
import cts.qea.automation.utils.Validator;


/**
 * Class to integrate ALM for test results upload
 * 
 */
public class ALMRestCommonUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ALMRestCommonUtils.class);
	private String strProjectName = "";
	private String strDomainName = "";

	private String URL_BASE = "";
	private String URL_SIGN_IN = URL_BASE + "/api/authentication/sign-in";
	private String URL_SIGN_OUT = URL_BASE + "/api/authentication/sign-out";

	private String URL_FETCH_TEST_SET_INSTANCES = URL_BASE + "/rest/domains/" + strDomainName + "/projects/" + strProjectName + "/test-instances?query={cycle-id[CYCLE_ID]}&page-size=max";
	private String URL_FETCH_TEST_INSTANCE_DETAILS = URL_BASE + "/rest/domains/" + strDomainName + "/projects/" + strProjectName + "/test-instances/TEST_INSTANCE_ID";
	private String URL_CREATE_NEW_TEST_INSTANCE_RUN = URL_BASE + "/rest/domains/" + strDomainName + "/projects/" + strProjectName + "/runs";
	private String URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW = URL_BASE + "/rest/domains/" + strDomainName + "/projects/" + strProjectName + "/test-instances/TEST_INSTANCE_ID";
	private String URL_FETCH_TEST_INSTANCE_RUN_DETAILS = URL_BASE + "/rest/domains/" + strDomainName + "/projects/" + strProjectName + "/runs?query={cycle-id[CYCLE_ID];test-id[TEST_ID]}&page-size=max";
	private String URL_UPLOAD_TEST_SET_INSTANCES_RESULT = URL_BASE + "/rest/domains/" + strDomainName + "/projects/" + strProjectName + "/runs/RUN_ID/attachments";
	private String URL_UPDATE_NEW_TEST_INSTANCE_RUN = URL_BASE + "/rest/domains/DEFAULT/" + strDomainName + "/projects/" + strProjectName + "/runs/RUN_ID";

	/*private String XSRF_TOKEN = "";
	private String ALM_USER = "";
	private String QCSession = "";
	private String LWSSO_COOKIE_KEY = "";*/
	private String almPassword = "";
	private List<String> listCommonResponseCookie;
	private List<String> listCommonRequestCookie = new ArrayList<String>();
	private String ext_strTestInstanceDetails = "";
	private String ext_strTestInstanceNewRunDetails = "";
	private String ext_strTestInstanceID;
	private String ext_strTestID;
	private String ext_strTestSetID = "";
	private String ext_strRunID;
	private String ext_strCurrentStatus;
	private String ext_strTestcaseName;
	private String ext_strComments;
	private String ext_strReportPath;
	private boolean ext_flagAttachFileOnlyForFailure = false;
	private String ext_strTesterName;

	private boolean flagStatusUdateRequired = false;
	Date objDate = new Date();
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public String strTestInstanceRunData = "";
	public String strTestInstanceRunAbstractDetails = "";

	/**
	 * create ALM Rest object which will further used for ALM interactions
	 * 
	 * @param baseURL
	 * @param projectName
	 * @param domainName
	 * @param almPassword
	 * @param testSetID
	 * @param testcaseName
	 * @param comments
	 * @param reportPath
	 * @param status
	 * @param testerName
	 * @param flagAttachFileOnlyForFailure
	 * @throws Exception
	 */
	public ALMRestCommonUtils(String baseURL, String projectName, String domainName, String almPassword,
			String testSetID, String testcaseName, String comments, String reportPath, String status, String testerName,
			boolean flagAttachFileOnlyForFailure) throws Exception {
		super();
		try {
			if (!(baseURL != null && baseURL != "" && projectName != null && projectName != "" && domainName != null
					&& domainName != "" && testSetID != null && testSetID != "" && testcaseName != null
					&& testcaseName != "" && reportPath != null && reportPath != "" && status != null
					&& status != "")) {
				throw new Exception("Exception : one of the arguments is missing");
			}
			this.URL_BASE = baseURL;
			this.almPassword = almPassword;
			this.strProjectName = projectName;
			this.strDomainName = domainName;
			this.ext_strTestSetID = testSetID;
			this.ext_strTestcaseName = testcaseName;
			this.ext_strComments = comments;
			this.ext_strReportPath = reportPath;
			this.ext_strCurrentStatus = status;
			if (testerName.trim() == "") {
				this.ext_strTesterName = "s-almautomation";
			} else {
				this.ext_strTesterName = testerName;
			}

			this.ext_flagAttachFileOnlyForFailure = flagAttachFileOnlyForFailure;
			URL_SIGN_IN = URL_BASE + "/api/authentication/sign-in";
			URL_SIGN_OUT = URL_BASE + "/api/authentication/sign-out";

			URL_FETCH_TEST_SET_INSTANCES = URL_BASE + "/rest/domains/" + domainName + "/projects/" + projectName + "/test-instances?query={cycle-id[CYCLE_ID]}&page-size=5000";
			URL_FETCH_TEST_INSTANCE_DETAILS = URL_BASE + "/rest/domains/" + domainName + "/projects/" + projectName + "/test-instances/TEST_INSTANCE_ID";
			URL_CREATE_NEW_TEST_INSTANCE_RUN = URL_BASE + "/rest/domains/" + domainName + "/projects/" + projectName + "/runs";
			URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW = URL_BASE + "/rest/domains/" + domainName + "/projects/" + projectName + "/test-instances/TEST_INSTANCE_ID";
			URL_FETCH_TEST_INSTANCE_RUN_DETAILS = URL_BASE + "/rest/domains/" + domainName + "/projects/" + projectName + "/runs?query={cycle-id[CYCLE_ID];test-id[TEST_ID]}&page-size=max";
			URL_UPLOAD_TEST_SET_INSTANCES_RESULT = URL_BASE + "/rest/domains/" + domainName + "/projects/" + projectName + "/runs/RUN_ID/attachments";
			URL_UPDATE_NEW_TEST_INSTANCE_RUN = URL_BASE + "/rest/domains/" + domainName + "/projects/" + projectName + "/runs/RUN_ID";

			LOG.info("\n-------------ALM CONNECTION PROPERTIES----------------------------");
			LOG.info("ALM URL : " + baseURL);
			LOG.info("Project Name : " + projectName);
			LOG.info("Domain Name : " + domainName);
			LOG.info("Test Set ID : " + testSetID);
			LOG.info("Testcase Name : " + testcaseName);
			LOG.info("Tester Name : " + ext_strTesterName);
			LOG.info("Results Status: " + status);
			LOG.info("Report Path : " + reportPath);
			LOG.info("Failure Only Attachment Flag : " +ext_flagAttachFileOnlyForFailure);

			LOG.info("-----------------------------------------");

		} catch (Exception e) {
			throw new Exception(e.getMessage() + "\nALM Integration Error: Usage= ALMRestCommonUtils(<ALM BASE URL>,<PROJECT NAME>,<DOMAIN NAME>,<CREDENTIALS>,<TEST SET ID>,<TESTCASE NAME>,<COMMENTS>,<REPORT PATH>,<TC STATUS>,<TESTER NAME>)");
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		/*ALMRestCommonUtils objALMRestUtil = null;
		try {
			objALMRestUtil = new ALMRestCommonUtils("http://prodpc12almv1.tmghealth.com:8080/qcbin"
, "Clear_Spring_Health",
					"IMPLEMENTATIONS", "cG5hZ2FyYWphbjpHcmFzc0AwNA==", "4602", "test1", "ALMRestAPI", "C:\\Users\\bpreetham\\Desktop\\TMG_Call\\CallTestCase\\report\\Run_20201116_134040.zip", "Passed",
					"prengasamy", false);
			objALMRestUtil.LoginALM();
			objALMRestUtil.updateResultsWithAttachement();
			objALMRestUtil.LogoutALM();
		} catch (Exception exception) {
			LOG.error(exception.getMessage());
		}*/
		/*try {
			objALMRestUtil = new ALMRestCommonUtils("http://10.32.100.57:8080/qcbin", "TruProvider",
					"APP_RATIONALIZATION", "", "3829", "TC001_MyTest", "ALMRestAPI", "I:\\hello.zip", "Failed",
					"s-almautomation", false);
			objALMRestUtil.LoginALM();
			objALMRestUtil.updateResultsWithAttachement();
			objALMRestUtil.LogoutALM();
		} catch (Exception exception) {
			LOG.info(exception.getMessage());
		}*/

		LOG.info(StringUtils.EMPTY+StringUtils.getJaroWinklerDistance("TC001_CR025554CFM_CMS COB-OHI File Load Process [1]", "[1]TC001_CR025554CFM_CMS COB-OHI File Load Process"));
	}

	/**
	 * 
	 * @param testSetID
	 * @param testcaseName
	 * @param comments
	 * @param reportPath
	 * @param status
	 * @throws Exception
	 */
	public static void updateALM(String testSetID, String testcaseName, String comments, String reportPath, String status) throws Exception {
		String url = Config.getEnvDetails("ALM", "URL");
		String password  = SecurityUtils.getDecodedValue(Config.getEnvDetails("ALM", "Password"));
		String domain = Config.getEnvDetails("ALM", "Domain");
		String project = Config.getEnvDetails("ALM", "Project");
		String userName = Config.getEnvDetails("ALM", "Username");

		ALMRestCommonUtils objALMRestUtil = new ALMRestCommonUtils(url , project, domain, password, testSetID, testcaseName, comments, reportPath, status, userName, false);
		objALMRestUtil.LoginALM();
		objALMRestUtil.updateResultsWithAttachement();
		objALMRestUtil.LogoutALM();
	}

	/**
	 * updates the final TC status using Run ID and uploads the zip results in ALM
	 * 
	 * @return
	 * @throws Exception
	 */

	public String updateResultsWithAttachement() throws Exception {
		String strRunID = updateGridRowTCStatusInALM();
		// fetches all test instances from test set
		String strTestSetInstances = fetchTestSetInstances();
		// updates the testInstanceID and TestID from given response
		updateTestInstanceDetails(strTestSetInstances);
		updateNewTestInstanceRunWithRunID(strRunID);
		URL_UPLOAD_TEST_SET_INSTANCES_RESULT = URL_UPLOAD_TEST_SET_INSTANCES_RESULT.replaceAll("RUN_ID", strRunID);
		File reportFile = new File(ext_strReportPath);
		LOG.info("Info:Report File path-" + ext_strReportPath);
		if (ext_flagAttachFileOnlyForFailure == true) {
			LOG.info("Note : Report Attached only for the failed testcases");
			if (ext_strCurrentStatus.contains("Fail")) {
				if (reportFile.exists()) {
					LOG.info("Report Exists : " + ext_strReportPath);
					uploadtestSetInstanceResult(ext_strReportPath);
				} else {
					LOG.info("Report File Not Found : " + ext_strReportPath);
				}
			}
		} else {
			if (reportFile.exists()) {
				LOG.info("Report Exists : " + ext_strReportPath);
				uploadtestSetInstanceResult(ext_strReportPath);
			} else {
				LOG.info("Report File Not Found : " + ext_strReportPath);
			}
		}
		LOG.info("TASK COMPLETED");
		return "DONE";
	}

	/**
	 * helps to update the status in grid level
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateGridRowTCStatusInALM() throws Exception {
		
		LOG.info("test1");
		// fetches all test instances from test set
		String strTestSetInstances = fetchTestSetInstances();
		
		// updates the testInstanceID and TestID from given response
		updateTestInstanceDetails(strTestSetInstances);
		// updates grid view data for a test instance
		updateTestInstanceGridViewRow();
		if (flagStatusUdateRequired) {
			createNewTestInstanceRun();
		}
		String testInstanceRunDetails = fetchTestInstanceRunDetails();
		updateTestInstanceRunDetails(testInstanceRunDetails);
		LOG.info("Run ID :" + ext_strRunID);
		return this.ext_strRunID;
	}

	private void updateNewTestInstanceRunWithRunID(String strRunID) throws Exception {
		// LOG.info("Start Method : updateNewTestInstanceRunWithRunID");
		ext_strTestInstanceNewRunDetails = fetchTestInstanceNewRunDataFormat();
		// LOG.info("fetch the test Instance details" +
		// ext_strTestInstanceNewRun);
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_CASE_NAME", ext_strTestcaseName);
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_INSTACE_ID", ext_strTestInstanceID);
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_SET_ID", ext_strTestSetID);
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("HOST_NAME",
				java.net.InetAddress.getLocalHost().getHostName());
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("STATUS", ext_strCurrentStatus);
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("OS_NAME", System.getProperty("os.name"));
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("EXEC_DATE", dateFormat.format(objDate));
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("EXEC_TIME", timeFormat.format(objDate));
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_ID", ext_strTestID);
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("COMMENT", ext_strComments);
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("ATTACHMENT", "N");
		ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("OWNER", ext_strTesterName);

		URL_UPDATE_NEW_TEST_INSTANCE_RUN = URL_UPDATE_NEW_TEST_INSTANCE_RUN.replaceAll("RUN_ID", strRunID);

		LOG.info("TEST_INSTACE_ID: " + ext_strTestInstanceID);
		LOG.info("TEST_ID: " + ext_strTestID);
		LOG.info("HOST_NAME: "+ java.net.InetAddress.getLocalHost().getHostName());	

		// LOG.info("Updated fetch the test Instance details" +
		// URL_UPDATE_NEW_TEST_INSTANCE_RUN);
		//LOG.info("updateNewTestInstanceRunWithRunID : "+ ext_strTestInstanceNewRunDetails);		
		URL objURL = new URL(URL_UPDATE_NEW_TEST_INSTANCE_RUN);
		// LOG.info("Start : My URL Connection" + objURL);
		HttpURLConnection objALMConnection = getAlMConnection(objURL, "PUT");
		// LOG.info("URL Connection details" + objALMConnection);
		OutputStream objOutStream = (objALMConnection.getOutputStream());
		objOutStream.write(ext_strTestInstanceNewRunDetails.getBytes());
		objOutStream.flush();
		// LOG.info("URL Connection Start :" + objALMConnection);
		objALMConnection.connect();
		int responseCode = objALMConnection.getResponseCode();
		// LOG.info("Reponse code : " + responseCode);
		LOG.info("ALM Connection status code: " + responseCode);
		if (responseCode == 200) {
			responseLog(objALMConnection.getHeaderFields());
			BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getInputStream())));
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
		} else {
			if (objALMConnection.getErrorStream() != null) {
				BufferedReader objBufferReader = new BufferedReader(new InputStreamReader((objALMConnection.getErrorStream())));
				StringBuilder objStringBuilder = new StringBuilder();
				String output;
				while ((output = objBufferReader.readLine()) != null) {
					objStringBuilder.append(output);
				}
				// LOG.info("Error in updateNewTestInstanceRunWithRunID Method : " +
				// objStringBuilder.toString());
				LOG.error("ALM Connection Error: " + objStringBuilder.toString());
			} else {
				// LOG.info("Response Code in updateNewTestInstanceRunWithRunID Method
				// : " + responseCode);
				LOG.info("ALM Response Code is not error and Unprocessed code: " + responseCode);
			}

			throw new Exception("Response Code:" + responseCode + "\n" + "One of the Reasons :\n"
					+ "	a.Invalid Run ID\n" + "	b.Invalid Parameter (Project Name|Project domain)");
		}

		objALMConnection.disconnect();
		// LOG.info("Stop Method : updateNewTestInstanceRunWithRunID");

	}

	private void responseLog(Map<String, List<String>> map) {
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			LOG.info("ALM Respon details - Code: " + entry.getKey() + "\t Value: " + entry.getValue());
		}
	}

	/**
	 * 
	 * login into HP ALM and fetches cookies for next request header
	 * 
	 * @throws Exception
	 */
	public void LoginALM() throws Exception {
		try {
			System.setProperty("http.nonProxyHosts", "0.0.0.0|localhost*|127.0.0.1|10.207.209.36*|172.30.57.118*");
			System.setProperty("socksProxyHost", "");
			System.setProperty("socksProxyPort", "");
			URL objURL = new URL(URL_SIGN_IN);
			// LOG.info(URL_SIGN_IN);
			HttpURLConnection objALMConnection = (HttpURLConnection) objURL.openConnection();
			objALMConnection.setConnectTimeout(5000);
			String basicAuth = "";
//			if (almPassword.equalsIgnoreCase("") || almPassword == null) {
//				almPassword = "cy1hbG1hdXRvbWF0aW9uOmNSPDQhbDRFb2w9QmpFZEk=";
//			}
//			basicAuth = "Basic " + almPassword;
			
//			byte[] credBytes = (ext_strTesterName + ":" + almPassword).getBytes();
	        basicAuth = "Basic " + Base64.encodeBase64String((ext_strTesterName + ":" + almPassword).getBytes());
	        LOG.info(objALMConnection.getURL().toString());
            objALMConnection.setRequestProperty("Authorization", basicAuth);
            objALMConnection.setRequestMethod("GET");
			objALMConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			objALMConnection.setRequestProperty("Content-Language", "en-US");
			objALMConnection.setUseCaches(false);
			int responseCode = objALMConnection.getResponseCode();

			if (responseCode == 200) {
				Map<String, List<String>> map = objALMConnection.getHeaderFields();
				for (Map.Entry<String, List<String>> entry : map.entrySet()) {
					if (entry.getKey() != null && entry.getKey().equals("Set-Cookie")) {
						listCommonResponseCookie = entry.getValue();
					}
				}
			} else {
				LOG.info("Error: ALM Login Fail... ALM Response code: " + responseCode);
				throw new Exception("ALM Login Fail and response code:" + responseCode);
			}
			objALMConnection.disconnect();
		} catch (Exception exception) {
			throw new Exception("Error code " + exception.getMessage() + "\n" + "one of the reasons and please verify the configuration details,\n" + "a.Incorrect URL\n" + "b.Incorrect Credentials");
		}
		LOG.info("Info:Connected to ALM");
	}

	/**
	 * 
	 * login into HP ALM and fetches cookies for next request header
	 * 
	 * @throws Exception
	 */
	public void LogoutALM() throws Exception {
		try {
			System.setProperty("http.nonProxyHosts", "0.0.0.0|localhost*|127.0.0.1|10.207.209.36*|172.30.57.118*");
			System.setProperty("socksProxyHost", "");
			System.setProperty("socksProxyPort", "");

			System.setProperty("http.nonProxyHosts", "0.0.0.0|localhost*|127.0.0.1|10.207.209.36*|172.30.57.118*");
			System.setProperty("socksProxyHost", "");
			System.setProperty("socksProxyPort", "");

			URL objURL = new URL(URL_SIGN_OUT);
			HttpURLConnection objALMConnection = getAlMConnection(objURL, "GET");
			// LOG.info("URL Connection details" + objALMConnection);
			int responseCode = objALMConnection.getResponseCode();
			if (responseCode == 200) {
				LOG.info("Logout Response code : " + responseCode);
			} else {
				throw new Exception("Logout response code :" + responseCode);
			}
			objALMConnection.disconnect();

		} catch (Exception exception) {
			throw new Exception("Error code " + exception.getMessage());
		}
		LOG.info("Info:Logged Out");
		LOG.info("------------------ALM Connection Logged Out-----------------------");
	}

	/**
	 * 
	 * fetches the test set instances under test set id
	 * 
	 * @return
	 * @throws Exception
	 */
	private String fetchTestSetInstances() throws Exception {
		StringBuilder testSetInstanceDetails = new StringBuilder();
		try {
			LOG.info("Fetch Test Instance details Method Start");
			if (URL_FETCH_TEST_SET_INSTANCES.indexOf("CYCLE_ID") != -1) {
				URL_FETCH_TEST_SET_INSTANCES = URL_FETCH_TEST_SET_INSTANCES.replace("CYCLE_ID", ext_strTestSetID);
			}
			URL objURL = new URL(URL_FETCH_TEST_SET_INSTANCES);
			HttpURLConnection objALMConnection = getAlMConnection(objURL, "GET");
			LOG.info("URL Connection details: " + objALMConnection);
			int responseCode = objALMConnection.getResponseCode();
			if (responseCode == 200) {
				LOG.info("Response code is : " + responseCode);
				//				Map<String, List<String>> map = objALMConnection.getHeaderFields();
				BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getInputStream())));
				String output;
				while ((output = br.readLine()) != null) {
					testSetInstanceDetails.append(output);
				}
			} else {
				LOG.info("Error in Fetch Test Instance details Method throwing response code" + responseCode);
				throw new Exception("Response Code" + responseCode);
			}
			objALMConnection.disconnect();
			LOG.info("Connection Disconnect");
			Document document = getXmlDocument(testSetInstanceDetails.toString());
			NodeList list = document.getElementsByTagName("Entity");
			if (list.getLength() == 0) {
				LOG.info("Error in Fetch Test Instance details Method throwing zero instances");
				throw new Exception("Fetched test cases/instances -0");
			}
		} catch (Exception exception) {
			throw new Exception("Error :while fetching the Test set instances/test cases \n " + exception.getMessage()
			+ "\n" + "\nOne of the reasons\n" + "	a.Incorrect Project Name\n" + "	b.Incorrect Test Set ID\n"
			+ "	c.Incorrect Doamin Name\n" + "	d.No test cases added in Test Set");
		}
		LOG.info("Info:Fetched Records from ALM");
		LOG.info("Fetch Test Instance details Method Completed");
		return testSetInstanceDetails.toString();

	}

	public static String takeOffBOM(InputStream inputStream) throws IOException {
		BOMInputStream bomInputStream = new BOMInputStream(inputStream);
		return IOUtils.toString(bomInputStream, "UTF-8");
	}

	/**
	 * updates a grid row of test set testcases i.e upadates status etc at grid row
	 * level
	 * 
	 * @throws Exception
	 */

	private void updateTestInstanceGridViewRow() throws Exception {
		try {
			// fetch the required xml data format for abstract test instace i.e Grid view

			LOG.info("Start: updateTestInstanceGridViewRow method");

			ext_strTestInstanceDetails = fetchTestInstanceGridViewDataFormat();
			// LOG.info("Retrieve the test instance abstract values" +
			// ext_strTestInstanceAbstractUpdate);
			// updating grid view data with values
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("HOST_NAME", java.net.InetAddress.getLocalHost().getHostName());
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("EXEC_DATE", dateFormat.format(objDate));
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("EXEC_TIME", timeFormat.format(objDate));
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("STATUS", ext_strCurrentStatus);
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("OWNER", ext_strTesterName);
			URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW = URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW.replaceAll("TEST_INSTANCE_ID", ext_strTestInstanceID);
			URL objURL = new URL(URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW);
			LOG.info("ALM Application Connection URL: " + URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW);
			LOG.info("updateTestInstanceGridViewRow : " + ext_strTestInstanceDetails );
			HttpURLConnection objALMConnection = (HttpURLConnection) objURL.openConnection();
			objALMConnection.setConnectTimeout(5000);
			objALMConnection.setRequestMethod("PUT");
			objALMConnection.setRequestProperty("Content-Type", "application/xml");
			objALMConnection.setRequestProperty("Accept", "application/xml");
			objALMConnection.setRequestProperty("Content-Language", "en-US");
			objALMConnection.setUseCaches(true);
			objALMConnection.setRequestProperty("Cookie", getRequestCookie());
			objALMConnection.setDoOutput(true);
			objALMConnection.setDoInput(true);

			LOG.info("ALM URL Connection : " + objALMConnection);
			OutputStream objOutStream = (objALMConnection.getOutputStream());
			objOutStream.write(ext_strTestInstanceDetails.getBytes());
			objOutStream.flush();
			objALMConnection.connect();
			int responseCode = objALMConnection.getResponseCode();
			LOG.info("Response code getting while update Test inStance Grid" + responseCode);

			if (responseCode == 200) {
				responseLog(objALMConnection.getHeaderFields());
				BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getInputStream())));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
			} else {
				if (objALMConnection.getErrorStream() != null) {
					BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getErrorStream())));
					StringBuilder sb = new StringBuilder();
					String output;
					LOG.info("Getting this the error is observed while initiate the URL Connection" + sb);
					while ((output = br.readLine()) != null) {
						sb.append(output);
					}
				}
				LOG.info("Unable to connect the with the URL Parameters" + objALMConnection);
				throw new Exception("\n Response Code-" + responseCode + "\n");
			}
			objALMConnection.disconnect();
			LOG.info("Stop: updateTestInstanceGridViewRow method");
		} catch (Exception exception) {
			throw new Exception("Error: " + exception.getMessage());
		}
	}

	/**
	 * fetch the test instance run details based on test ID and test set id
	 * 
	 * @return
	 */
	private String fetchTestInstanceRunDetails() {
		StringBuilder testSetInstanceDetails = new StringBuilder();

		try {

			// fetch test instance run id of the latest run which is updates/created in
			// previous step
			URL_FETCH_TEST_INSTANCE_RUN_DETAILS = URL_FETCH_TEST_INSTANCE_RUN_DETAILS.replaceAll("CYCLE_ID",
					ext_strTestSetID);
			URL_FETCH_TEST_INSTANCE_RUN_DETAILS = URL_FETCH_TEST_INSTANCE_RUN_DETAILS.replaceAll("TEST_ID",
					ext_strTestID);

			URL objURL = new URL(URL_FETCH_TEST_INSTANCE_RUN_DETAILS);
			HttpURLConnection objALMConnection = getAlMConnection(objURL, "GET");

			// LOG.info("URL Connection details" + objALMConnection);

			int responseCode = objALMConnection.getResponseCode();
			if (responseCode == 200) {
//				Map<String, List<String>> map = objALMConnection.getHeaderFields();
				BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getInputStream())));

				String output;
				while ((output = br.readLine()) != null) {
					testSetInstanceDetails.append(output);
				}
			} else {
				// LOG.info("Error response code:" + responseCode);
			}
			objALMConnection.disconnect();

			Document document = getXmlDocument(testSetInstanceDetails.toString());
			NodeList list = document.getElementsByTagName("Entity");
			if (list.getLength() == 0) {
				throw new Exception("\nFetched test case runs/instances -0\n" + "			Reasons could be\n"
						+ "				a.Test case has no runs as it is not updated before"
						+ "				b.Grid row is not updated before");
			}
		} catch (Exception exception) {
			LOG.error("Error:" + exception.getMessage());
		}
		return testSetInstanceDetails.toString();
		// return results;
	}

	public String escapeXml(String s) {
		return s.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;").replaceAll("\"", "&quot;")
				.replaceAll("'", "&apos;");
	}

	/**
	 * fetches the test instance details based on test instance id
	 * 
	 * @return
	 */
	protected String fetchTestInstanceDetails() {
		StringBuilder testInstanceDetailsXMLStr = new StringBuilder();
		URL_FETCH_TEST_INSTANCE_DETAILS = URL_FETCH_TEST_INSTANCE_DETAILS.replace("TEST_INSTANCE_ID", ext_strTestInstanceID);
		try {
			URL objURL = new URL(URL_FETCH_TEST_INSTANCE_DETAILS);
			HttpURLConnection objALMConnection = getAlMConnection(objURL, "GET");
			LOG.info("URL Connection details" + objALMConnection);
			int responseCode = objALMConnection.getResponseCode();
			if (responseCode == 200) {
				//				Map<String, List<String>> map = objALMConnection.getHeaderFields();
				//				for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				//
				//				}
				BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getInputStream())));
				String output;
				while ((output = br.readLine()) != null) {
					testInstanceDetailsXMLStr.append(output);
				}
				LOG.info("Test Instace Details:" + testInstanceDetailsXMLStr.toString());
			} else {
				LOG.info("Error response code:" + responseCode);
			}
			objALMConnection.disconnect();
		} catch (Exception exception) {
			LOG.error("Connection Error Details : " + exception.getMessage());
		}
		return testInstanceDetailsXMLStr.toString();
	}

	/**
	 * updates a grid row of test set testcases i.e upadates status etc at grid row level
	 * 
	 * @throws Exception
	 */
	protected void updateTestInstanceRunGridView() throws Exception {
		try {
			// fetch the required xml data format for abstract test instace i.e Grid view
			// row fields
			ext_strTestInstanceDetails = fetchTestInstanceGridViewDataFormat();
			// updating grid view data with values
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("HOST_NAME", java.net.InetAddress.getLocalHost().getHostName());
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("EXEC_DATE", dateFormat.format(objDate));
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("EXEC_TIME", timeFormat.format(objDate));
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("STATUS", ext_strCurrentStatus);
			ext_strTestInstanceDetails = ext_strTestInstanceDetails.replaceAll("OWNER", "");

			URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW = URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW.replaceAll("PROJECT_ID", strProjectName);
			URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW = URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW.replaceAll("TEST_INSTANCE_ID", ext_strTestInstanceID);
			URL objURL = new URL(URL_UPDATE_TEST_SET_INSTANCES_GRID_VIEW);
			HttpURLConnection objALMConnection = (HttpURLConnection) objURL.openConnection();
			objALMConnection.setRequestMethod("PUT");
			objALMConnection.setRequestProperty("Content-Type", "application/xml");
			objALMConnection.setRequestProperty("Accept", "application/xml");
			objALMConnection.setRequestProperty("Content-Language", "en-US");
			objALMConnection.setUseCaches(true);
			// LOG.info("cookie to be sent " + getRequestCookie());
			objALMConnection.setRequestProperty("Cookie", getRequestCookie());
			objALMConnection.setDoOutput(true);
			objALMConnection.setDoInput(true);
			OutputStream objOutStream = (objALMConnection.getOutputStream());
			objOutStream.write(ext_strTestInstanceDetails.getBytes());
			objOutStream.flush();

			objALMConnection.connect();
			int responseCode = objALMConnection.getResponseCode();
			if (responseCode == 200 || responseCode == 201) {
				responseLog(objALMConnection.getHeaderFields());
				BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getInputStream())));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
			} else {
				if (objALMConnection.getErrorStream() != null) {
					BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getErrorStream())));
					StringBuilder sb = new StringBuilder();
					String output;
					while ((output = br.readLine()) != null) {
						sb.append(output);
					}
				} else {
					LOG.info("Unprocessed ALM Response Code : " + responseCode);
				}
				throw new Exception("Response Code" + responseCode);
			}
			objALMConnection.disconnect();
		} catch (Exception exception) {
			throw new Exception("Error:Unable to update test instance Grid View data-" + exception.getMessage());
		}

	}

	private HttpURLConnection getAlMConnection(URL objUrl, String strMethod) {
		HttpURLConnection objALMConnection = null;
		try {
			objALMConnection = (HttpURLConnection) objUrl.openConnection();
			objALMConnection.setConnectTimeout(5000);
			objALMConnection.setRequestMethod(strMethod);
			objALMConnection.setRequestProperty("Content-Type", "application/xml");
			objALMConnection.setRequestProperty("Accept", "application/xml");
			objALMConnection.setRequestProperty("Content-Language", "en-US");
			objALMConnection.setUseCaches(true);
			// LOG.info("cookie to be sent " + getRequestCookie());
			objALMConnection.setRequestProperty("Cookie", getRequestCookie());
			objALMConnection.setDoOutput(true);
			objALMConnection.setDoInput(true);
		} catch (IOException e) {
			LOG.error("Error on ALM Connection: " + e.getMessage());
		}
		return objALMConnection;

	}

	/**
	 * updates/creates a new run for particular test instance of test set
	 */
	private void createNewTestInstanceRun() {
		try {
			// LOG.info("Start the method: createNewTestInstanceRun");
			ext_strTestInstanceNewRunDetails = fetchTestInstanceNewRunDataFormat();
			// LOG.info("fetch Test Instance details" +
			// ext_strTestInstanceNewRun);
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_CASE_NAME", ext_strTestcaseName);
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_INSTACE_ID", ext_strTestInstanceID);
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_SET_ID", ext_strTestSetID);
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("HOST_NAME", java.net.InetAddress.getLocalHost().getHostName());
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("STATUS", ext_strCurrentStatus);
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("OS_NAME", System.getProperty("os.name"));
			// testInstanceNewRunStr=testInstanceNewRunStr.replaceAll("EXEC_TIME",dateFormat.format(date)+ " " +timeFormat.format(date));
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("EXEC_DATE", dateFormat.format(objDate));
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("EXEC_TIME", timeFormat.format(objDate));
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("TEST_ID", ext_strTestID);
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("COMMENT", ext_strComments);
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("ATTACHMENT", "N");
			ext_strTestInstanceNewRunDetails = ext_strTestInstanceNewRunDetails.replaceAll("OWNER", ext_strTesterName);
			// LOG.info(" Updated the fetch Test Instance details" +
			// ext_strTestInstanceNewRun);
			URL objURL = new URL(URL_CREATE_NEW_TEST_INSTANCE_RUN);
			HttpURLConnection objALMConnection = getAlMConnection(objURL, "POST");
			// LOG.info("URL Connection details" + objALMConnection);
			OutputStream objOutStream = (objALMConnection.getOutputStream());
			objOutStream.write(ext_strTestInstanceNewRunDetails.getBytes());
			objOutStream.flush();
			// LOG.info("URL Connection Start");
			objALMConnection.connect();
			int responseCode = objALMConnection.getResponseCode();
			// LOG.info("Getting the response code : " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_CREATED) {
				responseLog(objALMConnection.getHeaderFields());
				BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getInputStream())));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
			} else {
				if (objALMConnection.getErrorStream() != null) {
					BufferedReader br = new BufferedReader(new InputStreamReader((objALMConnection.getErrorStream())));
					StringBuilder sb = new StringBuilder();
					String output;
					while ((output = br.readLine()) != null) {
						sb.append(output);
					}
					// LOG.info("URL Connection is not working getting this error message:
					// " + sb.toString());
				} else {
					// LOG.info(responseCode);
					LOG.info("getting this response code while executiing createNewTestInstanceRun methos : " + responseCode);
				}
			}
			objALMConnection.disconnect();
			// LOG.info("Completed method: createNewTestInstanceRun ");
		} catch (Exception exception) {
			// exception.printStackTrace();
		}
	}

	/**
	 * uploads the zip file to one of the runs of test instances of test set
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	private void uploadtestSetInstanceResult(String filePath) throws Exception {
		try {
			URL urlUpdateRunStepsURL = new URL(URL_UPLOAD_TEST_SET_INSTANCES_RESULT);
			String boundary = "12345678";
			final String LINE_FEED = "\r\n";
			HttpURLConnection objHttpConn = (HttpURLConnection) urlUpdateRunStepsURL.openConnection();
			objHttpConn.setConnectTimeout(10000);
			objHttpConn.setUseCaches(false);
			objHttpConn.setDoOutput(true); // indicates POST method
			objHttpConn.setDoInput(true);
			objHttpConn.setRequestProperty("Cookie", getRequestCookie());
			objHttpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			File uploadFile = new File(filePath);
			OutputStream objOutputStream = objHttpConn.getOutputStream();

			PrintWriter objPrintWriter = new PrintWriter(new OutputStreamWriter(objOutputStream), true);
			String strFileName = uploadFile.getName();

			objPrintWriter.append("--" + boundary).append(LINE_FEED);
			objPrintWriter.append("Content-Disposition: form-data; name=\"filename\"").append(LINE_FEED);
			objPrintWriter.append(LINE_FEED);
			objPrintWriter.append(strFileName).append(LINE_FEED);
			objPrintWriter.append("--" + boundary).append(LINE_FEED);
			objPrintWriter.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + strFileName + "\"")
					.append(LINE_FEED);
			objPrintWriter.append(LINE_FEED);
			objPrintWriter.flush();
			FileInputStream objInputStream = new FileInputStream(uploadFile);

			// User Story 1128099 START
//			byte[] objByteSteam = new byte[objInputStream.available()];
			byte[] objByteStream = Validator.validateByteArrayXml(new byte[objInputStream.available()]);
			// User Story 1128099 END

			objInputStream.read(objByteStream);
			objOutputStream.write(objByteStream);
			objOutputStream.flush();
			objInputStream.close();
			objPrintWriter.append(LINE_FEED);
			objPrintWriter.append("--" + boundary + "--");
			objPrintWriter.flush();
			objPrintWriter.close();
			int responseCode = objHttpConn.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_CREATED) {
				// LOG.info("Successfully Uploaded");
				objHttpConn.disconnect();
			} else {
				throw new Exception("uploadtestSetInstanceResult : Response code:" + responseCode);
			}

		} catch (Exception exception) {
			throw new Exception("Unable to upload the result-" + exception.getMessage());
		}

	}
	/**
	 * fetches the cookies out of response of login
	 * 
	 * @return
	 */
	private String getRequestCookie() {
		// XSRF-TOKEN ALM_USER QCSession LWSSO_COOKIE_KEY
		if (listCommonRequestCookie.size() == 0) {
			for (String parameter : listCommonResponseCookie) {
				String key = parameter.split(";")[0].split("=")[0];
				String value = parameter.split(";")[0].split("=")[1];
				listCommonRequestCookie.add(parameter.split(";")[0]);
				LOG.info("XSRF-TOKEN ALM_USER QCSession - Cookies Code: " + key + "\t Value: " + value);
			}
		}
		return listCommonRequestCookie.get(0) + ";" + listCommonRequestCookie.get(1) + ";" + listCommonRequestCookie.get(2) + ";" + listCommonRequestCookie.get(3);
	}

	/**
	 * prepares the XML document and returns the doc object
	 * 
	 * @param testSetInstancesStr
	 * @return
	 */
	public Document getXmlDocument(String testSetInstancesStr) {
		Document objDocument = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = dbFactory.newDocumentBuilder();
			InputSource iSource = new InputSource(new StringReader(testSetInstancesStr));
			objDocument = docBuilder.parse(iSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		objDocument.getDocumentElement().normalize();
		return objDocument;

	}

	@SuppressWarnings("deprecation")
	public String updateTestInstanceDetails(String strTestSetInstances) throws Exception {
		Document objDocument = getXmlDocument(strTestSetInstances);
		NodeList objEntities = objDocument.getElementsByTagName("Entity");

		Element objEntiry = null;
		Element objField = null;
		boolean isTestcaseFound = false;
		// loop for each employee
		if (objEntities.getLength() >= 1) {
			outerLoop: for (int i = 0; i < objEntities.getLength(); i++) {
				objEntiry = (Element) objEntities.item(i);
				NodeList fields = objEntiry.getElementsByTagName("Field");

				// User Story 1128099 START
				int loopCount = Validator.validateLoopCount(fields.getLength());

				for (int j = 0; j < loopCount; j++) {
				// User Story 1128099 END

					objField = (Element) fields.item(j);
					// LOG.info(field.getAttribute("Name"));
					String value = null;
					try {
						value = objField.getElementsByTagName("Value").item(0).getFirstChild().getNodeValue();
						LOG.info(value);
					} catch (Exception e) {
						LOG.error("Error Message: " + e.getMessage() + "\t Value: " + value);
					}
					if (value != null) {
						if (objField.getAttribute("Name").equalsIgnoreCase("name") 
								&& (StringUtils.containsIgnoreCase(value, ext_strTestcaseName) 
										|| StringUtils.getJaroWinklerDistance(value, ext_strTestcaseName) > 0.93)) {
							isTestcaseFound = true;
							break outerLoop;
						}
					}
				}
			}
		} else {
			throw new Exception("Error:Test case fetched - 0");
		}
		if (!isTestcaseFound) {
			throw new Exception("Error:test case \"" + ext_strTestcaseName + "\" not Found in the Test Set:" + ext_strTestSetID);
		} else {
			NodeList fields = objEntiry.getElementsByTagName("Field");
			for (int j = 0; j < fields.getLength(); j++) {
				objField = (Element) fields.item(j);
				if (objField.getAttribute("Name").equals("id")) {
					ext_strTestInstanceID = objField.getElementsByTagName("Value").item(0).getFirstChild().getNodeValue();
					break;
				}
			}
			for (int j = 0; j < fields.getLength(); j++) {
				objField = (Element) fields.item(j);
				if (objField.getAttribute("Name").equals("test-id")) {
					ext_strTestID = objField.getElementsByTagName("Value").item(0).getFirstChild().getNodeValue();
					break;
				}
			}
			for (int j = 0; j < fields.getLength(); j++) {
				objField = (Element) fields.item(j);
				if (objField.getAttribute("Name").equals("status")) {
					String status = objField.getElementsByTagName("Value").item(0).getFirstChild().getNodeValue();
					LOG.info("Current Status is : " + status);
					if (status.equals(ext_strCurrentStatus)) {
						flagStatusUdateRequired = true;
					}
					LOG.info("Info: Test case current Status in ALM is: " + status + " , So not required to create a new instance and test case execution status: " + ext_strCurrentStatus);
					break;
				}
			}
		}
		LOG.info("Info: TestID= " + ext_strTestID + "\ttestInstanceID= " + ext_strTestInstanceID + "\tTest Set ID= " + ext_strTestSetID);
		return ext_strTestInstanceID;
	}

	/**
	 * updates the runID
	 * 
	 * @param testInstanceRunDetails
	 */
	public void updateTestInstanceRunDetails(String testInstanceRunDetails) {
		String runID = null;
		Document doc = getXmlDocument(testInstanceRunDetails);
		NodeList entities = doc.getElementsByTagName("Entity");
		Element entity = null;
		Element field = null;
		// loop for each employee
		entity = (Element) entities.item(entities.getLength() - 1);
		NodeList fields = entity.getElementsByTagName("Field");

		// User Story 1128099 START
		int loopCount = Validator.validateLoopCount(fields.getLength());

		for (int j = 0; j < loopCount; j++) {
		// User Story 1128099 END

			field = (Element) fields.item(j);
			if (field.getAttribute("Name").equals("id")) {
				runID = field.getElementsByTagName("Value").item(0).getFirstChild().getNodeValue();
				break;
			}
		}
		ext_strRunID = runID;
		LOG.info("Info: Test Case RunID=" + runID);
	}

	/**
	 * assigns testInstanceNewRunStr from fetches run details
	 * 
	 * @param strTestNewRun
	 */
	public void updateTestInstanceNewRunDetails(String strTestNewRun) {
		strTestNewRun = strTestNewRun.replaceAll("TEST_CASE_NAME", "TS_B_AM_Enrollment1_WithAppReset");
		strTestNewRun = strTestNewRun.replaceAll("TEST_INSTACE_ID", ext_strTestInstanceID);
		strTestNewRun = strTestNewRun.replaceAll("TEST_SET_ID", ext_strTestSetID);
		try {
			strTestNewRun = strTestNewRun.replaceAll("HOST_NAME", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			strTestNewRun = strTestNewRun.replaceAll("HOST_NAME", "Unknown");
		}
		strTestNewRun = strTestNewRun.replaceAll("STATUS", "Passed");
		strTestNewRun = strTestNewRun.replaceAll("OS_NAME", System.getProperty("os.name"));
		strTestNewRun = strTestNewRun.replaceAll("TEST_ID", ext_strTestID);

		ext_strTestInstanceNewRunDetails = strTestNewRun;
	}

	/**
	 * updates test instance run status i.e Passed or Failed so as to check second
	 * update to run details is needed or not after updating updating grid row for
	 * the particular test case instance of test set
	 * 
	 * @param testInstanceDetails
	 * @param currentStatus
	 */

	public void updateTestInstanceRunStatus(String testInstanceDetails, String currentStatus) {
		Document doc = getXmlDocument(testInstanceDetails);
		NodeList entities = doc.getElementsByTagName("Entity");
		Element entity = null;
		Element field = null;
		// loop for each employee
		outerLoop: for (int i = 0; i < entities.getLength(); i++) {
			entity = (Element) entities.item(i);
			NodeList fields = entity.getElementsByTagName("Field");
			for (int j = 0; i < fields.getLength(); j++) {
				field = (Element) fields.item(j);
				if (field.getAttribute("Name").equals("status")) {
					LOG.info("updateTestInstanceRunStatus " + field.getElementsByTagName("Value").item(0).getFirstChild().getNodeValue());
//					String status = field.getElementsByTagName("Value").item(0).getFirstChild().getNodeValue();
					// if (status.equals(ALMRest.this.currentStatus)) {
					// isStatusUdateRequired = true;
					// }
					// LOG.info("Info: Is Alm Run details update required=" +
					// flagStatusUdateRequired);
					break outerLoop;
				}
			}
		}
	}

	/**
	 * fetches the template for test instance new run details from xml file
	 * 
	 * @return
	 */// fetchTestInstanceGridViewDataFormat
	public String fetchTestInstanceNewRunDataFormat() {
		String strResult = "";
		try {
			InputStream objInputStream = this.getClass().getClassLoader()
					.getResourceAsStream("TestInstanceRunData.xml");

			// ClassLoader.getSystemClassLoader().getResourceAsStream("TestInstanceRunData.xml");
			strResult = IOUtils.toString(objInputStream, StandardCharsets.UTF_8);
			// LOG.info(strResult);
		} catch (Exception e) {
			LOG.error("fetchTestInstanceNewRunDataFormat excption : " + e.toString());
		}

		if (SystemUtils.IS_OS_LINUX) {
			// LOG.info("This is linux machine, hence removing first character to
			// linux os issue");
			strResult = strResult.substring(1);
		}
		//LOG.info("fetchTestInstanceNewRunDataFormat : " + strResult);
		return strResult;
	}

	/**
	 * fetches the test instance grid row data.i.e to update the row status in the
	 * grid view
	 * 
	 * @return
	 * @throws IOException
	 */
	public String fetchTestInstanceGridViewDataFormat() throws IOException {
		String strResult = "";
		try {
			//InputStream in = new BufferedInputStream(new FileInputStream(new File("TestInstanceGridViewData.xml").getAbsolutePath()));
			InputStream objInputStream = this.getClass().getClassLoader().getResourceAsStream("TestInstanceGridViewData.xml");
			LOG.info("TestInstanceGridViewData.xml " + objInputStream.toString());
			strResult = IOUtils.toString(objInputStream, StandardCharsets.UTF_8);
		} catch (Exception e) {
			LOG.error("fetchTestInstanceGridViewDataFormat excption : " + e.toString());
		}

		if (SystemUtils.IS_OS_LINUX) {
			// LOG.info("This is linux machine, hence removing first character to
			// linux os issue");
			strResult = strResult.substring(1);
		}
		LOG.info("fetchTestInstanceGridViewDataFormat : " + strResult);
		return strResult;
	}

}
