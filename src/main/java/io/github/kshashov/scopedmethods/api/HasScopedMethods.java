package io.github.kshashov.scopedmethods.api;

import io.github.kshashov.scopedmethods.ScopedMethodsProperties;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * If the 'classAnnotationRequired' option is {@code true}, this annotation must be set for the all classes in which {@link ScopedMethod} methods are declared. Otherwise, the {@link ScopedMethod} annotation will not work.
 *
 * @see ScopedMethod
 * @see ScopedMethodsProperties
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HasScopedMethods {
}
