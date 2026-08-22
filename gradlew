#!/usr/bin/env sh
##############################################################################
# Gradle start up script for UN*X
# (This is a standard wrapper script placeholder. If you need the gradle
# wrapper JAR, either run `gradle wrapper --gradle-version 8.4` locally or
# ask me to add the wrapper JAR into the repo.)
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="-Xmx2048m"

CLASSPATH=""
APP_NAME="Gradle"

# Resolve links - $0 may be a link
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/"$link
  fi
done

PRG_DIR=`dirname "$PRG"`

# Default JAVA_HOME if not set
if [ -z "$JAVA_HOME" ] ; then
  JAVA_CMD=`which java 2>/dev/null`
else
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

if [ -z "$JAVA_CMD" ] ; then
  echo "ERROR: JAVA_HOME is not set and 'java' could not be found in your PATH." 1>&2
  exit 1
fi

# Locate the wrapper JAR
WRAPPER_JAR="$PRG_DIR/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_PROP="$PRG_DIR/gradle/wrapper/gradle-wrapper.properties"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "WARNING: gradle-wrapper.jar not found. Run 'gradle wrapper --gradle-version 8.4' locally or ask me to add the wrapper JAR." 1>&2
fi

exec "$JAVA_CMD" $DEFAULT_JVM_OPTS -jar "$WRAPPER_JAR" "$@"
