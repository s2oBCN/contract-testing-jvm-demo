package es.testacademy.cursos.contract.testing.jvm.demo.consumer.users;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
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

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArrayMinLike;
import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "UserService", pactVersion = PactSpecVersion.V3)
public class UserConsumerPactTest {

    @Pact(consumer = "FrontendApplication")
    RequestResponsePact getAllUsers(PactDslWithProvider builder) {
        return builder.given("users exist")
                .uponReceiving("get all users")
                .method("GET")
                .path("/users")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json; charset=utf-8"))
                .body(newJsonArrayMinLike(2, array ->
                        array.object(object -> {
                            object.stringType("id", "09");
                            object.ipV4Address("ipAddress");
                            object.stringType("name", "UserA");

                        })
                ).build())
                .toPact();
    }

    @Pact(consumer = "FrontendApplication")
    RequestResponsePact getOneUser(PactDslWithProvider builder) {
        return builder.given("user with ID 10 exists")
                .uponReceiving("get user with ID 10")
                .method("GET")
                .path("/users/10")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json; charset=utf-8"))
                .body(newJsonBody(object -> {
                    object.stringType("id", "10");
                    object.ipV4Address("ipAddress");
                    object.stringType("name", "UserA");
                }).build())
                .toPact();
    }

    @Pact(consumer = "FrontendApplication")
    RequestResponsePact noUsersExist(PactDslWithProvider builder) {
        return builder.given("no users exist")
                .uponReceiving("get all users")
                .method("GET")
                .path("/users")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json; charset=utf-8"))
                .body("[]")
                .toPact();
    }

    @Pact(consumer = "FrontendApplication")
    RequestResponsePact userDoesNotExist(PactDslWithProvider builder) {
        return builder.given("user with ID 11 does not exist")
                .uponReceiving("get user with ID 11")
                .method("GET")
                .path("/users/11")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getAllUsers")
    void getAllUsers_whenUsersExist(MockServer mockServer) {
        User user = new User();
        user.setId("09");
        user.setIpAddress("127.0.0.1");
        user.setName("UserA");
        List<User> expected = List.of(user, user);
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        List<User> users = new UserService(restTemplate).getAllUsers();
        assertEquals(expected, users);
    }

    @Test
    @PactTestFor(pactMethod = "getOneUser")
    void getUserById_whenUserWithId10Exists(MockServer mockServer) {
        User expected = new User();
        expected.setId("10");
        expected.setIpAddress("127.0.0.1");
        expected.setName("UserA");
        expected.setAdmin(false);
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        User user = new UserService(restTemplate).getUser("10");
        assertEquals(expected, user);
    }

    @Test
    @PactTestFor(pactMethod = "noUsersExist")
    void getAllUsers_whenNoUsersExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        List<User> users = new UserService(restTemplate).getAllUsers();
        assertEquals(Collections.emptyList(), users);
    }

    @Test
    @PactTestFor(pactMethod = "userDoesNotExist")
    void getUserById_whenUserWithId11DoesNotExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        HttpClientErrorException e = assertThrows(HttpClientErrorException.class,
                () -> new UserService(restTemplate).getUser("11"));
        assertEquals(404, e.getRawStatusCode());
    }
}
