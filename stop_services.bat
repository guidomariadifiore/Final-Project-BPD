@echo off
echo =========================================
echo Stopping Final Project BPD Services...
echo =========================================

:: We use /T to kill the child java.exe/mvn.cmd processes attached to these windows
taskkill /F /T /FI "WINDOWTITLE eq BPD-UserService*" >nul 2>&1
taskkill /F /T /FI "WINDOWTITLE eq BPD-ZonesService*" >nul 2>&1
taskkill /F /T /FI "WINDOWTITLE eq BPD-PostingService*" >nul 2>&1
taskkill /F /T /FI "WINDOWTITLE eq BPD-CamundaApp*" >nul 2>&1
taskkill /F /T /FI "WINDOWTITLE eq BPD-BillpostingAPI*" >nul 2>&1

echo All 5 services have been successfully stopped!
echo.
