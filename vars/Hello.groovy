
def call() {
pipeline {
    
    agent { label "testing-agent" }

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }

        stage('New Stage') {
            steps {
                echo 'Hello Stage 2'
                sh "date"
                sh "hostname"
            }
        }

        stage('Shared Library work') {
            steps {
                echo 'This is coming from Shared Library'
            }
        }
    }
}
}