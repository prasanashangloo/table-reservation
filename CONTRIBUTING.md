# Contributing and branch rules

Owner / Author: **Prasana Shangloo** (23102A0076)

## Branch naming rules
| Branch | Pattern | Created from | Merges into |
|---|---|---|---|
| Release | `main` | - | tagged releases only |
| Integration | `develop` | `main` | `main` via PR |
| Feature | `feature/<short-kebab-name>` e.g. `feature/user-registration` | `develop` | `develop` via PR |
| Bug fix | `bugfix/<short-kebab-name>` | `develop` | `develop` via PR |
| Hot fix | `hotfix/<short-kebab-name>` | `main` | `main` and `develop` |

## Commit messages (Conventional Commits)
`feat(scope): ...`, `fix: ...`, `test: ...`, `docs: ...`, `ci: ...`, `chore: ...` - imperative, present tense, under 72 characters.

## Pull requests
Use the PR template. A PR needs one review comment resolved and a green Jenkins build before merging into `develop`.

## Issues
Create issues from the templates in `.github/ISSUE_TEMPLATE` (user story, bug report).
