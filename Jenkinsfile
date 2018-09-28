#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw clean"
    }

    stage('backend tests') {
        try {
            sh "./mvnw test"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/surefire-reports/TEST-*.xml'
        }
    }

    stage('quality analysis') {
        withSonarQubeEnv('Sonar') {
            sh "./mvnw sonar:sonar"
        }
    }

    stage('install') {
        sh "./mvnw install -DskipTests"
    }

}
