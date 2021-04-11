package com.github.kshashov.methodscopes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "scopedmethods")
public class ScopedMethodsProperties {
    boolean classAnnotationRequired;
    private String[] packages = new String[0];
}
