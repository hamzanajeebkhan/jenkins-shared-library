
def call() {

pipeline {
    agent {
        docker { image "maven:3.8.4-openjdk-17" }
    }

    environment {
        DOCKER_HUB_REPO = "techiescamp/jenkins-java-app"
        IMAGE_TAG = "2.0.0"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(branches: [[name: 'main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/hamzanajeebkhan/kube-petclinc-app.git']])
            }
        }

        stage('Build with Maven') {
            steps {
                
                sh 'mvn -B -Dmaven.repo.local=/root/.m2/repository clean install -DskipTests'
                
            }
        }
        
        stage('Build and Push Docker Image') {
            steps {
                sh '''
                    curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-27.1.2.tgz;
                    tar xf docker-27.1.2.tgz;
                    mv docker/docker /usr/local/bin/;
                    docker --version;
                    pwd;
                    ls -ltr;
                    docker build . -t my-first-maven-app
                '''
            }
        }
        
    }
}

}