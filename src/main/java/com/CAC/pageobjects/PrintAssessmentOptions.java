package com.CAC.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class PrintAssessmentOptions {

    private WebDriver driver;
    private WebDriverWait wait;

    public PrintAssessmentOptions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 7); // Reduced wait time
    }

    public void fillAssessment() {
        List<WebElement> questionElements = findQuestionElements();
        for (WebElement questionElement : questionElements) {
            fillQuestionAndAnswers(questionElement);
            scrollIntoView(questionElement);
            handleNestedQuestions(questionElement);
        }
      
    }

    private List<WebElement> findQuestionElements() {
        return driver.findElements(By.xpath("//cac-question/div[@class='item']"));
    }

    private void fillQuestionAndAnswers(WebElement questionElement) {
        WebElement answerContainer = questionElement.findElement(By.className("answer"));
        fillAnswers(answerContainer);
    }

    private void fillAnswers(WebElement answerContainer) {
        if (!answerContainer.findElements(By.xpath(".//mat-radio-button")).isEmpty()) {
            selectRadioOption(answerContainer);
        } else if (!answerContainer.findElements(By.xpath(".//input[contains(@class, 'mat-mdc-input-element mat-datepicker')]")).isEmpty()) {
            enterDate(answerContainer);
        } else if (!answerContainer.findElements(By.xpath(".//mat-checkbox")).isEmpty()) {
            selectCheckboxOptions(answerContainer);
        } else if (!answerContainer.findElements(By.xpath(".//mat-form-field[contains(@class, 'dropdown')]//mat-select")).isEmpty()) {
            selectDropdownOption(answerContainer);
        } else if (!answerContainer.findElements(By.xpath(".//textarea")).isEmpty()) {
            enterTextarea(answerContainer);
        } else if (!answerContainer.findElements(By.xpath(".//input[@type='text']")).isEmpty()) {
            enterTextbox(answerContainer);
        }
    }

    private void enterDate(WebElement answerContainer) {
        WebElement dateInput = answerContainer.findElement(By.xpath(".//input[contains(@class, 'mat-mdc-input-element mat-datepicker')]"));
        dateInput.sendKeys("12/12/2024");
        wait.until(ExpectedConditions.attributeToBeNotEmpty(dateInput, "value")); // Wait until the value is set
    }

    private void selectRadioOption(WebElement answerContainer) {
        List<WebElement> radioOptions = answerContainer.findElements(By.xpath(".//mat-radio-group//mat-radio-button"));
        if (!radioOptions.isEmpty()) {
            radioOptions.get(0).click();
        }
    }

    private void selectCheckboxOptions(WebElement answerContainer) {
        List<WebElement> checkboxes = answerContainer.findElements(By.xpath(".//mat-checkbox"));
        for (int i = 0; i < Math.min(checkboxes.size(), 2); i++) {
            checkboxes.get(i).click();
        }
    }

    private void selectDropdownOption(WebElement answerContainer) {
        WebElement matSelect = answerContainer.findElement(By.xpath(".//mat-select"));
        matSelect.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='listbox']")));

        List<WebElement> dropdownOptions = driver.findElements(By.xpath("//div[@role='listbox']//mat-option//span[@class='mdc-list-item__primary-text']"));
        if (dropdownOptions.size() > 1) {
            dropdownOptions.get(1).click(); // Select the 2nd option
        } else if (!dropdownOptions.isEmpty()) {
            dropdownOptions.get(0).click(); // Select the 1st option if only one is present
        }
    }

    private void enterTextbox(WebElement answerContainer) {
        WebElement textInput = answerContainer.findElement(By.xpath(".//input[@type='text']"));
        textInput.sendKeys("Sample text");
    }

    private void enterTextarea(WebElement answerContainer) {
        WebElement textarea = answerContainer.findElement(By.xpath(".//textarea"));
        textarea.sendKeys("Sample Textarea");
    }

    private void handleNestedQuestions(WebElement rootQuestionElement) {
        try {
            WebElement nestedContainer = rootQuestionElement.findElement(By.xpath("ancestor::cac-question/following-sibling::*[1][contains(@class, 'nested') and contains(@class, 'ng-star-inserted')]"));

            List<WebElement> nestedQuestions = nestedContainer.findElements(By.xpath(".//cac-render-form[@class='ng-star-inserted']//cac-question/div[@class='item']"));

            for (WebElement nestedQuestionElement : nestedQuestions) {
                scrollIntoView(nestedQuestionElement);
                fillQuestionAndAnswers(nestedQuestionElement);
                handleNestedQuestions(nestedQuestionElement);
            }
        } catch (NoSuchElementException e) {
            System.out.println("No immediate nested questions found for the current question.");
        }
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    
}
