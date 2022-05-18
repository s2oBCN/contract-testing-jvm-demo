package es.testacademy.cursos.contract.testing.jvm.demo.users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String ipAddress;
    private boolean admin;
}
