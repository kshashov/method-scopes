package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * Added scoped methods support for the methods annotated with {@link ScopedMethod}.
 * <p>
 * Use {@link ScopedMethodsHolder} to get the current scope id for the specified group.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(ScopedMethodsProperties.class)
public class ProxyScopedMethodsConfiguration extends BaseScopedMethodConfiguration {

    @Bean
    DefaultPointcutAdvisor scopedMethodsPointcutAdvisor(ScopedMethodsManager methodScopesManager) {
        Pointcut pointcut = new AnnotationMatchingPointcut(null, ScopedMethod.class, true);
        Advice interceptor = new MethodScopesInterceptor(methodScopesManager);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, interceptor);
        if (this.attributes != null) {
            advisor.setOrder(this.attributes.<Integer>getNumber("order"));
        }
        return advisor;
    }

    /**
     * Creates scope before original method invocation and removes scope after it. Do nothing for the methods without {@link ScopedMethod} annotation.
     */
    private static class MethodScopesInterceptor extends BaseScopedMethodInterceptor implements MethodInterceptor {
        private final ScopedMethodsManager scopesManager;

        public MethodScopesInterceptor(ScopedMethodsManager scopesManager) {
            this.scopesManager = scopesManager;
        }

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            ScopedMethod scopedMethod = AnnotatedElementUtils.findMergedAnnotation(methodInvocation.getMethod(), ScopedMethod.class);
            return invoke(methodInvocation::proceed, scopedMethod, scopesManager);
        }
    }
}
