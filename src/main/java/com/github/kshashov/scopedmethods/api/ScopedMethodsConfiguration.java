package com.github.kshashov.scopedmethods.api;

import javax.validation.constraints.NotNull;

public interface ScopedMethodsConfiguration {

    @NotNull String getGroup();

    @NotNull String validateScope(@NotNull String key, @NotNull String parentKey);

    void onScopeFinished(@NotNull String key);
}
