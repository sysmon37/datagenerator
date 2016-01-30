@echo off
if "%1" == "" (
  echo No parameters provided -- usage: %0 config_file
) else (
  java -jar DataGenSW.jar -config %1
)