#Windows
# --- Build Stage ---
FROM maven:3.9.11-eclipse-temurin-24 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# --- Runtime Stage ---
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY .env .env 
EXPOSE 9090
ENTRYPOINT ["java","-jar","app.jar"]

#Linux
# # --- Build Stage ---
# FROM maven:3.9.8-eclipse-temurin-17 AS build
# WORKDIR /app

# # Copy pom first (to leverage Docker cache for dependencies)
# COPY pom.xml .

# # Copy source code
# COPY src ./src

# # Build without running tests
# RUN mvn -B clean package -DskipTests

# # --- Runtime Stage ---
# FROM eclipse-temurin:17-jdk AS runtime
# WORKDIR /app

# # Copy only the fat jar from build stage
# COPY --from=build /app/target/*.jar app.jar
# COPY .env .env
# #EXPOSE 9090
# ENTRYPOINT ["java", "-jar", "app.jar"]
