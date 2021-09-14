#!/bin/bash

set -eo pipefail

# typically the directory where the Dockerfile resides
# NOTE: Everything in there possibly increases the final image size, so create a
# .dockerignore for unnecessary files
DOCKER_CONTEXT=$1
APP=$2
IMAGE_NAME=$3

if [[ -z "${REGISTRY_TOKEN}" ]]; then
    echo "Missing environment: DOCKER_USER" >&2
    exit 1
fi

if [[ -z "${REGISTRY_TOKEN_PW}" ]]; then
    echo "Missing environment: DOCKER_PW" >&2
    exit 1
fi

if [[ -z "${CONTAINER_REGISTRY}" ]]; then
    echo "Missing environment: DOCKER_REGISTRY" >&2
    exit 1
fi

if [[ -z "${APP}" ]]; then
    echo "APP name is missing" >&2
    exit 1
fi

if [[ -z "${DOCKER_CONTEXT}" ]]; then
    echo "No docker context given" >&2
    exit 1
fi

if [[ -z "${IMAGE_NAME}" ]]; then
    echo "No image name given" >&2
    exit 1
fi

export CONTAINER_REPOSITORY=${CONTAINER_REGISTRY}/${APP}
echo "Using repository ${CONTAINER_REPOSITORY}"

export CONTAINER_IMAGE="${CONTAINER_REPOSITORY}/${IMAGE_NAME}:${CURRENT_VERSION}"

echo "Logging in into target registry ..."
echo "${REGISTRY_TOKEN_PW}" |
    docker login -u "${REGISTRY_TOKEN}" --password-stdin "${CONTAINER_REGISTRY}"

echo "Building Docker image '${CONTAINER_IMAGE}' ..."

docker build -t "${CONTAINER_IMAGE}" \
    --label "org.label-schema.build-date=${BUILD_DATE}" \
    --label "org.label-schema.name=${APP}" \
    --label "org.label-schema.url=${CONTAINER_REPOSITORY}" \
    --label "org.label-schema.vcs-ref=${GIT_COMMIT_ID}" \
    --label "org.label-schema.version=${CURRENT_VERSION}" \
    --label "org.label-schema.docker.cmd=docker run ${CONTAINER_IMAGE}" \
    --build-arg CURRENT_VERSION \
    "${DOCKER_CONTEXT}"

echo "Pushing Docker image '${CONTAINER_IMAGE}' ..."
docker push "${CONTAINER_IMAGE}"
