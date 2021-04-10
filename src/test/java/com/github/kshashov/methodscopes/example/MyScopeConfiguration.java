package com.github.kshashov.methodscopes.example;

import com.github.kshashov.methodscopes.api.MethodScopesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class MyScopeConfiguration implements MethodScopesConfiguration {
    public static final String SCOPE = "mygroup";

    @Override
    public @NotNull String getGroup() {
        return SCOPE;
    }

    @Override
    public @NotNull String validateScope(@NotNull String key, @NotNull String parentKey) {
        log.info("enter mygroup: " + parentKey + " -> " + key);
        return key;
    }

    @Override
    public void onScopeFinished(@NotNull String key) {
        log.info("finish mygroup: " + key);
    }
}
