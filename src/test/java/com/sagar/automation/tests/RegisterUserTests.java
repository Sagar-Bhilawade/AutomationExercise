package com.sagar.automation.tests; // Place in your tests package

import com.sagar.automation.base.BaseTest;
import com.sagar.automation.pages.AccountCreatedPage;
import com.sagar.automation.pages.AccountDeletedPage;
import com.sagar.automation.pages.HomePage;
import com.sagar.automation.pages.LoginPage; // The page where signup form is
import com.sagar.automation.pages.SignupPage; // The page for account information
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test; // Import TestNG annotations
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.Status;
import java.util.Map; // Import Map for test data

public class RegisterUserTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(RegisterUserTests.class.getName());

    // This test method will get its data from the generic DataProvider in BaseTest
    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class, description = "Verify User Registration and Account Deletion")
    public void RegisterUserTest(Map<String, String> testData) {
        // Extract data from the map using the parameter names defined in your Excel schema
        String name = testData.get("NAME");
        String email = testData.get("EMAIL"); // This email will be used for signup
        String password = testData.get("PASSWORD");
        String title = testData.get("TITLE"); // e.g., "Mr" or "Mrs"
        String day = testData.get("DAY");
        String month = testData.get("MONTH");
        String year = testData.get("YEAR");
        String firstName = testData.get("FIRST_NAME");
        String lastName = testData.get("LAST_NAME");
        String company = testData.get("COMPANY");
        String address1 = testData.get("ADDRESS1");
        String address2 = testData.get("ADDRESS2");
        String country = testData.get("COUNTRY");
        String state = testData.get("STATE");
        String city = testData.get("CITY");
        String zipcode = testData.get("ZIPCODE");
        String mobileNumber = testData.get("MOBILE_NUMBER");
        String expectedLoggedInUsername = testData.get("ExpectedLoggedInUsername"); // e.g., the 'name' used for signup
        String expectedAccountCreatedMsg = testData.get("ExpectedAccountCreatedMsg"); // e.g., "ACCOUNT CREATED!"
        String expectedAccountDeletedMsg = testData.get("ExpectedAccountDeletedMsg"); // e.g., "ACCOUNT DELETED!"

        log.info("Starting test: RegisterUserTest with data set for user: " + name + " (" + email + ")");
        logAndCapture(Status.INFO, "Test execution started for user registration and deletion.", "RegisterUser_Test_Start");

        // 1. Launch browser and Navigate to url 'http://automationexercise.com' (Handled by BaseTest @BeforeMethod)
        // 2. Verify that home page is visible successfully
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home Page is not visible successfully.");
        log.info("Home Page is visible successfully.");
        logAndCapture(Status.INFO, "Home Page is visible successfully.", "HomePage_Visible");

        // 3. Click on 'Signup / Login' button
        homePage.getHeader().clickLoginSignupLink();
        log.info("Clicked 'Signup / Login' button.");
        logAndCapture(Status.INFO, "Clicked 'Signup / Login' button.", "Clicked_Signup_Login");

        // 4. Verify 'New User Signup!' is visible (This is on LoginPage)
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isSignupFormDisplayed(), "New User Signup! section is not visible.");
        log.info("'New User Signup!' is visible.");
        logAndCapture(Status.INFO, "'New User Signup!' section is visible.", "NewUserSignup_Visible");

        // 5. Enter name and email address for signup
        loginPage.enterSignupName(name);
        loginPage.enterSignupEmail(email);
        log.info("Entered name and email for signup.");
        logAndCapture(Status.INFO, "Entered name and email for signup.", "Entered_Signup_Details");

        // 6. Click 'Signup' button
        loginPage.clickSignupButton();
        log.info("Clicked 'Signup' button.");
        logAndCapture(Status.INFO, "Clicked 'Signup' button.", "Clicked_Signup_Button");

        // 7. Verify that 'ENTER ACCOUNT INFORMATION' is visible
        SignupPage signupPage = new SignupPage(driver);
        Assert.assertTrue(signupPage.isEnterAccountInformationHeaderVisible(), "ENTER ACCOUNT INFORMATION header is not visible.");
        log.info("'ENTER ACCOUNT INFORMATION' is visible.");
        logAndCapture(Status.INFO, "'ENTER ACCOUNT INFORMATION' page is visible.", "EnterAccountInfo_Visible");

        // 8. Fill details: Title, Name, Email, Password, Date of birth
        signupPage.selectTitle(title);
        signupPage.enterPassword(password);
        signupPage.selectDateOfBirth(day, month, year);
        log.info("Filled account information details.");
        logAndCapture(Status.INFO, "Filled account information details.", "AccountInfo_Filled");

        // 9. Select checkbox 'Sign up for our newsletter!'
        signupPage.checkNewsletterCheckbox();
        log.info("Checked Newsletter checkbox.");
        logAndCapture(Status.INFO, "Checked Newsletter checkbox.", "Newsletter_Checked");

        // 10. Select checkbox 'Receive special offers from our partners!'
        signupPage.checkOffersCheckbox();
        log.info("Checked Offers checkbox.");
        logAndCapture(Status.INFO, "Checked Offers checkbox.", "Offers_Checked");

        // 11. Fill details: First name, Last name, Company, Address, Address2, Country, State, City, Zipcode, Mobile Number
        signupPage.fillAddressDetails(firstName, lastName, company, address1, address2, country, state, city, zipcode, mobileNumber);
        log.info("Filled address details.");
        logAndCapture(Status.INFO, "Filled address details.", "AddressDetails_Filled");

        // 12. Click 'Create Account button'
        signupPage.clickCreateAccountButton();
        log.info("Clicked 'Create Account' button.");
        logAndCapture(Status.INFO, "Clicked 'Create Account' button.", "CreateAccount_Clicked");

        // 13. Verify that 'ACCOUNT CREATED!' is visible
        AccountCreatedPage accountCreatedPage = new AccountCreatedPage(driver);
        Assert.assertTrue(accountCreatedPage.isAccountCreatedHeaderVisible(), "ACCOUNT CREATED! header is not visible.");
        Assert.assertEquals(accountCreatedPage.getElementText(By.cssSelector("h2.title.text-center b")), expectedAccountCreatedMsg, "Account Created message mismatch!");
        log.info("'ACCOUNT CREATED!' is visible.");
        logAndCapture(Status.INFO, "'ACCOUNT CREATED!' page is visible.", "AccountCreated_Visible");

        // 14. Click 'Continue' button
        accountCreatedPage.clickContinueButton();
        log.info("Clicked 'Continue' button on Account Created page.");
        logAndCapture(Status.INFO, "Clicked 'Continue' button on Account Created page.", "AccountCreated_Continue_Clicked");

        // 15. Verify that 'Logged in as username' is visible
        // After clicking continue, it should redirect back to the Home Page, but now logged in
        Assert.assertTrue(homePage.isHomePageDisplayed(), "User was not redirected to Home Page after account creation!");
        String actualLoggedInUsername = homePage.getHeader().getLoggedInUsernameText();
        Assert.assertTrue(actualLoggedInUsername.contains(expectedLoggedInUsername),
                "Logged in username text does not contain expected username: " + expectedLoggedInUsername + ". Actual: " + actualLoggedInUsername);
        log.info("'Logged in as username' is visible: " + actualLoggedInUsername);
        logAndCapture(Status.PASS, "User successfully registered and logged in as: " + actualLoggedInUsername, "LoggedIn_As_User");

        // 16. Click 'Delete Account' button (assuming it's in the header)
        homePage.getHeader().clickElement(By.cssSelector("a[href='/delete_account']")); // Assuming locator for delete account link
        log.info("Clicked 'Delete Account' button.");
        logAndCapture(Status.INFO, "Clicked 'Delete Account' button.", "DeleteAccount_Clicked");

        // 17. Verify that 'ACCOUNT DELETED!' is visible and click 'Continue' button
        AccountDeletedPage accountDeletedPage = new AccountDeletedPage(driver);
        Assert.assertTrue(accountDeletedPage.isAccountDeletedHeaderVisible(), "ACCOUNT DELETED! header is not visible.");
        Assert.assertEquals(accountDeletedPage.getElementText(By.cssSelector("h2.title.text-center b")), expectedAccountDeletedMsg, "Account Deleted message mismatch!");
        log.info("'ACCOUNT DELETED!' is visible.");
        logAndCapture(Status.PASS, "'ACCOUNT DELETED!' page is visible.", "AccountDeleted_Visible");

        accountDeletedPage.clickContinueButton();
        log.info("Clicked 'Continue' button on Account Deleted page.");
        logAndCapture(Status.INFO, "Clicked 'Continue' button on Account Deleted page.", "AccountDeleted_Continue_Clicked");

        // Final verification: User should be logged out (e.g., Signup/Login link visible)
        Assert.assertTrue(homePage.getHeader().isLoginSignupLinkDisplayed(), "Login/Signup link should be visible after account deletion!");
        log.info("User is logged out after account deletion.");
        logAndCapture(Status.PASS, "Account deletion verified. User is logged out.", "User_Logged_Out");

        log.info("Test 'RegisterUserTest' completed successfully.");
    }
}
