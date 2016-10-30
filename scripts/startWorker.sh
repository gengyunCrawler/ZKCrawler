#!/bin/sh
#
#################################

echo "Starting ZKCrawler-Worker ......"

if [ "$JAVA_HOME" != "" ];then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA=java
fi

echo "ENV: $JAVA"

JAR_FILE=ZKCrawler-Worker-1.0-SNAPSHOT.jar

echo "jar: $JAR_FILE"

$JAVA -Dfile.encoding=UTF-8  -jar $JAR_FILE >& out_$JAR_FILE.TXT &


echo "ZKCrawler-Worker Started."