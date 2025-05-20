FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

RUN apt-get update && apt-get install -y findutils

COPY gradlew build.gradle settings.gradle gradle /app/
RUN chmod +x gradlew

COPY . .

RUN ./gradlew build -x test --no-daemon

# 2단계: 실행
FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]