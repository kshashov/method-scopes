package com.github.kshashov.methodscopes;

import com.github.kshashov.methodscopes.api.MethodScopesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@EnableConfigurationProperties(ScopedMethodsProperties.class)
public class MethodScopesAutoConfiguration {

    @Bean
    MethodScopesManager methodScopesManager(Optional<List<MethodScopesConfiguration>> methodScopesConfigurations) {
        return new MethodScopesManager(methodScopesConfigurations.orElse(new ArrayList<>()));
    }

    @Bean
    ScopedMethodsBeanPostProcessor scopedMethodsBeanPostProcessor(MethodScopesManager methodScopesManager, ScopedMethodsProperties properties) {
        return new ScopedMethodsBeanPostProcessor(methodScopesManager, properties.isClassAnnotationRequired(), properties.getPackages());
    }
}
