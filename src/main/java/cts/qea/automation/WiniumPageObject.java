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

import cts.qea.automation.Util.State;
import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.LinkedHashMap;

import static cts.qea.automation.Assert.assertTrue;

public abstract class WiniumPageObject {

	protected WrappedWiniumDriver driver;
	LinkedHashMap<String, By> elements = new LinkedHashMap<String, By>();

	public State state = null;

	/**
	 * <b>Description</b> Initializes driver and State for Application
	 * @param        driver     WebDriver 
	 */
	protected WiniumPageObject(WrappedWiniumDriver pDriver){

		driver = (WrappedWiniumDriver) pDriver;
		String sta = Config.getEnvDetails("aut", "state");
		State[] states_avlble = State.values();
		for (int i = 0; i < states_avlble.length; i++) {
			if(states_avlble[i].toString().equalsIgnoreCase(sta)){
				state = states_avlble[i];
				break;
			}
		}
	}

	/**
	 * <b>Description</b> Initializes driver and State for Application
	 *   
	 * @param        name       Name of the HTML Element 
	 * @param        property   By Property to identify HTML element 
	 */
	protected void element(String name, By property){
		elements.put(name.toLowerCase(), property);		
	}

	/**
	 * <b>Description</b> Returns WrappedWebElement for the supplied element from the OR repository. 
	 *                    NoSuchElementException exception if element is not present
	 *                    
	 * @param        name               Name of the HTML Element 
	 * @return       WrappedWebElement  WrappedWebElement web element
	 */
	public WrappedWiniumElement element(String... name) throws NoSuchElementException{

		checkElement(name[0]);
		WrappedWiniumElement ele = new WrappedWiniumElement(driver, name[0], elements.get(name[0].toLowerCase()));
		
		if (name.length > 1) {
			RemoteWebElement relement = null;
			for (int i = 0; i < name.length - 2; i++) {
				checkElement(name[i]);
				if (relement == null) {
					relement = driver.findElement(elements.get(name[i].toLowerCase()));
				} else {
					relement = (RemoteWebElement) relement.findElement(elements.get(name[i].toLowerCase()));
				}
			}
			ele = new WrappedWiniumElement(driver, relement, name[name.length - 1],
					elements.get(name[name.length - 1].toLowerCase()));
		}
		return ele;
	}
	

	private void checkElement(String name) {
		if (!elements.containsKey(name.toLowerCase())) {
			Report.log("Element <" + name + "> is NOT defined in Object Repository for Winium Page "
					+ this.getClass().getSimpleName(), Status.FAIL);
			throw new NoSuchElementException(this.getClass().getSimpleName() + "->" + name);
		}
	}

	/**
	 * <b>Description</b> Abstract method for validating Page exists
	 *                    
	 * @param        vType      Validation Type (Assert/Verify)
	 */
	public abstract void validatePageExists(ValidationType vType);

	/**
	 * <b>Description</b> Wrapper for Validating message displayed on the HTML Page
	 *                    
	 * @param        vType         Validation Type (Assert/Verify)
	 * @param        elementName   Element Name on the Page
	 * @param        message       Message to verify on the HTML Page
	 */
	public boolean validateMessage(ValidationType vType, String elementName, String message){
		return validateMessage(vType, elementName, message, true, false);
	}

	/**
	 * <b>Description</b> Validate Message on the HTML Page
	 * 
	 * @param        vType           Validation Type (Assert/Verify)
	 * @param        elementName     Element Name on the Page
	 * @param        message         Message to verify on the HTML Page
	 * @param        substrMatching  boolean to verify substring
	 * @param        caseMatching    boolean for CaseInsensitive
	 * @return       Boolean         TRUE / FALSE 
	 */
	public boolean validateMessage(ValidationType vType, String elementName, String message, boolean substrMatching, boolean caseMatching) throws NoSuchElementException{

		String text = null;
		boolean flag = false;

		try{
			text = element(elementName).getText();
		}catch (NoSuchElementException e) {
			assertTrue(vType, "Verify " + message + " is displayed "+e, false);
//			throw e;                
		}

		if(substrMatching){
			if(!caseMatching){
				flag = text.toUpperCase().contains(message.toUpperCase());
			}else{
				flag = text.contains(message);
			}
		}else{
			if(!caseMatching){
				flag = text.equalsIgnoreCase(message);
			}else{
				flag = text.equals(message);
			}
		} 
		assertTrue(vType, "Verify " + message + " is displayed", flag);
		return flag;
	}

	public LinkedHashMap<String, By> getObjectDetails(){
		return elements;
	}
}
