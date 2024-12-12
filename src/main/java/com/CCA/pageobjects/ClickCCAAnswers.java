package com.CCA.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

import java.util.List;


public class ClickCCAAnswers {
	private WebDriver driver;
	private WebDriverWait wait;

	public ClickCCAAnswers(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 7);
	}

	public void printAssessment() throws InterruptedException {
		while (true) {
			List<WebElement> questionContainers = driver.findElements(By.xpath("//div[starts-with(@id, 'divNq_')]"));
			for (WebElement questionContainer : questionContainers) {
				processQuestionContainer(questionContainer);
			}
			if (isContinueButtonPresent()) {
				WebElement continueButton = driver.findElement(By.id("cphBody_cphButtonsBottom_btnContinue2"));
				System.out.println("Clicking the 'Continue' button...");
				continueButton.click();

			} else {
				break;
			}
		}
	}

	private void processQuestionContainer(WebElement questionContainer) {
		WebElement answerContainer = questionContainer.findElement(By.xpath(".//div[@class='hracolright']"));

		if (selectRadioButton(answerContainer)) return;
		if (selectCheckboxes(answerContainer)) return;
		if (enterTextareaInput(answerContainer)) return;
		if (enterNumericValue(answerContainer)) return;
		if (enterDatePicker(answerContainer))return;
		if (selectDropdownOption(driver, answerContainer)) return;
		enterTextInput(answerContainer);

		List<WebElement> nestedQuestions = questionContainer.findElements(By.xpath(".//div[contains(@class, 'NestedQuestions')]//div[starts-with(@id, 'divNq_')]"));
		for (WebElement nestedQuestion : nestedQuestions) {
			processQuestionContainer(nestedQuestion);
		}
	}
	
//	private void processQuestionContainer(WebDriver driver, WebElement questionContainer) {
//	    WebDriverWait wait = new WebDriverWait(driver, 10); // 10 seconds wait time
//	    WebElement answerContainer = wait.until(ExpectedConditions.visibilityOf(
//	        questionContainer.findElement(By.xpath(".//div[@class='hracolright']"))));
//	    if (selectRadioButton(answerContainer)) return;
//	    if (selectCheckboxes(answerContainer)) return;
//	    if (enterTextareaInput(answerContainer)) return;
//	    if (enterNumericValue(answerContainer)) return;
//	    if (enterDatePicker(answerContainer)) return;
//	    if (selectDropdownOption(driver, answerContainer)) return;
//	    enterTextInput(answerContainer);
//
//	    List<WebElement> nestedQuestions = questionContainer.findElements(By.xpath(".//div[contains(@class, 'NestedQuestions')]//div[starts-with(@id, 'divNq_')]"));
//	    for (WebElement nestedQuestion : nestedQuestions) {
//	        processQuestionContainer(driver, nestedQuestion);
//	    }
//	}

	private boolean selectRadioButton(WebElement answerContainer) {
		List<WebElement> radioOptions = answerContainer.findElements(By.xpath(".//input[@type='radio']"));
		if (!radioOptions.isEmpty()) {
			WebElement firstRadioButton=radioOptions.get(0);
			JavascriptExecutor jsExecutor=(JavascriptExecutor)driver;
			jsExecutor.executeScript("arguments[0].click();",firstRadioButton);
			Report.log("Selected the first Radio Button", Status.Pass);
		//	radioOptions.get(0).click();
			return true;
		}
		return false;
	}

	private boolean selectCheckboxes(WebElement answerContainer) {
		List<WebElement> checkboxOptions = answerContainer.findElements(By.xpath(".//input[@type='checkbox']"));
		if (!checkboxOptions.isEmpty()) {
			for (int i = 1; i < Math.min(3, checkboxOptions.size()); i++) {
				checkboxOptions.get(i).click();
			}
			Report.log("Selected the first two checkboxes", Status.Pass);
			return true;
		}
		return false;
	}

	private boolean enterTextareaInput(WebElement answerContainer) {
        List<WebElement> textareaElements = answerContainer.findElements(By.xpath(".//textarea[contains(@class, 'FormControl answerTextBox')]"));
        if (!textareaElements.isEmpty()) {
            for (WebElement textareaElement : textareaElements) {
                try {
                    if (wait.until(ExpectedConditions.visibilityOf(textareaElement)) != null) {
                    textareaElement.clear();
                    textareaElement.sendKeys("Automated Input");
                    System.out.println("Entered input in textarea: " + textareaElement.getAttribute("id"));
                    }
                }catch (TimeoutException e) {
                    System.out.println("Failed to enter input in textarea: " + textareaElement.getAttribute("id"));
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

//	private boolean selectDropdownOption(WebElement answerContainer) { 
//		WebElement matSelect = answerContainer.findElement(By.xpath("//select[starts-with(@class, 'FormControl dropDown')]"));
//		if (matSelect != null) {
//			Select dropDown = new Select(matSelect);
//			List<WebElement> options = dropDown.getOptions();
//			options.get(2).click();
//			Report.log("Selected the first Dropdown Button", Status.Pass);
//			return true;
//		}
//		return false;
//	}

	 private boolean selectDropdownOption(WebDriver driver, WebElement answerContainer) {
	      WebDriverWait wait = new WebDriverWait(driver, 20); // 20 seconds wait time
	      WebElement matSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[starts-with(@class, 'FormControl dropDown')]")));
	      Report.log("Dropdown is visible", Status.Pass);
	        if (matSelect != null && matSelect.isEnabled()) {
	            wait.until(ExpectedConditions.elementToBeClickable(matSelect));
	            Select dropDown = new Select(matSelect);
	            List<WebElement> options = dropDown.getOptions();
	            options.get(2).click();
	            Report.log("Selected the first Dropdown Button", Status.Pass);
	            return true;
	        }
	        return false;
	    }
	
	private void enterTextInput(WebElement answerContainer) {
		WebElement answerElement = answerContainer.findElement(By.xpath(".//input[contains(@id, 'ctrlQuestion_txt')]"));
		if (answerElement != null) {
			answerElement.sendKeys("Text Input");
		}
	}

	    private boolean enterNumericValue(WebElement answerContainer) {
	    	List<WebElement> numericvalueElements = answerContainer.findElements(By.xpath("//input[starts-with(@class, 'FormControl answerTextBox')]"));
	    	if (!numericvalueElements.isEmpty()) {
	    		for(WebElement numericvalueElement : numericvalueElements) {
	    			try {
	    				 wait.until(ExpectedConditions.elementToBeClickable(numericvalueElement));
	    				 numericvalueElement.clear();
	    				 numericvalueElement.sendKeys("15");
	    				 System.out.println("Entered number is: " + numericvalueElement.getAttribute("id"));
	    				}catch (Exception e) {
	    					System.out.println("Failed to enter number in textbox: " + numericvalueElement.getAttribute("id"));
	    					e.printStackTrace();
	    				}
	    		 	}
	    			return true;
	    		}
	    		return false;
	    }

	    private boolean enterDatePicker(WebElement answerContainer) {
//	    	List<WebElement> dateinputBoxes = answerContainer.findElements(By.xpath("//input[starts-with(@class, 'FormControl dateTextBox')]"));
	    	List<WebElement> dateinputBoxes = answerContainer.findElements(By.cssSelector("input[type='text'][maxlength='10'][size='10'][class*='FormControl']"));
	    	if (!dateinputBoxes.isEmpty()) {
	    		for(WebElement dateinputBox : dateinputBoxes) {
	    			try {
	    				 wait.until(ExpectedConditions.elementToBeClickable(dateinputBox));
	    				 dateinputBox.clear();
	    				 dateinputBox.sendKeys("12/12/2024");
	    				 System.out.println("Entered date is: " + dateinputBox.getAttribute("id"));
	    			} catch (Exception e) {
	    				System.out.println("Failed to enter the date in datepicker box: " + dateinputBox.getAttribute("id"));
	    				e.printStackTrace();
	    			}
	    		}
	    		return true;
	        }
	        return false; // Indicate failure
	     }

	private boolean isContinueButtonPresent() {
		try {
			driver.findElement(By.id("cphBody_cphButtonsBottom_btnContinue2"));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}