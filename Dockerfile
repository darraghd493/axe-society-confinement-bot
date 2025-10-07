FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*-all.jar app.jar
COPY .env .env
CMD ["java", "-jar", "app.jar"]
