FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/account-balance-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
