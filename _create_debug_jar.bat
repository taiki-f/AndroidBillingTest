@echo off
cd %~dp0

echo デバッグビルド
call gradlew.bat :billing:assembleDebug

echo 成果物の収集
call gradlew.bat :billing:createDebugJar
