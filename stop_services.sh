#!/bin/bash

echo "========================================="
echo "Stopping Final Project BPD Services..."
echo "========================================="

kill_pid() {
    if [ -f "$1" ]; then
        PID=$(cat "$1")
        echo "Killing process $PID ($1)..."
        # Kill the process and its children (process group)
        kill -- -$PID 2>/dev/null || kill $PID 2>/dev/null
        rm "$1"
    fi
}

# Kill processes via their tracked PID files
kill_pid "Final-project-services/user-service.pid"
kill_pid "Final-project-services/zones-service.pid"
kill_pid "Final-project-services/posting-service.pid"
kill_pid "camundaengineapp1/camunda-engine.pid"
kill_pid "billpostingapi/billposting-api.pid"

# Force cleanup any lingering processes just in case the PID tracking missed a child Java process
echo "Cleaning up any dangling service processes..."
pkill -f "user-service.jar"
pkill -f "zones-service.jar"
pkill -f "posting-service.jar"
pkill -f "camundaengineapp1"
pkill -f "billpostingapi"

echo "All services successfully stopped!"
echo ""
