package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.api.EnableScopedMethods;
import com.github.kshashov.scopedmethods.api.ScopedMethod;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * Filters beand according to configuration properties and creates proxies for the supported ones.
 */
public class ScopedMethodsBeanPostProcessor implements BeanPostProcessor {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher(".");
    private final ScopedMethodsManager scopesManager;
    private final boolean classAnnotationRequired;
    private final String[] packages;

    public ScopedMethodsBeanPostProcessor(ScopedMethodsManager methodScopesManager, boolean classAnnotationRequired, String[] packages) {
        this.scopesManager = methodScopesManager;
        this.classAnnotationRequired = classAnnotationRequired;
        this.packages = packages;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        boolean supported = true;

        // Validate class annotation
        if (classAnnotationRequired && (AnnotationUtils.findAnnotation(bean.getClass(), EnableScopedMethods.class) == null)) {
            supported = false;
        }

        if (!supported) return bean;

        // Validate package
        if (packages.length > 0) {
            supported = false;

            String beanPackage = ClassUtils.getPackageName(bean.getClass());
            for (String pattern : packages) {
                if (pathMatcher.match(pattern, beanPackage)) {
                    supported = true;
                }
            }
        }

        if (!supported) return bean;

        // Validate methods
        supported = false;
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (AnnotatedElementUtils.findMergedAnnotation(method, ScopedMethod.class) != null) {
                supported = true;
                break;
            }
        }

        if (!supported) return bean;

        // Create proxy
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvice(new MethodScopesInterceptor(scopesManager));
        return proxyFactory.getProxy();
    }

    /**
     * Creates scope before original method invocation and removes scope after it. Do nothing for the methods without {@link ScopedMethod} annotation.
     */
    private static class MethodScopesInterceptor implements MethodInterceptor {
        private final ScopedMethodsManager scopesManager;

        private MethodScopesInterceptor(ScopedMethodsManager scopesManager) {
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
}
