package com.example.inventoryservice.controllers;

import com.example.config.TestsConfig;
import com.example.inventoryservice.config.ContextHolder;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = TestsConfig.class)
@AutoConfigureObservability
@ActiveProfiles("test")
@Testcontainers
@Sql(scripts = "/setup.sql",
     executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/teardown.sql",
     executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductControllerTest {

    private static final String API_V_1_PRODUCTS = "/api/v1/products";
    @Autowired
    private RequestSpecification requestSpec;

    @MockBean
    private ContextHolder contextHolder;

    @BeforeEach
    void setUp() {
        Mockito.when(this.contextHolder.isAuthenticated()).thenReturn(true);
        Mockito.when(this.contextHolder.getUserId()).thenReturn(1L);
        Mockito.when(this.contextHolder.getUsername()).thenReturn("JhonDoe");
    }

    @Test
    void getAll_When_AllProductsAreRequested_ShouldReturnListOf15Products() {
        given()
            .spec(this.requestSpec)
        .when()
            .get(API_V_1_PRODUCTS)
        .then()
            .log().ifValidationFails()
            .statusCode(200)
            .body("size()", is(15));
    }

    @Test
    void getOneById_WhenProductExist_ShouldReturnsProduct() {
        given()
            .spec(this.requestSpec)
        .when()
            .get(API_V_1_PRODUCTS + "/1")
        .then()
            .statusCode(200)
            .body("id", is(1))
            .body("name", equalTo("Apple iPhone 13 Pro"))
            .body("price", is(999.00f));
    }

    @Test
    void addOne_WhenProductDoesNotExist_ShouldSave() {
        var createProduct = ProductCreateDto.builder()
                                            .name("Apple iPhone 14 Pro")
                                            .price(BigDecimal.valueOf(999.00f))
                                            .description("The latest iPhone")
                                            .quantity(10)
                                            .build();

        given()
            .spec(this.requestSpec)
            .body(createProduct)
        .when()
            .post(API_V_1_PRODUCTS + "/add")
        .then()
            .log().ifValidationFails()
            .statusCode(201);
    }

    @Test
    void decreaseStock_WhenAllGivenProductsExist_ShouldDecreaseTheQuantities() {

        var prodQuantity1 = ProductStockQuantityDto.builder()
                                                   .productId(1L)
                                                   .quantity(1)
                                                   .build();
        var productQuantities = List.of(prodQuantity1);

        given()
            .spec(this.requestSpec)
            .body(productQuantities)
        .when()
            .put(API_V_1_PRODUCTS + "/decrease-stock")
        .then()
            .log().ifValidationFails()
            .statusCode(204);
    }


    @Test
    void increaseStock_WhenAllProductsExist_ShouldIncreaseTheQuantities() {

        var prodQuantity1 = ProductStockQuantityDto.builder()
                                                   .productId(1L)
                                                   .quantity(1)
                                                   .build();
        var productQuantities = List.of(prodQuantity1);

        given()
            .spec(this.requestSpec)
            .body(productQuantities)
        .when()
            .put(API_V_1_PRODUCTS + "/increase-stock")
        .then()
            .log().ifValidationFails()
            .statusCode(204);
    }
}