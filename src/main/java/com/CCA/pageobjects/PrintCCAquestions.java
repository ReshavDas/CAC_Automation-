 package com.CCA.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cts.qea.automation.reports.Report;
import cts.qea.automation.reports.Status;

import java.time.Duration;
import java.util.List;

public class PrintCCAquestions {
    private WebDriver driver;
    private WebDriverWait wait;

    public PrintCCAquestions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5); // Reuse the same WebDriverWait instance
    }

    public void printAssessment() {
        while (true) {
            // Process all questions on the current page
            processQuestions(findQuestionContainers());

            // Check if the "Continue" button is present and clickable after answering all questions
            if (isContinueButtonPresent()) {
                clickContinueButton();
            } else {
                // Exit the loop if the "Continue" button is not found
                break;
            }
        }
    }

    private void processQuestions(List<WebElement> questionContainers) {
        for (WebElement questionContainer : questionContainers) {
            WebElement answerContainer = questionContainer.findElement(By.xpath(".//div[@class='hracolright']"));

            if (isRadioButton(answerContainer)) {
                selectFirstRadioOption(answerContainer);
            } else if (isCheckbox(answerContainer)) {
                selectFirstTwoCheckboxOptions(answerContainer);
            } else if (isTextarea(answerContainer)) {
                enterTextareaInput(answerContainer);
            } else if (isDropDownOptions(answerContainer)) {
                printDropDownOptions(answerContainer);
            } else if (isTextbox(answerContainer)) {
                printTextboxOptions(answerContainer);
            } else if (isNumericValue(answerContainer)) {
            	enterNumericValue(driver, answerContainer);
            } else if (isDateBox(answerContainer)) {
            	enterDatePicker(driver, answerContainer);
            }
        }
    }

    private List<WebElement> findQuestionContainers() {
        return driver.findElements(By.xpath("//div[starts-with(@id, 'divNq_')]"));
    }

    private boolean isRadioButton(WebElement answerContainer) {
        return !answerContainer.findElements(By.xpath(".//table[contains(@class, 'BaseFontMedium radioButtonList')]")).isEmpty();
    }

    private boolean isDropDownOptions(WebElement answerContainer) {
        return !answerContainer.findElements(By.xpath(".//select[contains(@id,'cphBody_cphContent_HRAWizard1')]")).isEmpty();
    }

    private boolean isCheckbox(WebElement answerContainer) {
        return !answerContainer.findElements(By.xpath(".//table[@class='questionTable']//input[@type='checkbox']")).isEmpty();
    }

    private boolean isTextbox(WebElement answerContainer) {
        return !answerContainer.findElements(By.xpath(".//input[contains(@id, 'ctrlQuestion_txt')]")).isEmpty();
    }

    private boolean isTextarea(WebElement answerContainer) {
        return !answerContainer.findElements(By.xpath(".//textarea[contains(@class, 'FormControl answerTextBox')]")).isEmpty();
    }
    
    private boolean isDateBox(WebElement answerContainer) {
    	return !answerContainer.findElements(By.xpath("//input[starts-with(@class, 'FormControl dateTextBox')]")).isEmpty();
    }
    
    private boolean isNumericValue(WebElement answerContainer) {
    	return !answerContainer.findElements(By.xpath("//input[starts-with(@class, 'FormControl answerTextBox')]")).isEmpty();
    }

    private void selectFirstRadioOption(WebElement answerContainer) {
        List<WebElement> radioOptions = answerContainer.findElements(By.xpath(".//input[@type='radio']"));
        if (!radioOptions.isEmpty()) {
            radioOptions.get(1).click();
            Report.log("Selected the first Radio Button", Status.Pass);
        }
    }

    private void printDropDownOptions(WebElement answerContainer) {
        WebElement matSelect = answerContainer.findElement(By.xpath(".//select[contains(@id,'cphBody_cphContent_HRAWizard1')]"));
        Select dropDown = new Select(matSelect);
        List<WebElement> options = dropDown.getOptions();
        options.get(2).click();
        Report.log("Selected the first Dropdown Button", Status.Pass);
    }

    private void selectFirstTwoCheckboxOptions(WebElement answerContainer) {
        List<WebElement> checkboxOptions = answerContainer.findElements(By.xpath(".//input[@type='checkbox']"));
        for (int i = 1; i < Math.min(3, checkboxOptions.size()); i++) {
            checkboxOptions.get(i).click();
        }
        Report.log("Selected the first two checkboxes", Status.Pass);
    }

    private void printTextboxOptions(WebElement answerContainer) {
        WebElement answerElement = answerContainer.findElement(By.xpath(".//input[contains(@id, 'ctrlQuestion_txt')]"));
        answerElement.sendKeys("Text Input");
    }

    private void enterTextareaInput(WebElement answerContainer) {
        List<WebElement> textareaElements = answerContainer.findElements(By.xpath(".//textarea[contains(@class, 'FormControl answerTextBox')]"));
        for (WebElement textareaElement : textareaElements) {
            try {
                wait.until(ExpectedConditions.visibilityOf(textareaElement));
                textareaElement.clear();
                textareaElement.sendKeys("Automated Input");
                System.out.println("Entered input in textarea: " + textareaElement.getAttribute("id"));
            } catch (Exception e) {
                System.out.println("Failed to enter input in textarea: " + textareaElement.getAttribute("id"));
                e.printStackTrace();
            }
        }
    }
    
//    private void enterDatePicker(WebElement answerContainer) {
//    	 WebElement dateInput = answerContainer.findElement(By.xpath("//input[starts-with(@class, 'FormControl dateTextBox')]"));
//         dateInput.sendKeys("12/12/2024");
//         wait.until(ExpectedConditions.attributeToBeNotEmpty(dateInput, "value")); // Wait until the value is set	
//    }

    private void enterDatePicker(WebDriver driver, WebElement answerContainer) {
        WebDriverWait wait = new WebDriverWait(driver, 5); // Use the older constructor with timeout in seconds
        
        WebElement dateInput = answerContainer.findElement(By.xpath("//input[starts-with(@class, 'FormControl dateTextBox')]"));
        // Wait until the date input is visible
        wait.until(ExpectedConditions.visibilityOf(dateInput));
        
        dateInput.sendKeys("12/12/2024");
    }
    
    private void enterNumericValue(WebDriver driver, WebElement answerContainer) {
    	WebDriverWait wait = new WebDriverWait(driver, 6); // Use the older constructor with timeout in seconds
    	
    	WebElement numberInput = answerContainer.findElement(By.xpath("//input[starts-with(@class, 'FormControl answerTextBox')]"));
    	// Wait until the numeric input is visible
    	wait.until(ExpectedConditions.visibilityOf(numberInput));
    	
    	numberInput.sendKeys("10");
    }
   

    private boolean isContinueButtonPresent() {
        try {
            driver.findElement(By.id("cphBody_cphButtonsBottom_btnContinue2"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void clickContinueButton() {
        WebElement continueButton = driver.findElement(By.id("cphBody_cphButtonsBottom_btnContinue2"));
        System.out.println("Clicking the 'Continue' button...");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cphBody_cphButtonsBottom_btnContinue2")));
        continueButton.click();
    }
}
