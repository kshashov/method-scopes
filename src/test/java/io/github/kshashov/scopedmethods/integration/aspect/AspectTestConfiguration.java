package io.github.kshashov.scopedmethods.integration.aspect;

import io.github.kshashov.scopedmethods.api.EnableScopedMethods;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableScopedMethods(mode = AdviceMode.ASPECTJ, proxyTargetClass = true)
@ComponentScan("io.github.kshashov.scopedmethods.integration.aspect")
public class AspectTestConfiguration {
    public static final String SCOPE = "mygroup";
}
