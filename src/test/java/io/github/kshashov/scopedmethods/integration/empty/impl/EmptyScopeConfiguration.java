package io.github.kshashov.scopedmethods.integration.empty.impl;

import io.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class EmptyScopeConfiguration implements ScopedMethodsConfiguration {
    public static final String SCOPE = "";

    @Override
    public @NotNull String getGroup() {
        return SCOPE;
    }

    @Override
    public @NotNull String validateScope(@NotNull String key, @NotNull String parentKey) {
        throw new IllegalStateException();
    }

    @Override
    public void onScopeFinished(@NotNull String key) {
        throw new IllegalStateException();
    }
}
