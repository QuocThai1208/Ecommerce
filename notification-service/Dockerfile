# Stage 1: Build
FROM bellsoft/liberica-openjdk-alpine:25 AS build
WORKDIR /app

# Cài đặt maven
RUN apk add --no-cache maven

# Copy pom và download dependency
COPY pom.xml .
RUN mvn dependency:go-offline

# Build code
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM bellsoft/liberica-openjdk-alpine:25
WORKDIR /app

COPY --from=build /app/target/notification-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]