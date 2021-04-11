package com.github.kshashov.scopedmethods.api;

import com.github.kshashov.scopedmethods.ScopedMethodsBeanPostProcessor;
import com.github.kshashov.scopedmethods.ScopedMethodsProperties;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * If the 'classAnnotationRequired' option is {@code true}, this annotation must be set for the all classes in which {@link com.github.kshashov.scopedmethods.api.ScopedMethod} methods are declared. Otherwise, the {@link com.github.kshashov.scopedmethods.api.ScopedMethod} annotation will not work.
 *
 * @see ScopedMethod
 * @see ScopedMethodsBeanPostProcessor
 * @see ScopedMethodsProperties
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EnableScopedMethods {
}
