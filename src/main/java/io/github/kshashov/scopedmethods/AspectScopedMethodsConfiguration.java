package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;

/**
 * Added scoped methods support for the methods annotated with {@link ScopedMethod}.
 * <p>
 * Use {@link ScopedMethodsHolder} to get the current scope id for the specified group.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(ScopedMethodsProperties.class)
public class AspectScopedMethodsConfiguration extends BaseScopedMethodConfiguration {

    @Bean
    ScopedMethodAspect scopedMethodAspect(ScopedMethodsManager scopesManager) {
        ScopedMethodAspect aspect = new ScopedMethodAspect();
        aspect.setScopesManager(scopesManager);
        if (this.attributes != null) {
            aspect.setOrder(this.attributes.<Integer>getNumber("order"));
        }
        return aspect;
    }

    @Aspect
    public static class ScopedMethodAspect extends BaseScopedMethodInterceptor implements Ordered {
        private ScopedMethodsManager scopesManager;
        private int order;

        /**
         * Matches the execution of any method with the ScopedMethod annotation.
         */
        @Pointcut("@annotation(scopedMethod)")
        public void annotatedMethod(ScopedMethod scopedMethod) {
        }

        @Around(value = "annotatedMethod(scopedMethod)", argNames = "pjp,scopedMethod")
        public Object around(ProceedingJoinPoint pjp, ScopedMethod scopedMethod) throws Throwable {
            return invoke(pjp::proceed, scopedMethod, scopesManager);
        }

        @Override
        public int getOrder() {
            return order;
        }

        void setOrder(int order) {
            this.order = order;
        }

        void setScopesManager(ScopedMethodsManager scopesManager) {
            this.scopesManager = scopesManager;
        }
    }
}
