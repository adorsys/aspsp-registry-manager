FROM openjdk:8-alpine

WORKDIR /service
ENV JAVA_OPTS ""
ENV SERVICE_PARAMS ""
ADD registry-manager-app/target/registry-manager-app.jar /service/
CMD java $JAVA_OPTS -jar registry-manager-app.jar $SERVICE_PARAMS