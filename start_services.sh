#!/bin/bash

echo "========================================="
echo "Starting Final Project BPD Services..."
echo "========================================="

echo "[1/5] Starting User Service (Legacy)..."
cd Final-project-services
nohup java -jar user-service.jar > user-service.log 2>&1 &
echo $! > user-service.pid
cd ..

echo "[2/5] Starting Zones Service (Legacy)..."
cd Final-project-services
nohup java -jar zones-service.jar > zones-service.log 2>&1 &
echo $! > zones-service.pid
cd ..

echo "[3/5] Starting Posting Service (Legacy)..."
cd Final-project-services
nohup java -jar posting-service.jar > posting-service.log 2>&1 &
echo $! > posting-service.pid
cd ..

echo "[4/5] Starting Camunda Engine (Spring Boot)..."
cd camundaengineapp1
nohup mvn spring-boot:run > camunda-engine.log 2>&1 &
echo $! > camunda-engine.pid
cd ..

echo "[5/5] Starting Billposting API (Spring Boot)..."
cd billpostingapi
nohup mvn spring-boot:run > billposting-api.log 2>&1 &
echo $! > billposting-api.pid
cd ..

echo ""
echo "All 5 services have been launched in the background!"
echo "You can check the .log files in each respective directory to see their console outputs."
echo ""
