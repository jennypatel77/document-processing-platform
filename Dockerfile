FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/docprocessor-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]