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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Queue;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cts.Osp.License.Validator.LicenseContent;
import cts.Osp.License.Validator.LicenseValidationResults;
import cts.Osp.License.Validator.LicenseValidator;
import cts.qea.automation.annotations.Description;
import cts.qea.automation.annotations.Tags;
import cts.qea.automation.keywordComponents.KeywordTestCase;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.security.SecurityUtils;
import cts.qea.automation.utils.WebDriverKillProcessor;

public abstract class TestCase {
	// Enhancement
	private static final Logger LOG = LoggerFactory.getLogger(TestCase.class);
	private static final String NO_STRING = "No";
	private static final String YES_STRING = "Yes";
	protected WrappedWebDriver driver = null;
	protected WrappedWiniumDriver wdriver = null;
	protected DataProvider dp = null;
	static volatile LinkedHashMap<String, Queue<String>> tcIterator = new LinkedHashMap<String, Queue<String>>();
	public volatile String iteration = null;
	public volatile String tcStatus = "fail", resultPath = "", buildVersion = null, almtestsetid = "",
			almtestcasename = "";
	public volatile int day = 1, totalDays = 1;
	protected boolean isStandAloneTest = false, initDone = false, isIterationRequired = false;
	// Enhancement
	protected boolean isPassedTCRerun = false;
	protected String scenarioName = null;
	public volatile String browserName = null, browserVersion = null, build = null; // Added as part of Implementing
	// browser details in report

	/**
	 * <b>Description</b> Runner for invoking Test Suite
	 * 
	 * @throws Exception Exception
	 */
	@Test
	public final void runner() throws Exception {

		try {
			if (initDone == false)
				start();
			// Enhancement
			Report.log("Rerun Pass Test case Flag is : " + isPassedTCRerun, Status.PASS);
			if (skipPassedTc())
				test();

		} catch (ExceptionInInitializerError | Exception e) {
			e.printStackTrace();
			Report.log("Exception Occured due to : " + e, Status.FAIL);
			Report.log(e);
		} finally {
			if (!(this.getClass().getSuperclass().equals(KeywordTestCase.class)))
				end();
		}
	}

	/*
	 * ValidateLicense will ensure that a valid license key is available.
	 * 
	 * should return one of three values: 1: invalid License. Don't allow the
	 * program to continue 2: Within Grace Period. Can continue until the license is
	 * invalid. 3: License is valid.
	 */
	private LicenseValidationResults ValidateLicense() {
		// get the product code.
		String productCode = "TAS"; // NOT configurable. This must remain hard-coded

		// get the product version
		String productVersion = Config.getEnvDetails("config", "productVersion");

		// get the product module 
		String productModule = Config.getEnvDetails("config", "productModule");
		
		if(!Arrays.asList("Facets Modules", "QNXT Modules").contains(productModule))
			productModule = "Invalid Module";

		// get the path to the license.xml file
		String licensePath = Config.getEnvDetails("config", "licenseFilePath");

		// get the license file name
		String licenseXML = licensePath + Config.getEnvDetails("config", "licenseFileName");

		// =============================================
		// LOAD LICENSE.xml file
		// =============================================
		File file = new File(licenseXML);
		DocumentBuilder builder = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		org.w3c.dom.Document dDoc = null;
		try {
			dDoc = builder.parse(file);
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// =============================================
		// load up the LicenseContent from the LIcensae file
		// =============================================
		LicenseContent licContent = new LicenseContent();

		NodeList nodeList = dDoc.getElementsByTagName("entry");
		int nodeCount = nodeList.getLength();
		org.w3c.dom.Node n = null;
		org.w3c.dom.Element eElement = null;

		Date date = null;
		for (int i = 0; i < nodeCount; i++) {
			n = nodeList.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) n.getChildNodes();
				System.out.println("\nCurrent Element :" + n.getNodeName());

				// get the attribute
				String attribute = eElement.getAttribute("key");
				System.out.println("\nCurrent Attribute :" + attribute);

				// get the value
				String value = eElement.getTextContent();
				System.out.println("\nCurrent Value :" + value);

				switch (attribute) {
				case "TOOLCODE":
					licContent.setProductCode(value);
					break;
				case "MODULES":
					licContent.setModules(value);
					break;
				case "CUSTOMERCODE":
					licContent.setCustomerCode(value);
					break;
				case "ENVIRONMENTCODE":
					licContent.setEnvironmentCode(value);
					break;
				case "LICENSETYPE":
					licContent.setLicenseTypes(value);
					break;
				case "EXPIRATIONDATE":
					SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

					try {
						date = myFormat.parse(value);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					licContent.setExpirationDate(date);	
					break;			
				
				case "LICENSECOUNT":
					licContent.setLicenseCount(Integer.parseInt(value));
					break;
				case "RELEASENUMBER":
					licContent.setReleaseNumber(value);
					break;
				case "WARNINGPERIOD":
					licContent.setWarningPeriod(Integer.parseInt(value));
					break;
				case "GRACEPERIOD":
					licContent.setGracePeriod(Integer.parseInt(value));
					break;
				case "LICENSEKEY":
					licContent.setLicenseKey(value);
					break;
				}
			}
			n.getNextSibling();	
		}
		
		licContent.setEndDate(date);
		
		// create object to hold results
		LicenseValidationResults licenseResults = new LicenseValidationResults();

		try {
			licenseResults = LicenseValidator.Validate(licContent, productCode, productVersion, productModule);
		} catch (Exception e) {
			// TODO log an appropriate error somewhere.
			e.printStackTrace();
		}

		return licenseResults;
	}

	/**
	 * <b>Description</b> Start Test Suite which creates driver, Starts Report
	 * initialize data provider, load previous day data
	 * 
	 * @throws Exception Exception
	 */
	private final void start() throws Exception {
		// Driver Initialization
		initDone = true;

		String itr = getIteration(this);
		buildVersion = Config.getEnvDetails("aut", "build");
		// Enhancement
		setPassedTCRerun();
		setIterationRequired();

		if (isIterationRequired) {
			setIerationValue(itr);
		}

		// Start of report initialization for both suite and stand Alone run
		if (scenarioName != null && !scenarioName.equals("")) {
			setAttribute("name", scenarioName);
			Thread.currentThread().setName(scenarioName);
		}

		// Initializing Data provider
		dp = new DataProvider();

		// If last day sec is empty or null load the excel data
		String fileName = Config.getEnvDetails("testData", "dataFileName");
		String sheetName = Config.getEnvDetails("testData", "dataSheetName");

		if (scenarioName != null)
			dp.loadFromExcel(fileName, sheetName, scenarioName, iteration);
		else
			dp.loadFromExcel(fileName, sheetName, this.getAttribute("CanonicalName"), iteration);

		// Added as part of Implementing browser details in report
		browserName = Config.getEnvDetails("browser", "browser");
		browserVersion = Config.getEnvDetails("browser", "grid_cap_browser_version");
		build = Config.getEnvDetails("aut", "build");
		if (!Config.getEnvDetails("config", "seleniumNeeded").equalsIgnoreCase(NO_STRING)) {
			driver = getWebDriver();

			if (driver.getCapabilities().getBrowserName().equalsIgnoreCase("internet explorer")) {
				browserName = "Internet Explorer";
			} else if (driver.getCapabilities().getBrowserName().equalsIgnoreCase("firefox")) {
				browserName = "Mozilla Firefox";
			} else if (driver.getCapabilities().getBrowserName().equalsIgnoreCase("chrome")) {
				browserName = "Google Chrome";
			} else if (driver.getCapabilities().getBrowserName().equalsIgnoreCase("phantomjs")) {
				browserName = "PhantomJS";
			} else if (driver.getCapabilities().getBrowserName().equalsIgnoreCase("Edge")) { // ajd
				browserName = "Edge";
			}
			browserVersion = driver.getCapabilities().getVersion();
		}

		Report.startTest(this);

		Report.log("Test Data File: " + Config.getEnvDetails("testData", "dataFileName"), Status.DONE);
		Report.log("Data Factory File: " + Config.getEnvDetails("testData", "tdmFileName"), Status.DONE);

		// Licensing Validation - log error if license is invalid.
		LicenseValidationResults lvr = ValidateLicense();

		switch (LicenseValidationResults.licenseDetermination) {
		case 1: // INVALID
			Report.log("Invalid License Key", Status.fail);
			System.out.println(lvr.message);
			end();
			System.exit(16);
		case 2: // IN GRACE PERIOD
			Report.log("Valid License Key:  Currently running in the grace period", Status.WARN);
			System.out.println(lvr.message);
			break;
		case 3: // Valid
			Report.log("Valid License Key", Status.PASS);
			break;
		case 4: // Warning
			Report.log("Valid License Key: Approaching expiration date grace period", Status.PASS);
			System.out.println(lvr.message);
			break;
		case 5: // Expired
			Report.log("Expired License Key.  Key is valid, it's passed its expiration date", Status.fail);
			System.out.println(lvr.message);
			end();
			System.exit(16);
		}

		if (!Config.getEnvDetails("config", "seleniumNeeded").equalsIgnoreCase(NO_STRING)) {
			driver = getWebDriver();
		}

		if (!Config.getEnvDetails("config", "winiumNeeded").equalsIgnoreCase(NO_STRING)) {
			wdriver = getWiniumDriver();
		}

		if (!(this.getClass().getSuperclass().equals(BatchTestCase.class))) {
			isStandAloneTest = true;
		}

		if (!(this.getClass().getSuperclass().equals(BatchTestCase.class))) {
			isStandAloneTest = true;
		}

		// Initialize previous day data with ini as reference.
		loadPrevDayData();
		setAttribute("day", dp.get("LastDay", "Day").trim());

		// Depending upon the user required for the test case the user loading Will
		// happen
		if (!dp.get("Metadata", "Credentials").equals("")
				&& !dp.get("Metadata", "Credentials").equalsIgnoreCase("NA")) {
			for (String userType : dp.get("Metadata", "Credentials").split(";")) {
				UserInfo user = UserList.takeUser(dp.get("Metadata", "AppName"), userType);
				dp.set("credentials", userType + "_UserName", user.getUserId());
				try {
					ResourceBundle.getBundle("keys");
					dp.set("credentials", userType + "_Password", SecurityUtils.getDecodedValue(user.getPassword()));
				} catch (MissingResourceException e) {
					Report.log("Credentials are not encrypted. Please encrypt!", Status.WARN);
					dp.set("credentials", userType + "_Password", user.getPassword());
				}
			}
		}

		if (iteration == null || iteration.trim().equals("")) {
			Report.log("Iteration shouldn't be null", Status.FAIL);
			throw new Exception("Iteration shouldn't be null");
		}
		// Fail the Test Case if build Number = empty
		if (buildVersion == null || buildVersion.trim().equals("")) {
			Report.log("BuildVersion shouldn't be null", Status.FAIL);
			throw new Exception("BuildVersion shouldn't be null");
		}

//		if (almtestsetid == null || almtestsetid.trim().equals("")) {
//			Report.log("AlmTestsetid shouldn't be null", Status.FAIL);
//			throw new Exception("AlmTestsetid shouldn't be null");
//		}

	}

	private void setIerationValue(String itr) {
		if (itr != null)
			iteration = itr;
		else {
			iteration = "1";
		}
	}

	public String getAlmTestsetId() throws Exception {
		String strTestId = dp.get("Metadata", "almtestsetid");
		LOG.info("ALM Test Set Id: {}", strTestId);
		if (StringUtils.isNotBlank(strTestId) && !"NA".equalsIgnoreCase(strTestId)) {
			almtestsetid = strTestId;
		}
		Report.log("ALM Test Set Id: " + almtestsetid, Status.PASS);
		return almtestsetid;
	}

	public String getAlmTestcasename() throws Exception {
		String strName = dp.get("Metadata", "almtestcasename");
		LOG.info("ALM Test Case Name: {}", strName);
		if (StringUtils.isNotBlank(strName) && !"NA".equalsIgnoreCase(strName)) {
			almtestcasename = strName;
		}
		Report.log("ALM Test Case Name: " + almtestcasename, Status.PASS);
		return almtestcasename;
	}

	// Enhancement
	private void setPassedTCRerun() {
		if (YES_STRING.equalsIgnoreCase(Config.getEnvDetails("config", "reRunOnlyFailedTestCases"))) {
			isPassedTCRerun = true;
		} else {
			isPassedTCRerun = false;
		}
	}

	private void setIterationRequired() {
		if (YES_STRING.equalsIgnoreCase(Config.getEnvDetails("config", "isIterationRequired"))) {
			isIterationRequired = true;
		} else {
			isIterationRequired = false;
		}
	}

	/**
	 * <b>Description</b> Returns boolean to execute test case or skip test case
	 * 
	 * @return boolean Boolean value to execute test case or skip test case
	 */
	private final boolean skipPassedTc() { // Enhancement
		if (isPassedTCRerun && isStandAloneTest
				&& (dp.get("DayWiseResult", getAttribute("nameWithDayIteration") + "_Verdict").equalsIgnoreCase("PASS")
						|| dp.get("DayWiseResult", getAttribute("nameWithDayIteration") + "_Verdict")
								.equalsIgnoreCase("DONE"))) {
			Report.log(
					"The Test Case has been passed earlier, to re-run the entire testcase please delete the entire Ini",
					Status.PASS);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * <b>Description</b> Load Previous Day data from ini to Data Provider
	 */
	private final void loadPrevDayData() throws Exception {
		if (scenarioName != null && !scenarioName.equals("")) {
			dp.loadFromIni(Config.getEnvDetails("dayWiseData", "iniPath") + File.separator + buildVersion
					+ File.separator + this.getAttribute("ScenarioWithIteration") + ".ini");
		} else {
			dp.loadFromIni(Config.getEnvDetails("dayWiseData", "iniPath") + File.separator + buildVersion
					+ File.separator + this.getAttribute("nameWithIteration") + ".ini");
		}
	}

	/**
	 * <b>Description</b> End Test case and Post Completion activities Quit driver,
	 * end Reporting, Save Data Provider to ini file.
	 * 
	 * @throws Exception
	 */
	public final void end() throws Exception {
		/*
		 * if(scenarioName != null && !scenarioName.equals("")){ setAttribute("name",
		 * scenarioName); }
		 */

		try {
			System.out.println("The driver value " + driver);
			System.out.println("The wdriver value " + wdriver);
			// Quit the driver
			if (driver != null) {
				driver.quit();
				WebDriverKillProcessor.killOpenedWebBroser();
			}
			if (wdriver != null) {
				try {
					wdriver.close();
					quitDriver();
				} catch (Exception ee) {
					System.out.println("Driver End Error: " + ee.getMessage());
				}

				try {
					wdriver.stopService();
				} catch (Exception e) {
					System.out.println("Service End Error: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			Report.log("Exception Occured while ending the test case due to :" + e.getMessage(), Status.PASS);
			e.printStackTrace();
		} finally {
			threadSleep();
		}
		if (Config.getEnvDetails("report", "ALMExecution").equalsIgnoreCase("YES")) {
			LOG.info("ALM status update process started");
			Report.log("ALM status update process started", Status.PASS);
			Report.zipReport();
			Report.executeALM(getAlmTestsetId(), getAlmTestcasename());
		}

		// End the report
		Report.endTest(this);

		if (dp != null) {
			// Retrieved user should be added back to the users
			for (String userType : dp.get("Metadata", "Credentials").split(";")) {
				UserInfo user = new UserInfo();
				user.setUserId(dp.get("credentials", userType + "_UserName"));
				user.setPassword(dp.get("credentials", userType + "_Password"));
				UserList.putUser(dp.get("Metadata", "AppName"), userType, user);
			}
			// set data into dp to update in ini file
			String iniFolderPath = Config.getEnvDetails("dayWiseData", "iniPath");

			// If batch test case disn't start then don't update the ini file.
			if (!isStandAloneTest && this.getAttribute("day").equals(dp.get("LastDay", "Day").trim())) {
				return;
			}

			DataProvider tempDP = new DataProvider();
			tempDP.setGroup("DayWiseResult", dp.getGroup("DayWiseResult"));
			tempDP.set("DayWiseResult", this.getAttribute("nameWithDayIteration") + "_Verdict",
					this.getAttribute("tcStatus"));
			tempDP.set("DayWiseResult", this.getAttribute("nameWithDayIteration") + "_HtmlPath",
					this.getAttribute("resultPath"));
			tempDP.set("LastDay", "Day", this.getAttribute("day"));
			tempDP.setGroup("PreRequisite", dp.getGroup("PreRequisite"));
			try {
				if (scenarioName != null && !scenarioName.equals("")) {
					tempDP.saveToIni(iniFolderPath + File.separator + buildVersion,
							this.getAttribute("ScenarioWithIteration") + ".ini");
				} else {
					tempDP.saveToIni(iniFolderPath + File.separator + buildVersion,
							this.getAttribute("nameWithIteration") + ".ini");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void quitDriver() {
		if (wdriver != null) {
			wdriver.quit();
		}
	}

	private void threadSleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>Description</b> Abstract method to test
	 */
	public abstract void test() throws Exception;

	/**
	 * <b>Description</b> Gets a specified attribute value
	 * 
	 * @return String value for specified Attribute
	 */
	public final String getAttribute(String name) {
		String attValue = null;
		if (name.equalsIgnoreCase("Name")) {
			if (scenarioName != null && !scenarioName.equals("")) {
				attValue = scenarioName;
			} else {
				attValue = this.getClass().getSimpleName();
			}
		} else if (name.equalsIgnoreCase("CanonicalName")) {
			attValue = this.getClass().getCanonicalName();

		} else if (name.equalsIgnoreCase("Description")) {
			try {
				String itrDesc = dp.get("Metadata", "Description");
				if (!itrDesc.equals(""))
					attValue = itrDesc;
				else
					attValue = this.getClass().getAnnotation(Description.class).value();
			} catch (Exception e) {
				attValue = "No description available";
			}

		} else if (name.equalsIgnoreCase("Tags")) {
			try {
				String tags[] = null, tagValue = "";
				tags = this.getClass().getAnnotation(Tags.class).value();
				tagValue = tags[0];
				for (int i = 1; i < tags.length; i++) {
					tagValue = tagValue + "," + tags[i];
				}
				attValue = tagValue;
			} catch (Exception e) {
				attValue = "No Tags available";
			}

		} else if (name.equalsIgnoreCase("Iteration")) {
			attValue = this.iteration;

		} else if (name.equalsIgnoreCase("nameWithIteration")) {
			attValue = this.getClass().getSimpleName() + "_" + iteration;

		} else if (name.equalsIgnoreCase("ScenarioWithIteration")) {
			attValue = scenarioName + "_" + iteration;

		} else if (name.equalsIgnoreCase("nameWithDayIteration")) {
			attValue = this.getClass().getSimpleName() + "_" + day + "_" + iteration;

		} else if (name.equalsIgnoreCase("day")) {
			attValue = Integer.toString(this.day);

		} else if (name.equalsIgnoreCase("tcStatus")) {
			attValue = this.tcStatus;

		} else if (name.equalsIgnoreCase("almtestsetid")) {
			attValue = this.almtestsetid;
		} else if (name.equalsIgnoreCase("resultPath")) {
			attValue = this.resultPath;

		} else if (name.equalsIgnoreCase("totalDays")) {
			attValue = Integer.toString(this.totalDays);
			// Added as part of Implementing browser details in report
		} else if (name.equalsIgnoreCase("browserName")) {
			attValue = browserName;
			// Added as part of Implementing browser details in report
		} else if (name.equalsIgnoreCase("browserVersion")) {
			attValue = browserVersion;
			// Added as part of Implementing browser details in report
		} else if (name.equalsIgnoreCase("buildVersion")) {
			attValue = buildVersion;
		}
		return attValue;
	}

	/**
	 * <b>Description</b> Sets a value to the specified Attribute
	 * 
	 * @param attributeName  name of the Attribute
	 * @param attributeValue value for the attribute
	 */
	public final void setAttribute(String attributeName, String attributeValue) {

		if (attributeName.equalsIgnoreCase("tcStatus")) {
			tcStatus = attributeValue;
		} else if (attributeName.equalsIgnoreCase("resultPath")) {
			resultPath = attributeValue;
		} else if (attributeName.equalsIgnoreCase("day")) {
			if (attributeValue == null || attributeValue.equalsIgnoreCase("")) {
				day = 1;
			} else {
				day = Integer.parseInt(attributeValue);
			}
		} else if (attributeName.equalsIgnoreCase("totalDays")) {
			if (attributeValue == null || attributeValue.equalsIgnoreCase("")) {
				totalDays = 1;
			} else {
				totalDays = Integer.parseInt(attributeValue);
			}
		} else if (attributeName.equalsIgnoreCase("tcStatus")) {
			tcStatus = attributeValue;
		}
	}

	/**
	 * <b>Description</b> Add iteration to the test case specified
	 * 
	 * @param tc  Name of the Test case
	 * @param itr Iteration for the test case
	 */
	public final static synchronized void addIteration(String tc, String itr) {
		Queue<String> iteration = null;

		if (tcIterator.containsKey(tc)) {
			iteration = tcIterator.get(tc);
		} else {
			iteration = new LinkedList<String>();
		}

		iteration.add(itr);
		tcIterator.put(tc, iteration);
	}

	/**
	 * <b>Description</b> Gets Iteration for the specified test case
	 * 
	 * @param tc Name of the Test case
	 * @return iteration iteration for the test case
	 */
	public final static synchronized String getIteration(TestCase tc) {
		Queue<String> iteLst = null;
		String itr = null;
		if (tcIterator.containsKey(tc.getClass().getSimpleName())) {
			iteLst = tcIterator.get(tc.getClass().getSimpleName());
			itr = iteLst.poll();
		} else if (StringUtils.isNotBlank(tc.iteration) && !StringUtils.isNumeric(tc.iteration)) {
			itr = tc.iteration;
		} else { // if (StringUtils.isNotBlank(tc.iteration) && Long.valueOf(tc.iteration) > 0) {
			itr = tc.iteration;
		}
		return itr;
	}

	public WrappedWebDriver getWebDriver() throws Exception {
		if (driver == null)
			driver = new WrappedWebDriver();
		return driver;
	}

	public WrappedWiniumDriver getWiniumDriver() throws Exception {
		if (wdriver == null) {
			wdriver = new WrappedWiniumDriver();
			Report.log("Winium Desktop Driver running at " + wdriver.getServiceUrl(), Status.PASS);
		}
		return wdriver;
	}
}