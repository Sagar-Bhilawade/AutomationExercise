package com.sagar.automation.pages;

import com.sagar.automation.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Header extends BasePage { // Extends BasePage to use its driver and wait utilities
    private static final Logger log = LogManager.getLogger(Header.class.getName()); // Get the logger instance from BaseTest

    // Locators for common header elements - now private static final
    private static final By HOME_LINK = By.cssSelector("a[href='/']");
    private static final By PRODUCTS_LINK = By.cssSelector("a[href='/products']");
    private static final By CART_LINK = By.cssSelector("a[href='/view_cart']");
    private static final By LOGIN_SIGNUP_LINK = By.cssSelector("a[href='/login']");
    private static final By TEST_CASES_LINK = By.cssSelector("a[href='/test_cases']");
    private static final By API_TESTING_LINK = By.cssSelector("a[href='/api_testing']");
    private static final By CONTACT_US_LINK = By.cssSelector("a[href='/contact_us']");
    private static final By LOGGED_IN_USERNAME = By.cssSelector("li:nth-child(10) > a"); // Example for a logged-in user display

    /**
     * Constructor for the Header component.
     * It implicitly calls the BasePage constructor to initialize driver and wait.
     */
    public Header(WebDriver driver) {
        super(driver); // Call the BasePage constructor
        log.info("Header Page Object initialized.");
    }

    public void clickHomeLink() {
        log.info("Attempting to click 'Home' link.");
        clickElement(HOME_LINK);
        log.info("'Home' link clicked successfully.");
    }

    public void clickProductsLink() {
        log.info("Attempting to click 'Products' link.");
        clickElement(PRODUCTS_LINK);
        log.info("'Products' link clicked successfully.");
    }

    public void clickCartLink() {
        log.info("Attempting to click 'Cart' link.");
        clickElement(CART_LINK);
        log.info("'Cart' link clicked successfully.");
    }

    public void clickLoginSignupLink() {
        log.info("Attempting to click 'Signup / Login' link.");
        clickElement(LOGIN_SIGNUP_LINK);
        log.info("'Signup / Login' link clicked successfully.");
    }

    public void clickTestCasesLink() {
        log.info("Attempting to click 'Test Cases' link.");
        clickElement(TEST_CASES_LINK);
        log.info("'Test Cases' link clicked successfully.");
    }

    public void clickApiTestingLink() {
        log.info("Attempting to click 'API Testing' link.");
        clickElement(API_TESTING_LINK);
        log.info("'API Testing' link clicked successfully.");
    }

    public void clickContactUsLink() {
        log.info("Attempting to click 'Contact Us' link.");
        clickElement(CONTACT_US_LINK);
        log.info("'Contact Us' link clicked successfully.");
    }

    public String getLoggedInUsernameText() {
        log.info("Attempting to get logged in username text.");
        String username = getElementText(LOGGED_IN_USERNAME);
        log.info("Retrieved logged in username: " + username);
        return username;
    }

    public boolean isLoginSignupLinkDisplayed() {
        log.info("Checking if 'Login / Signup' link is displayed.");
        boolean isDisplayed = isElementDisplayed(LOGIN_SIGNUP_LINK);
        log.info("'Login / Signup' link displayed status: " + isDisplayed);
        return isDisplayed;
    }

    public boolean isLoggedInUsernameDisplayed() {
        log.info("Checking if logged in username is displayed.");
        boolean isDisplayed = isElementDisplayed(LOGGED_IN_USERNAME);
        log.info("Logged in username displayed status: " + isDisplayed);
        return isDisplayed;
    }
}
