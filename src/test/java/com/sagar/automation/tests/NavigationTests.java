package com.sagar.automation.tests;

import com.sagar.automation.base.BaseTest;
import com.sagar.automation.pages.HomePage;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class NavigationTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(NavigationTests.class.getName());

    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class,
          description = "Verify all header navigation links are working")
    public void HeaderNavigationTest(Map<String, String> testData) {
        log.info("Starting HeaderNavigationTest");
        logAndCapture(Status.INFO, "Test execution started for header navigation verification", "Navigation_Test_Start");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home Page is not displayed");
        logAndCapture(Status.INFO, "Home Page verified successfully", "HomePage_Initial_Verified");

        // Test Home link
        homePage.getHeader().clickHomeLink();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home link navigation failed");
        logAndCapture(Status.PASS, "Home link navigation verified", "Home_Link_Verified");

        // Test Products link
        homePage.getHeader().clickProductsLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("/products"), "Products link navigation failed");
        logAndCapture(Status.PASS, "Products link navigation verified", "Products_Link_Verified");

        // Navigate back to home
        homePage.getHeader().clickHomeLink();

        // Test Cart link
        homePage.getHeader().clickCartLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("/view_cart"), "Cart link navigation failed");
        logAndCapture(Status.PASS, "Cart link navigation verified", "Cart_Link_Verified");

        // Navigate back to home
        homePage.getHeader().clickHomeLink();

        // Test Login/Signup link
        homePage.getHeader().clickLoginSignupLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "Login/Signup link navigation failed");
        logAndCapture(Status.PASS, "Login/Signup link navigation verified", "LoginSignup_Link_Verified");

        // Navigate back to home
        homePage.getHeader().clickHomeLink();

        // Test Contact Us link
        homePage.getHeader().clickContactUsLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("/contact_us"), "Contact Us link navigation failed");
        logAndCapture(Status.PASS, "Contact Us link navigation verified", "ContactUs_Link_Verified");

        log.info("HeaderNavigationTest completed successfully");
    }

    @Test(description = "Verify browser back and forward navigation")
    public void BrowserNavigationTest() {
        log.info("Starting BrowserNavigationTest");
        logAndCapture(Status.INFO, "Test execution started for browser navigation", "BrowserNav_Test_Start");

        HomePage homePage = new HomePage(driver);
        String homeUrl = driver.getCurrentUrl();

        // Navigate to products
        homePage.getHeader().clickProductsLink();
        String productsUrl = driver.getCurrentUrl();
        Assert.assertTrue(productsUrl.contains("/products"), "Products page navigation failed");
        logAndCapture(Status.INFO, "Navigated to Products page", "Products_Page_Navigated");

        // Navigate to login
        homePage.getHeader().clickLoginSignupLink();
        String loginUrl = driver.getCurrentUrl();
        Assert.assertTrue(loginUrl.contains("/login"), "Login page navigation failed");
        logAndCapture(Status.INFO, "Navigated to Login page", "Login_Page_Navigated");

        // Test browser back navigation
        driver.navigate().back();
        Assert.assertEquals(driver.getCurrentUrl(), productsUrl, "Browser back navigation failed");
        logAndCapture(Status.PASS, "Browser back navigation verified", "Browser_Back_Verified");

        // Test browser forward navigation
        driver.navigate().forward();
        Assert.assertEquals(driver.getCurrentUrl(), loginUrl, "Browser forward navigation failed");
        logAndCapture(Status.PASS, "Browser forward navigation verified", "Browser_Forward_Verified");

        // Test browser refresh
        driver.navigate().refresh();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "Browser refresh failed");
        logAndCapture(Status.PASS, "Browser refresh navigation verified", "Browser_Refresh_Verified");

        log.info("BrowserNavigationTest completed successfully");
    }
}