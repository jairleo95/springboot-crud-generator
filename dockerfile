FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=crud-generator-1.0.jar
ADD ${JAR_FILE} app.jar
COPY config.json /config/
ENTRYPOINT ["java","-jar","/app.jar"]