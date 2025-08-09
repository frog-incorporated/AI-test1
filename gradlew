#!/usr/bin/env sh
# Lightweight Gradle wrapper script.
# This script delegates execution to the `gradle` command installed on the
# system. It is provided so that CI workflows invoking `./gradlew` will
# continue to work even though the standard Gradle wrapper JAR is not
# included in this repository.

if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
else
  echo "Error: gradle command not found. Please install Gradle or use your own wrapper." >&2
  exit 1
fi