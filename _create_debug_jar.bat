@echo off
cd %~dp0

echo �f�o�b�O�r���h
call gradlew.bat :billing:assembleDebug

echo ���ʕ��̎��W
call gradlew.bat :billing:createDebugJar
