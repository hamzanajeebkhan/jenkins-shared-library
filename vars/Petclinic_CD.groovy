def call() {
    pipeline {
    agent { label "maven"}

    parameters {
        string(name: 'IMAGE_NAME', defaultValue: 'default', description: 'Add the name of image to deploy')
        string(name: 'CONTAINER_NAME', defaultValue: 'default', description: 'Name of container to be deployed')
    }

    stages {
        stage('Pre-Requisite') {
            steps {
                sh '''
                    curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-27.1.2.tgz;
                    tar xf docker-27.1.2.tgz;
                    mv docker/docker /usr/local/bin/;
                    docker --version;
                    pwd;
                '''
            }
        }

        stage('Deployment') {
            steps {
                sh """
                    docker rm -f $params.CONTAINER_NAME 2> /dev/null;
                    docker run -d -p 80:8080 --name $params.CONTAINER_NAME $params.IMAGE_NAME
                """
            }
        }

        stage('Show Credentials') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'vagrant-ssh', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    echo "PASSWORD: $PASSWORD"
                    echo "USERNAME: $USERNAME"
                }
                }
            }
        }
    }
}
