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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cts.qea.automation.exception.TASSendKeyOperationUnsupportedExecution;
import cts.qea.automation.exception.NotImplementedException;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

public class WrappedWebElement implements WebElement {

	final WrappedWebDriver driver;
	final By findBy;
	final String name;
	final WebElement webelement;

	/**
	 * <b>Description</b> Initializing driver, HTML element name , By Property
	 * 
	 * @param driver WebDriver
	 * @param name   HTML element name
	 * @return By Find By Property
	 */
	WrappedWebElement(WrappedWebDriver driver, String name, By findBy) {
		this.driver = driver;
		this.name = name;
		this.findBy = findBy;
		this.webelement = null;
	}

	/**
	 * <b>Description</b> Initializing driver, HTML element name , By Property
	 * 
	 * @param driver WebDriver
	 * @param name   HTML element name
	 * @return By Find By Property
	 */
	WrappedWebElement(WrappedWebDriver driver, WebElement ele, String name, By findBy) {
		this.driver = driver;
		this.name = name;
		this.findBy = findBy;
		this.webelement = ele;
	}

	/**
	 * <b>Description</b> Wrapper to Web driver Click, will perform a click on HTML
	 * element
	 */
	public void click() {

		try {
			getWait().until(ExpectedConditions.elementToBeClickable(findby()));
			findby().click();
		} catch (Exception E) {
			getWait().until(ExpectedConditions.elementToBeClickable(findby()));
			findby().click();
		}

		Report.log(prettyName(name) + " is clicked", Status.DONE);
	}

	private WebElement findby() {
		if (webelement == null) {
			return driver.findElement(findBy);
		} else {
			return webelement.findElement(findBy);
		}
	}

	private WebDriverWait getWait() {
		return new WebDriverWait(driver, (Integer.parseInt(Config.getEnvDetails("browser", "timeoutMilli")) / 1000));
	}

	/**
	 * <b>Description</b> Wrapper to Webdriver SendKeys , which will type characters
	 * in Text box
	 * 
	 * @param keys Char Sequences to enter in HTML element Text Box
	 */
	public final void sendKeys(CharSequence... keys) {
		// exit without doing anything if param is null or null string
		if (keys[0] == null || keys[0].equals(""))
			return;

		// treat sendKeys as clear if keys is BLANK
		if (keys[0].toString().toUpperCase().equalsIgnoreCase("BLANK")) {
			clear();
			return;
		}

		try {
			getWait().until(ExpectedConditions.elementToBeClickable(findby()));
		} catch (Exception e) {
		}
		if (!findby().isEnabled()) {
			throw new TASSendKeyOperationUnsupportedExecution("The sendKey() operation can not supported when the element is disabled or read-only. Element Name: " + name);
		}
		findby().clear();
		findby().sendKeys(keys[0].toString());

		if (findby().getAttribute("type").equalsIgnoreCase("password")) {
			Report.log(prettyName(name) + " is entered", Status.DONE);
		} else {
			Report.log(prettyName(name) + " entered as " + keys[0].toString(), Status.DONE);
		}
	}

	/**
	 * <b>Description</b> Wrapper to Webdriver select , which will select a value in
	 * Drop Down
	 * 
	 * @param dropDownValues Drop down value in the select box
	 */
	public void select(String dropDownValues) {

		if (dropDownValues == null || dropDownValues.equalsIgnoreCase(""))
			return;
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		boolean changeDropDown = false;
		if (findby().getCssValue("display").equalsIgnoreCase("none")) {
			executor.executeScript("arguments[0].style.display='block';", findby());
			changeDropDown = true;
		}

		for (String listBoxValue : dropDownValues.split(";")) {
			// treat select as clear if dropDownValue is BLANK
			if (listBoxValue.toString().toUpperCase().equalsIgnoreCase("BLANK")) {
				try {
					new Select(findby()).selectByVisibleText("");
				} catch (Exception e) {
					new Select(findby()).selectByVisibleText(" ");
					new Select(findby()).deselectAll();
				}
				return;
			}
			if (listBoxValue.toLowerCase().startsWith("index=")) {
				// If parameter starts with index= then the index value will be selected
				new Select(findby()).selectByIndex(Integer.parseInt(listBoxValue.split("=")[1]));

			} else if (listBoxValue.toLowerCase().startsWith("value=")) {
				// If parameter starts with value= then the value will be selected
				new Select(findby()).selectByValue(listBoxValue.split("=")[1]);

			} else {
				new Select(findby()).selectByVisibleText(listBoxValue);
			}
			try {
				dropDownValues = new Select(findby()).getFirstSelectedOption().getText();
			} catch (Exception e) {
				dropDownValues = "";
			}
		}

		if (changeDropDown)
			executor.executeScript("arguments[0].style.display='none';", findby());
		Report.log(prettyName(name) + " is selected as " + dropDownValues, Status.DONE);
	}

	/**
	 * <b>Description</b> Wrapper to Webdriver check , which will check an option
	 * box
	 * 
	 * @param checkType Check Type (CHECK/UNCHECK)
	 */
	public void check(String checkType) {
		if (checkType == null || checkType.equalsIgnoreCase(""))
			return;

		WebDriverWait wait = new WebDriverWait(driver,
				(Integer.parseInt(Config.getEnvDetails("browser", "timeoutMilli")) / 1000));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(findBy));
		} catch (Exception e) {
		}
		try {
			if (checkType.toUpperCase().equalsIgnoreCase("Y") || checkType.toUpperCase().equalsIgnoreCase("YES")
					|| checkType.toUpperCase().equalsIgnoreCase("TRUE") || checkType.toUpperCase().equalsIgnoreCase("C")
					|| checkType.toUpperCase().equalsIgnoreCase("CHECK")) {
				if (!findby().isSelected()) {
					findby().click();
				}
			} else if (checkType.toUpperCase().equalsIgnoreCase("N") || checkType.toUpperCase().equalsIgnoreCase("NO")
					|| checkType.toUpperCase().equalsIgnoreCase("FALSE")
					|| checkType.toUpperCase().equalsIgnoreCase("U")
					|| checkType.toUpperCase().equalsIgnoreCase("UNCHECK")) {
				if (findby().isSelected()) {
					findby().click();
				}
			}
		} catch (InvalidElementStateException e) {
			findby().click();
		}

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
		} catch (Exception e) {
		} finally {
			/*************** Temp Solution ********/
			try {
				driver.switchTo().alert().accept();
				driver.switchTo().defaultContent();
			} catch (Exception e) {
			}

			try {
				if (findby().isSelected()) {
					Report.log(prettyName(name) + " is Checked", Status.DONE);
				} else {
					Report.log(prettyName(name) + " is Unchecked", Status.DONE);
				}
			} catch (InvalidElementStateException e) {
				Report.log("InvalidElementStateException happened so " + prettyName(name)
						+ " just clicked may be a check or uncheck", Status.DONE);
			}

		}
	}

	public WebElement findElement(By by) {
		return findby().findElement(by);
	}

	public List<WebElement> findElements(By by) {
		return findby().findElements(by);
	}

	public String getAttribute(String attrName) {
		return findby().getAttribute(attrName);
	}

	public String getCssValue(String arg0) {
		return findby().getCssValue(arg0);
	}

	public Point getLocation() {
		return findby().getLocation();
	}

	public Dimension getSize() {
		return findby().getSize();
	}

	public String getTagName() {
		return findby().getTagName();
	}

	public String getText() {
		String text = "";
		WebDriverWait wait = new WebDriverWait(driver,
				(Integer.parseInt(Config.getEnvDetails("browser", "timeoutMilli")) / 1000));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
			text = findby().getText();
		} catch (Exception e) {
		}
		return text;
	}

	public boolean isDisplayed() {
		boolean flag, changeDropDown = false;
		String timeOutInMilliSeconds = "10000";
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		try {

			if (findby().getCssValue("display").equalsIgnoreCase("none")) {
				executor.executeScript("arguments[0].style.display='block';", findby());

				changeDropDown = true;
			}
			if (!Config.getEnvDetails("browser", "timeoutMilli").equals(""))
				timeOutInMilliSeconds = Config.getEnvDetails("browser", "timeoutMilli");
			driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

			flag = findby().isDisplayed();
		} catch (Exception e) {
			flag = false;
		}
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(timeOutInMilliSeconds), TimeUnit.MILLISECONDS);
		if (changeDropDown)
			executor.executeScript("arguments[0].style.display='none';", findby());
		Report.log("Verified the visibility of " + prettyName(name) + " and visibility is found to be " + flag,
				Status.DONE);
		return flag;
	}

	public boolean isEnabled() {
		return findby().isEnabled();
	}

	public boolean isSelected() {
		return findby().isSelected();
	}

	public void submit() {
		findby().submit();
		Report.log(prettyName(name) + " is submitted", Status.DONE);
	}

	public void clear() {
		findby().clear();
		Report.log(prettyName(name) + " is set to BLANK", Status.DONE);
	}

	public WebElement getWebElement() {
		return findby();
	}

	public List<WebElement> getWebElements() {
		return driver.findElements(findBy);
	}

	public void assertExists(String name, ValidationType vType) throws AssertionError {
		Assert.assertTrue(vType, "Verify " + prettyName(name) + " is present on the page", isDisplayed());
	}

	public void assertNotExists(String name, ValidationType vType) throws AssertionError {
		List<WebElement> elements = driver.findElements(findBy);
		Assert.assertFalse(vType, "Verify " + prettyName(name) + " is not present on the page",
				elements.size() == 0 || elements.get(0).isDisplayed());
	}

	private static String prettyName(String name) {
		if (name.startsWith("req_"))
			name = name.replace("req_", "");

		if (name.startsWith("txt") && name.substring(0, 3).equalsIgnoreCase("txt")) {
			name = name.substring(3, name.length());

		} else if (name.startsWith("lst") && name.substring(0, 3).equalsIgnoreCase("lst")) {
			name = name.substring(3, name.length()) + " drop down";

		} else if (name.startsWith("btn") && name.substring(0, 3).equalsIgnoreCase("btn")) {
			name = name.substring(3, name.length()) + " button";

		} else if (name.startsWith("lnk") && name.substring(0, 3).equalsIgnoreCase("lnk")) {
			name = name.substring(3, name.length()) + " link";

		} else if (name.startsWith("opt") && name.substring(0, 3).equalsIgnoreCase("opt")) {
			name = name.substring(3, name.length()) + " radio button";

		} else if (name.startsWith("chk") && name.substring(0, 3).equalsIgnoreCase("chk")) {
			name = name.substring(3, name.length()) + " check box";

		} else if (name.startsWith("img") && name.substring(0, 3).equalsIgnoreCase("img")) {
			name = name.substring(3, name.length()) + " image";

		} else if (name.startsWith("tab") && name.substring(0, 3).equalsIgnoreCase("tab")) {
			name = name.substring(3, name.length()) + " tab";

		} else if (name.startsWith("dt_txt") && name.substring(0, 6).equalsIgnoreCase("dt_txt")) {
			name = name.substring(6, name.length());

		}

		return name;
	}

	public void mouseOver(long milliSec) {
		WebElement element = findby();
		actions().moveToElement(element).build().perform();
		try {
			Thread.sleep(milliSec);
		} catch (InterruptedException e) {
		}
	}

	public Actions actions() {
		Actions builder = new Actions((WebDriver) driver);
		return builder;
	}

	public boolean validateDropDownValues(ValidationType vType, String[] values, boolean checkOrder,
			boolean caseSensitive) {

		WebElement element = findby();
		boolean flag = true;

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		boolean changeDropDown = false;
		if (element.getCssValue("display").equalsIgnoreCase("none")) {
			executor.executeScript("arguments[0].style.display='block';", element);
			element = findby();
			changeDropDown = true;
		}
		List<WebElement> dropDownValues = element.findElements(By.tagName("option"));

		List<String> applicationValues = new ArrayList<String>();
		for (int i = 0; i < dropDownValues.size(); i++) {
			if (StringUtils.isNotBlank(dropDownValues.get(i).getText().toString().trim())){
				applicationValues.add(dropDownValues.get(i).getText().toString().trim());
			}
		}

		if (changeDropDown){
			executor.executeScript("arguments[0].style.display='none';", element);
		}

		List<String> checkWithValues = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) {
			checkWithValues.add(values[i]);
		}

		if (checkOrder) {
			if (caseSensitive) {
				if (applicationValues.equals(checkWithValues)) {
					flag = true;
				} else {
					flag = false;
				}
			} else {
				for (int i = 0; i < applicationValues.size(); i++) {
					if (checkWithValues.get(i).equalsIgnoreCase(applicationValues.get(i))) {
						flag = true;
					} else {
						flag = false;
						break;
					}
				}
			}
		}

		if (!checkOrder && caseSensitive) {
			for (int i = 0; i < applicationValues.size(); i++) {
				if (checkWithValues.contains(applicationValues.get(i))) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}

		if (!checkOrder && !caseSensitive) {
			for (int i = 0; i < applicationValues.size(); i++) {
				flag = false;
				for (int j = 0; j < checkWithValues.size(); j++) {
					if (checkWithValues.get(j).equalsIgnoreCase(applicationValues.get(i))) {
						flag = true;
					}
				}
				if (!flag) {
					break;
				}
			}
		}
		return flag;
	}

	public void ajaxWait(WaitFor ajaxWait, ValidationType vType) {
		String timeOutInMilliSeconds = "20000";

		timeOutInMilliSeconds = Config.getEnvDetails("browser", "timeoutMilli");
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(timeOutInMilliSeconds) / 1000, 200);
		try {
			switch (ajaxWait) {
			case REFRESH:
				wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(findBy)));
				wait.until(ExpectedConditions.stalenessOf(findby()));
				break;
			case APPEAR:
				throw new NotImplementedException("Case Not implemented ");
			case DISAPPEAR:
				throw new NotImplementedException("Case Not implemented ");
			case APPEAR_AND_DISAPPEAR:
				wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
				wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(findBy)));
				break;
			case ENABLE:
				throw new NotImplementedException("Case Not implemented");
			case POPULATE_LIST:
				throw new NotImplementedException("Case Not implemented");
			case VISIBLE:
				throw new NotImplementedException("Case Not implemented");
			case SLEEP:
				try {
					Thread.sleep(Long.parseLong(timeOutInMilliSeconds));
				} catch (Exception e) {
				}
				break;
			}
		} catch (Exception e) {

			Assert.assertNull(vType,
					"Failed while performing ajaxwait for element-> " + name + " with WaitFor type is: " + ajaxWait, e);
		} finally {
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(timeOutInMilliSeconds), TimeUnit.MILLISECONDS);
		}
	}

	public String getSelectedValue() {
		String dropDownValue = null;
		WebElement element = findby();
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		boolean changeDropDown = false;
		if (element.getCssValue("display").equalsIgnoreCase("none")) {
			executor.executeScript("arguments[0].style.display='block';", element);
			element = findby();
			changeDropDown = true;
		}
		dropDownValue = new Select(element).getFirstSelectedOption().getText();
		if (changeDropDown)
			executor.executeScript("arguments[0].style.display='none';", element);
		Report.log(prettyName(name) + " is retrieved as " + dropDownValue, Status.DONE);

		return dropDownValue;
	}

	public String getValue() {
		String value = getAttribute("value");
		Report.log(prettyName(name) + " is retrieved as " + value, Status.DONE);
		return value;
	}

	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		TakesScreenshot ts = null;
		ts = (TakesScreenshot) getAugmentedDriver(driver);
		return ts.getScreenshotAs(arg0);
	}

	protected WebDriver getAugmentedDriver(WebDriver driver) {
		// If the driver is instance of RemoteWebDriver return augmented driver
		if (driver.getClass().equals(RemoteWebDriver.class)) {
			Augmenter augmenter = new Augmenter();
			driver = augmenter.augment(driver);
		} else {
			driver = this.driver;
		}
		return driver;
	}

	public Rectangle getRect() {
		return null;
	}

	public WrappedWebElement getWrappedWebElement() {
		return this;
	}

	/**
	 * <b>Description</b> Wrapper to Web driver Click, will perform a click on HTML
	 * element
	 */
	public void jclick() {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		try {
			executor.executeScript("arguments[0].click();", findby());
		} catch (Exception E) {
			executor.executeScript("arguments[0].click();", findby());
		}

		Report.log(prettyName(name) + " is clicked", Status.DONE);
	}
	public void jsendkeys(String Using_Id_or_Name , String Value_of_element , String Value_to_enter) {
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		try {
		executor.executeScript("document.getElementBy"+Using_Id_or_Name+"('"+Value_of_element+"').value='"+Value_to_enter+"';");
		Report.log("Element is entered", Status.DONE);
		}catch(Exception e) {
			Report.log("Element is not entered", Status.Fail);
		}
	}
}
