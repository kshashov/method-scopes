package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import org.aspectj.lang.Aspects;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Added scoped methods support for the methods annotated with {@link ScopedMethod}.
 * <p>
 * Use {@link ScopedMethodsHolder} to get the current scope id for the specified group.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(ScopedMethodsProperties.class)
public class AspectScopedMethodsConfiguration extends BaseScopedMethodConfiguration implements DisposableBean {

    @Bean
    ScopedMethodAspect scopedMethodAspect(ScopedMethodsManager scopesManager) {
        ScopedMethodAspect aspect = Aspects.aspectOf(ScopedMethodAspect.class);
        aspect.setScopesManager(scopesManager);
        if (this.attributes != null) {
            aspect.setOrder(this.attributes.<Integer>getNumber("order"));
        }
        return aspect;
    }

    @Override
    public void destroy() throws Exception {
        // Reset aspect
        ScopedMethodAspect aspect = Aspects.aspectOf(ScopedMethodAspect.class);
        aspect.setScopesManager(null);
    }
}
