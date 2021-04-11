package com.github.kshashov.methodscopes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(ScopedMethodsProperties.class)
public class MethodScopesAutoConfiguration {

    @Bean
    MethodScopesBeanPostProcessor telegramControllerBeanPostProcessor(MethodScopesManager methodScopesManager, ScopedMethodsProperties properties) {
        return new MethodScopesBeanPostProcessor(methodScopesManager, properties.isClassAnnotationRequired(), properties.getPackages());
    }
}
