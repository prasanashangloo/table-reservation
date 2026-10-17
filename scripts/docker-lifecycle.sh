#!/usr/bin/env bash
# Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
# Task 11 - runs the complete container lifecycle and saves the output as the "Docker command log".
# Prerequisite: mvn -B clean package -DskipTests   (creates target/reservation.war)
set -uo pipefail
LOG="evidence/task-11/docker-command-log.txt"
IMG="table-reservation"; TAG="${1:-1.0.0}"; NAME="table-reservation"; PORT="${2:-8081}"
mkdir -p "$(dirname "$LOG")"
run() { echo "$ $*" | tee -a "$LOG"; "$@" 2>&1 | tee -a "$LOG"; echo | tee -a "$LOG"; }
: > "$LOG"
echo "Docker lifecycle log - $(date) - Prasana Shangloo (23102A0076)" | tee -a "$LOG"
run docker build --build-arg VERSION="$TAG" -t "$IMG:$TAG" -t "$IMG:latest" .
run docker images "$IMG"
run docker inspect --format 'Image {{.RepoTags}} size={{.Size}} created={{.Created}} user={{.Config.User}} ports={{.Config.ExposedPorts}}' "$IMG:$TAG"
run docker rm -f "$NAME"
run docker run -d --name "$NAME" -p "$PORT:8080" -e APP_ENV=dev "$IMG:$TAG"
sleep 25
run docker ps --filter "name=$NAME"
run docker logs --tail 20 "$NAME"
run curl -s "http://localhost:$PORT/api/health"
run docker inspect --format 'health={{.State.Health.Status}}' "$NAME"
run docker stop "$NAME"
run docker ps -a --filter "name=$NAME"
run docker start "$NAME"
run docker restart "$NAME"
sleep 20
run curl -s "http://localhost:$PORT/api/health"
run docker stop "$NAME"
run docker rm "$NAME"
run docker ps -a --filter "name=$NAME"
echo "Log saved to $LOG (image $IMG:$TAG left in place; remove with: docker rmi $IMG:$TAG $IMG:latest)"
