package com.sagar.automation.pages; // Adjust package as needed

import com.sagar.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select; // Import Select for dropdowns
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SignupPage extends BasePage {
    private static final Logger log = LogManager.getLogger(SignupPage.class.getName());

    // Locators for elements on the "ENTER ACCOUNT INFORMATION" page
    private static final By ENTER_ACCOUNT_INFO_HEADER = By.cssSelector(".login-form h2 b"); // "ENTER ACCOUNT INFORMATION"

    // Title radio buttons
    private static final By MR_TITLE_RADIO = By.id("id_gender1");
    private static final By MRS_TITLE_RADIO = By.id("id_gender2");

    // Account Information fields
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By DAYS_DROPDOWN = By.id("days");
    private static final By MONTHS_DROPDOWN = By.id("months");
    private static final By YEARS_DROPDOWN = By.id("years");
    private static final By NEWSLETTER_CHECKBOX = By.id("newsletter");
    private static final By OFFERS_CHECKBOX = By.id("optin");

    // Address Information fields
    private static final By FIRST_NAME_INPUT = By.id("first_name");
    private static final By LAST_NAME_INPUT = By.id("last_name");
    private static final By COMPANY_INPUT = By.id("company");
    private static final By ADDRESS1_INPUT = By.id("address1");
    private static final By ADDRESS2_INPUT = By.id("address2");
    private static final By COUNTRY_DROPDOWN = By.id("country");
    private static final By STATE_INPUT = By.id("state");
    private static final By CITY_INPUT = By.id("city");
    private static final By ZIPCODE_INPUT = By.id("zipcode");
    private static final By MOBILE_NUMBER_INPUT = By.id("mobile_number");

    // Create Account button
    private static final By CREATE_ACCOUNT_BUTTON = By.cssSelector("button[data-qa='create-account']");

    /**
     * Constructor for the SignupPage Page Object.
     * Initializes the BasePage with the WebDriver instance.
     * @param driver The Selenium WebDriver instance.
     */
    public SignupPage(WebDriver driver) {
        super(driver);
        log.info("SignupPage Page Object initialized.");
    }

    /**
     * Verifies if the "ENTER ACCOUNT INFORMATION" header is visible, indicating the page is loaded.
     * @return True if the header is visible, false otherwise.
     */
    public boolean isEnterAccountInformationHeaderVisible() {
        log.info("Verifying 'ENTER ACCOUNT INFORMATION' header visibility.");
        boolean isVisible = isElementDisplayed(ENTER_ACCOUNT_INFO_HEADER);
        log.info("'ENTER ACCOUNT INFORMATION' header visibility status: " + isVisible);
        return isVisible;
    }

    /**
     * Selects the title (Mr. or Mrs.).
     * @param title "Mr" or "Mrs".
     */
    public void selectTitle(String title) {
        log.info("Selecting title: " + title);
        if ("Mr".equalsIgnoreCase(title)) {
            clickElement(MR_TITLE_RADIO);
        } else if ("Mrs".equalsIgnoreCase(title)) {
            clickElement(MRS_TITLE_RADIO);
        } else {
            log.warn("Invalid title specified: " + title + ". No title selected.");
        }
    }

    /**
     * Enters the account password.
     * @param password The password to enter.
     */
    public void enterPassword(String password) {
        log.info("Entering password."); // Avoid logging actual password
        enterText(PASSWORD_INPUT, password);
    }

    /**
     * Selects the date of birth from dropdowns.
     * @param day Day (e.g., "1", "15").
     * @param month Month (e.g., "January", "5").
     * @param year Year (e.g., "1990", "2000").
     */
    public void selectDateOfBirth(String day, String month, String year) {
        log.info("Selecting Date of Birth: {}/{}/{}", day, month, year);
        selectDropdownByVisibleText(DAYS_DROPDOWN, day);
        selectDropdownByVisibleText(MONTHS_DROPDOWN, month);
        selectDropdownByVisibleText(YEARS_DROPDOWN, year);
    }

    /**
     * Checks the "Sign up for our newsletter!" checkbox.
     */
    public void checkNewsletterCheckbox() {
        log.info("Checking 'Sign up for our newsletter!' checkbox.");
        if (!waitForElementVisible(NEWSLETTER_CHECKBOX).isSelected()) {
            clickElement(NEWSLETTER_CHECKBOX);
        }
    }

    /**
     * Checks the "Receive special offers from our partners!" checkbox.
     */
    public void checkOffersCheckbox() {
        log.info("Checking 'Receive special offers from our partners!' checkbox.");
        if (!waitForElementVisible(OFFERS_CHECKBOX).isSelected()) {
            clickElement(OFFERS_CHECKBOX);
        }
    }

    /**
     * Fills in the address information details.
     * @param firstName User's first name.
     * @param lastName User's last name.
     * @param company Company name.
     * @param address1 First line of address.
     * @param address2 Second line of address (optional).
     * @param country Country.
     * @param state State.
     * @param city City.
     * @param zipcode Zipcode.
     * @param mobileNumber Mobile number.
     */
    public void fillAddressDetails(String firstName, String lastName, String company,
                                   String address1, String address2, String country,
                                   String state, String city, String zipcode, String mobileNumber) {
        log.info("Filling address details for: {} {}", firstName, lastName);
        enterText(FIRST_NAME_INPUT, firstName);
        enterText(LAST_NAME_INPUT, lastName);
        enterText(COMPANY_INPUT, company);
        enterText(ADDRESS1_INPUT, address1);
        if (address2 != null && !address2.isEmpty()) {
            enterText(ADDRESS2_INPUT, address2);
        }
        selectDropdownByVisibleText(COUNTRY_DROPDOWN, country);
        enterText(STATE_INPUT, state);
        enterText(CITY_INPUT, city);
        enterText(ZIPCODE_INPUT, zipcode);
        enterText(MOBILE_NUMBER_INPUT, mobileNumber);
        log.info("Address details filled.");
    }

    /**
     * Clicks the "Create Account" button.
     */
    public void clickCreateAccountButton() {
        log.info("Clicking 'Create Account' button.");
        clickElement(CREATE_ACCOUNT_BUTTON);
    }
}
