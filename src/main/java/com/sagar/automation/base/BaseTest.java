package com.sagar.automation.base;

import com.sagar.automation.utilities.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {
    public static WebDriver driver; // Made public static for easy access in Page Objects
    public static Logger log; // Logger instance

    @BeforeMethod  // It will run before  each test method
    public void setUp() {
        // Initialize Logger for the BaseTest class
        log = LogManager.getLogger(BaseTest.class.getName());
        log.info("Starting test setup...");
        // Load configuration properties
        // Ensure ConfigReader.initializeProperties() is called here or higher up if not already initialized
        ConfigReader.initializeProperties(); // Call it explicitly to ensure properties are loaded
        String browserName = ConfigReader.getProperty("browser");
        String appUrl = ConfigReader.getProperty("url.automationExercise");

    // Basic validation for browser name
        if(browserName == null || browserName.isEmpty()){
            browserName = "chrome";  //Setting Default chrome browser if not mentioned any specific
            log.warn("Browser property not found or empty in config.properties. Defaulting to Chrome. ");
        }
   //     Initialize WebDriver based on browser name
        switch (browserName.toLowerCase()){
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
                log.info("Edge browser lauched.");
                break;
            default:
                log.error("Invalid browser specified in config.properties: " + browserName);
                throw new IllegalArgumentException("Invalid browser specified: " + browserName);
        }

        // 3.Configure WebDriver settings
        driver.manage().window().maximize(); // Maximize browser window
        log.info("Browser window maximized");

//        Implicit wait: Tells WebDriver to wait for a certain amount of time
//        Before throwing a NoSuchElementException if an element is not immediately found.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("implicit.wait"))));
        log.info("Implicit wait set to " + ConfigReader.getProperty("implicit.wait") + " seconds. ");

//        Page load timeout: Sets the time limit for the WebDriver to wait for a page to load.
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Long.parseLong(ConfigReader.getProperty("page.load.timeout"))));
        log.info("Page load timeout set to " + ConfigReader.getProperty("page.load.timeout") + " seconds.");

//        4. Navigate to the application URL:
        driver.get(appUrl);
        log.info("Naviageted to URL: " + appUrl);
        log.info("Test Setup completed");
    }

    /**
     *  This method runs after each TestNG @Test method
     *  It safely closes and quits the WebDriver instance
     */
    @AfterMethod
    public void tearDown(){
        if(driver != null){ //Ensure driver is not null before quitting
            log.info("Closing browser. . .");
            driver.quit(); //Closes all browser windows and kills the WebDriver session
            log.info("Browser closed successfully.");

        }
    }

}
