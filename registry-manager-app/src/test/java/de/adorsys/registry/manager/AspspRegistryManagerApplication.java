package de.adorsys.registry.manager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication(scanBasePackages = "de.adorsys.registry.manager")
@ActiveProfiles("test")
public class AspspRegistryManagerApplication {
}
