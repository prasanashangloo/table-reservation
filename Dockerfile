# Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
# Task 11 - Container image for the Table Reservation Platform.
# Expects the WAR to be built first:  mvn -B clean package -DskipTests
FROM eclipse-temurin:17-jre-alpine

ARG VERSION=dev
LABEL org.opencontainers.image.title="table-reservation" \
      org.opencontainers.image.authors="Prasana Shangloo (23102A0076)" \
      org.opencontainers.image.vendor="Prasana Shangloo" \
      org.opencontainers.image.version="${VERSION}"

ENV APP_ENV=dev \
    SERVER_PORT=8080

RUN addgroup -S app && adduser -S app -G app
WORKDIR /opt/app
COPY target/reservation.war app.war
USER app

EXPOSE 8080
HEALTHCHECK --interval=15s --timeout=3s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.war"]
