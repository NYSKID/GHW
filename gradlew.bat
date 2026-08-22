@echo off
REM Gradle start up script for Windows (cmd)
REM Placeholder: the gradle-wrapper.jar must exist under gradle\wrapper\gradle-wrapper.jar
SETLOCAL
SET DEFAULT_JVM_OPTS=-Xmx2048m
IF NOT DEFINED JAVA_HOME (
  where java >nul 2>&1
  IF ERRORLEVEL 1 (
    ECHO ERROR: JAVA_HOME is not set and 'java' is not in your PATH.
    EXIT /B 1
  )
  SET JAVA_CMD=java
) ELSE (
  SET JAVA_CMD=%JAVA_HOME%\bin\java
)

SET WRAPPER_JAR=%~dp0\gradle\wrapper\gradle-wrapper.jar
IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO WARNING: gradle-wrapper.jar not found. Run 'gradle wrapper --gradle-version 8.4' locally or ask me to add the wrapper JAR.
)

"%JAVA_CMD%" %DEFAULT_JVM_OPTS% -jar "%WRAPPER_JAR%" %*
