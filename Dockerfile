FROM gradle AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:17-alpine

ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar

ENV testPwd=1111

ENV pinpointPath=/data/pinpoint-agent-2.2.3-NCP-RC1

ENTRYPOINT ["java","-javaagent:/data/pinpoint-agent-2.2.3-NCP-RC1/pinpoint-bootstrap-2.2.3-NCP-RC1.jar","-Dpinpoint.applicationName=likelike","-Dpinpoint.agentId=likelike","-Dspring.profiles.active=prod","-jar","/app.jar"]
