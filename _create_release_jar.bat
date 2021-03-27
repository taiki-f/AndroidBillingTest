@echo off
cd %~dp0

echo リリースビルド
call gradlew.bat :billing:assembleRelease

echo 成果物の収集
call gradlew.bat :billing:createReleaseJar
