package io.github.kshashov.scopedmethods.api;

import io.github.kshashov.scopedmethods.ScopedMethodsManager;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Indicates that the annotated method should be invoked in a scope with {@link #key()} id and {@link #group()} group. The current implementation doesn't allow to spawn new threads in annotated methods.
 * The current scope id for specified group can be get by {@link ScopedMethodsManager#getCurrent(String)} method.
 *
 * <pre>
 * public class Service1 {
 *     ...
 *     &#64;ScopedMethod(key = "inner")
 *     public void doSomething() {
 *         log.info(scopesManager.getCurrent());
 *     }
 * }
 *
 * public class Service2 {
 *     ...
 *     &#64;ScopedMethod(key = "outer")
 *     public void doSomething() {
 *         log.info(scopesManager.getCurrent());    // outer
 *         service1.doSomething();                  // inner
 *         log.info(scopesManager.getCurrent());    // outer
 *     }
 * }
 *
 * </pre>
 *
 * @see ScopedMethodsConfiguration Custom configuration with scope listeners
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScopedMethod {
    /**
     * @return Id of this scope
     */
    @AliasFor("key")
    String value() default "";

    /**
     * @return Id of this scope
     */
    @AliasFor("value")
    String key() default "";

    /**
     * @return Group id of this scope
     */
    String group() default "";
}
