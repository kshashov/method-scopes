package com.github.kshashov.methodscopes;

import com.github.kshashov.methodscopes.api.MethodScopesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MethodScopesManager {
    private static final ThreadLocal<Map<String, Stack<String>>> ACTIVE_SCOPES = new ThreadLocal<>();
    private final Map<String, MethodScopesConfiguration> scopesConfigurations;

    @Autowired
    public MethodScopesManager(Optional<List<MethodScopesConfiguration>> scopesConfigurations) {
        ACTIVE_SCOPES.set(new HashMap<>());

        this.scopesConfigurations = scopesConfigurations.orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(c -> c.getGroup(), c -> c));
    }

    public String getCurrent(@NotNull String group) {
        Map<String, Stack<String>> scopes = ACTIVE_SCOPES.get();

        Stack<String> groupScopes = scopes.get(group);
        if ((groupScopes == null) || groupScopes.empty()) {
            return null;
        }

        return groupScopes.peek();
    }

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

    void popScope(@NotNull String group) {
        Objects.requireNonNull(group);

        Map<String, Stack<String>> scopes = ACTIVE_SCOPES.get();

        if (scopes.containsKey(group)) {
            String key = scopes.get(group).pop();
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
