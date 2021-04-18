package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.EnableScopedMethods;
import io.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseScopedMethodConfiguration implements ImportAware {
    protected AnnotationAttributes attributes;

    @Bean
    ScopedMethodsManager methodScopesManager(Optional<List<ScopedMethodsConfiguration>> methodScopesConfigurations) {
        return new ScopedMethodsManager(methodScopesConfigurations.orElse(new ArrayList<>()));
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        attributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableScopedMethods.class.getName(), false));
        if (this.attributes == null) {
            throw new IllegalArgumentException(
                    "@EnableScopedMethods is not present on importing class " + importMetadata.getClassName());
        }
    }
}
