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

package cts.qea.automation.reports;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import cts.qea.automation.Config;
import cts.qea.automation.DataProvider;
import cts.qea.automation.TestCase;
import cts.qea.automation.WrappedWebDriver;
import cts.qea.automation.alm.ALMRestCommonUtils;
import cts.qea.automation.reports.ReportTemplate.Caller;
import cts.qea.automation.reports.html.HTMLReport;
import cts.qea.automation.security.SecurityUtils;
import cts.qea.automation.utils.DateHelper;
import cts.qea.automation.utils.ExcelDataAccess;
import cts.qea.automation.utils.ZipHelper;

public class Report {

	private static final Logger LOG = LoggerFactory.getLogger(Report.class);
	private static final String REPORT_HTML_FILE_NAME = "Report.html";
	private static final String ITERATION = "iteration";
	private static final String NAME = "name";
	private static final String YES = "Yes";
	static LinkedHashMap<String, TestCaseElement> testcases = new LinkedHashMap<String, TestCaseElement>();
	static LinkedHashMap<String, List<StepElement>> testcases_steps = new LinkedHashMap<String, List<StepElement>>();

	static SummaryElement summary = new SummaryElement();
	static String resultsPath = null;
	private static String almZipFileName = null;
	static volatile Caller caller = null;
	static volatile boolean initDone = false;
	static ExtentReports extent = null;
	static ExtentTest test = null;
	static ExtentTest childTest = null;
	static ExcelDataAccess excel = null;

	private static void initReport() {
		String userName = System.getProperty("user.name");
		if (StringUtils.isBlank(userName)) {
			userName = "default_user";
		}

		resultsPath = "report" + File.separator + userName + File.separator + "Run_"
				+ DateHelper.getCurrentDatenTime("yyyyMMdd") + "_" + DateHelper.getCurrentDatenTime("HHmmss");
		if (!new File(resultsPath).isDirectory()) {
			new File(resultsPath).mkdirs();
		}
	}

	/**
	 * 
	 */
	public static synchronized void startSuite() {
		if (initDone == true) {
			return;
		}
		initDone = true;
		if (caller == null) {
			caller = Caller.SUITE;
		}
		initReport();
		if (!new File(resultsPath).isDirectory()) {
			new File(resultsPath).mkdirs();
		}
		summary.setTitle(Config.getEnvDetails("report", "title"));
		summary.setStartTime(DateHelper.getCurrentDatenTime("MM-dd-yyyy") + " " + DateHelper.getCurrentDatenTime("hh-mm-ss_a zzz"));
		summary.setEnv(Config.env);
		summary.setBuild(Config.getEnvDetails("aut", "build"));
		// start reporters
		ExtentSparkReporter htmlReporter = new ExtentSparkReporter(resultsPath + File.separator + REPORT_HTML_FILE_NAME);
		// report title
		htmlReporter.config().setDocumentTitle(Config.getEnvDetails("report", "title"));
		// report or build name
		htmlReporter.config().setReportName(Config.getEnvDetails("report", "title"));

		// TODO Offline report. commented due to current version not supported.
		 htmlReporter.config().enableOfflineMode(true);
		
		// TODO - Following future is not enabled Chart by default is off
//		htmlReporter.config().setChartVisibilityOnOpen(false);

		// create ExtentReports and attach reporter(s)
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("User", System.getProperty("user.name"));
		extent.setSystemInfo("Java version", System.getProperty("java.version"));

		extent.setSystemInfo("Environment", Config.env);
		extent.setSystemInfo("Build Number", Config.getEnvDetails("aut", "build"));

		extent.setSystemInfo("Test Data File", Config.getEnvDetails("testData", "dataFileName"));
		extent.setSystemInfo("Data Factory File", Config.getEnvDetails("testData", "tdmFileName"));

		// Add excel output
		if (Config.getEnvDetails("config", "generateMetaExcel").equalsIgnoreCase(YES)) {
			verifyAndCreate(Config.getEnvDetails("config", "generateMetaExcel"), "Data");
		}

	}

	public static synchronized void endSuite() {
		summary.setEndTime(DateHelper.getCurrentDatenTime("MM-dd-yyyy") + " " + DateHelper.getCurrentDatenTime("hh-mm-ss_a zzz"));

		String reportTypes = Config.getEnvDetails("report", "type");

		if ((";" + reportTypes + ";").toUpperCase().contains(";HTML;")) {
			HTMLReport.endOfSuite(resultsPath, summary, new ArrayList<TestCaseElement>(testcases.values()));
		}
		
		//ALM Integration
		//executeALM("5401","AllTabs");
//		summary = null;
//		testcases=null;
//		testcases_steps =null;

		// cleanup
		resetValues();
	}

	public static void resetValues() {
		if (caller == Caller.TEST) {
			summary = new SummaryElement();
			testcases.clear();
		}
		testcases_steps.clear();
		extent.flush();
	}

	//ALM Integration
	public static void executeALM(String testsetid,String tcname) {
		LOG.info("ALM File status and File upload function process...");
		String status = "Failed";
		if (summary.tcfail == 0) {
			status = "Passed";
		}
		if (StringUtils.isBlank(tcname)){
			TestCaseElement a = new ArrayList<TestCaseElement>(testcases.values()).get(0); 
			tcname = a.name; 
		}
		LOG.info("Test Case Name: " + tcname);
//		for (TestCaseElement a: new ArrayList<TestCaseElement>(testcases.values())) {
			
//			ExcelDataAccess tracebility = new ExcelDataAccess("", "EnvConfig.xlsx");
			try {
//				tracebility.setDatasheetName("Tracebility");
//				String id = TestCase.getIteration(tc)
//				String testsetid = tracebility.getValue(a.name, "ALM Test Set ID");
//				String testname = tracebility.getValues(a.name, "ALM TC").toString();
//				System.out.println(tracebility.getValues(a.name, "ALM TC").toString());
//				for (String almtc: tracebility.getValues(a.name, "ALM TC")) {
//				System.out.println(tcname);
				updateALM(testsetid, tcname, "TAS-ALM-Run", almZipFileName, status);
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
	}
	
	/**
	 * ALM connect and update the status and attachment
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
	
	public static void zipReport() {
		almZipFileName = resultsPath+ "\\"+ resultsPath.replace("\\", "_") + ".zip";
		LOG.info("Report Zip File Create. File Name: "+ almZipFileName, Status.PASS);
		new ZipHelper().zipDirectory(new File(resultsPath), almZipFileName );
	}
	
	public static synchronized void openReport() {
		String reportPath = resultsPath + File.separator + REPORT_HTML_FILE_NAME;
		System.out.println("Report File Path: " + reportPath);
		File htmlFile = new File(reportPath);
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void startTest(TestCase tc) {
		// set thread name to TC Name so Report.log can recognize steps to tescase based
		// on thread name
		if (tc.getAttribute(NAME).equalsIgnoreCase(tc.getAttribute(ITERATION))) {
			Thread.currentThread().setName(tc.getAttribute(NAME));
		} else {//TODO set thread name as test case name
			Thread.currentThread().setName(tc.getAttribute(NAME) + "_" + tc.getAttribute(ITERATION));
		}

		if (caller == null) {
			caller = Caller.TEST;
			startSuite();
		}

		TestCaseElement tcInfo = addTcInfo(tc);
		tcInfo.resultFolderPath = tcInfo.uname + "_" + tcInfo.starttime.replace(":", "");
		tcInfo.resultFileName = tcInfo.resultFolderPath;

		if (!new File(resultsPath + File.separator + tcInfo.resultFolderPath).isDirectory()){
			new File(resultsPath + File.separator + tcInfo.resultFolderPath).mkdirs();
		}

		// TODO - remove this from TestCase and here
		tc.setAttribute("resultPath", resultsPath + File.separator + tcInfo.resultFolderPath);

		// creates a toggle for the given test, adds all log events under it
		tcInfo.description = tc.getAttribute("description");
		test = extent.createTest(tcInfo.uname, tcInfo.description);
		
		for (String t: tc.getAttribute("Tags").split(",")) {
			test.assignCategory(t);
		}
		//ALM Integration
		test.assignAuthor(StringUtils.isBlank(tc.getAttribute("Author")) ? "" : tc.getAttribute("Author"));
	}

	/**
	 * <b>Description</b>Ends Test case Reporting
	 * 
	 * @param tc Test case Id
	 */
	public static synchronized void endTest(TestCase tc) {
		TestCaseElement tcInfo = getTcInfo();
		// re- assigning the description for capturing the iteration specific iteration.
		tcInfo.description = tc.getAttribute("description");
		List<StepElement> steps = getTcSteps();
		tcInfo.endtime = DateHelper.getCurrentDatenTime("hh:mm:ss");

		if (tcInfo.fail > 0) {
			tcInfo.status = Status.Fail;
			summary.incFailCnt();
		} else if (tcInfo.warning > 0) {
			tcInfo.status = Status.WARN;
			summary.incWarnCnt();
		} else if (tcInfo.pass > 0) {
			tcInfo.status = Status.Pass;
			summary.incPassCnt();
		}
		summary.incTotalCnt();
		tc.setAttribute("tcStatus", tcInfo.status.toString());

		String reportTypes = Config.getEnvDetails("report", "type");

		if ((";" + reportTypes + ";").toUpperCase().contains(";HTML;")) {
			HTMLReport.endOfTest(resultsPath, tcInfo, steps);
		}

		removeStepInfo();

		// cleanup
		if (caller == Caller.TEST) {
			removeTCInfo();
		}

		endSuite();

		// open report if caller is JUnit
		if (caller == Caller.TEST) {
			openReport();
		}
	}

	public static synchronized void log(String message, Status status) {
		boolean takeScreenShot = false;
		String screenShot = null, reportStatus = null;
		String screenShotPath = null;
		TestCaseElement tcInfo = getTcInfo();
		tcInfo.rStepCounter++;
		switch (status) {
		case BUSINESSSTEP:
			takeScreenShot = false;
			tcInfo.bStepCounter++;
			tcInfo.rStepCounter = 0;
			reportStatus = "BUSINESSSTEP";
			break;
		case KEYWORD:
			takeScreenShot = false;
			reportStatus = "KEYWORD";
			tcInfo.rStepCounter++;
			break;
		case Pass:
			tcInfo.pass++;
			takeScreenShot = true;
			reportStatus = "Pass";
			break;
		case PASS:
			tcInfo.pass++;
			takeScreenShot = false;
			reportStatus = "Pass";
			break;
		case FAIL:
		case Fail:
			tcInfo.fail++;
			takeScreenShot = true;
			reportStatus = "Fail";
			break;
		case fail:
			tcInfo.fail++;
			takeScreenShot = false;
			reportStatus = "Fail";
			break;// 390661
		case DONE:
			takeScreenShot = false;
			reportStatus = "Done";
			break;
		case WARN:
			tcInfo.warning++;
			takeScreenShot = true;
			reportStatus = "Warn";
			break;
		case DEBUG: //unusedS
			break;
		case SKIP://implementation to support Extent-report 5.0.9
			tcInfo.skip++;
			takeScreenShot = false;
			reportStatus = "Skip";
			break;
		}

		// Code for uploading absolute path to generated RQM pdf files
		if (!Config.getEnvDetails("config", "seleniumNeeded").equalsIgnoreCase("No") && takeScreenShot) {
			screenShot = "ss_" + tcInfo.bStepCounter + "_" + tcInfo.rStepCounter + ".png";
			Report.takeScreenShot(resultsPath + File.separator + tcInfo.resultFolderPath + File.separator + screenShot,
					tcInfo.getWebDriver());
//				File file = new File(screenShot);
//				screenShotPath = file.getAbsolutePath().split(screenShot, 2)[0];
//				screenShotPath = screenShotPath.replace("\\", "/");
			screenShotPath = tcInfo.resultFolderPath + File.separator + screenShot;
		} else if (takeScreenShot) {
			screenShot = "ss_" + tcInfo.bStepCounter + "_" + tcInfo.rStepCounter + ".png";
			fullPageWindowsScreenshot(
					resultsPath + File.separator + tcInfo.resultFolderPath + File.separator + screenShot);
//			File file = new File(screenShot);
//			screenShotPath = file.getAbsolutePath().split(screenShot, 2)[0];
//			screenShotPath = screenShotPath.replace("\\", "/");
			screenShotPath = tcInfo.resultFolderPath + File.separator + screenShot;
		}

		StepElement stp = new StepElement();

		if (tcInfo.rStepCounter == 0) {
			stp.stepid = String.valueOf(tcInfo.bStepCounter);
		} else {
			stp.stepid = tcInfo.bStepCounter + "." + tcInfo.rStepCounter;
		}
		stp.tcid = tcInfo.tcid;
		stp.description = message;
		stp.status = reportStatus;
		stp.time = DateHelper.getCurrentDatenTime("hh:mm:ss");
		stp.screenshot = screenShot;
		stp.screenShotPath = screenShotPath;

		System.out.println(stp.time + " :: Status-" + status.toString() + " :: " + message);

		message = message.replaceAll("\n", "<br>");

		if (status.equals(Status.BUSINESSSTEP)) {
			childTest = test.createNode(message);
		} else {
			if (screenShot == null) {
				if (childTest != null) {
					childTest.log(getExtentStatus(status), message);
				} else {
					test.log(getExtentStatus(status), message);
				}
			} else {
				if (childTest != null) {
					childTest.log(getExtentStatus(status), message,
							MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
				} else {
					test.log(getExtentStatus(status), message,
							MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
				}
			}
		}
		getTcSteps().add(stp);
	}

	public static synchronized void log(String[][] data, Status status) {
		Markup m = MarkupHelper.createTable(data);
		if (childTest != null)
			childTest.log(getExtentStatus(status), m);
		else
			test.log(getExtentStatus(status), m);
	}

	public static synchronized void log(Throwable e) {
		TestCaseElement tcInfo = getTcInfo();
		String screenShot = DateHelper.getCurrentDatenTime("dd-MM-yyyy HH-mm-ss") + ".png";
		String screenShotPath = resultsPath + File.separator + tcInfo.resultFolderPath + File.separator;
		fullPageWindowsScreenshot(screenShotPath + screenShot);
		File file = new File(screenShot);
		screenShotPath = file.getAbsolutePath().split(screenShot, 2)[0];
		//		screenShotPath = screenShotPath.replace("\\", "/");
		screenShotPath = screenShotPath + resultsPath + File.separator + tcInfo.resultFolderPath + File.separator
				+ screenShot;

		//		try {
		if (childTest == null) {
			test.log(com.aventstack.extentreports.Status.FAIL, e,
					MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		}else {
			childTest.log(com.aventstack.extentreports.Status.FAIL, e,
					MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
		}
		/*} catch (IOException e1) {
			e1.printStackTrace();
		}*/
	}

	public static synchronized void log(String type, String markuptext) {
		Markup m = null;
		if (type.equalsIgnoreCase("label")) {
			m = MarkupHelper.createLabel(markuptext, ExtentColor.BLUE);
		} else {
			m = MarkupHelper.createCodeBlock(markuptext);
		}
		if (childTest == null)
			test.info(m);
		else
			childTest.info(m);

	}

	public static synchronized void logDPValuesByGroup(DataProvider dp, String groupName) {
		ArrayList<String> li = dp.getColumnNames(groupName);

		String[][] data = new String[li.size()][2];
		for (int i = 0; i < li.size(); i++) {
			data[i][0] = li.get(i);
			try { //Fixed Null pointer exception
				data[i][1]=dp.get(groupName, li.get(i));
			}catch(NullPointerException e) {
				data[i][1]="";
			}
		}
		log(data, Status.DONE);
	}

	/**
	 * <b>Description</b>Custom defined business step for reports
	 * 
	 * @param message  Message of the custom report
	 * @param filePath Directory of file.
	 */
	public static synchronized void addAttachement(String message, String filePath) {
		TestCaseElement tcInfo = getTcInfo();
		tcInfo.rStepCounter++;

		StepElement stp = new StepElement();
		stp.stepid = tcInfo.bStepCounter + "." + tcInfo.rStepCounter;

		stp.tcid = "";
		stp.description = message;
		stp.status = "Done";
		stp.time = DateHelper.getCurrentDatenTime("hh:mm:ss");
		stp.screenshot = filePath;

		getTcSteps().add(stp);
	}

	/**
	 * <b>Description</b>Adds Footer to the reporting
	 * 
	 * @param key  Name of the Step
	 * @param value value of the step
	 */
	public static synchronized void addMetaInfo(String key, String value) {
		// footerAttributes.put(name, value);
		TestCaseElement tcInfo = getTcInfo();
		tcInfo.addMetaInfo(key, value);

		test.assignCategory(key + ":" + value);

		if (Config.getEnvDetails("config", "generateMetaExcel").equalsIgnoreCase("Yes")) {
			addDataToExcel(Thread.currentThread().getName(), key, value);
		}
	}

	private static synchronized TestCaseElement addTcInfo(TestCase tc) {
		String key = Thread.currentThread().getName();

		if (testcases.containsKey(key)) {
			return testcases.get(key);
		}

		TestCaseElement tcInfo = new TestCaseElement(tc, summary.runid);
		testcases.put(key, tcInfo);

		return tcInfo;
	}

	private static TestCaseElement getTcInfo() {
		String key = Thread.currentThread().getName();

		if (testcases.containsKey(key)) {
			return testcases.get(key);
		} else {
			return null;
		}
	}

	private static List<StepElement> getTcSteps() {
		String key = Thread.currentThread().getName();

		if (!testcases_steps.containsKey(key)) {
			testcases_steps.put(key, new ArrayList<StepElement>());
		}

		return testcases_steps.get(key);
	}

	private static void removeStepInfo() {
		String key = Thread.currentThread().getName();

		if (testcases_steps.containsKey(key)) {
			testcases_steps.get(key).clear();
			testcases_steps.remove(key);
		}
	}

	private static void removeTCInfo() {
		String key = Thread.currentThread().getName();

		if (!testcases.containsKey(key)) {
			testcases.remove(key);
		}
	}

	/**
	 * <b>Description</b>Captures web driver screen shot
	 * 
	 * @param path Directory of file.
	 */
	public static synchronized void takeScreenShot(String path, WrappedWebDriver driver) {
		File scrFile1 = null;
		String browser = Config.getEnvDetails("browser", "fullPageScreenshot");

		if (browser.equalsIgnoreCase("yes")) {
			// TODO: review
			try {
				scrFile1 = driver.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile1, new File(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			File f = new File(path);
			if (!(f.exists())) {
				fullPageWindowsScreenshot(path);
			}
		}
		File f = new File(path);
		if (!(f.exists())) {
			fullPageWindowsScreenshot(path);
		}
	}

	/**
	 * <b>Description</b>Captures Robot screen shot
	 * 
	 * @param path Directory of file.
	 */
	public static synchronized void fullPageWindowsScreenshot(String path) {
		try {
			// Get the screen size
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			Rectangle rect = new Rectangle(0, 0, screenSize.width, screenSize.height);
			Robot robot = new Robot();
			BufferedImage image = robot.createScreenCapture(rect);
			File file;

			// Save the screenshot as a png
			file = new File(path);
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("rawtypes")
	public static synchronized void writeToHTML(Class cls, Object obj, String htmlFile, String xslFile) {
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			StreamSource xslt = new StreamSource(xslFile); // xslFile = "report/tc.xsl"
			Transformer transformer = tf.newTransformer(xslt);

			JAXBContext ctx = JAXBContext.newInstance(cls);
			JAXBSource source = new JAXBSource(ctx, obj);

			PrintStream p = new PrintStream(new FileOutputStream(htmlFile));
			StreamResult result = new StreamResult(p);

			transformer.transform(source, result);
			p.close();
		} catch (Exception e) {
			LOG.error("Error on writeToHTML: " + e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public static synchronized void writeToXML(Class cls, Object obj, String htmlFile, String xslFile) {
		try {
			JAXBContext ctx = JAXBContext.newInstance(cls);
			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);
			/*
			 * if(xslFile!=null && !xslFile.isEmpty()){
			 * m.setProperty("com.sun.xml.bind.xmlHeaders",
			 * "<?xml-stylesheet type='text/xsl' href='"+xslFile+"' ?>"); }
			 */

			StringWriter sw = new StringWriter();
			sw.write("<?xml version='1.0'?>");
			sw.write("\n");
			sw.write("<?xml-stylesheet type='text/xsl' href='" + xslFile + "'?>");
			sw.write("\n");

			m.marshal(obj, sw);
			sw.close();

			PrintStream p = new PrintStream(new FileOutputStream(htmlFile));
			p.print(sw.toString());
			p.close();
		} catch (Exception e) {
			LOG.error("Error on writeToXML: " + e.getMessage());
		}
	}

	//Merged and fixed PMD issues
	private static com.aventstack.extentreports.Status getExtentStatus(Status cogStatus) {
		com.aventstack.extentreports.Status extentStatus;
		switch (cogStatus) {
			case Pass:
			case PASS:
				extentStatus = com.aventstack.extentreports.Status.PASS;
				break;
			case FAIL:
			case Fail:
			case fail:
				extentStatus = com.aventstack.extentreports.Status.FAIL;
				break;
			case WARN:
				extentStatus = com.aventstack.extentreports.Status.WARNING;
				break;
			case DEBUG:
				extentStatus = com.aventstack.extentreports.Status.INFO;
				break;
			case SKIP: //Test Skipped
				extentStatus = com.aventstack.extentreports.Status.SKIP;
				break;
			case DONE:
			case BUSINESSSTEP:
			case KEYWORD:
			default:
				extentStatus = com.aventstack.extentreports.Status.INFO;
		}
		return extentStatus;
	}

	public static synchronized void closeBusinessStep() {
		childTest = null;
	}

	private static void verifyAndCreate(String filename, String sheetname) {
		excel = new ExcelDataAccess("", filename);
		if (!excel.checkExistance()) {
			try {
				excel.createWorkbook();
			} catch (Exception e) {
				// DO Nothing
				e.printStackTrace();
			}
		}
		try {
			excel.setDatasheetName(sheetname);
		} catch (Exception e) {
			try {
				excel.addSheet(sheetname);
				excel.setDatasheetName(sheetname);
				excel.setValue(0, 0, "Testcase");
			} catch (Exception e1) {
				// DO Nothing
				e1.printStackTrace();
			}
		}
	}

	private static void addDataToExcel(String row, String column, String value) {
		try {
			if (excel.getRowNum(row, 0) == -1) {
				int rowNum = excel.addRow();
				excel.setValue(rowNum, "Testcase", row);
			}
			if (excel.getColumnNum(column, 0) == -1) {
				excel.addColumn(column);
			}
			excel.setValue(row, column, value);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if report is available.
	 * 
	 * @return <code>true</code> if report is available, otherwise
	 *         <code>false</code>.
	 */
	public static boolean isReportExists() {
		boolean reportExist = Boolean.FALSE;

		String reportPath = resultsPath + File.separator + REPORT_HTML_FILE_NAME;
		System.out.println("Report File Path: " + reportPath);
		File htmlFile = new File(reportPath);

		if (htmlFile.exists() && !htmlFile.isDirectory()) {
			reportExist = Boolean.TRUE;
		}

	    return reportExist;
	}

	/**
	 * @param caller the caller to set
	 */
	public static synchronized void setCaller(Caller theCaller) {
		caller = theCaller;
	}

	/**
	 * @return the caller
	 */
	public static Caller getCaller() {
		return caller;
	}
}