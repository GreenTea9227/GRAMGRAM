FROM gradle AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test


FROM eclipse-temurin:17-alpine

ARG JAR_FILE=/app/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar

ENV testPwd=1111

ARG pinpointPath=${pinpointPath}

COPY ${pinpointPath}/pinpoint-bootstrap-2.2.3-NCP-RC1.jar pinpoint-bootstrap-2.2.3-NCP-RC1.jar

ENTRYPOINT ["java","-javaagent:pinpoint-bootstrap-2.2.3-NCP-RC1.jar","-Dpinpoint.applicationName=likelike","-Dpinpoint.agentId=likelike","-Dspring.profiles.active=prod","-jar","/app.jar"]
