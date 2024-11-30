# Build the JAR
FROM maven:3.6.3-openjdk-17 as build
WORKDIR /workspace/app

COPY . /workspace/app
RUN mvn clean install

# Copy the JAR and run it
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /workspace/app/target/product-calculator-1.0.0.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]