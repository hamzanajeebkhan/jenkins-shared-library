
def call() {

pipeline {
    agent { label "maven"}

    environment {
        DOCKER_HUB_REPO = "techiescamp/jenkins-java-app"
        IMAGE_TAG = "2.0.0"
    }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch of Petclinic Repository')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(branches: [[name: "$params.BRANCH_NAME"]], extensions: [], userRemoteConfigs: [[url: 'https://github.com/hamzanajeebkhan/kube-petclinc-app.git']])
            }
        }

        stage('Build with Maven') {
            steps {
                
                sh 'mvn -B -Dmaven.repo.local=/root/.m2/repository clean install -DskipTests'
                
            }
        }
        
        stage('Build and Push Docker Image') {
        when { expression { params.BRANCH_NAME == 'main'} }
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

        stage('Downstream CD pipeline') {
         when { expression { params.BRANCH_NAME == 'main'} }
            steps {
                build job: 'Petclinic-CD', parameters: [string(name: 'IMAGE_NAME', value: 'my-first-maven-app'), string(name: 'CONTAINER_NAME', value: 'petclinic')]
            }
        }
        
    }
}

}