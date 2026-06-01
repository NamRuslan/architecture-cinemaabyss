FROM gradle AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN apt-get update && \
    apt-get install -y openjdk-21-jdk && \
    apt-get clean
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

RUN gradle clean build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "-Dserver.port=8082", "app.jar"]
