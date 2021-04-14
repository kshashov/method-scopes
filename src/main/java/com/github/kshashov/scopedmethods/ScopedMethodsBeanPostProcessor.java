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

        if (isHasSupportedClass(bean)
                && isHasSupportedMethods(bean)
                && isHasSupportedPackage(bean)) {
            // Create proxy
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.addAdvice(new MethodScopesInterceptor(scopesManager));
            return proxyFactory.getProxy();
        }

        return bean;
    }

    /**
     * Checks class package in white list.
     *
     * @param bean bean object
     * @return {@code true} if class has supported package or no filters are set
     */
    private boolean isHasSupportedPackage(Object bean) {
        if (packages.length == 0) {
            return true;
        }

        String beanPackage = ClassUtils.getPackageName(bean.getClass());
        for (String pattern : packages) {
            if (pathMatcher.match(pattern, beanPackage)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check for {@link EnableScopedMethods} annotation on bean methods.
     *
     * @param bean bean object
     * @return {@code true} if annotation exists
     */
    private boolean isHasSupportedMethods(Object bean) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (AnnotatedElementUtils.findMergedAnnotation(method, ScopedMethod.class) != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check for {@link EnableScopedMethods} annotation existance if 'classAnnotationRequired' option is enabled.
     *
     * @param bean bean object
     * @return {@code true} if annotation exists or no needed
     */
    private boolean isHasSupportedClass(Object bean) {
        return !classAnnotationRequired
                || (AnnotationUtils.findAnnotation(bean.getClass(), EnableScopedMethods.class) != null);
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
