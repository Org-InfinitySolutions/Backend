FROM amazoncorretto:21-alpine

WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8082/actuator/health || exit 1


