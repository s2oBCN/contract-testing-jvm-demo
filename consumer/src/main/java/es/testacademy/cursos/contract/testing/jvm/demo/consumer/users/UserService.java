package es.testacademy.cursos.contract.testing.jvm.demo.consumer.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        return restTemplate.exchange("/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>(){}).getBody();
    }

    public User getUser(String userName) {
        return restTemplate.getForEntity("/users/{userName}", User.class, userName).getBody();
    }

}
