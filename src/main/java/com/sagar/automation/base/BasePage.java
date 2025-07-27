package com.sagar.automation.base;

import com.sagar.automation.utilities.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage { //Made  abstract because its not concrete page
    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor for BasePage
     * Initializes WebDriver  and the WebDriverWait instances
     * All page objects will inherit this constructor
     */
    public BasePage(WebDriver driver) {
        this.driver = driver; //Get a WebDriver instance from BaseTest
        //Set explicit wait time as per config properties
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("explicit.wait"))));
    }

    /**
     * Waits for element to be visible and returns it
     *
     * @param locator The  By locator of the element
     * @return The WebElement once its visible
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for elements to be clickable and returns it
     *
     * @param locator The By locator of the element
     * @return The WebElement once its clickable
     */
    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Clicks an element after waiting for it to be clickable
     *
     * @param locator The By locator of the element to click
     */
    public void clickElement(By locator) {
        waitForElementClickable(locator).click();
    }

    /**
     * Enters text into an input field after waiting for it to be visible
     * @param locator The By locator of the input field
     * @param text    The text to enter
     */
    protected void enterText(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Gets a text of an element after waiting for it to be visible
     *
     * @param locator The By locator of the element
     * @return The text of the element
     */
    public String getElementText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    /**
     * Checks if an element is displayed after waiting for it to be visible
     *
     * @param locator The By locator of the element
     * @return True if element is displayed, false otherwise
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Helper method to select an option from a dropdown by visible text.
     * @param locator The By locator of the dropdown element.
     * @param text The visible text of the option to select.
     */
    protected void selectDropdownByVisibleText(By locator, String text) {
        WebElement dropdownElement = waitForElementVisible(locator);
        Select select = new Select(dropdownElement);
        select.selectByVisibleText(text);
    }

}
