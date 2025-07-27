package com.sagar.automation.pages;

import com.sagar.automation.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

//Homepage class represents the main loading page of the Automation Exercise website
public class HomePage extends BasePage { // Now extends BasePage
    private static final Logger log = LogManager.getLogger(HomePage.class.getName()); // Get the logger instance from BaseTest
    private Header header; // Composition: HomePage has a Header

    // Locators specific to the Home Page - now private static final
    private static final By HOME_PAGE_SLIDER = By.id("slider");
    private static final By FEATURED_ITEMS_TITLE = By.cssSelector(".features_items .title.text-center");

    private static final By FIRST_FEATURED_VIEW_PRODUCT_BTN = By.xpath("//a[@href='/product_details/1']");

    /**
     * Constructor for the HomePage Page Object.
     * Initializes the BasePage (which sets up driver and wait) and the Header component.
     */
    public HomePage(WebDriver driver) {
        super(driver); // Call the BasePage constructor
        this.header = new Header(driver); // Initialize the Header component
        log.info("HomePage Page Object initialized.");
    }

    /**
     * Returns the Header component instance, allowing access to common navigation methods.
     * @return The Header Page Object instance.
     */
    public Header getHeader() {
        log.info("Providing Header component instance.");
        return header;
    }

    /**
     * Checks if the home page is currently displayed by verifying the presence of key elements.
     * This method combines checks for specific home page elements and common header elements.
     * @return True if the home page is displayed, False otherwise.
     */
    public boolean isHomePageDisplayed() {
        log.info("Verifying if Home Page is displayed.");
        boolean sliderDisplayed = isElementDisplayed(HOME_PAGE_SLIDER);
        boolean featuredItemsDisplayed = isElementDisplayed(FEATURED_ITEMS_TITLE);
        // Assuming the home link in the header is a good indicator for the home page being loaded
        boolean homeLinkInHeaderDisplayed = header.isElementDisplayed(By.cssSelector("a[href='/']"));

        if (sliderDisplayed && featuredItemsDisplayed && homeLinkInHeaderDisplayed) {
            log.info("Home page is displayed (slider, featured items, and home link found).");
            return true;
        } else {
            log.warn("Home page is NOT fully displayed. " +
                    "Slider: " + sliderDisplayed + ", " +
                    "Featured Items: " + featuredItemsDisplayed + ", " +
                    "Home Link in Header: " + homeLinkInHeaderDisplayed);
            return false;
        }
    }

    // Add more methods specific to the Home Page here
    // For example, interacting with the slider, verifying product categories, etc.
    public String getFeaturedItemsTitle() {
        log.info("Attempting to get text of Featured Items title.");
        String title = getElementText(FEATURED_ITEMS_TITLE);
        log.info("Retrieved Featured Items title: " + title);
        return title;
    }

    /** Example of a method that might be specific to the home page,
    *  e.g., clicking on a specific product from the featured section
     */
    public void clickFirstFeaturedProduct() {
        log.info("Attempting to click on the view product of the first featured product.");
        clickElement(FIRST_FEATURED_VIEW_PRODUCT_BTN);
        log.info("First featured product clicked successfully.");
    }
}
