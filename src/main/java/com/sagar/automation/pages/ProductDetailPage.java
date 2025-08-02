package com.sagar.automation.pages;

import com.sagar.automation.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailPage extends BasePage {
    private static final Logger log = LogManager.getLogger(ProductDetailPage.class.getName());

    // Locators
    private static final By PRODUCT_NAME = By.cssSelector(".product-information h2");
    private static final By PRODUCT_PRICE = By.cssSelector(".product-information span span");
    private static final By QUANTITY_INPUT = By.id("quantity");
    private static final By ADD_TO_CART_BUTTON = By.cssSelector(".btn.btn-default.cart");
    private static final By VIEW_CART_BUTTON = By.cssSelector("a[href='/view_cart'] u");
    private static final By PRODUCT_DETAILS_SECTION = By.cssSelector(".product-details");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        log.info("ProductDetailPage initialized");
    }

    public boolean isProductDetailDisplayed() {
        log.info("Checking if product detail page is displayed");
        return isElementDisplayed(PRODUCT_DETAILS_SECTION);
    }

    public String getProductName() {
        log.info("Getting product name");
        return getElementText(PRODUCT_NAME);
    }

    public String getProductPrice() {
        log.info("Getting product price");
        return getElementText(PRODUCT_PRICE);
    }

    public void setQuantity(String quantity) {
        log.info("Setting quantity to: " + quantity);
        enterText(QUANTITY_INPUT, quantity);
    }

    public void clickAddToCart() {
        log.info("Clicking Add to Cart button");
        clickElement(ADD_TO_CART_BUTTON);
    }

    public void clickViewCart() {
        log.info("Clicking View Cart button");
        clickElement(VIEW_CART_BUTTON);
    }
}