# Demo Video Script (Task 15) - about 10 minutes

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

Record the screen with OBS Studio or the built-in recorder (Win+G / QuickTime). Speak over it or add captions. Keep terminal font large.

| Time | Screen | Say / do | Evidence it proves |
|---|---|---|---|
| 0:00 | Title slide | Name, project, what the system does | - |
| 0:30 | GitHub Project board | Three sprints, 15 cards in Done | Tasks 1-2 |
| 1:15 | Terminal: `git log --oneline --graph --decorate` | Feature branches, PR merges, merge conflict commit, tag `v1.0.0` | Tasks 4-6 |
| 2:00 | Edit a line, commit on `feature/demo-change`, push, open PR, merge | Reviewed merge into `develop` | Task 5 |
| 3:00 | Jenkins starts automatically | Stage view: Checkout, Build, Unit tests, Selenium gate, Package, Docker build/push, Docker deploy | Tasks 7, 8, 10, 12 |
| 4:30 | Jenkins Test Result page | 5 Selenium journeys + API tests passed; archived WAR | Tasks 9-10 |
| 5:15 | Browser to the deployed app | Register, log in, check availability, book (PENDING), confirm, cancel | MVP |
| 6:30 | Terminal: `scripts/smoke-test.sh http://localhost:8081` | Double booking returns 409 | O1 |
| 7:00 | Docker Hub tags + `docker ps` | Image tag equals Jenkins build number | Tasks 11-12 |
| 7:45 | Terminal: `ansible-playbook -i inventory.ini site.yml` twice | Second recap shows `changed=0` | Tasks 13-14 |
| 8:45 | Deploy a deliberately broken tag | Health check fails, previous tag restored, `curl /api/health` still UP | Task 14 |
| 9:30 | Failed-pipeline evidence from Task 10 | Defect, failed gate, fix commit, green rerun | Task 10 |
| 9:50 | Closing slide | Limitations and future work | Task 15 |

## Pre-recording checklist
- [ ] `mvn clean verify` passes locally
- [ ] Jenkins job works end to end once before recording
- [ ] Docker Hub repo and `dockerhub-creds` configured
- [ ] Ansible target node reachable (`ansible -m ping`)
- [ ] A broken image tag prepared for the rollback demo
- [ ] Browser zoom 125%, notifications off
