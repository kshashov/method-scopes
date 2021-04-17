package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "scopedmethods")
public class ScopedMethodsProperties {
    /**
     * If {@code true}, {@link io.github.kshashov.scopedmethods.api.HasScopedMethods} annotation must be set for the all classes in which {@link ScopedMethod} methods are declared. Otherwise, the {@link ScopedMethod} annotation will not work.
     */
    boolean classAnnotationRequired;
}
