FROM maven:3.9-eclipse-temurin-21-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/imovel-1.0-SNAPSHOT.jar"]
