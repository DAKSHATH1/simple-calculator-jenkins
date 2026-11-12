FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/application-0.0.1-SNAPSHOT.jar app.jar

CMD ["sh", "-c", "while true; do sleep 3600; done"]
