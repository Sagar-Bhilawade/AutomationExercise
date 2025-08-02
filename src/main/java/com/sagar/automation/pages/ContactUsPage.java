package com.sagar.automation.pages;

import com.sagar.automation.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class ContactUsPage extends BasePage {
    private static final Logger log = LogManager.getLogger(ContactUsPage.class.getName());

    // Locators
    private static final By GET_IN_TOUCH_HEADER = By.cssSelector(".contact-form h2");
    private static final By NAME_INPUT = By.cssSelector("input[data-qa='name']");
    private static final By EMAIL_INPUT = By.cssSelector("input[data-qa='email']");
    private static final By SUBJECT_INPUT = By.cssSelector("input[data-qa='subject']");
    private static final By MESSAGE_TEXTAREA = By.cssSelector("textarea[data-qa='message']");
    private static final By SUBMIT_BUTTON = By.cssSelector("input[data-qa='submit-button']");
    private static final By SUCCESS_MESSAGE = By.cssSelector(".status.alert.alert-success");
    private static final By HOME_BUTTON = By.cssSelector(".btn.btn-success");

    public ContactUsPage(WebDriver driver) {
        super(driver);
        log.info("ContactUsPage initialized");
    }

    public boolean isContactUsPageDisplayed() {
        log.info("Checking if Contact Us page is displayed");
        return isElementDisplayed(GET_IN_TOUCH_HEADER);
    }

    public void fillContactForm(String name, String email, String subject, String message) {
        log.info("Filling contact form with provided data");
        enterText(NAME_INPUT, name);
        enterText(EMAIL_INPUT, email);
        enterText(SUBJECT_INPUT, subject);
        enterText(MESSAGE_TEXTAREA, message);
    }

    public void clickSubmitButton() {
        log.info("Clicking Submit button");
        clickElement(SUBMIT_BUTTON);
    }

    public void handleAlert() {
        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            log.info("Alert present with text: " + alert.getText());
            alert.accept();
            log.info("Alert accepted");
        } catch (Exception e) {
            log.info("No alert present or alert handling failed: " + e.getMessage());
        }
    }

    public boolean isSuccessMessageDisplayed() {
        log.info("Checking if success message is displayed");
        return isElementDisplayed(SUCCESS_MESSAGE);
    }

    public String getSuccessMessage() {
        log.info("Getting success message text");
        return getElementText(SUCCESS_MESSAGE);
    }

    public void clickHomeButton() {
        log.info("Clicking Home button");
        clickElement(HOME_BUTTON);
    }
}