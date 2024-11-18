FROM openjdk:17
WORKDIR /app
COPY target/*.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080

