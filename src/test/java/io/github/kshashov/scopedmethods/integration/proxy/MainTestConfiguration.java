package io.github.kshashov.scopedmethods.integration.proxy;

import io.github.kshashov.scopedmethods.api.EnableScopedMethods;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableScopedMethods(proxyTargetClass = true)
@ComponentScan("io.github.kshashov.scopedmethods.integration.proxy")
public class MainTestConfiguration {
}
