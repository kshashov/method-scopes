package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.api.ScopedMethod;

public abstract class BaseScopedMethodInterceptor {

    protected Object invoke(
            Invocation<Object> invocation,
            ScopedMethod scopedMethod,
            ScopedMethodsManager scopesManager) throws Throwable {
        boolean supported = scopedMethod != null;

        if (supported) {
            scopesManager.startScope(scopedMethod.group(), scopedMethod.key());
        }

        try {
            return invocation.get();
        } finally {
            if (supported) {
                scopesManager.popScope(scopedMethod.group());
            }
        }
    }

    @FunctionalInterface
    public interface Invocation<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Throwable;
    }
}
