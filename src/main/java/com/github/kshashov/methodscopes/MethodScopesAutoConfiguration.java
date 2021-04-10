package com.github.kshashov.methodscopes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Configuration
public class MethodScopesAutoConfiguration {

    @Value("${scoped-methods.classAnnotationRequired:false}")
    boolean classAnnotationRequired;

    @Value("${scoped-methods.packages:}")
    private String[] packages = new String[0];

    @Bean
    MethodScopesBeanPostProcessor telegramControllerBeanPostProcessor(MethodScopesManager methodScopesManager) {
        return new MethodScopesBeanPostProcessor(methodScopesManager, classAnnotationRequired, packages);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(metadata.getAnnotationAttributes(ConfigurationPropertiesScan.class.getName()));
        String[] basePackages = attributes.getStringArray("basePackages");
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        if (packagesToScan.isEmpty()) {
            packagesToScan.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        packagesToScan.removeIf((candidate) -> !StringUtils.hasText(candidate));
        return packagesToScan;
    }
}
