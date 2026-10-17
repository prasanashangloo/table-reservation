# Task 9 - Selenium Test Design and Local Execution

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

## Test plan
| Item | Detail |
|---|---|
| Scope | Critical UI journeys of the MVP |
| Tools | Selenium WebDriver 4.21, JUnit 5, headless Chrome (Selenium Manager fetches chromedriver), Maven Failsafe |
| Environment | App started on a random port by `@SpringBootTest(RANDOM_PORT)`; H2 in-memory database |
| Test data | Unique e-mail per test (`selenium-<uuid>@test.com`), password `secret123`, first free slot for tomorrow |
| Entry criteria | `mvn package` succeeds; Chrome installed |
| Exit criteria | All 5 journeys pass |

## Journeys and assertions
| ID | Journey | Key assertions |
|---|---|---|
| J1 | Register and log in | Message "Registration successful"; header shows "Selenium User" |
| J2 | Invalid login | Message "Invalid email or password" with error styling |
| J3 | View availability | >= 5 table rows (party of 2); slot buttons present |
| J4 | Book then confirm | New row status `PENDING`, then `CONFIRMED` |
| J5 | Cancel booking | Status `CANCELLED`; cancel button removed |

Plus 5 API tests in `ReservationApiTest` (health, duplicate registration, auth required, full lifecycle with double-booking 409, past-date 400).

## Failure screenshot mechanism
To see it work on demand: `mvn verify -Dsimulate.failure=true` runs `ScreenshotMechanismIT`, which fails deliberately and writes `target/screenshots/deliberateFailureProducesScreenshot.png`. Test data lives in `TestData.java`.

`ScreenshotOnFailure` (a JUnit 5 `TestExecutionExceptionHandler`) saves `target/screenshots/<testMethod>.png` when any test throws, then rethrows so the test still fails. Jenkins archives these PNGs.

## Local execution
```bash
mvn test       # API tests (surefire)
mvn verify     # API tests + Selenium journeys (failsafe), reports in target/failsafe-reports
# see the browser while testing:
mvn verify -Dheadless=false
# custom Chrome path:
mvn verify -Dchrome.bin=/usr/bin/chromium
```
Report: `target/failsafe-reports/*.txt|*.xml` (optionally generate HTML with `mvn surefire-report:failsafe-report-only site -DgenerateReports=false`).
