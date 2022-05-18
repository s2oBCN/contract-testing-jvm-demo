package es.testacademy.cursos.contract.testing.jvm.demo.consumer.users;

import lombok.Data;

@Data
public class User {

    private String id;
    private String name;
    private String ipAddress;
    private boolean admin;

}
