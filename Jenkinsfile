pipeline {
    agent any

    stages {
        stage('Check tools') {
            steps {
                sh 'git --version'
                sh 'java -version || echo "no java"'
                sh 'docker --version || echo "no docker"'
                sh 'ls services/order-service'
            }
        }
    }
}