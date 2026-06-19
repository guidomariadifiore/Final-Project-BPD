@echo off
echo Starting Final Project BPD Services...

echo [1/5] Starting User Service...
start "BPD-UserService" cmd /c "cd Final-project-services && java -jar user-service.jar"

echo [2/5] Starting Zones Service...
start "BPD-ZonesService" cmd /c "cd Final-project-services && java -jar zones-service.jar"

echo [3/5] Starting Posting Service...
start "BPD-PostingService" cmd /c "cd Final-project-services && java -jar posting-service.jar"

echo [4/5] Starting Camunda Engine...
start "BPD-CamundaApp" cmd /c "cd Camunda_engine && mvn spring-boot:run"

echo [5/5] Starting Billposting API...
start "BPD-BillpostingAPI" cmd /c "cd billpostingapi && mvn spring-boot:run"

echo.
echo All 5 services have been launched in their own terminal windows!
echo Please wait a few seconds for the Spring Boot applications to finish starting.
echo.
