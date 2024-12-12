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
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriverService;

import winium.wrappers.CustomWiniumDriver;

public class WrappedWiniumDriver extends RemoteWebDriver {

	CustomWiniumDriver driver = null;
	SessionId winiumSessionId = null;
	WiniumDriverService service = null;
	
	/**
	 * <b>Description</b> Gets WrappedWiniumDriver
	 */
	public WrappedWiniumDriver() throws Exception {
		String section = Config.getEnvDetails("config", "appname");
		String apppath = Config.getEnvDetails(section, "APP_PATH");
		DesktopOptions options = new DesktopOptions();
		options.setApplicationPath(apppath);

		String winiumDriverPath = Config.getEnvDetails("winium", "driverPath");
		System.out.println("\n\nWinium Driver Path: " + winiumDriverPath);

		int winiumDriverPort = 0;
		if (StringUtils.isNotBlank(Config.getEnvDetails("winium", "port"))) {
			winiumDriverPort = Integer.valueOf(Config.getEnvDetails("winium", "port"));
		}
		String winiumLogPath = Config.getEnvDetails("winium", "logPath");
		String winiumJsonOutPath = Config.getEnvDetails("winium", "jsonOutputPath");

		// Create a service to launch Winium Driver
		service = new WrappedWiniumDriverService.Builder()
				.usingDriverExecutable(new File(winiumDriverPath))
				.usingPort(winiumDriverPort)
				.withLogFile(winiumLogPath)
				.withJsonOutputPath(winiumJsonOutPath)
				.buildDesktopService();
		service.start();

		driver = new CustomWiniumDriver(service, options);
		winiumSessionId = driver.getSessionId();
		System.out.println("Started Session: " + winiumSessionId.toString());
	}

	public CustomWiniumDriver getWiniumDriver() {
		return driver;
	}
	
	public void close() {
		if (driver != null) {
			driver.close();
		}
	}

	public RemoteWebElement findElement(By arg0) {
		return (RemoteWebElement) driver.findElement(arg0);
	}

	public List<WebElement> findElements(By arg0) {
		return driver.findElements(arg0);
	}

	public void get(String arg0) {
		driver.get(arg0);
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public String getPageSource() {
		return driver.getPageSource();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	public void quit() {
		driver.quit();
	}
	
	public Actions actions() {
		Actions builder = new Actions(driver);
		return builder;
	}

	public void kill() {
		driver.quit();//.kill();
	}

	@Override
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	@Override
	public Navigation navigate() {
		return driver.navigate();
	}

	@Override
	public Options manage() {
		return driver.manage();
	}

	/**
	 * Stops the Winium Driver Service
	 */
	public void stopService() {
		if (service != null && service.isRunning()) {
			service.stop();
		}
	}

	/**
	 * Returns the Winium Driver Service URL
	 * 
	 * @return the Winium Driver Service URL
	 */
	public String getServiceUrl() {
		return service.getUrl().toString();
	}
}
