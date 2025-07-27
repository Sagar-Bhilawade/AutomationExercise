package com.sagar.automation.pages; // Adjust package as needed

import com.sagar.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountCreatedPage extends BasePage {
    private static final Logger log = LogManager.getLogger(AccountCreatedPage.class.getName());

    // Locators for elements on the "ACCOUNT CREATED!" page
    private static final By ACCOUNT_CREATED_HEADER = By.cssSelector("h2.title.text-center b"); // "ACCOUNT CREATED!"
    private static final By CONTINUE_BUTTON = By.cssSelector(".btn.btn-primary"); // The 'Continue' button

    /**
     * Constructor for the AccountCreatedPage Page Object.
     * Initializes the BasePage with the WebDriver instance.
     * @param driver The Selenium WebDriver instance.
     */
    public AccountCreatedPage(WebDriver driver) {
        super(driver);
        log.info("AccountCreatedPage Page Object initialized.");
    }

    /**
     * Verifies if the "ACCOUNT CREATED!" header is visible, indicating the page is loaded.
     * @return True if the header is visible, false otherwise.
     */
    public boolean isAccountCreatedHeaderVisible() {
        log.info("Verifying 'ACCOUNT CREATED!' header visibility.");
        boolean isVisible = isElementDisplayed(ACCOUNT_CREATED_HEADER);
        log.info("'ACCOUNT CREATED!' header visibility status: " + isVisible);
        return isVisible;
    }

    /**
     * Clicks the "Continue" button on the account created page.
     * This typically redirects back to the Home Page, but now with the user logged in.
     */
    public void clickContinueButton() {
        log.info("Clicking 'Continue' button on Account Created page.");
        clickElement(CONTINUE_BUTTON);
    }
}
