
def call() {
pipeline {
    
    agent { label "app-1" }

    stages {
        stage('New Stage') {
            steps {
                echo 'Hello Stage 2'
                sh "date"
                sh "hostname"
            }
        }

        stage('Shared Library work from branch') {
            steps {
                echo 'This is coming from Shared Library from branch'
            }
        }
    }
}
}