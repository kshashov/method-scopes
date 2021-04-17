package io.github.kshashov.scopedmethods.integration.proxy.impl;

import io.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class MyScopeConfiguration implements ScopedMethodsConfiguration {
    public static final String SCOPE = "mygroup";

    @Override
    public @NotNull String getGroup() {
        return SCOPE;
    }

    @Override
    public @NotNull String validateScope(@NotNull String key, @NotNull String parentKey) {
        return key;
    }

    @Override
    public void onScopeFinished(@NotNull String key) {
        log.info("finish mygroup: " + key);
    }
}
