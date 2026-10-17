# Automated Restaurant Table Reservation Platform

**Owner / Author:** Prasana Shangloo (23102A0076)  
**License:** MIT, Copyright (c) 2026 Prasana Shangloo

A small Spring Boot application plus a complete DevOps workflow: Git/GitHub -> Jenkins CI/CD -> Selenium quality gate -> Docker -> Ansible provisioning with health check and rollback.

**MVP features:** user registration and login, availability/slot view, booking request, confirmation, cancellation, status tracking.

## Quick start
```bash
# prerequisites: JDK 17, Maven 3.9+, Chrome (for Selenium)
mvn clean verify                    # unit/API tests + Selenium journeys
java -jar target/reservation.war    # http://localhost:8080
curl http://localhost:8080/api/health
```
Docker: `docker build -t table-reservation . && docker run -p 8081:8080 table-reservation`

## Repository layout
```
src/main/java/...      Spring Boot app (web / service / repo / model)
src/main/resources/    application.properties, static UI (index.html)
src/test/java/...      API tests (*Test) and Selenium journeys (*IT)
Jenkinsfile            Pipeline: build, test gate, package, Tomcat or Docker deploy
Dockerfile             Container image
ansible/               Inventory, provision.yml, deploy.yml (auto-rollback), rollback.yml
puppet/reservation.pp  Puppet alternative to the Ansible provisioning
docker-compose.yml     Optional app + MySQL stack
docs/                  One document per task group
scripts/git-demo.sh    Reproducible branch / conflict / tag demo
scripts/smoke-test.sh  End-to-end API check against a running instance
scripts/init-repo.sh   Task 4: creates the repo with meaningful initial commits
scripts/docker-lifecycle.sh  Task 11: runs and logs the container lifecycle
Vagrantfile            Task 14: clean Ubuntu node for Ansible
evidence/              One folder per task: exact deliverable + what to capture
CONTRIBUTING.md        Branch naming and commit rules
docs/00-traceability-matrix.md  Every requirement, where it is met, what remains
docs/diagrams/         PNG + Graphviz sources: lifecycle, architecture, use case, ERD, states, pipeline
.github/               Issue and PR templates
```

## Task to deliverable map
| # | Task | Where | Evidence you still capture |
|---|---|---|---|
| 1 | Problem definition and scope | `docs/01-problem-and-scope.md` | Instructor approval |
| 2 | Agile planning and DevOps workflow | `docs/02-agile-and-devops-plan.md` | GitHub Project board screenshot |
| 3 | Requirements, architecture, setup | `docs/03-requirements-and-architecture.md` | Local app screenshot, `mvn package` log |
| 4 | GitHub repo initialisation | `docs/04-git-workflow.md`, `.github/` | Repo URL, initial commits, issues |
| 5 | Feature branch and PR | `docs/04-git-workflow.md` | PR link, review comments, merge |
| 6 | MVP completion, conflict, tag | `docs/04-git-workflow.md`, `scripts/git-demo.sh` | Conflict screenshot, `v1.0.0` tag |
| 7 | Jenkins CI job | `docs/05-jenkins-setup.md` | Build log, trigger, archived WAR |
| 8 | Pipeline as code and deploy | `Jenkinsfile`, `docs/05-jenkins-setup.md` | Pipeline run, deployed URL screenshot |
| 9 | Selenium tests | `src/test/.../selenium`, `docs/06-test-plan.md` | Local report, failure screenshot |
| 10 | Continuous testing in Jenkins | `Jenkinsfile`, `docs/05-jenkins-setup.md` | Failed run, fix commit, green rerun |
| 11 | Docker image and lifecycle | `Dockerfile`, `docs/07-docker.md` | Command log, running container |
| 12 | Jenkins-Docker CD | `Jenkinsfile`, `docs/07-docker.md` | Docker Hub tag, pipeline log |
| 13 | Configuration management | `ansible/provision.yml`, `docs/08-...` | First execution log |
| 14 | Provisioning and reliability | `ansible/deploy.yml`, `rollback.yml` | `changed=0` recap, health check, rollback demo |
| 15 | Final release, docs, viva | `docs/09-final-release-and-viva.md` | Demo video, presentation |

## Status of this codebase
The source, tests, Jenkinsfile, Dockerfile and playbooks were written but **not compiled or executed** in the authoring environment (no Maven/Internet access there). Run `mvn clean verify` first and fix anything environment-specific (Java/Chrome versions); then work through the tasks in order and capture the evidence listed above.
- Video: see docs/10-demo-video-script.md

## Ownership
All code, documentation and configuration in this repository are the work of **Prasana Shangloo (23102A0076)**, who is the owner and maintainer. Set your Git identity before committing so history shows your name:
```bash
git config --global user.name "Prasana Shangloo"
git config --global user.email "you@example.com"   # use your GitHub e-mail
```

## User interface
Single-page UI with a table-and-seats motif, responsive layout, dark mode, keyboard focus styles and reduced-motion support. All element IDs used by the Selenium tests are unchanged. Layout previews are in `docs/ui-preview/` (rendered against a stand-in API, so replace them with real screenshots of your running app for Task 3).

