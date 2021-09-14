#! /bin/sh

exec java -Duser.timezone=Europe/Berlin \
    $JAVA_OPTS \
    -jar $JAR_FILE
