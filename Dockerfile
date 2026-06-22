FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -q -Dmaven.test.skip=true package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY agent/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar

COPY --from=build /src/target/otel-springboot-demo-1.0.0.jar app.jar

ENV JAVA_TOOL_OPTIONS="-javaagent:/otel/opentelemetry-javaagent.jar"
ENV OTEL_SERVICE_NAME="springboot-java21-demo"
ENV OTEL_EXPORTER_OTLP_ENDPOINT="http://alloy:4318"
ENV OTEL_EXPORTER_OTLP_PROTOCOL="http/protobuf"
ENV OTEL_TRACES_EXPORTER="otlp"
ENV OTEL_METRICS_EXPORTER="none"
ENV OTEL_LOGS_EXPORTER="none"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]