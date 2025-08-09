@echo off
REM Lightweight Gradle wrapper script for Windows.
where gradle >NUL 2>&1
IF %ERRORLEVEL% EQU 0 (
  gradle %*
) ELSE (
  echo Error: gradle command not found. Please install Gradle or use your own wrapper.
  exit /B 1
)