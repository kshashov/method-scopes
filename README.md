# Scoped Methods Spring Boot Starter
![Maven metadata URL](https://img.shields.io/maven-metadata/v?color=green&label=Maven%20Central&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fio%2Fgithub%2Fkshashov%2Fscoped-methods-spring-boot-starter%2Fmaven-metadata.xml)
[![JitPack](https://jitpack.io/v/kshashov/scoped-methods.svg)](https://jitpack.io/#kshashov/scoped-methods)
[![CircleCI](https://circleci.com/gh/kshashov/scoped-methods.svg?style=svg)](https://circleci.com/gh/kshashov/scoped-methods)
[![codecov](https://codecov.io/gh/kshashov/scoped-methods/branch/main/graph/badge.svg?token=QMR9GEVMSN)](https://codecov.io/gh/kshashov/scoped-methods)
## Download
### Maven
```xml
<dependency>
    <groupId>io.github.kshashov</groupId>
    <artifactId>scoped-methods-spring-boot-starter</artifactId>
    <version>0.9.1</version>
</dependency>
```

### Gradle
```groovy
implementation group: 'io.github.kshashov', name: 'scoped-methods-spring-boot-starter', version: '0.9.1'
```
## Usage
Firstly, you need to enable scoped methods support by adding `@EnableScopedMethods` annotation to your configuration
```java
@EnableScopedMethods(proxyTargetClass = true)
@SpringBootApplication
public class MethodScopesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MethodScopesApplication.class, args);
    }
}
```

After that you can annotate your methods with `@ScopedMethod` annotation and get the current scope id in runtime with `ScopedMethodsHolder` singleton:
```java
@Service
public class Service1 {

    @ScopedMethod(key = "inner")
    public void doSomething() {
        log.info(ScopedMethodsHolder.getCurrent());
    }
}

@Service
public class Service2 {

    @ScopedMethod(key = "outer")
    public void doSomething() {
        log.info(ScopedMethodsHolder.getCurrent());    // outer
        service1.doSomething();                        // inner
        log.info(ScopedMethodsHolder.getCurrent());    // outer
    }
}
```
#### On practice
Imagine that you need to add some kind of metadata for a method so that it acts differently depending on this metadata.
For example, you need to use replica or master datasources in your backend methods. Using this library you just need to specify replica scope for some methods:
```java
public interface MyService {
    @ScopedMethod("replica")
    String get(String name);
}
```
Then, somewhere in the `AbstractRoutingDataSource` implementation, you could have something like this:
```java
public class MasterSlaveDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String scopeId = ScopedMethodsHolder.getCurrent();
        return scopeId != null? scopeId : "master"; // Use master DS by default
    }
}
```

## @EnableScopedMethods
Arguments:
* `mode`: indicates how interceptor should be applied. The default is `AdviceMode#PROXY`. Please note that proxy mode allows for interception of calls through the proxy only. Local calls within the same class cannot get intercepted that way
* `proxyTargetClass`: indicate whether subclass-based (CGLIB) proxies are to be created as opposed to standard Java interface-based proxies. Applicable only if `mode` is set to `AdviceMode#PROXY`
* `order`: indicates the ordering of the execution of the interceptor when multiple advices are applied at a specific joinpoint

## @ScopedMethod
Arguments:
* `value` or `key`: scope identificator
* `group`: allow you to have several sets of scopes for different purposes
    ```java
    @ScopedMethod(group = "datasource", key = "master")
    @ScopedMethod(group = "datasource", key = "replica")
    
    @ScopedMethod(group = "mygroup", key = "key1")
    @ScopedMethod(group = "mygroup", key = "key2")
    ```
The current implementation does not allow placing several such annotations on single method.

## ScopedMethodsHolder
Inject `ScopedMethodsHolder` bean to get the current scope id at any time. Do not forget to specify the `group` argument if you have declare your scopes with this parameter.
```java
ScopedMethodsHolder.getCurrent(); // default "" group
ScopedMethodsHolder.getCurrent("datasource");
ScopedMethodsHolder.getCurrent("mygroup");
```
## Configurations

### ScopedMethodsConfiguration

You can declare your own `ScopedMethodsConfiguration` implementation to subscribe on scope changing. For example, it may be useful to keep track of a case when a master scope is created inside a replica scope.

