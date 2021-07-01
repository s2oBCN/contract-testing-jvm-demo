package es.testacademy.cursos.contract.testing.jvm.demo.provider;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProviderApplication {

    @SneakyThrows
    public static void main(String[] args) {
        waitToForcePossibleErrorInDockerCompose();
        SpringApplication.run(ProviderApplication.class, args);
    }

    private static void waitToForcePossibleErrorInDockerCompose() throws InterruptedException {
        Thread.sleep(4000);
    }

}
