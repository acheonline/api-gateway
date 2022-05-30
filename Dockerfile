FROM amazoncorretto:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} api-gateway-0.0.2-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/api-gateway-0.0.2-SNAPSHOT.jar"]