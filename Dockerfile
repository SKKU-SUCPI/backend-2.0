# Java 21 이미지 사용
FROM openjdk:21-jdk

ENV APP_HOME=/app
WORKDIR $APP_HOME

# 로컬에서 이미 빌드된 jar 파일 경로를 복사
ARG JAR_FILE=build/libs/sucpi-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]