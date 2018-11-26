@Library('jenkins-shared-library')_
pipeline {
    agent any
    stages {
        stage('Example') {
            steps {
                echo 'Hello World'
            }
        }
    }
    post { 
        always { 
            notifySlack(text, channel, attachments)
        }
    }
}
