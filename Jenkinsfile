pipeline {
    agent any

    stages {

    post { 
        always { 
            echo 'I will always say Hello again!'
            slackSend currentBuild.result
            echo 'Slack notified'
        }
    }
}
}
