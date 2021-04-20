package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.Set;

public class MethodScopesInterceptor extends BaseScopedMethodInterceptor implements MethodInterceptor {
    private final ScopedMethodsManager scopesManager;

    public MethodScopesInterceptor(ScopedMethodsManager scopesManager) {
        this.scopesManager = scopesManager;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Set<ScopedMethod> scopedMethod = AnnotatedElementUtils.findMergedRepeatableAnnotations(methodInvocation.getMethod(), ScopedMethod.class);
        return invoke(methodInvocation::proceed, scopedMethod, scopesManager);
    }
}
