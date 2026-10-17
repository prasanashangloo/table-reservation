# Tasks 7, 8 and 10 - Jenkins CI and Pipeline as Code

> Owner / Author: **Prasana Shangloo** (23102A0076) | Copyright (c) 2026

## Task 7 - Install Jenkins and create a CI job
```bash
# Ubuntu/Debian (check https://www.jenkins.io/doc/book/installing/linux/ for the current repo key steps)
sudo apt update && sudo apt install -y openjdk-17-jdk git maven
# add the Jenkins apt repository and key per the official page, then:
sudo apt install -y jenkins
sudo systemctl enable --now jenkins
sudo cat /var/lib/jenkins/secrets/initialAdminPassword      # unlock at http://localhost:8080
```
Jenkins also uses port 8080 by default - if Tomcat runs on the same machine, change one of them (e.g. Jenkins `--httpPort=9090` or Tomcat `server.xml`).

1. Install suggested plugins (Git, Pipeline, JUnit, Credentials Binding).
2. *Manage Jenkins -> Tools*: add JDK named `JDK17` and Maven named `Maven3`.
3. New Item -> **Freestyle** `reservation-build`:
   - Source: Git, your repo URL, branch `*/develop`.
   - Trigger: *Poll SCM* `H/5 * * * *` (or a GitHub webhook).
   - Build step: `mvn -B clean package -DskipTests`.
   - Post-build: *Archive the artifacts* `target/*.war`.
4. Make a commit, wait for the poll, and capture: build log (SUCCESS), the trigger line "Started by an SCM change", archived `reservation.war`.

## Task 8 - Pipeline as code
The `Jenkinsfile` in the repository root has the stages: **Checkout -> Build -> Unit & API Tests -> Selenium UI Tests -> Package -> Deploy to Tomcat** (docker stages are used in Task 12).

Setup: create a *Pipeline* job -> *Pipeline script from SCM* -> your repo -> script path `Jenkinsfile`.

Tomcat target:
```bash
sudo apt install -y tomcat10      # or unpack Apache Tomcat 10.1 into /opt/tomcat
sudo chown -R jenkins: /opt/tomcat   # so the job can copy the WAR and run startup/shutdown scripts
```
Run **Build with Parameters** and choose `APP_ENV=staging`, `DEPLOY_MODE=tomcat`. The pipeline writes `setenv.sh` (the parameterised environment setting), copies the WAR, restarts Tomcat and calls `http://localhost:8080/reservation/api/health`. The page footer shows `Environment: staging`, which is your configuration evidence.

## Task 10 - Continuous testing as a quality gate
- `junit` publishes `target/failsafe-reports/*.xml` -> **Test Result** trend in Jenkins.
- Screenshots of failures are archived from `target/screenshots/`.
- Because *Selenium UI Tests* runs before *Package* and *Deploy*, a red test marks the build FAILED and later stages are skipped.

### Deliberate-defect exercise
1. In `BookingService.cancel`, comment out `b.setStatus(BookingStatus.CANCELLED);`.
2. Commit: `git commit -am "test: introduce cancel defect (demo)"` and push.
3. Jenkins: *Selenium UI Tests* fails (journey J5 and the API lifecycle test), deploy is skipped. Capture the failed stage view, Test Result page and `cancelJourney.png`.
4. Restore the line and commit: `git commit -am "fix: restore cancel status update"`.
5. Jenkins reruns green and deploys. Capture the successful run.
