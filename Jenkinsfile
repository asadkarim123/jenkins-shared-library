
pipeline {
    agent any
    
    stages {
        stage('Checkout Git repository') {
	        steps {
                echo "hello world"
            }
        }
    }

post {
always {
notifySlack currentBuild.result
}
}
        }
