# Etapa 1: Construção
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build
ARG LNCR_MAVEN_ACTOR
ARG LNCR_MAVEN_PAT
ENV LNCR_MAVEN_ACTOR=$LNCR_MAVEN_ACTOR LNCR_MAVEN_PAT=$LNCR_MAVEN_PAT
WORKDIR /app
COPY /app .
RUN mvn clean test package -s maven_settings.xml

# Etapa 2: Execução
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/app*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
