package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import io.github.kshashov.scopedmethods.api.ScopedMethods;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import java.util.Arrays;
import java.util.Collections;

@Aspect
public class ScopedMethodAspect extends BaseScopedMethodInterceptor implements Ordered {
    private ScopedMethodsManager scopesManager;
    private int order;

    /**
     * Matches the execution of any method with the ScopedMethod annotation.
     *
     * @param scopedMethods scopedMethods
     */
    @Pointcut("@annotation(scopedMethods)")
    public void annotatedMethod(ScopedMethods scopedMethods) {
    }

    /**
     * Matches the execution of any method with the ScopedMethods annotation.
     *
     * @param scopedMethod scopedMethod
     */
    @Pointcut("@annotation(scopedMethod)")
    public void annotated2Method(ScopedMethod scopedMethod) {
    }

    @Around(value = "execution(* *(..)) && annotatedMethod(scopedMethods)", argNames = "pjp,scopedMethods")
    public Object around(ProceedingJoinPoint pjp, ScopedMethods scopedMethods) throws Throwable {
        if (scopesManager == null) {
            return pjp.proceed();
        }

        System.out.println("[hui] " + scopedMethods.value());

        return invoke(new Invocation<Object>() {
            @Override
            public Object get() throws Throwable {
                return pjp.proceed();
            }
        }, Arrays.asList(scopedMethods.value()), scopesManager);
    }

    @Around(value = "execution(* *(..)) && annotated2Method(scopedMethod)", argNames = "pjp,scopedMethod")
    public Object around2(ProceedingJoinPoint pjp, ScopedMethod scopedMethod) throws Throwable {
        if (scopesManager == null) {
            return pjp.proceed();
        }

        return invoke(new Invocation<Object>() {
            @Override
            public Object get() throws Throwable {
                return pjp.proceed();
            }
        }, Collections.singletonList(scopedMethod), scopesManager);
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
