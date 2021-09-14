ARG BASE_IMAGE=openjdk:12-alpine
ARG CURRENT_VERSION=local
ARG STAGE=local
ARG JAR_FILE='service.jar'

FROM ${BASE_IMAGE}

ENV TZ 'Europe/Berlin'

ARG JAR_FILE
ENV JAR_FILE ${JAR_FILE}

COPY docker-entrypoint.sh .

ENV JAVA_OPTS ${JAVA_OPTS}
ARG CURRENT_VERSION
ENV CURRENT_VERSION ${CURRENT_VERSION}

COPY build/libs/*.jar ./${JAR_FILE}

ENTRYPOINT ["./docker-entrypoint.sh"]
