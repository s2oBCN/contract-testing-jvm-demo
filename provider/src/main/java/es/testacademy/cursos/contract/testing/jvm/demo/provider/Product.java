package es.testacademy.cursos.contract.testing.jvm.demo.provider;

import lombok.Data;

@Data
public class Product {
    private final String id;
    private final String type;
    private final String name;
    private final String version;
}
