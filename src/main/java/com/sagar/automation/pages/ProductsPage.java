package com.sagar.automation.pages;

import com.sagar.automation.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {
    private static final Logger log = LogManager.getLogger(ProductsPage.class.getName());

    // Locators
    private static final By PAGE_TITLE = By.cssSelector(".title.text-center");
    private static final By PRODUCTS_LIST = By.cssSelector(".features_items .col-sm-4");
    private static final By SEARCH_INPUT = By.id("search_product");
    private static final By SEARCH_BUTTON = By.id("submit_search");
    private static final By SEARCH_RESULTS_TITLE = By.cssSelector(".title.text-center");
    private static final By VIEW_PRODUCT_BUTTONS = By.cssSelector("a[href*='/product_details/']");

    public ProductsPage(WebDriver driver) {
        super(driver);
        log.info("ProductsPage initialized");
    }

    public boolean isProductsPageDisplayed() {
        log.info("Checking if Products page is displayed");
        return isElementDisplayed(PAGE_TITLE);
    }

    public String getPageTitle() {
        log.info("Getting page title");
        return getElementText(PAGE_TITLE);
    }

    public boolean areProductsVisible() {
        log.info("Checking if products are visible");
        return isElementDisplayed(PRODUCTS_LIST);
    }

    public int getProductCount() {
        log.info("Getting product count");
        List<WebElement> products = driver.findElements(PRODUCTS_LIST);
        int count = products.size();
        log.info("Found " + count + " products");
        return count;
    }

    public void searchProduct(String keyword) {
        log.info("Searching for product: " + keyword);
        enterText(SEARCH_INPUT, keyword);
        clickElement(SEARCH_BUTTON);
    }

    public boolean isSearchResultsDisplayed() {
        log.info("Checking if search results are displayed");
        return isElementDisplayed(SEARCH_RESULTS_TITLE);
    }

    public String getSearchResultsText() {
        log.info("Getting search results text");
        return getElementText(SEARCH_RESULTS_TITLE);
    }

    public int getSearchResultCount() {
        log.info("Getting search result count");
        List<WebElement> results = driver.findElements(PRODUCTS_LIST);
        return results.size();
    }

    public void clickViewProduct(int index) {
        log.info("Clicking view product for index: " + index);
        List<WebElement> viewButtons = driver.findElements(VIEW_PRODUCT_BUTTONS);
        if (index < viewButtons.size()) {
            viewButtons.get(index).click();
        } else {
            throw new IndexOutOfBoundsException("Product index " + index + " not found");
        }
    }
}