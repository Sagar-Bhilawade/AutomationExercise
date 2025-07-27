package com.sagar.automation.pages; // Adjust package as needed

import com.sagar.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountDeletedPage extends BasePage {
    private static final Logger log = LogManager.getLogger(AccountDeletedPage.class.getName());

    // Locators for elements on the "ACCOUNT DELETED!" page
    private static final By ACCOUNT_DELETED_HEADER = By.cssSelector("h2.title.text-center b"); // "ACCOUNT DELETED!"
    private static final By CONTINUE_BUTTON = By.cssSelector(".btn.btn-primary"); // The 'Continue' button

    /**
     * Constructor for the AccountDeletedPage Page Object.
     * Initializes the BasePage with the WebDriver instance.
     * @param driver The Selenium WebDriver instance.
     */
    public AccountDeletedPage(WebDriver driver) {
        super(driver);
        log.info("AccountDeletedPage Page Object initialized.");
    }

    /**
     * Verifies if the "ACCOUNT DELETED!" header is visible, indicating the page is loaded.
     * @return True if the header is visible, false otherwise.
     */
    public boolean isAccountDeletedHeaderVisible() {
        log.info("Verifying 'ACCOUNT DELETED!' header visibility.");
        boolean isVisible = isElementDisplayed(ACCOUNT_DELETED_HEADER);
        log.info("'ACCOUNT DELETED!' header visibility status: " + isVisible);
        return isVisible;
    }

    /**
     * Clicks the "Continue" button on the account deleted page.
     * This typically redirects back to the Home Page.
     */
    public void clickContinueButton() {
        log.info("Clicking 'Continue' button on Account Deleted page.");
        clickElement(CONTINUE_BUTTON);
    }
}
