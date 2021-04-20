package io.github.kshashov.scopedmethods.api;

import java.lang.annotation.*;

/**
 * Aggregated annotation for storing several {@link ScopedMethod} instances.
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScopedMethods {
    /**
     * @return array of {@link ScopedMethod} instances
     */
    ScopedMethod[] value();
}
