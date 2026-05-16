FROM maven:3.9.11-eclipse-temurin-17 as maven

WORKDIR /app

COPY . .

RUN mvn -pl bot clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=maven /app/bot/target/*.jar bot.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "bot.jar"]