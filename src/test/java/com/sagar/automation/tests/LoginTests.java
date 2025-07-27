package com.sagar.automation.tests;

import com.sagar.automation.base.BaseTest;
import com.sagar.automation.pages.HomePage;
import com.sagar.automation.pages.LoginPage;
import com.sagar.automation.utilities.ExcelReader; // ExcelReader is still used by the DataProvider
import org.testng.Assert;
import org.testng.annotations.DataProvider; // Still needed for @DataProvider annotation
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.Status;

import java.io.IOException;
import java.lang.reflect.Method; // Still needed if test method accepts Method
import java.util.Map;

public class LoginTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(LoginTests.class.getName());


    // The @Test method now refers to the generic DataProvider in BaseTest
    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class, description = "Verify user can login with valid email and password")
    public void LoginWithValidUsernameAndPassword(Map<String, String> testData) { // TestNG will pass a Map for each data set
        // Extract data from the map using the parameter names defined in your Excel schema row
        String email = testData.get("EMAIL"); // Use "EMAIL" as per your Excel schema
        String password = testData.get("PASSWORD"); // Use "PASSWORD" as per your Excel schema
        String expectedUsername = testData.get("ExpectedUsername"); // Assuming this param exists in Excel

        log.info("Starting test: LoginTest with data set: " + testData.get("TestName")); // Log the TestName from Excel
        logAndCapture(Status.INFO, "Test execution started for valid login scenario with data: " + testData, "Test_Start");

        // 1. Initialize HomePage by passing the WebDriver instance from BaseTest
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home Page is not displayed before login attempt!");
        log.info("Home Page verified as displayed.");
        logAndCapture(Status.INFO, "Home Page is displayed successfully.", "HomePage_Displayed");

        // 2. Click on 'Signup / Login' link from the Header
        homePage.getHeader().clickLoginSignupLink();
        log.info("Navigated to Login/Signup Page.");
        logAndCapture(Status.INFO, "Clicked 'Signup / Login' link and navigated to Login/Signup Page.", "Navigated_to_LoginSignup_Page");

        // 3. Initialize LoginPage by passing the WebDriver instance
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login Page is not displayed!");
        log.info("Login Page verified as displayed.");
        logAndCapture(Status.INFO, "Login Page is displayed successfully.", "LoginPage_Displayed");

        // Basic validation for credentials (now checking map values)
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) { // Corrected: password == null check
            log.error("Email or Password missing in test data for this row.");
            logAndCapture(Status.WARNING, "Email or Password missing in test data for this row. Test cannot proceed.", "Credentials_Missing_Error");
            Assert.fail("Test cannot proceed: Email or Password missing in test data.");
        }
        log.info("Using email: " + email + " for login.");
        logAndCapture(Status.INFO, "Attempting to login with provided credentials.", "Attempting_Login");

        // 5. Perform login
        loginPage.login(email, password);
        log.info("Login action performed with provided credentials.");
        logAndCapture(Status.INFO, "Credentials entered and Login button clicked.", "After_Login_Attempt");

        // 6. Verify successful login
        Assert.assertTrue(homePage.isHomePageDisplayed(), "User was not redirected to Home Page after successful login!");
        log.info("User successfully redirected to Home Page after login.");
        logAndCapture(Status.INFO, "User successfully redirected to Home Page after login.", "Redirected_to_HomePage_After_Login");

        // Verify the logged-in username is displayed
        String actualLoggedInUsername = homePage.getHeader().getLoggedInUsernameText();
        log.info("Actual logged in username displayed: '" + actualLoggedInUsername + "'");

        String expectedUsernamePrefix = "Logged in as ";
        Assert.assertTrue(actualLoggedInUsername.startsWith(expectedUsernamePrefix),
                "Logged in username text does not start with '" + expectedUsernamePrefix + "'. Actual: " + actualLoggedInUsername);
        Assert.assertTrue(actualLoggedInUsername.contains(expectedUsername), // Assert against expected username from Excel
                "Logged in username text does not contain the expected username: " + expectedUsername + ". Actual: " + actualLoggedInUsername);

        log.info("Logged in username verified successfully.");
        logAndCapture(Status.PASS, "User successfully logged in. Logged in username verified: '" + actualLoggedInUsername + "'", "LoggedIn_Username_Verified");

        log.info("Test 'LoginTest' completed successfully for email: " + email);
    }

    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class, description = "Verifies login failure with invalid credentials and error message")
    public void LoginWithInvalidCredentials(Map<String, String> testData){
        // Extract data from the map using the parameter names defined in your Excel schema row
        String email = testData.get("EMAIL"); // Use "EMAIL" as per your Excel schema
        String password = testData.get("PASSWORD"); // Use "PASSWORD" as per your Excel schema
        String expectedErrorMessage = testData.get("ExpectedErrorMessage"); // Assuming this param exists in Excel
        log.info("Starting test: LoginTest with data set: " + testData.get("TestName"));
        logAndCapture(Status.INFO, "Test execution started for valid login scenario with data: " + testData, "Test_Start");

        //Initialize HomePage by passing the WebDriver instance from BaseTest
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home Page is not displayed before login attempt!");
        log.info("Home Page verifies as displayed.");
        logAndCapture(Status.INFO, "Home Page is displayed successfully.", "HomePage_Displayed");

        //Click on 'Signup / Login' link from the Header
        homePage.getHeader().clickLoginSignupLink();
        log.info("Navigated to Login/Signup Page");
        logAndCapture(Status.INFO, "Clicked 'Signup / Login' link and navigated to Login/Signup Page.", "Navigated_to_LoginSignup_Page");

        //Initialize LoginPage by passing the WebDriver instance
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login Page is not displayed!");
        log.info("Login Page verified as displayed.");
        logAndCapture(Status.INFO, "Login Page is displayed successfully.", "LoginPage_Displayed");

        //Basic validation for credentials(now checking map values)
        if (email == null || email.isEmpty() || password == null || password.isEmpty()){
            log.error("Email or Password is missing in the test data for this row.");
            logAndCapture(Status.WARNING, "Email or password missing in test data for this row. Test cannot proceed.", "Credentials_Missing_Error");
            Assert.fail("Test cannot proceed: Email or Password is missing in the test data. ");
        }
        log.info("Using email: " + email + "for login.");
        logAndCapture(Status.INFO, "Attempting to login with provided credentials.", "Attempting_Login");

        //Perform Login
        loginPage.login(email, password);
        log.info("Login action performed with provided credentials.");
        logAndCapture(Status.INFO, "Credentials entered and Login button clicked.", "After_Login_Attempt");

        // Verify Error Message
        Assert.assertEquals(loginPage.getLoginErrorMessage(), expectedErrorMessage);
        log.info("Expected error message verified successfully.");
        logAndCapture(Status.PASS, "Successfully verified error message.", "Error_Message_Verified");

        //Also, assert that the user is NOT logged in (e.g., login link is still displayed)
        Assert.assertTrue(homePage.getHeader().isLoginSignupLinkDisplayed(), "Login/Signup link should still be displayed after invalid login!");
        logAndCapture(Status.PASS, "User is not logged in; Login/Signup link is still displayed.", "Not_Logged_In");

        log.info("Test '{}' completed successfully.", testData.get("TestName"));



    }
}
