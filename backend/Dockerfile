FROM openjdk:latest

COPY  webapp-backend/target/webapp-backend.jar .

RUN chmod +x webapp-backend.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","webapp-backend.jar"]
