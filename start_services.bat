@echo off
echo =========================================
echo Starting Final Project BPD Services...
echo =========================================

echo [1/5] Starting User Service (Legacy)...
start "BPD-UserService" cmd /c "cd Final-project-services && java -jar user-service.jar"

echo [2/5] Starting Zones Service (Legacy)...
start "BPD-ZonesService" cmd /c "cd Final-project-services && java -jar zones-service.jar"

echo [3/5] Starting Posting Service (Legacy)...
start "BPD-PostingService" cmd /c "cd Final-project-services && java -jar posting-service.jar"

echo [4/5] Starting Camunda Engine (Spring Boot)...
start "BPD-CamundaApp" cmd /c "cd camundaengineapp1 && mvn spring-boot:run"

echo [5/5] Starting Billposting API (Spring Boot)...
start "BPD-BillpostingAPI" cmd /c "cd billpostingapi && mvn spring-boot:run"

echo.
echo All 5 services have been launched in their own terminal windows!
echo Please wait a few seconds for the Spring Boot applications to finish starting.
echo.
