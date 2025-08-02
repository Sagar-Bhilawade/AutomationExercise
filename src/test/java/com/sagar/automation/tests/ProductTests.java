package com.sagar.automation.tests;

import com.sagar.automation.base.BaseTest;
import com.sagar.automation.pages.HomePage;
import com.sagar.automation.pages.ProductsPage;
import com.sagar.automation.pages.ProductDetailPage;
import com.sagar.automation.pages.CartPage;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class ProductTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(ProductTests.class.getName());

    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class, 
          description = "Verify user can view all products and navigate to product details")
    public void ViewAllProductsTest(Map<String, String> testData) {
        String expectedProductsPageTitle = testData.get("ExpectedProductsPageTitle");
        
        log.info("Starting ViewAllProductsTest");
        logAndCapture(Status.INFO, "Test execution started for viewing all products", "ViewProducts_Test_Start");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home Page is not displayed");
        logAndCapture(Status.INFO, "Home Page verified successfully", "HomePage_Verified");

        // Navigate to Products page
        homePage.getHeader().clickProductsLink();
        logAndCapture(Status.INFO, "Clicked Products link", "Products_Link_Clicked");

        ProductsPage productsPage = new ProductsPage(driver);
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page is not displayed");
        Assert.assertEquals(productsPage.getPageTitle(), expectedProductsPageTitle, "Products page title mismatch");
        logAndCapture(Status.PASS, "Products page displayed with correct title", "Products_Page_Verified");

        // Verify products list is visible
        Assert.assertTrue(productsPage.areProductsVisible(), "Products are not visible on the page");
        int productCount = productsPage.getProductCount();
        Assert.assertTrue(productCount > 0, "No products found on the page");
        logAndCapture(Status.PASS, "Found " + productCount + " products on the page", "Products_Count_Verified");

        log.info("ViewAllProductsTest completed successfully");
    }

    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class,
          description = "Verify user can search for products")
    public void SearchProductTest(Map<String, String> testData) {
        String searchKeyword = testData.get("SearchKeyword");
        String expectedResultsText = testData.get("ExpectedResultsText");

        log.info("Starting SearchProductTest with keyword: " + searchKeyword);
        logAndCapture(Status.INFO, "Test execution started for product search", "SearchProduct_Test_Start");

        HomePage homePage = new HomePage(driver);
        homePage.getHeader().clickProductsLink();
        
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.searchProduct(searchKeyword);
        logAndCapture(Status.INFO, "Searched for product: " + searchKeyword, "Product_Search_Performed");

        Assert.assertTrue(productsPage.isSearchResultsDisplayed(), "Search results are not displayed");
        Assert.assertTrue(productsPage.getSearchResultsText().contains(expectedResultsText), 
                         "Search results text doesn't match expected");
        
        int searchResultCount = productsPage.getSearchResultCount();
        Assert.assertTrue(searchResultCount > 0, "No search results found");
        logAndCapture(Status.PASS, "Search completed with " + searchResultCount + " results", "Search_Results_Verified");

        log.info("SearchProductTest completed successfully");
    }

    @Test(dataProvider = "testDataFromExcel", dataProviderClass = BaseTest.class,
          description = "Verify user can add products to cart")
    public void AddProductToCartTest(Map<String, String> testData) {
        String productIndex = testData.get("ProductIndex");
        String expectedQuantity = testData.get("ExpectedQuantity");

        log.info("Starting AddProductToCartTest");
        logAndCapture(Status.INFO, "Test execution started for adding product to cart", "AddToCart_Test_Start");

        HomePage homePage = new HomePage(driver);
        homePage.getHeader().clickProductsLink();

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.clickViewProduct(Integer.parseInt(productIndex));
        logAndCapture(Status.INFO, "Clicked on product " + productIndex, "Product_Clicked");

        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        Assert.assertTrue(productDetailPage.isProductDetailDisplayed(), "Product detail page is not displayed");
        
        String productName = productDetailPage.getProductName();
        String productPrice = productDetailPage.getProductPrice();
        logAndCapture(Status.INFO, "Product details: " + productName + " - " + productPrice, "Product_Details_Captured");

        productDetailPage.setQuantity(expectedQuantity);
        productDetailPage.clickAddToCart();
        logAndCapture(Status.INFO, "Added product to cart with quantity: " + expectedQuantity, "Product_Added_To_Cart");

        productDetailPage.clickViewCart();
        
        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page is not displayed");
        Assert.assertTrue(cartPage.isProductInCart(productName), "Product is not found in cart");
        Assert.assertEquals(cartPage.getProductQuantity(productName), expectedQuantity, "Product quantity mismatch in cart");
        logAndCapture(Status.PASS, "Product successfully added to cart and verified", "Cart_Verification_Complete");

        log.info("AddProductToCartTest completed successfully");
    }
}