package de.adorsys.registry.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "de.adorsys.registry.manager")
public class AspspRegistryManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AspspRegistryManagerApplication.class, args); //NOSONAR
    }
}