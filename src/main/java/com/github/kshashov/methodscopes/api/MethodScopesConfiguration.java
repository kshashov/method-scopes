package com.github.kshashov.methodscopes.api;

import javax.validation.constraints.NotNull;

public interface MethodScopesConfiguration {

    @NotNull String getGroup();

    @NotNull String validateScope(@NotNull String key, @NotNull String parentKey);

    void onScopeFinished(@NotNull String key);
}
