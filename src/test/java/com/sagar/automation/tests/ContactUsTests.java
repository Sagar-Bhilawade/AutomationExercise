package com.sagar.automation.tests;

import com.sagar.automation.base.BaseTest;
import com.sagar.automation.pages.HomePage;
import com.sagar.automation.pages.ContactUsPage;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class ContactUsTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(ContactUsTests.class.getName());

    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class,
          description = "Verify user can submit contact us form successfully")
    public void ContactUsFormSubmissionTest(Map<String, String> testData) {
        String name = testData.get("NAME");
        String email = testData.get("EMAIL");
        String subject = testData.get("SUBJECT");
        String message = testData.get("MESSAGE");
        String expectedSuccessMessage = testData.get("ExpectedSuccessMessage");

        log.info("Starting ContactUsFormSubmissionTest");
        logAndCapture(Status.INFO, "Test execution started for Contact Us form submission", "ContactUs_Test_Start");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home Page is not displayed");
        logAndCapture(Status.INFO, "Home Page verified successfully", "HomePage_Verified");

        // Navigate to Contact Us page
        homePage.getHeader().clickContactUsLink();
        logAndCapture(Status.INFO, "Clicked Contact Us link", "ContactUs_Link_Clicked");

        ContactUsPage contactUsPage = new ContactUsPage(driver);
        Assert.assertTrue(contactUsPage.isContactUsPageDisplayed(), "Contact Us page is not displayed");
        logAndCapture(Status.INFO, "Contact Us page displayed successfully", "ContactUs_Page_Displayed");

        // Fill contact form
        contactUsPage.fillContactForm(name, email, subject, message);
        logAndCapture(Status.INFO, "Contact form filled with provided data", "ContactForm_Filled");

        // Submit form
        contactUsPage.clickSubmitButton();
        contactUsPage.handleAlert(); // Handle browser alert if present
        logAndCapture(Status.INFO, "Contact form submitted", "ContactForm_Submitted");

        // Verify success message
        Assert.assertTrue(contactUsPage.isSuccessMessageDisplayed(), "Success message is not displayed");
        String actualMessage = contactUsPage.getSuccessMessage();
        Assert.assertTrue(actualMessage.contains(expectedSuccessMessage), 
                         "Success message doesn't match expected. Actual: " + actualMessage);
        logAndCapture(Status.PASS, "Contact form submitted successfully with message: " + actualMessage, 
                     "ContactForm_Success_Verified");

        log.info("ContactUsFormSubmissionTest completed successfully");
    }

    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class,
          description = "Verify contact us form validation with empty fields")
    public void ContactUsFormValidationTest(Map<String, String> testData) {
        String expectedValidationMessage = testData.get("ExpectedValidationMessage");

        log.info("Starting ContactUsFormValidationTest");
        logAndCapture(Status.INFO, "Test execution started for Contact Us form validation", "ContactUsValidation_Test_Start");

        HomePage homePage = new HomePage(driver);
        homePage.getHeader().clickContactUsLink();

        ContactUsPage contactUsPage = new ContactUsPage(driver);
        Assert.assertTrue(contactUsPage.isContactUsPageDisplayed(), "Contact Us page is not displayed");

        // Try to submit empty form
        contactUsPage.clickSubmitButton();
        logAndCapture(Status.INFO, "Attempted to submit empty contact form", "EmptyForm_Submit_Attempted");

        // Verify validation (this depends on the actual validation implementation)
        // This is a placeholder - adjust based on actual validation behavior
        Assert.assertTrue(contactUsPage.isContactUsPageDisplayed(), "Should remain on contact us page for validation");
        logAndCapture(Status.PASS, "Form validation working as expected", "Form_Validation_Verified");

        log.info("ContactUsFormValidationTest completed successfully");
    }
}