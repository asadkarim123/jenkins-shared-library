
/* import shared library */
@Library('jenkins-shared-library')_

pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

    parameters { 
        booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'Check if you want to skip tests') 
    }
    
    stages {
        stage('Checkout Git repository') {
	        steps {
                git branch: 'master', credentialsId: 'git-credentials' , url: 'https://github.com/asadkarim123/jenkins-shared-library'
            }

	}
	    }
}

    post {
        always {
	    /* Use slackNotifier.groovy from shared library and provide current build result as parameter */   
            notifySlack(currentBuild.Result)
            cleanWs()
        }
    }
