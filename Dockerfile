FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN ./mvnw clean package -DskipTests -Dcheckstyle.skip

FROM eclipse-temurin:21-jre-jammy AS runtime

RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /usr/share/man/* && \
    rm -rf /usr/share/doc/* && \
    find /var/log -type f -exec truncate --size 0 {} \; && \
    mkdir -p /jtc && \
    chmod -R 755 /jtc

RUN mkdir -p /jtc && \
    addgroup --system --gid 1001 apiuser && \
    adduser --system --uid 1001 --ingroup apiuser --no-create-home apiuser && \
    chown -R apiuser:apiuser /jtc

WORKDIR /jtc
USER apiuser

COPY --from=builder --chown=apiuser:apiuser /build/target/*.jar api.jar

RUN echo '#!/bin/bash\ncurl -f http://localhost:8080/actuator/health || exit 1' > /jtc/healthcheck.sh && \
    chmod +x /jtc/healthcheck.sh

ENV JAVA_OPTS="\
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75 \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/tmp \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD /jtc/healthcheck.sh

ENTRYPOINT exec java $JAVA_OPTS -jar api.jar