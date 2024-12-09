package akros.vendingmachine.controller;

import akros.vendingmachine.AbstractProductIT;
import akros.vendingmachine.dto.ProductDTO;
import akros.vendingmachine.dto.ProductResponseDto;
import akros.vendingmachine.repository.ProductRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;

import static akros.vendingmachine.AppConstant.PRODUCT_API_PATH;
import static akros.vendingmachine.AppConstant.PRODUCT_KEY;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.OK;

class ProductControllerIT extends AbstractProductIT {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void should_add_product() {
        //Given
        String AUTH_API_PATH = "/api/v1/products";
        ProductDTO productDTO = ProductDTO.builder()
                .productName("Cola")
                .productPrice(350)
                .build();

        //When
        ProductResponseDto responseDto = restTemplate.exchange(AUTH_API_PATH , POST, new HttpEntity<>(productDTO), ProductResponseDto.class).getBody();

        //Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getStatus()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void should_get_all_products() {
        //Given
        ProductDTO productDTO1 = ProductDTO.builder()
                .productName("Cola")
                .productPrice(350)
                .build();

        ProductDTO productDTO2 = ProductDTO.builder()
                .productName("Fanta")
                .productPrice(350)
                .build();

        //When
       addProducts(productDTO1, productDTO2);

        //Then
        given()
                .contentType(JSON)
                .when()
                .get("/api/v1/products")
                .then()
                .statusCode(OK.value())
                .body(".", hasSize(4));
    }

    @Test
    void should_get_product_by_id() {
        //Given
        ProductDTO productDTO1 = ProductDTO.builder()
                .productName("Cola")
                .productPrice(350)
                .build();

        //When
        addProduct(productDTO1);

        //Then
        given()
                .contentType(JSON)
                .when()
                .get(PRODUCT_API_PATH + "1")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void should_not_get_product_when_id_not_exists() {
        //Given
        String AUTH_API_PATH = "/api/v1/products";
        int nonExistingId = Integer.MAX_VALUE;
        ProductDTO productDTO = ProductDTO.builder()
                .productName("Cola")
                .productPrice(350)
                .build();

        //When
        ProductResponseDto responseDto = restTemplate.exchange(AUTH_API_PATH , POST, new HttpEntity<>(productDTO), ProductResponseDto.class).getBody();
        var responseEntity = restTemplate.getForEntity(PRODUCT_API_PATH + nonExistingId, ProductResponseDto.class);

        //Then
        assertThat(responseDto).isNotNull();
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void should_delete_product_by_id() {
        //Given
        String AUTH_API_PATH = "/api/v1/products";
        ProductDTO productDTO = ProductDTO.builder()
                .productName("Cola")
                .productPrice(350)
                .build();

        //When
        ProductResponseDto responseDto = restTemplate.exchange(AUTH_API_PATH , POST, new HttpEntity<>(productDTO), ProductResponseDto.class).getBody();

        //Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getData()).isNotNull();
        assertThat(responseDto.getData().get(PRODUCT_KEY)).isNotNull();
        String entityUrl = PRODUCT_API_PATH + "/" + 1;
        restTemplate.delete(entityUrl);

    }

    @Test
    void should_update_product_by_id() {
        //Given
        String updatedName = "Fanta";
        String AUTH_API_PATH = "/api/v1/products";
        ProductDTO productDTO = ProductDTO.builder()
                .productName("Cola")
                .productPrice(350)
                .build();

        //When
        restTemplate.exchange(AUTH_API_PATH , POST, new HttpEntity<>(productDTO), ProductResponseDto.class).getBody();

        productDTO.setProductName(updatedName);
        ProductResponseDto responseDto = restTemplate.exchange(AUTH_API_PATH+ "/" + 1, PUT, new HttpEntity<>(productDTO), ProductResponseDto.class).getBody();

        //Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getData()).isNotNull();
        assertThat(responseDto.getData().get(PRODUCT_KEY)).isNotNull();
        var data = (LinkedHashMap) responseDto.getData().get(PRODUCT_KEY);
        assertThat(data.get("productName")).isEqualTo(updatedName);

    }

    private void addProducts(  ProductDTO productDTO1, ProductDTO productDTO2) {
        restTemplate.exchange("/api/v1/products" , POST, new HttpEntity<>(productDTO1), ProductResponseDto.class).getBody();
        restTemplate.exchange("/api/v1/products" , POST, new HttpEntity<>(productDTO1), ProductResponseDto.class).getBody();
        restTemplate.exchange("/api/v1/products" , POST, new HttpEntity<>(productDTO2), ProductResponseDto.class).getBody();
        restTemplate.exchange("/api/v1/products" , POST, new HttpEntity<>(productDTO2), ProductResponseDto.class).getBody();
    }



    private void addProduct( ProductDTO productDTO1) {
        restTemplate.exchange("/api/v1/products" , POST, new HttpEntity<>(productDTO1), ProductResponseDto.class).getBody();
    }
}