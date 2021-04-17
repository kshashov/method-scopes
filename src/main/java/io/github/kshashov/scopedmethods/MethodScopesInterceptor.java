package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * Creates scope before original method invocation and removes scope after it. Do nothing for the methods without {@link ScopedMethod} annotation.
 */
public class MethodScopesInterceptor implements MethodInterceptor {
    private final ScopedMethodsManager scopesManager;

    public MethodScopesInterceptor(ScopedMethodsManager scopesManager) {
        this.scopesManager = scopesManager;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        ScopedMethod scopedMethod = AnnotatedElementUtils.findMergedAnnotation(methodInvocation.getMethod(), ScopedMethod.class);
        boolean supported = scopedMethod != null;

        if (supported) {
            scopesManager.startScope(scopedMethod.group(), scopedMethod.key());
        }

        try {
            return methodInvocation.proceed();
        } finally {
            if (supported) {
                scopesManager.popScope(scopedMethod.group());
            }
        }
    }
}
