// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
// Tasks 8, 10 and 12: checkout -> build -> test (Selenium gate) -> package -> deploy.
// DEPLOY_MODE=tomcat deploys the WAR to a local Tomcat; DEPLOY_MODE=docker builds, pushes and runs a container.
pipeline {
    agent any

    parameters {
        choice(name: 'APP_ENV', choices: ['dev', 'staging', 'prod'], description: 'Environment setting passed to the app (parameterised config)')
        choice(name: 'DEPLOY_MODE', choices: ['tomcat', 'docker'], description: 'Deployment target')
        string(name: 'TOMCAT_HOME', defaultValue: '/opt/tomcat', description: 'Tomcat install directory (tomcat mode)')
        string(name: 'DOCKER_REPO', defaultValue: 'your-dockerhub-user/table-reservation', description: 'Image repository. Docker Hub: user/name. Local registry: localhost:5000/table-reservation')
        booleanParam(name: 'REGISTRY_LOGIN', defaultValue: true, description: 'Log in to the registry with credential dockerhub-creds (untick for an open local registry)')
        string(name: 'HOST_PORT', defaultValue: '8081', description: 'Host port for the container (docker mode)')
    }

    tools {
        maven 'Maven3'   // Manage Jenkins > Tools: add a Maven installation named "Maven3"
        jdk   'JDK17'    // Manage Jenkins > Tools: add a JDK installation named "JDK17"
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '15'))
        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'git log -1 --pretty=format:"%h %an %s"'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean compile'
            }
        }

        stage('Unit & API Tests') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        // Quality gate: if a Selenium test fails, this stage fails and no later stage (deploy) runs.
        stage('Selenium UI Tests') {
            steps {
                sh 'mvn -B verify'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/failsafe-reports/*.xml'
                    archiveArtifacts artifacts: 'target/screenshots/*.png', allowEmptyArchive: true
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B package -DskipTests'
                archiveArtifacts artifacts: 'target/reservation.war', fingerprint: true
            }
        }

        stage('Deploy to Tomcat') {
            when { expression { params.DEPLOY_MODE == 'tomcat' } }
            steps {
                sh '''
                    echo "export APP_ENV=${APP_ENV}" > "${TOMCAT_HOME}/bin/setenv.sh"
                    chmod +x "${TOMCAT_HOME}/bin/setenv.sh"
                    cp target/reservation.war "${TOMCAT_HOME}/webapps/reservation.war"
                    "${TOMCAT_HOME}/bin/shutdown.sh" || true
                    sleep 5
                    "${TOMCAT_HOME}/bin/startup.sh"
                '''
                sh '''
                    for i in $(seq 1 30); do
                      if curl -sf http://localhost:8080/reservation/api/health; then echo; exit 0; fi
                      sleep 2
                    done
                    echo "Deployment health check failed"; exit 1
                '''
            }
        }

        stage('Docker Build & Push') {
            when { expression { params.DEPLOY_MODE == 'docker' } }
            steps {
                sh 'docker build --build-arg VERSION=${IMAGE_TAG} -t ${DOCKER_REPO}:${IMAGE_TAG} -t ${DOCKER_REPO}:latest .'
                script {
                    if (params.REGISTRY_LOGIN) {
                        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                                          usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                            sh 'echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin'
                        }
                    }
                }
                sh '''
                    docker push ${DOCKER_REPO}:${IMAGE_TAG}
                    docker push ${DOCKER_REPO}:latest
                '''
            }
        }

        stage('Docker Deploy') {
            when { expression { params.DEPLOY_MODE == 'docker' } }
            steps {
                sh '''
                    docker rm -f table-reservation || true
                    docker run -d --name table-reservation --restart unless-stopped \
                      -p ${HOST_PORT}:8080 -e APP_ENV=${APP_ENV} ${DOCKER_REPO}:${IMAGE_TAG}
                    for i in $(seq 1 30); do
                      if curl -sf http://localhost:${HOST_PORT}/api/health; then echo; exit 0; fi
                      sleep 2
                    done
                    docker logs table-reservation
                    echo "Container health check failed"; exit 1
                '''
            }
        }
    }

    post {
        success { echo "Pipeline succeeded: build #${env.BUILD_NUMBER} (${params.DEPLOY_MODE}, ${params.APP_ENV})" }
        failure { echo 'Pipeline failed - deployment was skipped. Check the test reports and screenshots.' }
    }
}
