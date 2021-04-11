# Scoped Methods Spring Boot Starter

[![JitPack](https://jitpack.io/v/kshashov/scoped-methods.svg)](https://jitpack.io/#kshashov/scoped-methods)
[![CircleCI](https://circleci.com/gh/kshashov/scoped-methods?style=svg)](https://circleci.com/gh/kshashov/scoped-methods)
[![codecov](https://codecov.io/gh/kshashov/scoped-methods/branch/master/graph/badge.svg)](https://codecov.io/gh/kshashov/scoped-methods)

## Download
### Maven
TODO
### Gradle
TODO
## Example
Imagine that you need to add some kind of metadata for a method so that it acts differently depending on this metadata.
 
For example, you need to use a replica or master datasource for your base methods:
```java
public interface MyService {
    @ScopedMethod("master")
    void update(String name);

    @ScopedMethod("replica")
    String get(String name);
}
```
Then, somewhere in the `AbstractRoutingDataSource` implementation, you could have something like this:
```java
public class MasterSlaveDataSource extends AbstractRoutingDataSource {
    ...
            
    @Override
    protected Object determineCurrentLookupKey() {
        return scopesManager.getCurrent();
    }
}
```

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

## @EnableScopedMethods
If the `classAnnotationRequired` option is `true` (see the _Configurations_ section), this annotation must be set for the class in which `@ScopedMethod` annotated methods are declared.

## ScopedMethodsManager

Inject `ScopedMethodsManager` bean to get the ability to retrive the current scope id at any time. Do not forget to specify the `group` argument if you have declare your scopes with this parameter.
```java
scopesManager.getCurrent(); // default "" group
scopesManager.getCurrent("datasource");
scopesManager.getCurrent("mygroup");
```
## Configurations

### Properties
Property | Description | Default value
--- | ---| --- 
|`scopedmethods.classAnnotationRequired`|TODO|`false`
|`scopedmethods.packages`|TODO|`[]`

### MethodScopesConfiguration

You can declare your own `ScopedMethodsConfiguration` implementation to subscribe on scope changing. For example, it may be useful to keep track of a case when a master scope is created inside a replica scope.

