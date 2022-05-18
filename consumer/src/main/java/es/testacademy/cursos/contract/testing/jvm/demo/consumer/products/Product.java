package es.testacademy.cursos.contract.testing.jvm.demo.consumer.products;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Product {
    private String id;
    private String type;
    private String name;
    private String version;
}
