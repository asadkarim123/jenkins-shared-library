pipeline {
    agent any
//def label = "python_agent"
//def label = "test"

    parameters {
        string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')

        text(name: 'BIOGRAPHY', defaultValue: '', description: 'Enter some information about the person')

        booleanParam(name: 'TOGGLE', defaultValue: true, description: 'Toggle this value')

        choice(name: 'CHOICE', choices: ['One', 'Two', 'Three'], description: 'Pick something')

        password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'Enter a password')

        file(name: "FILE", description: "Choose a file to upload")
    }


    stages {

        stage('Source') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh "sleep 1s;"
                sh "whoami;pwd;ls -al;"
            }
        }

        stage('Check Styles') {
            steps {
                sh "sleep 1s;"
            }
        }

        stage('Test') {
            steps {
                sh "sleep 1s;"
            }
        }

        stage('Documentation') {
            steps {
                sh "sleep 1s;"
            }
        }

        stage('Deploy') {
            steps {
                sh "sleep 1s;"
            }
        }
    }
    post { 
        always { 
            echo 'I will always say Hello again!'
            slackSend currentBuild.result
            echo 'Slack notified'
        }
    }
}
