FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY libs/xoLib.jar libs/xoLib.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]