@echo off
cd %~dp0

echo �����[�X�r���h
call gradlew.bat :billing:assembleRelease

echo ���ʕ��̎��W
call gradlew.bat :billing:createReleaseJar
