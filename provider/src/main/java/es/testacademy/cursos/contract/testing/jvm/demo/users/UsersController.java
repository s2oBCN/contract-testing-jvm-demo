package es.testacademy.cursos.contract.testing.jvm.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {

    private UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("users")
    public List<User> getUsers() {
        return usersRepository.fetchAll();
    }

    @GetMapping("users/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable("name") String id) {
        Optional<User> user = usersRepository.getByName(id);
        return ResponseEntity.of(user);
    }

}
