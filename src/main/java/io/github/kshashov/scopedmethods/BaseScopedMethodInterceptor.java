package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;

import java.util.Collection;
import java.util.Objects;

/**
 * Creates scope before original method invocation and removes scope after it. Do nothing for the methods without {@link ScopedMethod} annotation.
 */
public abstract class BaseScopedMethodInterceptor {

    protected Object invoke(
            Invocation<Object> invocation,
            Collection<ScopedMethod> scopedMethods,
            ScopedMethodsManager scopesManager) throws Throwable {
        Objects.requireNonNull(scopesManager);
        boolean supported = !scopedMethods.isEmpty();

        if (supported) {
            for (ScopedMethod scopedMethod : scopedMethods) {
                scopesManager.startScope(scopedMethod.group(), scopedMethod.key());
            }
        }

        try {
            return invocation.get();
        } finally {
            if (supported) {
                for (ScopedMethod scopedMethod : scopedMethods) {
                    scopesManager.popScope(scopedMethod.group());
                }
            }
        }
    }

    @FunctionalInterface
    public interface Invocation<T> {

        /**
         * Gets a result.
         *
         * @return a result of invocation
         * @throws Throwable throwable
         */
        T get() throws Throwable;
    }
}
