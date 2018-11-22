
pipeline {
    agent any
    
    stages {
        stage('Checkout Git repository') {
	        steps {
                git branch: 'master', credentialsId: 'git-credentials' , url: 'https://github.com/lvthillo/maven-hello-world'
            }
        }
    }

post {
always {
notifySlack currentBuild.result
}
}
        }
