package com.sagar.automation.pages;

import com.sagar.automation.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

// Removed unused import java.awt.*

public class LoginPage extends BasePage {
    //Initializes logger specifically for Login page
    private static final Logger log = LogManager.getLogger(LoginPage.class.getName());

    // Locators for elements on the Login Page
    private static final By LOGIN_EMAIL_INPUT = By.xpath("//input[@data-qa='login-email']");
    private static final By LOGIN_PASSWORD_INPUT = By.xpath("//input[@data-qa='login-password']");
    private static final By LOGIN_SUBMIT_BTN = By.xpath("//button[@data-qa='login-button']");
    private static final By LOGIN_FORM_HEADER = By.xpath("//div[@class='login-form']//h2");
    private static final By LOGIN_ERROR_MESSAGE = By.xpath("//input[@data-qa='login-password']/following::p");

    // NEW Locators for the "New User Signup!" section
    private static final By SIGNUP_FORM_HEADER = By.xpath("//div[@class='signup-form']//h2"); // "New User Signup!" header
    private static final By SIGNUP_NAME_INPUT = By.xpath("//input[@data-qa='signup-name']");
    private static final By SIGNUP_EMAIL_INPUT = By.xpath("//input[@data-qa='signup-email']");
    private static final By SIGNUP_BUTTON = By.xpath("//button[@data-qa='signup-button']");

    /**
     * Constructor for the LoginPage page Object
     * Initializes BasePage which sets up driver and wait
     */
    public LoginPage(WebDriver driver) {
        super(driver);
        log.info("Login Page object got initialized");
    }

    /**
     * Enters the email into login email field
     * @param email The email to enter
     */
    public void enterLoginEmail(String email){
        log.info("Entering login email: " + email);
        enterText(LOGIN_EMAIL_INPUT, email);
    }

    /**
     * Enters the password into the login password field.
     * @param password The password to enter.
     */
    public void enterLoginPassword(String password) {
        log.info("Entering login password."); // Avoid logging actual password
        enterText(LOGIN_PASSWORD_INPUT, password);
    }

    /**
     * Clicks the login button
     */
    public void clickLoginButton(){
        log.info("Clicking the login button");
        clickElement(LOGIN_SUBMIT_BTN);
    }

    /**
     * Performs the login operation with provided credentials
     * @param email The email for the login
     * @param password The password for the login
     */
    public void login(String email, String password){
        log.info("Attempting to login with email: " + email);
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLoginButton();
        log.info("Login Attempt completed");
    }

    /**
     * Checks if the LoginPage is displayed by verifying the presence of the Login form header
     * @return True if the LoginPage is displayed, false otherwise
     */
    public boolean isLoginPageDisplayed(){
        log.info("Checking if LoginPage is displayed. ");
        boolean isDisplayed = isElementDisplayed(LOGIN_FORM_HEADER);
        log.info("Login Page displayed status: " + isDisplayed);
        return isDisplayed;
    }

    /**
     * Retrieves the text of the login error message (if any).
     * @return The error message text, or empty string if not found.
     */
    public String getLoginErrorMessage() {
        log.info("Attempting to get login error message.");
        String errorMessage = "";
        try {
            errorMessage = getElementText(LOGIN_ERROR_MESSAGE);
            log.warn("Login error message found: " + errorMessage);
        } catch (Exception e) {
            log.info("No login error message displayed.");
        }
        return errorMessage;
    }

    // NEW methods for Signup functionality

    /**
     * Checks if the "New User Signup!" form header is visible.
     * @return True if the signup form header is visible, false otherwise.
     */
    public boolean isSignupFormDisplayed() {
        log.info("Checking if 'New User Signup!' form is displayed.");
        boolean isDisplayed = isElementDisplayed(SIGNUP_FORM_HEADER);
        log.info("'New User Signup!' form displayed status: " + isDisplayed);
        return isDisplayed;
    }

    /**
     * Enters the name into the signup name field.
     * @param name The name to enter for signup.
     */
    public void enterSignupName(String name) {
        log.info("Entering signup name: " + name);
        enterText(SIGNUP_NAME_INPUT, name);
    }

    /**
     * Enters the email into the signup email field.
     * @param email The email to enter for signup.
     */
    public void enterSignupEmail(String email) {
        log.info("Entering signup email: " + email);
        enterText(SIGNUP_EMAIL_INPUT, email);
    }

    /**
     * Clicks the 'Signup' button on the New User Signup form.
     */
    public void clickSignupButton() {
        log.info("Clicking 'Signup' button on the New User Signup form.");
        clickElement(SIGNUP_BUTTON);
    }
}
