/* import shared library */
@Library('jenkins-shared-library')_
    
node {
   echo 'Hello World'
}

    post {
        always {
	    /* Use slackNotifier.groovy from shared library and provide current build result as parameter */   
            notifySlack(currentBuild.Result)
            cleanWs()
        }
    }
