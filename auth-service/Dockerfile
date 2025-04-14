FROM amazoncorretto:21-alpine

WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"

# Expor a porta da aplicação
EXPOSE 8081

# Ponto de entrada para iniciar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Verificação de saúde
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8081/actuator/health || exit 1


