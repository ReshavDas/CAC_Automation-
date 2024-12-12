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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import cts.qea.automation.utils.RobotHelper;
import winium.elements.desktop.MouseClickableElement;
import winium.elements.desktop.TreeView;

public class WrappedWiniumElement extends RemoteWebElement {

	final WrappedWiniumDriver driver;
	final By findBy;
	final String name;
	final RemoteWebElement relement;

	/**
	 * <b>Description</b> Initializing driver, HTML element name , By Property
	 * 
	 * @param driver WebDriver
	 * @param name   HTML element name
	 * @return By Find By Property
	 */
	WrappedWiniumElement(WrappedWiniumDriver driver, String name, By findBy) {
		this.driver = driver;
		this.name = name;
		this.findBy = findBy;
		this.relement = null;
	}

	/**
	 * <b>Description</b> Initializing driver, HTML element name , By Property
	 * 
	 * @param driver WebDriver
	 * @param name   HTML element name
	 * @return By Find By Property
	 */
	WrappedWiniumElement(WrappedWiniumDriver driver, RemoteWebElement ele, String name, By findBy) {
		this.driver = driver;
		this.name = name;
		this.findBy = findBy;
		this.relement = ele;
	}

	/**
	 * <b>Description</b> Wrapper to Web driver Click, will perform a click on HTML
	 * element
	 */
	public void click() {
		try {
			findby().click();
		} catch (Exception E) {
			findby().click();
		}
		Report.log(prettyName(name) + " is clicked", Status.DONE);
	}

	public void doubleClick() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MouseClickableElement clickableElement = new MouseClickableElement(getWebElement());
		clickableElement.doubleClick();

		Report.log(prettyName(name) + " is double clicked", Status.DONE);
	}

	public RemoteWebElement scrollToTreeItem(By byStrategy) {
		TreeView treeView = new TreeView(getWebElement());
		try {
			return treeView.scrollTo(byStrategy);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while scrolling in TreeView: " + e.getMessage());
		}
	}

	private RemoteWebElement findby() {
		if (relement == null) {
			return (RemoteWebElement) driver.findElement(findBy);
		} else {
			return (RemoteWebElement) relement.findElement(findBy);
		}
	}

	private WebDriverWait getWait() {
		return new WebDriverWait(driver, (Integer.parseInt(Config.getEnvDetails("browser", "timeoutMilli")) / 1000));
	}

	public void clear() {
//		try {
//			
		RemoteWebElement textObject = findby();
		textObject.clear();
		/*
		 * Robot rb = new Robot(); click(); rb.keyPress(KeyEvent.VK_HOME);
		 * rb.keyRelease(KeyEvent.VK_HOME);
		 * 
		 * int totalChars = textObject.getText().length();
		 * 
		 * for (int i =0; i<totalChars; i++) { rb.keyPress(KeyEvent.VK_BACK_SPACE);
		 * rb.keyRelease(KeyEvent.VK_BACK_SPACE); rb.keyPress(KeyEvent.VK_DELETE);
		 * rb.keyRelease(KeyEvent.VK_DELETE); } } catch (AWTException e) {
		 * e.printStackTrace(); }
		 */

		Report.log(prettyName(name) + " is set to BLANK", Status.DONE);
	}

	/*
	 * public void doubleClick() { // Actions builder = new Actions(driver); //
	 * builder.doubleClick().perform(); Robot robot; try { robot = new Robot(); //
	 * first click robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	 * robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // second click
	 * robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	 * robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); Report.log(prettyName(name)
	 * + " is double clicked", Status.DONE); } catch (AWTException e) {
	 * Report.log(prettyName(name) + " is double clicked", Status.FAIL);
	 * e.printStackTrace(); } }
	 */

	public void pressTab() {
		try {
			Robot rb = new Robot();
			rb.keyPress(KeyEvent.VK_TAB);
			rb.keyRelease(KeyEvent.VK_TAB);
			Report.log("Tab Key is pressed", Status.DONE);
		} catch (AWTException e) {
			Report.log("Tab Key is pressed", Status.FAIL);
			e.printStackTrace();
		}
	}

	public void pressShiftTab() {
		try {
			Robot rb = new Robot();
			rb.keyPress(KeyEvent.VK_SHIFT);
			rb.keyPress(KeyEvent.VK_TAB);
			rb.keyRelease(KeyEvent.VK_TAB);
			rb.keyRelease(KeyEvent.VK_SHIFT);
			Report.log("Shift Tab is pressed", Status.DONE);
		} catch (AWTException e) {
			Report.log("Shift Tab is pressed", Status.FAIL);
			e.printStackTrace();
		}
	}

	public void pressEnter() {
		try {
			Robot rb = new Robot();
			rb.keyPress(KeyEvent.VK_ENTER);
			rb.keyRelease(KeyEvent.VK_ENTER);
			Report.log("Enter Key is pressed", Status.DONE);
		} catch (AWTException e) {
			Report.log("Enter Key is pressed", Status.FAIL);
			e.printStackTrace();
		}
	}

	/**
	 * <b>Description</b> Wrapper to Webdriver SendKeys , which will type characters
	 * in Text box
	 * 
	 * @param keys Char Sequences to enter in HTML element Text Box
	 */
	public final void type(String keys) {

		// exit without doing anything if param is null or null string
		if (keys == null || keys.equals(""))
			return;

		// treat sendKeys as clear if keys is BLANK
		if (keys.toUpperCase().equalsIgnoreCase("BLANK")) {
			// TODO - this will not work and needs correction
			clear();
			return;
		}

		Robot robot;
		try {
			robot = new Robot();
			for (int i = 0; i < keys.length(); i++) {
				if (Character.isUpperCase(keys.charAt(i))) {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(RobotHelper.getKeyEvent(keys.charAt(i)));
					robot.keyRelease(RobotHelper.getKeyEvent(keys.charAt(i)));
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					robot.keyPress(RobotHelper.getKeyEvent(keys.charAt(i)));
					robot.keyRelease(RobotHelper.getKeyEvent(keys.charAt(i)));
				}
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}

		Report.log(prettyName(name) + " entered as " + keys, Status.DONE);

	}

	public void enterValueinDropdown(String keys) {
		click();
		pressTab();
		type(keys);
//		click();
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
		// WebElement element = findby();
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		boolean changeDropDown = false;
		if (findby().getCssValue("display").equalsIgnoreCase("none")) {
			executor.executeScript("arguments[0].style.display='block';", findby());
			changeDropDown = true;
			// executor.executeScript("document.getElementById('ptype1').style.display='block';");
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

			/*************** Temp Solution ********/
			try {
				driver.switchTo().alert().accept();
				driver.switchTo().defaultContent();
			} catch (Exception e) {
			}
			try {
				dropDownValues = new Select(findby()).getFirstSelectedOption().getText();
			} catch (Exception e) {
				dropDownValues = "";
			}
			// }
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

		try {
			getWait().until(ExpectedConditions.elementToBeClickable(findBy));
		} catch (Exception e) {
		}
		// WebElement element = findby();
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
			getWait().until(ExpectedConditions.visibilityOfElementLocated(findBy));
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

	public RemoteWebElement findElement(By by) {
		return (RemoteWebElement) findby().findElement(by);
	}

	public void switchTo(String win) {
		Object[] hand = driver.getWindowHandles().toArray();
		for (Object h : hand) {
			driver.switchTo().window(h.toString());
			if (getTitle().equals(win)) {
				return;
			}
		}
	}

	public String getTitle() {
		return driver.findElement(By.id("TitleBar")).getAttribute("Name");
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

	public String getTextValue() {
		return findby().getAttribute("Text");
	}
	
	public String getText() {
		return findby().getAttribute("Name");
	}

	public boolean isDisplayed() {
		boolean flag;
		try {
			flag = findby().isDisplayed();
		} catch (Exception e) {
			flag = false;
		}
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

	public RemoteWebElement getWebElement() {
		return (RemoteWebElement) findby();
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

	public static String prettyName(String name) {
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
		}else if (name.startsWith("wbl") && name.substring(0, 3).equalsIgnoreCase("wbl")) {
			name = name.substring(3, name.length());
		}else if (name.startsWith("cmb") && name.substring(0, 3).equalsIgnoreCase("cmb")) {
			name = name.substring(3, name.length());
		}else if (name.startsWith("menu") && name.substring(0, 4).equalsIgnoreCase("menu")) {
			name = name.substring(4, name.length());
		}

		return name;
	}

	public void mouseOver(long milliSec) {
		RemoteWebElement element = findby();
		actions().moveToElement(element).build().perform();
		try {
			Thread.sleep(milliSec);
		} catch (InterruptedException e) {
		}
	}

	public Actions actions() {
		Actions builder = new Actions((RemoteWebDriver) driver);
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
			if (StringUtils.isNotBlank((dropDownValues.get(i).getText().toString().trim()))){
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

	public String getValue() {
		String value = getAttribute("value");
		Report.log(prettyName(name) + " is retrieved as " + value, Status.DONE);
		return value;
	}

	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		// TODO: added by system for latest selenium 2.48.2 jar
		TakesScreenshot ts = null;
		ts = (TakesScreenshot) getAugmentedDriver(driver);
		return ts.getScreenshotAs(arg0);
	}

	protected WrappedWiniumDriver getAugmentedDriver(WrappedWiniumDriver driver) {
		// If the driver is instance of RemoteWebDriver return augmented driver
		if (driver.getClass().equals(RemoteWebDriver.class)) {
			Augmenter augmenter = new Augmenter();
			driver = (WrappedWiniumDriver) augmenter.augment(driver);
		} else {
			driver = this.driver;
		}
		return driver;
	}

	public Rectangle getRect() {
		return null;
	}

	public void pressKey(int key) {
		try {
			Robot rb = new Robot();
			rb.keyPress(key);
			rb.keyRelease(key);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		Report.log(KeyEvent.getKeyText(key) + " Pressed", Status.DONE);
	}

	@Override
	public void sendKeys(CharSequence... keys) {
		clear();
		click();
		findby().sendKeys(keys);

		Report.log(prettyName(name) + " entered as " + keys[0].toString(), Status.DONE);
	}
	
	public void pressShiftF8() {
		try {
			Robot rb = new Robot();
			rb.keyPress(KeyEvent.VK_SHIFT);
			rb.keyPress(KeyEvent.VK_F8);
			rb.keyRelease(KeyEvent.VK_F8);
			rb.keyRelease(KeyEvent.VK_SHIFT);
			Report.log("Shift F8 is pressed", Status.DONE);
		} catch (AWTException e) {
			Report.log("Shift F8 is pressed", Status.FAIL);
			e.printStackTrace();
		}
	}

	/**
	 * TODO This is will be removed after testing
	 * 
	 * @param keys
	 */
	public void sendKeys_clone(CharSequence... keys) {
		click();
		// exit without doing anything if param is null or null string
		if (keys[0] == null || keys[0].equals("")) {
			return;
		}

		// treat sendKeys as clear if keys is BLANK
		if (keys[0].toString().toUpperCase().equalsIgnoreCase("BLANK")) {
			// TODO - this will not work and needs correction
			clear();
			return;
		}
		Robot robot;
		try {
			robot = new Robot();
			for (int i = 0; i < keys[0].length(); i++) {
				if (Character.isUpperCase(keys[0].charAt(i)) || keys[0].charAt(i) == '_') {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(RobotHelper.getKeyEvent(keys[0].charAt(i)));
					robot.keyRelease(RobotHelper.getKeyEvent(keys[0].charAt(i)));
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					robot.keyPress(RobotHelper.getKeyEvent(keys[0].charAt(i)));
					robot.keyRelease(RobotHelper.getKeyEvent(keys[0].charAt(i)));
				}
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
		Report.log(prettyName(name) + " entered as " + keys[0].toString(), Status.DONE);
	}
	
	public void sendKeys_stash(CharSequence... keys) {
		clear();
		click();
		findby().sendKeys(keys);

		Report.log(prettyName(name) + " entered as " + keys[0].toString().replaceAll(".", "*"), Status.DONE);
	}

	public void sendKeysEdit(String string) {
		clear();
		click();
		findby().sendKeys(string);

		Report.log(prettyName(name) + " entered as " + string, Status.DONE);
		
	}

	public void selectAllText() {
		try {
			Robot rb = new Robot();
			rb.keyPress(KeyEvent.VK_END);
			rb.keyRelease(KeyEvent.VK_END);
			rb.keyPress(KeyEvent.VK_SHIFT);
			rb.keyPress(KeyEvent.VK_HOME);
			rb.keyRelease(KeyEvent.VK_HOME);
			rb.keyRelease(KeyEvent.VK_SHIFT);
		} catch (AWTException e) {
			Report.log("Select All Text Failed", Status.FAIL);
			e.printStackTrace();
		}

		Report.log("Select All Text Success", Status.DONE);
	}
}
