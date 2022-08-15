## Packaging and running the application

Build applicaton using:
```shell script
./mvnw package
```

and run using `java -jar target/kotlinspringship*.jar`.

## Build and run via docker

`docker build -f Dockerfile -t pl.battleships/kotlin-spring-ship .`

`docker run -i --rm -p 8051:8051 pl.battleships/kotlin-spring-ship`