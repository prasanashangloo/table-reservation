# Requirements traceability matrix

> Owner / Author: **Prasana Shangloo (23102A0076)**

Every requirement sentence of the 15-task specification, where it is met in this repository, and what is left for you to run. "Needs your run" means the repository is ready but the result (log, screenshot, URL) can only come from your own machine or account.

| Task | Requirement | Where it is met | Status |
|---|---|---|---|
| 1 Problem Definition and Scope | Study the real-time need for the platform | docs/01-problem-and-scope.md | Written |
| 1 Problem Definition and Scope | Identify target users, existing pain points, stakeholders, constraints and measurable success criteria | docs/01-problem-and-scope.md | Written |
| 1 Problem Definition and Scope | Freeze a small 15-Task MVP scope | docs/01-problem-and-scope.md (Approved MVP scope) | Written; needs instructor approval |
| 2 Agile Planning and DevOps Workflow | Create user stories and acceptance criteria | docs/02-agile-and-devops-plan.md | Written |
| 2 Agile Planning and DevOps Workflow | Prepare a product backlog | docs/02-agile-and-devops-plan.md, docs/backlog.csv | Written |
| 2 Agile Planning and DevOps Workflow | 15-Task Kanban/Scrum plan | docs/task-board.md, docs/02-agile-and-devops-plan.md | Written |
| 2 Agile Planning and DevOps Workflow | Definition of Done | docs/02-agile-and-devops-plan.md | Written |
| 2 Agile Planning and DevOps Workflow | DevOps lifecycle diagram from development to operations | docs/diagrams/devops-lifecycle.png | Written |
| 3 Requirements, Architecture and Technology Setup | MVP covers registration, availability/slot view, booking request, confirmation, cancellation and status tracking | src/main (UserController, BookingController, BookingService, static/index.html) | Implemented; run mvn to confirm |
| 3 Requirements, Architecture and Technology Setup | Select programming stack, Maven/Gradle/Ant build tool, database if required | pom.xml (Java 17, Spring Boot, Maven, H2/MySQL) | Done |
| 3 Requirements, Architecture and Technology Setup | Tomcat/Nginx deployment target | WAR for Tomcat (docs/05-jenkins-setup.md) | Done |
| 3 Requirements, Architecture and Technology Setup | SRS summary, use-case, architecture, data model, API list | docs/03-requirements-and-architecture.md, docs/diagrams/ | Written |
| 3 Requirements, Architecture and Technology Setup | Working local setup | README.md Quick start | Needs your run: mvn clean verify |
| 4 Git and GitHub Repository Initialization | Create a GitHub repository | docs/04-git-workflow.md | Needs your GitHub account |
| 4 Git and GitHub Repository Initialization | Add README, .gitignore, folder structure | README.md, .gitignore | Done |
| 4 Git and GitHub Repository Initialization | Issue templates | /.github/ISSUE_TEMPLATE, pull_request_template.md | Done |
| 4 Git and GitHub Repository Initialization | Branch naming rules | CONTRIBUTING.md | Done |
| 4 Git and GitHub Repository Initialization | Commit the initial application skeleton with meaningful messages | scripts/init-repo.sh | Script ready; you run it |
| 5 Feature Development with Branching | Implement first core workflow on a feature branch | User registration and login, docs/04-git-workflow.md | Code ready; branch is yours to create |
| 5 Feature Development with Branching | Use add, commit, push, pull and logs | docs/04-git-workflow.md, scripts/git-demo.sh | Commands documented |
| 5 Feature Development with Branching | Raise a pull request, review changes, merge into development branch | docs/04-git-workflow.md | Needs GitHub |
| 6 MVP Completion and Git Collaboration | Implement remaining essential MVP functions | src/main/java/.../BookingService.java, static/index.html | Implemented |
| 6 MVP Completion and Git Collaboration | Second feature branch, conflict creation/resolution | scripts/git-demo.sh, docs/04-git-workflow.md | Script ready |
| 6 MVP Completion and Git Collaboration | Tagging and release-ready source baseline | docs/04-git-workflow.md (v1.0.0) | You run git tag |
| 6 MVP Completion and Git Collaboration | Updated backlog | docs/backlog.csv, docs/task-board.md | Done |
| 7 Jenkins Installation and Continuous Integration Job | Install and configure Jenkins | docs/05-jenkins-setup.md | Guide ready |
| 7 Jenkins Installation and Continuous Integration Job | Connect GitHub repository, Maven build job triggered by commit or scheduled polling | docs/05-jenkins-setup.md (Poll SCM H/5 * * * *) | Guide ready |
| 7 Jenkins Installation and Continuous Integration Job | Archive the build artefact | Jenkinsfile (archiveArtifacts target/reservation.war) | Done |
| 8 Pipeline as Code and Server Deployment | Jenkinsfile with checkout, build, package and deploy stages | Jenkinsfile | Done |
| 8 Pipeline as Code and Server Deployment | Deploy the build to Tomcat or Nginx | Jenkinsfile stage Deploy to Tomcat | Done |
| 8 Pipeline as Code and Server Deployment | Parameterize at least one environment setting | Jenkinsfile parameter APP_ENV -> setenv.sh -> app footer | Done |
| 9 Selenium Test Design and Local Execution | Identify three to five critical user journeys | docs/06-test-plan.md (J1-J5) | Done |
| 9 Selenium Test Design and Local Execution | Selenium WebDriver test cases with assertions, test data and screenshots for failures | src/test/.../selenium (ReservationJourneysIT, TestData, ScreenshotOnFailure, ScreenshotMechanismIT) | Written; run to confirm |
| 9 Selenium Test Design and Local Execution | Execute the suite locally through Maven | mvn verify | Needs your run |
| 10 Continuous Testing in Jenkins | Integrate Selenium suite into Jenkins; publish test reports | Jenkinsfile (junit failsafe-reports) | Done |
| 10 Continuous Testing in Jenkins | Failed tests stop deployment | Jenkinsfile (Selenium stage before Package/Deploy) | Done |
| 10 Continuous Testing in Jenkins | Correct a deliberately introduced defect and rerun | docs/05-jenkins-setup.md (Deliberate-defect exercise) | Procedure ready |
| 11 Docker Image and Container Lifecycle | Create a Dockerfile | Dockerfile | Done |
| 11 Docker Image and Container Lifecycle | Build and tag image, run container, map ports, inspect logs, stop/restart/remove | scripts/docker-lifecycle.sh, docs/07-docker.md | Script ready; saves docker-command-log.txt |
| 11 Docker Image and Container Lifecycle | Document the complete container lifecycle | docs/07-docker.md | Done |
| 12 Jenkins-Docker Continuous Deployment | Build a versioned Docker image | Jenkinsfile (tag = BUILD_NUMBER) | Done |
| 12 Jenkins-Docker Continuous Deployment | Publish to Docker Hub or a local registry | Jenkinsfile (DOCKER_REPO, REGISTRY_LOGIN) | Done |
| 12 Jenkins-Docker Continuous Deployment | Deploy a fresh container automatically after successful tests | Jenkinsfile stage Docker Deploy | Done |
| 13 Configuration Management Script | Identify prerequisites: packages, users, folders, files, ports and services | docs/08-configuration-management.md | Done |
| 13 Configuration Management Script | Puppet manifest/modules or Ansible inventory and YAML playbook | ansible/inventory.ini, ansible/provision.yml, puppet/reservation.pp | Done |
| 14 Automated Provisioning and Reliability Validation | Provision a clean target environment | Vagrantfile, ansible/site.yml | Ready |
| 14 Automated Provisioning and Reliability Validation | Deploy the application or container | ansible/deploy.yml | Done |
| 14 Automated Provisioning and Reliability Validation | Rerun to demonstrate idempotency | docs/08-configuration-management.md (changed=0) | Needs your run |
| 14 Automated Provisioning and Reliability Validation | Health check | ansible/deploy.yml (uri /api/health) | Done |
| 14 Automated Provisioning and Reliability Validation | Test rollback/recovery to the previous stable release | ansible/deploy.yml rescue, ansible/rollback.yml | Done |
| 15 Final End-to-End Release, Documentation and Viva | Run the complete workflow from Git commit to Jenkins, Selenium gate, Docker deployment and provisioning | docs/10-demo-video-script.md | Needs your run |
| 15 Final End-to-End Release, Documentation and Viva | Technical documentation, architecture, troubleshooting guide, limitations and future enhancement plan | docs/09-final-release-and-viva.md, Reservation_Platform_Report.docx | Done |
| 15 Final End-to-End Release, Documentation and Viva | Presentation | Reservation_Platform_Presentation.pptx | Done |
