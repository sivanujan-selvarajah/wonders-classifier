# Dockerfile für wonders-classifier
FROM openjdk:21-jdk-slim

WORKDIR /usr/src/app

# Quellcode + Maven Wrapper kopieren
COPY models models
COPY src src
COPY .mvn .mvn
COPY pom.xml mvnw ./

# Maven Build im Container
RUN chmod +x mvnw
RUN ./mvnw -Dmaven.test.skip=true package

# Exponierter Port
EXPOSE 8080

# Startbefehl: passe JAR-Namen an dein Projekt an!
CMD ["java", "-jar", "target/wonders-classifier-0.0.1-SNAPSHOT.jar"]