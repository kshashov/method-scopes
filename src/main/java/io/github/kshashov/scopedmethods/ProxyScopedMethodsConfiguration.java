package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.EnableScopedMethods;
import io.github.kshashov.scopedmethods.api.HasScopedMethods;
import io.github.kshashov.scopedmethods.api.ScopedMethod;
import io.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Added scoped methods support for the methods annotated with {@link ScopedMethod}.
 * <p>
 * Use {@link ScopedMethodsHolder} to get the current scope id for the specified group.
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(ScopedMethodsProperties.class)
public class ProxyScopedMethodsConfiguration implements ImportAware {
    private AnnotationAttributes attributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        attributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableScopedMethods.class.getName(), false));
        if (this.attributes == null) {
            throw new IllegalArgumentException(
                    "@EnableScopedMethods is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    ScopedMethodsManager methodScopesManager(Optional<List<ScopedMethodsConfiguration>> methodScopesConfigurations) {
        return new ScopedMethodsManager(methodScopesConfigurations.orElse(new ArrayList<>()));
    }

    @Bean
    DefaultPointcutAdvisor scopedMethodsPointcutAdvisor(
            ScopedMethodsManager methodScopesManager,
            ScopedMethodsProperties properties) {
        Class<? extends Annotation> classAnnotationType = properties.isClassAnnotationRequired() ? HasScopedMethods.class : null;
        Pointcut pointcut = new AnnotationMatchingPointcut(classAnnotationType, ScopedMethod.class, true);
        Advice interceptor = new MethodScopesInterceptor(methodScopesManager);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, interceptor);
        if (this.attributes != null) {
            advisor.setOrder(this.attributes.<Integer>getNumber("order"));
        }
        return advisor;
    }
}
