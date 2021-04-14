package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Repository storing information about currently active scopes. The current implementation doesn't allow to spawn new threads in annotated methods.
 */
@Slf4j
public class ScopedMethodsManager {
    private final Map<String, ScopedMethodsConfiguration> scopesConfigurations;

    public ScopedMethodsManager(@NotNull List<ScopedMethodsConfiguration> scopesConfigurations) {
        Objects.requireNonNull(scopesConfigurations);

        this.scopesConfigurations = scopesConfigurations.stream()
                .collect(Collectors.toMap(c -> c.getGroup(), c -> c));
    }

    /**
     * Starts scope for the specified group.
     *
     * @param group group id
     * @param key   scope id
     */
    void startScope(@NotNull String group, @NotNull String key) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(key);

        String parent = null;
        Map<String, Stack<String>> scopes = ScopedMethodsHolder.ACTIVE_SCOPES.get();

        if (!scopes.containsKey(group)) {
            scopes.put(group, new Stack<>());
        } else {
            if (!scopes.get(group).isEmpty()) {
                parent = scopes.get(group).peek();
            }
        }

        key = validateScope(group, key, parent);
        scopes.get(group).add(key);
    }

    /**
     * Finishes the current scope for the specified group.
     *
     * @param group group id
     */
    void popScope(@NotNull String group) {
        Objects.requireNonNull(group);

        Map<String, Stack<String>> scopes = ScopedMethodsHolder.ACTIVE_SCOPES.get();

        if (scopes.containsKey(group)) {
            String key = null;
            if (!scopes.get(group).isEmpty()) {
                key = scopes.get(group).pop();
                onScopeFinished(group, key);
            }
        }
    }

    private void onScopeFinished(@NotNull String group, @NotNull String key) {
        if (scopesConfigurations.containsKey(group)) {
            scopesConfigurations.get(group).onScopeFinished(key);
        }
    }

    private String validateScope(@NotNull String group, @NotNull String key, @NotNull String parent) {
        if (scopesConfigurations.containsKey(group)) {
            return scopesConfigurations.get(group).validateScope(key, parent);
        }
        return key;
    }
}
