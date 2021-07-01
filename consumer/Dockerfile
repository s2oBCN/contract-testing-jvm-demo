FROM openjdk:12-alpine

COPY build/libs/*.jar app.jar

COPY wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh

EXPOSE 8085

CMD ["java","-jar","/app.jar"]