def call () {
    pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: maven
                    image: maven:3.8.4-openjdk-17
                    command:
                    - cat
                    tty: true
                  - name: kaniko
                    image: gcr.io/kaniko-project/executor:debug
                    command:
                    - /busybox/cat
                    tty: true
            '''
        }
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
                container('maven') {
                    sh 'mvn -B -Dmaven.repo.local=/root/.m2/repository clean install -DskipTests'
                }
            }
        }
    }
}
}