package com.sagar.automation.tests;

import com.sagar.automation.base.BaseTest;
import com.sagar.automation.pages.HomePage;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CrossBrowserTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(CrossBrowserTests.class.getName());

    @Test
    @Parameters({"browser"})
    public void CrossBrowserCompatibilityTest(String browserName) {
        log.info("Starting Cross Browser Compatibility Test on: " + browserName);
        logAndCapture(Status.INFO, "Test execution started on browser: " + browserName, "CrossBrowser_Test_Start");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home Page should be displayed on " + browserName);
        logAndCapture(Status.INFO, "Home Page verified on " + browserName, "HomePage_Verified_" + browserName);

        // Test basic navigation
        homePage.getHeader().clickProductsLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("/products"), "Products navigation should work on " + browserName);
        logAndCapture(Status.PASS, "Products navigation verified on " + browserName, "Products_Navigation_" + browserName);

        // Test login page
        homePage.getHeader().clickLoginSignupLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "Login navigation should work on " + browserName);
        logAndCapture(Status.PASS, "Login navigation verified on " + browserName, "Login_Navigation_" + browserName);

        log.info("Cross Browser Compatibility Test completed successfully on: " + browserName);
    }

    @Test(description = "Verify responsive design on different viewport sizes")
    public void ResponsiveDesignTest() {
        log.info("Starting Responsive Design Test");
        logAndCapture(Status.INFO, "Test execution started for responsive design", "Responsive_Test_Start");

        HomePage homePage = new HomePage(driver);
        
        // Test desktop view (default)
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed in desktop view");
        logAndCapture(Status.INFO, "Desktop view verified", "Desktop_View_Verified");

        // Test tablet view
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(768, 1024));
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed in tablet view");
        logAndCapture(Status.INFO, "Tablet view verified", "Tablet_View_Verified");

        // Test mobile view
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed in mobile view");
        logAndCapture(Status.PASS, "Mobile view verified", "Mobile_View_Verified");

        // Restore to desktop view
        driver.manage().window().maximize();

        log.info("Responsive Design Test completed successfully");
    }
}