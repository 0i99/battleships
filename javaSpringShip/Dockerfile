FROM openjdk:11

WORKDIR /usr/src/javaspringship

COPY target/javaspringship*.jar /usr/src/javaspringship
COPY target/javaspringship*config.zip /usr/src/javaspringship/config.zip

RUN cd /usr/src/javaspringship && \
    unzip *.zip && \
    mkdir logs

CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5085 \
 -Xms256m -Xmx1024m \
 -Dlogging.config=config/logback.xml \
 -jar javaspringship*.jar