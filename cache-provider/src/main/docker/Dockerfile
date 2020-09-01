FROM 10.3.98.241/library/java:8-alpine
MAINTAINER xuyf "xuyf@huntkey.net"
ENV TIME_ZONE Asia/Shanghai
RUN apk add --no-cache tzdata && echo "${TIME_ZONE}" > /etc/timezone && ln -sf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime
VOLUME /tmp
ADD cache-provider-1.0-SNAPSHOT.jar app.jar
EXPOSE 2000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.cloud.config.profile=docker","--spring.cloud.config.uri=http://${RX_CONFIG_SERVER_SERVICE_SERVICE_HOST}:${RX_CONFIG_SERVER_SERVICE_SERVICE_PORT}"]