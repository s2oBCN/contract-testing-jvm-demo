package es.testacademy.cursos.contract.testing.jvm.demo.users;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UsersRepository {

    private final Map<String, User> USERS = Map.of(
            "UserA", new User("UserA", "fc763eba-0905-41c5-a27f-3934ab26786c", "127.0.0.1", false),
            "UserB", new User("UserB", "fc763eba-0905-41c5-a27f-3934ab26786c", "127.0.0.1", false)
    );

    public List<User> fetchAll() {
        return List.copyOf(USERS.values());
    }

    public Optional<User> getByName(String name) {
        return Optional.ofNullable(USERS.get(name));
    }

}
