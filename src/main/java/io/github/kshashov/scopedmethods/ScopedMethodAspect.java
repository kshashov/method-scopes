package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

@Aspect
public class ScopedMethodAspect extends BaseScopedMethodInterceptor implements Ordered {
    private ScopedMethodsManager scopesManager;
    private int order;

    /**
     * Matches the execution of any method with the ScopedMethod annotation.
     */
    @Pointcut("@annotation(scopedMethod)")
    public void annotatedMethod(ScopedMethod scopedMethod) {
    }

    @Around(value = "execution(* *(..)) && annotatedMethod(scopedMethod)", argNames = "pjp,scopedMethod")
    public Object around(ProceedingJoinPoint pjp, ScopedMethod scopedMethod) throws Throwable {
        if (scopesManager == null) {
            return pjp.proceed();
        }

        return invoke(new Invocation<Object>() {
            @Override
            public Object get() throws Throwable {
                return pjp.proceed();
            }
        }, scopedMethod, scopesManager);
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setScopesManager(ScopedMethodsManager scopesManager) {
        this.scopesManager = scopesManager;
    }
}
