// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
// Tasks 8 and 10 - Pipeline as code (Windows agent):
// Checkout -> Build -> Unit & API Tests -> Selenium UI Tests (quality gate) -> Package -> Deploy to Tomcat.
// If a test fails, that stage fails and every later stage (Package, Deploy) is skipped.
pipeline {
    agent any

    parameters {
        choice(name: 'APP_ENV', choices: ['dev', 'staging', 'prod'], description: 'Environment setting passed to the deployed app (parameterised config)')
        string(name: 'TOMCAT_HOME', defaultValue: 'C:\\tomcat', description: 'Tomcat install directory')
        string(name: 'TOMCAT_PORT', defaultValue: '8082', description: 'Port Tomcat listens on (not 8080, Jenkins uses it)')
    }

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.12.1'
        MVN       = 'C:\\Program Files\\apache-maven-3.9.16\\bin\\mvn.cmd'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '15'))
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                bat 'git log -1 --oneline'
            }
        }

        stage('Build') {
            steps {
                bat '"%MVN%" -B clean compile'
            }
        }

        stage('Unit & API Tests') {
            steps {
                bat '"%MVN%" -B test'
            }
            post {
                always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml' }
            }
        }

        // Quality gate: if a Selenium journey fails, this stage fails and Package/Deploy never run.
        stage('Selenium UI Tests') {
            steps {
                bat '"%MVN%" -B verify -Dheadless=true'
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
                bat '"%MVN%" -B package -DskipTests'
                archiveArtifacts artifacts: 'target/reservation.war', fingerprint: true
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                bat 'if not exist "%TOMCAT_HOME%\\conf\\Catalina\\localhost" mkdir "%TOMCAT_HOME%\\conf\\Catalina\\localhost"'
                writeFile file: "${params.TOMCAT_HOME}\\conf\\Catalina\\localhost\\reservation.xml", text: """<?xml version="1.0" encoding="UTF-8"?>
<Context>
  <Parameter name="APP_ENV" value="${params.APP_ENV}" override="false"/>
</Context>
"""
                bat 'copy /Y target\\reservation.war "%TOMCAT_HOME%\\webapps\\reservation.war"'
                powershell '''
                    $url = "http://localhost:$($env:TOMCAT_PORT)/reservation/api/health"
                    for ($i = 0; $i -lt 40; $i++) {
                        try {
                            $r = Invoke-RestMethod -Uri $url -TimeoutSec 3
                            if ($r.environment -eq $env:APP_ENV) { Write-Output ("Health check OK: " + ($r | ConvertTo-Json -Compress)); exit 0 }
                        } catch { }
                        Start-Sleep -Seconds 3
                    }
                    Write-Error "Deployment health check failed"
                    exit 1
                '''
            }
        }
    }

    post {
        success { echo "Pipeline succeeded: build #${env.BUILD_NUMBER}, APP_ENV=${params.APP_ENV}, URL http://localhost:${params.TOMCAT_PORT}/reservation/" }
        failure { echo 'Pipeline failed - deployment was skipped. Check the Test Result page and the screenshots.' }
    }
}
