FROM amazoncorretto:11-al2-jdk as builder

WORKDIR /opt/build

#MAVEN INSTALL
COPY ./target/ ./target/
RUN mkdir -p target/extracted && java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM amazoncorretto:11-al2-jdk as base
ARG BUILD=/opt/build/target/extracted

RUN mkdir -m777 /opt/apps && \
    mkdir -m777 /opt/config

#RUN addgroup -S rssreader && adduser -S rssreader -G rssreader

RUN yum update -y
RUN yum upgrade -y

# Copy the application layers
COPY --from=builder ${BUILD}/dependencies/ /opt/apps/
COPY --from=builder ${BUILD}/spring-boot-loader/ /opt/apps/
COPY --from=builder ${BUILD}/snapshot-dependencies/ /opt/apps/
COPY --from=builder ${BUILD}/application/ /opt/apps/

FROM base

EXPOSE 8089

#USER rssreader

WORKDIR /opt/apps

HEALTHCHECK NONE

CMD ["java", "-Djava.rmi.server.hostname=rss-reader", "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.port=8089", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "org.springframework.boot.loader.JarLauncher"]
