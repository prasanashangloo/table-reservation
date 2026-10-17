# Task 15 - Final Release, Documentation and Viva

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

## End-to-end demonstration script (about 10 minutes)
1. Show the board and `git log --oneline --graph` (planning + Git history).
2. Make a small change on a feature branch, push, open and merge the PR.
3. Show Jenkins start automatically (webhook/poll): Checkout -> Build -> Unit tests -> **Selenium gate** -> Package -> Docker build/push -> Docker deploy.
4. Open the deployed app; register, log in, check availability, book, confirm, cancel.
5. Show `/api/health` and the Docker Hub tag with the same build number.
6. Run `ansible-playbook site.yml` on the target node: show `changed=0` on the second run.
7. Break a release, show automatic rollback.
8. Show the failed-test pipeline evidence (from Task 10).

## Troubleshooting guide
| Symptom | Likely cause | Fix |
|---|---|---|
| `mvn verify` fails: cannot find Chrome | Chrome not installed or non-standard path | Install Chrome/Chromium, or `-Dchrome.bin=/path` |
| Selenium: `session not created` on CI | Missing flags in container/CI | Already set: `--headless=new --no-sandbox --disable-dev-shm-usage` |
| Jenkins cannot reach Docker | `jenkins` not in `docker` group | `usermod -aG docker jenkins`, restart Jenkins |
| Port 8080 in use | Jenkins and Tomcat both default to 8080 | Change one port (`--httpPort`, Tomcat `server.xml`) or use `SERVER_PORT` |
| App 404 on Tomcat | Wrong context path | WAR name is the context: `/reservation/` |
| Tomcat deploy ignores `APP_ENV` | `setenv.sh` not executable / Tomcat not restarted | `chmod +x`, run shutdown/startup |
| `docker push` denied | Wrong repo name or token | Check `DOCKER_REPO` and `dockerhub-creds` |
| Ansible `unreachable` | SSH key/user/inventory host wrong | `ansible -m ping`, check `ansible_user` |
| Ansible docker module missing | Collection not installed | `ansible-galaxy collection install -r requirements.yml` |
| Booking returns 409 | Slot already taken | Pick another slot/date |
| Login lost after restart | Sessions and the default H2 DB are in memory | Expected for the MVP; use the `mysql` profile for persistent data |

## Limitations
- Sessions are held in memory (lost on restart, not shared across instances).
- No roles: the booking owner confirms their own booking (stands in for restaurant staff).
- One restaurant, fixed tables and slots; no opening-hours or special-day rules.
- Default database is in-memory; MySQL profile is provided but not load-tested.
- No e-mail/SMS notifications, payments or rate limiting; HTTP only in the demo.
- Selenium tests need a Chrome install on the build agent.

## Future enhancement plan
1. Roles (diner, host, admin) with Spring Security + JWT; admin dashboard for tables/slots.
2. E-mail/SMS confirmations and reminders.
3. Waiting list and automatic re-offer of cancelled slots.
4. Persistent database with Flyway migrations; backup policy.
5. Observability: Actuator, Prometheus/Grafana, centralised logs.
6. Kubernetes or ECS deployment, blue/green or canary releases.
7. Security scanning in the pipeline (OWASP dependency check, Trivy, SonarQube).
8. Terraform to create the target infrastructure.

## Viva questions and model answers
**1. What is DevOps and which phases does this project cover?** A culture and set of practices joining development and operations through automation: plan, code, build, test, release, deploy, operate, monitor. This project automates all of them (Git, Jenkins, Selenium, Docker, Ansible).

**2. Difference between Continuous Integration, Delivery and Deployment?** CI merges and tests every commit automatically. Delivery keeps every build releasable. Deployment pushes every passing build to an environment automatically - our Docker stage does the last.

**3. Why a Jenkinsfile instead of a freestyle job?** Pipeline as code is versioned, reviewable, reproducible and supports stages, parameters and conditions.

**4. How does a failed Selenium test stop deployment?** Tests run in an earlier stage; a non-zero Maven exit fails the stage and Jenkins skips all later stages (Package/Deploy).

**5. Why use `git merge --no-ff`?** It keeps an explicit merge commit, so feature history stays visible in `git log --graph`.

**6. How did you resolve the merge conflict?** Two branches changed the same `<title>` line; Git inserted `<<<<<<<`, `=======`, `>>>>>>>` markers; I edited to the desired line, `git add`, and committed.

**7. Image vs container?** An image is an immutable template; a container is a running (or stopped) instance with a writable layer.

**8. Why a non-root user and HEALTHCHECK in the Dockerfile?** Least privilege reduces damage if compromised; HEALTHCHECK lets Docker/orchestrators detect an unhealthy app.

**9. What is idempotency and how did you prove it?** Running automation repeatedly gives the same end state without extra changes. The second `ansible-playbook` run reported `changed=0`.

**10. How does rollback work?** `deploy.yml` stores the current tag, deploys the new one, health-checks it; on failure the `rescue` block restarts the previous tag. `rollback.yml` does it manually.

**11. Why Ansible over Puppet?** Agentless (SSH), simple YAML, push-based; Puppet is agent/pull with its own DSL and suits long-lived large fleets. Both are declarative.

**12. How do you prevent double-bookings?** The service checks `existsByTableIdAndBookingDateAndSlotAndStatusNot(..., CANCELLED)` inside a transaction and returns 409; the API test proves it. For high concurrency, add a database unique constraint on table/date/slot for non-cancelled rows.

**13. How are passwords protected?** BCrypt hashes with a per-password salt; plaintext is never stored.

**14. Why WAR and JAR both?** The WAR deploys to external Tomcat (task 8) and, with the Spring Boot plugin, also runs with `java -jar` inside Docker (task 11).

**15. What would you improve first?** Persistent sessions/JWT with roles and a DB uniqueness constraint, then notifications and monitoring.
