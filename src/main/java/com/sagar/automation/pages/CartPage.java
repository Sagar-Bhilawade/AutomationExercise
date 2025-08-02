package com.sagar.automation.pages;

import com.sagar.automation.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {
    private static final Logger log = LogManager.getLogger(CartPage.class.getName());

    // Locators
    private static final By CART_INFO_TABLE = By.id("cart_info_table");
    private static final By CART_PRODUCTS = By.cssSelector("#cart_info_table tbody tr");
    private static final By PROCEED_TO_CHECKOUT_BUTTON = By.cssSelector(".btn.btn-default.check_out");

    public CartPage(WebDriver driver) {
        super(driver);
        log.info("CartPage initialized");
    }

    public boolean isCartPageDisplayed() {
        log.info("Checking if cart page is displayed");
        return isElementDisplayed(CART_INFO_TABLE);
    }

    public boolean isProductInCart(String productName) {
        log.info("Checking if product is in cart: " + productName);
        List<WebElement> products = driver.findElements(CART_PRODUCTS);
        for (WebElement product : products) {
            if (product.getText().contains(productName)) {
                return true;
            }
        }
        return false;
    }

    public String getProductQuantity(String productName) {
        log.info("Getting quantity for product: " + productName);
        List<WebElement> products = driver.findElements(CART_PRODUCTS);
        for (WebElement product : products) {
            if (product.getText().contains(productName)) {
                WebElement quantityElement = product.findElement(By.cssSelector(".cart_quantity button"));
                return quantityElement.getText();
            }
        }
        return "0";
    }

    public void clickProceedToCheckout() {
        log.info("Clicking Proceed to Checkout button");
        clickElement(PROCEED_TO_CHECKOUT_BUTTON);
    }
}