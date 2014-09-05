@echo off 

SET ANT_HOME=%CD%\apache-ant-1.9.3

call apache-ant-1.9.3/bin/ant.bat -f "..\QueueIT.Security" clean jar

IF %ERRORLEVEL% NEQ 0 GOTO Error

xcopy ..\QueueIT.Security\dist\QueueIt.Security.jar /Y
IF %ERRORLEVEL% NEQ 0 GOTO Error

GOTO End

:Error
echo ###########################################
echo # ERROR OCCURED WHILE COMPILING / TESTING #
echo ###########################################

:End