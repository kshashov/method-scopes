package com.github.kshashov.methodscopes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan()
@SpringBootApplication
public class MethodScopesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MethodScopesApplication.class, args);
	}
}
