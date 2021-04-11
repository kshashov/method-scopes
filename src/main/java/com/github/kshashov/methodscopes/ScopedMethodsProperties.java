package com.github.kshashov.methodscopes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "scopedmethods")
public class ScopedMethodsProperties {
    boolean classAnnotationRequired;
    private String[] packages = new String[0];
}
