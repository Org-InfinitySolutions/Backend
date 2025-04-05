package com.infinitysolutions.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        properties = {
                "eureka.client.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "spring.cloud.service-registry.auto-registration.enabled=false",
                "spring.security.user.name=test",
                "spring.security.user.password=test"
        }
)

@ActiveProfiles("test")
public class BaseIntegrationTest {
}
