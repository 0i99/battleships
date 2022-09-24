# kotlinquarkusship Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8050/q/dev/.

    ## Packaging and running the application

    The application can be packaged using:
    ```shell script
    ./mvnw package
    ```

    The application is now runnable using `java -jar target/kotlinquarkusship*.jar`.


## Build and run via docker

`docker build -f Dockerfile -t pl.battleships/kotlin-quarkus-ship .`

`docker run -i --rm -p 8051:8051 pl.battleships/kotlin-quarkus-ship`

### Build native and run via docker

`docker build -f Dockerfile.native -t pl.battleships/kotlin-quarkus-ship-native .`

`docker run -i --rm -p 8051:8051 pl.battleships/kotlin-quarkus-ship-native`
