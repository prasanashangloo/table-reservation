# Tasks 11 and 12 - Docker and Jenkins-Docker CD

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

## Task 11 - Image and container lifecycle
To run everything below and save the output automatically: `bash scripts/docker-lifecycle.sh` (writes `evidence/task-11/docker-command-log.txt`).

```bash
mvn -B clean package -DskipTests
docker build --build-arg VERSION=1.0.0 -t table-reservation:1.0.0 .
docker images table-reservation
docker run -d --name table-reservation -p 8081:8080 -e APP_ENV=dev table-reservation:1.0.0
docker ps
docker logs -f table-reservation          # Ctrl+C to leave
curl http://localhost:8081/api/health
docker inspect --format '{{.State.Health.Status}}' table-reservation
docker stop table-reservation
docker start table-reservation            # restart
docker restart table-reservation
docker stop table-reservation && docker rm table-reservation
docker rmi table-reservation:1.0.0
```
Lifecycle: **Dockerfile -> build -> image -> run -> container (running) -> stop (exited) -> start/restart -> rm -> image remains until rmi**. Save the terminal output as the "Docker command log".

Image details: base `eclipse-temurin:17-jre-alpine`, non-root user `app`, port 8080, `HEALTHCHECK` on `/api/health`, `APP_ENV` configurable.

## Task 12 - Jenkins to Docker Hub to container
1. Create a Docker Hub repository `<user>/table-reservation` and an access token.
2. Jenkins: *Credentials -> Add* "Username with password", ID `dockerhub-creds` (token as password).
3. Add the `jenkins` user to the docker group: `sudo usermod -aG docker jenkins && sudo systemctl restart jenkins`.
4. Run the pipeline with `DEPLOY_MODE=docker`, `DOCKER_REPO=<user>/table-reservation`, `HOST_PORT=8081`.

Stages run only after the Selenium gate passes: **Docker Build & Push** (tags `:<BUILD_NUMBER>` and `:latest`) -> **Docker Deploy** (removes the old container, starts the new one, health-checks it).

Evidence: Docker Hub tag list showing the build number, console log of push, `docker ps` output, and a commit -> container timeline (commit hash in `git log`, Jenkins build #, image tag).
