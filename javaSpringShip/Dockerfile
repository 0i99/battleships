FROM maven:3.8.6-jdk-11 as build-stage

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY assembly /usr/src/app/assembly
COPY conf /usr/src/app/conf

#RUN apt-get update && apt-get install -y \
#    base64

RUN base64 -d /usr/src/app/conf/settings.xml.base64 > /usr/src/app/conf/settings.xml && \
    mvn -s /usr/src/app/conf/settings.xml -f /usr/src/app/pom.xml clean package


# -----------------------------


FROM openjdk:11

WORKDIR /usr/src/javaspringship

COPY --from=build-stage /usr/src/app/target/javaspringship*.jar /usr/src/javaspringship

COPY --from=build-stage /usr/src/app/target/javaspringship*config.zip /usr/src/javaspringship/config.zip

RUN cd /usr/src/javaspringship && \
    unzip *.zip && \
    mkdir logs

CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5085 \
 -Xms256m -Xmx1024m \
 -Dlogging.config=config/logback.xml \
 -jar javaspringship*.jar
