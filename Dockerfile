FROM eclipse-temurin:17-jreecho.echo WORKDIR /appecho.echo COPY target/application-0.0.1-SNAPSHOT.jar app.jarecho.echo ENTRYPOINT ["java", "-jar", "app.jar"]
