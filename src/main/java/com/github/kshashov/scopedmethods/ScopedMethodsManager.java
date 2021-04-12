package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository storing information about currently active scopes. The current implementation doesn't allow to spawn new threads in annotated methods.
 */
@Slf4j
public class ScopedMethodsManager {
    private static final ThreadLocal<Map<String, Stack<String>>> ACTIVE_SCOPES = new ThreadLocal<>();
    private final Map<String, ScopedMethodsConfiguration> scopesConfigurations;

    public ScopedMethodsManager(@NotNull List<ScopedMethodsConfiguration> scopesConfigurations) {
        Objects.requireNonNull(scopesConfigurations);

        ACTIVE_SCOPES.set(new HashMap<>());

        this.scopesConfigurations = scopesConfigurations
                .stream()
                .collect(Collectors.toMap(c -> c.getGroup(), c -> c));
    }

    /**
     * Shortcut for {@link #getCurrent(String)} for empty group.
     *
     * @return the current scope
     */
    public String getCurrent() {
        return getCurrent("");
    }

    /**
     * Returns the current scope id for the specified group.
     *
     * @param group group id
     * @return the current scope id for the specified group or {@code null} if nothing
     */
    public String getCurrent(@NotNull String group) {
        Map<String, Stack<String>> scopes = ACTIVE_SCOPES.get();

        Stack<String> groupScopes = scopes.get(group);
        if ((groupScopes == null) || groupScopes.empty()) {
            return null;
        }

        return groupScopes.peek();
    }

    /**
     * Starts scope for the specified group.
     *
     * @param group group id
     * @param key   scope id
     */
    void startScope(@NotNull String group, @NotNull String key) {
        Objects.requireNonNull(group);

        String parent = null;
        Map<String, Stack<String>> scopes = ACTIVE_SCOPES.get();

        if (!scopes.containsKey(group)) {
            scopes.put(group, new Stack<>());
        } else {
            parent = scopes.get(group).peek();
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

        Map<String, Stack<String>> scopes = ACTIVE_SCOPES.get();

        if (scopes.containsKey(group)) {
            String key = null;
            try {
                key = scopes.get(group).pop();
            } catch (EmptyStackException ex) {
                // Do nothing
            }
            onScopeFinished(group, key);
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
