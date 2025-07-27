package com.sagar.automation.base;

import com.sagar.automation.utilities.ConfigReader;
import com.sagar.automation.utilities.ExcelReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.MediaEntityBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class BaseTest {
    public static WebDriver driver;
    public static Logger log = LogManager.getLogger(BaseTest.class.getName());

    public static ExtentReports extent;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private boolean takeScreenshotAfterEachStep;

    @BeforeSuite
    public void setupExtentReports() {
        log.info("Setting up ExtentReports...");
        String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        sparkReporter.config().setDocumentTitle("Automation Exercise Test Report");
        sparkReporter.config().setReportName("Functional Test Results");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Tester", "Sagar Automation Team");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        log.info("ExtentReports setup complete. Report will be generated at: " + reportPath);
    }

    @BeforeMethod
    public void setUp(Method method) {
        log.info("Starting test setup for: " + method.getName());

        ConfigReader.initializeProperties();
        this.takeScreenshotAfterEachStep = Boolean.parseBoolean(ConfigReader.getProperty("screenshot.after.each.step", "false"));

        ExtentTest test = extent.createTest(method.getName(), method.getAnnotation(Test.class).description());
        extentTest.set(test);

        // Moved this block AFTER driver initialization
        // logAndCapture(Status.INFO, "Test '" + method.getName() + "' started.", "Test_Start"); // <-- OLD POSITION

        String browserName = ConfigReader.getProperty("browser");
        String appUrl = ConfigReader.getProperty("url.automationExercise");

        if (browserName == null || browserName.isEmpty()) {
            browserName = "chrome";
            log.warn("Browser property not found or empty in config.properties. Defaulting to Chrome.");
            // No screenshot here, as driver might not be initialized yet
        }

        switch (browserName.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                log.info("Chrome browser launched.");
                break;
            case "firefox":
                driver = new FirefoxDriver();
                log.info("Firefox driver launched.");
                break;
            case "edge":
                driver = new EdgeDriver();
                log.info("Edge browser launched.");
                break;
            default:
                log.error("Invalid browser specified in config.properties: " + browserName);
                extentTest.get().log(Status.FAIL, "Invalid browser specified: " + browserName); // Log to report without screenshot
                throw new IllegalArgumentException("Invalid browser specified: " + browserName);
        }

        // --- Driver is now initialized ---

        // Now it's safe to take screenshots
        logAndCapture(Status.INFO, "Test '" + method.getName() + "' started.", "Test_Start"); // <-- NEW POSITION

        driver.manage().window().maximize();
        log.info("Browser window maximized");
        logAndCapture(Status.INFO, "Browser window maximized.", "Browser_Maximized");

        long implicitWait = Long.parseLong(ConfigReader.getProperty("implicit.wait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        log.info("Implicit wait set to " + implicitWait + " seconds.");
        logAndCapture(Status.INFO, "Implicit wait set to " + implicitWait + " seconds.", "Implicit_Wait_Set");

        long pageLoadTimeout = Long.parseLong(ConfigReader.getProperty("page.load.timeout"));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        log.info("Page load timeout set to " + pageLoadTimeout + " seconds.");
        logAndCapture(Status.INFO, "Page load timeout set to " + pageLoadTimeout + " seconds.", "Page_Load_Timeout_Set");

        driver.get(appUrl);
        log.info("Navigated to URL: " + appUrl);
        logAndCapture(Status.INFO, "Navigated to URL: <a href='" + appUrl + "'>" + appUrl + "</a>", "Navigated_to_URL");
        log.info("Test Setup completed");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentTest currentTest = extentTest.get();
            if (currentTest != null) {
                currentTest.log(Status.FAIL, "Test Failed: " + result.getName());
                currentTest.log(Status.FAIL, "Exception: " + result.getThrowable());
                String screenshotPath = takeScreenshot(result.getName() + "_FAILURE");
                if (screenshotPath != null) {
                    currentTest.fail("Screenshot on Failure", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, "Screenshot on Failure").build());
                    log.error("Screenshot captured for failed test: " + result.getName() + " at " + screenshotPath);
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentTest currentTest = extentTest.get();
            if (currentTest != null) {
                currentTest.log(Status.PASS, "Test Passed: " + result.getName());
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentTest currentTest = extentTest.get();
            if (currentTest != null) {
                currentTest.log(Status.SKIP, "Test Skipped: " + result.getName());
            }
        }

        if (driver != null) {
            log.info("Closing browser...");
            driver.quit();
            log.info("Browser closed successfully.");
        }
        extentTest.remove();
        log.info("Test tear down completed for: " + result.getName());
    }

    @AfterSuite
    public void tearDownExtentReports() {
        log.info("Flushing ExtentReports...");
        extent.flush();
        log.info("ExtentReports flushed successfully.");
    }

    /**
     * Helper method to take a screenshot.
     * @param screenshotName Name of the screenshot file.
     * @return Path to the saved screenshot, or null if failed.
     */
    protected String takeScreenshot(String screenshotName) {
        if (driver == null) {
            log.warn("Cannot take screenshot: WebDriver is null. Returning null path.");
            return null;
        }
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
            String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_.-]", "") + "_" + timestamp + ".png";
            Path destPath = Paths.get(System.getProperty("user.dir"), "reports", "screenshots", fileName);
            Files.createDirectories(destPath.getParent());
            Files.copy(srcFile.toPath(), destPath);

            String relativePath = "screenshots/" + fileName;
            log.info("Screenshot captured for step '" + screenshotName + "': " + destPath.toString());
            return relativePath;
        } catch (IOException e) {
            log.error("Failed to take screenshot for step '" + screenshotName + "': " + e.getMessage(), e); // Log exception
            return null;
        } catch (WebDriverException e) { // Catch WebDriver-specific exceptions if driver becomes invalid mid-test
            log.error("WebDriver exception while taking screenshot for step '" + screenshotName + "': " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Helper method to log a step to ExtentReports and optionally take a screenshot.
     * @param status The status of the log entry (e.g., Status.INFO, Status.PASS).
     * @param message The message to log.
     * @param screenshotName A descriptive name for the screenshot (used for filename and report).
     */
    protected void logAndCapture(Status status, String message, String screenshotName) {
        ExtentTest currentTest = extentTest.get();
        if (currentTest != null) {
            if (takeScreenshotAfterEachStep) {
                String path = takeScreenshot(screenshotName);
                if (path != null) {
                    currentTest.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(path, screenshotName).build());
                } else {
                    currentTest.log(status, message + " (Screenshot failed to attach)"); // More descriptive message
                }
            } else {
                currentTest.log(status, message);
            }
        } else {
            log.warn("ExtentTest instance is null. Cannot log to ExtentReports for message: " + message);
        }
    }

    /**
     * Helper method to get the current ExtentTest instance for logging.
     * @return The ExtentTest instance for the current thread.
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Generic DataProvider to fetch test data from Excel based on the test method's name.
     * This DataProvider can be used by any test method in classes extending BaseTest.
     *
     * @param method The TestNG test method that is requesting data.
     * @return Object[][] containing test data for the specific method, where each inner array has one Map<String, String>.
     * @throws IOException If there's an issue reading the Excel file.
     */
    @DataProvider(name = "testDataFromExcel")
    public Object[][] getTestDataFromExcel(Method method) throws IOException {
        log.info("DataProvider: Fetching data for test method: " + method.getName() + " from ExcelReader.");
        return ExcelReader.getTestData(method.getName());
    }
}
