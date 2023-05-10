FROM gradle AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test


FROM eclipse-temurin:17-alpine

ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar

ENV testPwd=1111

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]