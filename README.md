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
## Example

```java

@EnableScopedMethods(proxyTargetClass = true)
@SpringBootApplication
public class MethodScopesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MethodScopesApplication.class, args);
	}
}

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
 
For example, you need to use a replica or master datasource for your base methods:
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
        return ScopedMethodsHolder.getCurrent();
    }
}
```

## @EnableScopedMethods
TODO

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

## @HasScopedMethods
If the `classAnnotationRequired` option is `true` (see the _Configurations_ section), this annotation must be set for the class in which `@ScopedMethod` annotated methods are declared.

## ScopedMethodsHolder
Inject `ScopedMethodsHolder` bean to get the current scope id at any time. Do not forget to specify the `group` argument if you have declare your scopes with this parameter.
```java
ScopedMethodsHolder.getCurrent(); // default "" group
ScopedMethodsHolder.getCurrent("datasource");
ScopedMethodsHolder.getCurrent("mygroup");
```
## Configurations

### Properties
Property | Description | Default value
--- | ---| --- 
|`scopedmethods.classAnnotationRequired`|Indicates whether `@EnableScopedMethods` annotation must be set for the all classes in which `@ScopedMethod` annotated methods are declared|`false`

### ScopedMethodsConfiguration

You can declare your own `ScopedMethodsConfiguration` implementation to subscribe on scope changing. For example, it may be useful to keep track of a case when a master scope is created inside a replica scope.

