# Tasks 4-6 - Git and GitHub Workflow

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

## Task 4 - Repository initialisation
Fastest way: `export GIT_AUTHOR_EMAIL=<your GitHub e-mail>` then `bash scripts/init-repo.sh` - it creates the repository with seven meaningful commits. The manual version is below. Branch rules are also in `CONTRIBUTING.md`.

```bash
cd table-reservation
git init -b main
git add .gitignore README.md pom.xml
git commit -m "chore: add gitignore, README and Maven build"
git add src Dockerfile Jenkinsfile
git commit -m "feat: add Spring Boot application skeleton"
git add docs ansible scripts .github
git commit -m "docs: add project docs, Ansible playbooks and GitHub templates"
# create an empty repo on GitHub named table-reservation, then:
git remote add origin https://github.com/<you>/table-reservation.git
git push -u origin main
```
Create the first issues (one per user story US1-US8) using the issue templates in `.github/ISSUE_TEMPLATE`.

### Branch policy
| Branch | Purpose | Rules |
|---|---|---|
| `main` | Released, tagged code only | Protected; merge from `develop` via PR |
| `develop` | Integration branch | Protected; PR + 1 review + green Jenkins build |
| `feature/<short-name>` | New work, e.g. `feature/user-registration` | Branch from `develop` |
| `bugfix/<short-name>` | Defects | Branch from `develop` |
| `hotfix/<short-name>` | Urgent production fix | Branch from `main`, merge to `main` and `develop` |

Commit messages follow Conventional Commits: `feat(scope): ...`, `fix: ...`, `docs: ...`, `test: ...`, `chore: ...`, `ci: ...`.

## Task 5 - Feature development with branching
```bash
git checkout -b develop && git push -u origin develop
git checkout -b feature/user-registration
# ...implement / adjust the registration workflow...
git add -A
git commit -m "feat(auth): user registration with BCrypt hashing"
git push -u origin feature/user-registration
git pull origin develop            # keep the branch current
git log --oneline --graph --decorate
```
On GitHub: open a PR `feature/user-registration` -> `develop`, request a review, leave at least two review comments (e.g. "add duplicate-email test", "validate password length"), address them with new commits, then merge (use *Create a merge commit*). Evidence: PR URL, review comments, merge commit in `git log`.

## Task 6 - MVP completion, conflict and tag
1. Remaining features (availability, booking, confirm, cancel, status) are developed on `feature/booking-flow` (second feature branch) and merged by PR.
2. **Create a conflict deliberately** - `scripts/git-demo.sh` has two branches editing the same `<title>` line in `index.html`. Merging the second produces:
```
<<<<<<< HEAD
<title>Table Reservation - Status Banner</title>
=======
<title>Reserve a Table</title>
>>>>>>> feature/title-rename
```
3. Resolve by editing the file to the final single line, then:
```bash
git add src/main/resources/static/index.html
git commit -m "merge: resolve title conflict between status-banner and title-rename"
```
4. Release baseline:
```bash
git checkout main && git merge --no-ff develop
git tag -a v1.0.0 -m "MVP release baseline"
git push origin main --tags
```
5. Update the backlog: mark US1-US8 Done on the project board.

Evidence: screenshot of conflict markers, resolved commit, `git tag` output, updated board.
