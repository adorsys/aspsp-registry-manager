FROM adorsys/java:11

MAINTAINER https://github.com/adorsys/aspsp-registry-manager/

ENV SERVER_PORT 8089
ENV JAVA_OPTS -Xmx1024m
ENV JAVA_TOOL_OPTIONS -Xmx1024m

WORKDIR /opt/aspsp-registry-manager

COPY registry-manager-app/target/registry-manager-app.jar /opt/aspsp-registry-manager/registry-manager-app.jar

EXPOSE 8089

CMD exec $JAVA_HOME/bin/java $JAVA_OPTS -jar /opt/aspsp-registry-manager/registry-manager-app.jar