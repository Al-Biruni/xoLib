FROM gradle:jdk10 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:10-jre-slim
WORKDIR /app
EXPOSE 6500


COPY /home/gradle/src/easytext.web/build/distributions/easytext.web.tar /app/
WORKDIR /app



RUN java:8 java -jar
