FROM amazoncorretto:11-alpine as builder

WORKDIR /opt/build

ARG MAVEN_CLI_OPTS
ARG MAVEN_OPTS
ARG MAVEN_USER
ARG MAVEN_PASSWORD

COPY .m2 .m2
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x ./mvnw
COPY src src
COPY ignore.xml .
COPY pom.xml .

#MAVEN INSTALL
RUN ./mvnw -DskipTests $MAVEN_CLI_OPTS $MAVEN_OPTS install
RUN mkdir -p target/extracted && java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM amazoncorretto:11-alpine as base

RUN mkdir -m777 /opt/apps && \
    mkdir -m777 /opt/config

RUN addgroup -S rssreader && adduser -S rssreader -G rssreader

run apk update && apk upgrade --available

# Copy the application layers
COPY --from=builder ${BUILD}/dependencies/ /opt/apps/
COPY --from=builder ${BUILD}/spring-boot-loader/ /opt/apps/
COPY --from=builer ${BUILD}/application/ /opt/apps

FROM base

EXPOSE 8089

USER rssreader

WORKDIR /opt/apps

HEALTHCHECK NONE

CMD ["java", "-Djava.rmi.server.hostname=rss-reader", "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.port=8089", "-Dcom.sun.management.jmxremote.ssl=false", "org.springframework.boot.loader.JarLauncher"]
