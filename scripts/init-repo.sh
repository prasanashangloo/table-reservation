#!/usr/bin/env bash
# Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
# Task 4 - creates the local repository with MEANINGFUL initial commits, then shows how to publish it.
# Run once from the project root:  bash scripts/init-repo.sh
set -euo pipefail
git init -b main
git config user.name  "${GIT_AUTHOR_NAME:-Prasana Shangloo}"
git config user.email "${GIT_AUTHOR_EMAIL:?Set GIT_AUTHOR_EMAIL to your GitHub e-mail, e.g. export GIT_AUTHOR_EMAIL=you@example.com}"

git add .gitignore LICENSE AUTHORS CITATION.cff README.md CONTRIBUTING.md
git commit -m "docs: add README, license, contributing guide and gitignore"

git add .github
git commit -m "chore: add issue templates, PR template and CODEOWNERS"

git add pom.xml src/main
git commit -m "feat: add Spring Boot application skeleton with booking API and UI"

git add src/test
git commit -m "test: add API tests and Selenium user journeys"

git add Jenkinsfile Dockerfile .dockerignore docker-compose.yml
git commit -m "ci: add Jenkins pipeline, Dockerfile and compose file"

git add ansible puppet Vagrantfile
git commit -m "chore: add Ansible playbooks, Puppet manifest and Vagrant test node"

git add docs scripts evidence
git commit -m "docs: add project documents, evidence folders and helper scripts"

git log --oneline
echo
echo "Next: create an EMPTY repository named table-reservation on GitHub, then run:"
echo "  git remote add origin https://github.com/<your-username>/table-reservation.git"
echo "  git push -u origin main"
echo "  git checkout -b develop && git push -u origin develop"
