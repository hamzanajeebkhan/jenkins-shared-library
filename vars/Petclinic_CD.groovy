def call() {
    pipeline {
    agent { label "maven"}

    parameters {
        stringParam('IMAGE_NAME', 'default', 'Add the name of image to deploy')
        stringParam('CONTAINER_NAME', 'default', 'Name of container to be deployed')
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
                    docker rm -f $param.CONTAINER_NAME 2> /dev/null;
                    docker run -d -p 80:8080 --name $param.CONTAINER_NAME $param.IMAGE_NAME
                """
            }
        }
        
    }
}
}