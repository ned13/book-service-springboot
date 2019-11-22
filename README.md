# book-service-spring-boot
## Overview

## Environment

## How to build

## How to run
- Use spring-boot-maven-plugin
```
mvn spring-boot:run
```

- Specify JVM options
```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-XX:NativeMemoryTracking=summary"
```