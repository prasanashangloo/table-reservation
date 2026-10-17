# Task board (Kanban) - 15-task plan

> Owner / Author: **Prasana Shangloo** (23102A0076)

Import `docs/backlog.csv` into a GitHub Project (Table view) or move these cards by hand. "Done" means the Definition of Done in `02-agile-and-devops-plan.md` is met **and** the evidence exists in `evidence/task-NN/`.

| Backlog | Ready | In progress | In review (PR) | Testing (Jenkins) | Done |
|---|---|---|---|---|---|
| T11 Dockerfile and lifecycle | T7 Jenkins CI job | | | | T1 Problem and scope (document) |
| T12 Jenkins to Docker CD | T8 Jenkinsfile and deploy | | | | T2 Agile plan (document) |
| T13 Ansible provisioning | T10 Selenium gate in Jenkins | | | | T3 Architecture and setup (document) |
| T14 Idempotency and rollback | | | | | T4 Repo initialisation (files ready) |
| T15 Final release and viva | | | | T9 Selenium tests (written; confirm with mvn verify) | |

Cards for Tasks 5 and 6 move to Done only after the pull request, merge conflict and `v1.0.0` tag exist on GitHub.

## Sprint plan
| Sprint | Goal | Tasks |
|---|---|---|
| 1 Foundation | Scope, plan, design, repo, first feature | 1-5 |
| 2 Build and CI | Complete MVP, Jenkins CI, pipeline, tests | 6-10 |
| 3 Ship and operate | Docker, CD, configuration management, release | 11-15 |
