FROM maven:3.9.11-eclipse-temurin-17 as maven

WORKDIR /app

COPY . .

RUN mvn -pl core clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=maven /app/core/target/*.jar core.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "core.jar"]