package io.github.kshashov.scopedmethods.api;

import javax.validation.constraints.NotNull;

/**
 * Declares scope listeners for specified group.
 */
public interface ScopedMethodsConfiguration {

    /**
     * @return the group for which this configuration is intended
     */
    @NotNull String getGroup();

    /**
     * This method is called before {@link ScopedMethod} method invocation.
     *
     * @param key       new scope id
     * @param parentKey previous (outer) scope id or {@code null} if nothing
     * @return processed scope id
     */
    @NotNull String validateScope(@NotNull String key, @NotNull String parentKey);

    /**
     * This method is called after {@link ScopedMethod} method invocation.
     *
     * @param key finished scope id
     */
    void onScopeFinished(@NotNull String key);
}
