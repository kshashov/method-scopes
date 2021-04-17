package io.github.kshashov.scopedmethods.api;

import io.github.kshashov.scopedmethods.ScopedMethodsConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Enables {@link ScopedMethod} support for using scoped methods capabilities similar to the @Transactional support.
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
 * </pre>
 *
 * @see ScopedMethod
 * @see HasScopedMethods
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(ScopedMethodsConfigurationSelector.class)
public @interface EnableScopedMethods {

    /**
     * Indicate whether subclass-based (CGLIB) proxies are to be created ({@code true}) as
     * opposed to standard Java interface-based proxies ({@code false}). The default is
     * {@code false}. <strong>Applicable only if {@link #mode()} is set to
     * {@link AdviceMode#PROXY}</strong>.
     * <p>Note that setting this attribute to {@code true} will affect <em>all</em>
     * Spring-managed beans requiring proxying, not just those marked with
     * {@link ScopedMethod}.
     */
    boolean proxyTargetClass() default false;

    /**
     * Indicate how transactional advice should be applied.
     * <p><b>The default is {@link AdviceMode#PROXY}.</b>
     * Please note that proxy mode allows for interception of calls through the proxy
     * only. Local calls within the same class cannot get intercepted that way; an
     * {@link ScopedMethod} annotation on such a method within a local call will be
     * ignored since Spring's interceptor does not even kick in for such a runtime
     * scenario. For a more advanced mode of interception, consider switching this to
     * {@link AdviceMode#ASPECTJ}.
     */
    AdviceMode mode() default AdviceMode.PROXY;

    /**
     * Indicate the ordering of the execution of the transaction advisor
     * when multiple advices are applied at a specific joinpoint.
     * <p>The default is {@link Ordered#LOWEST_PRECEDENCE}.
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
