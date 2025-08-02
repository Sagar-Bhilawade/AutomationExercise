package com.sagar.automation.tests.api;

import com.sagar.automation.base.BaseTest;
import com.sagar.automation.utilities.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTests extends BaseTest {
    private static final Logger log = LogManager.getLogger(ApiTests.class.getName());
    private String authToken;

    @BeforeClass
    public void setupApi() {
        ConfigReader.initializeProperties();
        RestAssured.baseURI = ConfigReader.getProperty("url.dummyJson");
        log.info("API base URI set to: " + RestAssured.baseURI);
    }

    @Test(description = "Verify API authentication and token generation")
    public void ApiAuthenticationTest() {
        log.info("Starting API Authentication Test");
        
        String username = ConfigReader.getProperty("api.username");
        String password = ConfigReader.getProperty("api.password");

        Response response = given()
                .contentType("application/json")
                .body("{\n" +
                      "  \"username\": \"" + username + "\",\n" +
                      "  \"password\": \"" + password + "\"\n" +
                      "}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        authToken = response.jsonPath().getString("token");
        Assert.assertNotNull(authToken, "Authentication token should not be null");
        log.info("API Authentication successful. Token received: " + authToken.substring(0, 10) + "...");
    }

    @Test(dependsOnMethods = "ApiAuthenticationTest", description = "Verify GET all products API")
    public void GetAllProductsApiTest() {
        log.info("Starting Get All Products API Test");

        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("products", notNullValue())
                .body("products.size()", greaterThan(0))
                .body("total", greaterThan(0))
                .body("skip", equalTo(0))
                .body("limit", equalTo(30));

        log.info("Get All Products API test completed successfully");
    }

    @Test(dependsOnMethods = "ApiAuthenticationTest", description = "Verify GET single product API")
    public void GetSingleProductApiTest() {
        log.info("Starting Get Single Product API Test");
        
        int productId = 1;

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("title", notNullValue())
                .body("price", notNullValue())
                .body("category", notNullValue())
                .extract().response();

        String productTitle = response.jsonPath().getString("title");
        Double productPrice = response.jsonPath().getDouble("price");
        
        Assert.assertNotNull(productTitle, "Product title should not be null");
        Assert.assertTrue(productPrice > 0, "Product price should be greater than 0");
        
        log.info("Get Single Product API test completed successfully. Product: " + productTitle + ", Price: $" + productPrice);
    }

    @Test(dependsOnMethods = "ApiAuthenticationTest", description = "Verify POST create product API")
    public void CreateProductApiTest() {
        log.info("Starting Create Product API Test");

        String newProductJson = "{\n" +
                "  \"title\": \"Test Product\",\n" +
                "  \"description\": \"Test product description\",\n" +
                "  \"price\": 99.99,\n" +
                "  \"discountPercentage\": 10.5,\n" +
                "  \"rating\": 4.5,\n" +
                "  \"stock\": 50,\n" +
                "  \"brand\": \"Test Brand\",\n" +
                "  \"category\": \"electronics\"\n" +
                "}";

        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(newProductJson)
                .when()
                .post("/products/add")
                .then()
                .statusCode(200)
                .body("title", equalTo("Test Product"))
                .body("price", equalTo(99.99f))
                .body("id", notNullValue())
                .extract().response();

        Integer createdProductId = response.jsonPath().getInt("id");
        Assert.assertNotNull(createdProductId, "Created product ID should not be null");
        
        log.info("Create Product API test completed successfully. Created product ID: " + createdProductId);
    }

    @Test(dependsOnMethods = "ApiAuthenticationTest", description = "Verify PUT update product API")
    public void UpdateProductApiTest() {
        log.info("Starting Update Product API Test");
        
        int productId = 1;
        String updatedProductJson = "{\n" +
                "  \"title\": \"Updated Test Product\",\n" +
                "  \"price\": 199.99\n" +
                "}";

        given()
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(updatedProductJson)
                .when()
                .put("/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("title", equalTo("Updated Test Product"))
                .body("price", equalTo(199.99f));

        log.info("Update Product API test completed successfully");
    }

    @Test(dependsOnMethods = "ApiAuthenticationTest", description = "Verify DELETE product API")
    public void DeleteProductApiTest() {
        log.info("Starting Delete Product API Test");
        
        int productId = 1;

        given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("isDeleted", equalTo(true))
                .body("deletedOn", notNullValue());

        log.info("Delete Product API test completed successfully");
    }

    @Test(description = "Verify API error handling for invalid endpoints")
    public void ApiErrorHandlingTest() {
        log.info("Starting API Error Handling Test");

        // Test 404 for non-existent endpoint
        given()
                .when()
                .get("/nonexistent")
                .then()
                .statusCode(404);

        // Test 404 for non-existent product
        given()
                .when()
                .get("/products/99999")
                .then()
                .statusCode(404);

        log.info("API Error Handling test completed successfully");
    }
}