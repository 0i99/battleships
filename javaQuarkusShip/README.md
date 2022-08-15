# javaquarkusship Project

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

    The application is now runnable using `java -jar target/javaquarkusship*.jar`.


## Build and run via docker

`docker build -f Dockerfile -t pl.battleships/java-quarkus-ship .`

`docker run -i --rm -p 8051:8051 pl.battleships/java-quarkus-ship`

### Build native and run via docker

`docker build -f Dockerfile.native -t pl.battleships/java-quarkus-ship-native .`

`docker run -i --rm -p 8051:8051 pl.battleships/java-quarkus-ship-native`

## Comparison in start time

Please notice application start time on below logs. Native solution takes 0.026s to start. Wow.

```
docker run --rm -p 8051:8051 pl.battleships/java-quarkus-ship
Listening for transport dt_socket at address: 5085
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2022-07-31 16:59:03,727 INFO  [io.quarkus] (main) javaquarkusship 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.11.1.Final) started in 1.340s. Listening on: http://0.0.0.0:8050
2022-07-31 16:59:03,736 INFO  [io.quarkus] (main) Profile prod activated. 
2022-07-31 16:59:03,736 INFO  [io.quarkus] (main) Installed features: [cdi, rest-client-reactive, rest-client-reactive-jackson, resteasy-reactive, smallrye-context-propagation, smallrye-openapi, vertx]
```

```
docker run --rm -p 8051:8051 pl.battleships/java-quarkus-ship-native
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2022-07-31 16:58:45,543 INFO  [io.quarkus] (main) javaquarkusship 1.0.0-SNAPSHOT native (powered by Quarkus 2.11.1.Final) started in 0.026s. Listening on: http://0.0.0.0:8050
2022-07-31 16:58:45,543 INFO  [io.quarkus] (main) Profile prod activated. 
2022-07-31 16:58:45,543 INFO  [io.quarkus] (main) Installed features: [cdi, rest-client-reactive, rest-client-reactive-jackson, resteasy-reactive, smallrye-context-propagation, smallrye-openapi, vertx]
^C2022-07-31 16:58:50,044 INFO  [io.quarkus] (Shutdown thread) javaquarkusship stopped in 0.005s
```
