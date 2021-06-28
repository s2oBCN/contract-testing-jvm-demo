package es.testacademy.cursos.contract.testing.jvm.demo.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonArrayMinLike;
import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(PactConsumerTestExt.class)
public class ProductConsumerPactTest {

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact getAllProducts(PactDslWithProvider builder) {
        return builder.given("products exist")
                .uponReceiving("get all products")
                .method("GET")
                .path("/products")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json; charset=utf-8"))
                .body(newJsonArrayMinLike(2, array ->
                        array.object(object -> {
                            object.stringType("id", "09");
                            object.stringType("type", "CREDIT_CARD");
                            object.stringType("name", "Gem Visa");
                        })
                ).build())
                .toPact();
    }

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact getOneProduct(PactDslWithProvider builder) {
        return builder.given("product with ID 10 exists")
                .uponReceiving("get product with ID 10")
                .method("GET")
                .path("/products/10")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json; charset=utf-8"))
                .body(newJsonBody(object -> {
                    object.stringType("id", "10");
                    object.stringType("type", "CREDIT_CARD");
                    object.stringType("name", "28 Degrees");
                }).build())
                .toPact();
    }

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact noProductsExist(PactDslWithProvider builder) {
        return builder.given("no products exist")
                .uponReceiving("get all products")
                .method("GET")
                .path("/products")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json; charset=utf-8"))
                .body("[]")
                .toPact();
    }

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact productDoesNotExist(PactDslWithProvider builder) {
        return builder.given("product with ID 11 does not exist")
                .uponReceiving("get product with ID 11")
                .method("GET")
                .path("/products/11")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getAllProducts")
    void getAllProducts_whenProductsExist(MockServer mockServer) {
        Product product = new Product();
        product.setId("09");
        product.setType("CREDIT_CARD");
        product.setName("Gem Visa");
        List<Product> expected = List.of(product, product);

        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        List<Product> products = new ProductService(restTemplate).getAllProducts();

        assertEquals(expected, products);
    }

    @Test
    @PactTestFor(pactMethod = "getOneProduct")
    void getProductById_whenProductWithId10Exists(MockServer mockServer) {
        Product expected = new Product();
        expected.setId("10");
        expected.setType("CREDIT_CARD");
        expected.setName("28 Degrees");

        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        Product product = new ProductService(restTemplate).getProduct("10");

        assertEquals(expected, product);
    }

    @Test
    @PactTestFor(pactMethod = "noProductsExist")
    void getAllProducts_whenNoProductsExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        List<Product> products = new ProductService(restTemplate).getAllProducts();

        assertEquals(Collections.emptyList(), products);
    }

    @Test
    @PactTestFor(pactMethod = "productDoesNotExist")
    void getProductById_whenProductWithId11DoesNotExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();

        HttpClientErrorException e = assertThrows(HttpClientErrorException.class,
                () -> new ProductService(restTemplate).getProduct("11"));
        assertEquals(404, e.getRawStatusCode());
    }
}
