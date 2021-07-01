FROM openjdk:12-alpine
COPY build/libs/*.jar app.jar
EXPOSE 8085
CMD ["java","-jar","/app.jar"]