#!/bin/sh

BUILD_VERSION=$(cat ./build.gradle.kts | sed -En 's/^version = "(.+)"$/\1/p')
GIT_COMMIT_ID="$(git rev-parse --short=8 HEAD)"
COMMIT_USER="$(git log -1 --pretty=format:'%an')"
COMMIT_MESSAGE="$(git log -1 --pretty=format:'%s')"

echo "Exporting the following properties:"
echo ""
echo "BUILD_VERSION=$BUILD_VERSION"
echo "BUILD_DATE=\"$(date +'%Y-%m-%d %T')\""
echo "GIT_COMMIT_ID=$GIT_COMMIT_ID"
echo "CURRENT_VERSION=$BUILD_VERSION-$GIT_COMMIT_ID"
echo "COMMIT_USER=\"$COMMIT_USER\""
echo "COMMIT_MESSAGE=\"$COMMIT_MESSAGE\""

{
    echo "export BUILD_VERSION=$BUILD_VERSION"
    echo "export BUILD_DATE=\"$(date +'%Y-%m-%d %T')\"";
    echo "export GIT_COMMIT_ID=$GIT_COMMIT_ID"
    echo "export CURRENT_VERSION=$BUILD_VERSION-$GIT_COMMIT_ID"
    echo "export COMMIT_USER=\"$COMMIT_USER\""
    echo "export COMMIT_MESSAGE=\"$COMMIT_MESSAGE\""
} >> "$BASH_ENV"
