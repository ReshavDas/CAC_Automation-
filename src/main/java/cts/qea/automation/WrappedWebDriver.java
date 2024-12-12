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
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.*;//AJD
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

/**All deprecated interfaces listed below will be deleted in tas framework next version
* FindsById, FindsByClassName, FindsByLinkText, FindsByName,FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices 
*/
public class WrappedWebDriver
		implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText, FindsByName,
		FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities, TakesScreenshot {

	private static final Logger LOG = LoggerFactory.getLogger(WrappedWebDriver.class);
	WebDriver driver = null;

	/**
	 * <b>Description</b> Gets WrappedWebDriver
	 */
	public WrappedWebDriver() throws Exception {
		String browser = "firefox";
		String grid = "false";
		String hub_url = "";
		String grid_cap_platform = "Windows";
		String grid_cap_browser_version = "";

		hub_url = Config.getEnvDetails("browser", "grid_hub");
		// #browser must be one of {chrome|edge}  /AJD
		browser = Config.getEnvDetails("browser", "browser");
		grid = Config.getEnvDetails("browser", "grid");
		grid_cap_platform = Config.getEnvDetails("browser", "grid_cap_platform");
		grid_cap_browser_version = Config.getEnvDetails("browser", "grid_cap_browser_version");

		if (grid.equalsIgnoreCase("true")) {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setBrowserName(browser);

			if (!grid_cap_platform.isEmpty()) {
				Platform enumval = Platform.valueOf(grid_cap_platform);
				capabilities.setPlatform(enumval);
			}

			if (!grid_cap_browser_version.isEmpty()) {
				capabilities.setVersion(grid_cap_browser_version);
			}
			try {
				if (browser.equalsIgnoreCase("internet explorer")) {
					DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
					ieCapabilities.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					ieCapabilities.setJavascriptEnabled(true);
					ieCapabilities.setCapability("nativeEvents", false);
					ieCapabilities.setCapability("requireWindowFocus", true);
					driver = new RemoteWebDriver(new URL(hub_url), ieCapabilities);
				} else {
					driver = new RemoteWebDriver(new URL(hub_url), capabilities);
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					driver = new RemoteWebDriver(DesiredCapabilities.internetExplorer());
				} catch (Exception e1) {
					e1.printStackTrace();
					driver = new RemoteWebDriver(DesiredCapabilities.firefox());
				}
			}
		} else {
			try {
				if (browser.equalsIgnoreCase("internet explorer")) {
					InternetExplorerOptions options = new InternetExplorerOptions();
					if (Config.getEnvDetails("config", "EnsureCleanSession").equalsIgnoreCase("yes")) {
						options.destructivelyEnsureCleanSession();
					}

					options.enablePersistentHovering();
					options.requireWindowFocus();
					options.introduceFlakinessByIgnoringSecurityDomains();
					options.ignoreZoomSettings();

					System.setProperty("webdriver.ie.driver", new File(".").getCanonicalPath() + File.separator
							+ "binaries" + File.separator + "IEDriverServer.exe");
					driver = new InternetExplorerDriver(options);
				} else if (browser.equalsIgnoreCase("firefox")) {

					System.setProperty("webdriver.gecko.driver", new File(".").getCanonicalPath() + File.separator
							+ "binaries" + File.separator + "geckodriver.exe");
					File pathToBinary = new File("C:" + File.separator + "Program Files" + File.separator
							+ "Mozilla Firefox" + File.separator + "firefox.exe");
					FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
					FirefoxProfile fp = new FirefoxProfile();
					fp.setPreference("browser.startup.homepage", "about:blank");
					fp.setPreference("startup.homepage_welcome_url", "about:blank");
					fp.setPreference("startup.homepage_welcome_url.additional", "about:blank");
					FirefoxOptions fo = new FirefoxOptions().setBinary(ffBinary);
					fo.setProfile(fp);
					driver = new FirefoxDriver(fo);
				}
				// Removed PhantomJS - Officially not supported
				// https://github.com/SeleniumHQ/selenium/issues/5295
				else if (browser.equalsIgnoreCase("chrome")) {
					System.setProperty("webdriver.chrome.driver", new File(".").getCanonicalPath() + File.separator
							+ "binaries" + File.separator + "chromedriver.exe");
					ChromeOptions options = new ChromeOptions();
					// fix for issue with Chrome browser version 114.0.5735.199 (64-bit)
					// unknown error: DevToolsActivePort file doesn't exist
					options.addArguments("--remote-debugging-port=9222");
					driver = new ChromeDriver(options);
				} else if (browser.equalsIgnoreCase("opera")) {
					driver = new OperaDriver();
				} else if (browser.equalsIgnoreCase("safari")) {
					driver = new SafariDriver();
				} else if (browser.equalsIgnoreCase("Edge")) {  //ajd
					System.setProperty("webdriver.edge.verboseLogging", "true");
					System.setProperty("webdriver.edge.driver", new File(".").getCanonicalPath() + File.separator
							+ "binaries" + File.separator + "msedgedriver.exe");
					EdgeDriverService service = EdgeDriverService.createDefaultService();
										
					driver = new EdgeDriver(service);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("Driver initialization failed: " + e.getMessage());
				Report.log("Driver initialization failed", Status.WARN);
				Report.log(e);
			}
		}

		String timeOut = Config.getEnvDetails("browser", "timeoutMilli");
		if (timeOut != null){
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(timeOut), TimeUnit.MILLISECONDS);
		} else {
			driver.manage().timeouts().implicitlyWait(Integer.parseInt("20000"), TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void close() {
		driver.close();
	}

	@Override
	public WebElement findElement(By arg0) {
		return driver.findElement(arg0);
	}

	@Override
	public List<WebElement> findElements(By arg0) {
		return driver.findElements(arg0);
	}

	@Override
	public void get(String arg0) {
		driver.get(arg0);
		Report.log("url", arg0);
	}

	@Override
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	@Override
	public Options manage() {
		return driver.manage();
	}

	@Override
	public Navigation navigate() {
		return driver.navigate();
	}

	@Override
	public void quit() {
		driver.quit();
	}

	@Override
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	public Object trigger(String script) {
		return ((JavascriptExecutor) driver).executeScript(script);
	}

	public Object trigger(String script, WebElement element) {
		return ((JavascriptExecutor) driver).executeScript(script, element);
	}

	/**
	 * Opens a new tab for the given URL
	 *
	 * @param url The URL to
	 * @throw JavaScriptException If unable to open tab
	 */
	public void openTab(String url) {
		String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
		// Create Object reference
		Object element = ((JavascriptExecutor) driver).executeScript(String.format(script, url));
		if (element instanceof WebElement) {
			WebElement anchor = (WebElement) element;
			anchor.click();
			((JavascriptExecutor) driver).executeScript("var a=arguments[0];a.parentNode.removeChild(a);", anchor,
					driver);

		} else {
			// throw new JavaScriptException(element, "Unable to open tab", 1);
		}
	}

	public Actions actions() {
		Actions builder = new Actions(driver);
		return builder;
	}

	@Override
	@Deprecated
	public Keyboard getKeyboard() {
		return ((HasInputDevices) getAugmentedDriver(driver)).getKeyboard();
	}

	@Override
	@Deprecated
	public Mouse getMouse() {
		HasInputDevices hasInputDevice = (HasInputDevices) getAugmentedDriver(driver);
		return hasInputDevice.getMouse();
	}

	@Override
	public Capabilities getCapabilities() {
		return ((HasCapabilities) getAugmentedDriver(driver)).getCapabilities();
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		return ((TakesScreenshot) getAugmentedDriver(driver)).getScreenshotAs(arg0);
	}

	protected WebDriver getAugmentedDriver(WebDriver driver) {
		// If the driver is instance of RemoteWebDriver return augmented driver
		if (driver.getClass().equals(RemoteWebDriver.class)) {
			driver = new Augmenter().augment(driver);
		} else {
			driver = this.driver;
		}
		return driver;
	}

	public Object executeAsyncScript(String arg0, Object... arg1) {
		return ((RemoteWebDriver) driver).executeAsyncScript(arg0, arg1);
	}
	@Override
	public Object executeScript(String arg0, Object... arg1) {
		return ((RemoteWebDriver) driver).executeScript(arg0, arg1);
	}

	@Override
	public WebElement findElementById(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.id(arg0));
	}

	@Override
	public List<WebElement> findElementsById(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.id(arg0));
	}

	@Override
	public WebElement findElementByClassName(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.className(arg0));
	}

	@Override
	public List<WebElement> findElementsByClassName(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.className(arg0));
	}

	@Override
	public WebElement findElementByLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.linkText(arg0));
	}

	@Override
	public WebElement findElementByPartialLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.partialLinkText(arg0));
	}

	@Override
	public List<WebElement> findElementsByLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.linkText(arg0));
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.partialLinkText(arg0));
	}

	@Override
	public WebElement findElementByName(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.name(arg0));
	}

	@Override
	public List<WebElement> findElementsByName(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.name(arg0));
	}

	@Override
	public WebElement findElementByCssSelector(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.cssSelector(arg0));
	}

	@Override
	public List<WebElement> findElementsByCssSelector(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.cssSelector(arg0));
	}

	@Override
	public WebElement findElementByTagName(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.tagName(arg0));
	}

	@Override
	public List<WebElement> findElementsByTagName(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.tagName(arg0));
	}

	@Override
	public WebElement findElementByXPath(String arg0) {
		return ((RemoteWebDriver) driver).findElement(By.xpath(arg0));
	}

	@Override
	public List<WebElement> findElementsByXPath(String arg0) {
		return ((RemoteWebDriver) driver).findElements(By.xpath(arg0));
	}

	public void kill() {
		((FirefoxDriver) driver).quit();// .kill();
	}

	public WebDriver getWebDriver() {
		return (WebDriver) driver;
	}

}