#!/usr/bin/env bash
# Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
# Tasks 4-6: reproducible Git evidence (feature branch, PR flow, conflict, tag).
# Run inside your cloned repo AFTER creating the GitHub repo. Read it first - it makes real commits.
set -euo pipefail

git checkout -b develop 2>/dev/null || git checkout develop
git push -u origin develop

# ---- Task 5: feature branch + PR ----
git checkout -b feature/user-registration
echo "// registration touch-up" >> src/main/java/com/example/reservation/web/UserController.java
git add -A && git commit -m "feat(auth): tidy registration controller"
git push -u origin feature/user-registration
echo ">> Now open a Pull Request on GitHub: feature/user-registration -> develop, review, and merge."
echo ">> Then:  git checkout develop && git pull && git log --oneline --graph --decorate"

# ---- Task 6: deliberate merge conflict ----
git checkout develop && git pull
git checkout -b feature/status-banner
sed -i 's|<title>Table Reservation</title>|<title>Table Reservation - Status Banner</title>|' src/main/resources/static/index.html
git commit -am "feat(ui): add status banner title"

git checkout develop
git checkout -b feature/title-rename
sed -i 's|<title>Table Reservation</title>|<title>Reserve a Table</title>|' src/main/resources/static/index.html
git commit -am "feat(ui): rename page title"

git checkout develop
git merge --no-ff feature/status-banner -m "Merge feature/status-banner"
echo ">> The next merge will CONFLICT on index.html <title>. Resolve the markers, then git add + git commit."
git merge --no-ff feature/title-rename || true
git status
echo ">> After resolving:  git add src/main/resources/static/index.html && git commit"

# ---- Release baseline ----
echo ">> When develop is stable:  git tag -a v1.0.0 -m 'MVP release baseline' && git push origin v1.0.0"
