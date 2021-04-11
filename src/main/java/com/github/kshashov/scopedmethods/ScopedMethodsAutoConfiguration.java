package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Added scoped methods support for the methods annotated with {@link com.github.kshashov.scopedmethods.api.ScopedMethod}.
 * <p>
 * Use {@link ScopedMethodsManager} to get the current scope id for the specified group.
 *
 * @see com.github.kshashov.scopedmethods.api.ScopedMethod
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ScopedMethodsProperties.class)
public class ScopedMethodsAutoConfiguration {

    @Bean
    ScopedMethodsManager methodScopesManager(Optional<List<ScopedMethodsConfiguration>> methodScopesConfigurations) {
        return new ScopedMethodsManager(methodScopesConfigurations.orElse(new ArrayList<>()));
    }

    @Bean
    ScopedMethodsBeanPostProcessor scopedMethodsBeanPostProcessor(ScopedMethodsManager methodScopesManager, ScopedMethodsProperties properties) {
        return new ScopedMethodsBeanPostProcessor(methodScopesManager, properties.isClassAnnotationRequired(), properties.getPackages());
    }
}
