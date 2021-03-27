@echo off
cd %~dp0

echo プロジェクトのクリーン
call gradlew.bat clean
