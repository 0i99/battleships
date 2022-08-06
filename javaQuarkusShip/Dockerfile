FROM maven:3.8.6-jdk-11 as build-stage

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY conf /usr/src/app/conf

RUN base64 -d /usr/src/app/conf/settings.xml.base64 > /usr/src/app/settings.xml && \
    mvn -s /usr/src/app/settings.xml -f /usr/src/app/pom.xml clean package



# -----------------------------



FROM openjdk:11

WORKDIR /usr/src/javaquarkusship

COPY --from=build-stage /usr/src/app/target/javaquarkusship*.jar /usr/src/javaquarkusship

RUN cd /usr/src/javaquarkusship && \
    mkdir logs

CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5085 \
 -Xms256m -Xmx1024m \
 -jar javaquarkusship*.jar
